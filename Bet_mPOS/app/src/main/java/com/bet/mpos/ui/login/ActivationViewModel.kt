package com.bet.mpos.ui.login

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bet.mpos.BuildConfig
import com.bet.mpos.BetApp
import com.bet.mpos.R
import com.bet.mpos.util.ESharedPreferences
import com.bet.mpos.util.Functions
import com.zoop.pos.Zoop
import com.zoop.pos.plugin.DashboardConfirmationResponse
import com.zoop.pos.plugin.DashboardThemeResponse
import com.zoop.pos.plugin.DashboardTokenResponse
import com.zoop.pos.plugin.ZoopFoundationPlugin
import com.zoop.pos.plugin.smartpos.SmartPOSPlugin
import com.zoop.pos.type.Callback
import com.zoop.pos.type.Environment
import com.zoop.pos.type.LogLevel
import com.zoop.pos.type.Request
//import com.zoop.sdk.Zoop
//import com.zoop.sdk.api.Callback
//import com.zoop.sdk.api.Environment
//import com.zoop.sdk.api.LogLevel
//import com.zoop.sdk.api.Request
//import com.zoop.sdk.plugin.DashboardConfirmationResponse
//import com.zoop.sdk.plugin.DashboardThemeResponse
//import com.zoop.sdk.plugin.DashboardTokenResponse
//import com.zoop.sdk.plugin.ZoopFoundationPlugin
//import com.zoop.sdk.plugin.smartpos.SmartPOSPlugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActivationViewModel : ViewModel(){

    private var loginRequest: Request? = null
    private var activity: ActivationActivity? = null

    private val _nextScreen = MutableLiveData<Boolean>()
    private val _loading = MutableLiveData<Boolean>()
    private val _userMessage = MutableLiveData<String>()
    private val _conectionError = MutableLiveData<String>()

    var nextScreen: LiveData<Boolean> = _nextScreen
    var loading: LiveData<Boolean> = _loading
    var userMessage: LiveData<String> = _userMessage
    var conectionError: LiveData<String> = _conectionError

    fun start(activity: ActivationActivity) {
        this.activity = activity
    }
    fun clkLogin() {
        if(Functions.isConnected()) {

            _loading.postValue(true)

            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.IO) {
                    initializeSDK()
                    activateTerminal()
                }
            }

        }else{
            _conectionError.postValue(activity?.getString(R.string.error_conection_message))
        }
    }

    fun clkCancel() {
        loginRequest?.cancel()
    }

    fun checkZoopLogin(): Boolean {
        val esp = ESharedPreferences.getInstance(BuildConfig.FILE_NAME_AD, BuildConfig.MASTER_KEY_ALIAS_AD)
        val access = esp.getString(BetApp.getAppContext().getString(R.string.saved_access_key), "")

        return access != ""
    }

    fun activateTerminal() {
        val esp = ESharedPreferences.getInstance(BuildConfig.FILE_NAME_AD, BuildConfig.MASTER_KEY_ALIAS_AD)
        val editor:SharedPreferences.Editor = esp.edit()

        loginRequest = ZoopFoundationPlugin.createDashboardActivationRequestBuilder()
            .tokenCallback(object : Callback<DashboardTokenResponse>() {
                override fun onStart() {
                    _userMessage.postValue("Requisitando token")
                }

                override fun onFail(error: Throwable) {
                    _userMessage.postValue("Falha ao requisitar token")
                }

                override fun onSuccess(response: DashboardTokenResponse) {
                    _userMessage.postValue("Token: ${response.token}")
                }
            })
            .confirmCallback(object : Callback<DashboardConfirmationResponse>() {
                override fun onFail(error: Throwable) {
                    _loading.postValue(false)
                    _userMessage.postValue("Operação cancelada")
                }

                override fun onSuccess(response: DashboardConfirmationResponse) {
                    editor.putString(BetApp.getAppContext().getString(R.string.saved_marketplace_id), response.credentials.marketplace)
                    editor.putString(BetApp.getAppContext().getString(R.string.saved_seller_id), response.credentials.seller)
                    editor.putString(BetApp.getAppContext().getString(R.string.saved_terminal), response.credentials.terminal)
                    editor.putString(BetApp.getAppContext().getString(R.string.saved_access_key), response.credentials.accessKey)
                    editor.putString(BetApp.getAppContext().getString(R.string.saved_seller_name), response.owner.name)
                    editor.apply()
                    println(response.credentials.toString())
                }
            })
            .themeCallback(object : Callback<DashboardThemeResponse>() {
                override fun onStart() {
                    _userMessage.postValue("Baixando temas")
                }

                override fun onFail(error: Throwable) {
                    _userMessage.postValue("Erro no download de temas")
                }

                override fun onSuccess(response: DashboardThemeResponse) {
                    _userMessage.postValue("Login realizado")
                    _nextScreen.postValue(true)
                }

            }).build()
        Zoop.post(loginRequest!!)
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

    fun initializeWithCredentials(){
        val esp = ESharedPreferences.getInstance(BuildConfig.FILE_NAME_AD, BuildConfig.MASTER_KEY_ALIAS_AD)
        val _marketplace = esp.getString(BetApp.getAppContext().getString(R.string.saved_marketplace_id), null)
        val _seller = esp.getString(BetApp.getAppContext().getString(R.string.saved_seller_id), null)
        val _terminal = esp.getString(BetApp.getAppContext().getString(R.string.saved_terminal), null)
        val _accessKey = esp.getString(BetApp.getAppContext().getString(R.string.saved_access_key), null)

        if (_marketplace != null && _seller != null && _terminal != null && _accessKey != null) {
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

            Zoop.findPlugin<SmartPOSPlugin>() ?: Zoop.make<SmartPOSPlugin>().run(Zoop::plug)

            _nextScreen.postValue(true)
        }
    }

    fun initializeWithCredentialsDebug(){

        Zoop.initialize(BetApp.getAppContext()) {
            credentials {
                marketplace = "e837e49d5f5b419db79f65f575dc9bee"
                seller = "061420cf1204425194f22eb74414e85f"
                //terminal = "1491901580"
                accessKey = "0739e4d9-aaeb-4074-a712-76f5a57e8d18"
            }
        }
        Zoop.setEnvironment(Environment.Production)
        Zoop.setLogLevel(LogLevel.Trace)
        Zoop.setStrict(false)
        Zoop.setTimeout(15 * 1000L)

        Zoop.findPlugin<SmartPOSPlugin>() ?: Zoop.make<SmartPOSPlugin>().run(Zoop::plug)

        _nextScreen.postValue(true)
    }

}