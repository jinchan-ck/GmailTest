package com.android.mail.browse;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.webkit.WebView;
import android.webkit.WebView.HitTestResult;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class WebViewContextMenu
  implements MenuItem.OnMenuItemClickListener, View.OnCreateContextMenuListener
{
  private Activity mActivity;

  public WebViewContextMenu(Activity paramActivity)
  {
    this.mActivity = paramActivity;
  }

  private void copy(CharSequence paramCharSequence)
  {
    ((ClipboardManager)this.mActivity.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText(null, paramCharSequence));
  }

  private void shareLink(String paramString)
  {
    Intent localIntent = new Intent("android.intent.action.SEND");
    localIntent.setType("text/plain");
    localIntent.putExtra("android.intent.extra.TEXT", paramString);
    try
    {
      this.mActivity.startActivity(Intent.createChooser(localIntent, this.mActivity.getText(getChooserTitleStringResIdForMenuType(MenuType.SHARE_LINK_MENU))));
      return;
    }
    catch (ActivityNotFoundException localActivityNotFoundException)
    {
    }
  }

  private boolean showShareLinkMenuItem()
  {
    PackageManager localPackageManager = this.mActivity.getPackageManager();
    Intent localIntent = new Intent("android.intent.action.SEND");
    localIntent.setType("text/plain");
    return localPackageManager.resolveActivity(localIntent, 65536) != null;
  }

  protected int getChooserTitleStringResIdForMenuType(MenuType paramMenuType)
  {
    switch (1.$SwitchMap$com$android$mail$browse$WebViewContextMenu$MenuType[paramMenuType.ordinal()])
    {
    default:
      throw new IllegalStateException("Unexpected MenuType");
    case 3:
    }
    return 2131427464;
  }

  protected int getMenuGroupResId(MenuGroupType paramMenuGroupType)
  {
    switch (1.$SwitchMap$com$android$mail$browse$WebViewContextMenu$MenuGroupType[paramMenuGroupType.ordinal()])
    {
    default:
      throw new IllegalStateException("Unexpected MenuGroupType");
    case 1:
      return 2131689779;
    case 2:
      return 2131689784;
    case 3:
      return 2131689787;
    case 4:
    }
    return 2131689790;
  }

  protected int getMenuResIdForMenuType(MenuType paramMenuType)
  {
    switch (1.$SwitchMap$com$android$mail$browse$WebViewContextMenu$MenuType[paramMenuType.ordinal()])
    {
    default:
      throw new IllegalStateException("Unexpected MenuType");
    case 1:
      return 2131689791;
    case 2:
      return 2131689792;
    case 3:
      return 2131689793;
    case 4:
      return 2131689780;
    case 5:
      return 2131689781;
    case 6:
      return 2131689782;
    case 7:
      return 2131689783;
    case 8:
      return 2131689785;
    case 9:
      return 2131689786;
    case 10:
      return 2131689788;
    case 11:
    }
    return 2131689789;
  }

  protected int getMenuResourceId()
  {
    return 2131820558;
  }

  public void onCreateContextMenu(ContextMenu paramContextMenu, View paramView, ContextMenu.ContextMenuInfo paramContextMenuInfo)
  {
    WebView.HitTestResult localHitTestResult = ((WebView)paramView).getHitTestResult();
    if (localHitTestResult == null)
      return;
    int i = localHitTestResult.getType();
    switch (i)
    {
    case 0:
    case 9:
    }
    this.mActivity.getMenuInflater().inflate(getMenuResourceId(), paramContextMenu);
    for (int j = 0; j < paramContextMenu.size(); j++)
      paramContextMenu.getItem(j).setOnMenuItemClickListener(this);
    String str1 = localHitTestResult.getExtra();
    int k = getMenuGroupResId(MenuGroupType.PHONE_GROUP);
    boolean bool1;
    if (i == 2)
      bool1 = true;
    while (true)
    {
      paramContextMenu.setGroupVisible(k, bool1);
      int m = getMenuGroupResId(MenuGroupType.EMAIL_GROUP);
      boolean bool2;
      label155: boolean bool3;
      label183: boolean bool4;
      if (i == 4)
      {
        bool2 = true;
        paramContextMenu.setGroupVisible(m, bool2);
        int n = getMenuGroupResId(MenuGroupType.GEO_GROUP);
        if (i != 3)
          break label545;
        bool3 = true;
        paramContextMenu.setGroupVisible(n, bool3);
        int i1 = getMenuGroupResId(MenuGroupType.ANCHOR_GROUP);
        if ((i != 7) && (i != 8))
          break label551;
        bool4 = true;
        paramContextMenu.setGroupVisible(i1, bool4);
      }
      switch (i)
      {
      case 5:
      case 6:
      default:
        return;
      case 2:
        try
        {
          String str4 = URLDecoder.decode(str1, Charset.defaultCharset().name());
          str3 = str4;
          paramContextMenu.setHeaderTitle(str3);
          MenuItem localMenuItem7 = paramContextMenu.findItem(getMenuResIdForMenuType(MenuType.DIAL_MENU));
          localMenuItem7.setOnMenuItemClickListener(null);
          localMenuItem7.setIntent(new Intent("android.intent.action.VIEW", Uri.parse("tel:" + str1)));
          MenuItem localMenuItem8 = paramContextMenu.findItem(getMenuResIdForMenuType(MenuType.SMS_MENU));
          localMenuItem8.setOnMenuItemClickListener(null);
          localMenuItem8.setIntent(new Intent("android.intent.action.SENDTO", Uri.parse("smsto:" + str1)));
          Intent localIntent = new Intent("android.intent.action.INSERT_OR_EDIT");
          localIntent.setType("vnd.android.cursor.item/contact");
          localIntent.putExtra("phone", str3);
          MenuItem localMenuItem9 = paramContextMenu.findItem(getMenuResIdForMenuType(MenuType.ADD_CONTACT_MENU));
          localMenuItem9.setOnMenuItemClickListener(null);
          localMenuItem9.setIntent(localIntent);
          MenuItem localMenuItem10 = paramContextMenu.findItem(getMenuResIdForMenuType(MenuType.COPY_PHONE_MENU));
          Copy localCopy4 = new Copy(str1);
          localMenuItem10.setOnMenuItemClickListener(localCopy4);
          return;
          bool1 = false;
          continue;
          bool2 = false;
          break label155;
          label545: bool3 = false;
          break label183;
          label551: bool4 = false;
        }
        catch (UnsupportedEncodingException localUnsupportedEncodingException2)
        {
          while (true)
            String str3 = str1;
        }
      case 4:
      case 3:
      case 7:
      case 8:
      }
    }
    paramContextMenu.setHeaderTitle(str1);
    paramContextMenu.findItem(getMenuResIdForMenuType(MenuType.EMAIL_CONTACT_MENU)).setIntent(new Intent("android.intent.action.VIEW", Uri.parse("mailto:" + str1)));
    MenuItem localMenuItem6 = paramContextMenu.findItem(getMenuResIdForMenuType(MenuType.COPY_MAIL_MENU));
    Copy localCopy3 = new Copy(str1);
    localMenuItem6.setOnMenuItemClickListener(localCopy3);
    return;
    paramContextMenu.setHeaderTitle(str1);
    Object localObject = "";
    try
    {
      String str2 = URLEncoder.encode(str1, Charset.defaultCharset().name());
      localObject = str2;
      label696: MenuItem localMenuItem4 = paramContextMenu.findItem(getMenuResIdForMenuType(MenuType.MAP_MENU));
      localMenuItem4.setOnMenuItemClickListener(null);
      localMenuItem4.setIntent(new Intent("android.intent.action.VIEW", Uri.parse("geo:0,0?q=" + (String)localObject)));
      MenuItem localMenuItem5 = paramContextMenu.findItem(getMenuResIdForMenuType(MenuType.COPY_GEO_MENU));
      Copy localCopy2 = new Copy(str1);
      localMenuItem5.setOnMenuItemClickListener(localCopy2);
      return;
      paramContextMenu.findItem(getMenuResIdForMenuType(MenuType.SHARE_LINK_MENU)).setVisible(showShareLinkMenuItem());
      paramContextMenu.setHeaderTitle(str1);
      MenuItem localMenuItem1 = paramContextMenu.findItem(getMenuResIdForMenuType(MenuType.COPY_LINK_MENU));
      Copy localCopy1 = new Copy(str1);
      localMenuItem1.setOnMenuItemClickListener(localCopy1);
      MenuItem localMenuItem2 = paramContextMenu.findItem(getMenuResIdForMenuType(MenuType.OPEN_MENU));
      localMenuItem2.setOnMenuItemClickListener(null);
      localMenuItem2.setIntent(new Intent("android.intent.action.VIEW", Uri.parse(str1)));
      MenuItem localMenuItem3 = paramContextMenu.findItem(getMenuResIdForMenuType(MenuType.SHARE_LINK_MENU));
      Share localShare = new Share(str1);
      localMenuItem3.setOnMenuItemClickListener(localShare);
      return;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException1)
    {
      break label696;
    }
  }

  public boolean onMenuItemClick(MenuItem paramMenuItem)
  {
    return onMenuItemSelected(paramMenuItem);
  }

  protected boolean onMenuItemSelected(MenuItem paramMenuItem)
  {
    return this.mActivity.onOptionsItemSelected(paramMenuItem);
  }

  private class Copy
    implements MenuItem.OnMenuItemClickListener
  {
    private final CharSequence mText;

    public Copy(CharSequence arg2)
    {
      Object localObject;
      this.mText = localObject;
    }

    public boolean onMenuItemClick(MenuItem paramMenuItem)
    {
      WebViewContextMenu.this.copy(this.mText);
      return true;
    }
  }

  protected static enum MenuGroupType
  {
    static
    {
      EMAIL_GROUP = new MenuGroupType("EMAIL_GROUP", 1);
      GEO_GROUP = new MenuGroupType("GEO_GROUP", 2);
      ANCHOR_GROUP = new MenuGroupType("ANCHOR_GROUP", 3);
      MenuGroupType[] arrayOfMenuGroupType = new MenuGroupType[4];
      arrayOfMenuGroupType[0] = PHONE_GROUP;
      arrayOfMenuGroupType[1] = EMAIL_GROUP;
      arrayOfMenuGroupType[2] = GEO_GROUP;
      arrayOfMenuGroupType[3] = ANCHOR_GROUP;
    }
  }

  protected static enum MenuType
  {
    static
    {
      COPY_LINK_MENU = new MenuType("COPY_LINK_MENU", 1);
      SHARE_LINK_MENU = new MenuType("SHARE_LINK_MENU", 2);
      DIAL_MENU = new MenuType("DIAL_MENU", 3);
      SMS_MENU = new MenuType("SMS_MENU", 4);
      ADD_CONTACT_MENU = new MenuType("ADD_CONTACT_MENU", 5);
      COPY_PHONE_MENU = new MenuType("COPY_PHONE_MENU", 6);
      EMAIL_CONTACT_MENU = new MenuType("EMAIL_CONTACT_MENU", 7);
      COPY_MAIL_MENU = new MenuType("COPY_MAIL_MENU", 8);
      MAP_MENU = new MenuType("MAP_MENU", 9);
      COPY_GEO_MENU = new MenuType("COPY_GEO_MENU", 10);
      MenuType[] arrayOfMenuType = new MenuType[11];
      arrayOfMenuType[0] = OPEN_MENU;
      arrayOfMenuType[1] = COPY_LINK_MENU;
      arrayOfMenuType[2] = SHARE_LINK_MENU;
      arrayOfMenuType[3] = DIAL_MENU;
      arrayOfMenuType[4] = SMS_MENU;
      arrayOfMenuType[5] = ADD_CONTACT_MENU;
      arrayOfMenuType[6] = COPY_PHONE_MENU;
      arrayOfMenuType[7] = EMAIL_CONTACT_MENU;
      arrayOfMenuType[8] = COPY_MAIL_MENU;
      arrayOfMenuType[9] = MAP_MENU;
      arrayOfMenuType[10] = COPY_GEO_MENU;
    }
  }

  private class Share
    implements MenuItem.OnMenuItemClickListener
  {
    private final String mUri;

    public Share(String arg2)
    {
      Object localObject;
      this.mUri = localObject;
    }

    public boolean onMenuItemClick(MenuItem paramMenuItem)
    {
      WebViewContextMenu.this.shareLink(this.mUri);
      return true;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.browse.WebViewContextMenu
 * JD-Core Version:    0.6.2
 */