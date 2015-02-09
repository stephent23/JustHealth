package justhealth.jhapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by charlottehutchinson on 06/11/14.
 */

public class Settings extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Settings");

        Button deactivate = (Button) findViewById(R.id.deactivate);
        deactivate.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View view) {
                        startActivity(new Intent(Settings.this, DeactivateAccount.class));
                    }
                }
        );

        Button privacy = (Button) findViewById(R.id.privacy);
        privacy.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View view) {
                        startActivity(new Intent(Settings.this, Privacy.class));
                    }
                }
        );

        Button profile = (Button) findViewById(R.id.profile);
        profile.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View view) {
                        startActivity(new Intent(Settings.this, Profile.class));
                    }
                }
        );

        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View view) {
                        Logout.logout(getApplicationContext());
                    }
                }
        );
    }

}
