package com.doner.paketdagitim

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).raporDao()
    
    private val _nokta1 = MutableStateFlow(0)
    val nokta1: StateFlow<Int> = _nokta1.asStateFlow()
    
    private val _nokta2 = MutableStateFlow(0)
    val nokta2: StateFlow<Int> = _nokta2.asStateFlow()
    
    private val _nokta3 = MutableStateFlow(0)
    val nokta3: StateFlow<Int> = _nokta3.asStateFlow()
    
    private val _nokta1Isim = MutableStateFlow("Hatay Döner")
    val nokta1Isim: StateFlow<String> = _nokta1Isim.asStateFlow()
    
    private val _nokta2Isim = MutableStateFlow("Öncü Döner")
    val nokta2Isim: StateFlow<String> = _nokta2Isim.asStateFlow()
    
    private val _nokta3Isim = MutableStateFlow("Reis Döner")
    val nokta3Isim: StateFlow<String> = _nokta3Isim.asStateFlow()
    
    val tumRaporlar = dao.tumRaporlariGetir()
    
    private val _mesaj = MutableStateFlow<String?>(null)
    val mesaj: StateFlow<String?> = _mesaj.asStateFlow()
    
    val toplamPaket: Int
        get() = _nokta1.value + _nokta2.value + _nokta3.value
    
    val toplamKazanc: Int
        get() = toplamPaket * 80
    
    val bugununTarihi: String
        get() {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return sdf.format(Date())
        }
    
    fun nokta1Arttir() { _nokta1.value++ }
    fun nokta1Azalt() { if (_nokta1.value > 0) _nokta1.value-- }
    fun nokta2Arttir() { _nokta2.value++ }
    fun nokta2Azalt() { if (_nokta2.value > 0) _nokta2.value-- }
    fun nokta3Arttir() { _nokta3.value++ }
    fun nokta3Azalt() { if (_nokta3.value > 0) _nokta3.value-- }
    
    fun noktaIsimleriniGuncelle(n1: String, n2: String, n3: String) {
        _nokta1Isim.value = n1
        _nokta2Isim.value = n2
        _nokta3Isim.value = n3
    }
    
    fun sifirla() {
        _nokta1.value = 0
        _nokta2.value = 0
        _nokta3.value = 0
    }
    
    fun raporuKaydet() {
        viewModelScope.launch {
            val rapor = GunlukRapor(
                tarih = bugununTarihi,
                nokta1Paket = _nokta1.value,
                nokta2Paket = _nokta2.value,
                nokta3Paket = _nokta3.value,
                toplamPaket = toplamPaket,
                toplamKazanc = toplamKazanc
            )
            dao.raporKaydet(rapor)
            _mesaj.value = "✅ Rapor kaydedildi! Toplam: $toplamPaket paket, $toplamKazanc TL"
        }
    }
    
    fun mesajiTemizle() {
        _mesaj.value = null
    }
    
    fun raporuSil(rapor: GunlukRapor) {
        viewModelScope.launch {
            dao.raporSil(rapor)
            _mesaj.value = "🗑️ Rapor silindi."
        }
    }
}
