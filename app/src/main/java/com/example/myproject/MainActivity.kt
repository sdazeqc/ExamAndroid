package com.example.myproject

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.myproject.classes.Entreprise
import com.example.myproject.data.EntrepriseDAO
import com.example.myproject.data.LienDAO
import com.example.myproject.data.RechercheDAO
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var db = TodoDataBase.getDatabase(this)
        var entrepriseDao = db.entrepriseDAO()
        var lienDao = db.lienDAO()
        var rechercheDao = db.rechercheDAO()

        button.setOnClickListener{
            QueryLocationEntreprise(entrepriseDao,lienDao,rechercheDao).execute()
        }
    }

    inner class QueryLocationEntreprise(entrepriseDao:EntrepriseDAO,lienDao:LienDAO,rechercheDao:RechercheDAO): AsyncTask<Void, Void, Boolean>() {
        var list = emptyList<Entreprise>()
        var entr = EntrepriseService(entrepriseDao,lienDao,rechercheDao)

        override fun doInBackground(vararg params: Void?): Boolean {
            list=entr.getEntreprise(ETEntreprise.text.toString())
            println(list)
            return true
        }

        override fun onPostExecute(result: Boolean?) {
            if ((result != null) && result) {
                listEntreprise.adapter = ArrayAdapter<Entreprise>(
                    this@MainActivity,
                    android.R.layout.simple_dropdown_item_1line,
                    list
                )
            }
        }

    }
}