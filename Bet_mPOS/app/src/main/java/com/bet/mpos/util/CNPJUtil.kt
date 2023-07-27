package com.bet.mpos.util

class CNPJUtil {

    companion object {

        fun validateCNPJ(cnpj: String): Boolean {
            val numbers = cnpj.replace("[^\\d]".toRegex(), "")

            if (numbers.length != 14) {
                return false
            }

            val digits = numbers.map { it.toString().toInt() }

            val firstDigit = calculateCnpjDigit(digits, 12, 5)
            if (digits[12] != firstDigit) {
                return false
            }

            val secondDigit = calculateCnpjDigit(digits, 13, 6)
            if (digits[13] != secondDigit) {
                return false
            }

            return true
        }

        private fun calculateCnpjDigit(digits: List<Int>, endIndex: Int, initialWeight: Int): Int {
            var sum = 0
            var weight = initialWeight
            val length = endIndex + 1
            for (i in 0 until endIndex) {
                sum += digits[i] * weight
                weight = if (weight == 2) 9 else weight - 1
            }

            val mod = sum % 11
            return if (mod < 2) 0 else 11 - mod
        }


    }

}