package edu.purdue.cs.voip.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

public class AudioSender extends Thread {
  Activity clientActivity;
  DatagramSocket socket;
  DatagramPacket packet;
  int buf_size = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO,
      AudioFormat.ENCODING_PCM_16BIT);
  AudioRecord record;
  boolean recording;
  InetAddress targetIP;
  int targetPort;
  
  public AudioSender(Activity clientActivity, String targetIP, int targetPort) {
    this.clientActivity = clientActivity;
    this.recording = false;
    try {
      this.targetIP = InetAddress.getByName(targetIP);
      this.targetPort = targetPort;
      socket = new DatagramSocket();
    } catch (SocketException e) {
      e.printStackTrace();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
  }

  public void startRecording() {
    record = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100, AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT, buf_size);
    record.startRecording();
    this.recording = true;
  }

  public void stopRecording() {
    this.recording = false;
    record.stop();
    record.release();
  }

  public void run() {
    while (true) {
      if (recording) {
        byte[] buffer = new byte[256];
        record.read(buffer, 0, buffer.length);
        
        packet = new DatagramPacket(buffer, buffer.length, targetIP, targetPort);
        
        try {
          socket.send(packet);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}