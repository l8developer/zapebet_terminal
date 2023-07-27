package com.bet.mpos.ui.login.enable

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.bet.mpos.BuildConfig
import com.bet.mpos.MainActivity
import com.bet.mpos.BetApp
import com.bet.mpos.R
import com.bet.mpos.util.ESharedPreferences
import com.bet.mpos.util.Functions
import com.zoop.sdk.Zoop
import com.zoop.sdk.api.Callback
import com.zoop.sdk.api.Environment
import com.zoop.sdk.api.LogLevel
import com.zoop.sdk.api.Request
import com.zoop.sdk.api.terminal.PublicDevice
import com.zoop.sdk.plugin.DashboardConfirmationResponse
import com.zoop.sdk.plugin.DashboardThemeResponse
import com.zoop.sdk.plugin.DashboardTokenResponse
import com.zoop.sdk.plugin.ZoopFoundationPlugin
import com.zoop.sdk.plugin.smartpos.SmartPOSPlugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference

class EnableViewModel : ViewModel() {

    private var loginRequest: Request? = null
    private var activity: Activity? = null
    private var navController: NavController? = null
    private lateinit var countDownTimer: CountDownTimer

    private val _userMessage = MutableLiveData<String>()
    private val _conectionError = MutableLiveData<String>()
    private val _timer = MutableLiveData<String>()

    var userMessage: LiveData<String> = _userMessage
    var conectionError: LiveData<String> = _conectionError
    var timer: LiveData<String> = _timer

    fun startActivation(fragmentActivity: Activity, navController: NavController) {
        if(Functions.isConnected()) {
            activity = fragmentActivity
            this.navController = navController
//            initializeWithCredentialsDebug()

            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.IO) {
                    initializeSDK()
                    activateTerminal()
                }
            }
            startTimer()

        }else{
            _conectionError.postValue(activity?.getString(R.string.error_conection_message))
        }
    }

    fun clkCancel() {
        _userMessage.postValue("Cancelando")
        loginRequest?.cancel()
        navController?.navigateUp()
        countDownTimer.cancel()
        _timer.value = "Operação cancelada"
    }

    fun checkZoopLogin(): Boolean {
        val esp = ESharedPreferences.getInstance(BuildConfig.FILE_NAME_AD, BuildConfig.MASTER_KEY_ALIAS_AD)
        val access = esp.getString(BetApp.getAppContext().getString(R.string.saved_access_key), "")

        return access != ""
    }

    fun activateTerminal() {
        val esp = ESharedPreferences.getInstance(BuildConfig.FILE_NAME_AD, BuildConfig.MASTER_KEY_ALIAS_AD)
        val editor: SharedPreferences.Editor = esp.edit()

        loginRequest = ZoopFoundationPlugin.createDashboardActivationRequestBuilder()
            .tokenCallback(object : Callback<DashboardTokenResponse>() {
                override fun onStart() {
                    _userMessage.postValue("Requisitando token")
                }

                override fun onFail(error: Throwable) {
                    _userMessage.postValue("Falha ao requisitar token")
                }

                override fun onSuccess(response: DashboardTokenResponse) {
                    _userMessage.postValue(response.token)
                }
            })
            .confirmCallback(object : Callback<DashboardConfirmationResponse>() {
                override fun onFail(error: Throwable) {
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
                    setAPN()
                    startActivity(MainActivity::class.java)
                }

            }).build()
        Zoop.post(loginRequest!!)
    }

    fun setAPN() {
        //APN AUTO
        SmartPOSPlugin.createPublicDeviceRequestBuilder()
            .callback(object : Callback<WeakReference<PublicDevice>>() {
                override fun onFail(error: Throwable) {
                }

                override fun onSuccess(response: WeakReference<PublicDevice>) {
                    response.get()?.setApn(com.zoop.sdk.api.terminal.System.ApnCredentials(
                        "allcom",
                        "allcom.vivo.com.br",
                        "allcom",
                        "allcom",
                        3
                    ))
                }

            }).build().run { Zoop.post(this) }
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
                    terminal = _terminal
                    accessKey = _accessKey
                }
            }
            Zoop.setEnvironment(Environment.Production)
            Zoop.setLogLevel(LogLevel.Trace)
            Zoop.setStrict(false)
            Zoop.setTimeout(15 * 1000L)

            Zoop.findPlugin<SmartPOSPlugin>() ?: Zoop.make<SmartPOSPlugin>().run(Zoop::plug)

            startActivity(MainActivity::class.java)
        }
    }

    fun initializeWithCredentialsDebug(){

        Zoop.initialize(BetApp.getAppContext()) {
            credentials {
                marketplace = "e837e49d5f5b419db79f65f575dc9bee"
                seller = "061420cf1204425194f22eb74414e85f"
                terminal = "1491901580"
                accessKey = "0739e4d9-aaeb-4074-a712-76f5a57e8d18"
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

    fun startTimer(){
        // Definir a duração total do timer em milissegundos
        val durationMillis: Long = 300000 // 5 minuto
//        val durationMillis: Long = 10000 // 10 seconds
        // Definir o intervalo entre as atualizações do timer em milissegundos
        val intervalMillis: Long = 1000 // 1 segundo

        // Inicializar o CountDownTimer
        countDownTimer = object : CountDownTimer(durationMillis, intervalMillis) {
            override fun onTick(millisUntilFinished: Long) {
                // Atualizar o TextView com o tempo restante
                val ms = millisUntilFinished / 1000

                val minutes = ms/60
                val seconds = ms%60

                _timer.value = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                // Ação a ser executada quando o timer terminar
                _timer.value = "00:00"
                _userMessage.postValue("Operação cancelada")
                clkCancel()
            }
        }

        // Iniciar o timer
        countDownTimer.start()
    }

    private fun startActivity(activity: Class<*>?) {

        val i = Intent(this.activity, activity) as Intent
        this.activity?.startActivity(i)
        this.activity?.finish()
    }
}