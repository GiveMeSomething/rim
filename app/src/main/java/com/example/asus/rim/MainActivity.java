package com.example.asus.rim;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ai.api.AIConfiguration;
import ai.api.AIServiceException;
import ai.api.android.AIDataService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.ui.AIButton;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();

    final DatabaseReference light_test_status = databaseReference.child("light").child("status");
    final DatabaseReference light_desk_status = databaseReference.child("light_desk").child("status");
    final DatabaseReference light_bedroom_status = databaseReference.child("light_bedroom").child("status");

    final ai.api.android.AIConfiguration config = new ai.api.android.AIConfiguration("c71225c7a4954dd580946ec46855bc30",
            AIConfiguration.SupportedLanguages.English,
            ai.api.android.AIConfiguration.RecognitionEngine.System);

    AIButton micButton;
    TextView responseText;

    String requestSpeech;
    String resultSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkConnection();

        micButton = findViewById(R.id.micButton);
        responseText = findViewById(R.id.aiResponse);

        micButton.initialize(config);
        micButton.setResultsListener(new AIButton.AIButtonListener() {
            @Override
            public void onResult(AIResponse result) {
                if (result.getResult().getResolvedQuery().equals("")) {
                    Toast.makeText(MainActivity.this, "Say something", Toast.LENGTH_SHORT).show();
                } else {
                    requestSpeech = result.getResult().getResolvedQuery();
                    showResult(requestSpeech);
                }
            }

            @Override
            public void onError(AIError error) {
                Toast.makeText(MainActivity.this, "Could you say that again ?", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled() {

            }
        });
    }

    private void checkConnection() {
        if (!isOnline()) {
            displayNetworkSetting(MainActivity.this).show();
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private AlertDialog displayNetworkSetting(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("No Internet");
        builder.setPositiveButton("Wifi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 0);
            }
        });
        builder.setNegativeButton("Mobile Data", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(new Intent(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS), 0);
            }
        });

        return builder.create();
    }

    private void showResult(String request) {
        final AIDataService aiDataService = new AIDataService(MainActivity.this, config);
        final AIRequest aiRequest = new AIRequest();
        aiRequest.setQuery(request);

        Thread getSpeech = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final AIResponse aiResponse = aiDataService.request(aiRequest);
                    resultSpeech = aiResponse.getResult().getFulfillment().getSpeech();
                } catch (AIServiceException e) {
                    e.printStackTrace();
                }
            }
        });
        getSpeech.start();

        if (resultSpeech != null) {
            if (!resultSpeech.equals("")) {
                responseText.setText(resultSpeech);
            }
        } else {
            Toast.makeText(MainActivity.this, "Can you say that again", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateValueToDatabase(String targetObjectName, String status) {
        if (isOnline()) {

        }
    }

    private String getValueFromDatabase(String targetObjectName) {
        if (isOnline()) {

        }
        return null;
    }

    private String createTargetObjectName(String targetObject, String location) {
        return null;
    }

    private void responseHandler(String section, String usage, String targetObject) {

    }
}
