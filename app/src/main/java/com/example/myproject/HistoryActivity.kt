package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.myproject.classes.Entreprise
import com.example.myproject.classes.Recherche
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class HistoryActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        val value = intent?.getStringExtra("value")

        var db = TodoDataBase.getDatabase(this)
        var entrepriseDao = db.entrepriseDAO()
        var lienDao = db.lienDAO()
        var rechercheDao = db.rechercheDAO()
        var DateMtn= SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        when(value){
            "current"->{
                var list = rechercheDao.getByCurrent(DateMtn)
                if(list.size>0){
                    textView11.visibility= View.INVISIBLE
                    listRecherche.visibility= View.VISIBLE
                    textView8.visibility= View.VISIBLE
                    listresult.visibility= View.VISIBLE
                    listRecherche.adapter = ArrayAdapter<Recherche>(
                            this@HistoryActivity,
                            android.R.layout.simple_dropdown_item_1line,
                            list
                    )
                }
                else{
                    textView11.setText("Aucune donnée a affiché")
                    textView11.visibility= View.VISIBLE
                    listRecherche.visibility= View.GONE
                    textView8.visibility= View.GONE
                    listresult.visibility= View.GONE
                }


            }
            "old"->{
                var list = rechercheDao.getByOld(DateMtn)
                if(list.size>0){
                    textView11.visibility= View.INVISIBLE
                    listRecherche.visibility= View.VISIBLE
                    textView8.visibility= View.VISIBLE
                    listresult.visibility= View.VISIBLE
                    listRecherche.adapter = ArrayAdapter<Recherche>(
                            this@HistoryActivity,
                            android.R.layout.simple_dropdown_item_1line,
                            list
                    )
                }
                else{
                    textView11.setText("Aucune donnée a affiché")
                    textView11.visibility= View.VISIBLE
                    listRecherche.visibility= View.GONE
                    textView8.visibility= View.GONE
                    listresult.visibility= View.GONE
                }

            }
        }

        listRecherche.setOnItemClickListener { _, _, position, _ ->
            val recherche = listRecherche.adapter.getItem(position) as Recherche
            when(value) {
                "current" -> {
                    listresult.adapter = ArrayAdapter<Entreprise>(
                            this@HistoryActivity,
                            android.R.layout.simple_dropdown_item_1line,
                            lienDao.getByRecherche(recherche.id?.toInt()!!)
                    )
                }
                "old" -> {
                    //depuis la bdd
                    listresult.adapter = ArrayAdapter<Entreprise>(
                            this@HistoryActivity,
                            android.R.layout.simple_dropdown_item_1line,
                            lienDao.getByRecherche(recherche.id?.toInt()!!)
                    )
                }
            }
        }

    }
}