package com.example.myproject

import android.os.Bundle
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
                listRecherche.adapter = ArrayAdapter<Recherche>(
                        this@HistoryActivity,
                        android.R.layout.simple_dropdown_item_1line,
                        rechercheDao.getByCurrent(DateMtn)
                )
            }
            "old"->{
                listRecherche.adapter = ArrayAdapter<Recherche>(
                        this@HistoryActivity,
                        android.R.layout.simple_dropdown_item_1line,
                        rechercheDao.getByOld(DateMtn)
                )
            }
        }

    }
}