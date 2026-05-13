package com.doner.paketdagitim

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gunluk_raporlar")
data class GunlukRapor(
    @PrimaryKey
    val tarih: String,
    val nokta1Paket: Int = 0,
    val nokta2Paket: Int = 0,
    val nokta3Paket: Int = 0,
    val toplamPaket: Int = 0,
    val toplamKazanc: Int = 0,
    val kayitZamani: Long = System.currentTimeMillis()
)
