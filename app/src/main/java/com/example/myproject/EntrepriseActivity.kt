package com.example.myproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myproject.classes.Entreprise
import kotlinx.android.synthetic.main.activity_entreprise.*

class EntrepriseActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entreprise)
        val entreprise = intent?.extras?.get("location") as? Entreprise ?: return
        println(entreprise)
        textView3.text="l'entreprise : "+entreprise.libelle
        textView4.text="numero : "+entreprise.siret
        textView5.text="activité : "+entreprise.activite
        textView6.text="adresse : "+entreprise.adresse + " dans le "+ entreprise.departement
    }
}