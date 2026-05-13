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
    val noktaNo: Int,
    val noktaIsim: String,
    val normal: Int,
    val ekonomik: Int
)

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).raporDao()
    
    // Gecici giris
    private val _girisNormal = MutableStateFlow(0)
    val girisNormal: StateFlow<Int> = _girisNormal.asStateFlow()
    private val _girisEkonomik = MutableStateFlow(0)
    val girisEkonomik: StateFlow<Int> = _girisEkonomik.asStateFlow()
    
    // Secili nokta
    private val _seciliNokta = MutableStateFlow(1)
    val seciliNokta: StateFlow<Int> = _seciliNokta.asStateFlow()
    
    // Her nokta icin ayri toplam
    private val _n1Normal = MutableStateFlow(0); private val _n1Eko = MutableStateFlow(0)
    private val _n2Normal = MutableStateFlow(0); private val _n2Eko = MutableStateFlow(0)
    private val _n3Normal = MutableStateFlow(0); private val _n3Eko = MutableStateFlow(0)
    
    val n1Normal: StateFlow<Int> = _n1Normal.asStateFlow()
    val n1Eko: StateFlow<Int> = _n1Eko.asStateFlow()
    val n2Normal: StateFlow<Int> = _n2Normal.asStateFlow()
    val n2Eko: StateFlow<Int> = _n2Eko.asStateFlow()
    val n3Normal: StateFlow<Int> = _n3Normal.asStateFlow()
    val n3Eko: StateFlow<Int> = _n3Eko.asStateFlow()
    
    // Son islem
    private var sonIslem: SonIslem? = null
    
    // Isimler
    private val _n1Isim = MutableStateFlow("Hatay Döner")
    val n1Isim: StateFlow<String> = _n1Isim.asStateFlow()
    private val _n2Isim = MutableStateFlow("Öncü Döner")
    val n2Isim: StateFlow<String> = _n2Isim.asStateFlow()
    private val _n3Isim = MutableStateFlow("Reis Döner")
    val n3Isim: StateFlow<String> = _n3Isim.asStateFlow()
    
    // Fiyatlar
    private val _normalFiyat = MutableStateFlow(80)
    val normalFiyat: StateFlow<Int> = _normalFiyat.asStateFlow()
    private val _ekonomikFiyat = MutableStateFlow(25)
    val ekonomikFiyat: StateFlow<Int> = _ekonomikFiyat.asStateFlow()
    
    val tumRaporlar = dao.tumRaporlariGetir()
    
    private val _mesaj = MutableStateFlow<String?>(null)
    val mesaj: StateFlow<String?> = _mesaj.asStateFlow()
    
    val toplamNormal: Int get() = _n1Normal.value + _n2Normal.value + _n3Normal.value
    val toplamEkonomik: Int get() = _n1Eko.value + _n2Eko.value + _n3Eko.value
    val toplamPaket: Int get() = toplamNormal + toplamEkonomik
    val toplamKazanc: Int get() = (toplamNormal * _normalFiyat.value) + (toplamEkonomik * _ekonomikFiyat.value)
    
    fun nokta1Tutar(): Int = (_n1Normal.value * _normalFiyat.value) + (_n1Eko.value * _ekonomikFiyat.value)
    fun nokta2Tutar(): Int = (_n2Normal.value * _normalFiyat.value) + (_n2Eko.value * _ekonomikFiyat.value)
    fun nokta3Tutar(): Int = (_n3Normal.value * _normalFiyat.value) + (_n3Eko.value * _ekonomikFiyat.value)
    
    val seciliNoktaIsim: String get() = when(_seciliNokta.value) {
        1 -> _n1Isim.value; 2 -> _n2Isim.value; 3 -> _n3Isim.value; else -> ""
    }
    
    val bugununTarihi: String
        get() { val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()); return sdf.format(Date()) }
    
    fun noktaSec(no: Int) { _seciliNokta.value = no }
    fun girisNormalArttir() { _girisNormal.value++ }
    fun girisNormalAzalt() { if (_girisNormal.value > 0) _girisNormal.value-- }
    fun girisEkonomikArttir() { _girisEkonomik.value++ }
    fun girisEkonomikAzalt() { if (_girisEkonomik.value > 0) _girisEkonomik.value-- }
    
    fun siparisiOnayla() {
        val n = _girisNormal.value; val e = _girisEkonomik.value
        if (n == 0 && e == 0) { _mesaj.value = "⚠️ Lutfen paket adedi girin!"; return }
        
        sonIslem = SonIslem(_seciliNokta.value, seciliNoktaIsim, n, e)
        
        when(_seciliNokta.value) {
            1 -> { _n1Normal.value += n; _n1Eko.value += e }
            2 -> { _n2Normal.value += n; _n2Eko.value += e }
            3 -> { _n3Normal.value += n; _n3Eko.value += e }
        }
        
        _mesaj.value = "✅ $seciliNoktaIsim: +$n normal, +$e ekonomik"
        _girisNormal.value = 0; _girisEkonomik.value = 0
    }
    
    fun sonIslemiGeriAl() {
        val islem = sonIslem
        if (islem != null) {
            when(islem.noktaNo) {
                1 -> { _n1Normal.value -= islem.normal; _n1Eko.value -= islem.ekonomik }
                2 -> { _n2Normal.value -= islem.normal; _n2Eko.value -= islem.ekonomik }
                3 -> { _n3Normal.value -= islem.normal; _n3Eko.value -= islem.ekonomik }
            }
            _mesaj.value = "↩️ Geri alindi: ${islem.noktaIsim} -${islem.normal}N, -${islem.ekonomik}E"
            sonIslem = null
        } else { _mesaj.value = "⚠️ Geri alinacak islem yok!" }
    }
    
    fun noktaIsimleriniGuncelle(n1: String, n2: String, n3: String) {
        _n1Isim.value = n1; _n2Isim.value = n2; _n3Isim.value = n3
    }
    
    fun fiyatlariGuncelle(normal: Int, ekonomik: Int) {
        _normalFiyat.value = normal; _ekonomikFiyat.value = ekonomik
    }
    
    fun gunuSifirla() {
        _n1Normal.value = 0; _n1Eko.value = 0
        _n2Normal.value = 0; _n2Eko.value = 0
        _n3Normal.value = 0; _n3Eko.value = 0
        _girisNormal.value = 0; _girisEkonomik.value = 0
        sonIslem = null
    }
    
    fun raporuKaydet() {
        viewModelScope.launch {
            val rapor = GunlukRapor(
                tarih = bugununTarihi,
                nokta1Isim = _n1Isim.value, nokta1Normal = _n1Normal.value, nokta1Ekonomik = _n1Eko.value, nokta1Tutar = nokta1Tutar(),
                nokta2Isim = _n2Isim.value, nokta2Normal = _n2Normal.value, nokta2Ekonomik = _n2Eko.value, nokta2Tutar = nokta2Tutar(),
                nokta3Isim = _n3Isim.value, nokta3Normal = _n3Normal.value, nokta3Ekonomik = _n3Eko.value, nokta3Tutar = nokta3Tutar(),
                toplamPaket = toplamPaket, toplamKazanc = toplamKazanc,
                normalFiyat = _normalFiyat.value, ekonomikFiyat = _ekonomikFiyat.value
            )
            dao.raporKaydet(rapor)
            _mesaj.value = "✅ Rapor kaydedildi! Toplam: $toplamPaket paket, $toplamKazanc TL"
        }
    }
    
    fun mesajiTemizle() { _mesaj.value = null }
    
    fun raporuSil(rapor: GunlukRapor) {
        viewModelScope.launch { dao.raporSil(rapor); _mesaj.value = "🗑️ Rapor silindi." }
    }
}
