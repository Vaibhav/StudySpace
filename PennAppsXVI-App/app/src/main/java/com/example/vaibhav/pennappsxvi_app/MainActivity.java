package com.example.vaibhav.pennappsxvi_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TableLayout table;
    private DatabaseReference mDatabase;
    private FirebaseDatabase database;
    private ArrayList<String> locationsList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Log.d("TESTING", "Before Database");
        DatabaseReference devicesRef = mDatabase.child("aea925bad7343ffc").child("num_devices");
        DatabaseReference locationRef = mDatabase.child("aea925bad7343ffc").child("location");
        DatabaseReference AT1 = mDatabase.child("aea925bad7343ffc");
        Log.d("TESTING", "After Database");

        table = (TableLayout) findViewById(R.id.MainTableLayout);
        String tmp = devicesRef.toString();
        Log.d("TESTING", "Value of tmp: " + tmp);

        /*
        devicesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Long value = (Long)dataSnapshot.getValue();
                Log.d("TESTING", "Value is: " + value);

                for(int i = 0; i < table.getChildCount(); i++) {
                    View view = table.getChildAt(i);
                    if (view instanceof TableRow) {
                        // then, you can remove the the row you want...
                        // for instance...
                        TableRow row = (TableRow) view;
                        for(int j = 0; j < row.getChildCount(); j++) {

                            TextView tv = (TextView)row.getChildAt(1);
                            tv.setText(value.toString());

                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TESTING", "Failed to read value.", error.toException());
            }
        });
        */

        AT1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String locValue = (String)dataSnapshot.child("location").getValue();
                Long numDevicesValue = (Long)dataSnapshot.child("num_devices").getValue();

                Log.d("TESTING", "Location Value is: " + locValue);
                Log.d("TESTING", "Devices Value is: " + numDevicesValue);

                boolean locationFound = false;

                for(int i = 1; i < table.getChildCount(); i++) {
                    View view = table.getChildAt(i);
                    if (view instanceof TableRow) {
                        // then, you can remove the the row you want...
                        // for instance...
                        TableRow row = (TableRow) view;
                        for(int j = 0; j < row.getChildCount(); j++) {
                            TextView tv = (TextView) row.getChildAt(0);
                            String curText = tv.getText().toString();
                            if (locValue == curText) {
                                locationFound = true;
                                TextView devicesView = (TextView)row.getChildAt(1);
                                devicesView.setText(numDevicesValue.toString());
                            }
                        }

                        if (!locationFound) {
                            int numberOfLocations = locationsList.size();
                            Log.d("TESTING", "numofLocs Value is: " + numberOfLocations);
                            TableRow curRow = (TableRow)table.getChildAt(numberOfLocations+1);

                            TextView curLocView = (TextView)curRow.getChildAt(0);
                            TextView curDevView = (TextView)curRow.getChildAt(1);

                            Log.d("TESTING", "Location Value is: " + curLocView.getText());
                            Log.d("TESTING", "Devices Value is: " + curDevView.getText());

                            curLocView.setText(locValue);
                            curDevView.setText(numDevicesValue.toString());
                        }


                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TESTING", "Failed to read value.", error.toException());
            }
        });

    }

    /*
    public void UpdateTable(String value){

        for(int i = 0; i < table.getChildCount(); i++) {
            View view = table.getChildAt(i);
            if (view instanceof TableRow) {

                TableRow row = (TableRow) view;

                for(int j = 0; j < row.getChildCount(); j++) {
                    TextView tv = (TextView)row.getChildAt(j);
                    tv.setText(value);
                }

            }
        }
    }
    */

}
