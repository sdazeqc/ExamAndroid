package com.example.myproject

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.example.myproject.classes.Entreprise
import com.example.myproject.data.EntrepriseDAO
import com.example.myproject.data.LienDAO
import com.example.myproject.data.RechercheDAO
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var db = TodoDataBase.getDatabase(this)
        var entrepriseDao = db.entrepriseDAO()
        var lienDao = db.lienDAO()
        var rechercheDao = db.rechercheDAO()

        button.setOnClickListener{
            if((!editTextNumber.text.toString().isBlank() and !ETEntreprise.text.toString().isBlank()) or !ETEntreprise.text.toString().isBlank()){
                QueryLocationEntreprise(entrepriseDao,lienDao,rechercheDao).execute()
            }
            else {
                Toast.makeText(this, "Le champ n'est pas rempli", Toast.LENGTH_SHORT).show()
            }
        }
        floatingActionButton5.setOnClickListener{
            textView2.visibility=View.VISIBLE
            editTextNumber.visibility=View.VISIBLE
            floatingActionButton5.visibility=View.INVISIBLE
            floatingActionButton6.visibility=View.VISIBLE
        }
        floatingActionButton6.setOnClickListener {
            textView2.visibility=View.GONE
            editTextNumber.visibility=View.GONE
            floatingActionButton5.visibility=View.VISIBLE
            floatingActionButton6.visibility=View.INVISIBLE
            editTextNumber.text.clear()
        }
    }

    inner class QueryLocationEntreprise(entrepriseDao:EntrepriseDAO,lienDao:LienDAO,rechercheDao:RechercheDAO): AsyncTask<Void, Void, Boolean>() {
        var list = emptyList<Entreprise>()
        var entr = EntrepriseService(entrepriseDao,lienDao,rechercheDao)

        override fun doInBackground(vararg params: Void?): Boolean {
            if(!editTextNumber.text.toString().isBlank() and !ETEntreprise.text.toString().isBlank()){
                if(editTextNumber.text.toString().length==2){
                    list=entr.getEntreprise(ETEntreprise.text.toString(),editTextNumber.text.toString())
                }
                else{
                    list=entr.getEntreprise(ETEntreprise.text.toString(),"",editTextNumber.text.toString())
                }

            }
            else if(!ETEntreprise.text.toString().isBlank()){
                list=entr.getEntreprise(ETEntreprise.text.toString())
            }


            println(list)
            return true
        }

        override fun onPreExecute() {
            listEntreprise.setAdapter(null);
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