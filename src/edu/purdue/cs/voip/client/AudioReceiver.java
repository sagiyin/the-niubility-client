package edu.purdue.cs.voip.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class AudioReceiver extends Thread {
  private static final AudioReceiver instance = new AudioReceiver();
  
  Activity clientActivity;
  DatagramSocket socket;
  DatagramPacket packet;
  int buf_size = AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_OUT_MONO,
      AudioFormat.ENCODING_PCM_16BIT)*4;
  AudioTrack track;

  boolean playing;
  InetAddress targetIP;
  int listeningPort;

  public static AudioReceiver getInstance() {
    return instance;
  }
  
  private AudioReceiver() {}
  
  public void init(Activity clientActivity, int listeningPort) {
    instance.clientActivity = clientActivity;
    instance.listeningPort = listeningPort;
  }

  public void startPlay() {
    if (instance.getState().equals(Thread.State.NEW)) {
      instance.start();
    }
    instance.track = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_OUT_MONO,
        AudioFormat.ENCODING_PCM_16BIT, buf_size, AudioTrack.MODE_STREAM);
    instance.track.play();
    instance.playing = true;
  }

  public void stopPlay() {
    instance.playing = false;
    instance.track.stop();
    instance.track.release();
  }

  public void run(){
    instance.playing = false;

    try {
      instance.socket = new DatagramSocket(listeningPort);
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    while (true) {
      if (instance.playing) {
        byte[] buffer = new byte[256];
        packet = new DatagramPacket(buffer, buffer.length);
          try {
            instance.socket.receive(packet);
          } catch (IOException e) {
            e.printStackTrace();
          }
          instance.track.write(buffer, 0, buffer.length);
      }
    }
  }
}