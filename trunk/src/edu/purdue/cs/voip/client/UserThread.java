/**
 * 
 */
package edu.purdue.cs.voip.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

/**
 * @author lzhen
 * 
 */
public class UserThread extends Thread {

  private static final UserThread instance = new UserThread();

  private Socket socket;
  private InputStream in;
  private OutputStream out;
  private BufferedReader incoming;
  private PrintStream outgoing;

  private Activity context;

  private InetAddress serverIP;
  private int port;

  private Deque<ClientRequest> queueOutgoing;
  private List<String> listClients = new ArrayList<String>();

  private InetAddress remoteIP;
  
  private StringBuffer messages = new StringBuffer();
  
  public static UserThread getInstance() {
    return instance;
  }

  private UserThread() {
  }

  public InetAddress getRemoteIP() {
    return remoteIP;
  }
  
  public void initialize(String serverIP, int port, Activity context) {
    this.context = context;
    try {
      this.serverIP = InetAddress.getByName(serverIP);
      this.port = port;
      queueOutgoing = new LinkedList<ClientRequest>();
    } catch (UnknownHostException e) {
      System.out.println("Unable to resolve server IP.");
      e.printStackTrace();
    }
  }

  public List<String> getListClients() {
    return listClients;
  }

  public void sendEndCallRequest(InetAddress remoteIP) {
    ClientRequest request = new ClientRequest();
    request.setRequestType(VOIPConstant.OP_REQUEST_DROP);
    request.setRequestTarget(remoteIP.getHostAddress());
    queueOutgoing.add(request);
  }
  
  public void sendRequestListAll() {
    ClientRequest request = new ClientRequest();
    request.setRequestType(VOIPConstant.REQUEST_LIST_ALL);
    queueOutgoing.add(request);
  }

  public void sendRealIP() {
    ClientRequest request = new ClientRequest();
    request.setRequestType(VOIPConstant.OP_REQUEST_SETREALLOCALIP);
    request.request_RealIp = socket.getLocalAddress().getHostAddress();
    queueOutgoing.add(request);
  }

  public void call(InetAddress remoteIP) {
    ClientRequest request= new ClientRequest();
    request.setRequestType(VOIPConstant.OP_REQUEST_CALL);
    request.setRequestTarget(remoteIP.getHostAddress());
    queueOutgoing.add(request);
  }
  
  public void sendAcceptCall() {
    ClientRequest request= new ClientRequest();
    request.setRequestType(VOIPConstant.OP_REQUEST_ACCEPT);
    request.setRequestTarget(remoteIP.getHostAddress());
    queueOutgoing.add(request);
  }
  
  public void sendDeclineCall() {
    ClientRequest request= new ClientRequest();
    request.setRequestType(VOIPConstant.OP_REQUEST_DECLINE);
    request.setRequestTarget(remoteIP.getHostAddress());
    queueOutgoing.add(request);
  }
  public void sendMessage(String remoteIP, String message) {
    ClientRequest request= new ClientRequest();
    request.setRequestType(VOIPConstant.OP_REQUEST_SENDMESSAGE);
    request.setRequestTarget(remoteIP);
    request.setRequestMessage(message);
    messages.append(socket.getLocalAddress().getHostAddress() + ":" + message + "\r\n");
    queueOutgoing.add(request); 
  }
  
  public void sendExitRequest() {
    ClientRequest request= new ClientRequest();
    request.setRequestType(VOIPConstant.OP_REQUEST_EXIT);
    queueOutgoing.add(request);
  }
  
  public String getMessages() {
    return messages.toString();
  }
  
  public void clearMessages() {
    messages = new StringBuffer();
  }
  public void run() {
    Gson gson = new Gson();
    try {
      socket = new Socket(serverIP, port);
      in = socket.getInputStream();
      out = socket.getOutputStream();
      incoming = new BufferedReader(new InputStreamReader(in));
      outgoing = new PrintStream(out);
    } catch (UnknownHostException e) {
      Log.e("Unknown Host", serverIP.toString());
      e.printStackTrace();
    } catch (IOException e) {
      Log.e("IOException", "Failed to create socket");
      e.printStackTrace();
    }

    if (socket != null) {
      if (context instanceof VOIP_ClientActivity)
        ((VOIP_ClientActivity) context).switchToOnlineList();
    } else {
      System.exit(-1);
    }
    sendRealIP();
    sendRequestListAll();
    String jsonString;

    while (true) {
      try {
        // Outgoing data
        while (!queueOutgoing.isEmpty()) {
          outgoing.println(gson.toJson(queueOutgoing.remove()));
          outgoing.flush();
        }

        // Incoming data
        while (incoming.ready()) {
          jsonString = incoming.readLine();
          Log.v("JSONResponse", jsonString);
          ServerResponse response = gson.fromJson(jsonString.toString(), ServerResponse.class);
          if (response.getResponseType().equals(VOIPConstant.RESPONSE_LIST_ALL)) {
            this.listClients = response.getListOfClients();
          } else if (response.getResponseType().equals(VOIPConstant.OP_REACH_CALLEE)) {
            this.remoteIP = InetAddress.getByName(response.getRequestTarget());
            context.startActivity(new Intent(context, IncomingCall.class));
          } else if (response.getResponseType().equals(VOIPConstant.OP_RESPONSE_CALL)) {
            if (response.getCalleeStatus() == VOIPConstant.CALLEE_STATUS_READY) {
              this.remoteIP = InetAddress.getByName(response.requestTarget);
              context.startActivity(new Intent(context, OutgoingCall.class));
            } else if (response.getCalleeStatus() == VOIPConstant.CALLEE_STATUS_DECLINE) {
              context.startActivity(new Intent(context, OnlineList.class));
            }
          } else if (response.getResponseType().equals(VOIPConstant.OP_RESPONSE_DROP)) {
            AudioSender.getInstance().stopRecording();
            AudioReceiver.getInstance().stopPlay();
            clearMessages();
            context.startActivity(new Intent(context, OnlineList.class));
          } else if (response.getResponseType().equals(VOIPConstant.OP_REACH_SENDMESSAGE)) {
            messages.append(response.getRequestTarget() + ": " + response.getReachMessage() + "\r\n");
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
