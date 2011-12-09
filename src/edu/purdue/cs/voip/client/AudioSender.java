package edu.purdue.cs.voip.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

public class AudioSender extends Thread {
  private static final AudioSender instance = new AudioSender();
  
  Activity clientActivity;
  DatagramSocket socket;
  DatagramPacket packet;
  int buf_size = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO,
      AudioFormat.ENCODING_PCM_16BIT)*4;
  AudioRecord record;
  boolean recording;
  InetAddress targetIP;
  int targetPort;
  
  public static AudioSender getInstance() {
    return instance;
  }
  
  private AudioSender() {}
  
  public void init(Activity clientActivity, InetAddress targetIP, int targetPort) {
    instance.clientActivity = clientActivity;
    instance.recording = false;
    try {
      instance.targetIP = targetIP;
      instance.targetPort = targetPort;
      instance.socket = new DatagramSocket();
    } catch (SocketException e) {
      e.printStackTrace();
    }
  }

  public void startRecording() {
    if (instance.getState().equals(Thread.State.NEW)) {
      instance.start();
    }
    instance.record = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100, AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT, buf_size);
    instance.record.startRecording();
    instance.recording = true;
  }

  public void stopRecording() {
    instance.recording = false;
    instance.record.stop();
    instance.record.release();
  }

  public void run() {
    while (true) {
      if (instance.recording) {
        byte[] buffer = new byte[256];
        record.read(buffer, 0, buffer.length);
        packet = new DatagramPacket(buffer, buffer.length, instance.targetIP, instance.targetPort);
        
        try {
          instance.socket.send(packet);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}