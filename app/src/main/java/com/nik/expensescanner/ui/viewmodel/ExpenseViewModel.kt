package com.nik.expensescanner.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nik.expensescanner.data.AppDatabase
import com.nik.expensescanner.data.Expense
import com.nik.expensescanner.data.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ExpenseRepository
    val allExpenses: Flow<List<Expense>>

    init {
        val expenseDao = AppDatabase.getDatabase(application).expenseDao()
        repository = ExpenseRepository(expenseDao)
        allExpenses = repository.allExpenses
    }

    fun insert(expense: Expense) = viewModelScope.launch {
        repository.insert(expense)
    }

    fun delete(expense: Expense) = viewModelScope.launch {
        repository.delete(expense)
    }
}
