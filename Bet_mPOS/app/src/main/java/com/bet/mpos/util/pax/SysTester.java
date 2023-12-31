package com.bet.mpos.util.pax;

import com.pax.dal.ISys;
import com.pax.dal.entity.BaseInfo;
import com.pax.dal.entity.EBeepMode;
import com.pax.dal.entity.ENavigationKey;
import com.pax.dal.entity.ETermInfoKey;
import com.pax.dal.entity.ETouchMode;
import com.bet.mpos.BetApp;
import com.bet.mpos.util.Convert;

import java.util.Locale;
import java.util.Map;

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

    public void beep(final EBeepMode beepMode, final int delayTime) {
        iSys.beep(beepMode, delayTime);
    }

    public String getTerminfo() {
        Map<ETermInfoKey, String> termInfo = iSys.getTermInfo();
        StringBuilder termInfoStr = new StringBuilder();
        for (ETermInfoKey key : ETermInfoKey.values()) {
            termInfoStr.append(key.name() + ":" + termInfo.get(key) + "\n");
        }
        return termInfoStr.toString();
    }

    public String getRadom(int len) {
        byte[] random = iSys.getRandom(len);
        if (random != null) {
            return Convert.getInstance().bcdToStr(random);
        } else {
            return "null";
        }

    }

    public String getDevInterfaceVer() {
        String verString = iSys.getDevInterfaceVer();
        return "version of device interface:" + verString;
    }

    public void showNavigationBar(boolean flag) {
        iSys.showNavigationBar(flag);
    }

    public void enableNavigationBar(boolean flag) {
        iSys.enableNavigationBar(flag);
    }

    public void enableNavigationKey(ENavigationKey navigationKey, boolean flag) {
        iSys.enableNavigationKey(navigationKey, flag);
    }

    public boolean isNavigationBarVisible() {
        boolean res = true;
        res = iSys.isNavigationBarVisible();
        return res;
    }

    public boolean isNavigationBarEnabled() {
        boolean res = true;
        res = iSys.isNavigationBarEnabled();
        return res;
    }

    public boolean isNavigationKeyEnabled(ENavigationKey navigationKey) {
        boolean res = true;
        res = iSys.isNavigationKeyEnabled(navigationKey);
        return res;
    }

    public void showStatusBar(boolean flag) {
        iSys.showStatusBar(flag);
    }

    public void enableStatusBar(boolean flag) {
        iSys.enableStatusBar(flag);
    }

    public boolean isStatusBarEnabled() {
        boolean res = true;
        res = iSys.isStatusBarEnabled();
        return res;
    }

    public boolean isStatusBarVisible() {
        boolean res = true;
        res = iSys.isStatusBarVisible();
        return res;
    }

    public void resetStatusBar() {
        iSys.resetStatusBar();
    }

    public void enablePowerKey(boolean flag) {
        iSys.enablePowerKey(flag);
    }

    public boolean isPowerKeyEnabled() {
        boolean res = true;
        res = iSys.isPowerKeyEnabled();
        return res;
    }

    public void setSettingsNeedPassword(boolean flag) {
        iSys.setSettingsNeedPassword(flag);
    }

    public void reboot() {
        iSys.reboot();
    }

    public void shutdown() {
        iSys.shutdown();
    }

    public void switchTouchMode(ETouchMode touchMode) {
        iSys.switchTouchMode(touchMode);
    }

    public int getAppLogs(String storePath, String startDate, String endDate) {
        int res = iSys.getAppLogs(storePath, startDate, endDate);
        return res;
    }

    public String readTUSN() {
        String res = iSys.readTUSN();
        return res;
    }
    
    public boolean isOnBase(){
        boolean isOnBase = iSys.isOnBase();
        return isOnBase;
    }
    
    public BaseInfo getBaseInfo(){
        BaseInfo baseInfo = iSys.getBaseInfo();
        return baseInfo;
    }
    
    public String getSystemLanguage(){
        String language = iSys.getSystemLanguage();
        return language;
    }
    
    public void setSystemLanguage(Locale locale){
        iSys.setSystemLanguage(locale);
    }
    
    public String getPN(){
        String pn = iSys.getPN();
        return pn;
    }

    public void setLed(){
        iSys.ledControl((byte) 0, (byte) 1);
    }
}
