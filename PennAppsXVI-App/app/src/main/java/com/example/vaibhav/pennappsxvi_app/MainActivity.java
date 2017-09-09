package com.example.vaibhav.pennappsxvi_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

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
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int TTL_IN_SECONDS = 3 * 60; // Three minutes.
    private static final String KEY_UUID = "key_uuid";
    private static final Strategy PUB_SUB_STRATEGY = new Strategy.Builder()
            .setTtlSeconds(TTL_IN_SECONDS).build();
    private TableLayout table;
    private DatabaseReference mDatabase;
    private FirebaseDatabase database;
    private ArrayList<String> locationsList = new ArrayList<String>();
    private GoogleApiClient mGoogleApiClient;
    private Switch mPublishSwitch;
    private Switch mSubscribeSwitch;
    private Message mPubMessage;
    private MessageListener mMessageListener;
    private ArrayAdapter<String> mNearbyDevicesArrayAdapter;

    private static String getUUID(SharedPreferences sharedPreferences) {
        String uuid = sharedPreferences.getString(KEY_UUID, "");
        if (TextUtils.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString();
            sharedPreferences.edit().putString(KEY_UUID, uuid).apply();
        }
        return uuid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_main);

        mPublishSwitch = (Switch) findViewById(R.id.publish_switch);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference AT1 = mDatabase.child("aea925bad7343ffc");

        table = (TableLayout) findViewById(R.id.MainTableLayout);


        // Build the message that is going to be published. This contains the device name and a
        // UUID.
        mPubMessage = DeviceMessage.newNearbyMessage(getUUID(getSharedPreferences(
                getApplicationContext().getPackageName(), Context.MODE_PRIVATE)));

        mMessageListener = new MessageListener() {
            @Override
            public void onFound(final Message message) {
                // Called when a new message is found.
                mNearbyDevicesArrayAdapter.add(
                        DeviceMessage.fromNearbyMessage(message).getMessageBody());
            }

            @Override
            public void onLost(final Message message) {
                // Called when a message is no longer detectable nearby.
                mNearbyDevicesArrayAdapter.remove(
                        DeviceMessage.fromNearbyMessage(message).getMessageBody());
            }
        };


        mPublishSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // If GoogleApiClient is connected, perform pub actions in response to user action.
                // If it isn't connected, do nothing, and perform pub actions when it connects (see
                // onConnected()).
                if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                    if (isChecked) {
                        publish();
                        table.setVisibility(View.VISIBLE);
                    } else {
                        unpublish();
                        table.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        final List<String> nearbyDevicesArrayList = new ArrayList<>();
        mNearbyDevicesArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                nearbyDevicesArrayList);

        buildGoogleApiClient();


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

    private void buildGoogleApiClient() {
        if (mGoogleApiClient != null) {
            return;
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Nearby.MESSAGES_API)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .build();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mPublishSwitch.setEnabled(false);
        mSubscribeSwitch.setEnabled(false);
        logAndShowSnackbar("Exception while connecting to Google Play services: " +
                connectionResult.getErrorMessage());
    }

    @Override
    public void onConnectionSuspended(int i) {
        logAndShowSnackbar("Connection suspended. Error code: " + i);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "GoogleApiClient connected");
        // We use the Switch buttons in the UI to track whether we were previously doing pub/sub (
        // switch buttons retain state on orientation change). Since the GoogleApiClient disconnects
        // when the activity is destroyed, foreground pubs/subs do not survive device rotation. Once
        // this activity is re-created and GoogleApiClient connects, we check the UI and pub/sub
        // again if necessary.
        if (mPublishSwitch.isChecked()) {
            publish();
            table.setVisibility(View.VISIBLE);
        }

    }


    /**
     * Publishes a message to nearby devices and updates the UI if the publication either fails or
     * TTLs.
     */
    private void publish() {
        Log.i(TAG, "Publishing");
        PublishOptions options = new PublishOptions.Builder()
                .setStrategy(PUB_SUB_STRATEGY)
                .setCallback(new PublishCallback() {
                    @Override
                    public void onExpired() {
                        super.onExpired();
                        Log.i(TAG, "No longer publishing");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mPublishSwitch.setChecked(false);
                            }
                        });
                    }
                }).build();

        Nearby.Messages.publish(mGoogleApiClient, mPubMessage, options)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            Log.i(TAG, "Published successfully.");
                        } else {
                            logAndShowSnackbar("Could not publish, status = " + status);
                            mPublishSwitch.setChecked(false);
                        }
                    }
                });
    }


    /**
     * Stops subscribing to messages from nearby devices.
     */
    private void unsubscribe() {
        Log.i(TAG, "Unsubscribing.");
        Nearby.Messages.unsubscribe(mGoogleApiClient, mMessageListener);
    }

    /**
     * Stops publishing message to nearby devices.
     */
    private void unpublish() {
        Log.i(TAG, "Unpublishing.");
        Nearby.Messages.unpublish(mGoogleApiClient, mPubMessage);
    }

    /**
     * Logs a message and shows a {@link Toast} using {@code text};
     *
     * @param text The text used in the Log message and the SnackBar.
     */
    private void logAndShowSnackbar(final String text) {
        Log.w(TAG, text);
        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
        toast.show();

    }

}
