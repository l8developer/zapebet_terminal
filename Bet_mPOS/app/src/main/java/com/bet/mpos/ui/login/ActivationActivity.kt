package com.bet.mpos.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bet.mpos.MainActivity
import com.bet.mpos.R
import com.bet.mpos.databinding.ActivityActivationBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ActivationActivity: AppCompatActivity() {

    private lateinit var binding: ActivityActivationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityActivationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ViewModelProvider(this).get(ActivationViewModel::class.java)

        viewModel.start(this)
        buttons(viewModel)
        observeViewModel(viewModel)
    }

    private fun buttons(viewModel: ActivationViewModel) {
        binding.btnLoginZoop.setOnClickListener {
            viewModel.clkLogin()
        }
        binding.btnLoginCancel.setOnClickListener {
            viewModel.clkCancel()
        }
    }

    private fun observeViewModel(viewModel: ActivationViewModel) {
        viewModel.loading.observe(this) {
            if (it) {
                binding.ivLoginZoop.visibility = View.INVISIBLE
                binding.pbLoginZoop.visibility = View.VISIBLE
                binding.btnLoginCancel.visibility = View.VISIBLE

                binding.tvZoopActivationMessage.visibility = View.VISIBLE

                binding.btnLoginZoop.isClickable = false
            } else {
                binding.ivLoginZoop.visibility = View.VISIBLE
                binding.pbLoginZoop.visibility = View.INVISIBLE
                binding.btnLoginCancel.visibility = View.INVISIBLE

                binding.tvZoopActivationMessage.visibility = View.INVISIBLE

                binding.btnLoginZoop.isClickable = true
            }
        }
        viewModel.nextScreen.observe(this){
            if(it) {
                binding.ivLoginZoop.setImageDrawable(getDrawable(R.drawable.baseline_check_24))
                GlobalScope.launch {
                    delay(2000)
                    startActivity(MainActivity::class.java)
                }
            }
        }
        viewModel.conectionError.observe(this){
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        viewModel.userMessage.observe(this){
            binding.tvZoopActivationMessage.text = it
        }
    }

    private fun startActivity(activity: Class<*>?) {

        val i = Intent(this, activity)
        startActivity(i)
        finish()
    }

    override fun onBackPressed() {
//        super.onBackPressed()
    }
}

