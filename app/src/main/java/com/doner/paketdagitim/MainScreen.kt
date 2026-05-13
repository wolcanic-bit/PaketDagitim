package com.doner.paketdagitim

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val nokta1 by viewModel.nokta1.collectAsState()
    val nokta2 by viewModel.nokta2.collectAsState()
    val nokta3 by viewModel.nokta3.collectAsState()
    val nokta1Isim by viewModel.nokta1Isim.collectAsState()
    val nokta2Isim by viewModel.nokta2Isim.collectAsState()
    val nokta3Isim by viewModel.nokta3Isim.collectAsState()
    val mesaj by viewModel.mesaj.collectAsState()
    val raporlar by viewModel.tumRaporlar.collectAsState(initial = emptyList())
    
    var isimDialog by remember { mutableStateOf(false) }
    val paraFormat = remember { NumberFormat.getNumberInstance(Locale("tr", "TR")) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Paket Dagitim Takip") },
                actions = {
                    IconButton(onClick = { isimDialog = true }) {
                        Icon(Icons.Filled.Settings, "Nokta Isimleri")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            mesaj?.let {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text(
                        text = it,
                        modifier = Modifier.padding(12.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            Text(
                text = viewModel.bugununTarihi,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            NoktaKarti(isim = nokta1Isim, adet = nokta1, onArttir = { viewModel.nokta1Arttir() }, onAzalt = { viewModel.nokta1Azalt() }, color = Color(0xFF2196F3))
            Spacer(modifier = Modifier.height(12.dp))
            NoktaKarti(isim = nokta2Isim, adet = nokta2, onArttir = { viewModel.nokta2Arttir() }, onAzalt = { viewModel.nokta2Azalt() }, color = Color(0xFF4CAF50))
            Spacer(modifier = Modifier.height(12.dp))
            NoktaKarti(isim = nokta3Isim, adet = nokta3, onArttir = { viewModel.nokta3Arttir() }, onAzalt = { viewModel.nokta3Azalt() }, color = Color(0xFFFF9800))
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "GUN SONU RAPORU", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Toplam Paket", fontSize = 14.sp)
                            Text(text = "${viewModel.toplamPaket}", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Toplam Kazanc", fontSize = 14.sp)
                            Text(text = "${paraFormat.format(viewModel.toplamKazanc)} TL", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Birim Fiyat: 80 TL", fontSize = 13.sp, color = Color.Gray)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { viewModel.raporuKaydet() }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))) {
                    Text("Raporu Kaydet")
                }
                OutlinedButton(onClick = { viewModel.sifirla() }, modifier = Modifier.weight(1f)) {
                    Text("Sifirla")
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            if (raporlar.isNotEmpty()) {
                Text(text = "KAYITLI RAPORLAR", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))
                raporlar.forEach { rapor ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
                        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text(text = rapor.tarih, fontWeight = FontWeight.Bold)
                                Text(text = "${rapor.toplamPaket} paket - ${paraFormat.format(rapor.toplamKazanc)} TL", fontSize = 14.sp, color = Color.Gray)
                            }
                            IconButton(onClick = { viewModel.raporuSil(rapor) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Sil", tint = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
    
    if (isimDialog) {
        var n1 by remember { mutableStateOf(nokta1Isim) }
        var n2 by remember { mutableStateOf(nokta2Isim) }
        var n3 by remember { mutableStateOf(nokta3Isim) }
        
        AlertDialog(
            onDismissRequest = { isimDialog = false },
            title = { Text("Nokta Isimlerini Duzenle") },
            text = {
                Column {
                    OutlinedTextField(value = n1, onValueChange = { n1 = it }, label = { Text("1. Nokta") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = n2, onValueChange = { n2 = it }, label = { Text("2. Nokta") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = n3, onValueChange = { n3 = it }, label = { Text("3. Nokta") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = { TextButton(onClick = { viewModel.noktaIsimleriniGuncelle(n1, n2, n3); isimDialog = false }) { Text("Kaydet") } },
            dismissButton = { TextButton(onClick = { isimDialog = false }) { Text("Iptal") } }
        )
    }
}

@Composable
fun NoktaKarti(isim: String, adet: Int, onArttir: () -> Unit, onAzalt: () -> Unit, color: Color) {
    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(text = "$isim", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = "$adet paket", fontSize = 14.sp, color = Color.Gray)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = onAzalt) { Text("-", color = Color.Red, fontSize = 24.sp) }
                Text(text = "$adet", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = color)
                TextButton(onClick = onArttir) { Text("+", color = color, fontSize = 24.sp) }
            }
        }
    }
}
