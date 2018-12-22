package com.example.asus.rim;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    final DatabaseReference light_test = databaseReference.child("light");
    final DatabaseReference light_desk = databaseReference.child("light_desk");
    final DatabaseReference light_bedroom = databaseReference.child("light_bedroom");

    final ai.api.android.AIConfiguration config = new ai.api.android.AIConfiguration("c71225c7a4954dd580946ec46855bc30",
            AIConfiguration.SupportedLanguages.English,
            ai.api.android.AIConfiguration.RecognitionEngine.System);

    AIButton micButton;
    TextView responseText;
    EditText chatBox;
    ImageButton sendButton;

    String requestSpeech;
    String resultSpeech;

    String currentApplicationName;
    String applicationLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkConnection();

        micButton = findViewById(R.id.micButton);
        responseText = findViewById(R.id.aiResponse);
        chatBox = findViewById(R.id.chatBox);
        sendButton = findViewById(R.id.sendButton);

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

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSpeech = chatBox.getText().toString();
                showResult(requestSpeech);
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

        RequestAIResponse getResponse = new RequestAIResponse(aiDataService, aiRequest);
        getResponse.start();

        final AIResponse aiResponse = getResponse.getAiResponse();

        AIResponseProcessor aiResponseProcessor = new AIResponseProcessor(aiResponse);
        resultSpeech = aiResponseProcessor.getText();

        if (resultSpeech != null) {
            if (!resultSpeech.equals("")) {
                responseText.setText(resultSpeech);
            }
        } else {
            Toast.makeText(MainActivity.this, "Can you say that again", Toast.LENGTH_SHORT).show();
        }
    }

//    private void updateValueToDatabase(String targetObjectName, String status) {
//        if (isOnline()) {
//
//        }
//    }
//
//    private String createTargetObjectName(String targetObject, String location) {
//        return null;
//    }
//
//    private void responseHandler(AIResponseProcessor aiResponseProcessor) {
//
//    }
}
