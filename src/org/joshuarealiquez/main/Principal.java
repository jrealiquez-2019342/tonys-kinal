/*
    Joshua Elí Isaac Realiquez Sosa
    IN5AM
    2019342
    28/03/2023
*/
package org.joshuarealiquez.main;

import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.joshuarealiquez.controller.EmpleadosController;
import org.joshuarealiquez.controller.EmpresaController;
import org.joshuarealiquez.controller.LoginController;
import org.joshuarealiquez.controller.MenuPrincipalController;
import org.joshuarealiquez.controller.PlatosController;
import org.joshuarealiquez.controller.PresupuestosController;
import org.joshuarealiquez.controller.ProductosController;
import org.joshuarealiquez.controller.ProductosPlatosController;
import org.joshuarealiquez.controller.ProgramadorController;
import org.joshuarealiquez.controller.ServiciosController;
import org.joshuarealiquez.controller.ServiciosEmpleadosController;
import org.joshuarealiquez.controller.ServiciosPlatosController;
import org.joshuarealiquez.controller.TipoEmpleadoController;
import org.joshuarealiquez.controller.TipoPlatoController;
import org.joshuarealiquez.controller.UsuarioController;


public class Principal extends Application {
    
    private Stage escenarioPrincipal;
    private Scene escena;
    private final String PAQUETE_VISTA = "/org/joshuarealiquez/view/";
   
    
    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        //Se ejecuta al cargar la aplicacion.
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Tony's Kinal 2023");
        
        escenarioPrincipal.getIcons().add(new Image("/org/joshuarealiquez/image/logoFinal.png"));
        

        //Parent root = FXMLLoader.load(getClass().getResource("/org/joshuarealiquez/view/MenuPrincipalView.fxml"));
        //Scene escena = new Scene(root);
        //escenarioPrincipal.setScene(escena);
        
        //ventanaPresupuestos();
        //ventanaServicios();
        //menuPrincipal();
        ventanaLogin();
        
        //vistaProgramador();
        escenarioPrincipal.show();
    }

    
    public void menuPrincipal(){
        try{
            MenuPrincipalController menu = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml",630,640);
            menu.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaLogin(){
        try{
            LoginController login = (LoginController)cambiarEscena("LoginView.fxml", 472,639);
            login.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaUsuario(){
        try{
            UsuarioController usuario = (UsuarioController)cambiarEscena("UsuarioView.fxml", 472,639);
            usuario.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaProgramador(){
        try{
            ProgramadorController programador = (ProgramadorController)cambiarEscena("ProgramadorView.fxml", 544,640);
            programador.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaEmpresa(){
        try{
            EmpresaController empresa = (EmpresaController)cambiarEscena("EmpresaView.fxml", 1169,640);
            empresa.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public void ventanaTipoEmpleado(){
        try{
            TipoEmpleadoController tipoEmpleado = (TipoEmpleadoController)cambiarEscena("TipoEmpleadoView.fxml", 818,640);
            tipoEmpleado.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaTipoPlato(){
        try{
            TipoPlatoController tipoPlato = (TipoPlatoController) cambiarEscena("TipoPlatoView.fxml",830,640);
            tipoPlato.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public void ventanaProductos(){
        try{
            ProductosController productos = (ProductosController)cambiarEscena("ProductosView.fxml",1013,640);
            productos.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaEmpleados(){
        try{
            EmpleadosController empleado = (EmpleadosController)cambiarEscena("EmpleadosView.fxml", 1273, 640);
            empleado.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public void ventanaServicios(){
        try{
            ServiciosController servicio = (ServiciosController)cambiarEscena("ServiciosView.fxml",1140, 640);
            servicio.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaPresupuestos(){
        try{
            PresupuestosController presupuestos = (PresupuestosController)cambiarEscena("PresupuestosView.fxml", 955, 640);
            presupuestos.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public void ventanaPlatos(){
        try{
            PlatosController platos = (PlatosController)cambiarEscena("PlatosView.fxml", 1250, 640);
            platos.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaServiciosPlatos(){
        try{
            ServiciosPlatosController sp = (ServiciosPlatosController)cambiarEscena("ServiciosPlatosView.fxml",955,640);
            sp.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaProductosPlatos(){
        try{
            ProductosPlatosController pp = (ProductosPlatosController)cambiarEscena("ProductosPlatosView.fxml",900,640);
            pp.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaServiciosEmpleados(){
        try{
            ServiciosEmpleadosController se = (ServiciosEmpleadosController)cambiarEscena("ServiciosEmpleadosView.fxml",955,640);
            se.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public static void main(String[] args) {
        launch(args);
    }
    
    
    
    
    
    
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception{
        Initializable resultado = null;
        
        FXMLLoader cargadorFXML = new FXMLLoader();
        
        //Permite leer cosas abstractas
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        //ahora necesito jalar todo el disenio de JAVAFX a JAVA
        //Builder Factory carga el archivo de javaFX para entenderlo en Java.
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        
        //Ahora necesitamos abrir la plantilla
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        
        //En este momenot ya dimos dos clic
        
        //Ahora debemos crear la escena
        //debemos pasarle el archivo, ancho y alto
        // ()objetos -> este tipo de casteo se denomina casteo de clase
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo), ancho, alto);
        escenarioPrincipal.setScene(escena);
        
        
        //Hacemos que el escenario principal se adapte al tamanio de la escena
        
        escenarioPrincipal.sizeToScene();
        
        resultado = (Initializable) cargadorFXML.getController();
        return resultado;
    }
    
}
