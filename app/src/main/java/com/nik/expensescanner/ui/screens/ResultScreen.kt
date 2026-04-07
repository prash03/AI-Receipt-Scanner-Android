package com.nik.expensescanner.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nik.expensescanner.data.Expense
import com.nik.expensescanner.ui.viewmodel.ExpenseViewModel
import com.nik.expensescanner.util.ReceiptParser
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(navController: NavController, rawText: String, viewModel: ExpenseViewModel = viewModel()) {
    val parsed = remember { ReceiptParser.parse(rawText) }
    
    var merchant by remember { mutableStateOf(parsed.merchant) }
    var amount by remember { mutableStateOf(parsed.amount.toString()) }
    var date by remember { mutableStateOf(if (parsed.date.isEmpty()) SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()) else parsed.date) }
    var category by remember { mutableStateOf("General") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Verify Details") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = merchant,
                onValueChange = { merchant = it },
                label = { Text("Merchant Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Total Amount (₹)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("Date (dd/mm/yyyy)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = {
                    val expense = Expense(
                        merchant = merchant,
                        amount = amount.toDoubleOrNull() ?: 0.0,
                        date = date,
                        category = category
                    )
                    viewModel.insert(expense)
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Expense")
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Scan Again")
            }
        }
    }
}
