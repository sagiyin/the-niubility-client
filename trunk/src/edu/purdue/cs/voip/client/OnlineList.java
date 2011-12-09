/**
 *
 */
package edu.purdue.cs.voip.client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

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
import android.widget.Toast;

/**
 * @author lzhen
 * 
 */
public class OnlineList extends Activity {

  // get current online user here
  private List<String> onlineUsers;
  private ListView lv;
  final private OnlineList self = this;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    onlineUsers = new ArrayList<String>();

    setContentView(R.layout.onlinelist);
    lv = (ListView) findViewById(R.id.list);
    final ArrayAdapter<String> refreshList = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, onlineUsers);

    // By using setAdpater method in listview we an add string array in list.
    lv.setAdapter(refreshList);

    lv.setTextFilterEnabled(true);

    lv.setOnItemClickListener(new OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        InetAddress remoteIP;
        try {
          remoteIP = InetAddress.getByName((((TextView) view).getText()).toString());
          UserThread.getInstance().call(remoteIP);
        } catch (UnknownHostException e) {
          e.printStackTrace();
        }
      }
    });

    ((Button) (findViewById(R.id.update))).setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        UserThread.getInstance().sendRequestListAll();
        onlineUsers.clear();
        onlineUsers.addAll(UserThread.getInstance().getListClients());
        refreshList.notifyDataSetChanged();
      }
    });
    
    ((Button) (findViewById(R.id.exit))).setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        UserThread.getInstance().sendExitRequest();
        
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        
        System.exit(0);
      }
    });
  }
  
  public void switchToOutgoingCall()
  {
    startActivity(new Intent(this, OutgoingCall.class));
  }
  
  public OnlineList returnSelf()
  {
    return self;
  }
}