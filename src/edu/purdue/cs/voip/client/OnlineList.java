/**
 *
 */
package edu.purdue.cs.voip.client;

import android.app.Activity;
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
public class OnlineList extends Activity {

  // get current online user here
  private String[] onlineUsers = { "lzhen", "SG", "BB" };
  private static String currentIP = null;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final OnlineList self = this;

    setContentView(R.layout.onlinelist);
    ListView lv = (ListView) findViewById(R.id.list);
    final ArrayAdapter<String> refreshList = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, onlineUsers);

    // By using setAdpater method in listview we an add string array in list.
    lv.setAdapter(refreshList);

    lv.setTextFilterEnabled(true);

    lv.setOnItemClickListener(new OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(self, OutgoingCall.class));

        currentIP = (String) ((TextView) view).getText();

      }
    });

    ((Button) (findViewById(R.id.update))).setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        onlineUsers[2] = "WORI";
        refreshList.notifyDataSetChanged();
      }
    });
  }

  public static String getCurrentIP() {
    return currentIP;
  }

}