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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate


@Composable
fun StatisticsScreen(navController: NavController, modifier: Modifier = Modifier) {
    val pieData = mapOf(
        "Food" to 150f,
        "Transport" to 80f,
        "Entertainment" to 100f,
        "Utilities" to 70f
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
            pieData = pieData,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))


    }
}


@Composable
fun PieChartComposable(
    pieData: Map<String, Float>, // Key = label, Value = amount
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val pieChart = PieChart(context)

            val entries = pieData.map { PieEntry(it.value, it.key) }
            val dataSet = PieDataSet(entries, "")
            dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList() // Or your own color list
            dataSet.sliceSpace = 3f
            dataSet.selectionShift = 5f

            val data = PieData(dataSet)
            data.setValueTextSize(14f)
            data.setValueTextColor(android.graphics.Color.WHITE)

            pieChart.data = data
            pieChart.description.isEnabled = false
            pieChart.isDrawHoleEnabled = true
            pieChart.setHoleColor(android.graphics.Color.TRANSPARENT)
            pieChart.setEntryLabelColor(android.graphics.Color.BLACK)
            pieChart.animateY(1000)

            pieChart.invalidate() // Refresh
            pieChart
        }
    )
}
