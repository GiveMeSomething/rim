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
import com.scaledrone.lib.Room;
import com.scaledrone.lib.RoomListener;
import com.scaledrone.lib.Scaledrone;

import ai.api.AIConfiguration;
import ai.api.AIServiceException;
import ai.api.android.AIDataService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.ui.AIButton;

public class MainActivity extends AppCompatActivity implements RoomListener {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();

    final DatabaseReference LIGHT_TEST = databaseReference.child("light");
    final DatabaseReference LIGHT_DESK = databaseReference.child("light_desk");
    final DatabaseReference LIGHT_BEDROOM = databaseReference.child("light_bedroom");

    AIButton micButton;
    EditText chatBox;
    ImageButton sendButton;

    String requestSpeech;
    String resultSpeech;

    private String channelID = "ePdCTlTAQ0mYPDZ5";
    private String roomName = "private-room";
    private Scaledrone scaledrone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkConnection();

        micButton = findViewById(R.id.micButton);
        chatBox = findViewById(R.id.chatBox);
        sendButton = findViewById(R.id.sendButton);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final String KEY_AI = getResources().getString(R.string.key_ai);
        final ai.api.android.AIConfiguration config = new ai.api.android.AIConfiguration(KEY_AI,
                AIConfiguration.SupportedLanguages.English,
                ai.api.android.AIConfiguration.RecognitionEngine.System);

        micButton.initialize(config);
        micButton.setResultsListener(new AIButton.AIButtonListener() {
            @Override
            public void onResult(AIResponse result) {
                if (result.getResult().getResolvedQuery().equals("")) {
                    Toast.makeText(MainActivity.this, "Say something", Toast.LENGTH_SHORT).show();
                } else {
                    requestSpeech = result.getResult().getResolvedQuery();
                    showResultFromAIDataRequest(requestSpeech);
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
                String chatboxMessage = chatBox.getText().toString();
                if (chatboxMessage.equals("")) {
                    Toast.makeText(MainActivity.this, "Can't send null message", Toast.LENGTH_LONG).show();
                } else {
                    showResultFromAIDataRequest(chatboxMessage);
                }
                chatBox.setText("");
            }
        });
    }

    // Successfully connected to Scaledrone room
    @Override
    public void onOpen(Room room) {
        System.out.println("Conneted to room");
    }

    // Connecting to Scaledrone room failed
    @Override
    public void onOpenFailure(Room room, Exception ex) {

    }

    // Received a message from Scaledrone room
    @Override
    public void onMessage(Room room, com.scaledrone.lib.Message receivedMessage) {

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

    private void showResultFromAIDataRequest(String request) {
        final String KEY_AI = getResources().getString(R.string.key_ai);
        final ai.api.android.AIConfiguration config = new ai.api.android.AIConfiguration(KEY_AI,
                AIConfiguration.SupportedLanguages.English,
                ai.api.android.AIConfiguration.RecognitionEngine.System);
        final AIDataService aiDataService = new AIDataService(MainActivity.this, config);
        final AIRequest aiRequest = new AIRequest();
        aiRequest.setQuery(request);

        Thread getSpeech = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final AIResponse aiResponse = aiDataService.request(aiRequest);
                    resultSpeech = aiResponse.getResult().getFulfillment().getSpeech();
                    if (resultSpeech != null) {
                        if (!resultSpeech.equals("")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                }
                            });
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Can you say that again", Toast.LENGTH_SHORT).show();
                    }
                } catch (AIServiceException e) {
                    e.printStackTrace();
                }
            }
        });
        getSpeech.start();
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
