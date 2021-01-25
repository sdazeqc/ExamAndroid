package com.example.myproject

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

import com.example.myproject.classes.*
import com.example.myproject.data.EntrepriseDAO
import com.example.myproject.data.LienDAO
import com.example.myproject.data.RechercheDAO


@Database(version = 1, entities = [Recherche::class, Entreprise::class, Lien::class])

abstract class TodoDataBase: RoomDatabase() {

    abstract fun entrepriseDAO(): EntrepriseDAO
    abstract fun lienDAO(): LienDAO
    abstract fun rechercheDAO(): RechercheDAO

    companion object {
        var INSTANCE: TodoDataBase? = null

        fun getDatabase(context: Context): TodoDataBase {
            if (INSTANCE == null) {
                INSTANCE = databaseBuilder(context, TodoDataBase::class.java, "todo.db")
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE!!
        }
    }
}