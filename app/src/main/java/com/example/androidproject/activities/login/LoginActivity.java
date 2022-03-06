package com.example.androidproject.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidproject.R;
import com.example.androidproject.activities.mainactivity.MainActivity;
import com.goebl.david.Response;
import com.goebl.david.Webb;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import org.chromium.net.CronetEngine;
import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    ListeningExecutorService loginTask = MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());
    ProgressBar loginProgressBar;
    TextView userIdError;
    TextView passwordError;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Init Content
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i("myApp","Login activity created");



        loginProgressBar = (ProgressBar)findViewById(R.id.progressBarLogin);
        userIdError = (TextView)findViewById(R.id.userError);
        passwordError = (TextView)findViewById(R.id.passwordError);

        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ((TextView)findViewById(R.id.UserBox)).getText().toString();
                String password = ((TextView)findViewById(R.id.PasswordBox)).getText().toString();
                LoginActivity.this.doLogin(username,password);
            }
        });
    }

    void doLogin(String username, String password) {
        loginProgressBar.setVisibility(View.VISIBLE);
        try {
            ListenableFuture<Long> sas = loginTask.submit(() -> {
                Webb webb = Webb.create();

                Response<String> response = webb.post("http://192.168.1.63:8080/androidAppServer/autentica")
                        .param("user",username)
                        .param("password",password)
                        .ensureSuccess()
                        .asString();

                String result = response.getBody();
                Log.i("myApp", result);
                return Long.valueOf(result);
            });

            Futures.addCallback(sas, new FutureCallback<Long>() {
                @Override
                public void onSuccess(@Nullable Long result) {
                    loginProgressBar.setVisibility(View.GONE);
                    startMainActivity(result);
                    Log.i("myApp", "Result : " + result);
                }

                @Override
                public void onFailure(Throwable t) {
                    loginProgressBar.setVisibility(View.GONE);
                    Log.i("myApp", "Error : " + t.getMessage());
                    t.printStackTrace();
                }
            }, getMainExecutor());

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(LoginActivity.this,"Error login",Toast.LENGTH_LONG).show();
            Log.i("myApp",e.getMessage());
        }
    }

    private void startMainActivity(long matricolaDocente) {
        Intent activityStarter = new Intent(this, MainActivity.class);
        activityStarter.putExtra("matricolaDocente",matricolaDocente);
        startActivity(activityStarter);
    }

}