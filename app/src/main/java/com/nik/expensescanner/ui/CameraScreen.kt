
package com.nik.expensescanner.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun CameraScreen(nav: NavController) {
    Button(onClick = { nav.navigate("result/Amount: 500 INR") }) {
        Text("Capture Receipt (Mock)")
    }
}
