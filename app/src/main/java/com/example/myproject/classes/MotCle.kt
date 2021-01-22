package com.example.myproject.classes

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.LocalDateTime

@Entity
data class MotCle (@PrimaryKey(autoGenerate = true) var id: Long?=null,
                    var libelle: String,
                    var departement:Int,
                    var codePostal:Int,
                    //var dateTime:LocalDateTime
                    var json:String?=null
                   ): Serializable
{

}