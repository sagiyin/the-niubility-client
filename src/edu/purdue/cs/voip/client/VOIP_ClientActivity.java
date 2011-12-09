package edu.purdue.cs.voip.client;

import java.io.File;
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

    String serverIP;
    ((EditText) findViewById(R.id.serverIP)).setText("128.10.25.214");
    try {
      Scanner s = new Scanner(new File(FILENAME));
      serverIP = s.next();

      ((EditText) findViewById(R.id.serverIP)).setText(serverIP);

    } catch (Exception e) {
      e.printStackTrace();
    }

    ((Button) findViewById(R.id.next)).setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {

        if (((((EditText) findViewById(R.id.serverIP)).getText().toString().length())) == 0) {
        } else {
          String textField = ((EditText) findViewById(R.id.serverIP)).getText().toString();
//          Scanner s = new Scanner(textField);
          String serverIP = textField;
//          int port = Integer.parseInt(s.next());

          UserThread.getInstance().initialize(serverIP, 8888, self);
          UserThread.getInstance().start();
        }
      }
    });
  }

  public UserThread getUserThread() {
    return userThread;
  }

  public void switchToOnlineList() {
    // save the valid ip & port
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