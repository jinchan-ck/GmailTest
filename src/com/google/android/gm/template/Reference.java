package com.google.android.gm.template;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Reference extends Expression
{
  private final ArrayList<String> mKeys;

  public Reference(ArrayList<String> paramArrayList)
  {
    this.mKeys = paramArrayList;
  }

  public static Object evaluate(EvalContext paramEvalContext, String paramString1, String paramString2)
  {
    if (paramEvalContext == null)
      return null;
    Object localObject = paramEvalContext.get(paramString1);
    if ((localObject instanceof Map))
      return ((Map)localObject).get(paramString2);
    throw new RuntimeException("Invalid reference: \"" + paramString1 + "\" is not a map");
  }

  public static Object evaluate(EvalContext paramEvalContext, String[] paramArrayOfString)
  {
    if (paramEvalContext == null)
      return null;
    String str = paramArrayOfString[0];
    Object localObject = paramEvalContext.get(str);
    int i = 1;
    int j = paramArrayOfString.length;
    while (i < j)
      if ((localObject instanceof Map))
      {
        str = paramArrayOfString[i];
        localObject = ((Map)localObject).get(str);
        i++;
      }
      else
      {
        throw new RuntimeException("Invalid reference: \"" + str + "\" is not a map");
      }
    return localObject;
  }

  public void emitCode(JavaCodeGenerator paramJavaCodeGenerator)
    throws IOException
  {
    paramJavaCodeGenerator.append("Reference.evaluate(").appendContextVariable().append(", ");
    if (this.mKeys.size() == 2)
    {
      Object[] arrayOfObject2 = new Object[2];
      arrayOfObject2[0] = this.mKeys.get(0);
      arrayOfObject2[1] = this.mKeys.get(1);
      paramJavaCodeGenerator.append("\"%s\", \"%s\"", arrayOfObject2);
    }
    while (true)
    {
      paramJavaCodeGenerator.append(")");
      return;
      paramJavaCodeGenerator.append("new String[]{");
      int i = 0;
      int j = this.mKeys.size();
      while (i < j)
      {
        if (i > 0)
          paramJavaCodeGenerator.append(",");
        Object[] arrayOfObject1 = new Object[1];
        arrayOfObject1[0] = this.mKeys.get(i);
        paramJavaCodeGenerator.append("\"%s\"", arrayOfObject1);
        i++;
      }
      paramJavaCodeGenerator.append("}");
    }
  }

  public Object evaluate(EvalContext paramEvalContext)
  {
    return evaluate(paramEvalContext, (String[])this.mKeys.toArray(new String[0]));
  }

  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    int i = 0;
    int j = this.mKeys.size();
    while (i < j)
    {
      if (i != 0)
        localStringBuilder.append('.');
      localStringBuilder.append((String)this.mKeys.get(i));
      i++;
    }
    return "$" + localStringBuilder.toString();
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.template.Reference
 * JD-Core Version:    0.6.2
 */