package com.google.android.gm.common.html.parser;

import com.google.android.gm.common.base.CharMatcher;
import com.google.android.gm.common.base.Preconditions;
import com.google.android.gm.common.base.X;
import com.google.common.collect.ImmutableSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Logger;

public class HtmlTree
{
  private static final boolean DEBUG;
  private static final PlainTextConverterFactory DEFAULT_CONVERTER_FACTORY = new PlainTextConverterFactory()
  {
    public HtmlTree.PlainTextConverter createInstance()
    {
      return new HtmlTree.DefaultPlainTextConverter();
    }
  };
  private static final Logger logger = Logger.getLogger(HtmlTree.class.getName());
  private final Stack<Integer> begins = new Stack();
  private PlainTextConverterFactory converterFactory = DEFAULT_CONVERTER_FACTORY;
  private final Stack<Integer> ends = new Stack();
  private String html;
  private final List<HtmlDocument.Node> nodes = new ArrayList();
  private int parent;
  private String plainText;
  private Stack<Integer> stack;
  private int[] textPositions;

  private void addNode(HtmlDocument.Node paramNode, int paramInt1, int paramInt2)
  {
    this.nodes.add(paramNode);
    this.begins.add(Integer.valueOf(paramInt1));
    this.ends.add(Integer.valueOf(paramInt2));
  }

  private boolean canBeginBlockAt(int paramInt)
  {
    int i = this.textPositions[paramInt];
    if (i == this.plainText.length())
      i--;
    for (int j = i; j > 0; j--)
    {
      char c = this.plainText.charAt(j);
      if (c == '\n')
        return true;
      if ((j < i) && (!Character.isWhitespace(c)))
        return false;
    }
    return true;
  }

  private void convertToPlainText()
  {
    if ((this.plainText == null) && (this.textPositions == null));
    int i;
    PlainTextConverter localPlainTextConverter;
    for (boolean bool = true; ; bool = false)
    {
      X.assertTrue(bool);
      i = this.nodes.size();
      this.textPositions = new int[i + 1];
      localPlainTextConverter = this.converterFactory.createInstance();
      for (int j = 0; j < i; j++)
      {
        this.textPositions[j] = localPlainTextConverter.getPlainTextLength();
        localPlainTextConverter.addNode((HtmlDocument.Node)this.nodes.get(j), j, ((Integer)this.ends.get(j)).intValue());
      }
    }
    this.textPositions[i] = localPlainTextConverter.getPlainTextLength();
    this.plainText = localPlainTextConverter.getPlainText();
  }

  private static final void debug(String paramString)
  {
    logger.finest(paramString);
  }

  private int getBlockEnd(int paramInt)
  {
    int i = Arrays.binarySearch(this.textPositions, paramInt);
    if (i >= 0)
      while ((i + 1 < this.textPositions.length) && (this.textPositions[(i + 1)] == paramInt))
        i++;
    i = -i - 2;
    if ((i >= 0) && (i <= this.nodes.size()));
    for (boolean bool = true; ; bool = false)
    {
      X.assertTrue(bool);
      return i;
    }
  }

  private int getBlockStart(int paramInt)
  {
    int i = Arrays.binarySearch(this.textPositions, paramInt);
    if (i >= 0)
      while ((i - 1 >= 0) && (this.textPositions[(i - 1)] == paramInt))
        i--;
    i = -i - 1;
    if ((i >= 0) && (i <= this.nodes.size()));
    for (boolean bool = true; ; bool = false)
    {
      X.assertTrue(bool);
      return i;
    }
  }

  void addEndTag(HtmlDocument.EndTag paramEndTag)
  {
    int i = this.nodes.size();
    addNode(paramEndTag, this.parent, i);
    if (this.parent != -1)
      this.ends.set(this.parent, Integer.valueOf(i));
    this.parent = ((Integer)this.stack.pop()).intValue();
  }

  void addSingularTag(HtmlDocument.Tag paramTag)
  {
    int i = this.nodes.size();
    addNode(paramTag, i, i);
  }

  void addStartTag(HtmlDocument.Tag paramTag)
  {
    int i = this.nodes.size();
    addNode(paramTag, i, -1);
    this.stack.add(Integer.valueOf(this.parent));
    this.parent = i;
  }

  void addText(HtmlDocument.Text paramText)
  {
    int i = this.nodes.size();
    addNode(paramText, i, i);
  }

