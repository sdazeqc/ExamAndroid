package com.example.myproject.data

import androidx.room.*
import com.example.myproject.classes.Entreprise
import com.example.myproject.classes.Lien
import com.example.myproject.classes.Recherche

@Dao
interface LienDAO {

    @Query("SELECT * FROM entreprise,lien where lien.search=:recherche and lien.company=entreprise.id")
    fun getByRecherche(recherche:Int): List<Entreprise>


    @Query("SELECT count(*) FROM lien ")
    fun count(): Int


    @Query("SELECT * FROM lien ")
    fun getALL(): List<Lien>

    @Insert
    fun insert(lien: Lien): Long

    @Update
    fun update(lien: Lien)

    @Delete
    fun delete(lien: Lien)
}