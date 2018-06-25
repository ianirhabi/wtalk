package com.example.irhabi.wtalk;

/**
 * Created by irhabi on 16/12/17.
 */
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;


public class register extends AppCompatActivity{
   private EditText penggunausername, penggunapassword ;
   private Button registerTombol;
   private String usernya, passnya ;
   private TextView pergikehalamanlogin;

   @Override
   protected void onCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_regis);

       penggunausername = (EditText)findViewById(R.id.emailnya);
       penggunapassword = (EditText)findViewById(R.id.masukpassword);
       registerTombol = (Button)findViewById(R.id.registombol);
       pergikehalamanlogin = (TextView) findViewById(R.id.login);

        Firebase.setAndroidContext(this);

        pergikehalamanlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent (register.this, Login.class));
            }
        });

        registerTombol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernya = penggunausername.getText().toString();
                passnya = penggunapassword.getText().toString();

                if (usernya.equals("")) {
                    penggunausername.setError("Username Tidak Boleh Kosong");
                } else if (passnya.equals("")) {
                    penggunapassword.setError("Password Tidak Boleh Kosong");
                } else if (!usernya.matches("[A-Za-z0-9]+")) {
                    penggunapassword.setError("Karakter Yang di Izinkan hanya huruf dan angka");
                } else if (usernya.length()< 5) {
                    penggunausername.setError("Panjang Minimal 5 Karakter");
                } else if (passnya.length() < 5) {
                    penggunapassword.setError("Panjang Password Minimal 5 Karakter");
                }
            else
            {

                final ProgressDialog pd = new ProgressDialog(register.this);
                pd.setMessage("Mohon Tunggu Sebentar");
                pd.show();

                String url = "https://intalk-4dfe0.firebaseio.com/proyekaplikasiuntukhoney.json";

                StringRequest request = new StringRequest(Request.Method.GET, url, new
                        Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                Firebase reference = new Firebase("https://intalk-4dfe0.firebaseio.com/proyekaplikasiuntukhoney");
                                if (s.equals("null")) {
                                    reference.child(usernya).child("password").setValue(passnya);
                                    Toast.makeText(register.this, "Registrasi Berhasil Silahkan Tekan Tombol Login", Toast.LENGTH_LONG).show();
                                } else {
                                    try {
                                        JSONObject obj = new JSONObject(s);

                                        if (!obj.has(usernya)) {
                                            reference.child(usernya).child("password").setValue(passnya);
                                            Toast.makeText(register.this, "Registrasi Berhasil Silahkan Tekan Tombol Login", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(register.this, "Username Sudah Ada Silahkan Gunakan Yang Lain", Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                pd.dismiss();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("" + error);
                        pd.dismiss();
                    }
                });

                RequestQueue rQue = Volley.newRequestQueue(register.this);
                rQue.add(request);
            }
        }
   });
 }
}
