package javax.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.annotation.meta.TypeQualifierNickname;
import javax.annotation.meta.TypeQualifierValidator;
import javax.annotation.meta.When;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Syntax("RegEx")
@TypeQualifierNickname
public @interface RegEx
{
  public abstract When when();

  public static class Checker
    implements TypeQualifierValidator<RegEx>
  {
    public When forConstantValue(RegEx paramRegEx, Object paramObject)
    {
      if (!(paramObject instanceof String))
        return When.NEVER;
      try
      {
        Pattern.compile((String)paramObject);
        return When.ALWAYS;
      }
      catch (PatternSyntaxException localPatternSyntaxException)
      {
      }
      return When.NEVER;
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     javax.annotation.RegEx
 * JD-Core Version:    0.6.2
 */