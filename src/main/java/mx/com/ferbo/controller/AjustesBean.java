package mx.com.ferbo.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
@Named(value = "ajustesBean")
@ViewScoped
public class AjustesBean implements Serializable 
{
    private static final long serialVersionUID = 572901091404696084L;
    private static Logger log = LogManager.getLogger(AjustesBean.class);
    
    private String contextPath = null;

    public AjustesBean() {
    }
    
    @PostConstruct
    public void init() {
        contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
    }
    
    public void domicilios(){
        String path = null;
        try{
            path = contextPath + "/protected/settings/sistema/domicilios.xhtml";
            FacesContext.getCurrentInstance().getExternalContext().redirect(path);
        }
        catch(Exception e) {
            e.getMessage();
        }
    }
    
}
