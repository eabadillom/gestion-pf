
package mx.com.ferbo.util;

import java.time.LocalDate;

enum StatusClass 
{
    QUALIFIED, //Verde
    UNQUALIFIED, //Rojo
    PROPOSAL, //Melon
    NEGOTIATION,//Amarillo
    NEW, //Azul
    RENEWAL, //Morado 
    CANCELLED, // Gris 
    EXPIRED; // Gris
}

enum StatusText 
{
    ENVIADA,
    APROBADA,
    RECHAZADA,
    CANCELADA;
}

public class ManageStatus 
{
    public ManageStatus() 
    {
    }
    
    public String getStatusClass(String statusClass) 
    {
        String mensaje = "";
        
        switch(statusClass)
        {
            case "E":
                mensaje = StatusClass.PROPOSAL.toString().toLowerCase();
                break;
            case "A":
                mensaje = StatusClass.QUALIFIED.toString().toLowerCase();
                break;
            case "R":
                mensaje = StatusClass.UNQUALIFIED.toString().toLowerCase();
                break;
            case "C":
                mensaje = StatusClass.CANCELLED.toString().toLowerCase();
                break;
        }
        return mensaje;
    }
    
    public String getStatusText(String statusText) 
    {
        String mensaje = "";
        switch(statusText)
        {
            case "E":
                mensaje = StatusText.ENVIADA.toString().toLowerCase();
                break;
            case "A":
                mensaje = StatusText.APROBADA.toString().toLowerCase();
                break;
            case "R":
                mensaje = StatusText.RECHAZADA.toString().toLowerCase();
                break;
            case "C":
                mensaje = StatusText.CANCELADA.toString().toLowerCase();
                break;
        }
        return mensaje;
    }

     public String getStatusClass(String statusClass, LocalDate fecha)   
    {
        String mensaje = "";
        
        switch(statusClass)
        {
            case "E":
                LocalDate hoy = LocalDate.now();
                if (fecha.isBefore(hoy)) {
                    mensaje = StatusClass.EXPIRED.toString().toLowerCase();
                } else {
                    mensaje = StatusClass.PROPOSAL.toString().toLowerCase();
                }
                break;
            case "A":
                mensaje = StatusClass.QUALIFIED.toString().toLowerCase();
                break;
            case "R":
                mensaje = StatusClass.UNQUALIFIED.toString().toLowerCase();
                break;
            case "C":
                mensaje = StatusClass.CANCELLED.toString().toLowerCase();
                break;
        }
        return mensaje;
    }
    
}

