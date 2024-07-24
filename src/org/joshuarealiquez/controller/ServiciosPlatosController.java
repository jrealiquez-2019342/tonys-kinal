/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joshuarealiquez.controller;

import java.net.URL;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import org.joshuarealiquez.bean.Plato;
import org.joshuarealiquez.bean.Servicios;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import org.joshuarealiquez.bean.ServicioPlato;
import org.joshuarealiquez.db.Conexion;
import org.joshuarealiquez.main.Principal;

/**
 *
 * @author isaac
 */
public class ServiciosPlatosController implements Initializable {

    private Principal escenarioPrincipal;

    private enum operaciones {
        AGREGAR, ACTUALIZAR, EDITAR, NINGUNO;
    }

    private operaciones tipoDeOperacion = operaciones.NINGUNO;

    private ObservableList listaPlato;
    private ObservableList listaServicio;
    private ObservableList listaServicioPlato;

    @FXML
    private Button btnNuevo;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnReporte;

    @FXML
    private ImageView imgNuevo;

    @FXML
    private ImageView imgEditar;

    @FXML
    private TableView tblServiciosPlatos;

    @FXML
    private TableColumn colServiciosCodigoServicio;

    @FXML
    private TableColumn colCodigoPlato;

    @FXML
    private TableColumn colCodigoServicio;

    @FXML
    private ComboBox cmbCodigoPlato;

    @FXML
    private ComboBox cmbCodigoServicio;

