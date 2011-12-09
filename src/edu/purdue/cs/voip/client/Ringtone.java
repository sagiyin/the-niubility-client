package edu.purdue.cs.voip.client;

import android.content.Context;
import android.media.MediaPlayer;

public class Ringtone extends Thread {
  private static final Ringtone instance = new Ringtone();
  private Context context;

  private boolean playing;

  public static Ringtone getInstance() {
    return instance;
  }

  private Ringtone() {
  }

  public void init(Context context) {
    this.context = context;
    this.playing = false;
  }

  public void startPlaying() {
    this.playing = true;
  }

  public void stopPlaying() {
    this.playing = false;
  }

  public void run() {
    MediaPlayer player = MediaPlayer.create(context, R.raw.ringtone);
    while (true) {
      if (playing) {
        if (!player.isPlaying()) {
          player.start();
          player.setLooping(true);
        }
      } else {
        if (player.isPlaying()) {
          player.stop();
        }
      }
    }
  }
}
