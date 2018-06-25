package com.example.irhabi.wtalk;

/**
 * Created by irhabi on 16/12/17.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class Login  extends  Activity{
    private TextView registerUser;
    private EditText username, password;
    private Button loginButton;
    private String user, pass, pengguna, sandinya;
    private SessionManager session;

    private TextInputLayout inputLayoutEmail, inputLayoutPassword;
    String email, passnya;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate (savedInstanceState);
        setContentView(R.layout.activity_login);

        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);

        session = new SessionManager(getApplicationContext());

        registerUser = (TextView) findViewById(R.id.register);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.loginButton);

        username.addTextChangedListener(new MyTextWatcher(username));
        password.addTextChangedListener(new MyTextWatcher(password));


        /**
         * mengecek user login session
         */
        if(session.isLoggedIn()==true){

            fungsi_masuk();
        }
        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, register.class));
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass = password.getText().toString();

                if (user.equals("")) {
                    password.setError("User Name Tidak Boleh Kosong!!");
                } else {
                    String url = "https://intalk-4dfe0.firebaseio.com/proyekaplikasiuntukhoney.json";
                    final ProgressDialog pd = new ProgressDialog(Login.this);
                    pd.setMessage("loading data...");
                    pd.show();

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String s) {
                            if (s.equals("null")) {
                                Toast.makeText(Login.this,
                                        "User Tidak Ditemukan", Toast.LENGTH_LONG).show();
                            } else {
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if (!obj.has(user)) {
                                        Toast.makeText(Login.this, "User Tidak Ada",
                                                Toast.LENGTH_LONG).show();
                                    } else if (obj.getJSONObject(user).getString("password").equals(pass)) {
                                        UserDetails.username = user;
                                        UserDetails.password = pass;

                                        session.createLoginSession(UserDetails.username, UserDetails.password);
                                        startActivity(new Intent(Login.this, kontak.class));

                                    } else {
                                        Toast.makeText(Login.this, "User Tidak Ada",
                                                Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            pd.dismiss();
                            ;
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError);
                            pd.dismiss();
                        }
                    });

                RequestQueue rQueue = Volley.newRequestQueue(Login.this);
                rQueue.add(request);
              }
            }
        });
    }


    private void fungsi_masuk(){
        HashMap<String, String> usersession = session.getUserDetails();

        email = usersession.get(SessionManager.KEY_EMAIL);
        passnya = usersession.get(SessionManager.KEY_PASS);

        String url = "https://intalk-4dfe0.firebaseio.com/proyekaplikasiuntukhoney.json";

        StringRequest request = new StringRequest(Request.Method.GET, url,new Response.Listener<String>(){

            @Override
            public void onResponse(String s){

                try{
                 JSONObject obj = new JSONObject(s);

                 if (!obj.has(email)){
                     Toast.makeText(Login.this,
                             "User Tidak Di temukan", Toast.LENGTH_LONG).show();
                 }
                 else if(obj.getJSONObject(email).getString("password").equals(passnya)) {
                     UserDetails.username = email;
                     UserDetails.password = passnya;
                     startActivity(new Intent(Login.this, kontak.class));
                     Toast.makeText(Login.this,
                             "Status Pengguna Sedang Masuk", Toast.LENGTH_LONG).show();
                    }
                 else{
                     Toast.makeText(Login.this,
                             "Username dan pass salah", Toast.LENGTH_LONG).show();
                 }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError){
                System.out.println(""+volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(Login.this);
        rQueue.add(request);


    }

    private class MyTextWatcher implements  TextWatcher{

        private View view ;

        private MyTextWatcher(View view){this.view = view;}

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){

        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){

        }

        public void  afterTextChanged(Editable editable){
            switch(view.getId()){
                case R.id.username:
                    break;
                case R.id.password:
                    break;
            }

        }

    }



}
