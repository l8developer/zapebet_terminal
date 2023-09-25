package com.bet.mpos.ui.bet.objectFragment

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import com.bet.mpos.BuildConfig
import com.bet.mpos.BetApp
import com.bet.mpos.R
import com.bet.mpos.adapters.AdapterProducts
import com.bet.mpos.api.APIClient
import com.bet.mpos.api.APIInterface
import com.bet.mpos.api.pojo.GameOddsResponse
import com.bet.mpos.dialogs.BetDialog
import com.bet.mpos.objects.BetDialogItem
import com.bet.mpos.objects.BetGame
import com.bet.mpos.objects.BetGamesFromLeague
import com.bet.mpos.objects.BetItem
import com.bet.mpos.objects.BetLeaguesFromCountry
import com.bet.mpos.objects.BetListItem
import com.bet.mpos.objects.OptionBet
import com.bet.mpos.util.ESharedPreferences
import com.bet.mpos.util.Functions
import retrofit2.Call
import retrofit2.Response

class BetObjectViewModel : ViewModel() {

    private var dialog : BetDialog? = null
    private lateinit var mActivity: FragmentActivity
    private lateinit var mNavController: NavController
    private var leagueNameDisplay: TextView? = null

    private val _list = MutableLiveData<ArrayList<BetItem>>().apply {}
    private val _leagueList = MutableLiveData<ArrayList<String>>().apply{}
    private val _leagueIdList = MutableLiveData<ArrayList<Int>>().apply {}
    private val _loading = MutableLiveData<Boolean>().apply {}

    var list: LiveData<ArrayList<BetItem>> = _list
    var leagueList: LiveData<ArrayList<String>> = _leagueList
    var leagueIdList: LiveData<ArrayList<Int>> = _leagueIdList
    var loading: LiveData<Boolean> = _loading

    fun start(
        navController: NavController,
        activity: FragmentActivity,
        arguments: Bundle?,
        tvLeague: TextView
    ) {
        mActivity = activity
        mNavController = navController
        leagueNameDisplay = tvLeague
        arguments?.takeIf { it.containsKey("id") }?.apply {
            val category = getString("id", "")
            Log.d("ProductObject", category)
        }
        //_loading.value = true
        loadLeaguesFromCountry("Italy")
        //getGamesAndOdds()
//        mountList()
    }

    fun loadLeaguesFromCountry(country: String)
    {
        _loading.value = true
        val retrofit = APIClient(BuildConfig.API_BET_URL).client
        val service = retrofit.create(APIInterface::class.java)
        val responseCall: Call<BetLeaguesFromCountry> = service.getLeaguesFromCountry(country)
        if (responseCall != null)
        {
            responseCall.enqueue(object : retrofit2.Callback<BetLeaguesFromCountry> {
                override fun onResponse(
                    call: Call<BetLeaguesFromCountry>?,
                    response: Response<BetLeaguesFromCountry>
                ) {
                    if (response.isSuccessful) {
                        val resp = response.body()
                        if (resp != null)
                        {
                            Log.d("leagues from country: ", resp.toString())
                            handleSuccessLeagues(resp)
                        }
                        else
                            handleFailed(BetApp.getAppContext().getString(R.string.error_generic_api))
                    } else {
                        Log.e("getGamesAndOdds: ", response.toString())
                        //_loading.value = false
                        handleFailed(BetApp.getAppContext().getString(R.string.error_generic_api))
                    }
                }

                override fun onFailure(call: Call<BetLeaguesFromCountry>, t: Throwable) {
                    Log.e("getGamesAndOdds onFailure: ", t.message.toString())
                    //_loading.value = false
                    handleFailed(BetApp.getAppContext().getString(R.string.error_generic_api))
                }
            })
        }
        _loading.value = false
    }

