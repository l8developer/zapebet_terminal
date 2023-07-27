package com.bet.mpos.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bet.mpos.databinding.DialogErrorNotificationBinding;

public class ErrorNotificationDialog extends Dialog {
    private DialogErrorNotificationBinding binding;

    public ErrorNotificationDialog(@NonNull Context context) {
        super(context);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DialogErrorNotificationBinding.inflate(LayoutInflater.from(getContext()));
        setContentView(binding.getRoot());
        setCanceledOnTouchOutside(false);

        binding.btnCancelar.setOnClickListener(click -> {cancel();});
    }
    //PARA USAR setTitle, setSubTitle e setButtonCancelMessage ANTES DEVE CHAMAR create()
//    dialog.create();
//    dialog.setTitle("Title");
//    dialog.setSubTitle("SubTile");
//    dialog.setButtonCancelMessage("close || fechar || cancelar");
//    dialog.show();
    public void setTitle(String title) {
        binding.tvTitle.setText(title);
    }

    public void setSubTitle(String subtitle) {
        binding.tvSubTitle.setText(subtitle);
    }

    public void setButtonCancelMessage(String message) {
        binding.btnCancelar.setText(message);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
