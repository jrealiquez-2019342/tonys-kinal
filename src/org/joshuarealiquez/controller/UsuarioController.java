package org.joshuarealiquez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import org.apache.commons.codec.digest.DigestUtils;
import org.joshuarealiquez.bean.Usuario;
import org.joshuarealiquez.db.Conexion;
import org.joshuarealiquez.main.Principal;

public class UsuarioController implements Initializable {

    private Principal escenarioPrincipal;

    @FXML
    private TextField txtNombreUsuario;

    @FXML
    private TextField txtApellidoUsuario;

    @FXML
    private TextField txtUsuario;

    @FXML
    private TextField txtPassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    
    //Con esto puedo intercambiar las escenas,
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void create() {

        if (validarCampos()) {
            guardarUsuario();
            JOptionPane.showMessageDialog(null, "Usuario Registrado");
            limpiarControles();
            escenarioPrincipal.ventanaLogin();
        } else {
            JOptionPane.showMessageDialog(null, "Hay campos vacios en el formulario.");
        }
    }

    public void guardarUsuario() {
        Usuario registro = new Usuario();
        registro.setNombreUsuario(txtNombreUsuario.getText());
        registro.setApellidoUsuario(txtApellidoUsuario.getText());
        registro.setUsuarioLogin(txtUsuario.getText());
        registro.setConstrasena(DigestUtils.md5Hex(txtPassword.getText().getBytes()));
        
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarUsuario(?,?,?,?)}");
            procedimiento.setString(1, registro.getNombreUsuario());
            procedimiento.setString(2, registro.getApellidoUsuario());
            procedimiento.setString(3, registro.getUsuarioLogin());
            procedimiento.setString(4, registro.getConstrasena());
            procedimiento.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean validarCampos() {
        if (txtNombreUsuario.getText().isEmpty() || txtApellidoUsuario.getText().isEmpty() || txtUsuario.getText().isEmpty() || txtPassword.getText().isEmpty()) {
            return false;
        }
        return true;
    }

    public void activarControles() {
        txtNombreUsuario.setDisable(false);
        txtApellidoUsuario.setDisable(false);
        txtUsuario.setDisable(false);
        txtPassword.setDisable(false);
    }

    public void desactivarControles() {
        txtNombreUsuario.setDisable(true);
        txtApellidoUsuario.setDisable(true);
        txtUsuario.setDisable(true);
        txtPassword.setDisable(true);
    }

    public void limpiarControles() {
        txtNombreUsuario.clear();
        txtApellidoUsuario.clear();
        txtUsuario.clear();
        txtPassword.clear();
    }
    
    public void ventanaLogin(){
        escenarioPrincipal.ventanaLogin();
    }
   

}
