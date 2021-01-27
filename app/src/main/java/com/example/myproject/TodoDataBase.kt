package com.example.myproject

import android.content.Context
import android.provider.SyncStateContract.Helpers.insert
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

import com.example.myproject.classes.*
import com.example.myproject.data.EntrepriseDAO
import com.example.myproject.data.LienDAO
import com.example.myproject.data.NafDAO
import com.example.myproject.data.RechercheDAO
import java.text.ParseException


@Database(version = 1, entities = [Recherche::class, Entreprise::class, Lien::class,code_NAF_APE::class])

abstract class TodoDataBase: RoomDatabase() {

    abstract fun entrepriseDAO(): EntrepriseDAO
    abstract fun lienDAO(): LienDAO
    abstract fun rechercheDAO(): RechercheDAO
    abstract fun nafDAO(): NafDAO

    fun seed(){
        var recherche=Recherche()
        recherche.libelle="ici"
        recherche.codePostal=null
        recherche.departement=null
        recherche.url="tsdfqsdfqsdf"
        recherche.date="2020-10-26"
        rechercheDAO().insert(recherche)

        var recherche2=Recherche()
        recherche.libelle="ici"
        recherche.codePostal=null
        recherche.departement=null
        recherche.url="tsdfqsdfqsdf"
        recherche.date="2020-12-26"
        rechercheDAO().insert(recherche2)
    }

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