/**
 * 
 */
package edu.purdue.cs.voip.client;

import java.net.InetAddress;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    
    Button sendMessage = (Button) findViewById(R.id.sendText);
    
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
        UserThread.getInstance().clearMessages();
        startActivity(new Intent(self, OnlineList.class));
      }
    });
    
    final TextView chatBox = ((TextView) (findViewById(R.id.chatBox)));
    final EditText inputBox = ((EditText) (findViewById(R.id.inputBox)));
    
   
    sendMessage.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        UserThread.getInstance().sendMessage(remoteIP.getHostAddress(), inputBox.getText().toString());
        inputBox.setText("");
        chatBox.setText(UserThread.getInstance().getMessages());
      }
    });
    
    chatBox.setOnTouchListener(new OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        chatBox.setText(UserThread.getInstance().getMessages());
        return true;
      }
    });
    
  }
}
