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
    
    val n1Normal by viewModel.n1Normal.collectAsState()
    val n1Eko by viewModel.n1Eko.collectAsState()
    val n2Normal by viewModel.n2Normal.collectAsState()
    val n2Eko by viewModel.n2Eko.collectAsState()
    val n3Normal by viewModel.n3Normal.collectAsState()
    val n3Eko by viewModel.n3Eko.collectAsState()
    
    val n1Isim by viewModel.n1Isim.collectAsState()
    val n2Isim by viewModel.n2Isim.collectAsState()
    val n3Isim by viewModel.n3Isim.collectAsState()
    
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
                        Icon(Icons.Filled.Settings, contentDescription = "Ayarlar")
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
                    Text(
                        text = it,
                        modifier = Modifier.padding(12.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            // Tarih
            Text(
                text = viewModel.bugununTarihi,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            // Nokta Secimi
            Text(
                text = "DONER SECIMI",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = seciliNokta == 1,
                    onClick = { viewModel.noktaSec(1) },
                    label = { Text(text = n1Isim) },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = seciliNokta == 2,
                    onClick = { viewModel.noktaSec(2) },
                    label = { Text(text = n2Isim) },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = seciliNokta == 3,
                    onClick = { viewModel.noktaSec(3) },
                    label = { Text(text = n3Isim) },
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
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "YENI SIPARIS: ${viewModel.seciliNoktaIsim}",
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
                        Text(
                            text = "Normal (${normalFiyat} TL)",
                            fontSize = 16.sp,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = { viewModel.girisNormalAzalt() }) {
                            Text(text = "-", fontSize = 24.sp, color = Color.Red)
                        }
                        Text(
                            text = "$girisNormal",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2196F3)
                        )
                        TextButton(onClick = { viewModel.girisNormalArttir() }) {
                            Text(text = "+", fontSize = 24.sp, color = Color(0xFF2196F3))
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Ekonomik Paket
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Ekonomik (${ekonomikFiyat} TL)",
                            fontSize = 16.sp,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = { viewModel.girisEkonomikAzalt() }) {
                            Text(text = "-", fontSize = 24.sp, color = Color.Red)
                        }
                        Text(
                            text = "$girisEkonomik",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF9800)
                        )
                        TextButton(onClick = { viewModel.girisEkonomikArttir() }) {
                            Text(text = "+", fontSize = 24.sp, color = Color(0xFFFF9800))
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
                            Text(text = "ONAYLA", fontSize = 16.sp)
                        }
                        OutlinedButton(
                            onClick = { viewModel.sonIslemiGeriAl() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "GERI AL", fontSize = 14.sp)
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Nokta Bazli Alt Toplam
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "NOKTA DETAYLARI",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Nokta 1
                    Text(
                        text = "$n1Isim: ${n1Normal}N + ${n1Eko}E = ${(n1Normal * normalFiyat) + (n1Eko * ekonomikFiyat)} TL",
                        fontSize = 15.sp
                    )
                    // Nokta 2
                    Text(
                        text = "$n2Isim: ${n2Normal}N + ${n2Eko}E = ${(n2Normal * normalFiyat) + (n2Eko * ekonomikFiyat)} TL",
                        fontSize = 15.sp
                    )
                    // Nokta 3
                    Text(
                        text = "$n3Isim: ${n3Normal}N + ${n3Eko}E = ${(n3Normal * normalFiyat) + (n3Eko * ekonomikFiyat)} TL",
                        fontSize = 15.sp
                    )
                    
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    Text(
                        text = "TOPLAM: ${viewModel.toplamPaket} paket - ${paraFormat.format(viewModel.toplamKazanc)} TL",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF2E7D32)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Rapor Butonlari
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { viewModel.raporuKaydet() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text(text = "Rapor Kaydet")
                }
                
                OutlinedButton(
                    onClick = { viewModel.gunuSifirla() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                ) {
                    Text(text = "Sifirla")
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Gecmis Raporlar
            if (raporlar.isNotEmpty()) {
                Text(
                    text = "KAYITLI RAPORLAR",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
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
                                Text(
                                    text = rapor.tarih,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${rapor.toplamPaket} paket",
                                        fontSize = 14.sp,
                                        color = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "${paraFormat.format(rapor.toplamKazanc)} TL",
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF2E7D32)
                                    )
                                    IconButton(
                                        onClick = { viewModel.raporuSil(rapor) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            Icons.Filled.Delete,
                                            contentDescription = "Sil",
                                            tint = Color.Red,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                            Text(text = "${rapor.nokta1Isim}: ${rapor.nokta1Normal}N + ${rapor.nokta1Ekonomik}E = ${rapor.nokta1Tutar} TL", fontSize = 14.sp)
                            Text(text = "${rapor.nokta2Isim}: ${rapor.nokta2Normal}N + ${rapor.nokta2Ekonomik}E = ${rapor.nokta2Tutar} TL", fontSize = 14.sp)
                            Text(text = "${rapor.nokta3Isim}: ${rapor.nokta3Normal}N + ${rapor.nokta3Ekonomik}E = ${rapor.nokta3Tutar} TL", fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
    
    // Isim Degistirme Dialog
    if (isimDialog) {
        var n1 by remember { mutableStateOf(n1Isim) }
        var n2 by remember { mutableStateOf(n2Isim) }
        var n3 by remember { mutableStateOf(n3Isim) }
        
        AlertDialog(
            onDismissRequest = { isimDialog = false },
            title = { Text(text = "Nokta Isimleri") },
            text = {
                Column {
                    OutlinedTextField(value = n1, onValueChange = { n1 = it }, label = { Text(text = "1. Nokta") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = n2, onValueChange = { n2 = it }, label = { Text(text = "2. Nokta") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = n3, onValueChange = { n3 = it }, label = { Text(text = "3. Nokta") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.noktaIsimleriniGuncelle(n1, n2, n3)
                    isimDialog = false
                }) { Text(text = "Kaydet") }
            },
            dismissButton = {
                TextButton(onClick = { isimDialog = false }) { Text(text = "Iptal") }
            }
        )
    }
    
    // Fiyat Degistirme Dialog
    if (fiyatDialog) {
        var nf by remember { mutableStateOf(normalFiyat.toString()) }
        var ef by remember { mutableStateOf(ekonomikFiyat.toString()) }
        
        AlertDialog(
            onDismissRequest = { fiyatDialog = false },
            title = { Text(text = "Paket Fiyatlari") },
            text = {
                Column {
                    OutlinedTextField(value = nf, onValueChange = { nf = it }, label = { Text(text = "Normal Paket (TL)") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = ef, onValueChange = { ef = it }, label = { Text(text = "Ekonomik Paket (TL)") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val n = nf.toIntOrNull() ?: normalFiyat
                    val e = ef.toIntOrNull() ?: ekonomikFiyat
                    viewModel.fiyatlariGuncelle(n, e)
                    fiyatDialog = false
                }) { Text(text = "Kaydet") }
            },
            dismissButton = {
                TextButton(onClick = { fiyatDialog = false }) { Text(text = "Iptal") }
            }
        )
    }
}
