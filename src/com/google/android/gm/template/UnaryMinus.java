package com.google.android.gm.template;

import java.io.IOException;

public class UnaryMinus extends Expression
{
  private final Expression mExpression;

  public UnaryMinus(Expression paramExpression)
  {
    this.mExpression = paramExpression;
  }

  public void emitCode(JavaCodeGenerator paramJavaCodeGenerator)
    throws IOException
  {
    paramJavaCodeGenerator.append("UnaryMinus.negate(").append(this.mExpression).appendLine(")");
  }

  public Object evaluate(EvalContext paramEvalContext)
  {
    return negate(this.mExpression.evaluate(paramEvalContext));
  }

  public Object negate(Object paramObject)
  {
    if ((paramObject instanceof Long))
      return Long.valueOf(-((Long)paramObject).longValue());
    if ((paramObject instanceof Double))
      return Double.valueOf(-((Double)paramObject).doubleValue());
    throw new RuntimeException("Cannot negate value of " + paramObject);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.template.UnaryMinus
 * JD-Core Version:    0.6.2
 */