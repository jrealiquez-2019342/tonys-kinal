
package org.joshuarealiquez.bean;

public class TipoPlato {
    private int codigoTipoPlato;
    private String descripcionTipo;
    
    public TipoPlato(){
        
    }
    
    public TipoPlato(int codigoTipoPlato, String descripcionTipo){
        this.codigoTipoPlato = codigoTipoPlato;
        this.descripcionTipo = descripcionTipo;
    }
    
    public void setCodigoTipoPlato(int codigoTipoPlato){
        this.codigoTipoPlato = codigoTipoPlato;
    }
    
    public int getCodigoTipoPlato(){
        return codigoTipoPlato;
    }
    
    public void setDescripcionTipo(String descripcionTipo){
        this.descripcionTipo = descripcionTipo;
    }
    
    public String getDescripcionTipo(){
        return descripcionTipo;
    }

    @Override
    public String toString() {
        return codigoTipoPlato + " | " + descripcionTipo;
    }
    
    
}
