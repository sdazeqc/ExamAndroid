package com.example.myproject.data

import androidx.room.*
import com.example.myproject.classes.*

@Dao
interface MotCleDAO {

    @Query("SELECT count(*) FROM motcle ")
    fun count(): Int

    @Query("SELECT * FROM motcle  ORDER BY libelle LIMIT 1 OFFSET :position")
    fun getByPosition(position: Int): MotCle?

    @Query("SELECT * FROM motcle ")
    fun getALL(): List<MotCle>

    @Insert
    fun insert(motcle: MotCle): Long

    @Update
    fun update(motcle: MotCle)

    @Delete
    fun delete(motcle: MotCle)
}