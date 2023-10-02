package com.bet.mpos.ui.login.apnConfiguration

import android.os.Handler
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.pax.dal.entity.ApnInfo
import com.bet.mpos.BetApp
import com.bet.mpos.R
import com.zoop.pos.Zoop
import com.zoop.pos.plugin.smartpos.SmartPOSPlugin
import com.zoop.pos.terminal.PublicDevice
import com.zoop.pos.type.Callback
import com.zoop.pos.type.Environment
import com.zoop.pos.type.LogLevel
//import com.zoop.sdk.Zoop
//import com.zoop.sdk.api.Callback
//import com.zoop.sdk.api.Environment
//import com.zoop.sdk.api.LogLevel
//import com.zoop.sdk.api.terminal.PublicDevice
//import com.zoop.sdk.plugin.smartpos.SmartPOSPlugin
import java.lang.ref.WeakReference

class ApnConfigurationViewModel : ViewModel() {

    fun clickConfirm(
        name: String,
        host: String,
        user: String,
        password: String,
        authType: String,
        navController: NavController
    ) {
        initializeSDK()
        println("${name} ${host} ${user} ${password} ${authType}")

        if(name != "" && host != "" && user != "" && password != "") {
            var type: Int = 0
            when (authType) {
                "NONE" -> type = 0
                "PAP" -> type = 1
                "CHAP" -> type = 2
                "PAP or CHAP" -> type = 3
            }
            setApn(name, host, user, password, type, navController)
        }
    }

    private fun setApn(
        name: String,
        host: String,
        user: String,
        password: String,
        authType: Int,
        navController: NavController
    ) {
    SmartPOSPlugin.createPublicDeviceRequestBuilder()
        .callback(object : Callback<WeakReference<PublicDevice>>() {
            override fun onFail(error: Throwable) {
            }

            override fun onSuccess(response: WeakReference<PublicDevice>) {
                response.get()?.setApn(com.zoop.pos.terminal.System.ApnCredentials(
                    name, host, user, password, authType
                ))
            }

        }).build().run { Zoop.post(this) }

        println(ApnInfo().apn)

        Handler().postDelayed(Runnable {
            navController.navigate(R.id.action_apnConfigurationFragment_to_homeLoginFragment)
        }, 1000)


    }

    fun initializeSDK(){
        Zoop.initialize(BetApp.getAppContext())
        Zoop.setEnvironment(Environment.Production)
        Zoop.setLogLevel(LogLevel.Trace)
        Zoop.setStrict(false)
        Zoop.setTimeout(15 * 1000L)

        val smartPOSPlugin = Zoop.make<SmartPOSPlugin>()
        Zoop.plug(smartPOSPlugin)

        Zoop.findPlugin<SmartPOSPlugin>() ?: Zoop.make<SmartPOSPlugin>().run(Zoop::plug)
    }

}