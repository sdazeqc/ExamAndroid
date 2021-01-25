package com.example.myproject.classes

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.LocalDate

@Entity
data class Entreprise (@PrimaryKey(autoGenerate = true) var id: Long?=null,
                   var libelle: String?="",
                   var activite:String?="",
                   var adresse: String?="",
                       var departement:String?="",
                       var siret:String?=""
): Serializable
{

    override fun toString(): String {
        return libelle +", "+adresse
    }
}