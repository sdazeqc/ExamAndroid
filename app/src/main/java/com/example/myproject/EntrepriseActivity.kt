package com.example.myproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myproject.classes.Entreprise
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
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

        var mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            var googleMap = it

            val loc1 = LatLng(entreprise.latitude!!.toDouble(), entreprise.longitude!!.toDouble())
            googleMap.addMarker(MarkerOptions().position(loc1).title(entreprise.libelle).visible(true))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc1,14f))
        })
    }
}