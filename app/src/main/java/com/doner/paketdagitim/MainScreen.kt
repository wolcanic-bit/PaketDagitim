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
    val girisNormal by viewModel.girisNormal.collectAsState()
    val girisEkonomik by viewModel.girisEkonomik.collectAsState()
    val seciliNokta by viewModel.seciliNokta.collectAsState()
    val toplamNormal by viewModel.toplamNormal.collectAsState()
    val toplamEkonomik by viewModel.toplamEkonomik.collectAsState()
    
    val nokta1Isim by viewModel.nokta1Isim.collectAsState()
    val nokta2Isim by viewModel.nokta2Isim.collectAsState()
    val nokta3Isim by viewModel.nokta3Isim.collectAsState()
    
    val normalFiyat by viewModel.normalFiyat.collectAsState()
    val ekonomikFiyat by viewModel.ekonomikFiyat.collectAsState()
    
    val mesaj by viewModel.mesaj.collectAsState()
    val raporlar by viewModel.tumRaporlar.collectAsState(initial = emptyList())
    
    var isimDialog by remember { mutableStateOf(false) }
    var fiyatDialog by remember { mutableStateOf(false) }
    val paraFormat = remember { NumberFormat.getNumberInstance(Locale("tr", "TR")) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Paket Dagitim") },
                actions = {
                    TextButton(onClick = { fiyatDialog = true }) { Text("💰", fontSize = 20.sp) }
                    IconButton(onClick = { isimDialog = true }) {
                        Icon(Icons.Filled.Settings, "Ayarlar")
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
            // Mesaj
            mesaj?.let {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text(it, modifier = Modifier.padding(12.dp), color = Color.White, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            // Tarih
            Text(viewModel.bugununTarihi, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            
            // Nokta Secimi
            Text("📍 DÖNER SECIMI", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = seciliNokta == 1,
                    onClick = { viewModel.noktaSec(1) },
                    label = { Text(nokta1Isim) },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = seciliNokta == 2,
                    onClick = { viewModel.noktaSec(2) },
                    label = { Text(nokta2Isim) },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = seciliNokta == 3,
                    onClick = { viewModel.noktaSec(3) },
                    label = { Text(nokta3Isim) },
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Siparis Girisi
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "📝 YENI SIPARIS: ${viewModel.seciliNoktaIsim}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Normal Paket
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Normal (${normalFiyat}₺)", fontSize = 16.sp, modifier = Modifier.weight(1f))
                        TextButton(onClick = { viewModel.girisNormalAzalt() }) {
                            Text("➖", fontSize = 24.sp, color = Color.Red)
                        }
                        Text("$girisNormal", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2196F3))
                        TextButton(onClick = { viewModel.girisNormalArttir() }) {
                            Text("➕", fontSize = 24.sp, color = Color(0xFF2196F3))
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Ekonomik Paket
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Ekonomik (${ekonomikFiyat}₺)", fontSize = 16.sp, modifier = Modifier.weight(1f))
                        TextButton(onClick = { viewModel.girisEkonomikAzalt() }) {
                            Text("➖", fontSize = 24.sp, color = Color.Red)
                        }
                        Text("$girisEkonomik", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF9800))
                        TextButton(onClick = { viewModel.girisEkonomikArttir() }) {
                            Text("➕", fontSize = 24.sp, color = Color(0xFFFF9800))
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Onayla ve Geri Al
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { viewModel.siparisiOnayla() },
                            modifier = Modifier.weight(2f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                        ) {
                            Text("✅ ONAYLA", fontSize = 16.sp)
                        }
                        OutlinedButton(
                            onClick = { viewModel.sonIslemiGeriAl() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("↩️", fontSize = 20.sp)
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Alt Toplam
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📊 ALT TOPLAM", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Normal", fontSize = 13.sp, color = Color.Gray)
                            Text("$toplamNormal adet", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("${paraFormat.format(toplamNormal * normalFiyat)} ₺", fontSize = 14.sp, color = Color(0xFF2196F3))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Ekonomik", fontSize = 13.sp, color = Color.Gray)
                            Text("$toplamEkonomik adet", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("${paraFormat.format(toplamEkonomik * ekonomikFiyat)} ₺", fontSize = 14.sp, color = Color(0xFFFF9800))
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text("Toplam: ${viewModel.toplamPaket} paket", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(
                        "${paraFormat.format(viewModel.toplamKazanc)} ₺",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Rapor Butonlari
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { viewModel.raporuKaydet() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) { Text("💾 Rapor Kaydet") }
                
                OutlinedButton(
                    onClick = { viewModel.gunuSifirla() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                ) { Text("🔄 Sifirla") }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
                       // Gecmis Raporlar
            if (raporlar.isNotEmpty()) {
                Text("📋 KAYITLI RAPORLAR", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))
                raporlar.forEach { rapor ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("📅 ${rapor.tarih}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("${rapor.toplamPaket} paket", fontSize = 14.sp, color = Color.Gray)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("${paraFormat.format(rapor.toplamKazanc)} ₺", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                                    IconButton(onClick = { viewModel.raporuSil(rapor) }, modifier = Modifier.size(32.dp)) {
                                        Icon(Icons.Filled.Delete, "Sil", tint = Color.Red, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                            Divider(modifier = Modifier.padding(vertical = 4.dp))
                            // Nokta 1
                            RaporSatiri("🥙 ${rapor.nokta1Isim}", rapor.nokta1Normal, rapor.nokta1Ekonomik, rapor.nokta1Tutar)
                            // Nokta 2
                            RaporSatiri("🥙 ${rapor.nokta2Isim}", rapor.nokta2Normal, rapor.nokta2Ekonomik, rapor.nokta2Tutar)
                            // Nokta 3
                            RaporSatiri("🥙 ${rapor.nokta3Isim}", rapor.nokta3Normal, rapor.nokta3Ekonomik, rapor.nokta3Tutar)
                        }
                    }
                }
            }
        }
    }
    
    // Isim Degistirme
    if (isimDialog) {
        var n1 by remember { mutableStateOf(nokta1Isim) }
        var n2 by remember { mutableStateOf(nokta2Isim) }
        var n3 by remember { mutableStateOf(nokta3Isim) }
        
        AlertDialog(
            onDismissRequest = { isimDialog = false },
            title = { Text("Nokta Isimleri") },
            text = {
                Column {
                    OutlinedTextField(n1, { n1 = it }, label = { Text("1. Nokta") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(n2, { n2 = it }, label = { Text("2. Nokta") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(n3, { n3 = it }, label = { Text("3. Nokta") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = { TextButton(onClick = { viewModel.noktaIsimleriniGuncelle(n1, n2, n3); isimDialog = false }) { Text("Kaydet") } },
            dismissButton = { TextButton(onClick = { isimDialog = false }) { Text("Iptal") } }
        )
    }
    
    // Fiyat Degistirme
    if (fiyatDialog) {
        var nf by remember { mutableStateOf(normalFiyat.toString()) }
        var ef by remember { mutableStateOf(ekonomikFiyat.toString()) }
        
        AlertDialog(
            onDismissRequest = { fiyatDialog = false },
            title = { Text("Paket Fiyatlari") },
            text = {
                Column {
                    OutlinedTextField(nf, { nf = it }, label = { Text("Normal Paket (TL)") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(ef, { ef = it }, label = { Text("Ekonomik Paket (TL)") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val n = nf.toIntOrNull() ?: normalFiyat
                    val e = ef.toIntOrNull() ?: ekonomikFiyat
                    viewModel.fiyatlariGuncelle(n, e)
                    fiyatDialog = false
                }) { Text("Kaydet") }
            },
            dismissButton = { TextButton(onClick = { fiyatDialog = false }) { Text("Iptal") } }
        )
    }
}
