package justhealth.jhapp;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class Login extends Activity implements SurfaceHolder.Callback {

    private MediaPlayer mp = null;
    SurfaceView surfaceView = null;
    SurfaceHolder surfaceHolder = null;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(true);

        surfaceView = (SurfaceView)findViewById(R.id.loginVideoSurface);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        mp = new MediaPlayer();

        TextView register = (TextView) findViewById(R.id.link_to_forgot_password);
        register.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        startActivity(new Intent(Login.this, ForgotPassword.class));
                    }
                }
        );

        TextView forgotPassword = (TextView) findViewById(R.id.link_to_register);
        forgotPassword.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        startActivity(new Intent(Login.this, Register.class));
                    }
                }
        );

        Button loginButton = (Button) findViewById(R.id.login);
        loginButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        requestLogin();
                    }
                }
        );
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Thanks to: http://stackoverflow.com/questions/8830111/integrating-video-file-in-android-app-as-app-background
        try {
            mp.setDisplay(holder);
            mp.setDataSource(this, Uri.parse("android:resource://" + getPackageName() + "/" + R.raw.phone));
            mp.prepare();
            mp.start();
        } catch (IOException e) {
            System.out.println("Video load failed");
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder h) {System.out.println("1");}
    @Override
    public void surfaceChanged(SurfaceHolder h, int a, int b, int c) {System.out.println("1");}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_login, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.terms:
                startActivity(new Intent(Login.this, TermsAndConditions.class));
                return true;
            case R.id.privacy:
                startActivity(new Intent(Login.this, Privacy.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void requestLogin() {
        HashMap<String, String> loginInformation = new HashMap<String, String>();
        loginInformation.put("username", ((EditText) findViewById(R.id.loginUsername)).getText().toString());
        loginInformation.put("password", ((EditText) findViewById(R.id.loginPassword)).getText().toString());

        String response = Request.post("authenticate", loginInformation, getApplicationContext());

        if (response.equals("Authenticated")) {
            SharedPreferences account = getSharedPreferences("account", 0);
            SharedPreferences.Editor edit = account.edit();
            edit.putString("username", loginInformation.get("username"));
            edit.putString("password", loginInformation.get("password"));
            edit.commit();

            if (getAccountType(loginInformation.get("username")).equals("Patient")) {
                startActivity(new Intent(Login.this, HomePatient.class));
            } else if (getAccountType(loginInformation.get("username")).equals("Carer")) {
                startActivity(new Intent(Login.this, HomeCarer.class));
            } else {
            }
        }
        else {
            Feedback.toast(response, false, getApplicationContext());
        }
    }

    private String getAccountType(String username) {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("username", username);

        String response = Request.post("getAccountInfo", parameters, getApplicationContext());
        try {
            JSONObject accountDetails = new JSONObject(response);
            String accountType  = accountDetails.getString("accounttype");
            return accountType;
        }
        catch (JSONException e) {
            System.out.println(e.getStackTrace());
        }
        return null;
    }
}