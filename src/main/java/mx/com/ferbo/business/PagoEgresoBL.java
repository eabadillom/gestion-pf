package mx.com.ferbo.business;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@RequestScoped
public class PagoEgresoBL {

    private static final Logger log = LogManager.getLogger(PagoEgresoBL.class);
}
