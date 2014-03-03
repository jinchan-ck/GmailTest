package com.google.android.gm.common.html.parser;

import com.google.android.gm.common.base.CharEscaper;
import com.google.android.gm.common.base.CharEscapers;
import com.google.android.gm.common.base.CharMatcher;
import com.google.android.gm.common.base.Preconditions;
import com.google.android.gm.common.base.StringUtil;
import com.google.android.gm.common.base.X;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlParser
{
  private static boolean DEBUG = false;
  public static final HtmlWhitelist DEFAULT_WHITELIST = HTML4.getWhitelist();
  private static final String END_COMMENT = "-->";
  static Pattern NEEDS_QUOTING_ATTRIBUTE_VALUE_REGEX = Pattern.compile("[\"'&<>=\\s]");
  private static final String START_COMMENT = "<!--";
  private static final Pattern TRUNCATED_ENTITY = Pattern.compile("\\& \\#? [0-9a-zA-Z]{0,8} $", 4);
  private int clipLength = 2147483647;
  private boolean clipped;
  private String html;
  private List<HtmlDocument.Node> nodes;
  private final boolean preserveAll;
  private final boolean preserveValidHtml;
  private State state;
  private final HashMap<String, HTML.Attribute> unknownAttributes;
  private final HashMap<String, HTML.Element> unknownElements;
  private List<HtmlWhitelist> whitelists;

  public HtmlParser()
  {
    this(ParseStyle.NORMALIZE);
  }

  public HtmlParser(ParseStyle paramParseStyle)
  {
    HtmlWhitelist[] arrayOfHtmlWhitelist = new HtmlWhitelist[1];
    arrayOfHtmlWhitelist[0] = DEFAULT_WHITELIST;
    this.whitelists = Lists.newArrayList(arrayOfHtmlWhitelist);
    this.unknownElements = Maps.newHashMap();
    this.unknownAttributes = Maps.newHashMap();
    boolean bool1;
    if (paramParseStyle == ParseStyle.PRESERVE_ALL)
    {
      bool1 = true;
      this.preserveAll = bool1;
      if ((!this.preserveAll) && (paramParseStyle != ParseStyle.PRESERVE_VALID))
        break label86;
    }
    label86: for (boolean bool2 = true; ; bool2 = false)
    {
      this.preserveValidHtml = bool2;
      return;
      bool1 = false;
      break;
    }
  }

  private void addAttribute(ArrayList<HtmlDocument.TagAttribute> paramArrayList, AttributeScanner paramAttributeScanner, int paramInt1, int paramInt2)
  {
    boolean bool1;
    String str1;
    if (paramInt1 < paramInt2)
    {
      bool1 = true;
      X.assertTrue(bool1);
      str1 = paramAttributeScanner.getName();
      if (str1 == null)
        break label125;
    }
    HTML.Attribute localAttribute;
    String str2;
    label125: for (boolean bool2 = true; ; bool2 = false)
    {
      X.assertTrue(bool2);
      localAttribute = lookupAttribute(str1);
      str2 = paramAttributeScanner.getValue();
      if (localAttribute != null)
        break label131;
      if (DEBUG)
        debug("Unknown attribute: " + str1);
      if (this.preserveAll)
      {
        String str6 = this.html.substring(paramInt1, paramInt2);
        paramArrayList.add(HtmlDocument.createTagAttribute(lookupUnknownAttribute(str1), str2, str6));
      }
      return;
      bool1 = false;
      break;
    }
    label131: if (str2 == null);
    for (String str3 = null; this.preserveAll; str3 = StringUtil.unescapeHTML(str2))
    {
      paramArrayList.add(HtmlDocument.createTagAttribute(localAttribute, str3, this.html.substring(paramInt1, paramInt2)));
      return;
    }
    if (this.preserveValidHtml)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      boolean bool3;
      if (paramInt1 <= paramAttributeScanner.startNamePos)
      {
        bool3 = true;
        X.assertTrue(bool3);
        String str4 = this.html.substring(paramInt1, paramAttributeScanner.startNamePos).replaceAll("\\S+", "");
        if (str4.length() == 0)
          str4 = " ";
        localStringBuilder.append(str4);
        if (str2 != null)
          break label333;
        if (paramAttributeScanner.startNamePos >= paramInt2)
          break label327;
      }
      label327: for (boolean bool6 = true; ; bool6 = false)
      {
        X.assertTrue(bool6);
        String str5 = this.html.substring(paramAttributeScanner.startNamePos, paramInt2);
        localStringBuilder.append(CharEscapers.asciiHtmlEscaper().escape(str5));
        paramArrayList.add(HtmlDocument.createTagAttribute(localAttribute, str3, localStringBuilder.toString()));
        return;
        bool3 = false;
        break;
      }
      label333: localStringBuilder.append(CharEscapers.asciiHtmlEscaper().escape(str1));
      boolean bool4;
      if (paramAttributeScanner.endNamePos < paramAttributeScanner.startValuePos)
      {
        bool4 = true;
        label361: X.assertTrue(bool4);
        localStringBuilder.append(this.html.substring(paramAttributeScanner.endNamePos, paramAttributeScanner.startValuePos));
        if (!paramAttributeScanner.attrValueIsQuoted)
          break label454;
        localStringBuilder.append(str2.replaceAll("<", "&lt;"));
        label409: if (paramAttributeScanner.endValuePos > paramInt2)
          break label513;
      }
      label513: for (boolean bool5 = true; ; bool5 = false)
      {
        X.assertTrue(bool5);
        localStringBuilder.append(this.html.substring(paramAttributeScanner.endValuePos, paramInt2));
        break;
        bool4 = false;
        break label361;
        label454: if (NEEDS_QUOTING_ATTRIBUTE_VALUE_REGEX.matcher(str2).find())
        {
          localStringBuilder.append('"');
          localStringBuilder.append(str2.replaceAll("\"", "&quot;"));
          localStringBuilder.append('"');
          break label409;
        }
        localStringBuilder.append(str2);
        break label409;
      }
    }
    paramArrayList.add(HtmlDocument.createTagAttribute(localAttribute, str3));
  }

  private void addEndTag(HTML.Element paramElement, int paramInt1, int paramInt2, int paramInt3)
  {
    boolean bool1;
    boolean bool2;
    label28: boolean bool3;
    if (paramElement != null)
    {
      bool1 = true;
      X.assertTrue(bool1);
      if (this.html.charAt(paramInt1) != '<')
        break label112;
      bool2 = true;
      X.assertTrue(bool2);
      if (this.html.charAt(paramInt1 + 1) != '/')
        break label118;
      bool3 = true;
      label51: X.assertTrue(bool3);
      if (!this.preserveAll)
        break label130;
      if (paramInt1 >= paramInt3)
        break label124;
    }
    label112: label118: label124: for (boolean bool6 = true; ; bool6 = false)
    {
      X.assertTrue(bool6);
      String str3 = this.html.substring(paramInt1, paramInt3);
      this.nodes.add(HtmlDocument.createEndTag(paramElement, str3));
      return;
      bool1 = false;
      break;
      bool2 = false;
      break label28;
      bool3 = false;
      break label51;
    }
    label130: if (this.preserveValidHtml)
    {
      StringBuilder localStringBuilder = new StringBuilder("</");
      boolean bool4;
      if (paramInt1 < paramInt2)
      {
        bool4 = true;
        X.assertTrue(bool4);
        String str1 = this.html.substring(paramInt1 + 2, paramInt2);
        localStringBuilder.append(CharEscapers.asciiHtmlEscaper().escape(str1));
        if (paramInt2 > paramInt3)
          break label297;
      }
      label297: for (boolean bool5 = true; ; bool5 = false)
      {
        X.assertTrue(bool5);
        String str2 = this.html.substring(paramInt2, paramInt3);
        if (str2.charAt(str2.length() - 1) != '>')
          str2 = str2 + '>';
        localStringBuilder.append(str2.replaceAll("\\S+.*>", ">"));
        this.nodes.add(HtmlDocument.createEndTag(paramElement, localStringBuilder.toString()));
        return;
        bool4 = false;
        break;
      }
    }
    this.nodes.add(HtmlDocument.createEndTag(paramElement));
  }

  private void addStartTag(HTML.Element paramElement, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean, ArrayList<HtmlDocument.TagAttribute> paramArrayList)
  {
    boolean bool1;
    boolean bool2;
    label22: boolean bool3;
    label37: String str3;
    String str4;
    if (paramInt1 < paramInt2)
    {
      bool1 = true;
      X.assertTrue(bool1);
      if (paramInt2 > paramInt3)
        break label109;
      bool2 = true;
      X.assertTrue(bool2);
      if (paramInt3 > paramInt4)
        break label115;
      bool3 = true;
      X.assertTrue(bool3);
      if (!this.preserveAll)
        break label136;
      str3 = this.html.substring(paramInt1, paramInt2);
      str4 = this.html.substring(paramInt3, paramInt4);
      if (!paramBoolean)
        break label121;
    }
    label109: label115: label121: for (HtmlDocument.Tag localTag3 = HtmlDocument.createSelfTerminatingTag(paramElement, paramArrayList, str3, str4); ; localTag3 = HtmlDocument.createTag(paramElement, paramArrayList, str3, str4))
    {
      this.nodes.add(localTag3);
      return;
      bool1 = false;
      break;
      bool2 = false;
      break label22;
      bool3 = false;
      break label37;
    }
    label136: if (this.preserveValidHtml)
    {
      boolean bool4;
      StringBuilder localStringBuilder;
      boolean bool5;
      label225: boolean bool8;
      label255: boolean bool6;
      label270: boolean bool7;
      label285: String str2;
      if (this.html.charAt(paramInt1) == '<')
      {
        bool4 = true;
        X.assertTrue(bool4);
        localStringBuilder = new StringBuilder("<");
        String str1 = this.html.substring(paramInt1 + 1, paramInt2);
        localStringBuilder.append(CharEscapers.asciiHtmlEscaper().escape(str1));
        int i = paramInt4 - 1;
        if (this.html.charAt(i) != '>')
          break label342;
        bool5 = true;
        X.assertTrue(bool5);
        if (paramBoolean)
        {
          i--;
          if (this.html.charAt(i) != '/')
            break label348;
          bool8 = true;
          X.assertTrue(bool8);
        }
        if (paramInt3 > i)
          break label354;
        bool6 = true;
        X.assertTrue(bool6);
        if (paramInt3 >= paramInt4)
          break label360;
        bool7 = true;
        X.assertTrue(bool7);
        str2 = this.html.substring(paramInt3, paramInt4);
        if (!paramBoolean)
          break label366;
      }
      label342: label348: label354: label360: label366: for (HtmlDocument.Tag localTag2 = HtmlDocument.createSelfTerminatingTag(paramElement, paramArrayList, localStringBuilder.toString(), str2); ; localTag2 = HtmlDocument.createTag(paramElement, paramArrayList, localStringBuilder.toString(), str2))
      {
        this.nodes.add(localTag2);
        return;
        bool4 = false;
        break;
        bool5 = false;
        break label225;
        bool8 = false;
        break label255;
        bool6 = false;
        break label270;
        bool7 = false;
        break label285;
      }
    }
    if (paramBoolean);
    for (HtmlDocument.Tag localTag1 = HtmlDocument.createSelfTerminatingTag(paramElement, paramArrayList); ; localTag1 = HtmlDocument.createTag(paramElement, paramArrayList))
    {
      this.nodes.add(localTag1);
      return;
    }
  }

  static List<HtmlDocument.Node> coalesceTextNodes(List<HtmlDocument.Node> paramList)
  {
    ArrayList localArrayList = new ArrayList(paramList.size());
    LinkedList localLinkedList = Lists.newLinkedList();
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      HtmlDocument.Node localNode = (HtmlDocument.Node)localIterator.next();
      if ((localNode instanceof HtmlDocument.Text))
      {
        localLinkedList.add((HtmlDocument.Text)localNode);
      }
      else
      {
        mergeTextNodes(localLinkedList, localArrayList);
        localArrayList.add(localNode);
      }
    }
    mergeTextNodes(localLinkedList, localArrayList);
    return localArrayList;
  }

  private static void debug(String paramString)
  {
    System.err.println(paramString);
  }

  private HTML.Attribute lookupUnknownAttribute(String paramString)
  {
    String str = paramString.toLowerCase();
    HTML.Attribute localAttribute = (HTML.Attribute)this.unknownAttributes.get(str);
    if (localAttribute == null)
    {
      localAttribute = new HTML.Attribute(str, 0);
      this.unknownAttributes.put(str, localAttribute);
    }
    return localAttribute;
  }

  private HTML.Element lookupUnknownElement(String paramString)
  {
    String str = paramString.toLowerCase();
    HTML.Element localElement = (HTML.Element)this.unknownElements.get(str);
    if (localElement == null)
    {
      localElement = new HTML.Element(str, 0, false, true, false, HTML.Element.Flow.NONE);
      this.unknownElements.put(str, localElement);
    }
    return localElement;
  }

  public static void main(String[] paramArrayOfString)
    throws IOException
  {
    DEBUG = true;
    String str = new String(ByteStreams.toByteArray(System.in), "ISO-8859-1");
    HtmlDocument localHtmlDocument = new HtmlParser().parse(str);
    System.out.println(localHtmlDocument.toString());
  }

  private static void mergeTextNodes(LinkedList<HtmlDocument.Text> paramLinkedList, List<HtmlDocument.Node> paramList)
  {
    if (!paramLinkedList.isEmpty())
    {
      if (paramLinkedList.size() == 1)
        paramList.add(paramLinkedList.removeFirst());
    }
    else
      return;
    int i = 0;
    int j = 0;
    Iterator localIterator = paramLinkedList.iterator();
    while (localIterator.hasNext())
    {
      HtmlDocument.Text localText2 = (HtmlDocument.Text)localIterator.next();
      i += localText2.getText().length();
      if (localText2.getOriginalHTML() != null)
        j += localText2.getOriginalHTML().length();
    }
    StringBuilder localStringBuilder1 = new StringBuilder(i);
    StringBuilder localStringBuilder2 = new StringBuilder(j);
    while (!paramLinkedList.isEmpty())
    {
      HtmlDocument.Text localText1 = (HtmlDocument.Text)paramLinkedList.removeFirst();
      localStringBuilder1.append(localText1.getText());
      if (localText1.getOriginalHTML() != null)
        localStringBuilder2.append(localText1.getOriginalHTML());
    }
    if (j > 0);
    for (String str = localStringBuilder2.toString(); ; str = null)
    {
      paramList.add(HtmlDocument.createText(localStringBuilder1.toString(), str));
      return;
    }
  }

  private int scanComment(int paramInt1, int paramInt2)
  {
    X.assertTrue(this.html.regionMatches(paramInt1, "<!--", 0, "<!--".length()));
    int i = this.html.indexOf("-->", paramInt1 + "<!--".length());
    int k;
    if (i != -1)
      k = i + "-->".length();
    while (true)
    {
      if (this.preserveAll)
        this.nodes.add(HtmlDocument.createHtmlComment(this.html.substring(paramInt1, k)));
      return k;
      int j = this.html.indexOf('>', paramInt1 + 4);
      if (j != -1)
        k = j + 1;
      else
        k = paramInt2;
    }
  }

  public void addWhitelist(HtmlWhitelist paramHtmlWhitelist)
  {
    this.whitelists.add(paramHtmlWhitelist);
  }

  public boolean isClipped()
  {
    return this.clipped;
  }

  HTML.Attribute lookupAttribute(String paramString)
  {
    ListIterator localListIterator = this.whitelists.listIterator(this.whitelists.size());
    while (localListIterator.hasPrevious())
    {
      HTML.Attribute localAttribute = ((HtmlWhitelist)localListIterator.previous()).lookupAttribute(paramString);
      if (localAttribute != null)
        return localAttribute;
    }
    return null;
  }

  HTML.Element lookupElement(String paramString)
  {
    ListIterator localListIterator = this.whitelists.listIterator(this.whitelists.size());
    while (localListIterator.hasPrevious())
    {
      HTML.Element localElement = ((HtmlWhitelist)localListIterator.previous()).lookupElement(paramString);
      if (localElement != null)
        return localElement;
    }
    return null;
  }

  public HtmlDocument parse(String paramString)
  {
    this.html = paramString;
    this.nodes = Lists.newLinkedList();
    this.state = State.IN_TEXT;
    this.clipped = false;
    int i = paramString.length();
    int j = Math.min(this.clipLength, i);
    int k = 0;
    if ((k < i) && (!this.clipped))
    {
      int m;
      boolean bool5;
      switch (1.$SwitchMap$com$google$android$gm$common$html$parser$HtmlParser$State[this.state.ordinal()])
      {
      default:
        throw new Error("Unknown state!");
      case 1:
        m = scanText(k, j);
        if ((m > k) || (this.state != State.IN_TEXT))
        {
          bool5 = true;
          label136: X.assertTrue(bool5);
          k = m;
          if (m < this.clipLength)
            break label288;
        }
        break;
      case 2:
      case 3:
      case 4:
      }
      label288: for (boolean bool2 = true; ; bool2 = false)
      {
        this.clipped = bool2;
        break;
        bool5 = false;
        break label136;
        m = scanTag(k, i);
        if (m > k);
        for (boolean bool4 = true; ; bool4 = false)
        {
          X.assertTrue(bool4);
          break;
        }
        m = scanComment(k, i);
        this.state = State.IN_TEXT;
        if (m > k);
        for (boolean bool3 = true; ; bool3 = false)
        {
          X.assertTrue(bool3);
          break;
        }
        m = scanCDATA(k, i);
        if ((m > k) || (this.state != State.IN_CDATA));
        for (boolean bool1 = true; ; bool1 = false)
        {
          X.assertTrue(bool1);
          break;
        }
      }
    }
    this.nodes = coalesceTextNodes(this.nodes);
    HtmlDocument localHtmlDocument = new HtmlDocument(this.nodes);
    this.nodes = null;
    return localHtmlDocument;
  }

  int scanCDATA(int paramInt1, int paramInt2)
  {
    HTML.Element localElement = ((HtmlDocument.Tag)this.nodes.get(this.nodes.size() - 1)).getElement();
    boolean bool;
    if ((HTML4.SCRIPT_ELEMENT.equals(localElement)) || (HTML4.STYLE_ELEMENT.equals(localElement)))
    {
      bool = true;
      X.assertTrue(bool);
    }
    for (int i = paramInt1; ; i++)
      if ((i >= paramInt2) || ((i + 2 < paramInt2) && (this.html.charAt(i) == '<') && (this.html.charAt(i + 1) == '/') && (this.html.regionMatches(true, i + 2, localElement.getName(), 0, localElement.getName().length()))))
      {
        if (i > paramInt1)
        {
          HtmlDocument.CDATA localCDATA = HtmlDocument.createCDATA(this.html.substring(paramInt1, i));
          this.nodes.add(localCDATA);
        }
        this.state = State.IN_TAG;
        return i;
        bool = false;
        break;
      }
  }

  int scanTag(int paramInt1, int paramInt2)
  {
    boolean bool1;
    int i;
    int k;
    int m;
    String str1;
    if (this.html.charAt(paramInt1) == '<')
    {
      bool1 = true;
      X.assertTrue(bool1);
      i = paramInt1 + 1;
      this.state = State.IN_TEXT;
      int j = this.html.charAt(i);
      k = 0;
      if (j == 47)
      {
        k = 1;
        i++;
      }
      TagNameScanner localTagNameScanner = new TagNameScanner(this.html);
      m = localTagNameScanner.scanName(i, paramInt2);
      str1 = localTagNameScanner.getTagName();
      if (str1 != null)
        break label322;
      if (k != 0)
        break label151;
      if (!this.preserveAll)
        break label145;
    }
    label145: for (String str4 = "<"; ; str4 = null)
    {
      HtmlDocument.Text localText = HtmlDocument.createText("<", str4);
      this.nodes.add(localText);
      this.state = State.IN_TEXT;
      return i;
      bool1 = false;
      break;
    }
    label151: boolean bool8 = this.preserveAll;
    HTML.Element localElement = null;
    if (bool8)
      localElement = lookupUnknownElement("");
    ArrayList localArrayList = null;
    int n = m;
    int i1 = m;
    AttributeScanner localAttributeScanner = new AttributeScanner(this.html);
    int i2 = m;
    boolean bool2 = false;
    int i4;
    char c;
    label262: boolean bool4;
    label276: String str2;
    String str3;
    if (i2 < paramInt2)
    {
      i4 = m;
      c = this.html.charAt(m);
      if ((m + 1 < paramInt2) && (c == '/') && (this.html.charAt(m + 1) == '>'))
      {
        bool2 = true;
        m++;
      }
    }
    else
    {
      if (m != paramInt2)
        break label602;
      if (paramInt1 >= paramInt2)
        break label558;
      bool4 = true;
      X.assertTrue(bool4);
      str2 = this.html.substring(paramInt1, paramInt2);
      if (!this.preserveAll)
        break label564;
      str3 = str2;
    }
    while (true)
    {
      this.nodes.add(HtmlDocument.createEscapedText(str2, str3));
      return paramInt2;
      label322: localElement = lookupElement(str1);
      if (localElement != null)
        break;
      if (DEBUG)
        debug("Unknown element: " + str1);
      if (!this.preserveAll)
        break;
      localElement = lookupUnknownElement(str1);
      break;
      bool2 = false;
      if (c == '>')
        break label262;
      if ((k != 0) && ('<' == c))
      {
        if (localElement != null)
          addEndTag(localElement, paramInt1, n, m);
        this.state = State.IN_TEXT;
        return m;
      }
      if (Character.isWhitespace(c))
      {
        m++;
        if (m <= i4)
          break label552;
      }
      label552: for (boolean bool7 = true; ; bool7 = false)
      {
        X.assertTrue(bool7);
        break;
        localAttributeScanner.reset();
        m = localAttributeScanner.scanName(m, paramInt2);
        if (m > i4);
        for (boolean bool6 = true; ; bool6 = false)
        {
          X.assertTrue(bool6);
          if (localAttributeScanner.getName() == null)
            break;
          m = localAttributeScanner.scanValue(m, paramInt2);
          if (localElement != null)
          {
            if (localArrayList == null)
              localArrayList = new ArrayList();
            addAttribute(localArrayList, localAttributeScanner, i1, m);
          }
          i1 = m;
          break;
        }
      }
      label558: bool4 = false;
      break label276;
      label564: boolean bool5 = this.preserveValidHtml;
      str3 = null;
      if (bool5)
        str3 = CharMatcher.is('<').replaceFrom(this.html.substring(paramInt1, paramInt2), "&lt;");
    }
    label602: boolean bool3;
    int i3;
    if (this.html.charAt(m) == '>')
    {
      bool3 = true;
      X.assertTrue(bool3);
      i3 = m + 1;
      if (localElement != null)
      {
        if (k == 0)
          break label660;
        addEndTag(localElement, paramInt1, n, i3);
      }
    }
    while (true)
    {
      return i3;
      bool3 = false;
      break;
      label660: if ((HTML4.SCRIPT_ELEMENT.equals(localElement)) || (HTML4.STYLE_ELEMENT.equals(localElement)))
        this.state = State.IN_CDATA;
      addStartTag(localElement, paramInt1, n, i1, i3, bool2, localArrayList);
    }
  }

  int scanText(int paramInt1, int paramInt2)
  {
    int i = paramInt1;
    label93: String str1;
    String str2;
    if (i < paramInt2)
    {
      if ((this.html.charAt(i) != '<') || (i + 1 >= paramInt2))
        break label226;
      char c = this.html.charAt(i + 1);
      if ((c != '/') && (!Character.isLetter(c)) && (c != '!') && (c != '?'))
        break label226;
      if (this.html.regionMatches(i + 1, "!--", 0, 3))
        this.state = State.IN_COMMENT;
    }
    else if (i > paramInt1)
    {
      int j = i;
      str1 = this.html.substring(paramInt1, j);
      if ((i == this.clipLength) && (this.clipLength < this.html.length()))
      {
        Matcher localMatcher = TRUNCATED_ENTITY.matcher(str1);
        if (localMatcher.find())
        {
          int k = localMatcher.start();
          j = paramInt1 + k;
          str1 = str1.substring(0, k);
        }
      }
      if (j > paramInt1)
      {
        if (!this.preserveAll)
          break label232;
        str2 = str1;
      }
    }
    while (true)
    {
      HtmlDocument.Text localText = HtmlDocument.createEscapedText(str1, str2);
      this.nodes.add(localText);
      return i;
      this.state = State.IN_TAG;
      break label93;
      label226: i++;
      break;
      label232: boolean bool = this.preserveValidHtml;
      str2 = null;
      if (bool)
        str2 = CharMatcher.is('<').replaceFrom(str1, "&lt;");
    }
  }

  public void setClipLength(int paramInt)
  {
    if (paramInt <= 0)
      throw new IllegalArgumentException("clipLength '" + paramInt + "' <= 0");
    this.clipLength = paramInt;
  }

  public void setWhitelist(HtmlWhitelist paramHtmlWhitelist)
  {
    Preconditions.checkNotNull(paramHtmlWhitelist);
    this.whitelists = Lists.newArrayList(new HtmlWhitelist[] { paramHtmlWhitelist });
  }

  private static class AttributeScanner
  {
    boolean attrValueIsQuoted = false;
    int endNamePos = -1;
    int endValuePos = -1;
    private final String html;
    private String name;
    int startNamePos = -1;
    int startValuePos = -1;
    private String value;

    public AttributeScanner(String paramString)
    {
      this.html = paramString;
    }

    private int skipSpaces(int paramInt1, int paramInt2)
    {
      for (int i = paramInt1; ; i++)
        if ((i >= paramInt2) || (!Character.isWhitespace(this.html.charAt(i))))
          return i;
    }

    public String getName()
    {
      if ((this.name == null) && (this.startNamePos != -1) && (this.endNamePos != -1))
        this.name = this.html.substring(this.startNamePos, this.endNamePos);
      return this.name;
    }

    public String getValue()
    {
      if ((this.value == null) && (this.startValuePos != -1) && (this.endValuePos != -1))
        this.value = this.html.substring(this.startValuePos, this.endValuePos);
      return this.value;
    }

    public void reset()
    {
      this.startNamePos = -1;
      this.endNamePos = -1;
      this.startValuePos = -1;
      this.endValuePos = -1;
      this.attrValueIsQuoted = false;
      this.name = null;
      this.value = null;
    }

    int scanName(int paramInt1, int paramInt2)
    {
      if (this.html.charAt(paramInt1) != '>');
      for (boolean bool = true; ; bool = false)
      {
        X.assertTrue(bool);
        if (paramInt1 != paramInt2)
          break;
        return paramInt1;
      }
      for (int i = paramInt1 + 1; ; i++)
        if (i < paramInt2)
        {
          char c = this.html.charAt(i);
          if ((c != '>') && (c != '=') && (c != '/') && (!Character.isWhitespace(c)));
        }
        else
        {
          this.startNamePos = paramInt1;
          this.endNamePos = i;
          return i;
        }
    }

    int scanValue(int paramInt1, int paramInt2)
    {
      int i = skipSpaces(paramInt1, paramInt2);
      if ((i == paramInt2) || (this.html.charAt(i) != '='))
        return paramInt1;
      int j = skipSpaces(i + 1, paramInt2);
      if (j == paramInt2)
        return j;
      int k = this.html.charAt(j);
      boolean bool1;
      label141: boolean bool2;
      label157: boolean bool3;
      if ((k == 39) || (k == 34))
      {
        this.attrValueIsQuoted = true;
        j++;
        int m = j;
        while ((j < paramInt2) && (this.html.charAt(j) != k))
          j++;
        this.startValuePos = m;
        this.endValuePos = j;
        if (j < paramInt2)
          j++;
        if (this.startValuePos <= -1)
          break label255;
        bool1 = true;
        X.assertTrue(bool1);
        if (this.endValuePos <= -1)
          break label261;
        bool2 = true;
        X.assertTrue(bool2);
        if (this.startValuePos > this.endValuePos)
          break label267;
        bool3 = true;
        label176: X.assertTrue(bool3);
        if (j > paramInt2)
          break label273;
      }
      label261: label267: label273: for (boolean bool4 = true; ; bool4 = false)
      {
        X.assertTrue(bool4);
        return j;
        int n = j;
        while (true)
        {
          if (j < paramInt2)
          {
            char c = this.html.charAt(j);
            if ((c != '>') && (!Character.isWhitespace(c)));
          }
          else
          {
            this.startValuePos = n;
            this.endValuePos = j;
            break;
          }
          j++;
        }
        label255: bool1 = false;
        break label141;
        bool2 = false;
        break label157;
        bool3 = false;
        break label176;
      }
    }
  }

  public static enum ParseStyle
  {
    static
    {
      PRESERVE_ALL = new ParseStyle("PRESERVE_ALL", 2);
      ParseStyle[] arrayOfParseStyle = new ParseStyle[3];
      arrayOfParseStyle[0] = NORMALIZE;
      arrayOfParseStyle[1] = PRESERVE_VALID;
      arrayOfParseStyle[2] = PRESERVE_ALL;
    }
  }

  private static enum State
  {
    static
    {
      IN_TAG = new State("IN_TAG", 1);
      IN_COMMENT = new State("IN_COMMENT", 2);
      IN_CDATA = new State("IN_CDATA", 3);
      State[] arrayOfState = new State[4];
      arrayOfState[0] = IN_TEXT;
      arrayOfState[1] = IN_TAG;
      arrayOfState[2] = IN_COMMENT;
      arrayOfState[3] = IN_CDATA;
    }
  }

  private static class TagNameScanner
  {
    private int endNamePos = -1;
    private final String html;
    private int startNamePos = -1;
    private String tagName;

    public TagNameScanner(String paramString)
    {
      this.html = paramString;
    }

    public String getTagName()
    {
      if ((this.tagName == null) && (this.startNamePos != -1) && (this.endNamePos != -1))
        this.tagName = this.html.substring(this.startNamePos, this.endNamePos);
      return this.tagName;
    }

    public int scanName(int paramInt1, int paramInt2)
    {
      for (int i = paramInt1; ; i++)
        if (i < paramInt2)
        {
          char c = this.html.charAt(i);
          if ((c != '>') && (c != '/') && (!Character.isWhitespace(c)));
        }
        else
        {
          if (i > paramInt1)
          {
            this.startNamePos = paramInt1;
            this.endNamePos = i;
          }
          return i;
        }
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.common.html.parser.HtmlParser
 * JD-Core Version:    0.6.2
 */