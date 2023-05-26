package com.example.projectmobile.ui.formreservation.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.projectmobile.R
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Button
import android.widget.ImageButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.projectmobile.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class MapViewActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_view)

        supportActionBar?.hide()

        val backButton: ImageButton = findViewById(R.id.returnButton)
        backButton.setOnClickListener {
            finish()
        }

        val finishButton: Button = findViewById(R.id.finish_button)
        finishButton.setOnClickListener {
            val intent = Intent(this@MapViewActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Verificar permissões de localização
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Obter a localização atual do usuário
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    // Personalize o mapa conforme suas necessidades, como definir a posição inicial e adicionar um marcador
                    val latitude = -4.969732  // Latitude da agência da locadora
                    val longitude = -39.016754  // Longitude da agência da locadora
                    val agencyLocation = LatLng(latitude, longitude)
                    googleMap.addMarker(
                        MarkerOptions().position(agencyLocation).title("Agência da Locadora")
                    )
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(agencyLocation, 16f))

                    // Configurar a rota com dois pontos: localização atual do usuário e agência da locadora
                    val userLocation = LatLng(location.latitude, location.longitude)
                    val routePolylineOptions = PolylineOptions()
                        .add(userLocation)
                        .add(agencyLocation)
                        .width(5f)
                        .color(ContextCompat.getColor(this, R.color.route_color))
                    googleMap.addPolyline(routePolylineOptions)
                }
            }
        } else {
            // Solicitar permissão de localização, se não estiver concedida
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 123
    }
}
