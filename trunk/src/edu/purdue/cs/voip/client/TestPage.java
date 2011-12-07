/**
 * 
 */
package edu.purdue.cs.voip.client;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * @author lzhen
 *
 */
public class TestPage extends Activity {
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      TextView tv = new TextView(this);
      tv.setText("Wo jiu bu hui, zen me le?! ");
      setContentView(tv);
  }

}
