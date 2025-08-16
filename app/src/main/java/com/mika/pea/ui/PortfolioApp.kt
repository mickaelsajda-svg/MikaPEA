package com.mika.pea.ui

@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mika.pea.PortfolioViewModel

@Composable
fun PortfolioApp(vm: PortfolioViewModel) {
    val state by vm.state.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mika PEA") }) }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Lignes en portefeuille : ${state.items.size}")
                Button(onClick = { vm.refreshAll() }) { Text("Rafraîchir") }
            }
        }
    }
}
