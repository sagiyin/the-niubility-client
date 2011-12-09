/**
 * 
 */
package edu.purdue.cs.voip.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * @author lzhen
 * 
 */
public class IncomingCall extends Activity {


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

//    final IncomingCall self = this;
//    setContentView(R.layout.incoming);
//
//    TextView textView = (TextView) findViewById(R.id.label);
    // will receive nickname
    // textView.setText(VOIP_ClientActivity.getCurrentIP());

    Button accept = (Button) findViewById(R.id.accept);
    Button reject = (Button) findViewById(R.id.reject);

    accept.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        acceptCall();
      }
    });

    reject.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        rejectCall();
      }
    });

  }

  public void acceptCall() {
    startActivity(new Intent(this, OutgoingCall.class));
  }

  public void rejectCall() {
    startActivity(new Intent(this, OnlineList.class));
  }

}
