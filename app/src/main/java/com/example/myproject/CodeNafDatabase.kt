package com.example.myproject

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myproject.classes.code_NAF_APE
import com.example.myproject.data.NafDAO

@Database(entities = [code_NAF_APE::class], version = 1)
abstract class CodeNafDatabase: RoomDatabase() {

    abstract fun NafapeDao(): NafDAO

    companion object {
        private var INSTANCE: CodeNafDatabase? = null
        @JvmStatic fun getDatabase(context: Context): CodeNafDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context, CodeNafDatabase::class.java, "NAFAPE.db")
                        .createFromAsset("databases/CodeNaf")
                        .allowMainThreadQueries()
                        .build()
            }
            return INSTANCE!!
        }
    }
}