package com.example.myproject

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import com.example.myproject.classes.Entreprise
import com.example.myproject.data.EntrepriseDAO
import com.example.myproject.data.LienDAO
import com.example.myproject.data.RechercheDAO
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import java.util.ArrayList

class MainActivity : AppCompatActivity() {


    private val saveIdRecherche = "idrecherche"
    private val SaveIdRecherche = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var db = TodoDataBase.getDatabase(this)
        var entrepriseDao = db.entrepriseDAO()
        var lienDao = db.lienDAO()
        var rechercheDao = db.rechercheDAO()


        if(savedInstanceState!=null && savedInstanceState.containsKey(SaveIdRecherche)){

            val rId=savedInstanceState.getLong(SaveIdRecherche)
            var saveRecherche = rechercheDao.getById(rId.toInt())
            if(saveRecherche!=null){
                if(saveRecherche.codePostal.toString()!="kotlin.unix"){
                    editTextNumber.setText(saveRecherche.codePostal.toString())
                    textView2.visibility=View.VISIBLE
                    editTextNumber.visibility=View.VISIBLE
                    floatingActionButton5.visibility=View.INVISIBLE
                    floatingActionButton6.visibility=View.VISIBLE
                }
                if(saveRecherche.departement.toString()!="kotlin.unix"){
                    editTextNumber.setText(saveRecherche.departement.toString())
                    textView2.visibility=View.VISIBLE
                    editTextNumber.visibility=View.VISIBLE
                    floatingActionButton5.visibility=View.INVISIBLE
                    floatingActionButton6.visibility=View.VISIBLE
                }
                ETEntreprise.setText(saveRecherche.libelle)

            }
        }

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

        listEntreprise.setOnItemClickListener { _, _, position, _ ->
            val entreprise = listEntreprise.adapter.getItem(position) as Entreprise
            val intent = Intent(this, EntrepriseActivity::class.java)
            intent.putExtra("location", entreprise)
            startActivity(intent)
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SaveIdRecherche, saveIdRecherche)
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
            queryProgressBar.visibility = View.VISIBLE
        }
        override fun onPostExecute(result: Boolean?) {
            queryProgressBar.visibility = View.INVISIBLE
            if ((result != null) && result) {
                listEntreprise.adapter = ArrayAdapter<Entreprise>(
                    this@MainActivity,
                    android.R.layout.simple_dropdown_item_1line,
                    list
                )
            }
        }

    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.recherche_menu,menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val db = TodoDataBase.getDatabase(this)
        val taskDAO = db.entrepriseDAO()

        when(item.itemId){
            R.id.menu_task_checkall -> {
                val intent = Intent(this, HistoryActivity::class.java)
                intent.putExtra("value", "old")
                startActivity(intent)
            }

            R.id.menu_task_uncheckall -> {
                val intent = Intent(this, HistoryActivity::class.java)
                intent.putExtra("value", "current")
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}