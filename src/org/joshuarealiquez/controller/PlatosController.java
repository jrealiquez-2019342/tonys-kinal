package org.joshuarealiquez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.joshuarealiquez.main.Principal;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.joshuarealiquez.bean.Plato;
import org.joshuarealiquez.bean.TipoPlato;
import org.joshuarealiquez.db.Conexion;

public class PlatosController implements Initializable {

    private Principal escenarioPrincipal;

    private enum operaciones {
        NINGUNO, NUEVO, ACTUALIZAR, ELIMINAR
    }

    private operaciones tipoDeOperacion = operaciones.NINGUNO;

    private ObservableList listaPlato;
    private ObservableList listaTipoPlato;

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
    private TableView tblPlatos;

    @FXML
    private TableColumn colCodigoPlato;

    @FXML
    private TableColumn colCantidadPlato;

    @FXML
    private TableColumn colNombrePlato;

    @FXML
    private TableColumn colDescripcionPlato;

    @FXML
    private TableColumn colPrecioPlato;

    @FXML
    private TableColumn colCodigoTipoPlato;

    @FXML
    private TextField txtCodigoPlato;

    @FXML
    private TextField txtNombrePlato;

    @FXML
    private TextField txtPrecioPlato;

    @FXML
    private TextField txtCantidad;

    @FXML
    private TextField txtDescripcionPlato;

