package justhealth.jhapp;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.CalendarContract;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Stephen on 06/01/15.
 */
public class SelfArchivedAppointments extends Activity {

    /**
     * This method runs when the page is first loaded. Sets the correct xml layout and sets the
     * correct custom action bar. Runs the printArchivedAppointments method.
     *
     * @param savedInstanceState a bundle if the state of the application was to be saved.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.self_appointments);
        // Inflate your custom layout
        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.appointment_action_bar, null);

        // Set up your ActionBar
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Past Appointments");

        // You customization
        final int actionBarColor = getResources().getColor(R.color.action_bar);
        actionBar.setBackgroundDrawable(new ColorDrawable(actionBarColor));

        printArchivedAppointments();
    }

    /**
     * Creates the action bar items for the SelfArchived Appointments page
     *
     * @param menu The options menu in which the items are placed
     * @return True must be returned in order for the options menu to be displayed
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_self_archived_appointments, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This method is called when any action from the action bar is selected
     *
     * @param item The menu item that was selected
     * @return in order for the method to work, true should be returned here
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.upcoming:
                //Intent future = new Intent(SelfArchivedAppointments.this, SelfAppointments.class);
                finish();
                return true;
            case R.id.add:
                startActivity(new Intent(SelfArchivedAppointments.this, CreateSelfAppointment.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This method loops through the Array that passed with the Intent and adds them all to a HashMap.
     * Depending on the Filter that is selected it then checks who the creator of the appointment is and runs the addToView method.
     */
    private void printArchivedAppointments() {
        Bundle intentExtras = getIntent().getExtras();
        String appointments = intentExtras.getString("appointments");

        JSONArray arrayApps = null;
        try {
            arrayApps = new JSONArray(appointments);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < arrayApps.length(); i++) {
            try {
                JSONObject obj = arrayApps.getJSONObject(i);
                final String appid = obj.getString("appid");
                final String name = obj.getString("name");
                final String appType = obj.getString("apptype");
                final String startDate = obj.getString("startdate");
                final String startTime = obj.getString("starttime");
                final String endDate = obj.getString("enddate");
                final String endTime = obj.getString("endtime");
                final String address = obj.getString("addressnamenumber");
                final String postcode = obj.getString("postcode");
                final String description = obj.getString("description");
                final String isPrivate = obj.getString("private");
                final String androidId = obj.getString("androideventid");

                final HashMap<String, String> appDetails = new HashMap<>();
                appDetails.put("appid", appid);
                appDetails.put("name", name);
                appDetails.put("appType", appType);
                appDetails.put("startDate", startDate);
                appDetails.put("startTime", startTime);
                appDetails.put("endDate", endDate);
                appDetails.put("endTime", endTime);
                appDetails.put("addressNameNumber", address);
                appDetails.put("postcode", postcode);
                appDetails.put("details", description);
                appDetails.put("private", isPrivate);
                appDetails.put("androidId", androidId);

                Date appDateTime = getDateTimeObject(startDate, startTime);
                Date now = new Date();
                if (appDateTime.before(now)) {

                    ContextThemeWrapper newContext = new ContextThemeWrapper(getBaseContext(), R.style.primaryButton);
                    Button app = new Button(newContext);
                    app.setBackgroundColor(Color.rgb(51, 122, 185));
                    app.setText(name + " " + startDate + " " + startTime);
                    LinearLayout layout = (LinearLayout) findViewById(R.id.upcomingAppointmentView);
                    layout.addView(app, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

                    LinearLayout.LayoutParams center = (LinearLayout.LayoutParams) app.getLayoutParams();
                    center.gravity = Gravity.CENTER;
                    center.setMargins(0,30,0,0);
                    app.setLayoutParams(center);

                    System.out.println("onclick listener applied");
                    app.setOnClickListener(new Button.OnClickListener() {
                        public void onClick(View view) {
                            appointmentAction(appDetails);
                        }
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method creates the dialog box when an appointment is clicked.
     *
     * @param appointmentDetails this is the HashMap of the appointment that has been pressed
     */
    private void appointmentAction(final HashMap<String, String> appointmentDetails) {
        System.out.println("method running");
        AlertDialog.Builder alert = new AlertDialog.Builder(SelfArchivedAppointments.this);
        alert.setTitle("Appointment Options")
                .setItems(R.array.patient_appointments_options, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            //View appointment
                            HashMap<String, Integer> appStart = getDateTimeFormat(appointmentDetails.get("startDate"), "00:00");
                            Calendar start = Calendar.getInstance();
                            start.set(appStart.get("year"), appStart.get("month"), appStart.get("day"), appStart.get("hour"), appStart.get("minute"));
                            Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
                            builder.appendPath("time");
                            ContentUris.appendId(builder, start.getTimeInMillis());
                            Intent intent = new Intent(Intent.ACTION_VIEW)
                                    .setData(builder.build());
                            startActivity(intent);
                        } else if (which == 1) {
                            //Edit appointment
                            Intent intent = new Intent(SelfArchivedAppointments.this, EditSelfAppointment.class);
                            intent.putExtra("appointmentDetails", appointmentDetails);
                            startActivity(intent);
                        } else if (which == 2) {
                            //Delete appointment
                            AlertDialog.Builder alert = new AlertDialog.Builder(SelfArchivedAppointments.this);

                            alert.setTitle("Delete Appointment");
                            alert.setMessage("Are you sure you want to delete this Appointment?");

                            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    deleteAppointment(appointmentDetails);
                                }
                            });

                            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // Cancelled.
                                }
                            });

                            alert.show();
                        }
                        //others to be added here
                    }
                });
        alert.show();
    }


    /**
     * This is run when the user selects to delete the appointment.
     * It checks whether the appointment has been added to the users calendar. If so this is deleted.
     * A post request is also made to the API which subsequently removes the event from the calendar.
     *
     * @param appointmentDetails A HashMap of the appointment to be deleted
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void deleteAppointment(HashMap<String, String> appointmentDetails) {
        //The API takes the username and AppID
        appointmentDetails.get("appid");
        SharedPreferences account = getSharedPreferences("account", 0);
        String username = account.getString("username", null);

        HashMap<String, String> details = new HashMap<String, String>();

        details.put("username", username);
        details.put("appid", appointmentDetails.get("appid"));
        //should return "Appointment Deleted"
        String postRequest = Request.post("deleteAppointment", details, this);
        System.out.println(appointmentDetails.get("androidId"));
        if (appointmentDetails.get("androidId") != "null") {
            long eventID = Long.parseLong(appointmentDetails.get("androidId"));
            ContentResolver cr = getContentResolver();
            ContentValues values = new ContentValues();
            Uri deleteUri = null;
            deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
            int rows = getContentResolver().delete(deleteUri, null, null);

            if (rows > 0) {
                if (postRequest.equals("Appointment Deleted")) {
                    //show the alert to say it is successful
                    Context context = getApplicationContext();
                    CharSequence text = "Appointment Deleted.";
                    //Length
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, text, duration);
                    //Position
                    toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 100);
                    toast.show();
                }
            }
        } else {
            if (postRequest.equals("Appointment Deleted")) {
                //show the alert to say it is successful
                Context context = getApplicationContext();
                CharSequence text = "Appointment Deleted.";
                //Length
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, text, duration);
                //Position
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 100);
                toast.show();
            }
        }

        finish();
        startActivity(getIntent());


    }
    /**
     * This method takes the date and time from the JustHealth database and adds each part to a HashMap.
     * This is needed when adding the appointment to the native android calendar.
     *
     * @param date the date of the appointment to be added to the android calendar
     * @param time the time of the appointment to be added to the android calendar
     * @return a HashMap of the date and time of the appointment
     */
    private HashMap<String, Integer> getDateTimeFormat(String date, String time) {
        HashMap<String, Integer> formattedDateTime = new HashMap<>();

        System.out.println(date);
        System.out.println(time);
        Integer year = Integer.parseInt(date.substring(0, 4));
        Integer month = Integer.parseInt(date.substring(5, 7));
        month -= 1;  //because January = 0... December = 11
        Integer day = Integer.parseInt(date.substring(8, 10));
        Integer hour = Integer.parseInt(time.substring(0, 2));
        Integer minute = Integer.parseInt(time.substring(3, 5));

        formattedDateTime.put("year", year);
        formattedDateTime.put("month", month);
        formattedDateTime.put("day", day);
        formattedDateTime.put("hour", hour);
        formattedDateTime.put("minute", minute);
        return formattedDateTime;
    }

    /**
     * This method takes the date and time as a string, concatenates it and returns it as an android date/time format.
     *
     * @param date the string of the date
     * @param time the string of the time
     * @return a Date object of the combined date and time strings
     */
    private Date getDateTimeObject(String date, String time) {
        String dateTime = date + " " + time;
        System.out.println("string: " + dateTime);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date newDate = format.parse(dateTime);
            System.out.println("DateType: " + newDate);
            return newDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}

