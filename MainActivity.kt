package com.termux.controlcenter

import android.content.Intent
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.termux.controlcenter.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
        handleUsbIntent(intent)
    }
    
    private fun setupUI() {
        setAppLanguage()
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.app_name)
        
        setupBottomNavigation()
        showDashboardFragment()
    }
    
    private fun setAppLanguage() {
        val sharedPrefs = getSharedPreferences("AppSettings", MODE_PRIVATE)
        val language = sharedPrefs.getString("app_language", "mr") ?: "mr"
        
        val resources = resources
        val configuration = resources.configuration
        val locale = when (language) {
            "mr" -> Locale("mr", "IN")
            else -> Locale.ENGLISH
        }
        
        Locale.setDefault(locale)
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
    
    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    showDashboardFragment()
                    true
                }
                R.id.nav_usb -> {
                    showUsbFragment()
                    true
                }
                R.id.nav_storage -> {
                    showStorageFragment()
                    true
                }
                R.id.nav_network -> {
                    showNetworkFragment()
                    true
                }
                R.id.nav_monitor -> {
                    showMonitorFragment()
                    true
                }
                else -> false
            }
        }
    }
    
    private fun showDashboardFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, DashboardFragment())
            .commit()
        updateToolbarTitle(getString(R.string.title_dashboard))
    }
    
    private fun showUsbFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, UsbManagerFragment())
            .commit()
        updateToolbarTitle(getString(R.string.title_usb_manager))
    }
    
    private fun showStorageFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, StorageFragment())
            .commit()
        updateToolbarTitle(getString(R.string.title_storage))
    }
    
    private fun showNetworkFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, NetworkToolsFragment())
            .commit()
        updateToolbarTitle(getString(R.string.title_network))
    }
    
    private fun showMonitorFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SystemMonitorFragment())
            .commit()
        updateToolbarTitle(getString(R.string.title_monitor))
    }
    
    private fun updateToolbarTitle(title: String) {
        supportActionBar?.title = title
    }
    
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_language -> {
                toggleLanguage()
                true
            }
            R.id.menu_settings -> {
                showSettingsFragment()
                true
            }
            R.id.menu_about -> {
                showAboutDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    private fun showSettingsFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SettingsFragment())
            .addToBackStack(null)
            .commit()
        updateToolbarTitle(getString(R.string.title_settings))
    }
    
    private fun toggleLanguage() {
        val sharedPrefs = getSharedPreferences("AppSettings", MODE_PRIVATE)
        val currentLang = sharedPrefs.getString("app_language", "mr") ?: "mr"
        val newLang = if (currentLang == "mr") "en" else "mr"
        
        sharedPrefs.edit().putString("app_language", newLang).apply()
        recreate()
    }
    
    private fun showAboutDialog() {
        android.app.AlertDialog.Builder(this)
            .setTitle(getString(R.string.about))
            .setMessage("Termux Control Center v1.0.0\nBugjaeger Replacement App")
            .setPositiveButton("OK", null)
            .show()
    }
    
    private fun handleUsbIntent(intent: Intent) {
        if (intent.action == UsbManager.ACTION_USB_DEVICE_ATTACHED) {
            showMessage(getString(R.string.usb_device_connected))
        }
    }
    
    private fun showMessage(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }
}
