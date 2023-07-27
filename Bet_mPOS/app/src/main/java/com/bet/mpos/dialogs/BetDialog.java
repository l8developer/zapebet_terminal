package com.bet.mpos.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;


import com.bet.mpos.BetApp;
import com.bet.mpos.R;
import com.bet.mpos.databinding.DialogBetValueBinding;
import com.bet.mpos.objects.BetDialogItem;
import com.bet.mpos.util.Functions;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class BetDialog extends Dialog{

    private DialogBetValueBinding binding;
    private BetDialogItem item;
    private OnFinishClickListener finishListener;
    private DecimalFormat decimalFormat;

    private int value = 0;

    public interface OnFinishClickListener{
        void onFinishClick(BetDialogItem item);
    }

    public void setOnFinishClickListener(OnFinishClickListener listener) {
        finishListener = listener;
    }

    public BetDialog(@NonNull Context context, BetDialogItem item) {
        super(context);
        this.item = item;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DialogBetValueBinding.inflate(LayoutInflater.from(getContext()));
        setContentView(binding.getRoot());

        binding.tvBetTitleDialog.setText(item.getTitle());
        binding.tvSubtitleDialog.setText(item.getSubTile());
        binding.tvBetOddDialog.setText(item.getOdd().toString());

        binding.etValueBetDialog.setEnabled(false);
        binding.etValueBetDialog.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                decimalFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.getDefault());
                decimalFormat.setCurrency(decimalFormat.getCurrency());
                decimalFormat.setMaximumFractionDigits(2);
                decimalFormat.setMinimumFractionDigits(2);

                binding.etValueBetDialog.removeTextChangedListener(this);
                binding.etValueBetDialog.setText("R$0.00");

                // Obtém o texto atual sem formatação
                String originalText = s.toString().replaceAll("[^\\d]", "");


                // Converte para double
                double amount = Double.parseDouble(originalText) / 100;

                // Formata o valor para o formato de moeda
                String formattedText = decimalFormat.format(amount);

                // Atualiza o texto do EditText com a formatação
                binding.etValueBetDialog.setText(formattedText);
                binding.etValueBetDialog.setSelection(formattedText.length());

                binding.etValueBetDialog.addTextChangedListener(this);

                String sVal = binding.etValueBetDialog.getText().toString();
                int iVal = Functions.real_to_int2(sVal);
                double dVal = Functions.int_to_double(iVal);
                Double ret = dVal * item.getOdd();
                binding.tvReturn.setText("Retornos Potenciais: R$ " + BetApp.getAppContext().getString(R.string.total_amount, ret));

            }
        });

        binding.etValueBetDialog.setText("0");


        // cancelar ao clicar fora do dialog
//        setCanceledOnTouchOutside(false);

        binding.btnCloseBetDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide();
            }
        });

        binding.btnContinueBetDialog.setOnClickListener( click -> {
            if(finishListener != null){
                String sVal = binding.etValueBetDialog.getText().toString();
                int iVal = Functions.real_to_int2(sVal);
                if(iVal > 99) {
                    item.setValue(iVal);
                    finishListener.onFinishClick(item);
                    hide();
                }else{
                    Toast.makeText(BetApp.getAppContext(), "Valor minímo R$ 1,00", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.etValueBetDialog.setText("1000");
                binding.etValueBetDialog.setEnabled(false);
                Double ret = 10.0*item.getOdd();
                binding.tvReturn.setText("Retornos Potenciais: R$ "+ BetApp.getAppContext().getString(R.string.total_amount, ret));

            }
        });
        binding.btn20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.etValueBetDialog.setText("2000");
                binding.etValueBetDialog.setEnabled(false);
                Double ret = 20.0*item.getOdd();
                binding.tvReturn.setText("Retornos Potenciais: R$ "+ BetApp.getAppContext().getString(R.string.total_amount, ret));
            }
        });
        binding.btn30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.etValueBetDialog.setText("3000");
                binding.etValueBetDialog.setEnabled(false);
                Double ret = 30.0*item.getOdd();
                binding.tvReturn.setText("Retornos Potenciais: R$ "+ BetApp.getAppContext().getString(R.string.total_amount, ret));
            }
        });
        binding.btn50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.etValueBetDialog.setText("5000");
                binding.etValueBetDialog.setEnabled(false);
                Double ret = 50.0*item.getOdd();
                binding.tvReturn.setText("Retornos Potenciais: R$ "+ BetApp.getAppContext().getString(R.string.total_amount, ret));
            }
        });
        binding.btn100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.etValueBetDialog.setText("10000");
                binding.etValueBetDialog.setEnabled(false);
                Double ret = 100.0*item.getOdd();
                binding.tvReturn.setText("Retornos Potenciais: R$ "+ BetApp.getAppContext().getString(R.string.total_amount, ret));
            }
        });

        binding.btnOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.etValueBetDialog.setEnabled(true);
                binding.etValueBetDialog.setText("0");

                InputMethodManager imm = (InputMethodManager)   getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

}
