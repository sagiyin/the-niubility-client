package edu.purdue.cs.voip.client;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Scanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class VOIP_ClientActivity extends Activity {
  /** Called when the activity is first created. */

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    final VOIP_ClientActivity self = this;

    final String FILENAME = "login_info";
    String nickname;
    String serverIP;

    if (this.getFileStreamPath(FILENAME).exists()) {
      // exist, read from file

      try {
        // FileInputStream fis = openFileInput(FILENAME);
        Scanner s = new Scanner(new File(FILENAME));
        nickname = s.next();
        serverIP = s.next();

        ((EditText) findViewById(R.id.nickname)).setText(nickname);
        ((EditText) findViewById(R.id.serverIP)).setText(serverIP);

      } catch (Exception e) {
      }
    }

    ((Button) findViewById(R.id.next)).setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        // check whether both field is filled
        //Log.v("VOIP_A","fuck");
        //Log.v("VOIP_A",((EditText) findViewById(R.id.nickname)).getText().toString());
        
        if (((((EditText) findViewById(R.id.nickname)).getText()) == null)
            || ((((EditText) findViewById(R.id.serverIP)).getText())) == null) {
        } else {

          // output to the file
          try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(((EditText) findViewById(R.id.nickname)).getText().toString().getBytes());
            fos.write("\n".getBytes());
            fos.write(((EditText) findViewById(R.id.serverIP)).getText().toString().getBytes());
            fos.close();
          } catch (Exception e) {
          }

          // connect to the server, jump to onlinelist page

          startActivity(new Intent(self, OnlineList.class));
        }
      }

    });
    // String FILENAME = "hello_file";
    // String string = "hello world!";
    //

  }

}