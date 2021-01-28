package com.example.myproject

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myproject.classes.code_NAF_APE
import com.example.myproject.classes.Entreprise
import com.example.myproject.data.EntrepriseDAO
import com.example.myproject.data.LienDAO
import com.example.myproject.data.RechercheDAO
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val saveIdRecherche = "idrecherche"
    private val SaveIdRecherche = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = getPreferences(MODE_PRIVATE)

        val textSearch = prefs.getString("Entreprise", "")
        val OtherSearch = prefs.getString("Other", "")

        ETEntreprise.setText(textSearch)
        ETOther.setText(OtherSearch)


        var db = TodoDataBase.getDatabase(this)
        //db.seed()
        var entrepriseDao = db.entrepriseDAO()
        var lienDao = db.lienDAO()
        var rechercheDao = db.rechercheDAO()
        var nafDAO = db.nafDAO()

        var nafbdd = CodeNafDatabase.getDatabase(this)
        //var nafDAO = nafbdd.NafapeDao()


        var listSearch=rechercheDao.getALL()
        //traitement de tout les liens
        var nbOld:Int =0
        //suppression 3mois +
        listSearch.forEach {

            var sdf = SimpleDateFormat("yyyy-MM-dd")
            val c=Calendar.getInstance()
            var datenowcompar = sdf.parse(it.date)

            c.add(Calendar.MONTH,-3);
            var datepasse=c.getTime()
            var datepast = sdf.format(datepasse)
            var datepastcompar = sdf.parse(datepast)
            println(datepastcompar)
            println(datenowcompar)
            if(datepastcompar>datenowcompar){
                nbOld=nbOld+1
                println("ici")
                var obj=lienDao.getLinkCompany(it.id?.toInt()!!)
                rechercheDao.delete(it)
                if(obj!=null){
                    var id=obj.company.toInt()
                    println("ici")
                    if(lienDao.getAllLinkCompany(id)?.size!!>1){
                        lienDao.deletById(it.id?.toInt()!!)

                    }
                    else{
                        lienDao.deletById(it.id?.toInt()!!)
                        entrepriseDao.deletById(id)
                    }
                }
            }

        }
        if(nbOld>=1){
            Toast.makeText(this, "Aucune recherche plus vieille de 3 mois", Toast.LENGTH_SHORT).show()
        }

        //remettre le textes dans les champs
        if(savedInstanceState!=null && savedInstanceState.containsKey(SaveIdRecherche)){

            val rId=savedInstanceState.getLong(SaveIdRecherche)
            var saveRecherche = rechercheDao.getById(rId.toInt())
            if(saveRecherche!=null){
                if(saveRecherche.codePostal.toString()!="kotlin.unix"){
                    ETOther.setText(saveRecherche.codePostal.toString())
                    txtOther.visibility=View.VISIBLE
                    ETOther.visibility=View.VISIBLE
                    btnDown.visibility=View.INVISIBLE
                    btnUp.visibility=View.VISIBLE
                }
                if(saveRecherche.departement.toString()!="kotlin.unix"){
                    ETOther.setText(saveRecherche.departement.toString())
                    txtOther.visibility=View.VISIBLE
                    ETOther.visibility=View.VISIBLE
                    btnDown.visibility=View.INVISIBLE
                    btnUp.visibility=View.VISIBLE
                }
                ETEntreprise.setText(saveRecherche.libelle)
            }
        }



        val categories= nafDAO.getAll()
        val dataAdapter = ArrayAdapter<code_NAF_APE>(this, android.R.layout.simple_spinner_item, categories!!)

        SCNaf.setAdapter(dataAdapter);



        btnSearch.setOnClickListener{
            if((!ETOther.text.toString().isBlank() and !ETEntreprise.text.toString().isBlank()) or !ETEntreprise.text.toString().isBlank()){
                QueryLocationEntreprise(entrepriseDao,lienDao,rechercheDao).execute()

                val prefs = getPreferences(MODE_PRIVATE)
                val editor = prefs.edit()

                editor.putString("Entreprise", ETEntreprise.text.toString())
                editor.putString("Other", ETOther.text.toString())

                editor.commit()
            }
            else {
                Toast.makeText(this, "Le champ n'est pas rempli", Toast.LENGTH_SHORT).show()
            }
        }
        btnDown.setOnClickListener{
            txtOther.visibility=View.VISIBLE
            ETOther.visibility=View.VISIBLE
            btnDown.visibility=View.INVISIBLE
            btnUp.visibility=View.VISIBLE
            SCNaf.visibility=View.VISIBLE
        }
        btnUp.setOnClickListener {
            txtOther.visibility=View.GONE
            ETOther.visibility=View.GONE
            btnDown.visibility=View.VISIBLE
            btnUp.visibility=View.INVISIBLE
            SCNaf.visibility=View.GONE
            ETOther.text.clear()
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
            if(!ETOther.text.toString().isBlank() and !ETEntreprise.text.toString().isBlank()){
                if(ETOther.text.toString().length==2){
                    list=entr.getEntreprise(ETEntreprise.text.toString(),ETOther.text.toString())
                }
                else{
                    list=entr.getEntreprise(ETEntreprise.text.toString(),"",ETOther.text.toString())
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
            QuerySearch.visibility = View.VISIBLE
        }
        override fun onPostExecute(result: Boolean?) {
            QuerySearch.visibility = View.INVISIBLE
            if ((result != null) && result) {
                if(list.size>0){
                    txtVide.visibility=View.INVISIBLE
                    listEntreprise.visibility=View.VISIBLE
                    listEntreprise.adapter = ArrayAdapter<Entreprise>(
                            this@MainActivity,
                            android.R.layout.simple_dropdown_item_1line,
                            list
                    )
                }
                else{
                    txtVide.setText("Aucune donnée a affiché")
                    txtVide.visibility=View.VISIBLE
                    listEntreprise.visibility=View.INVISIBLE
                }
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