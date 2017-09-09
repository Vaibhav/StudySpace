package com.example.vaibhav.pennappsxvi_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("TESTING", "Value is: " + value);


                TableLayout table = (TableLayout) findViewById(R.id.MainTableLayout);
                for(int i = 0; i < table.getChildCount(); i++) {
                    View view = table.getChildAt(i);
                    if (view instanceof TableRow) {
                        // then, you can remove the the row you want...
                        // for instance...
                        TableRow row = (TableRow) view;
                        for(int j = 0; j < row.getChildCount(); j++) {
                            TextView tv = (TextView)row.getChildAt(j);
                            tv.setText(value);
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



}
