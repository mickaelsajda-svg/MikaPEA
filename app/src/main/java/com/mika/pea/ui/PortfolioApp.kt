package com.mika.pea.ui

@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mika.pea.PortfolioViewModel
import com.mika.pea.data.db.entity.Holding



@Composable
fun PortfolioApp(vm: PortfolioViewModel) {
    var showAdd by remember { mutableStateOf(false) }
    var showSettings by remember { mutableStateOf(false) }

    val state by vm.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mika PEA") },
                actions = {
                    TextButton(onClick = { showSettings = true }) { Text("Paramètres") }
                    TextButton(onClick = { vm.refreshAll() }) { Text("Rafraîchir") }
                    TextButton(onClick = { showAdd = true }) { Text("Ajouter") }
                }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding)) {
            if (state.items.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Ajoutez une ligne (ex: AAPL, 10 actions)")
                }
            } else {
                LazyColumn(Modifier.fillMaxSize()) {
                    items(state.items, key = { it.holding.id }) { ui ->
                        HoldingRow(
                            holding = ui.holding,
                            lastPrice = ui.lastPrice,
                            onRefresh = { vm.refreshPrice(ui.holding.symbol) },
                            onDelete = { vm.delete(ui.holding) }
                        )
                    }
                }
            }
        }
    }

    if (showAdd) AddHoldingDialog(
        onDismiss = { showAdd = false },
        onConfirm = { s, sh, ap, cur ->
            vm.add(s, sh, ap, cur)
            showAdd = false
        }
    )

    if (showSettings) SettingsDialog(
        apiKey = state.apiKey,
        onDismiss = { showSettings = false },
        onSave = { vm.setApiKey(it); showSettings = false }
    )
}

@Composable
fun HoldingRow(holding: Holding, lastPrice: Double?, onRefresh: () -> Unit, onDelete: () -> Unit) {
    Card(Modifier.padding(12.dp).fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(holding.symbol, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(holding.currency)
            }
            Spacer(Modifier.height(8.dp))
            Text("Quantité: ${holding.shares}")
            Text("PRU: ${holding.avgPrice}")
            Text("Cours actuel: ${lastPrice?.let { String.format("%.2f", it) } ?: "—"}")
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onRefresh) { Text("Rafraîchir le cours") }
                OutlinedButton(onClick = onDelete) { Text("Supprimer") }
            }
        }
    }
}

@Composable
fun AddHoldingDialog(onDismiss: () -> Unit, onConfirm: (String, Double, Double, String) -> Unit) {
    var symbol by remember { mutableStateOf("") }
    var shares by remember { mutableStateOf("") }
    var avg by remember { mutableStateOf("") }
    var currency by remember { mutableStateOf("EUR") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ajouter une ligne") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = symbol, onValueChange = { symbol = it }, label = { Text("Symbole (ex: AAPL)") })
                OutlinedTextField(value = shares, onValueChange = { shares = it }, label = { Text("Quantité") })
                OutlinedTextField(value = avg, onValueChange = { avg = it }, label = { Text("PRU") })
                OutlinedTextField(value = currency, onValueChange = { currency = it }, label = { Text("Devise") })
            }
        },
        confirmButton = {
            Button(onClick = {
                val sh = shares.toDoubleOrNull() ?: 0.0
                val ap = avg.toDoubleOrNull() ?: 0.0
                if (symbol.isNotBlank() && sh > 0) onConfirm(symbol, sh, ap, currency)
            }) { Text("Ajouter") }
        },
        dismissButton = { OutlinedButton(onClick = onDismiss) { Text("Annuler") } }
    )
}

@Composable
fun SettingsDialog(apiKey: String, onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var key by remember { mutableStateOf(apiKey) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Paramètres") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Pour récupérer automatiquement les cours, ajoutez une clé API Alpha Vantage (gratuite). " +
                    "Sinon, l'application fonctionne sans mise à jour des prix."
                )
                OutlinedTextField(value = key, onValueChange = { key = it }, label = { Text("Clé API Alpha Vantage") })
            }
        },
        confirmButton = { Button(onClick = { onSave(key) }) { Text("Enregistrer") } },
        dismissButton = { OutlinedButton(onClick = onDismiss) { Text("Annuler") } }
    )
}
