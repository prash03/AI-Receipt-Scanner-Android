
package com.nik.expensescanner.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun HomeScreen(nav: NavController) {
    Button(onClick = { nav.navigate("camera") }) {
        Text("Scan Receipt")
    }
}