    private fun loadGamesFromLeague(leagueId: Int)
    {
        _loading.value = true
        val retrofit = APIClient(BuildConfig.API_BET_URL).client
        val service = retrofit.create(APIInterface::class.java)
        val responseCall: Call<BetGamesFromLeague> = service.getGamesFromLeague(leagueId)
        if (responseCall != null)
        {
            responseCall.enqueue(object : retrofit2.Callback<BetGamesFromLeague> {
                override fun onResponse(
                    call: Call<BetGamesFromLeague>?,
                    response: Response<BetGamesFromLeague>
                ) {
                    if (response.isSuccessful) {
                        val resp = response.body()
                        if (resp != null)
                        {
                            Log.d("games from league: ", resp.toString())
                            handleSuccessGames(resp)
                        }
                        else
                            handleFailed(BetApp.getAppContext().getString(R.string.error_generic_api))
                    } else {
                        Log.e("getGamesAndOdds: ", response.toString())
                        handleFailed(BetApp.getAppContext().getString(R.string.error_generic_api))
                    }
                }

                override fun onFailure(call: Call<BetGamesFromLeague>, t: Throwable) {
                    Log.e("getGamesAndOdds onFailure: ", t.message.toString())
                    handleFailed(BetApp.getAppContext().getString(R.string.error_generic_api))
                }
            })
        }
    }

//    private fun getGamesAndOdds() {
//        val retrofit = APIClient(BuildConfig.API_BET_URL).client
//        val service = retrofit.create(APIInterface::class.java)
//        val responseCall: Call<ArrayList<GameOddsResponse>> = service.getGameAndOdds(BuildConfig.ZB_TOKEN)
//        if (responseCall != null)
//        {
//            responseCall.enqueue(object : retrofit2.Callback<ArrayList<GameOddsResponse>> {
//                override fun onResponse(
//                    call: Call<ArrayList<GameOddsResponse>>?,
//                    response: Response<ArrayList<GameOddsResponse>>
//                ) {
//                    if (response.isSuccessful) {
//                        val resp = response.body()
//                        if (resp != null)
//                            handleSuccess(resp)
//                        else
//                            handleFailed(BetApp.getAppContext().getString(R.string.error_generic_api))
//                    } else {
//                        Log.e("getGamesAndOdds: ", response.toString())
//                        handleFailed(BetApp.getAppContext().getString(R.string.error_generic_api))
//                    }
//                }
//
//                override fun onFailure(call: Call<ArrayList<GameOddsResponse>>, t: Throwable) {
//                    Log.e("getGamesAndOdds onFailure: ", t.message.toString())
//                    handleFailed(BetApp.getAppContext().getString(R.string.error_generic_api))
//                }
//            })
//        }
//    }

