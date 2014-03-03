package com.google.android.gm.template;

import java.io.IOException;

public class Function extends Expression
{
  private final String mFunctionName;
  private final String mIndexKey;
  private final String mLastIndexKey;

  public Function(String paramString1, String paramString2)
  {
    if ((!"isFirst".equals(paramString1)) && (!"isLast".equals(paramString1)) && (!"index".equals(paramString1)))
      throw new IllegalArgumentException("Unknown function: \"" + paramString1 + "\"");
    this.mFunctionName = paramString1;
    this.mIndexKey = (paramString2 + "$index");
    this.mLastIndexKey = (paramString2 + "$lastIndex");
  }

  public static int getIntValue(EvalContext paramEvalContext, String paramString)
  {
    Object localObject = paramEvalContext.get(paramString);
    if ((localObject != null) && ((localObject instanceof Number)))
      return ((Number)localObject).intValue();
    throw new RuntimeException("\"" + paramString + "\" not found or not a number");
  }

  public void emitCode(JavaCodeGenerator paramJavaCodeGenerator)
    throws IOException
  {
    JavaCodeGenerator localJavaCodeGenerator1 = paramJavaCodeGenerator.append("(Function.getIntValue(").appendContextVariable();
    Object[] arrayOfObject1 = new Object[1];
    arrayOfObject1[0] = this.mIndexKey;
    localJavaCodeGenerator1.append(",\"%s\")", arrayOfObject1);
    if ("isFirst".equals(this.mFunctionName))
      paramJavaCodeGenerator.appendLine("==0)");
    do
    {
      return;
      if ("isLast".equals(this.mFunctionName))
      {
        JavaCodeGenerator localJavaCodeGenerator2 = paramJavaCodeGenerator.append("==Function.getIntValue(").appendContextVariable();
        Object[] arrayOfObject2 = new Object[1];
        arrayOfObject2[0] = this.mLastIndexKey;
        localJavaCodeGenerator2.append(",\"%s\")", arrayOfObject2).appendLine(")");
        return;
      }
    }
    while (!"index".equals(this.mFunctionName));
    paramJavaCodeGenerator.appendLine(")");
  }

  public Object evaluate(EvalContext paramEvalContext)
  {
    if (paramEvalContext == null)
      return null;
    int i = getIntValue(paramEvalContext, this.mIndexKey);
    if ("isFirst".equals(this.mFunctionName))
    {
      if (i == 0);
      for (boolean bool2 = true; ; bool2 = false)
        return Boolean.valueOf(bool2);
    }
    if ("isLast".equals(this.mFunctionName))
    {
      if (i == getIntValue(paramEvalContext, this.mLastIndexKey));
      for (boolean bool1 = true; ; bool1 = false)
        return Boolean.valueOf(bool1);
    }
    if ("index".equals(this.mFunctionName))
      return Integer.valueOf(i);
    throw new RuntimeException("Unknown function: \"" + this.mFunctionName + "\"");
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.template.Function
 * JD-Core Version:    0.6.2
 */