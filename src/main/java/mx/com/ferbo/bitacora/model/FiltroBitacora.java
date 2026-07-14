package mx.com.ferbo.bitacora.model;

import java.time.LocalDate;

import com.ferbo.tools.exception.BusinessException;

import mx.com.ferbo.bitacora.enums.TipoPantalla;
import mx.com.ferbo.bitacora.enums.NombrePantalla;

public class FiltroBitacora {

    private String documento;
    
    private Integer idUsuario;

    private LocalDate inicio;

    private LocalDate fin;

    private NombrePantalla nombrePantalla;

    private TipoPantalla tipoPantalla;

    public FiltroBitacora() {
    }

    public FiltroBitacora(String documento, Integer idUsuario, LocalDate inicio, LocalDate fin,
            NombrePantalla nombrePantalla, TipoPantalla tipoPantalla) {
        this.documento = documento;
        this.idUsuario = idUsuario;
        this.inicio = inicio;
        this.fin = fin;
        this.nombrePantalla = nombrePantalla;
        this.tipoPantalla = tipoPantalla;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        if ("".equalsIgnoreCase(documento)){
            throw new BusinessException("No se puede filtrar por un documento vacío");
        }
        this.documento = documento;
    }

    public LocalDate getInicio() {
        return inicio;
    }

    public void setInicio(LocalDate inicio) {
        this.inicio = inicio;
    }

    public LocalDate getFin() {
        return fin;
    }

    public void setFin(LocalDate fin) {
        this.fin = fin;
    }

    public NombrePantalla getNombrePantalla() {
        return nombrePantalla;
    }

    public void setNombrePantalla(NombrePantalla nombrePantalla) {
        this.nombrePantalla = nombrePantalla;
    }

    public TipoPantalla getTipoPantalla() {
        return tipoPantalla;
    }

    public void setTipoPantalla(TipoPantalla tipoPantalla) {
        this.tipoPantalla = tipoPantalla;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    
}