    @FXML
    private TextField txtServiciosCodigoServicio;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }

    public void cargarDatos() {
        tblServiciosPlatos.setItems(getServiciosPlatos());
        colServiciosCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServicioPlato, Integer>("Servicios_codigoServicio"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<ServicioPlato, Integer>("codigoPlato"));
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServicioPlato, Integer>("codigoServicio"));

        cmbCodigoPlato.setItems(getPlatos());
        cmbCodigoServicio.setItems(getServicios());

        desactivarControles();
    }

    public ObservableList<ServicioPlato> getServiciosPlatos() {
        ArrayList<ServicioPlato> lista = new ArrayList<ServicioPlato>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_ListarServiciosHasPlatos");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new ServicioPlato(resultado.getInt("Servicios_codigoServicio"),
                        resultado.getInt("codigoPlato"), resultado.getInt("codigoServicio"))
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaServicioPlato = FXCollections.observableArrayList(lista);
    }

    public ObservableList<Plato> getPlatos() {
        ArrayList<Plato> lista = new ArrayList<Plato>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_ListarPlatos");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Plato(resultado.getInt("codigoPlato"), resultado.getInt("cantidad"),
                        resultado.getString("nombrePlato"), resultado.getString("descripcionPlato"),
                        resultado.getDouble("precioPlato"), resultado.getInt("codigoTipoPlato"))
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaPlato = FXCollections.observableArrayList(lista);
    }

    public ObservableList<Servicios> getServicios() {
        ArrayList<Servicios> lista = new ArrayList<Servicios>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_ListarServicios");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Servicios(resultado.getInt("codigoServicio"), resultado.getDate("fechaServicio"),
                        resultado.getString("tipoServicio"), resultado.getTime("horaServicio"),
                        resultado.getString("lugarServicio"), resultado.getString("telefonoContacto"),
                        resultado.getInt("codigoEmpresa"))
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaServicio = FXCollections.observableArrayList(lista);
    }

    public void seleccionarElemento() {
        if (tblServiciosPlatos.getSelectionModel().getSelectedItem() != null) {
            txtServiciosCodigoServicio.setText(String.valueOf(((ServicioPlato) tblServiciosPlatos.getSelectionModel().getSelectedItem()).getServicios_codigoServicio()));
            cmbCodigoPlato.getSelectionModel().select(buscarPlato(((ServicioPlato) tblServiciosPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
            cmbCodigoServicio.getSelectionModel().select(buscarServicio(((ServicioPlato) tblServiciosPlatos.getSelectionModel().getSelectedItem()).getCodigoServicio()));
        }
    }

    public Plato buscarPlato(int codigoPlato) {
        Plato registro = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_BuscarPlato(?)");
            procedimiento.setInt(1, codigoPlato);
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                registro = new Plato(resultado.getInt("codigoPlato"),
                        resultado.getInt("cantidad"),
                        resultado.getString("nombrePlato"),
                        resultado.getString("descripcionPlato"),
                        resultado.getDouble("precioPlato"),
                        resultado.getInt("codigoTipoPlato")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return registro;
    }

    public Servicios buscarServicio(int codigoServicio) {
        Servicios registro = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_BuscarServicio(?)");
            procedimiento.setInt(1, codigoServicio);
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                registro = new Servicios(resultado.getInt("codigoServicio"), resultado.getDate("fechaServicio"),
                        resultado.getString("tipoServicio"), resultado.getTime("horaServicio"),
                        resultado.getString("lugarServicio"), resultado.getString("telefonoContacto"),
                        resultado.getInt("codigoEmpresa"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return registro;
    }

    public void nuevo() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                limpiarControles();
                activarControles();
                desactivarBotones();
                tblServiciosPlatos.getSelectionModel().clearSelection();
                tblServiciosPlatos.setDisable(true);
                tipoDeOperacion = operaciones.AGREGAR;
                break;
            case AGREGAR:
                if (validarCampos()) {
                    guardar();
                    desactivarControles();
                    activarBotones();
                    limpiarControles();
                    tblServiciosPlatos.setDisable(false);
                    tipoDeOperacion = operaciones.NINGUNO;
                    cargarDatos();
                } else {
                    JOptionPane.showMessageDialog(null, "Hay campos vacios en el formulario");
                }

                break;

        }
    }

    public void editar() {
        switch (tipoDeOperacion) {
            case AGREGAR:
                desactivarControles();
                limpiarControles();
                activarBotones();
                tblServiciosPlatos.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }

    public void guardar() {
        ServicioPlato registro = new ServicioPlato();
        registro.setServicios_codigoServicio(Integer.valueOf(txtServiciosCodigoServicio.getText()));
        registro.setCodigoPlato(((Plato)cmbCodigoPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
        registro.setCodigoServicio(((Servicios) cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_AgregarServicioHasPlato(?,?,?)");
            procedimiento.setInt(1, registro.getServicios_codigoServicio());
            procedimiento.setInt(2, registro.getCodigoPlato());
            procedimiento.setInt(3, registro.getCodigoServicio());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void desactivarControles() {
        txtServiciosCodigoServicio.setEditable(false);
        cmbCodigoPlato.setDisable(true);
        cmbCodigoPlato.setOpacity(100.00);
        
        cmbCodigoServicio.setDisable(true);
        cmbCodigoServicio.setOpacity(100.00);
    }

    public void activarControles() {
        txtServiciosCodigoServicio.setEditable(true);
        cmbCodigoPlato.setDisable(false);
        cmbCodigoServicio.setDisable(false);
    }

    public void limpiarControles() {
        txtServiciosCodigoServicio.clear();
        cmbCodigoPlato.getSelectionModel().select(null);
        cmbCodigoServicio.getSelectionModel().select(null);
    }

    public void activarBotones() {
        btnNuevo.setText("NUEVO");
        btnEditar.setText("EDITAR");
        imgNuevo.setImage(new Image("/org/joshuarealiquez/image/addIcon.png"));
        imgEditar.setImage(new Image("/org/joshuarealiquez/image/editIcon.png"));
        btnEliminar.setDisable(false);
        btnReporte.setDisable(false);
    }

    public void desactivarBotones() {
        btnNuevo.setText("GUARDAR");
        btnEditar.setText("CANCELAR");
        imgNuevo.setImage(new Image("/org/joshuarealiquez/image/saveIcon.png"));
        imgEditar.setImage(new Image("/org/joshuarealiquez/image/cancelIcon.png"));
        btnEliminar.setDisable(true);
        btnReporte.setDisable(true);
    }

    public boolean validarCampos() {
        if (cmbCodigoPlato.getSelectionModel().getSelectedItem() == null || cmbCodigoServicio.getSelectionModel().getSelectedItem() == null) {
            return false;
        }
        return true;
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    //Funcionamiento a los botones
    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }

}
