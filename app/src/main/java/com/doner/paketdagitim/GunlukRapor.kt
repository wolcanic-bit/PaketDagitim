package com.doner.paketdagitim

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gunluk_raporlar")
data class GunlukRapor(
    @PrimaryKey
    val tarih: String,
    val nokta1Isim: String = "",
    val nokta1Normal: Int = 0,
    val nokta1Ekonomik: Int = 0,
    val nokta1Tutar: Int = 0,
    val nokta2Isim: String = "",
    val nokta2Normal: Int = 0,
    val nokta2Ekonomik: Int = 0,
    val nokta2Tutar: Int = 0,
    val nokta3Isim: String = "",
    val nokta3Normal: Int = 0,
    val nokta3Ekonomik: Int = 0,
    val nokta3Tutar: Int = 0,
    val toplamPaket: Int = 0,
    val toplamKazanc: Int = 0,
    val normalFiyat: Int = 80,
    val ekonomikFiyat: Int = 25,
    val kayitZamani: Long = System.currentTimeMillis()
)