    private fun handleFailed(errorMessage: String) {
        _loading.value = false
        Toast.makeText(BetApp.getAppContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun handleSuccessLeagues(resp: BetLeaguesFromCountry)
    {
        _loading.value = false
        var list = ArrayList<String>()
        var idList = ArrayList<Int>()
        Log.d("teste retorno", resp.league.toString())
        resp.league.forEach { league ->
            Log.d("teste retorno loop", league.name)
            list.add(league.name)
            idList.add(league.id)
        }
        Log.d("teste retorno 2", _leagueList.value.toString())

        _leagueList.value = list
        _leagueIdList.value = idList

        try{
            _leagueIdList.value?.let { loadGamesFromLeague(it[0]) }
            leagueNameDisplay!!.text = _leagueList.value?.let { it[0] }
        }
        catch(e: Exception){
            Log.e("Erro jogos", "Erro ao carregar jogos da liga")
        }

    }

    private fun handleSuccessGames(resp: BetGamesFromLeague)
    {
        _loading.value = false
        var list = ArrayList<BetItem>()

        resp.odds.forEach { game ->
            val date = game.date_init.replace("T", " ")
            list.add(BetItem(
                date.substring(0, 19),
                game.uuid,
                game.home.name + " X " + game.away.name,
                game.venue_name,
                OptionBet(
                    game.odd[0].id.toString(),
                    game.home.name,
                    game.home.logo,
                    game.odd[0].odd.toString()
                ),
                OptionBet(
                    game.odd[1].id.toString(),
                    "Empate",
                    "",
                    game.odd[1].odd.toString()
                ),
                OptionBet(
                    game.odd[2].id.toString(),
                    game.away.name,
                    game.away.logo,
                    game.odd[2].odd.toString()
                )
            ))
        }

        _list.value = list

        Log.d("teste montagem dos games", _list.value.toString())
    }
//    private fun handleSuccess(response: ArrayList<GameOddsResponse>) {
//
//        var list = ArrayList<BetItem>()
//
//        response.forEach { game ->
//            val date = game.initial_game.replace("T", " ")
//            list.add(BetItem(
//                date.substring(0, 19),
//                game.uuid,
//                game.game,
//                game.place,
//                OptionBet(
//                    game.option.get(0).uuid,
//                    game.option.get(0).team,
//                    game.option.get(0).shield,
//                    game.option.get(0).odd
//                ),
//                OptionBet(
//                    game.option.get(1).uuid,
//                    game.option.get(1).team,
//                    game.option.get(1).shield,
//                    game.option.get(1).odd
//                ),
//                OptionBet(
//                    game.option.get(2).uuid,
//                    game.option.get(2).team,
//                    game.option.get(2).shield,
//                    game.option.get(2).odd
//                )
//            ))
//        }
//
//        _list.value = list
//    }

    private fun mountList() {
//        var list = ArrayList<BetItem>()
//        list.add(BetItem(
//            "2023-07-01 16:00:00",
//            "SÃO PAULO X FLUMINENSE",
//            "Morumbi",
//            OptionBet("São Paulo", "https://logodetimes.com/times/fluminense/logo-fluminense-256.png", "2.00"),
//            OptionBet("Empate", "", "3.20"),
//            OptionBet("Fluminense", "https://logodetimes.com/times/fluminense/logo-fluminense-256.png", "3.75"),
//        ))
//        list.add(BetItem(
//            "2023-07-01 18:30:00",
//            "FLAMENGO X FORTALEZA",
//            "Maracanã",
//            OptionBet("Flamengo", "https://logodetimes.com/times/flamengo/logo-flamengo-256.png", "1.66"),
//            OptionBet("Empate", "", "3.75"),
//            OptionBet("Fortaleza", "https://logodetimes.com/times/fortaleza/logo-fortaleza-256.png", "5.00"),
//        ))
//        list.add(BetItem(
//            "2023-07-01 18:30:00",
//            "BAHIA X GRÊMIO",
//            "Itaipava Arena Fonte Nova",
//            OptionBet("Bahia", "https://logodetimes.com/times/bahia/logo-bahia-256.png", "2.40"),
//            OptionBet("Empate", "", "3.30"),
//            OptionBet("Grêmio", "https://logodetimes.com/times/gremio/logo-gremio-256.png", "2.90"),
//        ))
//
//        _list.value = list
//        saveBets(BetListItem(list))
    }

    fun adapter(adapter: AdapterProducts, list: java.util.ArrayList<BetItem>?) {

        adapter.setOnBet1ClickListener { item ->

            saveBetGame(BetGame(item.bet0.id, item.bet0.name, item.location))
            dialog = BetDialog(
                mActivity!!,
                BetDialogItem(
                item.bet0.name,
                item.title,
                Functions.int_to_double(Functions.real_to_int(item.bet0.odd)),
                "",
                0
            ))
//            dialog = BetDialog(mActivity!!, betDialogItem)
            //dialog!!.create()
            dialog!!.show()
            dialog!!.setOnFinishClickListener { item ->

                val bundle = Bundle()
                bundle.putInt("value", item.value)

                //SE UNICO
//                mNavController.navigate(R.id.action_nav_value_entry_to_betDocumentFragment, bundle)
                //SE NAO
                //mNavController.navigate(R.id.action_productObjectFragment_to_betDocumentFragment, bundle)
                mNavController.navigate(R.id.action_nav_home_to_betDocumentFragment, bundle)
            }
        }

        adapter.setOnDrawClickListener { item ->
            saveBetGame(BetGame(item.bet1.id, item.bet1.name, item.location))
            dialog = BetDialog(
                mActivity!!,
                BetDialogItem(
                    item.bet1.name,
                    item.title,
                    Functions.int_to_double(Functions.real_to_int(item.bet1.odd)),
                    "",
                    0
                ))
            //dialog!!.create()
            dialog!!.show()

            dialog!!.setOnFinishClickListener { item ->
                val bundle = Bundle()
                bundle.putInt("value", item.value)
                //SE UNICO
//                mNavController.navigate(R.id.action_nav_value_entry_to_betDocumentFragment, bundle)
                //SE NAO
                //mNavController.navigate(R.id.action_productObjectFragment_to_betDocumentFragment, bundle)
                mNavController.navigate(R.id.action_nav_home_to_betDocumentFragment, bundle)
            }
        }

        adapter.setOnBet2ClickListener { item ->
            saveBetGame(BetGame(item.bet2.id, item.bet2.name, item.location))
            dialog = BetDialog(
                mActivity!!,
                BetDialogItem(
                    item.bet2.name,
                    item.title,
                    Functions.int_to_double(Functions.real_to_int(item.bet2.odd)),
                    "",
                    0
                ))
            //dialog!!.create()
            dialog!!.show()

            dialog!!.setOnFinishClickListener { item ->
                val bundle = Bundle()
                bundle.putInt("value", item.value)
                //SE UNICO
//                mNavController.navigate(R.id.action_nav_value_entry_to_betDocumentFragment, bundle)
                //SE NAO
                //mNavController.navigate(R.id.action_productObjectFragment_to_betDocumentFragment, bundle)
                mNavController.navigate(R.id.action_nav_home_to_betDocumentFragment, bundle)
            }
        }

    }

    fun readCategories(): BetListItem? {
        try {
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            val esp = ESharedPreferences.getInstance(BuildConfig.FILE_GENERAL, masterKeyAlias)
            val gson = Gson()
            val json: String? = esp.getString(
                BetApp.getAppContext().getString(R.string.saved_betlistitem_file_name), ""
            )
            val obj: BetListItem = gson.fromJson(json, BetListItem::class.java)

            return obj
        }catch (e: Exception) {
            Log.e("readCategories", e.toString())
            return null
        }
        return null
    }

    fun saveBetGame(betGame: BetGame) {
        println("saveBetGame")
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val encrypted = ESharedPreferences.getInstance(BuildConfig.FILE_GENERAL, masterKeyAlias)

        if(encrypted != null) {
            val gson = Gson()
            val json = gson.toJson(betGame)
            encrypted.edit()
                .putString(
                    BetApp.getAppContext().getString(R.string.saved_bet_game_file_name),
                    json)
                .apply()
        }
    }

    fun tabChanged(position: Int) {
        try {
            leagueNameDisplay!!.text = _leagueList.value?.let { it[position] }
            _leagueIdList.value?.let { loadGamesFromLeague(it[position]) }
        }
        catch(e: Exception)
        {
            Log.e("Erro jogos", "Erro ao carregar jogos da liga")
        }

    }


}