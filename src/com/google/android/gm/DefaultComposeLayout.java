package com.google.android.gm;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;

public class DefaultComposeLayout extends ComposeLayout
  implements View.OnClickListener, AdapterView.OnItemSelectedListener
{
  private MenuItem mAddCc;
  private ComposeArea mComposeArea;
  private int mComposeMode = -1;
  private Spinner mComposeModeButton;
  protected final ComposeController mController;
  private final Activity mParent;
  private MenuItem mRemoveCc;
  private ImageView mSaveButton;
  private ImageView mSendButton;

  public DefaultComposeLayout(Activity paramActivity, ComposeController paramComposeController)
  {
    this.mParent = paramActivity;
    this.mController = paramComposeController;
  }

  public void enableSave(boolean paramBoolean)
  {
    this.mSaveButton.setEnabled(paramBoolean);
  }

  public void enableSend(boolean paramBoolean)
  {
    this.mSendButton.setEnabled(paramBoolean);
  }

  protected View findViewById(int paramInt)
  {
    return this.mParent.findViewById(paramInt);
  }

  public ComposeArea getComposeArea()
  {
    return this.mComposeArea;
  }

  public void hideOrShowCcBcc(boolean paramBoolean1, boolean paramBoolean2)
  {
    if (this.mRemoveCc == null);
    do
    {
      do
      {
        return;
        if (!paramBoolean1)
          break;
        this.mRemoveCc.setVisible(true);
        this.mAddCc.setVisible(false);
      }
      while (!paramBoolean2);
      this.mComposeArea.hideOrShowCcBcc(true);
      return;
      this.mRemoveCc.setVisible(false);
      this.mAddCc.setVisible(true);
    }
    while (!paramBoolean2);
    this.mComposeArea.hideOrShowCcBcc(false);
  }

  public void onClick(View paramView)
  {
    switch (paramView.getId())
    {
    default:
      return;
    case 2131361824:
      this.mController.doSend(true);
      return;
    case 2131361825:
    }
    this.mController.doSave(true);
  }

  public boolean onCreateOptionsMenu(Menu paramMenu, boolean paramBoolean)
  {
    this.mParent.getMenuInflater().inflate(2131623936, paramMenu);
    return true;
  }

  public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    if (paramInt == 0)
      this.mComposeMode = 0;
    while (true)
    {
      this.mController.setComposeMode(this.mComposeMode);
      return;
      if (paramInt == 1)
        this.mComposeMode = 1;
      else if (paramInt == 2)
        this.mComposeMode = 2;
    }
  }

  public void onNothingSelected(AdapterView<?> paramAdapterView)
  {
  }

  public void onOrientationChanged(Configuration paramConfiguration)
  {
  }

  public boolean onPrepareOptionsMenu(Menu paramMenu)
  {
    boolean bool1 = this.mComposeArea.currentlyShowingCcBcc();
    this.mRemoveCc = paramMenu.findItem(2131361920);
    this.mRemoveCc.setVisible(bool1);
    this.mAddCc = paramMenu.findItem(2131361921);
    MenuItem localMenuItem = this.mAddCc;
    if (!bool1);
    for (boolean bool2 = true; ; bool2 = false)
    {
      localMenuItem.setVisible(bool2);
      return true;
    }
  }

  public void setComposeArea(ComposeArea paramComposeArea)
  {
    this.mComposeArea = paramComposeArea;
  }

  public void setupButtons()
  {
    this.mSendButton = ((ImageView)findViewById(2131361824));
    this.mSendButton.setOnClickListener(this);
    this.mSaveButton = ((ImageView)findViewById(2131361825));
    this.mSaveButton.setOnClickListener(this);
  }

  public void setupLayout()
  {
    this.mParent.setContentView(2130903045);
    ((ScrollView)findViewById(2131361805)).addView(getComposeArea().getView());
    setupButtons();
    this.mParent.getWindow().setBackgroundDrawable(null);
  }

  public void updateComposeMode(int paramInt)
  {
    this.mComposeMode = paramInt;
    if (this.mComposeModeButton == null)
    {
      this.mComposeModeButton = ((Spinner)findViewById(2131361823));
      String[] arrayOfString = this.mParent.getResources().getStringArray(2131492865);
      ArrayAdapter localArrayAdapter = new ArrayAdapter(this.mParent, 2130903048, 2131361826, arrayOfString);
      this.mComposeModeButton.setAdapter(localArrayAdapter);
      this.mComposeModeButton.setOnItemSelectedListener(this);
    }
    switch (paramInt)
    {
    default:
      return;
    case -1:
      this.mComposeModeButton.setVisibility(8);
      findViewById(2131361822).setVisibility(0);
      return;
    case 0:
      this.mComposeModeButton.setSelection(0);
      return;
    case 1:
      this.mComposeModeButton.setSelection(1);
      return;
    case 2:
    }
    this.mComposeModeButton.setSelection(2);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.DefaultComposeLayout
 * JD-Core Version:    0.6.2
 */