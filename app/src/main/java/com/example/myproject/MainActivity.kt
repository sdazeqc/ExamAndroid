package com.example.myproject

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.myproject.classes.Entreprise
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener{
            QueryLocationEntreprise().execute()
        }
    }

    inner class QueryLocationEntreprise(): AsyncTask<Void, Void, Boolean>() {
        var list = emptyList<Entreprise>()
        var entr = EntrepriseService()

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