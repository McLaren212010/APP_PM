package com.example.app_pm

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.app_pm.api.EndPoints
import com.example.app_pm.api.OutputPost2
import com.example.app_pm.api.Problem
import com.example.app_pm.api.ServiceBuilder
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, SensorEventListener {

    var sensor: Sensor? = null
    var sensorManager: SensorManager?= null
    var marker_1: Marker? = null
    var isRunning = false


    private var casaLat: Double = 0.0
    private var casaLng: Double = 0.0
    private lateinit var mMap: GoogleMap
    private lateinit var problems: List<Problem>
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var Descr = ""
    private val newWordActivityRequestCode2 = 2
    private val newWordActivityRequestCode3 = 3
    private var User_ID:Int = 0

    //variáveis para imnplementar updates periódicos de localização
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest : LocationRequest
    private val newWordActivityRequestCode = 1

    override fun onCreateOptionsMenu(menu2: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu2, menu2)
        return true
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)


        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT)

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
                var loc = LatLng(lastLocation.latitude, lastLocation.longitude)

                //mMap.addMarker(MarkerOptions().position(loc).title("Marker"))
             //   findViewById<TextView>(R.id.Coordenadas).setText("Lat: " +loc.latitude + " - Long: " + loc.longitude)

              //  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15.0f))



                //reversegeocoding
                val address = getAddress(lastLocation.latitude, lastLocation.longitude)
               findViewById<TextView>(R.id.Morada).setText("Morada: " + address)


               // Log.d("*** RUI","new location received - " +loc.latitude + " - " +loc.longitude)
            }
        }

        //call the service and add markers
        val request= ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getProblem()
        var position: LatLng

       call.enqueue(object : retrofit2.Callback<List<Problem>> {
           override fun onResponse(
               call: retrofit2.Call<List<Problem>>,
               response: retrofit2.Response<List<Problem>>
           ) {
               if (response.isSuccessful) {
                   problems = response.body()!!
                   for (Problem in problems) {


                       position = LatLng(
                           Problem.lat.toDouble(),
                           Problem.lng.toDouble()

                       )
                       mMap.addMarker(

                           MarkerOptions().position(position)
                               .title("Descrição do problema: " + Problem.descr.toString() + "; UserID: " + Problem.user_id)
                               .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))


                       )


                   }

               }
           }

           override fun onFailure(call: retrofit2.Call<List<Problem>>, t: Throwable) {
               Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
           }
       })
        createLocationRequest()

    }

   private fun startLocationUpdates(){
       if(ActivityCompat.checkSelfPermission(
               this,
               android.Manifest.permission.ACCESS_FINE_LOCATION
           ) != PackageManager.PERMISSION_GRANTED){
           ActivityCompat.requestPermissions(
               this,
               arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
               LOCATION_PERMISSION_REQUEST_CODE
           )
           return
       }

       fusedLocationClient.requestLocationUpdates(
           locationRequest,
           locationCallback,
           null /*Looper*/
       )

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
        Log.d("*** RUI", "onResume - startLocationUpdates")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        /*  val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/

        setUpMap()

    }


    fun onMarkerClick(marker: Marker): Boolean {
        // TODO Auto-generated method stub
        if (marker.equals(marker_1)) {
            Log.w("Click", "test")
            return true
        }
        return false
    }
    companion object{
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1          // add to implement location periodic updates
        private const val REQUEST_CHECK_SETTINGS = 2

    }

    fun setUpMap(){

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        } else {

            //1
            mMap.isMyLocationEnabled = true

            //2
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                //Got know last location. In some rare situation it can return null.«

                if (location != null){
                    lastLocation = location
                    //Toast.makeText(this@MapsActivity, lastLocation.toString(), Toast.LENGTH_SHORT).show()
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                 }
                }
            }

        }

    private fun getAddress(lat: Double, lng: Double):String{
        val geocoder= Geocoder(this)
        val list= geocoder.getFromLocation(lat, lng, 1)
        return list[0].getAddressLine(0)
    }

    fun calculateDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lng1, lat2, lng2, results)
        //distância em metros
        return results[0]

    }


    //Quando clica num dos botões do menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            //logout
            R.id.btn1 -> {
                val sharedPref: SharedPreferences = getSharedPreferences(
                    getString(R.string.preference_file_key),
                    Context.MODE_PRIVATE
                )
                with(sharedPref.edit()) {
                    putBoolean(getString(R.string.automatic_login), false)
                    putString(getString(R.string.username_login), null)
                    // putString(getString(R.string.password_login), null )
                    commit()
                }

                //volta a atividade login (Main)
                val intent = Intent(this@MapsActivity, Login::class.java)
                startActivity(intent)
                finish()
                true
            }

            //Quando clica para adicionar um novo marker
            R.id.add_marker -> {
                val intent = Intent(this@MapsActivity, RequestProblem::class.java)
                startActivityForResult(intent, newWordActivityRequestCode)
                true
            }

            R.id.filter_user -> {
                val intent = Intent(this@MapsActivity, Request_UserID::class.java)
                startActivityForResult(intent, newWordActivityRequestCode2)
                true

            }

            R.id.filter_distance -> {
                val intent = Intent(this@MapsActivity, Request_Distance::class.java)
                startActivityForResult(intent, newWordActivityRequestCode3)
                true
            }


            else -> super.onOptionsItemSelected(item)
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            Descr = data?.getStringExtra(RequestProblem.EXTRA_REPLY)!!

            val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
            )
            val Value = sharedPref.getInt(getString(R.string.id_login), 0)

            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.insert(
                Descr,
                lastLocation.latitude.toString(),
                lastLocation.longitude.toString(),
                Value
            )

            call.enqueue(object : Callback<OutputPost2> {
                override fun onResponse(call: Call<OutputPost2>, response: Response<OutputPost2>) {
                    if (response.isSuccessful) {
                        val c: OutputPost2 = response.body()!!
                        Toast.makeText(
                            this@MapsActivity,
                            "Inserido com sucesso",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<OutputPost2>, t: Throwable) {
                    Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                }
            })


        }   else if (requestCode == newWordActivityRequestCode2 && resultCode == Activity.RESULT_OK) {
                User_ID = data?.getIntExtra(Request_UserID.EXTRA_REPLY, 0)!!

            var position: LatLng
            var position2: LatLng

                val request = ServiceBuilder.buildService(EndPoints::class.java)
                val call = request.getProblemByID(User_ID)

                call.enqueue(object : Callback<List<Problem>> {

                    override fun onResponse(
                        call: Call<List<Problem>>,
                        response: Response<List<Problem>>
                    ) {
                        if (response.isSuccessful) {
                            mMap.clear();
                            problems = response.body()!!
                            for (problem in problems) {
                                if (problem.user_id == R.string.id_login) {
                                    position = LatLng(
                                        problem.lat.toDouble(),
                                        problem.lng.toDouble()
                                    )
                                    mMap.addMarker(
                                        MarkerOptions().position(position)
                                            .title(//"Coordenadas: " + problem.lat + " - " + problem.lng +
                                                "Tipo de problema:" + problem.descr
                                            )
                                            .icon(
                                                BitmapDescriptorFactory.defaultMarker(
                                                    BitmapDescriptorFactory.HUE_GREEN
                                                )
                                            )
                                    )
                                } else {

                                    position2 = LatLng(
                                        problem.lat.toDouble(),
                                        problem.lng.toDouble()
                                    )


                                    mMap.addMarker(
                                        MarkerOptions().position(position2)
                                            .title(//"Coordenadas: " + problem.lat + " - " + problem.lng +
                                                "Tipo de problema:" + problem.descr
                                            )
                                            .icon(
                                                BitmapDescriptorFactory.defaultMarker(
                                                    BitmapDescriptorFactory.HUE_BLUE
                                                )
                                            )
                                    )
                                }
                            }
                            createLocationRequest()
                        }
                    }

                    override fun onFailure(call: Call<List<Problem>>, t: Throwable) {
                        Toast.makeText(this@MapsActivity, R.string.NoMarker, Toast.LENGTH_SHORT)
                            .show()
                        mMap.clear();
                    }
                })
            } else if (requestCode == newWordActivityRequestCode3 && resultCode == Activity.RESULT_OK) {

                val insertvalue = data?.getDoubleExtra(RequestProblem.EXTRA_REPLY, 0.0)!!
                val metros = insertvalue * 1000

                mMap.clear();
                val request = ServiceBuilder.buildService(EndPoints::class.java)
                val call = request.getProblem()
                var position: LatLng

                call.enqueue(object : Callback<List<Problem>> {
                    override fun onResponse(
                        call: Call<List<Problem>>,
                        response: Response<List<Problem>>
                    ) {
                        if (response.isSuccessful) {
                            problems = response.body()!!
                            for (problem in problems) {
                                val dist = calculateDistance(
                                    lastLocation.latitude,
                                    lastLocation.longitude,
                                    problem.lat.toDouble(),
                                    problem.lng.toDouble()
                                )

                                Toast.makeText(
                                    this@MapsActivity,
                                    "Distância: " + dist,
                                    Toast.LENGTH_SHORT
                                ).show()
                                /*findViewById<TextView>(R.id.Distância).setText("Distância: " + calculateDistance(
                                    lastLocation.latitude, lastLocation.longitude,
                                    problem.lat.toDouble(),
                                    problem.lng.toDouble()).toString())*/

                                if (dist < metros) {
                                    position = LatLng(
                                        problem.lat.toDouble(),
                                        problem.lng.toDouble()
                                    )
                                    mMap.addMarker(
                                        MarkerOptions().position(position)
                                            .title("Coordenadas: " + problem.lat + " - " + problem.lng + "/ Tipo de problema:" + problem.descr)
                                            .icon(
                                                BitmapDescriptorFactory.defaultMarker(
                                                    BitmapDescriptorFactory.HUE_AZURE
                                                )
                                            )
                                    )
                                }
                            }
                            createLocationRequest()
                        }
                    }

                    override fun onFailure(call: Call<List<Problem>>, t: Throwable) {
                        Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })

            } else {
                Toast.makeText(
                    applicationContext, "@id/Empty", Toast.LENGTH_LONG
                ).show()
            }
        }

    override fun onSensorChanged(event: SensorEvent?) {
        try {
            if (event!!.values[0] < 30 && isRunning == false) {
                isRunning = true
                val success: Boolean = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.maptheme
                    )
                )
            } else{
                isRunning = false
            }
        } catch (e: Exception) {

        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

}