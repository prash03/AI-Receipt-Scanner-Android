package com.nik.expensescanner.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nik.expensescanner.ui.viewmodel.ExpenseViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: ExpenseViewModel = viewModel()) {
    val expenses by viewModel.allExpenses.collectAsState(initial = emptyList())
    
    val totalExpenses = expenses.sumOf { it.amount }
    val today = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    val todaySpending = expenses.filter { it.date == today }.sumOf { it.amount }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Expense Tracker") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Summary", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Total Expenses: ₹$totalExpenses", fontSize = 18.sp)
                    Text(text = "Today's Spending: ₹$todaySpending", fontSize = 18.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = { navController.navigate("camera") },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Scan Receipt")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { navController.navigate("dashboard") },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("View Dashboard")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("history") },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Expense History")
            }
        }
    }
}
