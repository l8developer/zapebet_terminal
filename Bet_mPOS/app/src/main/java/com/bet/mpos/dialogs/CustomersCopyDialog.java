package com.bet.mpos.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bet.mpos.databinding.DialogCustomerSCopyBinding;
import com.zoop.sdk.Zoop;
import com.zoop.sdk.api.Callback;
import com.zoop.sdk.api.Request;
import com.zoop.sdk.api.collection.ReceiptType;
import com.zoop.sdk.api.collection.TransactionData;
import com.zoop.sdk.api.terminal.Printer;
import com.zoop.sdk.plugin.smartpos.SmartPOSPlugin;
import com.zoop.sdk.plugin.smartpos.requestBuilder.SmartPOSPrinterResponse;

public class CustomersCopyDialog extends Dialog{

    private DialogCustomerSCopyBinding binding;
    private TransactionData transactionData;
    private OnPrintClickListener printListener;
    private OnCancelClickListener cancelListener;

    public interface OnPrintClickListener{
        void onPrintClick();
    }
    public interface OnCancelClickListener{
        void onCancelClick();
    }

    public void setOnPrintClickListener(OnPrintClickListener listener) {
        printListener = listener;
    }
    public void setOnCancelClickListener(OnCancelClickListener listener) {
        cancelListener = listener;
    }

    public CustomersCopyDialog(@NonNull Context context,  TransactionData transactionData) {
        super(context);
        this.transactionData = transactionData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DialogCustomerSCopyBinding.inflate(LayoutInflater.from(getContext()));
        setContentView(binding.getRoot());

        // cancelar ao clicar fora do dialog
        setCanceledOnTouchOutside(false);

        // observa o botão cancelar do dialog
        binding.btnCancelCustomersCopyDialog.setOnClickListener(click -> {
            cancel();
            if(cancelListener != null){
                cancelListener.onCancelClick();
            }
        });
        binding.btnPrintOutCustomersCopyDialog.setOnClickListener( click -> {
            if(printListener != null){
                printListener.onPrintClick();
            }
        });
//        binding.btnPrintOutCustomersCopyDialog.setOnClickListener(click -> { printOutCustomersCopy(); });
    }

    public void printOutCustomersCopy()
    {
        Request request = SmartPOSPlugin.Companion.createPrintRequestBuilder()
                .printData(new Printer.PrintData(transactionData, null, null, null))
                .receiptType(ReceiptType.CUSTOMER)
                .callback(new Callback<SmartPOSPrinterResponse>() {
                    @Override
                    protected void onSuccess(SmartPOSPrinterResponse smartPOSPrinterResponse) {
                        System.out.println(smartPOSPrinterResponse.getMessage());

                    }

                    @Override
                    protected void onFail(@NonNull Throwable throwable) {
                        System.out.println(throwable.getMessage());
                    }
                }).build();

        //Zoop zoop = Zoop.INSTANCE;
        //zoop.post(request);
        Zoop.INSTANCE.post(request);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

}
