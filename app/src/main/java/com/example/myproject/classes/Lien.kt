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
            entity = MotCle::class,
            parentColumns = ["id"],
            childColumns = ["search"],
            onDelete = ForeignKey.CASCADE
        )]
)
data class Lien(@PrimaryKey(autoGenerate = false) var company:Long,
                 var search:Long): Serializable
//@PrimaryKey(autoGenerate = false)
{

}