package mx.com.ferbo.util;

import javax.persistence.*;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
public class EntityManagerUtil {

	protected static String PERSIST_UNIT = "gestionPU";
    protected static EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSIST_UNIT);

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
