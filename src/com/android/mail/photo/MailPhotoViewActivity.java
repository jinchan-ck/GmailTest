package com.android.mail.photo;

import android.app.ActionBar;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.ex.photo.PhotoViewActivity;
import com.android.ex.photo.fragments.PhotoViewFragment;
import com.android.ex.photo.views.ProgressBarWrapper;
import com.android.mail.browse.AttachmentActionHandler;
import com.android.mail.providers.Attachment;
import com.android.mail.utils.AttachmentUtils;
import com.android.mail.utils.Utils;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MailPhotoViewActivity extends PhotoViewActivity
{
  private AttachmentActionHandler mActionHandler;
  private Menu mMenu;
  private MenuItem mSaveAllItem;
  private MenuItem mSaveItem;
  private MenuItem mShareAllItem;
  private MenuItem mShareItem;

  private void downloadAttachment()
  {
    Attachment localAttachment = getCurrentAttachment();
    if ((localAttachment != null) && (localAttachment.canSave()))
    {
      this.mActionHandler.setAttachment(localAttachment);
      this.mActionHandler.startDownloadingAttachment(0);
    }
  }

  private List<Attachment> getAllAttachments()
  {
    Cursor localCursor = getCursor();
    if ((localCursor == null) || (localCursor.isClosed()) || (!localCursor.moveToFirst()))
      return null;
    ArrayList localArrayList = Lists.newArrayList();
    do
      localArrayList.add(new Attachment(localCursor));
    while (localCursor.moveToNext());
    return localArrayList;
  }

  private void saveAllAttachments()
  {
    Cursor localCursor = getCursorAtProperPosition();
    if (localCursor == null);
    while (true)
    {
      return;
      int i = -1;
      while (true)
      {
        i++;
        if (!localCursor.moveToPosition(i))
          break;
        saveAttachment(new Attachment(localCursor));
      }
    }
  }

  private void saveAttachment()
  {
    saveAttachment(getCurrentAttachment());
  }

  private void saveAttachment(Attachment paramAttachment)
  {
    if ((paramAttachment != null) && (paramAttachment.canSave()))
    {
      this.mActionHandler.setAttachment(paramAttachment);
      this.mActionHandler.startDownloadingAttachment(1);
    }
  }

  private void shareAllAttachments()
  {
    Cursor localCursor = getCursorAtProperPosition();
    if (localCursor == null)
      return;
    ArrayList localArrayList = new ArrayList();
    int i = -1;
    while (true)
    {
      i++;
      if (!localCursor.moveToPosition(i))
        break;
      localArrayList.add(Utils.normalizeUri(new Attachment(localCursor).contentUri));
    }
    this.mActionHandler.shareAttachments(localArrayList);
  }

  private void shareAttachment()
  {
    shareAttachment(getCurrentAttachment());
  }

  private void shareAttachment(Attachment paramAttachment)
  {
    if (paramAttachment != null)
    {
      this.mActionHandler.setAttachment(paramAttachment);
      this.mActionHandler.shareAttachment();
    }
  }

  private void updateProgressAndEmptyViews(PhotoViewFragment paramPhotoViewFragment, Attachment paramAttachment)
  {
    final ProgressBarWrapper localProgressBarWrapper = paramPhotoViewFragment.getPhotoProgressBar();
    TextView localTextView = paramPhotoViewFragment.getEmptyText();
    ImageView localImageView = paramPhotoViewFragment.getRetryButton();
    if (paramAttachment.shouldShowProgress())
    {
      localProgressBarWrapper.setMax(paramAttachment.size);
      localProgressBarWrapper.setProgress(paramAttachment.downloadedSize);
      localProgressBarWrapper.setIndeterminate(false);
    }
    while (paramAttachment.downloadFailed())
    {
      localTextView.setText(2131427570);
      localTextView.setVisibility(0);
      localImageView.setVisibility(0);
      localImageView.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          MailPhotoViewActivity.this.downloadAttachment();
          localProgressBarWrapper.setVisibility(0);
        }
      });
      localProgressBarWrapper.setVisibility(8);
      return;
      if (paramPhotoViewFragment.isProgressBarNeeded())
        localProgressBarWrapper.setIndeterminate(true);
    }
    localTextView.setVisibility(8);
    localImageView.setVisibility(8);
  }

  protected Attachment getCurrentAttachment()
  {
    Cursor localCursor = getCursorAtProperPosition();
    if (localCursor == null)
      return null;
    return new Attachment(localCursor);
  }

  protected void onCreate(Bundle paramBundle)
  {
    requestWindowFeature(2);
    super.onCreate(paramBundle);
    this.mActionHandler = new AttachmentActionHandler(this, null);
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getMenuInflater().inflate(2131820554, paramMenu);
    this.mMenu = paramMenu;
    this.mSaveItem = this.mMenu.findItem(2131689774);
    this.mSaveAllItem = this.mMenu.findItem(2131689775);
    this.mShareItem = this.mMenu.findItem(2131689776);
    this.mShareAllItem = this.mMenu.findItem(2131689777);
    return true;
  }

  public void onFragmentVisible(PhotoViewFragment paramPhotoViewFragment)
  {
    super.onFragmentVisible(paramPhotoViewFragment);
    updateProgressAndEmptyViews(paramPhotoViewFragment, getCurrentAttachment());
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    switch (paramMenuItem.getItemId())
    {
    default:
      return super.onOptionsItemSelected(paramMenuItem);
    case 16908332:
      finish();
      return true;
    case 2131689774:
      saveAttachment();
      return true;
    case 2131689775:
      saveAllAttachments();
      return true;
    case 2131689776:
      shareAttachment();
      return true;
    case 2131689777:
    }
    shareAllAttachments();
    return true;
  }

  public boolean onPrepareOptionsMenu(Menu paramMenu)
  {
    updateActionItems();
    return true;
  }

  protected void updateActionBar(PhotoViewFragment paramPhotoViewFragment)
  {
    super.updateActionBar(paramPhotoViewFragment);
    Attachment localAttachment = getCurrentAttachment();
    ActionBar localActionBar = getActionBar();
    String str = AttachmentUtils.convertToHumanReadableSize(this, localAttachment.size);
    if (localAttachment.isSavedToExternal())
      localActionBar.setSubtitle(getResources().getString(2131427455) + " " + str);
    while (true)
    {
      updateActionItems();
      return;
      if ((localAttachment.isDownloading()) && (localAttachment.destination == 1))
        localActionBar.setSubtitle(2131427461);
      else
        localActionBar.setSubtitle(str);
    }
  }

  protected void updateActionItems()
  {
    boolean bool1 = Utils.isRunningJellybeanOrLater();
    Attachment localAttachment1 = getCurrentAttachment();
    if ((localAttachment1 != null) && (this.mSaveItem != null) && (this.mShareItem != null))
    {
      localMenuItem = this.mSaveItem;
      if ((!localAttachment1.isDownloading()) && (localAttachment1.canSave()) && (!localAttachment1.isSavedToExternal()))
      {
        bool2 = true;
        localMenuItem.setEnabled(bool2);
        this.mShareItem.setEnabled(localAttachment1.canShare());
        localList = getAllAttachments();
        if (localList != null)
        {
          localIterator1 = localList.iterator();
          do
          {
            bool3 = localIterator1.hasNext();
            bool4 = false;
            if (!bool3)
              break;
            localAttachment2 = (Attachment)localIterator1.next();
          }
          while ((localAttachment2.isDownloading()) || (!localAttachment2.canSave()) || (localAttachment2.isSavedToExternal()));
          bool4 = true;
          this.mSaveAllItem.setEnabled(bool4);
          bool5 = true;
          localIterator2 = localList.iterator();
          while (localIterator2.hasNext())
            if (!((Attachment)localIterator2.next()).canShare())
              bool5 = false;
          this.mShareAllItem.setEnabled(bool5);
        }
        if (!bool1)
        {
          this.mShareItem.setVisible(false);
          this.mShareAllItem.setVisible(false);
        }
      }
    }
    while (this.mMenu == null)
      while (true)
      {
        MenuItem localMenuItem;
        List localList;
        Iterator localIterator1;
        boolean bool3;
        boolean bool4;
        Attachment localAttachment2;
        boolean bool5;
        Iterator localIterator2;
        return;
        boolean bool2 = false;
      }
    this.mMenu.setGroupEnabled(2131689773, false);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.photo.MailPhotoViewActivity
 * JD-Core Version:    0.6.2
 */