    @FXML
    private ComboBox cmbCodigoTipoPlato;

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        cargarDatos();
    }

    public void cargarDatos() {
        tblPlatos.setItems(getPlatos());
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("codigoPlato"));
        colCantidadPlato.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("cantidad"));
        colNombrePlato.setCellValueFactory(new PropertyValueFactory<Plato, String>("nombrePlato"));
        colDescripcionPlato.setCellValueFactory(new PropertyValueFactory<Plato, String>("descripcionPlato"));
        colPrecioPlato.setCellValueFactory(new PropertyValueFactory<Plato, Double>("precioPlato"));
        colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("codigoTipoPlato"));

        cmbCodigoTipoPlato.setItems(getTipoPlatos());

        desactivarControles();
    }

    public ObservableList<Plato> getPlatos() {
        ArrayList<Plato> lista = new ArrayList<Plato>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_ListarPlatos");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Plato(resultado.getInt("codigoPlato"),
                        resultado.getInt("cantidad"), resultado.getString("nombrePlato"),
                        resultado.getString("descripcionPlato"), resultado.getDouble("precioPlato"),
                        resultado.getInt("codigoTipoPlato"))
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaPlato = FXCollections.observableArrayList(lista);
    }

    public ObservableList<TipoPlato> getTipoPlatos() {
        ArrayList<TipoPlato> lista = new ArrayList<TipoPlato>();

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_ListarTiposPlatos");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new TipoPlato(resultado.getInt("codigoTipoPlato"),
                        resultado.getString("descripcionTipo"))
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaTipoPlato = FXCollections.observableArrayList(lista);
    }

    public void seleccionarElemento() {
        if (tblPlatos.getSelectionModel().getSelectedItem() != null) {
            txtCodigoPlato.setText(String.valueOf(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
            txtNombrePlato.setText(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getNombrePlato());
            txtDescripcionPlato.setText(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getDescripcionPlato());
            txtPrecioPlato.setText(String.valueOf(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getPrecioPlato()));
            txtCantidad.setText(String.valueOf(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getCantidad()));
            cmbCodigoTipoPlato.getSelectionModel().select(buscarTipoPlato(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));

        }
    }

    public TipoPlato buscarTipoPlato(int codigoTipoPlato) {
        TipoPlato registro = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_BuscarTipoPlato(?)");
            procedimiento.setInt(1, codigoTipoPlato);
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                registro = new TipoPlato(
                        resultado.getInt("codigoTipoPlato"),
                        resultado.getString("descripcionTipo")
                );
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
                tblPlatos.getSelectionModel().clearSelection();
                tblPlatos.setDisable(true);
                tipoDeOperacion = operaciones.NUEVO;
                break;
            case NUEVO:
                if (validarCampos()) {
                    if (validarPrecioPlato() || validarCantidadPlato()) {

                    } else {
                        guardar();
                        limpiarControles();
                        activarBotones();
                        desactivarControles();
                        tblPlatos.setDisable(false);
                        tipoDeOperacion = operaciones.NINGUNO;
                        cargarDatos();
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Hay campos vacios en el formualario.");
                }

                break;
            case ACTUALIZAR:
                if (validarCampos()) {
                    actualizar();
                    desactivarControles();
                    limpiarControles();
                    activarBotones();
                    tblPlatos.setDisable(false);
                    tblPlatos.getSelectionModel().clearSelection();
                    tipoDeOperacion = operaciones.NINGUNO;
                    cargarDatos();
                } else {
                    JOptionPane.showMessageDialog(null, "Hay campos vacios en el formualario.");
                }

                break;

        }
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_EditarPlato(?,?,?,?,?)");

            Plato registro = (Plato) tblPlatos.getSelectionModel().getSelectedItem();

            registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
            registro.setNombrePlato(txtNombrePlato.getText());
            registro.setDescripcionPlato(txtDescripcionPlato.getText());
            registro.setPrecioPlato(Double.parseDouble(txtPrecioPlato.getText()));

            procedimiento.setInt(1, registro.getCodigoPlato());
            procedimiento.setInt(2, registro.getCantidad());
            procedimiento.setString(3, registro.getNombrePlato());
            procedimiento.setString(4, registro.getDescripcionPlato());
            procedimiento.setDouble(5, registro.getPrecioPlato());

            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblPlatos.getSelectionModel().getSelectedItem() != null) {
                    desactivarBotones();
                    activarControles();
                    tblPlatos.setDisable(true);
                    cmbCodigoTipoPlato.setDisable(true);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento de la tabla.");
                }
                break;
            case NUEVO:
                activarBotones();
                limpiarControles();
                desactivarControles();
                tblPlatos.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            case ACTUALIZAR:
                limpiarControles();
                activarBotones();
                desactivarControles();
                tblPlatos.setDisable(false);
                tblPlatos.getSelectionModel().clearSelection();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }

    public void guardar() {
        Plato registro = new Plato();

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_AgregarPlato(?,?,?,?,?)");

            registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
            registro.setNombrePlato(txtNombrePlato.getText());
            registro.setDescripcionPlato(txtDescripcionPlato.getText());
            registro.setPrecioPlato(Double.parseDouble(txtPrecioPlato.getText()));
            registro.setCodigoTipoPlato(((TipoPlato) cmbCodigoTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());

            procedimiento.setInt(1, registro.getCantidad());
            procedimiento.setString(2, registro.getNombrePlato());
            procedimiento.setString(3, registro.getDescripcionPlato());
            procedimiento.setDouble(4, registro.getPrecioPlato());
            procedimiento.setInt(5, registro.getCodigoTipoPlato());

            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar() {
        if (tblPlatos.getSelectionModel().getSelectedItem() != null) {
            int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el elemento?", "Eliminar Platos", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (respuesta == JOptionPane.YES_OPTION) {
                try {
                    PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_EliminarPlato(?)");
                    procedimiento.setInt(1, ((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato());
                    listaPlato.remove(tblPlatos.getSelectionModel().getSelectedIndex());
                    procedimiento.execute();
                    limpiarControles();
                    tblPlatos.getSelectionModel().clearSelection();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                tblPlatos.getSelectionModel().clearSelection();
                limpiarControles();

            }
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento de la tabla.");
        }

    }

    public void activarControles() {
        txtCodigoPlato.setEditable(false);
        txtNombrePlato.setEditable(true);
        txtPrecioPlato.setEditable(true);
        txtCantidad.setEditable(true);
        txtDescripcionPlato.setEditable(true);
        cmbCodigoTipoPlato.setDisable(false);
    }

    public void desactivarControles() {
        txtCodigoPlato.setEditable(false);
        txtNombrePlato.setEditable(false);
        txtPrecioPlato.setEditable(false);
        txtCantidad.setEditable(false);
        txtDescripcionPlato.setEditable(false);
        cmbCodigoTipoPlato.setDisable(true);
        cmbCodigoTipoPlato.setOpacity(100.00);
    }

    public void limpiarControles() {
        txtCodigoPlato.clear();
        txtNombrePlato.clear();
        txtPrecioPlato.clear();
        txtCantidad.clear();
        txtDescripcionPlato.clear();
        cmbCodigoTipoPlato.getSelectionModel().select(null);
    }

    public void desactivarBotones() {
        btnNuevo.setText("GUARDAR");
        btnEditar.setText("CANCELAR");

        imgNuevo.setImage(new Image("/org/joshuarealiquez/image/saveIcon.png"));
        imgEditar.setImage(new Image("/org/joshuarealiquez/image/cancelIcon.png"));

        btnEliminar.setDisable(true);
        btnReporte.setDisable(true);
    }

    public void activarBotones() {
        btnNuevo.setText("NUEVO");
        btnEditar.setText("EDITAR");

        imgNuevo.setImage(new Image("/org/joshuarealiquez/image/addIcon.png"));
        imgEditar.setImage(new Image("/org/joshuarealiquez/image/editIcon.png"));

        btnEliminar.setDisable(false);
        btnReporte.setDisable(false);
    }

    public boolean validarCampos() {
        if (txtNombrePlato.getText().isEmpty() || txtPrecioPlato.getText().isEmpty() || txtCantidad.getText().isEmpty() || txtDescripcionPlato.getText().isEmpty()
                || cmbCodigoTipoPlato.getSelectionModel().getSelectedItem() == null) {
            return false;
        }

        return true;
    }

    public boolean validarPrecioPlato() {
        try {
            double precio = Double.parseDouble(txtPrecioPlato.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error en el precio del plato.");
            return true;
        }

        return false;
    }

    public boolean validarCantidadPlato() {
        try {
            int cantidad = Integer.valueOf(txtCantidad.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error en la cantidad del plato.");
            return true;
        }
        return false;
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    //botones
    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }

    public void back() {
        escenarioPrincipal.ventanaTipoPlato();
    }
}
