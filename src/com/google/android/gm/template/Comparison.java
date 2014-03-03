package com.google.android.gm.template;

import java.io.IOException;

public class Comparison extends Expression
{
  private final Expression mExpression1;
  private final Expression mExpression2;
  private final Type mType;

  public Comparison(Type paramType, Expression paramExpression1, Expression paramExpression2)
  {
    this.mType = paramType;
    this.mExpression1 = paramExpression1;
    this.mExpression2 = paramExpression2;
  }

  public static boolean isEqual(Object paramObject1, Object paramObject2)
  {
    if (((paramObject1 instanceof Number)) && ((paramObject2 instanceof Number)))
    {
      Number localNumber1 = (Number)paramObject1;
      Number localNumber2 = (Number)paramObject2;
      if ((isInteger(localNumber1)) && (isInteger(localNumber2)))
        return localNumber1.longValue() == localNumber2.longValue();
      return localNumber1.doubleValue() == localNumber2.doubleValue();
    }
    return (paramObject1 == paramObject2) || ((paramObject1 != null) && (paramObject1.equals(paramObject2)));
  }

  static boolean isInteger(Number paramNumber)
  {
    return ((paramNumber instanceof Long)) || ((paramNumber instanceof Integer)) || ((paramNumber instanceof Short)) || ((paramNumber instanceof Byte));
  }

  public void emitCode(JavaCodeGenerator paramJavaCodeGenerator)
    throws IOException
  {
    if (this.mType == Type.NOT_EQUALS)
      paramJavaCodeGenerator.append("!");
    paramJavaCodeGenerator.append("Comparison.isEqual(").append(this.mExpression1).append(",").append(this.mExpression2).append(")");
  }

  public Object evaluate(EvalContext paramEvalContext)
  {
    boolean bool1 = isEqual(this.mExpression1.evaluate(paramEvalContext), this.mExpression2.evaluate(paramEvalContext));
    boolean bool2;
    if (this.mType == Type.EQUALS)
      bool2 = bool1;
    while (true)
    {
      return Boolean.valueOf(bool2);
      if (!bool1)
        bool2 = true;
      else
        bool2 = false;
    }
  }

  public static enum Type
  {
    static
    {
      Type[] arrayOfType = new Type[2];
      arrayOfType[0] = EQUALS;
      arrayOfType[1] = NOT_EQUALS;
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.template.Comparison
 * JD-Core Version:    0.6.2
 */