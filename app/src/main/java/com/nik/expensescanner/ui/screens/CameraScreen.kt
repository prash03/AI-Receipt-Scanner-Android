package com.nik.expensescanner.ui.screens

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun CameraScreen(navController: NavController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraExecutor: ExecutorService = remember { Executors.newSingleThreadExecutor() }
    
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    var isProcessing by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isProcessing) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
                Text("Analyzing receipt...", modifier = Modifier.padding(top = 16.dp))
            }
        } else {
            AndroidView(
                factory = { context ->
                    val previewView = PreviewView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }
                        imageCapture = ImageCapture.Builder().build()
                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview,
                                imageCapture
                            )
                        } catch (e: Exception) {
                            Log.e("CameraScreen", "Binding failed", e)
                        }
                    }, ContextCompat.getMainExecutor(context))
                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )

            Button(
                onClick = {
                    isProcessing = true
                    captureAndProcessImage(context, imageCapture!!, cameraExecutor) { text ->
                        isProcessing = false
                        navController.navigate("result?text=${text}")
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
                    .size(80.dp)
            ) {
                Text("Capture")
            }
        }
    }
}

private fun captureAndProcessImage(
    context: Context,
    imageCapture: ImageCapture,
    executor: ExecutorService,
    onResult: (String) -> Unit
) {
    imageCapture.takePicture(executor, object : ImageCapture.OnImageCapturedCallback() {
        override fun onCaptureSuccess(image: ImageProxy) {
            val mediaImage = image.image
            if (mediaImage != null) {
                val inputImage = InputImage.fromMediaImage(mediaImage, image.imageInfo.rotationDegrees)
                val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                
                recognizer.process(inputImage)
                    .addOnSuccessListener { visionText ->
                        onResult(visionText.text)
                        image.close()
                    }
                    .addOnFailureListener {
                        onResult("")
                        image.close()
                    }
            } else {
                image.close()
            }
        }

        override fun onError(exception: ImageCaptureException) {
            Log.e("CameraScreen", "Capture failed", exception)
            onResult("")
        }
    })
}
