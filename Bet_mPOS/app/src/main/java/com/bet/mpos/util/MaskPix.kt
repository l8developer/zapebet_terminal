package com.bet.mpos.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class MaskPix {

    fun mask(ediTxt: EditText): TextWatcher? {
        return object : TextWatcher {
            var isUpdating = false
            var old = ""
            override fun afterTextChanged(s: Editable) {

            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                val str: String = s.toString().replace("[^\\d.]".toRegex(), "")
                val str: String = unmask(s.toString())

                var mask = ""
                if (isUpdating) {
                    old = str
                    isUpdating = false
                    return
                }

                mask = maskPix(str)

                isUpdating = true
//                ediTxt.setText(mask)
                if(isUpdating == false)
                    ediTxt.text.clear()
                ediTxt.append(mask)
                ediTxt.setSelection(mask.length)
            }
        }
    }

    fun unmask(s: String): String {
        return s.replace("[.]".toRegex(), "").replace("[-]".toRegex(), "")
            .replace("[/]".toRegex(), "").replace("[(]".toRegex(), "").replace("[ ]".toRegex(), "")
            .replace("[:]".toRegex(), "").replace("[)]".toRegex(), "")
    }

    fun maskPix(key: String): String {
        val maskedKey = StringBuilder()

        // Verifica o tipo de chave Pix
        var keyType = "NONE"
        if(key.matches(Regex("[0-9]{11}")))
            if(key[2] == '9')
            {
                if(CPFUtil.validateCPF(key))
                    keyType = "CPF"
                else
                    keyType = "CEL"
            }
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

    fun maskType(key: String): String{
        var keyType = "NONE"
        if(key.matches(Regex("[0-9]{11}")))
            if(key[2] == '9')
                keyType = "CEL"
            else
                keyType = "CPF"
        else if(key.matches(Regex("[0-9]{14}")))
            keyType = "CNPJ"

        return keyType
    }
}


