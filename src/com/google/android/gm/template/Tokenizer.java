package com.google.android.gm.template;

import java.io.IOException;
import java.io.Reader;

public class Tokenizer
{
  static final int COMMAND_END = 125;
  static final int COMMAND_START = 123;
  static final String END_LITERAL = "{/literal}";
  static final int END_LITERAL_LENGTH = 0;
  static final int MAX_LENGTH = 1024;
  private final char[] mBuffer = new char[1024];
  private int mColumn = 0;
  private int mLength = 0;
  private int mLine = 1;
  private final Reader mReader;
  private int mUnreadChar = -1;

  public Tokenizer(Reader paramReader)
  {
    this.mReader = paramReader;
  }

  private String flush()
  {
    if (this.mLength != 0)
    {
      String str = String.valueOf(this.mBuffer, 0, this.mLength);
      this.mLength = 0;
      return str;
    }
    return "";
  }

  private void flushTo(StringBuilder paramStringBuilder)
  {
    paramStringBuilder.append(this.mBuffer, 0, this.mLength);
    this.mLength = 0;
  }

  private int read()
  {
    if (this.mUnreadChar == -1)
      try
      {
        int j = this.mReader.read();
        if (j == 10)
        {
          this.mLine = (1 + this.mLine);
          this.mColumn = 0;
          return j;
        }
        this.mColumn = (1 + this.mColumn);
        return j;
      }
      catch (IOException localIOException)
      {
        throw new RuntimeException("Cannot read from input stream: " + localIOException);
      }
    int i = this.mUnreadChar;
    this.mUnreadChar = -1;
    return i;
  }

  private Token readNumber(int paramInt)
  {
    if (paramInt == 48)
    {
      store(paramInt);
      paramInt = read();
      if (paramInt == 120)
      {
        do
        {
          store(paramInt);
          paramInt = read();
        }
        while (((97 <= paramInt) && (paramInt <= 102)) || ((65 <= paramInt) && (paramInt <= 70)) || (Character.isDigit(paramInt)));
        unread(paramInt);
        return Token.getToken(Token.Type.NUMBER, flush());
      }
      if (Character.isDigit(paramInt))
        throw new SyntaxError("Leading zero before " + (char)paramInt, this.mLine, this.mColumn);
    }
    if ((paramInt == 46) || (Character.isDigit(paramInt)))
      do
      {
        store(paramInt);
        paramInt = read();
      }
      while ((paramInt == 46) || (Character.isDigit(paramInt)));
    unread(paramInt);
    return Token.getToken(Token.Type.NUMBER, flush());
  }

  private Token readString(int paramInt)
  {
    while (true)
    {
      int i = read();
      if (i == paramInt)
        return Token.getToken(Token.Type.STRING, flush());
      if (i == 92)
      {
        int j = read();
        switch (j)
        {
        default:
          throw new SyntaxError("Invalid escape sequence: '\\" + (char)j + "'", this.mLine, this.mColumn);
        case 34:
        case 39:
        case 92:
          store(j);
          break;
        case 110:
          store(10);
          break;
        case 116:
          store(9);
          break;
        case 98:
          store(8);
          break;
        case 114:
          store(13);
          break;
        case 102:
        }
        store(12);
      }
      else
      {
        if (i == -1)
          throw new SyntaxError("Unterminated string: " + (char)paramInt + flush(), this.mLine, this.mColumn);
        store(i);
      }
    }
  }

  private Token readSymbol(int paramInt)
  {
    StringBuilder localStringBuilder;
    switch (paramInt)
    {
    default:
      localStringBuilder = new StringBuilder().append("Unexpected ");
      if (paramInt != -1)
        break;
    case 36:
    case 40:
    case 41:
    case 45:
    case 46:
    case 47:
    case 58:
    case 63:
    case 123:
    case 124:
    case 125:
    case 33:
    case 61:
    }
    for (String str = "end of file"; ; str = "character '" + (char)paramInt + "'")
    {
      throw new SyntaxError(str, this.mLine, this.mColumn);
      store(paramInt);
      return Token.getToken(Token.Type.SYMBOL, flush());
      store(paramInt);
      int i = read();
      if (i == 61)
        store(i);
      while (true)
      {
        return Token.getToken(Token.Type.SYMBOL, flush());
        unread(i);
      }
    }
  }

