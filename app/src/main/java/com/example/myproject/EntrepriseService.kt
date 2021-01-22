package com.example.myproject

import android.util.JsonReader
import com.example.myproject.classes.Entreprise
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class EntrepriseService {



    private val apiUrl="https://entreprise.data.gouv.fr"
    private val queryUrl="$apiUrl/api/sirene/v1/full_text/%s"

    fun getEntreprise(q: String): List<Entreprise>{
        val url= URL(String.format(queryUrl,q))
        var conn: HttpURLConnection?=null

        var list = mutableListOf<Entreprise>()
        var entreprise=Entreprise()
        try {
            conn = url.openConnection() as HttpURLConnection
            conn.connect()
            val code = conn.responseCode
            if(code != HttpURLConnection.HTTP_OK){
                return emptyList()
            }
            val inputStream = conn.inputStream?: return emptyList()
            val reader = JsonReader(inputStream.bufferedReader())

            reader.beginObject()
            while (reader.hasNext()){
                var firstsuivant=reader.nextName()
                if(firstsuivant=="etablissement"){
                    reader.beginArray()
                    while(reader.hasNext()){
                        reader.beginObject()
                        while (reader.hasNext()){
                            when(reader.nextName()){
                                "nom_raison_sociale"->entreprise.libelle=reader.nextString().toString()
                                "geo_adresse"->entreprise.adresse=reader.nextString().toString()
                                "departement"->entreprise.departement=reader.nextString().toString()
                                "libelle_activite_principale"->entreprise.activite=reader.nextString().toString()
                                "siret"->entreprise.siret=reader.nextString().toString()
                                else->reader.skipValue()
                            }
                        }
                        list.add(entreprise)
                        entreprise=Entreprise()
                        reader.endObject()
                    }
                    return list
                }
                else{
                    reader.skipValue()
                }
            }
            reader.endObject()

            return list
        }
        catch (e: IOException){
            return emptyList()
        }finally {
            conn?.disconnect()
        }
    }
}