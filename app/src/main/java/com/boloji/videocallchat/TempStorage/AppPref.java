package com.boloji.videocallchat.TempStorage;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.exifinterface.media.ExifInterface;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class AppPref {
    public static final String USER_PREFS = "app_preferences";
    public String AM_AppID = "AM_AppID";
    public String AM_NATIVE_Priority = "AM_NATIVE_Priority";
    public String AdsCounter = "AdsCounter";
    public String AppMode = "AppMode";
    public String App_version = "App_version";
    public String AudioPermisionGive = "AudioPermisionGive";
    public String AudioPermisionGiveSticker = "AudioPermisionGiveSticker";
    public String BroadCastPurchase = "BroadCastPurchase";
    public String BroadCastPurchaseExpDate = "BroadCastPurchaseExpDate";
    public String BroadCastPurchaseExpTime = "BroadCastPurchaseExpTime";
    public String Broadcastday = "Broadcastday";
    public String BrodcastMinutes = "BrodcastMinutes";
    public String CallFree = "CallFree";
    public String CallPermisionGive = "CallPermisionGive";
    public String CamPermisionGive = "CamPermisionGive";
    public String CertificateFile = "CertificateFile";
    public String CertificateFlagNew = "CertificateFlagNew";
    public String CertificateFlagOld = "CertificateFlagOld";
    public String ChatBlurCountPos = "password";
    public String ChatNickName = "ChatNickName";
    public String ChatUserName = "ChatUserName";
    public String ContactPurchaseCount = "ContactPurchaseCount";
    public String DOB = "DOB";
    public String DeviceId = "DeviceId";
    public String Email = "Email";
    public String FirTime = "first_time";
    public String FromUser = "FromUser";
    public String FromUserpp = "FromUserpp";
    public String GoogleRTC_server_url = "GoogleRTC_server_url";
    public String IsCallUpdate = "IsCallUpdate";
    public String IsContactPurchase = "IsContactPurchase";
    public String IsGenderUpdate = "IsGenderUpdate";
    public String Isfirsttimerandom = "Isfirsttimerandom";
    public String LoginType = "LoginType";
    public String Loginpp = "Loginpp";
    public String MainMobileno = "MainMobileno";
    public String NativePriority = "NativePriority";
    public String Nativecout = "Nativecout";
    public String NewRegId = "NewRegId";
    public String NoCall = "NoCall";
    public String OTP = "OTP";
    public String OneMinCoin = "OneMinCoin";
    public String PermisionGive = "PermisionGive";
    public String PremiumCheckInDate = "PremiumCheckInDate";
    public String PremiumMinutes = "PremiumMinutes";
    public String PremiumPurchase = "PremiumPurchase";
    public String RateingGive = "RateingGive";
    public String ReceiverListTheme = "ReceiverListTheme";
    public String ReceiverListThemeGoApp = "ReceiverListThemeGoApp";
    public String Receivertopprofile = "ReceiverTopProfile";
    public String RemoveAds = "RemoveAds";
    public String Reward_Counter = "Reward_Counter";
    public String RoomName = "RoomName";
    public String Show_advice = "Show_advice";
    public String SocialID = "SocialID";
    public String Socket_url = "Socket_url";
    public String StartAppAds = "StartAppAds";
    public String StartApp_ads = "StartApp_ads";
    public String StorePermisionGive = "StorePermisionGive";
    public String Termscon = "Termscon";
    public String Theme_version = "Theme_version";
    public String ToUser = "ToUser";
    public String User = "user";
    public String UserEmail = "UserEmail";
    public String UserIMagee = "UserIMagee";
    public String Usermobileno = "Usermobileno";
    public String UsernameSta = "UsernameSta";
    public String WebSocket = "WebSocket";
    public String WebSocket_url = "WebSocket_url";
    public String age = "age";
    public SharedPreferences appSharedPref;
    public String country_code = "country_code";
    public String famale_selected_avatra = "famale_selected_avatra";
    public String fource_download = "fource_download";
    public String gender = "gender";
    public String isFirstPremiumCall = "isFirstPremiumCall";
    public String isPremiumCall = "isPremiumCall";
    public String isPremiumCallVideo = "isPremiumCallVideo";
    public String male_selected_avatra = "male_selected_avatra";
    public String password = "password";
    public String phone_no = "phone_no";
    public String pp = "pp";
    public SharedPreferences.Editor prefEditor;
    public String user_type = "user_type";
    public String version = "version";

    public boolean getFirTime() {
        return this.appSharedPref.getBoolean(this.FirTime, false);
    }

    public void setFirTime(boolean z) {
        this.prefEditor.putBoolean(this.FirTime, z).commit();
    }

    public AppPref(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_PREFS, 0);
        this.appSharedPref = sharedPreferences;
        this.prefEditor = sharedPreferences.edit();
    }

    public void setChatUserID(String str) {
        ((List) new Gson().fromJson(this.appSharedPref.getString("Set", ""), new TypeToken<List<String>>() {
            /* class com.gtu.newlivevideocall.PrefrenceClass.ApplicationPreference.AnonymousClass1 */
        }.getType())).add(str);
        this.prefEditor.putString("Set", new Gson().toJson(str)).commit();
    }

    public void setTermscon(String str) {
        this.prefEditor.putString(this.Termscon, str).commit();
    }

    public String getTermscon() {
        return this.appSharedPref.getString(this.Termscon, "false");
    }

    public int getVersion() {
        return this.appSharedPref.getInt(this.version, 1);
    }

    public int getBroadcastday() {
        return this.appSharedPref.getInt(this.Broadcastday, 0);
    }

    public void setBroadcastday(int i) {
        this.prefEditor.putInt(this.Broadcastday, i).commit();
    }

    public int getReceivertopprofile() {
        return this.appSharedPref.getInt(this.Receivertopprofile, 0);
    }

    public void setReceivertopprofile(int i) {
        this.prefEditor.putInt(this.Receivertopprofile, i).commit();
    }

    public int getmale_selected_avatra() {
        return this.appSharedPref.getInt(this.male_selected_avatra, 0);
    }

    public void setmale_selected_avatra(int i) {
        this.prefEditor.putInt(this.male_selected_avatra, i).commit();
    }

    public void setCertificateFile(String str) {
        this.prefEditor.putString(this.CertificateFile, str).commit();
    }

    public String getCertificateFile() {
        return this.appSharedPref.getString(this.CertificateFile, "");
    }

    public void setCertificateFlagNew(String str) {
        this.prefEditor.putString(this.CertificateFlagNew, str).commit();
    }

    public String getCertificateFlagNew() {
        return this.appSharedPref.getString(this.CertificateFlagNew, "");
    }

    public void setCertificateFlagOld(String str) {
        this.prefEditor.putString(this.CertificateFlagOld, str).commit();
    }

    public String getCertificateFlagOld() {
        return this.appSharedPref.getString(this.CertificateFlagOld, "");
    }

    public int getfamale_selected_avatra() {
        return this.appSharedPref.getInt(this.famale_selected_avatra, 0);
    }

    public void setfamale_selected_avatra(int i) {
        this.prefEditor.putInt(this.famale_selected_avatra, i).commit();
    }

    public String getMainMobileno() {
        return this.appSharedPref.getString(this.MainMobileno, "");
    }

    public void setMainMobileno(String str) {
        this.prefEditor.putString(this.MainMobileno, str).commit();
    }

    public String getUsermobileno() {
        return this.appSharedPref.getString(this.Usermobileno, "");
    }

    public void setUsermobileno(String str) {
        this.prefEditor.putString(this.Usermobileno, str).commit();
    }

    public String getUserEmail() {
        return this.appSharedPref.getString(this.UserEmail, "");
    }

    public void setUserEmail(String str) {
        this.prefEditor.putString(this.UserEmail, str).commit();
    }

    public String getUsernameSta() {
        return this.appSharedPref.getString(this.UsernameSta, "");
    }

    public void setUsernameSta(String str) {
        this.prefEditor.putString(this.UsernameSta, str).commit();
    }

    public boolean getIsfirsttimerandom() {
        return this.appSharedPref.getBoolean(this.Isfirsttimerandom, false);
    }

    public void setIsfirsttimerandom(boolean z) {
        this.prefEditor.putBoolean(this.Isfirsttimerandom, z).commit();
    }

    public String getBroadCastPurchaseExpDate() {
        return this.appSharedPref.getString(this.BroadCastPurchaseExpDate, "02/09/2020 12:42:50 PM");
    }

    public void setBroadCastPurchaseExpDate(String str) {
        this.prefEditor.putString(this.BroadCastPurchaseExpDate, str).commit();
    }

    public String getUserIMagee() {
        return this.appSharedPref.getString(this.UserIMagee, "");
    }

    public void setUserIMagee(String str) {
        this.prefEditor.putString(this.UserIMagee, str).commit();
    }

    public String getReward_Counter() {
        return this.appSharedPref.getString(this.Reward_Counter, "02/09/2020 12:42:50 PM");
    }

    public void setReward_Counter(String str) {
        this.prefEditor.putString(this.Reward_Counter, str).commit();
    }

    public String getChatBlurCountPos() {
        return this.appSharedPref.getString(this.ChatBlurCountPos, "23");
    }

    public void setChatBlurCountPos(String str) {
        this.prefEditor.putString(this.ChatBlurCountPos, str).commit();
    }

    public int getContactPurchaseCount() {
        return this.appSharedPref.getInt(this.ContactPurchaseCount, 0);
    }

    public void setContactPurchaseCount(int i) {
        this.prefEditor.putInt(this.ContactPurchaseCount, i).commit();
    }

    public boolean getRemoveAds() {
        return this.appSharedPref.getBoolean(this.RemoveAds, true);
    }

    public void setRemoveAds(boolean z) {
        this.prefEditor.putBoolean(this.RemoveAds, z).commit();
    }

    public boolean getIsGenderUpdate() {
        return this.appSharedPref.getBoolean(this.IsGenderUpdate, false);
    }

    public void setIsGenderUpdate(boolean z) {
        this.prefEditor.putBoolean(this.IsGenderUpdate, z).commit();
    }

    public int getIsCallUpdate() {
        return this.appSharedPref.getInt(this.IsCallUpdate, 0);
    }

    public void setIsCallUpdate(int i) {
        this.prefEditor.putInt(this.IsCallUpdate, i).commit();
    }

    public int getNoCall() {
        return this.appSharedPref.getInt(this.NoCall, 0);
    }

    public void setNoCall(int i) {
        this.prefEditor.putInt(this.NoCall, i).commit();
    }

    public boolean getIsContactPurchase() {
        return this.appSharedPref.getBoolean(this.IsContactPurchase, false);
    }

    public void setIsContactPurchase(boolean z) {
        this.prefEditor.putBoolean(this.IsContactPurchase, z).commit();
    }

    public int getBroadCastPurchaseExpTime() {
        return this.appSharedPref.getInt(this.BroadCastPurchaseExpTime, 0);
    }

    public void setBroadCastPurchaseExpTime(int i) {
        this.prefEditor.putInt(this.BroadCastPurchaseExpTime, i).commit();
    }

    public int getPremiumMinutes() {
        return this.appSharedPref.getInt(this.PremiumMinutes, 0);
    }

    public void setPremiumMinutes(int i) {
        this.prefEditor.putInt(this.PremiumMinutes, i).commit();
    }

    public int getBrodcastPremiumMinutes() {
        return this.appSharedPref.getInt(this.BrodcastMinutes, 0);
    }

    public void setBrodcastPremiumMinutes(int i) {
        this.prefEditor.putInt(this.BrodcastMinutes, i).commit();
    }

    public boolean getCallFree() {
        return this.appSharedPref.getBoolean(this.CallFree, false);
    }

    public void setCallFree(boolean z) {
        this.prefEditor.putBoolean(this.CallFree, z).commit();
    }

    public boolean getBroadCastPurchase() {
        return this.appSharedPref.getBoolean(this.BroadCastPurchase, false);
    }

    public void setBroadCastPurchase(boolean z) {
        this.prefEditor.putBoolean(this.BroadCastPurchase, z).commit();
    }

    public boolean getPremiumPurchase() {
        return this.appSharedPref.getBoolean(this.PremiumPurchase, false);
    }

    public void setPremiumPurchase(boolean z) {
        this.prefEditor.putBoolean(this.PremiumPurchase, z).commit();
    }

    public String getChatUserName() {
        return this.appSharedPref.getString(this.ChatUserName, "Stranger");
    }

    public void setChatUserName(String str) {
        this.prefEditor.putString(this.ChatUserName, str).commit();
    }

    public String getChatNickName() {
        return this.appSharedPref.getString(this.ChatNickName, "");
    }

    public void setChatNickName(String str) {
        this.prefEditor.putString(this.ChatNickName, str).commit();
    }

    public String getReceiverListThemeGoApp() {
        return this.appSharedPref.getString(this.ReceiverListThemeGoApp, "");
    }

    public void setReceiverListThemeGoApp(String str) {
        this.prefEditor.putString(this.ReceiverListThemeGoApp, str).commit();
    }

    public String getReceiverListTheme() {
        return this.appSharedPref.getString(this.ReceiverListTheme, "");
    }

    public void setReceiverListTheme(String str) {
        this.prefEditor.putString(this.ReceiverListTheme, str).commit();
    }

    public int getIsFirstPremiumCall() {
        return this.appSharedPref.getInt(this.isFirstPremiumCall, 0);
    }

    public void setIsFirstPremiumCall(int i) {
        this.prefEditor.putInt(this.isFirstPremiumCall, i).commit();
    }

    public int getIsPremiumCallVideo() {
        return this.appSharedPref.getInt(this.isPremiumCallVideo, 0);
    }

    public void setIsPremiumCallVideo(int i) {
        this.prefEditor.putInt(this.isPremiumCallVideo, i).commit();
    }

    public String getCallPermisionGive() {
        return this.appSharedPref.getString(this.CallPermisionGive, "");
    }

    public void setCallPermisionGive(String str) {
        this.prefEditor.putString(this.CallPermisionGive, str).commit();
    }

    public String getMainURL() {
        return this.appSharedPref.getString(this.AudioPermisionGive, "");
    }

    public void setMainURL(String str) {
        this.prefEditor.putString(this.AudioPermisionGive, str).commit();
    }

    public String getstrMainURL() {
        return this.appSharedPref.getString(this.AudioPermisionGiveSticker, "");
    }

    public void setstrMainURL(String str) {
        this.prefEditor.putString(this.AudioPermisionGiveSticker, str).commit();
    }

    public String getStorePermisionGive() {
        return this.appSharedPref.getString(this.StorePermisionGive, "");
    }

    public void setStorePermisionGive(String str) {
        this.prefEditor.putString(this.StorePermisionGive, str).commit();
    }

    public String getCamPermisionGive() {
        return this.appSharedPref.getString(this.CamPermisionGive, "");
    }

    public void setCamPermisionGive(String str) {
        this.prefEditor.putString(this.CamPermisionGive, str).commit();
    }

    public String getPermisionGive() {
        return this.appSharedPref.getString(this.PermisionGive, "");
    }

    public void setPermisionGive(String str) {
        this.prefEditor.putString(this.PermisionGive, str).commit();
    }

    public String getDOB() {
        return this.appSharedPref.getString(this.DOB, "");
    }

    public void setDOB(String str) {
        this.prefEditor.putString(this.DOB, str).commit();
    }

    public String getpassword() {
        return this.appSharedPref.getString(this.password, "");
    }

    public void setpassword(String str) {
        this.prefEditor.putString(this.password, str).commit();
    }

    public String getphone_no() {
        return this.appSharedPref.getString(this.phone_no, "");
    }

    public void setphone_no(String str) {
        this.prefEditor.putString(this.phone_no, str).commit();
    }

    public String getcountry_code() {
        return this.appSharedPref.getString(this.country_code, "+91");
    }

    public void setcountry_code(String str) {
        this.prefEditor.putString(this.country_code, str).commit();
    }

    public String getage() {
        return this.appSharedPref.getString(this.age, "");
    }

    public void setage(String str) {
        this.prefEditor.putString(this.age, str).commit();
    }

    public String getgender() {
        return this.appSharedPref.getString(this.gender, "Male");
    }

    public void setgender(String str) {
        this.prefEditor.putString(this.gender, str).commit();
    }

    public String getpp() {
        return this.appSharedPref.getString(this.pp, "");
    }

    public void setpp(String str) {
        this.prefEditor.putString(this.pp, str).commit();
    }

    public String getloginpic() {
        return this.appSharedPref.getString(this.Loginpp, "");
    }

    public void setloginpic(String str) {
        this.prefEditor.putString(this.Loginpp, str).commit();
    }

    public String getUser() {
        return this.appSharedPref.getString(this.User, "");
    }

    public void setUser(String str) {
        this.prefEditor.putString(this.User, str).commit();
    }

    public String getEmail() {
        return this.appSharedPref.getString(this.Email, "");
    }

    public void setEmail(String str) {
        this.prefEditor.putString(this.Email, str).commit();
    }

    public String getSocialID() {
        return this.appSharedPref.getString(this.SocialID, "");
    }

    public void setSocialID(String str) {
        this.prefEditor.putString(this.SocialID, str).commit();
    }

    public String getLoginType() {
        return this.appSharedPref.getString(this.LoginType, "");
    }

    public void setLoginType(String str) {
        this.prefEditor.putString(this.LoginType, str).commit();
    }

    public String getShow_advice() {
        return this.appSharedPref.getString(this.Show_advice, "0");
    }

    public void setShow_advice(String str) {
        this.prefEditor.putString(this.Show_advice, str).commit();
    }

    public String getNativePriority() {
        return this.appSharedPref.getString(this.NativePriority, "Native");
    }

    public void setNativePriority(String str) {
        this.prefEditor.putString(this.NativePriority, str).commit();
    }

    public String getRoomName() {
        return this.appSharedPref.getString(this.RoomName, "");
    }

    public void setRoomName(String str) {
        this.prefEditor.putString(this.RoomName, str).commit();
    }

    public String getRateingGive() {
        return this.appSharedPref.getString(this.RateingGive, "false");
    }

    public void setRateingGive(String str) {
        this.prefEditor.putString(this.RateingGive, str).commit();
    }

    public String getTheme_version() {
        return this.appSharedPref.getString(this.Theme_version, "1");
    }

    public void setTheme_version(String str) {
        this.prefEditor.putString(this.Theme_version, str).commit();
    }

    public int getonemincoin() {
        return this.appSharedPref.getInt(this.OneMinCoin, 0);
    }

    public void setonemincoin(int i) {
        this.prefEditor.putInt(this.OneMinCoin, i).commit();
    }

    public int getNativecout() {
        return this.appSharedPref.getInt(this.Nativecout, 1);
    }

    public void setNativecout(int i) {
        this.prefEditor.putInt(this.Nativecout, i).commit();
    }

    public String getAM_NATIVE_Priority() {
        return this.appSharedPref.getString(this.AM_NATIVE_Priority, "AM");
    }

    public void setAM_NATIVE_Priority(String str) {
        this.prefEditor.putString(this.AM_NATIVE_Priority, str).commit();
    }

    public String getStartAppAds() {
        return this.appSharedPref.getString(this.StartAppAds, "");
    }

    public void setStartAppAds(String str) {
        this.prefEditor.putString(this.StartAppAds, str).commit();
    }

    public String getApp_version() {
        SharedPreferences sharedPreferences = this.appSharedPref;
        String str = this.App_version;
        return sharedPreferences.getString(str, "" + getVersion());
    }

    public void setApp_version(String str) {
        this.prefEditor.putString(this.App_version, str).commit();
    }

    public String getfource_download() {
        return this.appSharedPref.getString(this.fource_download, "false");
    }

    public void setfource_download(String str) {
        this.prefEditor.putString(this.fource_download, str).commit();
    }

    public String getStartApp_ads() {
        return this.appSharedPref.getString(this.StartApp_ads, "true");
    }

    public void setStartApp_ads(String str) {
        this.prefEditor.putString(this.StartApp_ads, str).commit();
    }

    public String getAdsCounter() {
        return this.appSharedPref.getString(this.AdsCounter, "1");
    }

    public void setAdsCounter(String str) {
        this.prefEditor.putString(this.AdsCounter, str).commit();
    }

    public String getAppMode() {
        return this.appSharedPref.getString(this.AppMode, ExifInterface.GPS_MEASUREMENT_3D);
    }

    public void setAppMode(String str) {
        this.prefEditor.putString(this.AppMode, str).commit();
    }

    public String getDeviceId() {
        return this.appSharedPref.getString(this.DeviceId, "");
    }

    public void setDeviceId(String str) {
        this.prefEditor.putString(this.DeviceId, str).commit();
    }

    public String getCallee() {
        return this.appSharedPref.getString(this.user_type, "");
    }

    public void setCallee(String str) {
        this.prefEditor.putString(this.user_type, str).commit();
    }

    public String getNewRegId() {
        return this.appSharedPref.getString(this.NewRegId, "");
    }

    public void setNewRegId(String str) {
        this.prefEditor.putString(this.NewRegId, str).commit();
    }

    public String getWebSocket() {
        return this.appSharedPref.getString(this.WebSocket, "");
    }

    public void setWebSocket(String str) {
        this.prefEditor.putString(this.WebSocket, str).commit();
    }

    public String getGoogleRTC_server_url() {
        return this.appSharedPref.getString(this.GoogleRTC_server_url, "https://appr.tc");
    }

    public void setGoogleRTC_server_url(String str) {
        this.prefEditor.putString(this.GoogleRTC_server_url, str).commit();
    }

    public String getWebSocketUrl() {
        return this.appSharedPref.getString(this.WebSocket_url, "");
    }

    public void setWebSocketUrl(String str) {
        this.prefEditor.putString(this.WebSocket_url, str).commit();
    }

    public String getSocket_url() {
        return this.appSharedPref.getString(this.Socket_url, "");
    }

    public void setSocket_url(String str) {
        this.prefEditor.putString(this.Socket_url, str).commit();
    }

    public String getToUser() {
        return this.appSharedPref.getString(this.ToUser, "");
    }

    public void setToUser(String str) {
        this.prefEditor.putString(this.ToUser, str).commit();
    }

    public String getFromUser() {
        return this.appSharedPref.getString(this.FromUser, "");
    }

    public void setFromUser(String str) {
        this.prefEditor.putString(this.FromUser, str).commit();
    }

    public String getFromUserpp() {
        return this.appSharedPref.getString(this.FromUserpp, "");
    }

    public void setFromUserpp(String str) {
        this.prefEditor.putString(this.FromUserpp, str).commit();
    }

    public String getOTP() {
        return this.appSharedPref.getString(this.OTP, "");
    }

    public void setOTP(String str) {
        this.prefEditor.putString(this.OTP, str).commit();
    }
}
