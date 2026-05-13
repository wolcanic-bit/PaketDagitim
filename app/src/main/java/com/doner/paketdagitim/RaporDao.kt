package com.doner.paketdagitim

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RaporDao {
    @Query("SELECT * FROM gunluk_raporlar ORDER BY kayitZamani DESC")
    fun tumRaporlariGetir(): Flow<List<GunlukRapor>>
    
    @Query("SELECT * FROM gunluk_raporlar WHERE tarih = :tarih LIMIT 1")
    suspend fun gununRaporunuGetir(tarih: String): GunlukRapor?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun raporKaydet(rapor: GunlukRapor)
    
    @Delete
    suspend fun raporSil(rapor: GunlukRapor)
}
