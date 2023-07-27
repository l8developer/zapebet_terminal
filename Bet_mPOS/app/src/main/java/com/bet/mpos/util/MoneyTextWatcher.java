package com.bet.mpos.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class MoneyTextWatcher implements TextWatcher {
    private final DecimalFormat decimalFormat;
    private final EditText editText;

    public MoneyTextWatcher(EditText editText) {
        this.editText = editText;
        decimalFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.getDefault());
        decimalFormat.setCurrency(decimalFormat.getCurrency());
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Não é necessário implementar neste caso
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Não é necessário implementar neste caso
    }

    @Override
    public void afterTextChanged(Editable s) {
        editText.removeTextChangedListener(this);

        // Obtém o texto atual sem formatação
        String originalText = s.toString().replaceAll("[^\\d]", "");

        // Converte para double
        double amount = Double.parseDouble(originalText) / 100;

        // Formata o valor para o formato de moeda
        String formattedText = decimalFormat.format(amount);

        // Atualiza o texto do EditText com a formatação
        editText.setText(formattedText);
        editText.setSelection(formattedText.length());

        editText.addTextChangedListener(this);
    }
}
