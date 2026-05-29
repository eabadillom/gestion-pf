package mx.com.ferbo.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author alberto
 */
@FacesConverter("monthYearConverter")
public class MonthYearConverter implements Converter<Date> 
{

    @Override
    public String getAsString(FacesContext context, UIComponent component, Date value) {
        if (value == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", new Locale("es"));
        String text = sdf.format(value);
        // Primera letra en mayúscula
        return text.substring(0,1).toUpperCase() + text.substring(1);
    }

    @Override
    public Date getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", new Locale("es"));
            return sdf.parse(value);
        } catch (Exception e) {
            return null;
        }
    }
}
