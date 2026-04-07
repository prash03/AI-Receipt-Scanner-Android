package com.nik.expensescanner.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nik.expensescanner.ui.viewmodel.ExpenseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController, viewModel: ExpenseViewModel = viewModel()) {
    val expenses by viewModel.allExpenses.collectAsState(initial = emptyList())
    
    val categoryTotals = expenses.groupBy { it.category }
        .mapValues { entry -> entry.value.sumOf { it.amount } }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Dashboard") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "Category Breakdown", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))
            
            categoryTotals.forEach { (category, total) ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = category, fontSize = 16.sp)
                    Text(text = "₹$total", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                }
                Divider()
            }
        }
    }
}
