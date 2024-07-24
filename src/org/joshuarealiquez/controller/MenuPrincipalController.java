
package org.joshuarealiquez.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.joshuarealiquez.main.Principal;

public class MenuPrincipalController implements Initializable{
    private Principal escenarioPrincipal;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Aca se ejecutan los valores que por defecto quiero en la clase.
        
    }

    //Con esto puedo intercambiar las escenas,
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    //Darle funcionamiento a los botones
    public void ventanaProgramador(){
        escenarioPrincipal.ventanaProgramador();
    }
    
    public void ventanaEmpresa(){
        escenarioPrincipal.ventanaEmpresa();
    }
    
    public void ventanaTipoEmpleado(){
        escenarioPrincipal.ventanaTipoEmpleado();
    }
    
    public void ventanaTipoPlato(){
        escenarioPrincipal.ventanaTipoPlato();
    }
    
    public void ventanaProductos(){
        escenarioPrincipal.ventanaProductos();
    }
    
    public void ventanaEmpleados(){
        escenarioPrincipal.ventanaEmpleados();
    }
    
    public void ventanaServicios(){
        escenarioPrincipal.ventanaServicios();
    }
    
    public void ventanaPresupuestos(){
        escenarioPrincipal.ventanaPresupuestos();
    }
    
    public void ventanaPlatos(){
        escenarioPrincipal.ventanaPlatos();
    }
    
    public void ventanaProductosPlatos(){
        escenarioPrincipal.ventanaProductosPlatos();
    }
    
    public void ventanaServiciosPlatos(){
        escenarioPrincipal.ventanaServiciosPlatos();
    }
    
    public void ventanaServiciosEmpleados(){
        escenarioPrincipal.ventanaServiciosEmpleados();
    }
    
    public void cerrarSesion(){
        escenarioPrincipal.ventanaLogin();
    }
}
