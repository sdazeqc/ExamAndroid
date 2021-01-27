package com.example.myproject.data

import androidx.room.*
import com.example.myproject.classes.code_NAF_APE

@Dao
interface NafDAO {

    @Query("SELECT * FROM code_NAF_APE")
    fun getAll(): List<code_NAF_APE>

    @Query("SELECT COUNT(*) FROM code_NAF_APE ")
    fun count(): Int

    @Insert
    fun insert(naf: code_NAF_APE): Long

    @Update
    fun update(naf: code_NAF_APE)

    @Delete
    fun delete(naf: code_NAF_APE)
}