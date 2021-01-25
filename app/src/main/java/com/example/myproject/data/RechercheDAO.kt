package com.example.myproject.data

import androidx.room.*
import com.example.myproject.classes.*

@Dao
interface RechercheDAO {

    @Query("SELECT entreprise.* FROM recherche,lien,entreprise WHERE recherche.libelle=:q and recherche.id=lien.search and lien.company=entreprise.id and recherche.date=:date")
    fun getByDate(q:String,date:String): List<Entreprise>

    @Query("SELECT count(*) FROM recherche ")
    fun count(): Int

    @Query("SELECT * FROM recherche  ORDER BY libelle LIMIT 1 OFFSET :position")
    fun getByPosition(position: Int): Recherche?

    @Query("SELECT * FROM recherche where libelle=:q and date=:date")
    fun getRechercheDate(q: String, date:String): Recherche?

    @Query("SELECT * FROM recherche ")
    fun getALL(): List<Recherche>

    @Insert
    fun insert(motcle: Recherche): Long

    @Update
    fun update(motcle: Recherche)

    @Delete
    fun delete(motcle: Recherche)

}