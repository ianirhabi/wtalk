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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class kontak extends Activity{
    private ListView userList;
    TextView noUsersText;
    ArrayList<String> al = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tampilan_kontak);

        userList = (ListView)findViewById(R.id.userList);
        noUsersText = (TextView)findViewById(R.id.noUsersText);


        pd = new ProgressDialog(kontak.this);
        pd.setMessage("Mohon Tunggu");
        pd.show();

        String url = "https://intalk-4dfe0.firebaseio.com/proyekaplikasiuntukhoney.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                doOnSuccess(response);
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("" + error);
            }
        });

        RequestQueue rQuue = Volley.newRequestQueue(kontak.this);
        rQuue.add(request);

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                UserDetails.chatWith = al.get(position);
                startActivity(new Intent(kontak.this, MainActivity.class));
            }
        });
    }

    public void doOnSuccess(String s){
        try{
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while(i.hasNext()){
                key = i.next().toString();

                if(!key.equals(UserDetails.username)){
                    al.add(key);
                }
                totalUsers++;
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        if(totalUsers <=1){
            noUsersText.setVisibility(View.VISIBLE);
            userList.setVisibility(View.GONE);
        }
        else{
            noUsersText.setVisibility(View.GONE);
            userList.setVisibility(View.VISIBLE);
            userList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al ));

        }
        pd.dismiss();
    }

}
