package justhealth.jhapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Stephen on 25/11/2014.
 */
public class Connections extends Activity {

    private int rowOfTable = 0;

    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.connections);
        getConnections();
    }

    private void getConnections() {
        HashMap<String, String> getConnectionsInfo = new HashMap<String, String>();

        SharedPreferences account = getSharedPreferences("account", 0);
        String username = account.getString("username", null);

        getConnectionsInfo.put("username", username);

        String response = Request.post("getConnections", getConnectionsInfo, getApplicationContext());

        JSONObject queryReturn = null;
        JSONArray outgoing = null;
        JSONArray incoming = null;
        JSONArray completed = null;
        try {
            queryReturn = new JSONObject(response);
            System.out.println(queryReturn);
            String outgoingString = queryReturn.getString("outgoing");
            outgoing = new JSONArray(outgoingString);
            String incomingString = queryReturn.getString("incoming");
            incoming = new JSONArray(incomingString);
            String completedString = queryReturn.getString("completed");
            completed = new JSONArray(completedString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.print(queryReturn);
        printTableHeader();
        printOutgoingConnections(outgoing);
        printIncomingConnections(incoming);
        printCompletedConnections(completed);
    }

    private void printTableHeader() {
        TableLayout searchTable = (TableLayout) findViewById(R.id.connectionsTable);

        searchTable.removeAllViews();
        //create heading row
        TableRow head = new TableRow(this);
        //add properties to the header row
        head.setBackgroundColor(getResources().getColor(R.color.header));

        //create the headings of the table
        TextView headingUsername = new TextView(this);
        headingUsername.setTextColor(Color.WHITE);
        headingUsername.setPadding(5, 5, 5, 5);

        TextView headingFirstName = new TextView(this);
        headingFirstName.setTextColor(Color.WHITE);
        headingFirstName.setPadding(5, 5, 5, 5);

        TextView headingSurname = new TextView(this);
        headingSurname.setTextColor(Color.WHITE);
        headingSurname.setPadding(5, 5, 5, 5);

        TextView headingCode = new TextView(this);
        headingSurname.setTextColor(Color.WHITE);
        headingSurname.setPadding(5, 5, 5, 5);

        TextView headingAction = new TextView(this);
        headingAction.setTextColor(Color.WHITE);
        headingAction.setPadding(5, 5, 5, 5);

        headingUsername.setText("Username");
        headingFirstName.setText("First Name");
        headingSurname.setText("Surname");
        headingCode.setText("Code");
        headingAction.setText("Action");

        //add the headings to the row
        head.addView(headingUsername);
        head.addView(headingFirstName);
        head.addView(headingSurname);
        head.addView(headingCode);
        head.addView(headingAction);
        searchTable.addView(head, 0);
    }

    private void printOutgoingConnections(JSONArray outgoing) {
        TableLayout searchTable = (TableLayout) findViewById(R.id.connectionsTable);

        TableRow outgoingHeadRow = new TableRow(this);
        outgoingHeadRow.setBackgroundColor(getResources().getColor(R.color.header));
        TextView incomingConnections = new TextView(this);
        incomingConnections.setText("Outgoing Connections");
        incomingConnections.setTextColor(Color.WHITE);

        outgoingHeadRow.addView(incomingConnections);
        searchTable.addView(outgoingHeadRow, 1);
        //update row of table
        rowOfTable += 2;

        if (outgoing.length() == 0) {
            TableRow row = new TableRow(this);
            TextView noRecords = new TextView(this);
            noRecords.setText("No Outgoing Connections");
            row.addView(noRecords);
            searchTable.addView(row, rowOfTable);
            rowOfTable += 1;
        } else {

            for (int i = 0; i < outgoing.length(); i++) {
                try {
                    JSONObject obj = outgoing.getJSONObject(i);
                    System.out.println(obj);
                    String outgoingUsername = obj.getString("username");
                    String outgoingFirstName = obj.getString("firstname");
                    String outgoingSurname = obj.getString("surname");
                    String outgoingCode = obj.getString("code");


                    TableRow row = new TableRow(this);
                    //add username to TextView
                    TextView forUsername = new TextView(this);
                    forUsername.setText(outgoingUsername);
                    //add first name to TextView
                    TextView forFirstName = new TextView(this);
                    forFirstName.setText(outgoingFirstName);
                    //add surname to TextView
                    TextView forSurname = new TextView(this);
                    forSurname.setText(outgoingSurname);
                    //add verification code to TextView
                    TextView forCode = new TextView(this);
                    forCode.setText(outgoingCode);
                    //cancel request button
                    Button outgoingCancel = new Button(this);
                    outgoingCancel.setText("Cancel Request");
                    outgoingCancel.setTextColor(Color.WHITE);
                    outgoingCancel.setBackgroundColor(getResources().getColor(R.color.header));
                    outgoingCancel.setPadding(5, 5, 5, 5);
                    outgoingCancel.setOnClickListener(cancelOnClick(outgoingCancel, outgoingUsername));

                    //add the views to the row
                    row.addView(forUsername);
                    row.addView(forFirstName);
                    row.addView(forSurname);
                    row.addView(forCode);
                    row.addView(outgoingCancel);

                    searchTable.addView(row, rowOfTable);
                    rowOfTable += 1;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void printIncomingConnections(JSONArray incoming) {
        TableLayout searchTable = (TableLayout) findViewById(R.id.connectionsTable);

        TableRow incomingHeadRow = new TableRow(this);
        incomingHeadRow.setBackgroundColor(getResources().getColor(R.color.header));
        TextView incomingConnections = new TextView(this);
        incomingConnections.setText("Incoming Connections");
        incomingConnections.setTextColor(Color.WHITE);

        incomingHeadRow.addView(incomingConnections);
        searchTable.addView(incomingHeadRow, rowOfTable);
        //update row of table
        rowOfTable += 1;

        if (incoming.length() == 0) {
            TableRow row = new TableRow(this);
            TextView noRecords = new TextView(this);
            noRecords.setText("No Incoming Connections");
            row.addView(noRecords);
            searchTable.addView(row, rowOfTable);
            rowOfTable += 1;
        } else {

            for (int i = 0; i < incoming.length(); i++) {
                try {
                    JSONObject obj = incoming.getJSONObject(i);
                    System.out.println(obj);
                    String incomingUsername = obj.getString("username");
                    String incomingFirstName = obj.getString("firstname");
                    String incomingSurname = obj.getString("surname");
                    //add button


                    TableRow row = new TableRow(this);
                    //add username to TextView
                    TextView forUsername = new TextView(this);
                    forUsername.setText(incomingUsername);
                    //add first name to TextView
                    TextView forFirstName = new TextView(this);
                    forFirstName.setText(incomingFirstName);
                    //add surname to TextView
                    TextView forSurname = new TextView(this);
                    forSurname.setText(incomingSurname);
                    //add connect button
                    Button incomingConnect = new Button(this);
                    incomingConnect.setText("Connect");
                    incomingConnect.setTextColor(Color.WHITE);
                    incomingConnect.setBackgroundColor(getResources().getColor(R.color.header));
                    incomingConnect.setPadding(5, 5, 5, 5);
                    incomingConnect.setOnClickListener(connectOnClick(incomingConnect, incomingUsername));
                    //add what to do on click

                    //add the views to the row
                    row.addView(forUsername);
                    row.addView(forFirstName);
                    row.addView(forSurname);
                    row.addView(incomingConnect);

                    searchTable.addView(row, rowOfTable);
                    rowOfTable += 1;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void printCompletedConnections(JSONArray completed) {
        TableLayout searchTable = (TableLayout) findViewById(R.id.connectionsTable);

        TableRow incomingHeadRow = new TableRow(this);
        incomingHeadRow.setBackgroundColor(getResources().getColor(R.color.header));
        TextView incomingConnections = new TextView(this);
        incomingConnections.setText("Completed Connections");
        incomingConnections.setTextColor(Color.WHITE);

        incomingHeadRow.addView(incomingConnections);
        searchTable.addView(incomingHeadRow, rowOfTable);
        //update row of table
        rowOfTable += 1;

        if (completed.length() == 0) {
            TableRow row = new TableRow(this);
            TextView noRecords = new TextView(this);
            noRecords.setText("No Completed Connections");
            row.addView(noRecords);
            searchTable.addView(row, rowOfTable);
            rowOfTable += 1;
        } else {


            for (int i = 0; i < completed.length(); i++) {
                try {
                    JSONObject obj = completed.getJSONObject(i);
                    System.out.println(obj);
                    String completedUsername = obj.getString("username");
                    String completedFirstName = obj.getString("firstname");
                    String completedSurname = obj.getString("surname");


                    TableRow row = new TableRow(this);
                    //add username to TextView
                    TextView forUsername = new TextView(this);
                    forUsername.setText(completedUsername);
                    //add first name to TextView
                    TextView forFirstName = new TextView(this);
                    forFirstName.setText(completedFirstName);
                    //add surname to TextView
                    TextView forSurname = new TextView(this);
                    forSurname.setText(completedSurname);
                    //add connect button
                    Button completedRemove = new Button(this);
                    completedRemove.setText("Remove");
                    completedRemove.setTextSize(10);
                    completedRemove.setTextColor(Color.WHITE);
                    completedRemove.setBackgroundColor(getResources().getColor(R.color.header));
                    completedRemove.setPadding(5, 5, 5, 5);
                    completedRemove.setOnClickListener(removeOnClick(completedRemove, completedUsername));
                    //add what to do on click

                    //add the views to the row
                    row.addView(forUsername);
                    row.addView(forFirstName);
                    row.addView(forSurname);
                    row.addView(completedRemove);

                    searchTable.addView(row, rowOfTable);
                    rowOfTable += 1;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * This method is the action listener that is applied to the remove button for all of the completed connections.
     * It run the removeConnection method, changes the text on the button and stops the button being clicked again.
     *
     * @param button   the button that the onclick listener is applied too
     * @param username the username of the person that they want to remove as a connection
     */
    View.OnClickListener removeOnClick(final Button button, final String username) {
        return new View.OnClickListener() {
            public void onClick(View view) {
                if (removeConnection(username) == true) {
                    button.setText("Connection Removed");
                    button.setTextSize(11);
                    button.setClickable(false);
                } else {
                    System.out.println("Failed");
                    //add alert unable to be removed
                }
            }
        };
    }

    /**
     * This method is the action listener that is applied to the Cancel button for all of the outgoing connections.
     * It run the cancelOutgoingConnection method, changes the text on the button and stops the button being clicked again.
     *
     * @param button   the button that the onclick listener is applied too
     * @param username the username of the person that they want to remove as a connection
     */
    View.OnClickListener cancelOnClick(final Button button, final String username) {
        return new View.OnClickListener() {
            public void onClick(View view) {
                if (cancelOutgoingConnection(username) == true) {
                    button.setText("Request Cancelled");
                    button.setTextSize(11);
                    button.setClickable(false);
                } else {
                    System.out.println("Failed");
                    //add alert unable to be removed
                }
            }
        };
    }

    /**
     * This method is the action listener that is applied to the Cancel button for all of the outgoing connections.
     * It runs the connect method, changes the text on the button and stops the button being clicked again.
     *
     * @param button           the button that the onclick listener is applied too
     * @param incomingUsername the username of the person that they want to remove as a connection
     */
    View.OnClickListener connectOnClick(final Button button, final String incomingUsername) {
        return new View.OnClickListener() {
            public void onClick(View view) {
                enterCode(incomingUsername, "");
            }
        };
    }

    private boolean cancelOutgoingConnection(String connection) {
        HashMap<String, String> deleteConnection = new HashMap<String, String>();

        SharedPreferences account = getSharedPreferences("account", 0);
        String username = account.getString("username", null);

        deleteConnection.put("user", username);
        deleteConnection.put("connection", connection);

        String responseString = Request.post("cancelConnection", deleteConnection, getApplicationContext());

        if (responseString == "True") {
            return true;
        }
        return false;
    }

    private boolean removeConnection(String connection) {
        HashMap<String, String> deleteConnection = new HashMap<String, String>();

        SharedPreferences account = getSharedPreferences("account", 0);
        String username = account.getString("username", null);

        deleteConnection.put("user", username);
        deleteConnection.put("connection", connection);

        String response = Request.post("deleteConnection", deleteConnection, getApplicationContext());
        if (response == "True") {
            return true;
        }
        return false;
    }


    private void enterCode(final String incomingUser, String message) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Please enter the verification code given by the requester");
        alert.setMessage(message);
        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        //set the maximum length of the field to be 4 numbers
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(4);
        input.setFilters(filterArray);
        alert.setView(input);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Editable value = input.getText();
                String code = value.toString();
                // Do something with value!
                System.out.println(code);
                submitCode(incomingUser, code);

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Cancelled.
            }
        });

        alert.show();
    }

    /**
     * This submits the code that was entered into the alert box by the user to the API.
     * Providing that the code is correct it then refreshes the connections page and informs the user.
     * If an incorrect code has been entered it also tells the user.
     * @param requestor the username of the user that has requested the connection.
     * @param codeAttempt the code that has been entered by the current user.
     */
    private void submitCode(String requestor, String codeAttempt) {
        SharedPreferences account = getSharedPreferences("account", 0);
        String username = account.getString("username", null);

        HashMap<String, String> attemptParameters = new HashMap<String, String>();

        //add search to HashMap
        attemptParameters.put("username", username);
        attemptParameters.put("requestor", requestor);
        attemptParameters.put("codeattempt", codeAttempt);

        String response = Request.post("completeConnection", attemptParameters, getApplicationContext());

        if(response.equals("Incorrect")) {
            enterCode(requestor, "An incorrect code was entered. Please try again.");
        }
        else if(response.equals("Correct")) {
            finish();
            startActivity(getIntent());
        }
    }
}