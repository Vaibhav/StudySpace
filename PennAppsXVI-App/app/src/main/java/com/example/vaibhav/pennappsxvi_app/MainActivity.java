package com.example.vaibhav.pennappsxvi_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.PublishCallback;
import com.google.android.gms.nearby.messages.PublishOptions;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeCallback;
import com.google.android.gms.nearby.messages.SubscribeOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private TableLayout table;
    private DatabaseReference mDatabase;
    private FirebaseDatabase database;
    private ArrayList<String> locationsList = new ArrayList<String>();


    private static final String TAG = MainActivity.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;

    private static final int TTL_IN_SECONDS = 3 * 60; // Three minutes.
    private static final String KEY_UUID = "key_uuid";
    private static final Strategy PUB_SUB_STRATEGY = new Strategy.Builder()
            .setTtlSeconds(TTL_IN_SECONDS).build();

    private Switch mPublishSwitch;
    private Switch mSubscribeSwitch;
    private Message mPubMessage;
    private MessageListener mMessageListener;
    private ArrayAdapter<String> mNearbyDevicesArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_main);

        mSubscribeSwitch = (Switch) findViewById(R.id.subscribe_switch);
        mPublishSwitch = (Switch) findViewById(R.id.publish_switch);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        DatabaseReference devicesRef = mDatabase.child("aea925bad7343ffc").child("num_devices");
        DatabaseReference locationRef = mDatabase.child("aea925bad7343ffc").child("location");
        DatabaseReference AT1 = mDatabase.child("aea925bad7343ffc");

        table = (TableLayout) findViewById(R.id.MainTableLayout);
        String tmp = devicesRef.toString();
        Log.d(TAG, "Value of tmp: " + tmp);

        AT1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String locValue = (String) dataSnapshot.child("location").getValue();
                Long numDevicesValue = (Long) dataSnapshot.child("num_devices").getValue();

                Log.d(TAG, "Location Value is: " + locValue);
                Log.d(TAG, "Devices Value is: " + numDevicesValue);

                boolean locationFound = false;


                View view = table.getChildAt(1);
                if (view instanceof TableRow) {
                    // then, you can remove the the row you want...
                    // for instance...
                    TableRow row = (TableRow) view;
                    for (int j = 0; j < row.getChildCount(); j++) {
                        TextView tv = (TextView) row.getChildAt(0);
                        String curText = tv.getText().toString();
                        if (locValue == curText) {
                            locationFound = true;
                            TextView devicesView = (TextView) row.getChildAt(1);
                            devicesView.setText(numDevicesValue.toString());
                        }
                    }

                    if (!locationFound) {
                        int numberOfLocations = locationsList.size();
                        Log.d(TAG, "numofLocs Value is: " + numberOfLocations);
                        TableRow curRow = (TableRow) table.getChildAt(numberOfLocations + 1);

                        TextView curLocView = (TextView) curRow.getChildAt(0);
                        TextView curDevView = (TextView) curRow.getChildAt(1);

                        Log.d(TAG, "Location Value is: " + curLocView.getText());
                        Log.d(TAG, "Devices Value is: " + curDevView.getText());

                        curLocView.setText(locValue);
                        curDevView.setText(numDevicesValue.toString());
                    }


                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

}
