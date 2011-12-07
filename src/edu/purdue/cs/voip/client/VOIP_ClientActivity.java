package edu.purdue.cs.voip.client;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class VOIP_ClientActivity extends ListActivity {
  /** Called when the activity is first created. */
  // get current online user here
  private String[] onlineUsers = { "lzhen", "SG", "BB" };
  private static String currentIP = null;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final VOIP_ClientActivity self = this;
    setListAdapter(new ArrayAdapter<String>(this, R.layout.main, onlineUsers));

    ListView lv = getListView();
    lv.setTextFilterEnabled(true);

    lv.setOnItemClickListener(new OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(self, OutgoingCall.class));
        
        currentIP = (String) ((TextView) view).getText();

        // When clicked, show a toast with the TextView text
        // Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
        // Toast.LENGTH_SHORT).show();
      }
    });
  }
  
  public static String getCurrentIP(){
    return currentIP;
  }

  // @Override
  // public void onCreate(Bundle savedInstanceState) {
  // super.onCreate(savedInstanceState);
  // final VOIP_ClientActivity self = this;
  // Button goTo = new Button(this);
  //
  // goTo.setText("goTo");
  // goTo.setOnClickListener(new View.OnClickListener() {
  // public void onClick(View v) {
  // startActivity(new Intent(self, TestPage.class));
  // }
  // });
  // setContentView(R.layout.main);
  // }

}