  public ArrayList<Block> createBlocks(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    ArrayList localArrayList = new ArrayList();
    int i = Math.max(getBlockStart(paramInt1), paramInt3);
    int j = Math.min(getBlockEnd(paramInt2), paramInt4);
    int k = -1;
    int m = i;
    while (m < j)
    {
      int n = ((Integer)this.begins.get(m)).intValue();
      int i1 = ((Integer)this.ends.get(m)).intValue();
      if (k == -1)
      {
        if ((n >= m) && (i1 <= j) && (canBeginBlockAt(m)))
        {
          k = m;
          m = i1 + 1;
        }
        else
        {
          m++;
        }
      }
      else if ((n >= k) && (i1 < j))
      {
        m = i1 + 1;
      }
      else
      {
        Block localBlock2 = new Block();
        localBlock2.start_node = k;
        localBlock2.end_node = m;
        localArrayList.add(localBlock2);
        k = -1;
        m++;
      }
    }
    if (k != -1)
    {
      Block localBlock1 = new Block();
      localBlock1.start_node = k;
      localBlock1.end_node = j;
      localArrayList.add(localBlock1);
    }
    return localArrayList;
  }

  void finish()
  {
    boolean bool1;
    if (this.stack.size() == 0)
    {
      bool1 = true;
      X.assertTrue(bool1);
      if (this.parent != -1)
        break label36;
    }
    label36: for (boolean bool2 = true; ; bool2 = false)
    {
      X.assertTrue(bool2);
      return;
      bool1 = false;
      break;
    }
  }

  public String getHtml()
  {
    return getHtml(-1);
  }

  public String getHtml(int paramInt)
  {
    if (this.html == null)
      this.html = getHtml(0, this.nodes.size(), paramInt);
    return this.html;
  }

  public String getHtml(int paramInt1, int paramInt2)
  {
    return getHtml(paramInt1, paramInt2, -1);
  }

  public String getHtml(int paramInt1, int paramInt2, int paramInt3)
  {
    if ((paramInt1 >= 0) && (paramInt2 <= this.nodes.size()));
    StringBuilder localStringBuilder;
    for (boolean bool = true; ; bool = false)
    {
      X.assertTrue(bool);
      localStringBuilder = new StringBuilder(10 * (paramInt2 - paramInt1));
      int i = 0;
      for (int j = paramInt1; j < paramInt2; j++)
      {
        HtmlDocument.Node localNode = (HtmlDocument.Node)this.nodes.get(j);
        localNode.toHTML(localStringBuilder);
        if ((paramInt3 > 0) && ((((localNode instanceof HtmlDocument.Tag)) && (((HtmlDocument.Tag)localNode).getElement().breaksFlow())) || (((localNode instanceof HtmlDocument.EndTag)) && (((HtmlDocument.EndTag)localNode).getElement().breaksFlow()))))
        {
          int k = localStringBuilder.substring(i + 1).lastIndexOf('\n');
          if (k != -1)
            i += k;
          if (localStringBuilder.length() - 1 - i > paramInt3)
          {
            localStringBuilder.append('\n');
            i = localStringBuilder.length() - 1;
          }
        }
      }
    }
    return localStringBuilder.toString();
  }

