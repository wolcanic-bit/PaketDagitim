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
    
    // Nokta 1
    private val _nokta1Normal = MutableStateFlow(0)
    val nokta1Normal: StateFlow<Int> = _nokta1Normal.asStateFlow()
    private val _nokta1Ekonomik = MutableStateFlow(0)
    val nokta1Ekonomik: StateFlow<Int> = _nokta1Ekonomik.asStateFlow()
    
    // Nokta 2
    private val _nokta2Normal = MutableStateFlow(0)
    val nokta2Normal: StateFlow<Int> = _nokta2Normal.asStateFlow()
    private val _nokta2Ekonomik = MutableStateFlow(0)
    val nokta2Ekonomik: StateFlow<Int> = _nokta2Ekonomik.asStateFlow()
    
    // Nokta 3
    private val _nokta3Normal = MutableStateFlow(0)
    val nokta3Normal: StateFlow<Int> = _nokta3Normal.asStateFlow()
    private val _nokta3Ekonomik = MutableStateFlow(0)
    val nokta3Ekonomik: StateFlow<Int> = _nokta3Ekonomik.asStateFlow()
    
    // Isimler
    private val _nokta1Isim = MutableStateFlow("Hatay Döner")
    val nokta1Isim: StateFlow<String> = _nokta1Isim.asStateFlow()
    private val _nokta2Isim = MutableStateFlow("Öncü Döner")
    val nokta2Isim: StateFlow<String> = _nokta2Isim.asStateFlow()
    private val _nokta3Isim = MutableStateFlow("Reis Döner")
    val nokta3Isim: StateFlow<String> = _nokta3Isim.asStateFlow()
    
    // Fiyatlar
    private val _normalFiyat = MutableStateFlow(80)
    val normalFiyat: StateFlow<Int> = _normalFiyat.asStateFlow()
    private val _ekonomikFiyat = MutableStateFlow(25)
    val ekonomikFiyat: StateFlow<Int> = _ekonomikFiyat.asStateFlow()
    
    val tumRaporlar = dao.tumRaporlariGetir()
    
    private val _mesaj = MutableStateFlow<String?>(null)
    val mesaj: StateFlow<String?> = _mesaj.asStateFlow()
    
    val toplamNormal: Int get() = _nokta1Normal.value + _nokta2Normal.value + _nokta3Normal.value
    val toplamEkonomik: Int get() = _nokta1Ekonomik.value + _nokta2Ekonomik.value + _nokta3Ekonomik.value
    val toplamPaket: Int get() = toplamNormal + toplamEkonomik
    val toplamKazanc: Int get() = (toplamNormal * _normalFiyat.value) + (toplamEkonomik * _ekonomikFiyat.value)
    
    val bugununTarihi: String
        get() {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return sdf.format(Date())
        }
    
    // Nokta 1 arttir/azalt
    fun nokta1NormalArttir() { _nokta1Normal.value++ }
    fun nokta1NormalAzalt() { if (_nokta1Normal.value > 0) _nokta1Normal.value-- }
    fun nokta1EkonomikArttir() { _nokta1Ekonomik.value++ }
    fun nokta1EkonomikAzalt() { if (_nokta1Ekonomik.value > 0) _nokta1Ekonomik.value-- }
    
    // Nokta 2
    fun nokta2NormalArttir() { _nokta2Normal.value++ }
    fun nokta2NormalAzalt() { if (_nokta2Normal.value > 0) _nokta2Normal.value-- }
    fun nokta2EkonomikArttir() { _nokta2Ekonomik.value++ }
    fun nokta2EkonomikAzalt() { if (_nokta2Ekonomik.value > 0) _nokta2Ekonomik.value-- }
    
    // Nokta 3
    fun nokta3NormalArttir() { _nokta3Normal.value++ }
    fun nokta3NormalAzalt() { if (_nokta3Normal.value > 0) _nokta3Normal.value-- }
    fun nokta3EkonomikArttir() { _nokta3Ekonomik.value++ }
    fun nokta3EkonomikAzalt() { if (_nokta3Ekonomik.value > 0) _nokta3Ekonomik.value-- }
    
    fun noktaIsimleriniGuncelle(n1: String, n2: String, n3: String) {
        _nokta1Isim.value = n1
        _nokta2Isim.value = n2
        _nokta3Isim.value = n3
    }
    
    fun fiyatlariGuncelle(normal: Int, ekonomik: Int) {
        _normalFiyat.value = normal
        _ekonomikFiyat.value = ekonomik
    }
    
    fun sifirla() {
        _nokta1Normal.value = 0; _nokta1Ekonomik.value = 0
        _nokta2Normal.value = 0; _nokta2Ekonomik.value = 0
        _nokta3Normal.value = 0; _nokta3Ekonomik.value = 0
    }
    
    fun raporuKaydet() {
        viewModelScope.launch {
            val rapor = GunlukRapor(
                tarih = bugununTarihi,
                nokta1Normal = _nokta1Normal.value,
                nokta1Ekonomik = _nokta1Ekonomik.value,
                nokta2Normal = _nokta2Normal.value,
                nokta2Ekonomik = _nokta2Ekonomik.value,
                nokta3Normal = _nokta3Normal.value,
                nokta3Ekonomik = _nokta3Ekonomik.value,
                toplamPaket = toplamPaket,
                toplamKazanc = toplamKazanc,
                normalFiyat = _normalFiyat.value,
                ekonomikFiyat = _ekonomikFiyat.value
            )
            dao.raporKaydet(rapor)
            _mesaj.value = "✅ Rapor kaydedildi! Toplam: $toplamPaket paket, $toplamKazanc TL"
        }
    }
    
    fun mesajiTemizle() { _mesaj.value = null }
    
    fun raporuSil(rapor: GunlukRapor) {
        viewModelScope.launch {
            dao.raporSil(rapor)
            _mesaj.value = "🗑️ Rapor silindi."
        }
    }
}
