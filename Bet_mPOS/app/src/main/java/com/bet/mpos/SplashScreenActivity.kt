package com.bet.mpos

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.bet.mpos.util.ESharedPreferences
//import com.zoop.sdk.Zoop
//import com.zoop.sdk.api.Environment
//import com.zoop.sdk.api.LogLevel
//import com.zoop.sdk.plugin.smartpos.SmartPOSPlugin
import com.zoop.pos.Zoop
import com.zoop.pos.plugin.smartpos.SmartPOSPlugin
import com.zoop.pos.type.Environment
import com.zoop.pos.type.LogLevel

class SplashScreenActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        initializeWithCredentialsDebug()

//        if(checkZoopLogin()) {
//            initializeWithCredentials()
//            startActivity(MainActivity::class.java)
//        }
//        else
//            startActivity(LoginActivity::class.java)
    }


    private fun startActivity(activity: Class<*>?) {

        Handler().postDelayed(Runnable {
            val i = Intent(this, activity)
            startActivity(i)
//            finish()
        }, 1500)
    }

    fun checkZoopLogin(): Boolean {
        val esp = ESharedPreferences.getInstance(BuildConfig.FILE_NAME_AD, BuildConfig.MASTER_KEY_ALIAS_AD)
        val access = esp.getString(BetApp.getAppContext().getString(R.string.saved_access_key), "")

        return access != ""
    }

    fun initializeWithCredentials(){
        val esp = ESharedPreferences.getInstance(BuildConfig.FILE_NAME_AD, BuildConfig.MASTER_KEY_ALIAS_AD)
        val _marketplace = esp.getString(BetApp.getAppContext().getString(R.string.saved_marketplace_id), null)
        val _seller = esp.getString(BetApp.getAppContext().getString(R.string.saved_seller_id), null)
        //val _terminal = esp.getString(BetApp.getAppContext().getString(R.string.saved_terminal), null)
        val _accessKey = esp.getString(BetApp.getAppContext().getString(R.string.saved_access_key), null)

        //println("market: ${_marketplace} seller: ${_seller} terminal: ${_terminal} accesskey: ${_accessKey}")
        println("market: ${_marketplace} seller: ${_seller} accesskey: ${_accessKey}")
        if (_marketplace != null && _seller != null && _accessKey != null) {
            Zoop.initialize(BetApp.getAppContext()) {
                credentials {
                    marketplace = _marketplace
                    seller = _seller
                    //terminal = _terminal
                    accessKey = _accessKey
                }
            }
            Zoop.setEnvironment(Environment.Production)
            Zoop.setLogLevel(LogLevel.Trace)
            Zoop.setStrict(false)
            Zoop.setTimeout(15 * 1000L)

            val smartPOSPlugin = Zoop.make<SmartPOSPlugin>()
            Zoop.plug(smartPOSPlugin)

            Zoop.findPlugin<SmartPOSPlugin>() ?: Zoop.make<SmartPOSPlugin>().run(Zoop::plug)
        }
    }

    fun initializeWithCredentialsDebug(){

        Zoop.initialize(BetApp.getAppContext()) {
            credentials {
                marketplace = "e837e49d5f5b419db79f65f575dc9bee"
                seller = "061420cf1204425194f22eb74414e85f"
                //terminal = "1492203195"
                accessKey = "94f31503-0c18-4e90-b436-cf447b1fa182"
            }
        }
        Zoop.setEnvironment(Environment.Production)
        Zoop.setLogLevel(LogLevel.Trace)
        Zoop.setStrict(false)
        Zoop.setTimeout(15 * 1000L)

        val smartPOSPlugin = Zoop.make<SmartPOSPlugin>()
        Zoop.plug(smartPOSPlugin)

        Zoop.findPlugin<SmartPOSPlugin>() ?: Zoop.make<SmartPOSPlugin>().run(Zoop::plug)

        startActivity(MainActivity::class.java)
    }

    override fun onBackPressed() {
//        super.onBackPressed()
    }

    override fun onStart() {
        super.onStart()

        initializeWithCredentialsDebug()

//        if(checkZoopLogin()) {
//            initializeWithCredentials()
//            startActivity(MainActivity::class.java)
//        }
//        else
//            startActivity(LoginActivity::class.java)
    }
}