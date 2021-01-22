package com.example.myproject.data

import androidx.room.*
import com.example.myproject.classes.Lien
import com.example.myproject.classes.MotCle

@Dao
interface LienDOA {

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