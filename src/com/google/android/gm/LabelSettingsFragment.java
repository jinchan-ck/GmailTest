package com.google.android.gm;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceFragment;
import android.provider.Settings.System;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.gm.persistence.Persistence;
import com.google.android.gm.preference.PreferenceUtils;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class LabelSettingsFragment extends ListFragment
  implements AdapterView.OnItemClickListener, LabelSettingsObserver
{
  private String mAccount;
  private List<Pair> mAttributeList;
  private boolean mCanVibrate;
  private Context mContext;
  private boolean mDoesAccountNotify;
  private boolean mDoesLabelNotify;
  private String mLabel;
  private boolean mNotifyOnce;
  private Persistence mPersistence;
  private Resources mResources;
  public Ringtone mRingtone;
  public String mRingtoneUri;
  private LabelSettingsObservable mSettingsObservable;
  private boolean mVibrate;

  private String getRingtoneString(Ringtone paramRingtone)
  {
    if (paramRingtone != null)
      return paramRingtone.getTitle(this.mContext);
    return this.mResources.getString(2131427742);
  }

  private void loadInitialSettings()
  {
    Set localSet = this.mPersistence.getNotificationLabelInformation(this.mContext, this.mAccount, this.mLabel);
    this.mDoesLabelNotify = false;
    this.mNotifyOnce = true;
    this.mRingtoneUri = Settings.System.DEFAULT_NOTIFICATION_URI.toString();
    if (this.mCanVibrate)
      this.mVibrate = false;
    if (this.mPersistence.shouldNotifyForLabel(this.mContext, this.mAccount, this.mLabel))
    {
      this.mDoesLabelNotify = true;
      if (this.mCanVibrate)
        this.mVibrate = TextUtils.equals(this.mResources.getString(2131427737), Persistence.extractVibrateSetting(this.mContext, localSet));
      this.mNotifyOnce = Boolean.parseBoolean(Persistence.extract(localSet, Persistence.LABEL_NOTIFICATION_ONCE));
      this.mRingtoneUri = Persistence.extract(localSet, Persistence.LABEL_NOTIFICATION_RINGTONE);
    }
    if ((this.mRingtoneUri != null) && (!this.mRingtoneUri.isEmpty()))
      this.mRingtone = RingtoneManager.getRingtone(this.mContext, Uri.parse(this.mRingtoneUri));
    this.mDoesAccountNotify = this.mPersistence.getEnableNotifications(this.mContext, this.mAccount);
  }

  public static Fragment newInstance(String paramString1, String paramString2)
  {
    LabelSettingsFragment localLabelSettingsFragment = new LabelSettingsFragment();
    Bundle localBundle = new Bundle();
    localBundle.putString("account", paramString1);
    localBundle.putString("label", paramString2);
    localLabelSettingsFragment.setArguments(localBundle);
    return localLabelSettingsFragment;
  }

  private void saveSettings()
  {
    if (this.mDoesLabelNotify)
    {
      HashSet localHashSet = new HashSet();
      Iterator localIterator = this.mAttributeList.iterator();
      while (localIterator.hasNext())
      {
        Pair localPair = (Pair)localIterator.next();
        localHashSet.add(localPair.key + localPair.value);
      }
      this.mPersistence.addNotificationLabel(this.mContext, this.mAccount, this.mLabel, localHashSet);
    }
    while (true)
    {
      this.mSettingsObservable.notifyChanged();
      return;
      this.mPersistence.removeNotificationLabel(this.mContext, this.mAccount, this.mLabel);
      PreferenceUtils.validateNotificationsForAccount(this.mContext, this.mAccount);
    }
  }

  private void setRingtone(Uri paramUri)
  {
    if (paramUri != null)
      this.mRingtoneUri = paramUri.toString();
    for (this.mRingtone = RingtoneManager.getRingtone(this.mContext, paramUri); ; this.mRingtone = null)
    {
      Iterator localIterator = this.mAttributeList.iterator();
      while (localIterator.hasNext())
      {
        Pair localPair = (Pair)localIterator.next();
        if (localPair.key == Persistence.LABEL_NOTIFICATION_RINGTONE)
          localPair.value = this.mRingtoneUri;
      }
      return;
      this.mRingtoneUri = "";
    }
  }

  private void showRingtonePicker()
  {
    Intent localIntent = new Intent("android.intent.action.RINGTONE_PICKER");
    if ((this.mRingtoneUri != null) && (!this.mRingtoneUri.isEmpty()))
      localIntent.putExtra("android.intent.extra.ringtone.EXISTING_URI", Uri.parse(this.mRingtoneUri));
    localIntent.putExtra("android.intent.extra.ringtone.SHOW_DEFAULT", true);
    localIntent.putExtra("android.intent.extra.ringtone.DEFAULT_URI", RingtoneManager.getActualDefaultRingtoneUri(this.mContext, 2));
    localIntent.putExtra("android.intent.extra.ringtone.SHOW_SILENT", true);
    localIntent.putExtra("android.intent.extra.ringtone.TYPE", 2);
    startActivityForResult(localIntent, 0);
  }

  private void showSynchronizationSettings()
  {
    Intent localIntent = new Intent(this.mContext, LabelSynchronizationActivity.class);
    localIntent.putExtra("account", this.mAccount);
    localIntent.putExtra("folder", this.mLabel);
    localIntent.putExtra("included-labels", this.mSettingsObservable.getIncludedLabels());
    localIntent.putExtra("partial-labels", this.mSettingsObservable.getPartialLabels());
    localIntent.putExtra("num-of-sync-days", this.mSettingsObservable.getNumberOfSyncDays());
    startActivityForResult(localIntent, 1);
  }

  private String vibrateAttributeValue(boolean paramBoolean)
  {
    if (paramBoolean)
      return this.mResources.getString(2131427737);
    return this.mResources.getString(2131427738);
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    this.mContext = getActivity();
    this.mResources = this.mContext.getResources();
    this.mPersistence = Persistence.getInstance();
    Vibrator localVibrator = (Vibrator)this.mContext.getSystemService("vibrator");
    if ((localVibrator != null) && (localVibrator.hasVibrator()));
    for (boolean bool = true; ; bool = false)
    {
      this.mCanVibrate = bool;
      this.mSettingsObservable = ((LabelSettingsObservable)getActivity());
      this.mSettingsObservable.registerObserver(this);
      loadInitialSettings();
      this.mAttributeList = Lists.newArrayList();
      this.mAttributeList.add(new Pair(Persistence.LABEL_NOTIFICATION_ON, Boolean.toString(this.mDoesLabelNotify)));
      this.mAttributeList.add(new Pair(Persistence.LABEL_NOTIFICATION_RINGTONE, this.mRingtoneUri));
      if (this.mCanVibrate)
        this.mAttributeList.add(new Pair(Persistence.LABEL_NOTIFICATION_VIBRATE, vibrateAttributeValue(this.mVibrate)));
      this.mAttributeList.add(new Pair(Persistence.LABEL_NOTIFICATION_ONCE, Boolean.toString(this.mNotifyOnce)));
      setListAdapter(new LabelSettingsAdapter(null));
      getListView().setOnItemClickListener(this);
      return;
    }
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if ((paramInt1 == 0) && (paramInt2 == -1) && (paramIntent != null))
      setRingtone((Uri)paramIntent.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI"));
    if ((paramInt1 == 1) && (paramInt2 == -1) && (paramIntent != null))
    {
      this.mSettingsObservable.setIncludedLabels(paramIntent.getStringArrayListExtra("included-labels"));
      this.mSettingsObservable.setPartialLabels(paramIntent.getStringArrayListExtra("partial-labels"));
    }
    saveSettings();
  }

  public void onChanged()
  {
    ((LabelSettingsAdapter)getListAdapter()).notifyDataSetChanged();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Bundle localBundle = getArguments();
    this.mAccount = localBundle.getString("account");
    this.mLabel = localBundle.getString("label");
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    return new PreferenceFragment()
    {
    }
    .onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
  }

  public void onDestroyView()
  {
    this.mSettingsObservable.unregisterObserver(this);
    super.onDestroyView();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    boolean bool = true;
    Pair localPair = (Pair)getListAdapter().getItem(paramInt);
    if (!paramView.isEnabled())
    {
      if (Persistence.isNotification(localPair.key))
        if ((!this.mSettingsObservable.getIncludedLabels().contains(this.mLabel)) && (!this.mSettingsObservable.getPartialLabels().contains(this.mLabel)))
          break label150;
      label150: for (int i = bool; ; i = 0)
      {
        if ((i != 0) && (!this.mDoesAccountNotify))
        {
          DialogInterface.OnClickListener local2 = new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
            {
              paramAnonymousDialogInterface.cancel();
            }
          };
          DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
            {
              LabelSettingsFragment.this.mPersistence.setEnableNotifications(LabelSettingsFragment.this.mContext, LabelSettingsFragment.this.mAccount, true);
              LabelSettingsFragment.access$402(LabelSettingsFragment.this, true);
              LabelSettingsFragment.access$502(LabelSettingsFragment.this, true);
              LabelSettingsFragment.this.saveSettings();
            }
          };
          new AlertDialog.Builder(this.mContext).setMessage(2131427698).setPositiveButton(17039379, local3).setNegativeButton(17039369, local2).create().show();
        }
        return;
      }
    }
    if (localPair.key == Persistence.LABEL_SYNCHRONIZATION)
      showSynchronizationSettings();
    do
    {
      while (true)
      {
        saveSettings();
        return;
        if (localPair.key == Persistence.LABEL_NOTIFICATION_ON)
        {
          if (!this.mDoesLabelNotify);
          while (true)
          {
            this.mDoesLabelNotify = bool;
            localPair.value = Boolean.toString(this.mDoesLabelNotify);
            break;
            bool = false;
          }
        }
        if ((!this.mDoesLabelNotify) || (localPair.key != Persistence.LABEL_NOTIFICATION_RINGTONE))
          break;
        showRingtonePicker();
      }
      if ((this.mDoesLabelNotify) && (this.mCanVibrate) && (localPair.key == Persistence.LABEL_NOTIFICATION_VIBRATE))
      {
        if (!this.mVibrate);
        while (true)
        {
          this.mVibrate = bool;
          localPair.value = vibrateAttributeValue(this.mVibrate);
          break;
          bool = false;
        }
      }
    }
    while ((!this.mDoesLabelNotify) || (localPair.key != Persistence.LABEL_NOTIFICATION_ONCE));
    if (!this.mNotifyOnce);
    while (true)
    {
      this.mNotifyOnce = bool;
      localPair.value = Boolean.toString(this.mNotifyOnce);
      break;
      bool = false;
    }
  }

  private final class LabelSettingsAdapter extends BaseAdapter
  {
    private LabelSettingsAdapter()
    {
    }

    public int getCount()
    {
      return 1 + LabelSettingsFragment.this.mAttributeList.size();
    }

    public LabelSettingsFragment.Pair getItem(int paramInt)
    {
      if (paramInt == 0)
        return new LabelSettingsFragment.Pair(Persistence.LABEL_SYNCHRONIZATION, null);
      return (LabelSettingsFragment.Pair)LabelSettingsFragment.this.mAttributeList.get(paramInt - 1);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      LabelSettingsFragment.Pair localPair = getItem(paramInt);
      int i;
      View localView;
      TextView localTextView1;
      TextView localTextView2;
      CheckBox localCheckBox;
      boolean bool2;
      int j;
      label183: String str;
      if (Persistence.isNotificationSubChoice(localPair.key))
      {
        i = 2130968641;
        localView = LayoutInflater.from(LabelSettingsFragment.this.mContext).inflate(i, null);
        localTextView1 = (TextView)localView.findViewById(2131689654);
        localTextView2 = (TextView)localView.findViewById(2131689524);
        localCheckBox = (CheckBox)localView.findViewById(2131689655);
        if ((localPair.key != Persistence.LABEL_NOTIFICATION_ONCE) && (localPair.key != Persistence.LABEL_NOTIFICATION_ON) && (localPair.key != Persistence.LABEL_NOTIFICATION_VIBRATE))
          localCheckBox.setVisibility(8);
        localTextView2.setVisibility(0);
        boolean bool1 = LabelSettingsFragment.this.mSettingsObservable.getIncludedLabels().contains(LabelSettingsFragment.this.mLabel);
        bool2 = LabelSettingsFragment.this.mSettingsObservable.getPartialLabels().contains(LabelSettingsFragment.this.mLabel);
        if ((bool1) || (bool2))
          break label308;
        j = 1;
        if (localPair.key != Persistence.LABEL_SYNCHRONIZATION)
          break label365;
        localTextView1.setText(2131427816);
        if (!bool1)
          break label314;
        str = LabelSettingsFragment.this.mResources.getString(2131427817);
        label220: localTextView2.setText(str);
      }
      while (true)
      {
        if (((Persistence.isNotification(localPair.key)) && ((j != 0) || (!LabelSettingsFragment.this.mDoesAccountNotify))) || ((Persistence.isNotificationSubChoice(localPair.key)) && (!LabelSettingsFragment.this.mDoesLabelNotify)))
        {
          localView.setEnabled(false);
          localTextView1.setEnabled(false);
          localTextView2.setEnabled(false);
          localCheckBox.setEnabled(false);
        }
        return localView;
        i = 2130968645;
        break;
        label308: j = 0;
        break label183;
        label314: if (bool2)
        {
          str = Utils.formatPlural(LabelSettingsFragment.this.mContext, 2131755043, LabelSettingsFragment.this.mSettingsObservable.getNumberOfSyncDays());
          break label220;
        }
        str = LabelSettingsFragment.this.mResources.getString(2131427818);
        break label220;
        label365: if (localPair.key == Persistence.LABEL_NOTIFICATION_ON)
        {
          localTextView1.setText(2131427697);
          localTextView2.setText(2131427700);
          localCheckBox.setChecked(LabelSettingsFragment.this.mDoesLabelNotify);
        }
        else if (localPair.key == Persistence.LABEL_NOTIFICATION_RINGTONE)
        {
          localTextView1.setText(2131427741);
          localTextView2.setText(LabelSettingsFragment.this.getRingtoneString(LabelSettingsFragment.this.mRingtone));
        }
        else if ((LabelSettingsFragment.this.mCanVibrate) && (localPair.key == Persistence.LABEL_NOTIFICATION_VIBRATE))
        {
          localTextView1.setText(2131427740);
          localTextView2.setVisibility(8);
          localCheckBox.setChecked(LabelSettingsFragment.this.mVibrate);
        }
        else if (localPair.key == Persistence.LABEL_NOTIFICATION_ONCE)
        {
          localTextView1.setText(2131427701);
          localTextView2.setText(2131427702);
          localCheckBox.setChecked(LabelSettingsFragment.this.mNotifyOnce);
        }
      }
    }
  }

  private static class Pair
  {
    public int key;
    public String value;

    public Pair(int paramInt, String paramString)
    {
      this.key = paramInt;
      this.value = paramString;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.LabelSettingsFragment
 * JD-Core Version:    0.6.2
 */