  private Token readWord(int paramInt)
  {
    do
    {
      store(paramInt);
      paramInt = read();
    }
    while ((paramInt == 95) || (Character.isLetterOrDigit(paramInt)));
    unread(paramInt);
    return Token.getToken(Token.Type.WORD, flush());
  }

  private void store(int paramInt)
  {
    if (paramInt == -1)
      throw new IllegalStateException("Cannot store end-of-file character");
    if (this.mLength == 1024)
      throw new SyntaxError("Token too long: \"" + flush() + "\"", this.mLine, this.mColumn);
    char[] arrayOfChar = this.mBuffer;
    int i = this.mLength;
    this.mLength = (i + 1);
    arrayOfChar[i] = ((char)paramInt);
  }

  private void unread(int paramInt)
  {
    if (this.mUnreadChar != -1)
      throw new IllegalStateException("Cannot unread more than one character in a row");
    this.mUnreadChar = paramInt;
  }

  public final int getColumn()
  {
    return this.mColumn;
  }

  public final int getLine()
  {
    return this.mLine;
  }

  public String nextFragment()
  {
    if (this.mLength != 0)
      throw new RuntimeException("nextFragment: buffer not flushed: \"" + flush() + "\"");
    StringBuilder localStringBuilder = null;
    while (true)
    {
      int i = read();
      if ((i != -1) && (i != 123))
      {
        if (this.mLength == 1023)
        {
          if (localStringBuilder == null)
            localStringBuilder = new StringBuilder();
          flushTo(localStringBuilder);
        }
        if (!Character.isWhitespace(i))
          break label162;
        if (i != 10)
          break label156;
      }
      label156: for (int j = 10; ; j = 32)
      {
        store(j);
        do
          i = read();
        while ((i != -1) && (Character.isWhitespace(i)));
        if ((i != -1) && (i != 123))
          break;
        if (i == 123)
          unread(i);
        if (localStringBuilder != null)
          break label170;
        return flush();
      }
      label162: store(i);
    }
    label170: flushTo(localStringBuilder);
    return localStringBuilder.toString();
  }

  public String nextLiteralFragment()
  {
    if (this.mLength != 0)
      throw new RuntimeException("nextLiteralFragment: buffer not flushed: \"" + flush() + "\"");
    StringBuilder localStringBuilder = new StringBuilder();
    int i;
    int j;
    do
    {
      do
      {
        i = read();
        if (i == -1)
          break;
        localStringBuilder.append((char)i);
      }
      while (i != 125);
      j = localStringBuilder.length();
    }
    while ((j < END_LITERAL_LENGTH) || (!"{/literal}".equals(localStringBuilder.substring(j - END_LITERAL_LENGTH))));
    if (i == -1)
      throw new SyntaxError("Unterminated literal fragment: \"" + localStringBuilder.toString() + "\"", this.mLine, this.mColumn);
    return localStringBuilder.substring(0, localStringBuilder.length() - END_LITERAL_LENGTH);
  }

  public Token nextToken()
  {
    if (this.mLength != 0)
      throw new RuntimeException("nextToken: buffer not flushed: \"" + flush() + "\"");
    int i;
    do
      i = read();
    while (Character.isWhitespace(i));
    if (i == -1)
      return null;
    if ((i == 95) || (Character.isLetter(i)))
      return readWord(i);
    if (Character.isDigit(i))
      return readNumber(i);
    if ((i == 34) || (i == 39))
      return readString(i);
    return readSymbol(i);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.template.Tokenizer
 * JD-Core Version:    0.6.2
 */