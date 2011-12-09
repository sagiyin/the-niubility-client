/**
 * 
 */
package edu.purdue.cs.voip.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;

/**
 * @author lzhen
 * 
 */
public class IncomingCall extends Activity {

  ImageView myImageView;
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    
    Ringtone.getInstance().startPlaying();
    final IncomingCall self = this;
    setContentView(R.layout.incoming);

//    TextView textView = (TextView) findViewById(R.id.label);
    // will receive nickname
    // textView.setText(VOIP_ClientActivity.getCurrentIP());
    myImageView = (ImageView) findViewById(R.id.userimage);
    
//    Animation myFadeInAnimation = AnimationUtils.loadAnimation(null, R.anim.fade_in);
//    Animation myFadeOutAnimation = AnimationUtils.loadAnimation(null, R.anim.fade_out);

    final RotateAnimation anim = new RotateAnimation(0f, 350f, 15f, 15f);
    anim.setInterpolator(new LinearInterpolator());
    anim.setRepeatCount(Animation.INFINITE);
    anim.setDuration(700);
//fade it in, and fade it out. 
//    myImageView.startAnimation(myFadeInAnimation);
//    myImageView.startAnimation(myFadeOutAnimation);
    myImageView.startAnimation(anim);
    
    Button accept = (Button) findViewById(R.id.accept);
    Button reject = (Button) findViewById(R.id.reject);

    accept.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        Ringtone.getInstance().stopPlaying();
        myImageView.setAnimation(null);
        UserThread.getInstance().sendAcceptCall();
      }
    });

    reject.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        Ringtone.getInstance().stopPlaying();
        myImageView.setAnimation(null);
        UserThread.getInstance().sendDeclineCall();
        startActivity(new Intent(self, OnlineList.class));
      }
    });
  }
  
}
