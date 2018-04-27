package com.hitchpeak.keystone.screens

//import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.hitchpeak.keystone.R
import com.hitchpeak.keystone.appServices.LocationShareService
import com.hitchpeak.keystone.screens.home.HomeFragment
import com.hitchpeak.keystone.screens.settings.SettingsFragment
import com.hitchpeak.keystone.utils.HttpClient
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        navigate(R.id.nav_home)

        // Initialize http client:
        HttpClient.init(applicationContext)

        // Start location sharing TODO("base starting location sharing on setting")
        startService(Intent(this, LocationShareService::class.java))


        // TODO: TESTING
        // It seems that we have a problem here
        println(HttpClient.baseUrl)
//        val testString = HttpClient.instance!!.getForObject(HttpClient.baseUrl + HttpClient.getEndpoint, String::class.java)
//        println(testString)
    }

    override fun onBackPressed() {

        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun navigate(id: Int) {
        val screenInstance = when (id) {
            R.id.nav_home -> HomeFragment()
            R.id.nav_settings -> SettingsFragment()
            else -> HomeFragment()
        }

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.content_container, screenInstance)
                .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        navigate(item.itemId)
        drawer_layout.closeDrawer(GravityCompat.START)

        return true
    }
}
