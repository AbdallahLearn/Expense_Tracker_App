package com.example.expense_tracking_project.screens.dataVisualization.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.expense_tracking_project.screens.authentication.presentation.component.SelectEditingTab
import com.example.expense_tracking_project.screens.authentication.presentation.component.SelectTransaction
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate


@Composable
fun StatisticsScreen(navController: NavController, modifier: Modifier = Modifier) {
    val selectedTab = remember { mutableStateOf("Expenses") }

    // Sample Data
    val expenseData = mapOf(
        "Food" to 150f,
        "Transport" to 80f,
        "Entertainment" to 100f,
        "Utilities" to 70f
    )

    val incomeData = mapOf(
        "Salary" to 500f,
        "Freelance" to 200f,
        "Investments" to 150f
    )

    SelectTransaction(
        showTabs = true,
        tabOptions = listOf("Expenses", "Income"),
        onTabSelected = { selectedTab.value = it }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Statistics",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        PieChartComposable(
            pieData = if (selectedTab.value == "Expenses") expenseData else incomeData,
            type = selectedTab.value,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}


@Composable
fun PieChartComposable(
    pieData: Map<String, Float>,
    type: String,
    modifier: Modifier = Modifier
) {
    // Remember the PieChart reference
    val context = LocalContext.current
    val pieChartRef = remember { PieChart(context) }

    // Update the chart whenever type or data changes
    LaunchedEffect(pieData, type) {
        val entries = pieData.map { PieEntry(it.value, it.key) }
        val dataSet = PieDataSet(entries, "")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f

        val data = PieData(dataSet)

        data.setValueFormatter(object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return if (type == "Expenses") "-${value.toInt()}" else "+${value.toInt()}"
            }
        })

        dataSet.valueTextColor = if (type == "Expenses") {
            android.graphics.Color.RED
        } else {
            android.graphics.Color.GREEN
        }

        data.setValueTextSize(14f)

        pieChartRef.data = data
        pieChartRef.description.isEnabled = false
        pieChartRef.isDrawHoleEnabled = true
        pieChartRef.setHoleColor(android.graphics.Color.TRANSPARENT)
        pieChartRef.setEntryLabelColor(android.graphics.Color.BLACK)
        pieChartRef.animateY(1000)

        pieChartRef.invalidate()
    }

    // Display the chart
    AndroidView(
        modifier = modifier,
        factory = { pieChartRef }
    )
}
