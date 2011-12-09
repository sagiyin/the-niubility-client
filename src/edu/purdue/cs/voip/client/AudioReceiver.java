package edu.purdue.cs.voip.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class AudioReceiver extends Thread {
  VOIP_ClientActivity clientActivity;
  DatagramSocket socket;
  DatagramPacket packet;
  int buf_size = AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_OUT_MONO,
      AudioFormat.ENCODING_PCM_16BIT);
  AudioTrack track;

  boolean playing;
  InetAddress targetIP;
  int listeningPort;

  public AudioReceiver(VOIP_ClientActivity clientActivity, int listeningPort) {
    this.clientActivity = clientActivity;
    this.listeningPort = listeningPort;
  }

  public void startPlay() {
    track = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_OUT_MONO,
        AudioFormat.ENCODING_PCM_16BIT, buf_size, AudioTrack.MODE_STREAM);
    track.play();
    this.playing = true;
  }

  public void stopPlay() {
    this.playing = false;
    track.stop();
    track.release();
  }

  public void run(){
    playing = false;

    try {
      socket = new DatagramSocket(listeningPort);
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    while (true) {
      if (playing) {
        byte[] buffer = new byte[256];
        packet = new DatagramPacket(buffer, buffer.length);
          try {
            socket.receive(packet);
          } catch (IOException e) {
            e.printStackTrace();
          }
        track.write(buffer, 0, buffer.length);
      }
    }
  }
}