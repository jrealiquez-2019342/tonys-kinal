package org.joshuarealiquez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javafx.fxml.FXML;

import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javax.swing.JOptionPane;

import org.joshuarealiquez.bean.TipoPlato;
import org.joshuarealiquez.db.Conexion;
import org.joshuarealiquez.main.Principal;

public class TipoPlatoController implements Initializable {

    private Principal escenarioPrincipal;
    private ObservableList<TipoPlato> listaTipoPlato;

    private enum operaciones {
        NUEVO, GUARDAR, ACTUALIZAR, ELIMINAR, CANCELAR, NINGUNO
    }

    private operaciones tipoDeOperacion = operaciones.NINGUNO;

    @FXML
    private TextField txtCodigoTipoPlato;

    @FXML
    private TextField txtDescripcionTipoPlato;

    @FXML
    private Label lbCampoVacioDescrip;

    @FXML
    private Button btnNuevo;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnReporte;
    
    @FXML
    private Button btnPlatos;

    @FXML
    private ImageView imgNuevo;

    @FXML
    private ImageView imgEditar;

    @FXML
    private ImageView imgEliminar;

    @FXML
    private ImageView imgReporte;

    @FXML
    private TableView tblTiposPlatos;

    @FXML
    private TableColumn colCodigoTipoPlato;

    @FXML
    private TableColumn colDescripcionTipoPlato;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        volverInvisible();
    }

    public void volverInvisible() {
        lbCampoVacioDescrip.setVisible(false);
    }

    public void cargarDatos() {
        tblTiposPlatos.setItems(getTipoPlato());

        colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato, Integer>("codigoTipoPlato"));
        colDescripcionTipoPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato, String>("descripcionTipo"));
    }

    public ObservableList<TipoPlato> getTipoPlato() {
        ArrayList<TipoPlato> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_ListarTiposPlatos");
            ResultSet resultado = procedimiento.executeQuery();

            while (resultado.next()) {
                lista.add(new TipoPlato(
                        resultado.getInt("codigoTipoPlato"),
                        resultado.getString("descripcionTipo")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaTipoPlato = FXCollections.observableArrayList(lista);
    }

    public void nuevo() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                limpiarControles();
                habilitarControles();

                desactivarBotones();
                tblTiposPlatos.getSelectionModel().clearSelection();
                tipoDeOperacion = operaciones.NUEVO;

                //es necesario deshabilitar la seleccion de la tabla
                tblTiposPlatos.setDisable(true);
                break;

            case NUEVO:
                if (txtDescripcionTipoPlato.getText().isEmpty()) {
                    lbCampoVacioDescrip.setVisible(true);
                } else {
                    lbCampoVacioDescrip.setVisible(false);
                    guardar();
                    limpiarControles();
                    desactivarControles();

                    habilitarBotones();

                    cargarDatos();
                    tblTiposPlatos.setDisable(false);
                    tipoDeOperacion = operaciones.NINGUNO;
                }
                break;
            case ACTUALIZAR:
                if (txtDescripcionTipoPlato.getText().isEmpty()) {
                    lbCampoVacioDescrip.setVisible(true);
                } else {
                    lbCampoVacioDescrip.setVisible(false);
                    actualizar();
                    habilitarBotones();
                    limpiarControles();
                    cargarDatos();
                    tblTiposPlatos.getSelectionModel().clearSelection();
                    tipoDeOperacion = operaciones.NINGUNO;
                }
                
                break;
        }
    }

    public void guardar() {
        TipoPlato registro = new TipoPlato();
        //registro.setCodigoTipoPlato(Integer.parseInt(txtCodigoTipoPlato.getText()));
        registro.setDescripcionTipo(txtDescripcionTipoPlato.getText());

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_AgregarTipoPlato(?)");
            procedimiento.setString(1, registro.getDescripcionTipo());

            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void actualizar() {

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_EditarTipoPlato(?,?)");
            TipoPlato registro = (TipoPlato) tblTiposPlatos.getSelectionModel().getSelectedItem();

            registro.setDescripcionTipo(txtDescripcionTipoPlato.getText());

            procedimiento.setInt(1, registro.getCodigoTipoPlato());
            procedimiento.setString(2, registro.getDescripcionTipo());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void seleccionarElemento() {
        if (tblTiposPlatos.getSelectionModel().getSelectedItem() != null) {
            txtCodigoTipoPlato.setText(String.valueOf(((TipoPlato) tblTiposPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
            txtDescripcionTipoPlato.setText(((TipoPlato) tblTiposPlatos.getSelectionModel().getSelectedItem()).getDescripcionTipo());
        }
    }

    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblTiposPlatos.getSelectionModel().getSelectedItem() != null) {
                    desactivarBotones();
                    habilitarControles();

                    tipoDeOperacion = operaciones.ACTUALIZAR;

                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento de la tabla.");
                }
                break;
            case NUEVO:
                limpiarControles();
                habilitarBotones();
                desactivarControles();
                lbCampoVacioDescrip.setVisible(false);
                tblTiposPlatos.setDisable(false);
                tblTiposPlatos.getSelectionModel().clearSelection();

                tipoDeOperacion = operaciones.NINGUNO;
                break;
            case ACTUALIZAR:
                limpiarControles();
                desactivarControles();
                habilitarBotones();
                lbCampoVacioDescrip.setVisible(false);
                tblTiposPlatos.getSelectionModel().clearSelection();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }

    public void eliminar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblTiposPlatos.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Tipo Plato", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_EliminarTipoPlato(?)");
                            procedimiento.setInt(1, ((TipoPlato) tblTiposPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());

                            procedimiento.execute();

                            listaTipoPlato.remove(tblTiposPlatos.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            tblTiposPlatos.getSelectionModel().clearSelection();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (respuesta == JOptionPane.NO_OPTION) {
                        limpiarControles();
                        tblTiposPlatos.getSelectionModel().clearSelection();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento de la tabla.");
                }
                break;
        }
    }

    public void habilitarBotones() {
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

    public void habilitarControles() {
        txtCodigoTipoPlato.setEditable(false);
        txtDescripcionTipoPlato.setEditable(true);
    }

    public void desactivarControles() {
        txtCodigoTipoPlato.setEditable(false);
        txtDescripcionTipoPlato.setEditable(false);
    }

    public void limpiarControles() {
        txtCodigoTipoPlato.clear();
        txtDescripcionTipoPlato.clear();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }
    
    public void platos(){
        escenarioPrincipal.ventanaPlatos();
    }
}
