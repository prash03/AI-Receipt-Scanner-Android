
package com.nik.expensescanner.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun ResultScreen(nav: NavController, text: String) {
    Column {
        Text("Extracted Data:")
        Text(text)
        Button(onClick = { nav.navigate("home") }) {
            Text("Back")
        }
    }
}
