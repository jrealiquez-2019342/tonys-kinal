
package org.joshuarealiquez.controller;

import java.net.URL;
import java.security.MessageDigest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import static org.apache.commons.codec.binary.Hex.decodeHex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.Md5Crypt;
import org.joshuarealiquez.bean.Login;
import org.joshuarealiquez.bean.Usuario;
import org.joshuarealiquez.db.Conexion;
import org.joshuarealiquez.main.Principal;

public class LoginController implements Initializable{
    
    private Principal escenarioPrincipal;
    
    //String secretPhrase = "25d55ad283aa400af464c76d713c07ad";
    
    private ObservableList<Usuario> listaUsuario;
    
    @FXML
    private TextField txtUserName;
    
    @FXML
    private TextField txtPassword;
    
    DigestUtils digestUtils = new DigestUtils();
    
    
    @Override
    public void initialize(URL location, ResourceBundle bundle){
        
    }
    
    public ObservableList<Usuario> getUsuario(){
        ArrayList<Usuario> lista = new ArrayList<Usuario>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_ListarUsuarios");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Usuario(resultado.getInt("codigoUsuario"),resultado.getString("nombreUsuario"),
                    resultado.getString("apellidoUsuario"), resultado.getString("usuarioLogin"),
                    resultado.getString("contrasena"))
                
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaUsuario = FXCollections.observableArrayList(lista);
    }
    
    @FXML
    private void sesion() throws Exception{
        Login login = new Login();
        int x = 0;
        boolean bandera = false;
        login.setUsuarioMaster(txtUserName.getText());
        login.setPasswordLogin(txtPassword.getText());    
        
        listaUsuario = getUsuario();
        
        while(x < listaUsuario.size()){
            String user = listaUsuario.get(x).getUsuarioLogin();
            String pass = listaUsuario.get(x).getConstrasena();
            //System.out.println("Pass>"+pass);
            
            if(user.equals(login.getUsuarioMaster()) && pass.equals(DigestUtils.md5Hex(login.getPasswordLogin()))){
                JOptionPane.showMessageDialog(null, "Sesion Iniciada\n"
                    +getUsuario().get(x).getNombreUsuario() + " " + 
                        getUsuario().get(x).getApellidoUsuario()+"\n"
                    +"Bienvenido!!"
                );
                escenarioPrincipal.menuPrincipal();
                x = getUsuario().size();
                bandera = true;
            }
            
            x++;
        }
        
        if(!bandera){
            JOptionPane.showMessageDialog(null, "Usuario y/o contraseña incorrecta.");
        }
    }
    
    public Principal getEscenarioPrincipal(){
        return escenarioPrincipal;
    }
    
    public void setEscenarioPrincipal(Principal escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    
    public void login(){
        menu();
    }
    
    
    public void signIn(){
        escenarioPrincipal.ventanaUsuario();
    }

    
    public void menu(){
        escenarioPrincipal.menuPrincipal();
    }
    
    
    
}
