package com.example.app_pm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import android.widget.Toast
import com.example.app_pm.api.EndPoints
import com.example.app_pm.api.ServiceBuilder
import com.example.app_pm.api.User
import com.google.android.gms.common.api.Response
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var users: List<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
      /*  val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/
    }
}