package com.example.asus.rim;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class Applications {
    private DatabaseReference applicationReference;
    private String status;
    private String location;

    public Applications(final DatabaseReference applicationReference) {
        this.applicationReference = applicationReference;
    }

    public String getStatus() {
        applicationReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                status = dataSnapshot.child("status").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return status;
    }

    public String getLocation() {
        applicationReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                location = dataSnapshot.child("location").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return location;
    }

    public String getName() {
        return applicationReference.getKey();
    }

    public void setStatus(String status) {
        this.status = status;
        applicationReference.child("status").setValue(status);
    }


}
