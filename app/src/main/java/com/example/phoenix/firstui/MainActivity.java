package com.example.phoenix.firstui;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.HttpGet;
import java.io.IOException;
import java.net.URISyntaxException;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.utils.URIBuilder;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

public class MainActivity extends AppCompatActivity {

    TextView name1, username1 ,password1, email1, phone1;
    String output;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name1 = (TextView) findViewById(R.id.name);
        username1 = (TextView) findViewById(R.id.username);
        password1 = (TextView) findViewById(R.id.password);
        email1 = (TextView) findViewById(R.id.email);
        phone1 = (TextView) findViewById(R.id.phone);
    }

    public void submitData(View v){
        new ValidateData().execute();
    }

    public void setLoginPage(){
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
    }

    public void callLoginPage(View v){
        setLoginPage();
    }

    class ValidateData extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            try{
                String name = name1.getText().toString();
                String username = username1.getText().toString();
                String email = email1.getText().toString();
                String phone = phone1.getText().toString();
                String password = password1.getText().toString();
                URIBuilder builder = new URIBuilder("http://androidserver-androidserver.7e14.starter-us-west-2.openshiftapps.com");
                builder.setParameter("choice", "1");
                builder.setParameter("name", name);
                builder.setParameter("username", username);
                builder.setParameter("email", email);
                builder.setParameter("phone", phone);
                builder.setParameter("password", password);
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(builder.build());
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                output = EntityUtils.toString(httpEntity);
            }
            catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return output;
        }

        @Override
        protected void onPostExecute(String x) {
            String s = x.trim();
            try {
                if(s.equals("emptyfields")){
                    Toast.makeText(getApplicationContext(), "Please fill all the information", Toast.LENGTH_SHORT).show();
                }
                else if(s.equals("emailerror")){
                    email1.setError("Please enter correct Email Address");
                    email1.requestFocus();
                }
                else if(s.equals("phoneerror")){
                    phone1.setError("Please enter correct Phone No.");
                    phone1.requestFocus();
                }
                else if(s.equals("true")){
                    Toast.makeText(getApplicationContext(), "Successfully Registered", Toast.LENGTH_SHORT).show();
                    setLoginPage();
                }
                else if(s.equals("false")){
                    username1.setError("Username taken");
                    username1.requestFocus();
                }
                else{}
            }
            catch (NullPointerException e) {
                Toast.makeText(getApplicationContext(), e + "Server Error try again", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
