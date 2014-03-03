package com.google.android.gm;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.SeekBar;

class MediaController
{
  private Context mContext;
  private MediaPlayer mMediaPlayer;
  private int mPauseImageResource;
  private int mPlayImageResource;
  private ImageView mPlayPauseButton;

  public static MediaController newMediaController(Context paramContext, ImageView paramImageView, int paramInt1, int paramInt2, SeekBar paramSeekBar, Uri paramUri)
  {
    MediaPlayer localMediaPlayer = MediaPlayer.create(paramContext, paramUri);
    if (localMediaPlayer == null)
      return null;
    MediaController localMediaController = new MediaController();
    localMediaController.mContext = paramContext;
    localMediaController.mMediaPlayer = localMediaPlayer;
    localMediaController.mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
    {
      public void onCompletion(MediaPlayer paramAnonymousMediaPlayer)
      {
        this.val$controller.updatePlayPauseButton();
      }
    });
    localMediaController.mPlayPauseButton = paramImageView;
    localMediaController.mPlayImageResource = paramInt1;
    localMediaController.mPauseImageResource = paramInt2;
    return localMediaController;
  }

  private void updatePlayPauseButton()
  {
    boolean bool = this.mMediaPlayer.isPlaying();
    ImageView localImageView = this.mPlayPauseButton;
    if (bool);
    for (int i = this.mPauseImageResource; ; i = this.mPlayImageResource)
    {
      localImageView.setImageResource(i);
      return;
    }
  }

  public void playOrPause()
  {
    if (this.mMediaPlayer.isPlaying())
      this.mMediaPlayer.pause();
    while (true)
    {
      updatePlayPauseButton();
      return;
      this.mMediaPlayer.start();
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.MediaController
 * JD-Core Version:    0.6.2
 */