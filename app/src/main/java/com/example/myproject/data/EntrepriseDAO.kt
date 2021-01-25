package com.example.myproject.data

import androidx.room.*
import com.example.myproject.classes.*

@Dao
interface EntrepriseDAO {

    @Query("SELECT * FROM entreprise where siret=:siret")
    fun getBySiret(siret:String): Entreprise?

    @Query("SELECT count(*) FROM entreprise ")
    fun count(): Int

    @Query("SELECT * FROM entreprise  ORDER BY libelle LIMIT 1 OFFSET :position")
    fun getByPosition(position: Int): Entreprise?

    @Query("SELECT * FROM entreprise ")
    fun getALL(): List<Entreprise>

    @Insert
    fun insert(entreprise: Entreprise): Long

    @Update
    fun update(entreprise: Entreprise)

    @Delete
    fun delete(entreprise: Entreprise)
}