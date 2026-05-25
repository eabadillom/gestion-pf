package mx.com.ferbo.util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Deprecated
public class JPAEntity {

	private static final String PERSISTENCE_NAME = "gestionPU";
	private static EntityManagerFactory factory;

	public static EntityManagerFactory getEntity() {
		if (factory == null) {
			factory = Persistence.createEntityManagerFactory(PERSISTENCE_NAME);
		}
		return factory;
	}

	public static void shutdown() {
		if (factory != null) {
			factory.close();
		}
	}

}
