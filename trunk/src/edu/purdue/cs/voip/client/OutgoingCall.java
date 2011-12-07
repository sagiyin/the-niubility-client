/**
 * 
 */
package edu.purdue.cs.voip.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author lzhen
 * 
 */
public class OutgoingCall extends Activity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    final OutgoingCall self = this;
    setContentView(R.layout.outgoing);

    TextView textView = (TextView) findViewById(R.id.label);
    textView.setText(OnlineList.getCurrentIP());
    
    ImageView userImage = (ImageView) findViewById(R.id.userimage);
    
    Button endCall = (Button) findViewById(R.id.endCall);

    endCall.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        startActivity(new Intent(self, VOIP_ClientActivity.class));
      }
    });

  }

}
