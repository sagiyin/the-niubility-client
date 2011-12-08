/**
 * 
 */
package edu.purdue.cs.voip.client;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author lzhen
 * 
 */
public class OnlineList extends ListActivity {

  // get current online user here
  private String[] onlineUsers = { "lzhen", "SG", "BB" };
  private static String currentIP = "temp";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.onlinelist);
    final OnlineList self = this;
    setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, onlineUsers));

    // Button endCall = (Button) findViewById(R.id.endCall);

    //ListView lv = getListView();
    final ListView lv = (ListView) findViewById(android.R.id.list);
    lv.setTextFilterEnabled(true);
    lv.setOnItemClickListener(new OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(self, OutgoingCall.class));

        currentIP = lv.getSelectedItem().toString();

        // When clicked, show a toast with the TextView text
        // Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
        // Toast.LENGTH_SHORT).show();
      }
    });
  }

  public static String getCurrentIP() {
    return currentIP;
  }

}
