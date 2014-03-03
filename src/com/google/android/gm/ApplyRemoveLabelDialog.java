package com.google.android.gm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import com.google.android.gm.comm.longshadow.LongShadowUtils;
import com.google.android.gm.provider.Gmail;
import com.google.android.gm.provider.Gmail.LabelMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ApplyRemoveLabelDialog extends AlertDialog
  implements DialogInterface.OnCancelListener, DialogInterface.OnClickListener
{
  private static final String COLOR = "color";
  public static final String EXTRA_ADDED_LABELS = "added-labels";
  public static final String EXTRA_ALL_LABELS = "all-labels";
  public static final String EXTRA_CURRENT_LABELS = "current-labels";
  public static final String EXTRA_REMOVED_LABELS = "removed-labels";
  private static final String LABEL_IS_PRESENT = "label-is-present";
  private static final String NAME = "name";
  private static final String SYSTEM_NAME = "system-name";
  private static final float UNREAD_ROUND_RECT_RADIUS = 10.0F;
  private String mAccount;
  private Activity mActivity;
  private SimpleAdapter mAdapter;
  private HashSet<Long> mAddedLabels = Sets.newHashSet();
  LabelOperations mChangeList;
  private Collection<ConversationInfo> mConversationInfos;
  private ListView mListView;
  private MenuHandler mMenuHandler;
  private HashSet<Long> mRemovedLabels = Sets.newHashSet();

  public ApplyRemoveLabelDialog(Activity paramActivity, MenuHandler paramMenuHandler)
  {
    super(paramActivity);
    this.mActivity = paramActivity;
    this.mMenuHandler = paramMenuHandler;
    setTitle(2131296359);
    setOnCancelListener(this);
    setButton(-1, this.mActivity.getString(17039370), this);
    setButton(-2, this.mActivity.getString(17039360), this);
    setInverseBackgroundForced(true);
    this.mListView = ((ListView)((LayoutInflater)this.mActivity.getSystemService("layout_inflater")).inflate(2130903041, null));
    setView(this.mListView, 0, 0, 0, 0);
  }

  public ListView getListView()
  {
    return this.mListView;
  }

  public void onCancel(DialogInterface paramDialogInterface)
  {
  }

  public void onClick(DialogInterface paramDialogInterface, int paramInt)
  {
    if (paramInt == -1)
      this.mMenuHandler.addOrRemoveLabel(this.mChangeList, this.mConversationInfos, true, null);
  }

  public void onPrepare(String paramString, Set<String> paramSet, Collection<ConversationInfo> paramCollection)
  {
    this.mAccount = paramString;
    this.mConversationInfos = paramCollection;
    ArrayList localArrayList1 = Lists.newArrayList();
    Gmail.LabelMap localLabelMap = LongShadowUtils.getLabelMap(this.mActivity.getContentResolver(), paramString);
    List localList = localLabelMap.getSortedUserLabelIds();
    this.mChangeList = new LabelOperations();
    localArrayList1.add(Long.valueOf(localLabelMap.getLabelIdInbox()));
    localArrayList1.addAll(localList);
    ArrayList localArrayList2 = Lists.newArrayList();
    Iterator localIterator = localArrayList1.iterator();
    if (localIterator.hasNext())
    {
      Long localLong = (Long)localIterator.next();
      HashMap localHashMap = Maps.newHashMap();
      String str = localLabelMap.getCanonicalName(localLong.longValue());
      localHashMap.put("system-name", str);
      localHashMap.put("name", LongShadowUtils.getHumanLabelName(this.mActivity, localLabelMap, localLong.longValue()));
      localHashMap.put("color", localLabelMap.getLabelColor(localLong.longValue()));
      boolean bool;
      if (this.mAddedLabels.contains(localLong))
        bool = true;
      while (true)
      {
        localHashMap.put("label-is-present", Boolean.valueOf(bool));
        localArrayList2.add(localHashMap);
        break;
        if (this.mRemovedLabels.contains(localLong))
          bool = false;
        else
          bool = paramSet.contains(str);
      }
    }
    this.mAdapter = new SimpleAdapter(getContext(), localArrayList2, 2130903063, new String[] { "name", "label-is-present" }, new int[] { 2131361860, 2131361860 })
    {
      Map<Integer, PaintDrawable> mColorBlockCache = Maps.newHashMap();

      public View getView(final int paramAnonymousInt, View paramAnonymousView, ViewGroup paramAnonymousViewGroup)
      {
        View localView1 = super.getView(paramAnonymousInt, paramAnonymousView, paramAnonymousViewGroup);
        ((CheckBox)localView1.findViewById(2131361860)).setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramAnonymous2View)
          {
            Map localMap = (Map)ApplyRemoveLabelDialog.this.mAdapter.getItem(paramAnonymousInt);
            if (!((Boolean)localMap.get("label-is-present")).booleanValue());
            for (boolean bool = true; ; bool = false)
            {
              localMap.put("label-is-present", Boolean.valueOf(bool));
              ApplyRemoveLabelDialog.this.mAdapter.notifyDataSetChanged();
              String str = (String)localMap.get("system-name");
              ApplyRemoveLabelDialog.this.mChangeList.add(str, bool);
              return;
            }
          }
        });
        View localView2 = localView1.findViewById(2131361861);
        Map localMap = (Map)getItem(paramAnonymousInt);
        if (Gmail.isSystemLabel(localMap.get("system-name").toString()));
        for (int i = 16777215; this.mColorBlockCache.containsKey(Integer.valueOf(i)); i = com.google.android.gm.utils.LabelColorUtils.getLabelColorInts(localMap.get("color").toString(), ApplyRemoveLabelDialog.this.mAccount)[0])
        {
          localView2.setBackgroundDrawable((Drawable)this.mColorBlockCache.get(Integer.valueOf(i)));
          return localView1;
        }
        PaintDrawable localPaintDrawable = new PaintDrawable(i);
        localPaintDrawable.setCornerRadius(10.0F);
        this.mColorBlockCache.put(Integer.valueOf(i), localPaintDrawable);
        localView2.setBackgroundDrawable(localPaintDrawable);
        return localView1;
      }
    };
    SimpleAdapter localSimpleAdapter = this.mAdapter;
    SimpleAdapter.ViewBinder local2 = new SimpleAdapter.ViewBinder()
    {
      public boolean setViewValue(View paramAnonymousView, Object paramAnonymousObject, String paramAnonymousString)
      {
        if (!(paramAnonymousView instanceof CheckBox))
          return false;
        CheckBox localCheckBox = (CheckBox)paramAnonymousView;
        if ((paramAnonymousObject instanceof Boolean))
          localCheckBox.setChecked(((Boolean)paramAnonymousObject).booleanValue());
        while (true)
        {
          return true;
          if (!(paramAnonymousObject instanceof String))
            break;
          localCheckBox.setText(paramAnonymousString);
        }
        return false;
      }
    };
    localSimpleAdapter.setViewBinder(local2);
    this.mListView.setAdapter(this.mAdapter);
  }

  public Bundle onSaveInstanceState()
  {
    return new Bundle();
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.ApplyRemoveLabelDialog
 * JD-Core Version:    0.6.2
 */