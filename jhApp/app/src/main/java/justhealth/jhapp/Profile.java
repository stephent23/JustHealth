package justhealth.jhapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Profile extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        final String profileInfo = getProfile();
        print(profileInfo);

        print(getProfile());

        //Edit Profile Link
        Button editProfile = (Button) findViewById(R.id.editProfile);
        editProfile.setOnClickListener(
            new Button.OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent(getBaseContext(), EditProfile.class);
                    intent.putExtra("profileInfo", profileInfo);
                    startActivityForResult(intent, 1);
                }
            }
        );
    }

    private String getProfile() {
        SharedPreferences account = getSharedPreferences("account", 0);
        String currentUser = account.getString("username", null);

        HashMap<String, String> profileInfo = new HashMap<String, String>();
        profileInfo.put("username", currentUser);

        return Request.post("getAccountInfo", profileInfo, getApplicationContext());
    }

    private void print(String profile) {

        try {
            JSONObject profileInfo = new JSONObject(profile);

            //Get TextViews
            TextView username = (TextView) findViewById(R.id.profileUsername);
            TextView firstName = (TextView) findViewById(R.id.profileFirstName);
            TextView surname = (TextView) findViewById(R.id.profileSurname);
            TextView dob = (TextView) findViewById(R.id.profileDOB);
            TextView gender = (TextView) findViewById(R.id.profileGender);
            TextView accountType = (TextView) findViewById(R.id.profileAccount);
            TextView email = (TextView) findViewById(R.id.profileEmail);

            //Populate TextViews
            username.setText(profileInfo.getString("username"));
            firstName.setText(profileInfo.getString("firstname"));
            surname.setText(profileInfo.getString("surname"));
            dob.setText(profileInfo.getString("dob"));
            gender.setText(profileInfo.getString("gender"));
            accountType.setText(profileInfo.getString("accounttype"));
            email.setText(profileInfo.getString("email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getExtras().containsKey("response")){
            Boolean success = false;
            if (resultCode == 1) { success = true;}
            Feedback.toast(data.getStringExtra("response"), success, getApplicationContext());
            System.out.println(data.getStringExtra("response"));
            finish();
            startActivity(getIntent());
        }
    }
}