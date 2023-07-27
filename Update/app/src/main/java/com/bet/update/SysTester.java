package com.bet.update;

import com.pax.dal.ISys;

public class SysTester {

    private static SysTester sysTester;
    private ISys iSys = null;

    private SysTester() {
        iSys = BetApp.getDal().getSys();
    }

    public static SysTester getInstance() {
        if (sysTester == null) {
            sysTester = new SysTester();
        }
        return sysTester;
    }

    public String installApp(String path){
        int code = iSys.installApp(path);
        if(code == 0) {
            return "instalado com sucesso";
        }
        else{
            return "Falha " + code;
        }
    }
}
