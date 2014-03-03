package javax.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.meta.TypeQualifier;
import javax.annotation.meta.TypeQualifierValidator;
import javax.annotation.meta.When;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@TypeQualifier(applicableTo="Ljava/lang/String;")
public @interface MatchesPattern
{
  public abstract int flags();

  @RegEx
  public abstract String value();

  public static class Checker
    implements TypeQualifierValidator<MatchesPattern>
  {
    public When forConstantValue(MatchesPattern paramMatchesPattern, Object paramObject)
    {
      if (Pattern.compile(paramMatchesPattern.value(), paramMatchesPattern.flags()).matcher((String)paramObject).matches())
        return When.ALWAYS;
      return When.NEVER;
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     javax.annotation.MatchesPattern
 * JD-Core Version:    0.6.2
 */