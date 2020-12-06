package com.example.app_pm

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.app_pm.api.EndPoints
import com.example.app_pm.api.ServiceBuilder
import com.example.app_pm.api.User
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private var casaLat: Double = 0.0
    private var casaLng: Double = 0.0
    private lateinit var mMap: GoogleMap
    private lateinit var users: List<User>
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    //variáveis para imnplementar updates periódicos de localização
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest : LocationRequest

    override fun onCreateOptionsMenu(menu2: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu2, menu2)
        return true
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        //request creation
        createLocationRequest()

        //iniciar fusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        casaLat = 41.4190
        casaLng = -8.3459



        //adiciona para implementar a localização por updates periódicos
        locationCallback = object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                lastLocation = p0.lastLocation
                var loc = LatLng(lastLocation.latitude,lastLocation.longitude)

                //mMap.addMarker(MarkerOptions().position(loc).title("Marker"))
                findViewById<TextView>(R.id.Coordenadas).setText("Lat: " +loc.latitude + " - Long: " + loc.longitude)

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15.0f))

                //reversegeocoding
                val address = getAddress(lastLocation.latitude, lastLocation.longitude)
                findViewById<TextView>(R.id.Morada).setText("Morada: " + address)

                findViewById<TextView>(R.id.Distância).setText("Distância: " + calculateDistance(
                    lastLocation.latitude, lastLocation.longitude,
                    casaLat, casaLng).toString())
                Log.d("*** RUI","new location received - " +loc.latitude + " - " +loc.longitude)
            }
        }

        //call the service and add markers
        val request= ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getUsers()
        var position: LatLng

       call.enqueue(object: retrofit2.Callback<List<User>>{
           override fun onResponse(call: retrofit2.Call<List<User>>, response: retrofit2.Response<List<User>>){
                if (response.isSuccessful){
                    users = response.body()!!
                    for (user in users){
                        position = LatLng(user.address.geo.lat.toString().toDouble(),
                            user.address.geo.lng.toString().toDouble())
                        mMap.addMarker(MarkerOptions().position(position).title(user.address.suite + " - " + user.address.city))                    }
                }
            }
            override fun onFailure(call: retrofit2.Call<List<User>>, t:Throwable){
                Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

   private fun startLocationUpdates(){
       if(ActivityCompat.checkSelfPermission(this,
           android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
           ActivityCompat.requestPermissions(this,
           arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
           LOCATION_PERMISSION_REQUEST_CODE)
           return
       }

       fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null /*Looper*/)

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    private fun createLocationRequest(){
        locationRequest= LocationRequest()
        //especificar intervalo de tempo para receber updates da localização
        locationRequest.interval=10000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    override fun onPause(){
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
        Log.d("*** RUI", "onPause - removeLocationUpdates")
    }


    override fun onResume(){
        super.onResume()
        startLocationUpdates()
        Log.d("*** RUI","onResume - startLocationUpdates")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        /*  val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/

        setUpMap()
    }

    companion object{
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1          // add to implement location periodic updates
        private const val REQUEST_CHECK_SETTINGS = 2

    }

    fun setUpMap(){

        if (ActivityCompat.checkSelfPermission(this,
            android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
               arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        } else {

            //1
            mMap.isMyLocationEnabled = true

            //2
            fusedLocationClient.lastLocation.addOnSuccessListener(this) {location ->
                //Got know last location. In some rare situation it can return null.«

                if (location != null){
                    lastLocation = location
                    //Toast.makeText(this@MapsActivity, lastLocation.toString(), Toast.LENGTH_SHORT).show()
                    val currentLatLng = LatLng (location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                 }
                }
            }

        }

    private fun getAddress(lat: Double, lng:Double):String{
        val geocoder= Geocoder(this)
        val list= geocoder.getFromLocation(lat,lng,1)
        return list[0].getAddressLine(0)
    }

    fun calculateDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1,lng1,lat2,lng2,results)
        //distância em metros
        return results[0]

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            //logout
            R.id.btn1 -> {
                val sharedPref: SharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE )
                with ( sharedPref.edit() ) {
                    putBoolean(getString(R.string.automatic_login), false )
                    putString(getString(R.string.username_login), null )
                   // putString(getString(R.string.password_login), null )
                    commit()
                }

                //volta a atividade login (Main)
                val intent = Intent(this@MapsActivity, Login::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}
