
package mx.com.ferbo.util;

import java.util.HashSet;
import java.util.Arrays;
import java.util.Set;

public class SetUtils {
    public static <T> Set<T> setOf(T... values) {
        return new HashSet<T>(Arrays.asList(values));
    }
}