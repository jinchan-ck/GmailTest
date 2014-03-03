package com.google.android.gm.photo;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.android.mail.photo.MailPhotoViewActivity;
import com.android.mail.providers.Attachment;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import com.android.mail.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.panorama.PanoramaClient;
import com.google.android.gms.panorama.PanoramaClient.OnPanoramaInfoLoadedListener;

public class GmailPhotoViewActivity extends MailPhotoViewActivity
  implements View.OnClickListener, GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, PanoramaClient.OnPanoramaInfoLoadedListener
{
  private static final String LOG_TAG = LogTag.getLogTag();
  private PanoramaClient mClient;
  private Attachment mCurrentAttachment;
  private Intent mCurrentViewerIntent;
  private ImageView mPanoramaButton;
  private Attachment mSavedAttachment;

  private Uri getUri(Attachment paramAttachment)
  {
    if (paramAttachment != null)
    {
      Uri localUri = paramAttachment.contentUri;
      if (localUri != null)
        return Utils.normalizeUri(localUri);
    }
    return Uri.EMPTY;
  }

  private void loadPanoramaInfo(Attachment paramAttachment)
  {
    if (this.mClient != null)
    {
      this.mSavedAttachment = null;
      Uri localUri = getUri(paramAttachment);
      if ((!Utils.isEmpty(localUri)) && (paramAttachment.isPresentLocally()))
      {
        LogUtils.d(LOG_TAG, new Throwable(), "Panorama loading info for %s", new Object[] { paramAttachment });
        this.mCurrentAttachment = paramAttachment;
        this.mCurrentViewerIntent = null;
        Bundle localBundle = new Bundle();
        localBundle.putParcelable("attachmentUri", localUri);
        this.mClient.loadPanoramaInfoAndGrantAccess(this, localUri, localBundle);
        return;
      }
    }
    setAnimatedVisibility(this.mPanoramaButton, false);
  }

  private void setAnimatedVisibility(View paramView, boolean paramBoolean)
  {
    if (paramBoolean);
    for (int i = 0; paramView.getVisibility() == i; i = 8)
      return;
    paramView.setVisibility(i);
    if (paramBoolean);
    for (int j = 2131034114; ; j = 2131034115)
    {
      Animator localAnimator = AnimatorInflater.loadAnimator(this, j);
      localAnimator.setTarget(paramView);
      localAnimator.start();
      return;
    }
  }

  public void onClick(View paramView)
  {
    switch (paramView.getId())
    {
    default:
      return;
    case 2131689670:
    }
    if (this.mCurrentViewerIntent != null)
    {
      Intent localIntent = this.mCurrentViewerIntent;
      try
      {
        startActivity(localIntent);
        return;
      }
      catch (ActivityNotFoundException localActivityNotFoundException)
      {
        String str2 = LOG_TAG;
        Object[] arrayOfObject2 = new Object[1];
        arrayOfObject2[0] = localIntent.getData();
        LogUtils.e(str2, localActivityNotFoundException, "Cannot view attachment: %s", arrayOfObject2);
        return;
      }
    }
    String str1 = LOG_TAG;
    Object[] arrayOfObject1 = new Object[1];
    arrayOfObject1[0] = this.mCurrentAttachment;
    LogUtils.e(str1, "Viewer intent is null for attachment: %s", arrayOfObject1);
  }

  public void onConnected()
  {
    String str = LOG_TAG;
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = this.mSavedAttachment;
    LogUtils.d(str, "Panorama onConnected loading info for %s", arrayOfObject);
    loadPanoramaInfo(this.mSavedAttachment);
  }

  public void onConnectionFailed(ConnectionResult paramConnectionResult)
  {
    LogUtils.e(LOG_TAG, "Panorama connection failed: %s", new Object[] { paramConnectionResult });
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == 0)
      this.mClient = new PanoramaClient(this, this, this);
    this.mPanoramaButton = ((ImageView)findViewById(2131689670));
    this.mPanoramaButton.setOnClickListener(this);
  }

  public void onDisconnected()
  {
    LogUtils.d(LOG_TAG, "Panorama disconnected", new Object[0]);
  }

  public void onNewPhotoLoaded()
  {
    super.onNewPhotoLoaded();
    Attachment localAttachment;
    if (this.mClient != null)
    {
      localAttachment = getCurrentAttachment();
      if (this.mClient.isConnected())
        loadPanoramaInfo(localAttachment);
    }
    else
    {
      return;
    }
    LogUtils.d(LOG_TAG, "Panorama saving attachment %s", new Object[] { localAttachment });
    this.mSavedAttachment = localAttachment;
  }

  public void onPanoramaInfoLoaded(ConnectionResult paramConnectionResult, int paramInt, Intent paramIntent)
  {
    String str = LOG_TAG;
    Object[] arrayOfObject = new Object[3];
    arrayOfObject[0] = Integer.valueOf(paramInt);
    arrayOfObject[1] = paramIntent;
    arrayOfObject[2] = paramConnectionResult;
    LogUtils.d(str, "Panorama found type: %d, viewerIntent: %s, result: %s", arrayOfObject);
    if (paramIntent != null)
    {
      Uri localUri = (Uri)paramIntent.getExtras().getParcelable("attachmentUri");
      if ((localUri != null) && (localUri.equals(getUri(this.mCurrentAttachment))))
        if (paramConnectionResult.getErrorCode() == 0)
        {
          if ((paramInt != 0) && (paramIntent != null))
          {
            setAnimatedVisibility(this.mPanoramaButton, true);
            this.mCurrentViewerIntent = paramIntent;
          }
        }
        else
          LogUtils.e(LOG_TAG, "Panorama error: %s", new Object[] { paramConnectionResult });
    }
    setAnimatedVisibility(this.mPanoramaButton, false);
  }

  protected void onResume()
  {
    super.onResume();
    if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) != 0)
    {
      if (this.mClient != null)
      {
        this.mClient.disconnect();
        this.mClient = null;
      }
      setAnimatedVisibility(this.mPanoramaButton, false);
    }
  }

  protected void onStart()
  {
    super.onStart();
    if (this.mClient != null)
      this.mClient.connect();
  }

  protected void onStop()
  {
    super.onStop();
    if (this.mClient != null)
      this.mClient.disconnect();
  }

  public void setViewActivated()
  {
    super.setViewActivated();
    onNewPhotoLoaded();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.photo.GmailPhotoViewActivity
 * JD-Core Version:    0.6.2
 */