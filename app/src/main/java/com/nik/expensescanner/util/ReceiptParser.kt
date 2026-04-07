package com.nik.expensescanner.util

import java.util.regex.Pattern

data class ParsedReceipt(
    val merchant: String = "Unknown Merchant",
    val amount: Double = 0.0,
    val date: String = ""
)

object ReceiptParser {
    fun parse(text: String): ParsedReceipt {
        val lines = text.split("\n")
        var merchant = "Unknown Merchant"
        var amount = 0.0
        var date = ""

        // Try to find merchant (usually first line)
        if (lines.isNotEmpty()) {
            merchant = lines[0].trim()
        }

        // Regex for amount (looking for Total, Amount, etc.)
        val amountPattern = Pattern.compile("(Total|Amount|Sum|Net|Payable)[:\\s]*[₹$]?\\s*(\\d+[.,]\\d{2})", Pattern.CASE_INSENSITIVE)
        val matcher = amountPattern.matcher(text)
        if (matcher.find()) {
            amount = matcher.group(2)?.replace(",", ".")?.toDoubleOrNull() ?: 0.0
        } else {
            // Fallback: look for the largest number with two decimal places
            val fallbackAmountPattern = Pattern.compile("(\\d+[.,]\\d{2})")
            val fallbackMatcher = fallbackAmountPattern.matcher(text)
            val amounts = mutableListOf<Double>()
            while (fallbackMatcher.find()) {
                fallbackMatcher.group(1)?.replace(",", ".")?.toDoubleOrNull()?.let { amounts.add(it) }
            }
            if (amounts.isNotEmpty()) {
                amount = amounts.maxOrNull() ?: 0.0
            }
        }

        // Regex for date (dd/mm/yyyy or yyyy-mm-dd)
        val datePattern = Pattern.compile("(\\d{1,2}[/-]\\d{1,2}[/-]\\d{2,4})")
        val dateMatcher = datePattern.matcher(text)
        if (dateMatcher.find()) {
            date = dateMatcher.group(1) ?: ""
        }

        return ParsedReceipt(merchant, amount, date)
    }
}
