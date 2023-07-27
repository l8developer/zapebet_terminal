package com.bet.mpos.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.bet.mpos.BetApp
import com.bet.mpos.R

fun EditText.addMoneyMask() {
    val maskWatcher = object : TextWatcher {
        private var isUpdating = false
        private var oldPixKey = ""

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Nada a ser feito aqui
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // Nada a ser feito aqui
        }

        override fun afterTextChanged(s: Editable?) {
            val pixKey = s.toString().replace("[^\\d.]".toRegex(), "")
//            val pixKey = unmask(s.toString())

//            var pixKey = s.toString()

                if (isUpdating || pixKey == oldPixKey) {
                    return
                }

                isUpdating = true

                // Remove o TextWatcher para evitar chamadas recursivas
                this@addMoneyMask.removeTextChangedListener(this)

                // Aplica a máscara
                val maskedPixKey = maskMoney(pixKey)

                // Atualiza o texto do EditText com a chave mascarada
//                this@addPixKeyMask.setText(maskedPixKey)
                if(isUpdating == true)
                    this@addMoneyMask.text.clear()
                this@addMoneyMask.append(maskedPixKey)

                this@addMoneyMask.setSelection(maskedPixKey.length)

                // Restaura o TextWatcher
                this@addMoneyMask.addTextChangedListener(this)

                isUpdating = false
                oldPixKey = maskedPixKey
        }
    }

    // Adiciona o TextWatcher ao EditText
    this.addTextChangedListener(maskWatcher)
}

fun maskMoney(key: String): String {

    var i = Integer.parseInt(key)
    println(i);
    return BetApp.getAppContext().getString(R.string.total_amount, i.toDouble())
}


