package javax.annotation.concurrent;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({java.lang.annotation.ElementType.TYPE})
public @interface ThreadSafe
{
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     javax.annotation.concurrent.ThreadSafe
 * JD-Core Version:    0.6.2
 */