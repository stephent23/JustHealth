package justhealth.jhapp;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class PatientCorrespondence extends Activity {

    //The notes associated with the patients
    JSONArray notes;

    /**
     * This method is initiated when the page is first loaded. It sets the correct xml layout and
     * displays the action bar.
     * Invokes the method, getNotes.
     *
     * @param savedInstanceState a bundle if the state of the application was to be saved.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_correspondence);

        // Set up your ActionBar
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Your Notes");

        //get Notes
        getNotes();

    }
    /**
     * This method makes a post request to the JustHealth API to retrieve all of the notes for a given user.
     * It then loops through the JSON Array that is returned from the server and adds them all to a HashMap.
     */
    private void getNotes() {
        SharedPreferences account = getSharedPreferences("account", 0);
        String username = account.getString("username", null);

        final HashMap<String, String> details = new HashMap<String, String>();

        //Text Boxes
        details.put("username", username);

        new AsyncTask<Void, Void, JSONArray>() {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                progressDialog = ProgressDialog.show(PatientCorrespondence.this, "Loading...", "Loading your notes", true);
            }

            @Override
            protected JSONArray doInBackground(Void... params) {
                try {
                    String response = Request.post("getPatientNotes", details, getApplicationContext());
                    return new JSONArray(response);
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(JSONArray response) {
                try {
                    super.onPostExecute(response);
                    notes = response;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();

                if (notes != null) {
                    for (int i = 0; i < notes.length(); i++) {
                        try {
                            JSONObject obj = notes.getJSONObject(i);
                            final String title = obj.getString("title");
                            final String date = obj.getString("datetime");
                            final String content = obj.getString("notes");
                            addToView(title, date, content);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.execute();
    }
    /**
     * This prints out the button for each of the appointments that are passed to the method.
     *
     * @param title the title of the created
     * @param date the date of the note created
     * @param content the content in the note created.
     */
    private void addToView(String title, String date, String content) {

        TextView textViewTitle = new TextView(this);
        textViewTitle.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textViewTitle.setText(title);
        textViewTitle.setTextColor(Color.rgb(51, 122, 185));
        textViewTitle.setTextSize(20);
        textViewTitle.setTypeface(null, Typeface.BOLD);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.correspondenceView);
        linearLayout.setBackgroundColor(Color.WHITE);
        linearLayout.addView(textViewTitle);


        TextView textViewDate = new TextView(this);
        textViewDate.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textViewDate.setText(date);
        textViewDate.setTextColor(Color.rgb(128, 128, 128));
        textViewTitle.setTextSize(20);
        textViewDate.setTypeface(null, Typeface.BOLD_ITALIC);

        linearLayout.addView(textViewDate);


        TextView textViewContent = new TextView(this);
        textViewContent.setText(content);
        textViewContent.setTextColor(Color.rgb(128, 128, 128));
        textViewTitle.setTextSize(18);
        textViewContent.setTypeface(null, Typeface.ITALIC);

        linearLayout.addView(textViewContent);

        LinearLayout.LayoutParams llp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp1.setMargins(0, 0, 0, 30); // llp.setMargins(left, top, right, bottom);    }
        TextView space1 = new TextView(this);
        space1.setLayoutParams(llp1);
        linearLayout.addView(space1);

        View v = new View(this);
        v.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 1));
        v.setBackgroundColor(Color.rgb(51, 51, 51));
        linearLayout.addView(v);

        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(50, 0, 0, 30); // llp.setMargins(left, top, right, bottom);    }
        TextView space = new TextView(this);
        space.setLayoutParams(llp);
        linearLayout.addView(space);
    }
}
