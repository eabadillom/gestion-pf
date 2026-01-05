package mx.com.ferbo.dao.n;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.Contacto;

public class ContactoDAO  extends BaseDAO<Contacto, Integer>{

    public ContactoDAO() {
        super(Contacto.class);
    }
    
}
