package com.androidchatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

public class Register extends AppCompatActivity {
    EditText username, password,city,street,postalcode,houseno;
    String user, pass,cities,Stret,post,house;
    ImageView registerButton;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText)findViewById(R.id.etusername);
        password = (EditText)findViewById(R.id.etpassword);
        city = (EditText)findViewById(R.id.etcity);
        street = (EditText)findViewById(R.id.etstreetno);
        postalcode = (EditText)findViewById(R.id.etpostalcode);
        houseno = (EditText)findViewById(R.id.ethouseno);
        registerButton=(ImageView)findViewById(R.id.ImageView02) ;
        login=(TextView)findViewById(R.id.logintext);
        allClear();

        Firebase.setAndroidContext(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass = password.getText().toString();
                cities = city.getText().toString();
                Stret = street.getText().toString();
                post = postalcode.getText().toString();
                house = houseno.getText().toString();


                if(user.equals("")){
                    username.setError("can't be blank");
                }
                else if(pass.equals("")){
                        password.setError("can't be blank");
                    }
                    else if(!user.matches("[A-Za-z0-9]+")){
                            username.setError("only alphabet or number allowed");
                        }
                        else if(user.length()<5){
                                username.setError("at least 5 characters long");
                            }
                            else if(pass.length()<5){
                                password.setError("at least 5 characters long");
                            }
                            else {
                                final ProgressDialog pd = new ProgressDialog(Register.this);
                                pd.setMessage("Loading...");
                                pd.show();

                                String url = "https://myandroidchat-f5e61.firebaseio.com/users.json";

                                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                                    @Override
                                    public void onResponse(String s) {
                                        Firebase reference = new Firebase("https://myandroidchat-f5e61.firebaseio.com/users");
                                        allClear();
                                        if(s.equals("null")) {
                                            reference.child(user).child("password").setValue(pass);
                                            Toast.makeText(Register.this, "registration successful", Toast.LENGTH_LONG).show();
                                        }
                                        else {
                                            try {
                                                JSONObject obj = new JSONObject(s);

                                                if (!obj.has(user)) {
                                                    reference.child(user).child("password").setValue(pass);

                                                    Toast.makeText(Register.this, "registration successful", Toast.LENGTH_LONG).show();
                                                } else {
                                                    Toast.makeText(Register.this, "username already exists", Toast.LENGTH_LONG).show();
                                                }

                                            } catch (JSONException e) {
                                                    e.printStackTrace();
                                            }
                                        }

                                        pd.dismiss();
                                    }

                                },new Response.ErrorListener(){
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        System.out.println("" + volleyError );
                                        pd.dismiss();
                                    }
                                });

                                RequestQueue rQueue = Volley.newRequestQueue(Register.this);
                                rQueue.add(request);
                            }
            }
        });


    }

    public void allClear(){
        username.setText("");
        password.setText("");
        city.setText("");
        street.setText("");
        postalcode.setText("");
        houseno.setText("");
    }
}