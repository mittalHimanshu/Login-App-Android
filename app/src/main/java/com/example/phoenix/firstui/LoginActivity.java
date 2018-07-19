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

public class LoginActivity extends AppCompatActivity {
    TextView username1 ,password1;
    String output;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username1 = (TextView) findViewById(R.id.username);
        password1 = (TextView) findViewById(R.id.password);
    }

    public void submitLoginData(View v){
        new ValidateLoginData().execute();
    }

    public void callSignUpPage(View v){
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
    }

    class ValidateLoginData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                String username = username1.getText().toString();
                String password = password1.getText().toString();
                URIBuilder builder = new URIBuilder("http://android-server-android-server.7e14.starter-us-west-2.openshiftapps.com");
                builder.setParameter("choice", "2");
                builder.setParameter("username", username);
                builder.setParameter("password", password);
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(builder.build());
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                output = EntityUtils.toString(httpEntity);
            } catch (URISyntaxException e) {
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
                if(s.equals("passmatch")){
                    Toast.makeText(getApplicationContext(), "Successfully Logged In", Toast.LENGTH_SHORT).show();
                }
                else if(s.equals("wrongpass")){
                    Toast.makeText(getApplicationContext(), "Password Incorrect", Toast.LENGTH_SHORT).show();
                    password1.requestFocus();
                    password1.setText("");
                }
                else if(s.equals("nouser")){
                    Toast.makeText(getApplicationContext(), "No username found", Toast.LENGTH_SHORT).show();
                    username1.requestFocus();
                    username1.setText("");
                    password1.setText("");
                }
                else{}
            }
            catch (NullPointerException e) {
                Toast.makeText(getApplicationContext(), e + "Server Error try again", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
