package com.bet.mpos

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.os.PowerManager.WakeLock
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.bet.mpos.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var wakeLock: WakeLock
    private val REQUIRED_PERMISSIONS =
        mutableListOf (
            Manifest.permission.CAMERA,
            //Manifest.permission.RECORD_AUDIO
        ).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    private val REQUEST_CODE_PERMISSIONS = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        showNavigatinoHeaderData()
        binding.appBarMain.toolbar.foreground = AppCompatResources.getDrawable(BetApp.getAppContext(), R.drawable.ic_logo_small_white)
        binding.appBarMain.toolbar.foregroundGravity = Gravity.CENTER
        //binding.appBarMain.toolbar.backgroundTintList = ColorStateList.valueOf(getColor(R.color.white))
        binding.mainLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        val drawerLayout: DrawerLayout = binding.mainLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
//                R.id.nav_bank_slip,
                R.id.nav_report,
                R.id.nav_receipt_reprint,
                R.id.nav_update,
                R.id.nav_ajusts,
                R.id.productObjectFragment,
                R.id.serviceFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        if (!allPermissionsGranted())
        {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }



    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            this.baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun showNavigatinoHeaderData() {
        // ------------ parte de mostrar as informações encriptadas no Navigation

        val fantasyName : TextView = binding.navView.getHeaderView(0).findViewById(R.id.tv_fantasy_name)
        val corporateReason : TextView = binding.navView.getHeaderView(0).findViewById(R.id.tv_corporate_reason)
        val document : TextView = binding.navView.getHeaderView(0).findViewById(R.id.tv_document)

        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.authentication()

        viewModel.fantasyName.observe(this) {
            if(it != "") {
                fantasyName.text = it
            }
            else{
                val layoutSecond: LinearLayout = binding.navView.getHeaderView(0).findViewById(R.id.layout_second)
                val layoutFantasyName: ConstraintLayout = binding.navView.getHeaderView(0).findViewById(R.id.layout_fantasy_name)
//                val layoutDocument: ConstraintLayout = binding.navView.getHeaderView(0).findViewById(R.id.layout_fantasy_name)

                layoutFantasyName.visibility = View.GONE
                layoutSecond.dividerPadding = 20
            }
        }
        viewModel.corporateReason.observe(this) {
            corporateReason.text = it
        }
        viewModel.document.observe(this) {
            document.text = it
        }
        // ----------
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.activity_main_drawer, menu)
//        MenuCompat.setGroupDividerEnabled(menu, true)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//
//        when(item.itemId){
//            R.id.item_logout -> {
//                println("Logout")
//                return true
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        when(item.itemId){
//            R.id.item_logout -> {
//                val esp = ESharedPreferences.getInstance(
//                    BuildConfig.FILE_NAME_AD,
//                    BuildConfig.MASTER_KEY_ALIAS_AD
//                )
//                val editor: SharedPreferences.Editor = esp.edit()
//                editor.putString(
//                    PixcredApp.getAppContext().getString(R.string.saved_access_key),
//                    ""
//                )
//                editor.apply()
//                finish()
//                return true
//            }
//        }
//        return false
//    }

    override fun onBackPressed() {
//        super.onBackPressed()
    }

}