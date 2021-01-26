package com.example.myproject

import android.util.JsonReader
import android.util.JsonToken
import com.example.myproject.classes.Entreprise
import com.example.myproject.classes.Lien
import com.example.myproject.classes.Recherche
import com.example.myproject.data.EntrepriseDAO
import com.example.myproject.data.LienDAO
import com.example.myproject.data.RechercheDAO
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class EntrepriseService(entrepriseDao:EntrepriseDAO, lienDao: LienDAO, rechercheDao:RechercheDAO) {



    private val apiUrl="https://entreprise.data.gouv.fr"
    private val queryUrl="$apiUrl/api/sirene/v1/full_text/%s"
    private val queryUrlCP="$apiUrl/api/sirene/v1/full_text/%s?code_postal=%s"
    private val queryUrlDepartement="$apiUrl/api/sirene/v1/full_text/%s?departement=%s"
    val entrepriseDao=entrepriseDao
    val lienDao=lienDao
    val rechercheDao=rechercheDao

    fun getEntreprise(q: String,departement:String?="",cp:String?=""): List<Entreprise>{

        val url:URL
        if(cp?.length!! >0){
             url= URL(String.format(queryUrlCP,q,cp))
            println("tata")

        }
        else if(departement?.length!! > 0){
             url= URL(String.format(queryUrlDepartement,q,departement))
            println("titi")
        }
        else{
             url= URL(String.format(queryUrl,q))
            println("toto")
        }


        var DateMtn= SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        if(rechercheDao.getRechercheDate(url.toString(),DateMtn)!=null){
            var listBdd=getBdd(url.toString(),DateMtn)
            println("tutu")
            return listBdd
        }


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
            var DateMtn= SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            var recherche=Recherche()
            recherche.libelle=q
            recherche.url=url.toString()
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
                                    "libelle_activite_principale"->{
                                        if (reader.peek() == JsonToken.NULL)  {
                                            entreprise.activite = reader.nextNull().toString()
                                        }else{
                                            entreprise.activite = reader.nextString()
                                        }
                                    }
                                    "geo_adresse"->{
                                        if (reader.peek() == JsonToken.NULL)  {
                                            entreprise.adresse = reader.nextNull().toString()
                                        }else{
                                            entreprise.adresse = reader.nextString()
                                        }
                                    }
                                    "departement"-> {
                                        if (reader.peek() == JsonToken.NULL)  {
                                            entreprise.departement = reader.nextNull().toString()
                                        }else{
                                            entreprise.departement = reader.nextString()
                                        }
                                    }
                                    "siret"->{
                                        if (reader.peek() == JsonToken.NULL)  {
                                            entreprise.siret = reader.nextNull().toString()
                                        }else{
                                            entreprise.siret = reader.nextString()
                                        }
                                    }
                                    "nom_raison_sociale"->{
                                        if (reader.peek() == JsonToken.NULL)  {
                                            entreprise.libelle = reader.nextNull().toString()
                                        }else{
                                            entreprise.libelle = reader.nextString()
                                        }
                                    }
                                    else->reader.skipValue()
                            }
                        }
                        //on insert et recup l'id de l'entreprise
                        var old = entrepriseDao.getBySiret(entreprise.siret.toString())

                        var idEntreprise:Long?=null

                        if(old!=null){
                            idEntreprise = old.id

                            old.libelle=entreprise.libelle
                            old.adresse=entreprise.adresse
                            old.activite=entreprise.activite
                            old.departement=entreprise.departement

                            entrepriseDao.update(old)
                        }
                        else{
                             idEntreprise = entrepriseDao.insert(entreprise)
                        }

                        //rajout de tout les liens
                        var lien= Lien(idEntreprise!!,idRecherche)
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


    fun getBdd(url:String,Date:String):List<Entreprise>{
        var list=rechercheDao.getByDate(url,Date)
        return list
    }

}