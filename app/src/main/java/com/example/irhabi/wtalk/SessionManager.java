package com.example.irhabi.wtalk;

/**
 * Created by irhabi on 16/12/17.
 */

import java.util.HashMap;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

@SuppressLint("CommitPrefEdits")
public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Nama sharedpreferences
    private static final String PREF_NAME = "Sesi";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";


    public static final String KEY_NAME = "nama";
    public static final  String KEY_EMAIL = "email" ;
    public static final  String KEY_PASS = "password";
    public static final String KEY_NAMA_CHAT_LAWAN = "penggunanya";

    //Constructor
    public SessionManager (Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);

        editor = pref.edit();
    }


    /**
     * Menciptakan login sesion
     */
    public void createLoginSession (String email, String pass){
        // Menyimpan login dengan nilai TRUE
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASS, pass);

        editor.commit();
    }

    /**
     * mengecek login dengan fungsi chekLogin() untuk status user
     * jika false user akan di arahkan ke halaman login
     * tapi jika else tidak akan melalakukan apapun
     */

    public void checkLogin(){
        //Check login status
        if(!this.isLoggedIn()){
            Intent i = new Intent(_context, Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            _context.startActivity(i);
        }
    }

    /**
     * menyimpan sesion data
     */

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();

        user.put (KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_PASS, pref.getString(KEY_PASS, null));
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_NAMA_CHAT_LAWAN, pref.getString(KEY_NAMA_CHAT_LAWAN, null));

        return user;
    }

    /**
     * Clear session details
     * fungsi ini untuk logout user
     */
    public void logoutUser(){
        // Clearing all data from  Shared Preferences
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }


}
