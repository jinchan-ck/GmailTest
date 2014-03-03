package com.google.android.gm.template;

import java.io.IOException;
import java.util.WeakHashMap;

public class Variable extends Expression
{
  private static final WeakHashMap<String, Variable> sPool = new WeakHashMap();
  private final String mName;

  private Variable(String paramString)
  {
    this.mName = paramString;
  }

  public static Object evaluate(EvalContext paramEvalContext, String paramString)
  {
    if (paramEvalContext == null)
      return null;
    return paramEvalContext.get(paramString);
  }

  public static final Variable getVariable(String paramString)
  {
    synchronized (sPool)
    {
      Variable localVariable = (Variable)sPool.get(paramString);
      if (localVariable == null)
      {
        localVariable = new Variable(paramString);
        sPool.put(paramString, localVariable);
      }
      return localVariable;
    }
  }

  public void emitCode(JavaCodeGenerator paramJavaCodeGenerator)
    throws IOException
  {
    JavaCodeGenerator localJavaCodeGenerator = paramJavaCodeGenerator.append("Variable.evaluate(").appendContextVariable();
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = this.mName;
    localJavaCodeGenerator.append(", \"%s\")", arrayOfObject);
  }

  public Object evaluate(EvalContext paramEvalContext)
  {
    return evaluate(paramEvalContext, this.mName);
  }

  public String toString()
  {
    return "$" + this.mName;
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.template.Variable
 * JD-Core Version:    0.6.2
 */