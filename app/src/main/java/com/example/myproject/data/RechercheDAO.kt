package com.example.myproject.data

import androidx.room.*
import com.example.myproject.classes.*

@Dao
interface RechercheDAO {

    @Query("SELECT count(*) FROM recherche ")
    fun count(): Int

    @Query("SELECT * FROM recherche  ORDER BY libelle LIMIT 1 OFFSET :position")
    fun getByPosition(position: Int): Recherche?

    @Query("SELECT * FROM recherche ")
    fun getALL(): List<Recherche>

    @Insert
    fun insert(motcle: Recherche): Long

    @Update
    fun update(motcle: Recherche)

    @Delete
    fun delete(motcle: Recherche)
}