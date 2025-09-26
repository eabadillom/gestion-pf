package mx.com.ferbo.dao.n;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.MedioCnt;

@Named
@ApplicationScoped
public class MedioCntDAO extends BaseDAO<MedioCnt, Integer>{

    public MedioCntDAO(){
        super(MedioCnt.class);
    }

}
