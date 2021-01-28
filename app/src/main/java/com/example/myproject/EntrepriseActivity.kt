package com.example.myproject

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myproject.classes.Entreprise
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_entreprise.*

class EntrepriseActivity: AppCompatActivity() {

    private val saveIdEntreprise = "idEntreprise"
    private val SaveIdEntreprise = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entreprise)
        var entreprise = intent?.extras?.get("location") as? Entreprise ?: return

        if(savedInstanceState!=null && savedInstanceState.containsKey(SaveIdEntreprise)){
            val rId=savedInstanceState.getLong(SaveIdEntreprise)
            var db = TodoDataBase.getDatabase(this)
            var entrepriseDao = db.entrepriseDAO()
            entreprise=entrepriseDao.getBySiret(rId.toString())!!
        }

        println(entreprise)
        txtNom.text=entreprise.libelle
        txtSiret.text="numero : "+entreprise.siret
        txtActivite.text="activité : "+entreprise.activite
        txtAdresse.text="adresse : "+entreprise.adresse + " dans le "+ entreprise.departement

        var mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            var googleMap = it

            if(entreprise.latitude!="kotlin.Unit" && entreprise.longitude!="kotlin.Unit"){
                val loc1 = LatLng(entreprise.latitude!!.toDouble(), entreprise.longitude!!.toDouble())
                googleMap.addMarker(MarkerOptions().position(loc1).title(entreprise.libelle).visible(true))
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc1,14f))
            }
            else{
                Toast.makeText(this, "Aucune coordonnée ", Toast.LENGTH_SHORT).show()
            }

        })


    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SaveIdEntreprise, saveIdEntreprise)
    }

}