package com.google.android.gm.template;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

public class Parser
{
  private Token mCurrentToken;
  private final Tokenizer mTokenizer;

  public Parser(Reader paramReader)
  {
    this.mTokenizer = new Tokenizer(paramReader);
    readNextToken();
  }

  private void checkExpectedToken(Token.Type paramType, String paramString)
  {
    if (!checkOptionalToken(paramType, paramString))
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Expected ").append(Token.getToken(paramType, paramString)).append(", found ");
      if (this.mCurrentToken == null);
      for (Object localObject = "end of file"; ; localObject = this.mCurrentToken)
        throw new SyntaxError(localObject, this.mTokenizer.getLine(), this.mTokenizer.getColumn());
    }
  }

  private boolean checkOptionalToken(Token.Type paramType, String paramString)
  {
    return (this.mCurrentToken != null) && (this.mCurrentToken.mType.equals(paramType)) && (this.mCurrentToken.mValue.equals(paramString));
  }

  private String peekExpectedToken(Token.Type paramType)
  {
    String str = peekOptionalToken(paramType);
    if (str == null)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Expected ").append(paramType).append(", found ");
      if (this.mCurrentToken == null);
      for (Object localObject = "end of file"; ; localObject = this.mCurrentToken)
        throw new SyntaxError(localObject, this.mTokenizer.getLine(), this.mTokenizer.getColumn());
    }
    return str;
  }

  private String peekOptionalToken(Token.Type paramType)
  {
    if ((this.mCurrentToken != null) && (this.mCurrentToken.mType.equals(paramType)))
      return this.mCurrentToken.mValue;
    return null;
  }

  private String readExpectedToken(Token.Type paramType)
  {
    String str = readOptionalToken(paramType);
    if (str == null)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Expected ").append(paramType).append(", found ");
      if (this.mCurrentToken == null);
      for (Object localObject = "end of file"; ; localObject = this.mCurrentToken)
        throw new SyntaxError(localObject, this.mTokenizer.getLine(), this.mTokenizer.getColumn());
    }
    return str;
  }

  private void readNextToken()
  {
    this.mCurrentToken = this.mTokenizer.nextToken();
  }

  private String readOptionalToken(Token.Type paramType)
  {
    String str = peekOptionalToken(paramType);
    if (str != null)
      readNextToken();
    return str;
  }

  private void skipExpectedToken(Token.Type paramType, String paramString)
  {
    if (!skipOptionalToken(paramType, paramString))
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Expected ").append(Token.getToken(paramType, paramString)).append(", found ");
      if (this.mCurrentToken == null);
      for (Object localObject = "end of file"; ; localObject = this.mCurrentToken)
        throw new SyntaxError(localObject, this.mTokenizer.getLine(), this.mTokenizer.getColumn());
    }
  }

  private boolean skipOptionalToken(Token.Type paramType, String paramString)
  {
    boolean bool = checkOptionalToken(paramType, paramString);
    if (bool)
      readNextToken();
    return bool;
  }

  public Template parse()
  {
    if (this.mCurrentToken == null)
      return null;
    skipExpectedToken(Token.Type.SYMBOL, "{");
    skipExpectedToken(Token.Type.WORD, "template");
    checkExpectedToken(Token.Type.SYMBOL, "}");
    return parseTemplateCommand();
  }

  Expression parseAtomicExpression()
  {
    if (this.mCurrentToken == null)
      throw new SyntaxError("Expression expected", this.mTokenizer.getLine(), this.mTokenizer.getColumn());
    String str = this.mCurrentToken.mValue;
    Object localObject;
    switch (1.$SwitchMap$com$google$android$gm$template$Token$Type[this.mCurrentToken.mType.ordinal()])
    {
    default:
      throw new SyntaxError("Unexpected token: " + this.mCurrentToken, this.mTokenizer.getLine(), this.mTokenizer.getColumn());
    case 1:
      try
      {
        if (str.startsWith("0x"))
          localObject = Constant.getConstant(Long.parseLong(str.substring(2), 16));
        while (true)
        {
          readNextToken();
          break;
          if (str.indexOf('.') != -1)
          {
            localObject = Constant.getConstant(Double.parseDouble(str));
          }
          else
          {
            Constant localConstant = Constant.getConstant(Long.parseLong(str, 10));
            localObject = localConstant;
          }
        }
      }
      catch (NumberFormatException localNumberFormatException)
      {
        throw new SyntaxError("Expected number, found \"" + str + "\"", this.mTokenizer.getLine(), this.mTokenizer.getColumn());
      }
    case 2:
      localObject = Constant.getConstant(str);
      readNextToken();
      break;
    case 3:
      if ("true".equals(str))
        localObject = Constant.TRUE;
      while (true)
      {
        readNextToken();
        break label326;
        if ("false".equals(str))
        {
          localObject = Constant.FALSE;
        }
        else
        {
          if (!"null".equals(str))
            break;
          localObject = Constant.NULL;
        }
      }
      return parseFunctionExpression();
    case 4:
      skipExpectedToken(Token.Type.SYMBOL, "$");
      localObject = parseReferenceExpression();
    }
    label326: return localObject;
  }

  Call parseCallCommand()
  {
    skipExpectedToken(Token.Type.WORD, "call");
    String str1 = readExpectedToken(Token.Type.WORD);
    skipExpectedToken(Token.Type.WORD, "data");
    skipExpectedToken(Token.Type.SYMBOL, "=");
    String str2 = readExpectedToken(Token.Type.STRING);
    checkExpectedToken(Token.Type.SYMBOL, "}");
    if ("all".equals(str2));
    for (Expression localExpression = null; ; localExpression = new Parser(new StringReader(str2.substring(1))).parseReferenceExpression())
      return new Call(str1, localExpression);
  }

  Command parseCommand()
  {
    skipExpectedToken(Token.Type.SYMBOL, "{");
    if (skipOptionalToken(Token.Type.SYMBOL, "/"))
      return parseEndCommand();
    String str = peekOptionalToken(Token.Type.WORD);
    if ("lb".equals(str))
    {
      readNextToken();
      checkExpectedToken(Token.Type.SYMBOL, "}");
      return Literal.LB;
    }
    if ("rb".equals(str))
    {
      readNextToken();
      checkExpectedToken(Token.Type.SYMBOL, "}");
      return Literal.RB;
    }
    if ("literal".equals(str))
    {
      readNextToken();
      checkExpectedToken(Token.Type.SYMBOL, "}");
      return parseLiteralCommand();
    }
    if ("template".equals(str))
    {
      readNextToken();
      checkExpectedToken(Token.Type.SYMBOL, "}");
      return parseTemplateCommand();
    }
    if ("if".equals(str))
      return parseIfCommand();
    if ("elseif".equals(str))
      return parseElseIfCommand();
    if ("else".equals(str))
      return parseElseCommand();
    if ("call".equals(str))
      return parseCallCommand();
    if ("foreach".equals(str))
      return parseForEachCommand();
    return parsePrintCommand();
  }

  Expression parseComparisonExpression()
  {
    Expression localExpression = parseUnaryExpression();
    if (this.mCurrentToken == null)
      return localExpression;
    String str = peekExpectedToken(Token.Type.SYMBOL);
    if (("?".equals(str)) || (":".equals(str)) || ("|".equals(str)) || ("}".equals(str)))
      return localExpression;
    if (("==".equals(str)) || ("!=".equals(str)))
    {
      skipExpectedToken(Token.Type.SYMBOL, str);
      if ("==".equals(str));
      for (Comparison.Type localType = Comparison.Type.EQUALS; ; localType = Comparison.Type.NOT_EQUALS)
        return new Comparison(localType, localExpression, parseUnaryExpression());
    }
    throw new SyntaxError("Unexpected symbol: \"" + str + "\"", this.mTokenizer.getLine(), this.mTokenizer.getColumn());
  }

  Else parseElseCommand()
  {
    skipExpectedToken(Token.Type.WORD, "else");
    checkExpectedToken(Token.Type.SYMBOL, "}");
    return new Else();
  }

  ElseIf parseElseIfCommand()
  {
    skipExpectedToken(Token.Type.WORD, "elseif");
    Expression localExpression = parseExpression();
    checkExpectedToken(Token.Type.SYMBOL, "}");
    return new ElseIf(localExpression);
  }

  End parseEndCommand()
  {
    String str = readExpectedToken(Token.Type.WORD);
    checkExpectedToken(Token.Type.SYMBOL, "}");
    return new End(str);
  }

  Expression parseExpression()
  {
    return parseTernaryExpression();
  }

  ForEach parseForEachCommand()
  {
    skipExpectedToken(Token.Type.WORD, "foreach");
    skipExpectedToken(Token.Type.SYMBOL, "$");
    String str1 = readExpectedToken(Token.Type.WORD);
    skipExpectedToken(Token.Type.WORD, "in");
    Expression localExpression = parseExpression();
    checkExpectedToken(Token.Type.SYMBOL, "}");
    ArrayList localArrayList = new ArrayList();
    Command localCommand;
    do
    {
      if (!checkOptionalToken(Token.Type.SYMBOL, "{"))
      {
        String str2 = this.mTokenizer.nextFragment();
        readNextToken();
        if ((str2 != null) && (!"".equals(str2)))
          localArrayList.add(new Fragment(str2));
      }
      localCommand = parseCommand();
      if (localCommand != null)
      {
        if (((localCommand instanceof End)) && ("foreach".equals(((End)localCommand).getCommandName())))
          return new ForEach(str1, localExpression, localArrayList);
        localArrayList.add(localCommand);
      }
    }
    while (localCommand != null);
    throw new SyntaxError("Unterminated foreach command", this.mTokenizer.getLine(), this.mTokenizer.getColumn());
  }

  Expression parseFunctionExpression()
  {
    String str1 = readExpectedToken(Token.Type.WORD);
    skipExpectedToken(Token.Type.SYMBOL, "(");
    skipExpectedToken(Token.Type.SYMBOL, "$");
    String str2 = readExpectedToken(Token.Type.WORD);
    skipExpectedToken(Token.Type.SYMBOL, ")");
    try
    {
      Function localFunction = new Function(str1, str2);
      return localFunction;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      throw new SyntaxError(localIllegalArgumentException.getMessage(), this.mTokenizer.getLine(), this.mTokenizer.getColumn());
    }
  }

  If parseIfCommand()
  {
    skipExpectedToken(Token.Type.WORD, "if");
    Expression localExpression = parseExpression();
    checkExpectedToken(Token.Type.SYMBOL, "}");
    If.Builder localBuilder = new If.Builder().addCondition(localExpression);
    while (true)
    {
      if (!checkOptionalToken(Token.Type.SYMBOL, "{"))
      {
        String str = this.mTokenizer.nextFragment();
        readNextToken();
        if ((str != null) && (!"".equals(str)))
          localBuilder.addNode(new Fragment(str));
      }
      Command localCommand = parseCommand();
      if (localCommand != null)
      {
        if (!(localCommand instanceof ElseIf))
          break label148;
        localBuilder.addCondition(((ElseIf)localCommand).getCondition());
      }
      while (localCommand == null)
      {
        throw new SyntaxError("Unterminated if command", this.mTokenizer.getLine(), this.mTokenizer.getColumn());
        label148: if ((localCommand instanceof Else))
        {
          localBuilder.addDefault();
        }
        else
        {
          if (((localCommand instanceof End)) && ("if".equals(((End)localCommand).getCommandName())))
            return localBuilder.build();
          localBuilder.addNode(localCommand);
        }
      }
    }
  }

  Literal parseLiteralCommand()
  {
    return new Literal(new Fragment(this.mTokenizer.nextLiteralFragment()));
  }

  Print parsePrintCommand()
  {
    skipOptionalToken(Token.Type.WORD, "print");
    Expression localExpression = parseExpression();
    boolean bool = true;
    String str;
    if (skipOptionalToken(Token.Type.SYMBOL, "|"))
    {
      str = readExpectedToken(Token.Type.WORD);
      if (("id".equals(str)) || ("noAutoescape".equals(str)))
        bool = false;
    }
    else
    {
      checkExpectedToken(Token.Type.SYMBOL, "}");
      return new Print(localExpression, bool);
    }
    throw new SyntaxError("Unknown flag: " + str, this.mTokenizer.getLine(), this.mTokenizer.getColumn());
  }

  Expression parseReferenceExpression()
  {
    String str = readExpectedToken(Token.Type.WORD);
    if (skipOptionalToken(Token.Type.SYMBOL, "."))
    {
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(str);
      do
        localArrayList.add(readExpectedToken(Token.Type.WORD));
      while (skipOptionalToken(Token.Type.SYMBOL, "."));
      return new Reference(localArrayList);
    }
    return Variable.getVariable(str);
  }

  Template parseTemplateCommand()
  {
    ArrayList localArrayList = new ArrayList();
    Command localCommand;
    do
    {
      if (!checkOptionalToken(Token.Type.SYMBOL, "{"))
      {
        String str = this.mTokenizer.nextFragment();
        readNextToken();
        if ((str != null) && (!"".equals(str)))
          localArrayList.add(new Fragment(str));
      }
      localCommand = parseCommand();
      if (localCommand != null)
      {
        if (((localCommand instanceof End)) && ("template".equals(((End)localCommand).getCommandName())))
          return new Template(localArrayList);
        localArrayList.add(localCommand);
      }
    }
    while (localCommand != null);
    throw new SyntaxError("Unterminated template", this.mTokenizer.getLine(), this.mTokenizer.getColumn());
  }

  Expression parseTernaryExpression()
  {
    Expression localExpression1 = parseComparisonExpression();
    if (this.mCurrentToken == null)
      return localExpression1;
    String str = peekExpectedToken(Token.Type.SYMBOL);
    if (("|".equals(str)) || ("}".equals(str)))
      return localExpression1;
    skipExpectedToken(Token.Type.SYMBOL, "?");
    Expression localExpression2 = parseComparisonExpression();
    skipExpectedToken(Token.Type.SYMBOL, ":");
    return new Ternary(localExpression1, localExpression2, parseComparisonExpression());
  }

  Expression parseUnaryExpression()
  {
    if (skipOptionalToken(Token.Type.SYMBOL, "-"))
      return new UnaryMinus(parseAtomicExpression());
    if (skipOptionalToken(Token.Type.SYMBOL, "!"))
      return new UnaryNot(parseAtomicExpression());
    return parseAtomicExpression();
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.template.Parser
 * JD-Core Version:    0.6.2
 */