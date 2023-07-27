package com.bet.mpos.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun EditText.addPixKeyMask() {
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
//            val pixKey = s.toString().replace("[^\\d.]".toRegex(), "")
            val pixKey = unmask(s.toString())

//            var pixKey = s.toString()

                if (isUpdating || pixKey == oldPixKey) {
                    return
                }

                isUpdating = true

                // Remove o TextWatcher para evitar chamadas recursivas
                this@addPixKeyMask.removeTextChangedListener(this)

                // Aplica a máscara
                val maskedPixKey = maskPixKey(pixKey)

                // Atualiza o texto do EditText com a chave mascarada
//                this@addPixKeyMask.setText(maskedPixKey)
                if(isUpdating == true)
                    this@addPixKeyMask.text.clear()
                this@addPixKeyMask.append(maskedPixKey)

                this@addPixKeyMask.setSelection(maskedPixKey.length)

                // Restaura o TextWatcher
                this@addPixKeyMask.addTextChangedListener(this)

                isUpdating = false
                oldPixKey = maskedPixKey
        }
    }

    // Adiciona o TextWatcher ao EditText
    this.addTextChangedListener(maskWatcher)
}

fun maskPixKey(key: String): String {
    val maskedKey = StringBuilder()

    // Verifica o tipo de chave Pix
    var keyType = "NONE"
    if(key.matches(Regex("[0-9]{11}")))
        if(key[2] == '9')
            keyType = "CEL"
        else
            keyType = "CPF"
    else if(key.matches(Regex("[0-9]{14}")))
        keyType = "CNPJ"

    println(keyType)
    // Aplica a máscara de acordo com o tipo de chave
    when (keyType) {
        "NONE" -> {
            maskedKey.append(key)
        }
        "CPF" -> {
            maskedKey.append(key.substring(0, 3))
            maskedKey.append(".")
            maskedKey.append(key.substring(3, 6))
            maskedKey.append(".")
            maskedKey.append(key.substring(6, 9))
            maskedKey.append("-")
            maskedKey.append(key.substring(9))
        }
        "CNPJ" -> {
            maskedKey.append(key.substring(0, 2))
            maskedKey.append(".")
            maskedKey.append(key.substring(2, 5))
            maskedKey.append(".")
            maskedKey.append(key.substring(5, 8))
            maskedKey.append("/")
            maskedKey.append(key.substring(8, 12))
            maskedKey.append("-")
            maskedKey.append(key.substring(12))
        }
        "CEL" -> {
            maskedKey.append("(")
            maskedKey.append(key.substring(0, 2))
            maskedKey.append(")")
            maskedKey.append(key.substring(2, 7))
            maskedKey.append("-")
            maskedKey.append(key.substring(7, 11))
        }
    }

    return maskedKey.toString()
}

fun unmask(s: String): String {
    return s.replace("[.]".toRegex(), "").replace("[-]".toRegex(), "")
        .replace("[/]".toRegex(), "").replace("[(]".toRegex(), "").replace("[ ]".toRegex(), "")
        .replace("[:]".toRegex(), "").replace("[)]".toRegex(), "")
}

