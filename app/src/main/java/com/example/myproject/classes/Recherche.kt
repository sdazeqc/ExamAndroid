package com.example.myproject.classes

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

var DateMtn= SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

@Entity
data class Recherche (@PrimaryKey(autoGenerate = true) var id: Long?=null,
                    var libelle: String="",
                    var departement:Int?=null,
                    var codePostal:Int?=null,
                    var date:String=DateMtn,
                      var url:String?=""
                   ): Serializable
{
    override fun toString(): String {
        if(departement!=null){
            return "nom = "+ libelle+" departement = "+ departement+" date="+date
        }
        if(codePostal!=null){
            return "nom = "+ libelle+" cp = "+codePostal+" date="+date
        }
        return "nom = "+ libelle +" date="+date
    }
}