  public ArrayList<String> getHtmlChunks(int paramInt1, int paramInt2, int paramInt3)
  {
    boolean bool;
    ArrayList localArrayList;
    int i;
    int j;
    StringBuilder localStringBuilder1;
    int k;
    if ((paramInt1 >= 0) && (paramInt2 <= this.nodes.size()))
    {
      bool = true;
      X.assertTrue(bool);
      localArrayList = new ArrayList();
      i = 0;
      j = 1;
      localStringBuilder1 = new StringBuilder(paramInt3 + 256);
      k = paramInt1;
      label57: if (k >= paramInt2)
        break label196;
      HtmlDocument.Node localNode = (HtmlDocument.Node)this.nodes.get(k);
      localNode.toHTML(localStringBuilder1);
      if (((localNode instanceof HtmlDocument.Tag)) && (HTML4.TEXTAREA_ELEMENT.equals(((HtmlDocument.Tag)localNode).getElement())))
        i++;
      if (((localNode instanceof HtmlDocument.EndTag)) && (HTML4.TEXTAREA_ELEMENT.equals(((HtmlDocument.EndTag)localNode).getElement())))
      {
        if (i != 0)
          break label190;
        j = 0;
      }
    }
    while (true)
    {
      if ((i == 0) && (localStringBuilder1.length() >= paramInt3))
      {
        localArrayList.add(localStringBuilder1.toString());
        localStringBuilder1.setLength(0);
      }
      k++;
      break label57;
      bool = false;
      break;
      label190: i--;
    }
    label196: if (localStringBuilder1.length() > 0)
      localArrayList.add(localStringBuilder1.toString());
    if ((j == 0) || (i != 0))
    {
      StringBuilder localStringBuilder2 = new StringBuilder("Returning unbalanced HTML:\n");
      localStringBuilder2.append(getHtml());
      localStringBuilder2.append("\nfromNode: ").append(paramInt1);
      localStringBuilder2.append("\ntoNode: ").append(paramInt2);
      localStringBuilder2.append("\nNum nodes_: ").append(getNumNodes());
      Iterator localIterator = localArrayList.iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        localStringBuilder2.append("\nChunk:\n").append(str);
      }
      logger.severe(localStringBuilder2.toString());
    }
    return localArrayList;
  }

  public List<HtmlDocument.Node> getNodesList()
  {
    return Collections.unmodifiableList(this.nodes);
  }

  public int getNumNodes()
  {
    return this.nodes.size();
  }

  public String getPlainText()
  {
    if (this.plainText == null)
      convertToPlainText();
    return this.plainText;
  }

  public String getPlainText(int paramInt1, int paramInt2)
  {
    if (this.plainText == null)
      convertToPlainText();
    int i = this.textPositions[paramInt1];
    int j = this.textPositions[paramInt2];
    return this.plainText.substring(i, j);
  }

  public int getTextPosition(int paramInt)
  {
    return this.textPositions[paramInt];
  }

  public int getTreeHeight()
  {
    int i = 0;
    int j = 0;
    int k = 0;
    if (k < this.nodes.size())
    {
      HtmlDocument.Node localNode = (HtmlDocument.Node)this.nodes.get(k);
      if ((localNode instanceof HtmlDocument.Tag))
      {
        i++;
        if (i > j)
          j = i;
        if (((HtmlDocument.Tag)localNode).getElement().isEmpty())
          i--;
      }
      while (true)
      {
        k++;
        break;
        if ((localNode instanceof HtmlDocument.EndTag))
          i--;
      }
    }
    return j;
  }

  public void setPlainTextConverterFactory(PlainTextConverterFactory paramPlainTextConverterFactory)
  {
    if (paramPlainTextConverterFactory == null)
      throw new NullPointerException("factory must not be null");
    this.converterFactory = paramPlainTextConverterFactory;
  }

  void start()
  {
    this.stack = new Stack();
    this.parent = -1;
  }

  public static class Block
  {
    public int end_node;
    public int start_node;
  }

  public static class DefaultPlainTextConverter
    implements HtmlTree.PlainTextConverter
  {
    private static final Set<HTML.Element> BLANK_LINE_ELEMENTS = ImmutableSet.of(HTML4.P_ELEMENT, HTML4.BLOCKQUOTE_ELEMENT, HTML4.PRE_ELEMENT);
    private int preDepth = 0;
    private final HtmlTree.PlainTextPrinter printer = new HtmlTree.PlainTextPrinter();

    public void addNode(HtmlDocument.Node paramNode, int paramInt1, int paramInt2)
    {
      String str;
      if ((paramNode instanceof HtmlDocument.Text))
      {
        str = ((HtmlDocument.Text)paramNode).getText();
        if (this.preDepth > 0)
          this.printer.appendPreText(str);
      }
      HTML.Element localElement1;
      do
      {
        do
        {
          HTML.Element localElement2;
          do
          {
            return;
            this.printer.appendNormalText(str);
            return;
            if (!(paramNode instanceof HtmlDocument.Tag))
              break;
            localElement2 = ((HtmlDocument.Tag)paramNode).getElement();
            if (BLANK_LINE_ELEMENTS.contains(localElement2))
              this.printer.setSeparator(HtmlTree.PlainTextPrinter.Separator.BlankLine);
            while (HTML4.BLOCKQUOTE_ELEMENT.equals(localElement2))
            {
              this.printer.incQuoteDepth();
              return;
              if (HTML4.BR_ELEMENT.equals(localElement2))
              {
                this.printer.appendForcedLineBreak();
              }
              else if (localElement2.breaksFlow())
              {
                this.printer.setSeparator(HtmlTree.PlainTextPrinter.Separator.LineBreak);
                if (HTML4.HR_ELEMENT.equals(localElement2))
                {
                  this.printer.appendNormalText("________________________________");
                  this.printer.setSeparator(HtmlTree.PlainTextPrinter.Separator.LineBreak);
                }
              }
            }
          }
          while (!HTML4.PRE_ELEMENT.equals(localElement2));
          this.preDepth = (1 + this.preDepth);
          return;
        }
        while (!(paramNode instanceof HtmlDocument.EndTag));
        localElement1 = ((HtmlDocument.EndTag)paramNode).getElement();
        if (BLANK_LINE_ELEMENTS.contains(localElement1))
          this.printer.setSeparator(HtmlTree.PlainTextPrinter.Separator.BlankLine);
        while (HTML4.BLOCKQUOTE_ELEMENT.equals(localElement1))
        {
          this.printer.decQuoteDepth();
          return;
          if (localElement1.breaksFlow())
            this.printer.setSeparator(HtmlTree.PlainTextPrinter.Separator.LineBreak);
        }
      }
      while (!HTML4.PRE_ELEMENT.equals(localElement1));
      this.preDepth -= 1;
    }

    public final String getPlainText()
    {
      return this.printer.getText();
    }

    public final int getPlainTextLength()
    {
      return this.printer.getTextLength();
    }
  }

  public static abstract interface PlainTextConverter
  {
    public abstract void addNode(HtmlDocument.Node paramNode, int paramInt1, int paramInt2);

    public abstract String getPlainText();

    public abstract int getPlainTextLength();
  }

  public static abstract interface PlainTextConverterFactory
  {
    public abstract HtmlTree.PlainTextConverter createInstance();
  }

  static final class PlainTextPrinter
  {
    private static final String HTML_SPACE_EQUIVALENTS = " \n\r\t\f";
    private int endingNewLines = 2;
    private int quoteDepth = 0;
    private final StringBuilder sb = new StringBuilder();
    private Separator separator = Separator.None;

    private void appendNewLine()
    {
      maybeAddQuoteMarks(false);
      this.sb.append('\n');
      this.endingNewLines = (1 + this.endingNewLines);
    }

    private void appendTextDirect(String paramString)
    {
      if (paramString.length() == 0)
        return;
      if (paramString.indexOf('\n') < 0);
      for (boolean bool = true; ; bool = false)
      {
        Preconditions.checkArgument(bool, "text must not contain newlines.");
        flushSeparator();
        maybeAddQuoteMarks(true);
        this.sb.append(paramString);
        this.endingNewLines = 0;
        return;
      }
    }

    private void flushSeparator()
    {
      switch (HtmlTree.2.$SwitchMap$com$google$android$gm$common$html$parser$HtmlTree$PlainTextPrinter$Separator[this.separator.ordinal()])
      {
      default:
      case 1:
      case 2:
      case 3:
      }
      while (true)
      {
        this.separator = Separator.None;
        return;
        if (this.endingNewLines == 0)
        {
          this.sb.append(" ");
          continue;
          while (this.endingNewLines < 1)
            appendNewLine();
          while (this.endingNewLines < 2)
            appendNewLine();
        }
      }
    }

    private static boolean isHtmlWhiteSpace(char paramChar)
    {
      return " \n\r\t\f".indexOf(paramChar) >= 0;
    }

    private void maybeAddQuoteMarks(boolean paramBoolean)
    {
      if ((this.endingNewLines > 0) && (this.quoteDepth > 0))
      {
        for (int i = 0; i < this.quoteDepth; i++)
          this.sb.append('>');
        if (paramBoolean)
          this.sb.append(' ');
      }
    }

    final void appendForcedLineBreak()
    {
      flushSeparator();
      appendNewLine();
    }

    final void appendNormalText(String paramString)
    {
      if (paramString.length() == 0);
      boolean bool2;
      do
      {
        return;
        boolean bool1 = isHtmlWhiteSpace(paramString.charAt(0));
        bool2 = isHtmlWhiteSpace(paramString.charAt(paramString.length() - 1));
        String str1 = CharMatcher.anyOf(" \n\r\t\f").trimFrom(paramString);
        String str2 = CharMatcher.anyOf(" \n\r\t\f").collapseFrom(str1, ' ');
        if (bool1)
          setSeparator(Separator.Space);
        appendTextDirect(str2);
      }
      while (!bool2);
      setSeparator(Separator.Space);
    }

    final void appendPreText(String paramString)
    {
      String[] arrayOfString = paramString.split("[\\r\\n]", -1);
      appendTextDirect(arrayOfString[0]);
      for (int i = 1; i < arrayOfString.length; i++)
      {
        appendNewLine();
        appendTextDirect(arrayOfString[i]);
      }
    }

    final void decQuoteDepth()
    {
      this.quoteDepth = Math.max(0, this.quoteDepth - 1);
    }

    final String getText()
    {
      return this.sb.toString();
    }

    final int getTextLength()
    {
      return this.sb.length();
    }

    final void incQuoteDepth()
    {
      this.quoteDepth = (1 + this.quoteDepth);
    }

    final void setSeparator(Separator paramSeparator)
    {
      if (paramSeparator.ordinal() > this.separator.ordinal())
        this.separator = paramSeparator;
    }

    static enum Separator
    {
      static
      {
        LineBreak = new Separator("LineBreak", 2);
        BlankLine = new Separator("BlankLine", 3);
        Separator[] arrayOfSeparator = new Separator[4];
        arrayOfSeparator[0] = None;
        arrayOfSeparator[1] = Space;
        arrayOfSeparator[2] = LineBreak;
        arrayOfSeparator[3] = BlankLine;
      }
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.common.html.parser.HtmlTree
 * JD-Core Version:    0.6.2
 */