package com.example.myproject.classes

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
        indices = [ Index(value = ["codeNAF"], unique = true) ]
)
data class code_NAF_APE(@PrimaryKey(autoGenerate = false) var codeNAF:String,
               var description:String,
               var section:String,
               var descriptionSection:String
) {
    override fun toString(): String {
        return "Naf(codeNAF='$codeNAF', description='$description', section='$section', descriptionSection='$descriptionSection')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as code_NAF_APE

        if (codeNAF != other.codeNAF) return false
        if (description != other.description) return false
        if (section != other.section) return false
        if (descriptionSection != other.descriptionSection) return false

        return true
    }

    override fun hashCode(): Int {
        var result = codeNAF.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + section.hashCode()
        result = 31 * result + descriptionSection.hashCode()
        return result
    }


}