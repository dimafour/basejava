import com.urise.webapp.model.Resume;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainReflection {
    public static void main(String[] args) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Resume r = new Resume();
        Method toString = r.getClass().getDeclaredMethod("toString");
        System.out.println(toString.invoke(r));
    }
}
