package com.example.myproject

import android.util.JsonReader
import com.example.myproject.classes.Entreprise
import com.example.myproject.classes.Lien
import com.example.myproject.classes.Recherche
import com.example.myproject.data.EntrepriseDAO
import com.example.myproject.data.LienDAO
import com.example.myproject.data.RechercheDAO
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class EntrepriseService(entrepriseDao:EntrepriseDAO, lienDao: LienDAO, rechercheDao:RechercheDAO) {



    private val apiUrl="https://entreprise.data.gouv.fr"
    private val queryUrl="$apiUrl/api/sirene/v1/full_text/%s"
    val entrepriseDao=entrepriseDao
    val lienDao=lienDao
    val rechercheDao=rechercheDao

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

            var recherche=Recherche()
            recherche.libelle=q

            //on insert et recup l'id de la recherche
            var idRecherche= rechercheDao.insert(recherche)


            reader.beginObject()
            while (reader.hasNext()){
                var next=reader.nextName()
                if(next=="etablissement"){
                    reader.beginArray()
                    while(reader.hasNext()){
                        reader.beginObject()
                        while (reader.hasNext()){
                            when(reader.nextName()){
                                    "libelle_activite_principale"->entreprise.activite = reader.nextString().toString()
                                    "geo_adresse"->entreprise.adresse = reader.nextString().toString()
                                    "departement"-> entreprise.departement = reader.nextString().toString()
                                    "siret"->entreprise.siret = reader.nextString().toString()
                                    "nom_raison_sociale"->entreprise.libelle = reader.nextString().toString()
                                    else->reader.skipValue()
                            }

                        }
                        //on insert et recup l'id de l'entreprise
                        var idEntreprise = entrepriseDao.insert(entreprise)

                        //rajout de tout les liens
                        var lien= Lien(idEntreprise,idRecherche)
                        lienDao.insert(lien)

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