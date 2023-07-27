package com.bet.mpos.objects;

import androidx.annotation.NonNull;

public class BetRegistration {
    private boolean success;
    private String message, codigo_bet;
    private int retorno_ganhos, retorno_aposta;

    public BetRegistration(boolean success, String message, String codigo_bet, int retorno_ganhos, int retorno_aposta) {
        this.success = success;
        this.message = message;
        this.codigo_bet = codigo_bet;
        this.retorno_ganhos = retorno_ganhos;
        this.retorno_aposta = retorno_aposta;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCodigo_bet() {
        return codigo_bet;
    }

    public void setCodigo_bet(String codigo_bet) {
        this.codigo_bet = codigo_bet;
    }

    public int getRetorno_ganhos() {
        return retorno_ganhos;
    }

    public void setRetorno_ganhos(int retorno_ganhos) {
        this.retorno_ganhos = retorno_ganhos;
    }

    public int getRetorno_aposta() {
        return retorno_aposta;
    }

    public void setRetorno_aposta(int retorno_aposta) {
        this.retorno_aposta = retorno_aposta;
    }

    @NonNull
    @Override
    public String toString() {
        return "{ " +
                "message: " + message + ", " +
                "success: " + success + ", " +
                "retorno_ganhos: " + retorno_ganhos + ", " +
                "retorno_aposta: " + retorno_aposta + ", " +
                "codigo_bet: " + codigo_bet + ", " +
                " }";
    }
}
