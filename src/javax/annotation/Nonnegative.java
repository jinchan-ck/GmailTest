package javax.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.meta.TypeQualifier;
import javax.annotation.meta.TypeQualifierValidator;
import javax.annotation.meta.When;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@TypeQualifier(applicableTo="Ljava/lang/Number;")
public @interface Nonnegative
{
  public abstract When when();

  public static class Checker
    implements TypeQualifierValidator<Nonnegative>
  {
    public When forConstantValue(Nonnegative paramNonnegative, Object paramObject)
    {
      if (!(paramObject instanceof Number))
        return When.NEVER;
      Number localNumber = (Number)paramObject;
      if ((localNumber instanceof Long))
      {
        if (localNumber.longValue() < 0L);
        for (i = 1; i != 0; i = 0)
          return When.NEVER;
      }
      if ((localNumber instanceof Double))
      {
        if (localNumber.doubleValue() < 0.0D);
        for (i = 1; ; i = 0)
          break;
      }
      if ((localNumber instanceof Float))
      {
        if (localNumber.floatValue() < 0.0F);
        for (i = 1; ; i = 0)
          break;
      }
      if (localNumber.intValue() < 0);
      for (int i = 1; ; i = 0)
        break;
      return When.ALWAYS;
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     javax.annotation.Nonnegative
 * JD-Core Version:    0.6.2
 */