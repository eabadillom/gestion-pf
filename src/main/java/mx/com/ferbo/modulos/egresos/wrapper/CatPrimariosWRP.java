package mx.com.ferbo.modulos.egresos.wrapper;

import java.util.List;

import mx.com.ferbo.modulos.egresos.model.catprimarios.CatConceptoEgreso;
import mx.com.ferbo.modulos.egresos.model.catprimarios.CategoriaEgreso;
import mx.com.ferbo.modulos.egresos.model.catprimarios.TipoEgreso;

public class CatPrimariosWRP {

    private Boolean vigente = Boolean.TRUE;

    private TipoEgreso tipoSelected;
    private List<TipoEgreso> lstTipos;

    private CategoriaEgreso categoriaSelected;
    private List<CategoriaEgreso> lstCategorias;

    private CatConceptoEgreso catConceptoSelected;
    private List<CatConceptoEgreso> lstCatConceptos;

    public TipoEgreso getTipoSelected() {
        return tipoSelected;
    }

    public void setTipoSelected(TipoEgreso tipoSelected) {
        this.tipoSelected = tipoSelected;
    }

    public List<TipoEgreso> getLstTipos() {
        return lstTipos;
    }

    public void setLstTipos(List<TipoEgreso> lstTipos) {
        this.lstTipos = lstTipos;
    }

    public CategoriaEgreso getCategoriaSelected() {
        return categoriaSelected;
    }

    public void setCategoriaSelected(CategoriaEgreso categoriaSelected) {
        this.categoriaSelected = categoriaSelected;
    }

    public List<CategoriaEgreso> getLstCategorias() {
        return lstCategorias;
    }

    public void setLstCategorias(List<CategoriaEgreso> lstCategorias) {
        this.lstCategorias = lstCategorias;
    }

    public CatConceptoEgreso getCatConceptoSelected() {
        return catConceptoSelected;
    }

    public void setCatConceptoSelected(CatConceptoEgreso catConceptoSelected) {
        this.catConceptoSelected = catConceptoSelected;
    }

    public List<CatConceptoEgreso> getLstCatConceptos() {
        return lstCatConceptos;
    }

    public void setLstCatConceptos(List<CatConceptoEgreso> lstCatConceptos) {
        this.lstCatConceptos = lstCatConceptos;
    }

    public Boolean getVigente() {
        return vigente;
    }

    public void setVigente(Boolean vigente) {
        this.vigente = vigente;
    }

    
}
