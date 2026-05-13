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

data class SonIslem(
    val noktaIsim: String,
    val normal: Int,
    val ekonomik: Int
)

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).raporDao()
    
    // Gecici giris (siparis ekrani - tek seferlik)
    private val _girisNormal = MutableStateFlow(0)
    val girisNormal: StateFlow<Int> = _girisNormal.asStateFlow()
    private val _girisEkonomik = MutableStateFlow(0)
    val girisEkonomik: StateFlow<Int> = _girisEkonomik.asStateFlow()
    
    // Secili nokta
    private val _seciliNokta = MutableStateFlow(1)
    val seciliNokta: StateFlow<Int> = _seciliNokta.asStateFlow()
    
    // Alt toplamlar (onaylanmis)
    private val _toplamNormal = MutableStateFlow(0)
    val toplamNormal: StateFlow<Int> = _toplamNormal.asStateFlow()
    private val _toplamEkonomik = MutableStateFlow(0)
    val toplamEkonomik: StateFlow<Int> = _toplamEkonomik.asStateFlow()
    
    // Son islem (geri almak icin)
    private var sonIslem: SonIslem? = null
    
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
    
    val toplamPaket: Int get() = _toplamNormal.value + _toplamEkonomik.value
    val toplamKazanc: Int get() = (_toplamNormal.value * _normalFiyat.value) + (_toplamEkonomik.value * _ekonomikFiyat.value)
    
    val seciliNoktaIsim: String get() = when(_seciliNokta.value) {
        1 -> _nokta1Isim.value
        2 -> _nokta2Isim.value
        3 -> _nokta3Isim.value
        else -> ""
    }
    
    val bugununTarihi: String
        get() {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return sdf.format(Date())
        }
    
    fun noktaSec(no: Int) { _seciliNokta.value = no }
    
    fun girisNormalArttir() { _girisNormal.value++ }
    fun girisNormalAzalt() { if (_girisNormal.value > 0) _girisNormal.value-- }
    fun girisEkonomikArttir() { _girisEkonomik.value++ }
    fun girisEkonomikAzalt() { if (_girisEkonomik.value > 0) _girisEkonomik.value-- }
    
    fun siparisiOnayla() {
        val n = _girisNormal.value
        val e = _girisEkonomik.value
        if (n == 0 && e == 0) {
            _mesaj.value = "⚠️ Lütfen paket adedi girin!"
            return
        }
        
        // Son islemi kaydet
        sonIslem = SonIslem(seciliNoktaIsim, n, e)
        
        // Alt toplama ekle
        _toplamNormal.value += n
        _toplamEkonomik.value += e
        
        _mesaj.value = "✅ $seciliNoktaIsim: +$n normal, +$e ekonomik eklendi"
        
        // Girisi sifirla
        _girisNormal.value = 0
        _girisEkonomik.value = 0
    }
    
    fun sonIslemiGeriAl() {
        val islem = sonIslem
        if (islem != null) {
            _toplamNormal.value -= islem.normal
            _toplamEkonomik.value -= islem.ekonomik
            _mesaj.value = "↩️ Geri alındı: ${islem.noktaIsim} -${islem.normal} normal, -${islem.ekonomik} ekonomik"
            sonIslem = null
        } else {
            _mesaj.value = "⚠️ Geri alınacak işlem yok!"
        }
    }
    
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
        _girisNormal.value = 0
        _girisEkonomik.value = 0
    }
    
    fun gunuSifirla() {
        _toplamNormal.value = 0
        _toplamEkonomik.value = 0
        _girisNormal.value = 0
        _girisEkonomik.value = 0
        sonIslem = null
    }
    
    fun raporuKaydet() {
        viewModelScope.launch {
            val rapor = GunlukRapor(
                tarih = bugununTarihi,
                nokta1Normal = 0, nokta1Ekonomik = 0,
                nokta2Normal = 0, nokta2Ekonomik = 0,
                nokta3Normal = 0, nokta3Ekonomik = 0,
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
