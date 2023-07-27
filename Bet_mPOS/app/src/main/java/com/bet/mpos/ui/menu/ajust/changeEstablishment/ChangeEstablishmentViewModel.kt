package com.bet.mpos.ui.menu.ajust.changeEstablishment

import android.content.SharedPreferences
import android.os.CountDownTimer
import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.bet.mpos.BuildConfig
import com.bet.mpos.BetApp
import com.bet.mpos.R
import com.bet.mpos.util.ESharedPreferences
import com.zoop.sdk.Zoop
import com.zoop.sdk.api.Callback
import com.zoop.sdk.api.Environment
import com.zoop.sdk.api.LogLevel
import com.zoop.sdk.api.Request
import com.zoop.sdk.plugin.DashboardConfirmationResponse
import com.zoop.sdk.plugin.DashboardThemeResponse
import com.zoop.sdk.plugin.DashboardTokenResponse
import com.zoop.sdk.plugin.ZoopFoundationPlugin
import com.zoop.sdk.plugin.smartpos.SmartPOSPlugin

class ChangeEstablishmentViewModel : ViewModel() {

    private var loginRequest: Request? = null
    private var navController: NavController? = null
    private lateinit var countDownTimer: CountDownTimer

    private val _userMessage = MutableLiveData<String>()
    private val _timer = MutableLiveData<String>()
    private val _success = MutableLiveData<Boolean>()

    var userMessage: LiveData<String> = _userMessage
    var timer: LiveData<String> = _timer
    var success: LiveData<Boolean> = _success

    fun startActivation(findNavController: NavController) {
        navController = findNavController

        initializeSDK()
        activateTerminal()
        startTimer()
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
                clkCancel(true)
            }
        }

        // Iniciar o timer
        countDownTimer.start()
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
//                    _userMessage.postValue("Baixando temas")
                }

                override fun onFail(error: Throwable) {
                    _userMessage.postValue("Erro no download de temas")
                }

                override fun onSuccess(response: DashboardThemeResponse) {
                    _userMessage.postValue("Troca realizada")
                    Handler().postDelayed(Runnable {
                        navController?.navigateUp()
                    }, 2000)
                }

            }).build()
        Zoop.post(loginRequest!!)
    }

    fun clkCancel(navigateUp: Boolean) {
        loginRequest?.cancel()
        countDownTimer.cancel()
//        _timer.value = "Operação cancelada"

        if(navigateUp)
            Handler().postDelayed(Runnable {
                navController?.navigateUp()
            }, 2000)
    }
}