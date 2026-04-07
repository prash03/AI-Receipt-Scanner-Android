package com.nik.expensescanner.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController

@Composable
fun PermissionScreen(navController: NavController) {
    val context = LocalContext.current
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
            if (granted) {
                navController.navigate("home") {
                    popUpTo("permission") { inclusive = true }
                }
            }
        }
    )

    LaunchedEffect(hasCameraPermission) {
        if (hasCameraPermission) {
            navController.navigate("home") {
                popUpTo("permission") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Scan receipts easily with camera", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
                launcher.launch(Manifest.permission.CAMERA)
            }) {
                Text(text = "Allow Camera Access")
            }
        }
    }
}
