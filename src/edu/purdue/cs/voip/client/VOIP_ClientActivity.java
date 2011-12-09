package edu.purdue.cs.voip.client;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Scanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class VOIP_ClientActivity extends Activity {
  /** Called when the activity is first created. */

  final private String FILENAME = "login_info";
  final private VOIP_ClientActivity self = this;
  private UserThread userThread;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    // final VOIP_ClientActivity self = this;
    String strIPPort;

    if (getFileStreamPath(FILENAME).exists()) {
        Scanner s;
        try {
          s = new Scanner(getFileStreamPath(FILENAME));
          strIPPort = s.next();
          ((EditText) findViewById(R.id.serverIP)).setText(strIPPort);
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }
    }

    //((EditText) findViewById(R.id.serverIP)).setText("128.10.25.221:8888");

    ((Button) findViewById(R.id.next)).setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {

        if (((((EditText) findViewById(R.id.serverIP)).getText().toString().length())) == 0) {
        } else {
          String textField = ((EditText) findViewById(R.id.serverIP)).getText().toString();
          Scanner s = new Scanner(textField);
          s.useDelimiter(":");
          String serverIP = s.next();
          int port = Integer.parseInt(s.next());

          UserThread.getInstance().initialize(serverIP, port, self);
          UserThread.getInstance().start();
        }
      }
    });
    
    Ringtone.getInstance().init(this);
    Ringtone.getInstance().start();
  }

  public UserThread getUserThread() {
    return userThread;
  }

  public void switchToOnlineList() {
    try {
      FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
      fos.write(((EditText) findViewById(R.id.serverIP)).getText().toString().getBytes());
      fos.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    startActivity(new Intent(this, OnlineList.class));
  }

}