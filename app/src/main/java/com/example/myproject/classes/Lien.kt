package com.example.myproject.classes

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

import java.time.LocalDate

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Entreprise::class,
            parentColumns = ["id"],
            childColumns = ["company"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Recherche::class,
            parentColumns = ["id"],
            childColumns = ["search"],
            onDelete = ForeignKey.CASCADE
        )],
    primaryKeys = ["company", "search"]
)

data class Lien( var company:Long,
                 var search:Long):Serializable
{

}