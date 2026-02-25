
package mx.com.ferbo.business.egresos.util;

import java.util.HashSet;
import java.util.Arrays;
import java.util.Set;

public class SetUtils {
    public static Set<String> s(String... values) {
        return new HashSet<String>(Arrays.asList(values));
    }
}