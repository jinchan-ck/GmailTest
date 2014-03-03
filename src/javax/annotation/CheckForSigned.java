package javax.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.meta.TypeQualifierNickname;
import javax.annotation.meta.When;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Nonnegative(when=When.MAYBE)
@TypeQualifierNickname
public @interface CheckForSigned
{
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     javax.annotation.CheckForSigned
 * JD-Core Version:    0.6.2
 */