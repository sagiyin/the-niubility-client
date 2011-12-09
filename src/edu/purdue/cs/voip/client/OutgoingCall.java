/**
 * 
 */
package edu.purdue.cs.voip.client;

import java.net.InetAddress;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * @author lzhen
 * 
 */
public class OutgoingCall extends Activity {
  InetAddress remoteIP;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    final OutgoingCall self = this;
    setContentView(R.layout.outgoing);

//    TextView textView = (TextView) findViewById(R.id.label);
//    
//    ImageView userImage = (ImageView) findViewById(R.id.userimage);
    
    Button endCall = (Button) findViewById(R.id.endCall);
    
    remoteIP = UserThread.getInstance().getRemoteIP();
    AudioReceiver.getInstance().init(this, 8888);
    AudioSender.getInstance().init(this, remoteIP, 8888);
    
    AudioReceiver.getInstance().startPlay();
    AudioSender.getInstance().startRecording();
    
    endCall.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        UserThread.getInstance().sendEndCallRequest(remoteIP);
        AudioSender.getInstance().stopRecording();
        AudioReceiver.getInstance().stopPlay();
        startActivity(new Intent(self, OnlineList.class));
      }
    });

  }

}
