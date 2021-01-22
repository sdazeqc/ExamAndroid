package com.example.myproject

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

import com.example.myproject.classes.*


@Database(version = 1, entities = [MotCle::class, Entreprise::class, Lien::class])

abstract class TodoDataBase: RoomDatabase() {

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