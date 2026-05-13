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
    val nokta1Normal by viewModel.nokta1Normal.collectAsState()
    val nokta1Ekonomik by viewModel.nokta1Ekonomik.collectAsState()
    val nokta2Normal by viewModel.nokta2Normal.collectAsState()
    val nokta2Ekonomik by viewModel.nokta2Ekonomik.collectAsState()
    val nokta3Normal by viewModel.nokta3Normal.collectAsState()
    val nokta3Ekonomik by viewModel.nokta3Ekonomik.collectAsState()
    
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
                title = { Text("Paket Dagitim Takip") },
                actions = {
                    IconButton(onClick = { fiyatDialog = true }) {
                        Text("💰", fontSize = 20.sp)
                    }
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
            
            // Tarih ve fiyat bilgisi
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(viewModel.bugununTarihi, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
                Text("Normal: ${normalFiyat}₺ | Eko: ${ekonomikFiyat}₺", fontSize = 13.sp, color = Color.Gray)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Nokta 1
            NoktaKarti(
                isim = nokta1Isim,
                normalAdet = nokta1Normal,
                ekonomikAdet = nokta1Ekonomik,
                normalFiyat = normalFiyat,
                ekonomikFiyat = ekonomikFiyat,
                onNormalArttir = { viewModel.nokta1NormalArttir() },
                onNormalAzalt = { viewModel.nokta1NormalAzalt() },
                onEkonomikArttir = { viewModel.nokta1EkonomikArttir() },
                onEkonomikAzalt = { viewModel.nokta1EkonomikAzalt() },
                color = Color(0xFF2196F3)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Nokta 2
            NoktaKarti(
                isim = nokta2Isim,
                normalAdet = nokta2Normal,
                ekonomikAdet = nokta2Ekonomik,
                normalFiyat = normalFiyat,
                ekonomikFiyat = ekonomikFiyat,
                onNormalArttir = { viewModel.nokta2NormalArttir() },
                onNormalAzalt = { viewModel.nokta2NormalAzalt() },
                onEkonomikArttir = { viewModel.nokta2EkonomikArttir() },
                onEkonomikAzalt = { viewModel.nokta2EkonomikAzalt() },
                color = Color(0xFF4CAF50)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Nokta 3
            NoktaKarti(
                isim = nokta3Isim,
                normalAdet = nokta3Normal,
                ekonomikAdet = nokta3Ekonomik,
                normalFiyat = normalFiyat,
                ekonomikFiyat = ekonomikFiyat,
                onNormalArttir = { viewModel.nokta3NormalArttir() },
                onNormalAzalt = { viewModel.nokta3NormalAzalt() },
                onEkonomikArttir = { viewModel.nokta3EkonomikArttir() },
                onEkonomikAzalt = { viewModel.nokta3EkonomikAzalt() },
                color = Color(0xFFFF9800)
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Gün Sonu Raporu
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("GUN SONU RAPORU", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Normal", fontSize = 13.sp, color = Color.Gray)
                            Text("${viewModel.toplamNormal} adet", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("${paraFormat.format(viewModel.toplamNormal * normalFiyat)} ₺", fontSize = 14.sp, color = Color(0xFF2196F3))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Ekonomik", fontSize = 13.sp, color = Color.Gray)
                            Text("${viewModel.toplamEkonomik} adet", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("${paraFormat.format(viewModel.toplamEkonomik * ekonomikFiyat)} ₺", fontSize = 14.sp, color = Color(0xFFFF9800))
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Toplam Paket", fontSize = 14.sp)
                            Text("${viewModel.toplamPaket}", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Toplam Kazanc", fontSize = 14.sp)
                            Text("${paraFormat.format(viewModel.toplamKazanc)} ₺", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Butonlar
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { viewModel.raporuKaydet() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) { Text("Raporu Kaydet") }
                
                OutlinedButton(onClick = { viewModel.sifirla() }, modifier = Modifier.weight(1f)) {
                    Text("Sifirla")
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Gecmis Raporlar
            if (raporlar.isNotEmpty()) {
                Text("KAYITLI RAPORLAR", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))
                raporlar.forEach { rapor ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(rapor.tarih, fontWeight = FontWeight.Bold)
                                Text(
                                    "${rapor.toplamPaket} paket - ${paraFormat.format(rapor.toplamKazanc)} ₺",
                                    fontSize = 14.sp, color = Color.Gray
                                )
                            }
                            IconButton(onClick = { viewModel.raporuSil(rapor) }) {
                                Icon(Icons.Filled.Delete, "Sil", tint = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
    
    // Isim Degistirme Dialogu
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
    
    // Fiyat Degistirme Dialogu
    if (fiyatDialog) {
        var nf by remember { mutableStateOf(normalFiyat.toString()) }
        var ef by remember { mutableStateOf(ekonomikFiyat.toString()) }
        
        AlertDialog(
            onDismissRequest = { fiyatDialog = false },
            title = { Text("Paket Fiyatlari") },
            text = {
                Column {
                    OutlinedTextField(nf, { nf = it }, label = { Text("Normal Paket Fiyati (TL)") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(ef, { ef = it }, label = { Text("Ekonomik Paket Fiyati (TL)") }, modifier = Modifier.fillMaxWidth())
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

@Composable
fun NoktaKarti(
    isim: String,
    normalAdet: Int,
    ekonomikAdet: Int,
    normalFiyat: Int,
    ekonomikFiyat: Int,
    onNormalArttir: () -> Unit,
    onNormalAzalt: () -> Unit,
    onEkonomikArttir: () -> Unit,
    onEkonomikAzalt: () -> Unit,
    color: Color
) {
    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("📍 $isim", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            
            // Normal Paket
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Normal (${normalFiyat}₺)", fontSize = 14.sp, modifier = Modifier.weight(1f))
                TextButton(onClick = onNormalAzalt) { Text("-", color = Color.Red, fontSize = 20.sp) }
                Text("$normalAdet", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = color)
                TextButton(onClick = onNormalArttir) { Text("+", color = color, fontSize = 20.sp) }
            }
            
            // Ekonomik Paket
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Ekonomik (${ekonomikFiyat}₺)", fontSize = 14.sp, modifier = Modifier.weight(1f))
                TextButton(onClick = onEkonomikAzalt) { Text("-", color = Color.Red, fontSize = 20.sp) }
                Text("$ekonomikAdet", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF9800))
                TextButton(onClick = onEkonomikArttir) { Text("+", color = Color(0xFFFF9800), fontSize = 20.sp) }
            }
            
            // Nokta toplami
            val noktaToplam = normalAdet + ekonomikAdet
            val noktaKazanc = (normalAdet * normalFiyat) + (ekonomikAdet * ekonomikFiyat)
            Text(
                "Toplam: $noktaToplam paket - ${noktaKazanc} ₺",
                fontSize = 13.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
