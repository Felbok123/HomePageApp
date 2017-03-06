package com.stefan.homepageapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FormActivity extends AppCompatActivity {
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent sendMail = new Intent(Intent.ACTION_SEND);
                    sendMail.setType("text/mail");
                    sendMail.putExtra(Intent.EXTRA_EMAIL, new String[]{"stefan.gustafsson@yh.nackademin.se"});
                    sendMail.putExtra(Intent.EXTRA_SUBJECT, "Mitt subject");
                    sendMail.putExtra(Intent.EXTRA_TEXT, "Hej Mitt Namn är ..");

                    startActivity(Intent.createChooser(sendMail, "Välj Epostprogam"));
                }
            });
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_home:
                Intent homeIntent = new Intent(FormActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                break;
            case R.id.action_Form:
                Intent formIntent = new Intent(FormActivity.this, FormActivity.class);
                startActivity(formIntent);
                break;
            case R.id.action_Info:
                Intent infoIntent = new Intent(FormActivity.this, InfoActivity.class);
                startActivity(infoIntent);
                break;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void getAttendees(View view) {
        final ArrayList<Attendees> attendeesList = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET, "http://nackevent.azurewebsites.net/getattendees", null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        try {

                            for (int i = 0; i < response.length(); i++) {


                                JSONObject j = response.getJSONObject(i);
                                Attendees attendee = new Attendees();
                                attendee.firstName = j.getString("firstName");
                                attendee.lastName = j.getString("lastName");

                                attendeesList.add(attendee);
                                count++;
                            }
                            final String c = String.valueOf(count);
                            ArrayAdapter<Attendees> arrayAdapter = new ArrayAdapter<>(FormActivity.this,
                                    android.R.layout.simple_list_item_1, android.R.id.text1, attendeesList);

                            final ListView listView = (ListView) findViewById(R.id.listView);
                            if (listView != null) {
                                listView.setAdapter(arrayAdapter);
                            }

                            if (listView != null) {
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Toast.makeText(FormActivity.this, c, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FormActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsArrayRequest);


    }
}
