package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myproject.classes.Entreprise
import com.example.myproject.classes.Recherche
import kotlinx.android.synthetic.main.activity_history.*
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
        var list:List<Recherche>
        when(value){
            "current"->{
                 list = rechercheDao.getByCurrent(DateMtn)
                if(list.size>0){
                    txtVideHistory.visibility= View.INVISIBLE
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
                    txtVideHistory.setText("Aucune donnée a affiché")
                    txtVideHistory.visibility= View.VISIBLE
                    listRecherche.visibility= View.GONE
                    textView8.visibility= View.GONE
                    listresult.visibility= View.GONE
                }
            }
            "old"->{
                list = rechercheDao.getByOld(DateMtn)
                println(list)
                if(list.size>0){
                    txtVideHistory.visibility= View.INVISIBLE
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
                    txtVideHistory.setText("Aucune donnée a affiché")
                    txtVideHistory.visibility= View.VISIBLE
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
                    var list = lienDao.getByRecherche(recherche.id?.toInt()!!)
                    if(list.size>0){
                        listresult.adapter = ArrayAdapter<Entreprise>(
                                this@HistoryActivity,
                                android.R.layout.simple_dropdown_item_1line,
                                list
                        )
                    }else{
                        listresult.setAdapter(null);
                        Toast.makeText(this, "Aucune entreprise", Toast.LENGTH_SHORT).show()
                    }
                }
                "old" -> {
                    //depuis la bdd

                    var list = lienDao.getByRecherche(recherche.id?.toInt()!!)
                    if(list.size>0){
                        listresult.adapter = ArrayAdapter<Entreprise>(
                                this@HistoryActivity,
                                android.R.layout.simple_dropdown_item_1line,
                                lienDao.getByRecherche(recherche.id?.toInt()!!)
                        )
                    }else{
                        listresult.setAdapter(null);
                        Toast.makeText(this, "Aucune entreprise", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        listresult.setOnItemClickListener { _, _, position, _ ->
            val entreprise = listresult.adapter.getItem(position) as Entreprise
            val intent = Intent(this@HistoryActivity, EntrepriseActivity::class.java)
            intent.putExtra("location", entreprise)
            startActivity(intent)
        }

    }
}