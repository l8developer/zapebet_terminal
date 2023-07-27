package com.bet.mpos.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bet.mpos.databinding.DialogCpfOrCellBinding;

public class CPFOrCellDialog extends Dialog{

    private DialogCpfOrCellBinding binding;
    private OnCPFClickListener cpfListener;
    private OnCellClickListener cellListener;

    public interface OnCPFClickListener{
        void onCPFClick();
    }
    public interface OnCellClickListener{
        void onCellClick();
    }

    public void setOnCPFClickListener(OnCPFClickListener listener) {
        cpfListener = listener;
    }
    public void setOnCellClickListener(OnCellClickListener listener) {
        cellListener = listener;
    }

    public CPFOrCellDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DialogCpfOrCellBinding.inflate(LayoutInflater.from(getContext()));
        setContentView(binding.getRoot());

        // cancelar ao clicar fora do dialog
//        setCanceledOnTouchOutside(false);

        // observa o botão cancelar do dialog
        binding.btnIsCell.setOnClickListener(click -> {
            if(cellListener != null){
                cellListener.onCellClick();
            }
        });
        binding.btnIsCpf.setOnClickListener( click -> {
            if(cpfListener != null){
                cpfListener.onCPFClick();
            }
        });
//        binding.btnPrintOutCustomersCopyDialog.setOnClickListener(click -> { printOutCustomersCopy(); });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

}
