package org.joshuarealiquez.controller;

import java.awt.Toolkit;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.joshuarealiquez.bean.TipoEmpleado;
import org.joshuarealiquez.bean.Empleado;
import org.joshuarealiquez.db.Conexion;
import org.joshuarealiquez.main.Principal;

public class EmpleadosController implements Initializable {

    private Principal escenarioPrincipal;

    private enum operaciones {
        NUEVO, EDITAR, ELIMINAR, NINGUNO
    }
    private operaciones tipoDeOperacion = operaciones.NINGUNO;

    private ObservableList<TipoEmpleado> listaTipoEmpleado;
    private ObservableList<Empleado> listaEmpleado;

    @FXML
    private Button btnNuevo;

    @FXML
    private ImageView imgNuevo;

    @FXML
    private Button btnEditar;

    @FXML
    private ImageView imgEditar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnReporte;

    @FXML
    private Button btnBack;

    @FXML
    private TextField txtCodigoEmpleado;

    @FXML
    private TextField txtNumeroEmpleado;

    @FXML
    private TextField txtNombresEmpleado;

    @FXML
    private TextField txtApellidosEmpleado;

    @FXML
    private TextField txtDireccionEmpleado;

    @FXML
    private TextField txtTelefonoContactoEmpleado;

    @FXML
    private TextField txtGradoCocineroEmpleado;

    @FXML
    private ComboBox cmbCodigoTipoEmpleado;

    @FXML
    private TableView tblEmpleados;

    @FXML
    private TableColumn colCodigoEmpleado;

    @FXML
    private TableColumn colNumeroEmpleado;

    @FXML
    private TableColumn colNombresEmpleado;

    @FXML
    private TableColumn colApellidosEmpleado;

    @FXML
    private TableColumn colDireccionEmpleado;

    @FXML
    private TableColumn colTelefonoEmpleado;

    @FXML
    private TableColumn colGradoCocineroEmpleado;

    @FXML
    private TableColumn colCodigoTipoEmpleado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        desactivarControles();
    }

    public void cargarDatos() {

        tblEmpleados.setItems(getEmpleados());
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoEmpleado"));
        colNumeroEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("numeroEmpleado"));
        colNombresEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombresEmpleado"));
        colApellidosEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("apellidosEmpleado"));
        colDireccionEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("direccionEmpleado"));
        colTelefonoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("telefonoContacto"));
        colGradoCocineroEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("gradoCocinero"));
        colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoTipoEmpleado"));

        cmbCodigoTipoEmpleado.setItems(getTiposEmpleados());

    }

    public ObservableList<Empleado> getEmpleados() {
        ArrayList lista = new ArrayList<Empleado>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_ListarEmpleados");
            ResultSet resultado = procedimiento.executeQuery();

            while (resultado.next()) {
                lista.add(new Empleado(resultado.getInt("codigoEmpleado"),
                        resultado.getInt("numeroEmpleado"),
                        resultado.getString("apellidosEmpleado"),
                        resultado.getString("nombresEmpleado"),
                        resultado.getString("direccionEmpleado"),
                        resultado.getString("telefonoContacto"),
                        resultado.getString("gradoCocinero"),
                        resultado.getInt("codigoTipoEmpleado"))
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaEmpleado = FXCollections.observableArrayList(lista);
    }

    public ObservableList<TipoEmpleado> getTiposEmpleados() {
        ArrayList lista = new ArrayList<TipoEmpleado>();

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_ListarTiposEmpleados");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new TipoEmpleado(resultado.getInt("codigoTipoEmpleado"),
                        resultado.getString("descripcion"))
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaTipoEmpleado = FXCollections.observableArrayList(lista);
    }

    public void seleccionar() {
        if (tblEmpleados.getSelectionModel().getSelectedItem() != null) {
            txtCodigoEmpleado.setText(String.valueOf(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
            txtNumeroEmpleado.setText(String.valueOf(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getNumeroEmpleado()));
            txtNombresEmpleado.setText(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getNombresEmpleado());
            txtApellidosEmpleado.setText(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getApellidosEmpleado());
            txtDireccionEmpleado.setText(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getDireccionEmpleado());
            txtTelefonoContactoEmpleado.setText(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getTelefonoContacto());
            txtGradoCocineroEmpleado.setText(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getGradoCocinero());
            cmbCodigoTipoEmpleado.getSelectionModel().select(buscarTipoEmpleado(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
        }
    }

    public TipoEmpleado buscarTipoEmpleado(int codigoTipoEmpleado) {
        TipoEmpleado registro = null;

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_BuscarTipoEmpleado(?)");
            procedimiento.setInt(1, codigoTipoEmpleado);
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                registro = new TipoEmpleado(resultado.getInt("codigoTipoEmpleado"),
                        resultado.getString("descripcion")
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
                activarControles();
                limpiarControles();
                desactivarBotones();
                tblEmpleados.setDisable(true);
                tblEmpleados.getSelectionModel().clearSelection();
                tipoDeOperacion = operaciones.NUEVO;
                break;
            case NUEVO:
                if (validarCampos()) {
                    if (validarNumeroTelefono() || validarNumeroEmpleado()) {

                    } else {
                        if (txtTelefonoContactoEmpleado.getText().length() != 8) {
                            JOptionPane.showMessageDialog(null, "El teléfono debe contener 8 caracteres.");
                        } else {
                            guardar();
                            limpiarControles();
                            desactivarControles();
                            activarBotones();
                            tblEmpleados.setDisable(false);
                            tipoDeOperacion = operaciones.NINGUNO;
                            cargarDatos();
                        }

                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Hay campos vacios en el formulario.");
                }

                break;
            case EDITAR:
                if (validarCampos()) {
                    actualizar();
                    desactivarControles();
                    activarBotones();
                    tblEmpleados.setDisable(false);
                    tblEmpleados.getSelectionModel().clearSelection();
                    cargarDatos();
                    tipoDeOperacion = operaciones.NINGUNO;
                } else {
                    JOptionPane.showMessageDialog(null, "Hay campos vacios en el formulario.");
                }

                break;
        }
    }

    public void guardar() {
        Empleado registro = new Empleado();

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_AgregarEmpleado(?,?,?,?,?,?,?)");

            registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
            registro.setApellidosEmpleado(txtApellidosEmpleado.getText());
            registro.setNombresEmpleado(txtNombresEmpleado.getText());
            registro.setDireccionEmpleado(txtDireccionEmpleado.getText());
            registro.setTelefonoContacto(txtTelefonoContactoEmpleado.getText());
            registro.setGradoCocinero(txtGradoCocineroEmpleado.getText());
            registro.setCodigoTipoEmpleado(((TipoEmpleado) cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());

            procedimiento.setInt(1, registro.getNumeroEmpleado());
            procedimiento.setString(2, registro.getApellidosEmpleado());
            procedimiento.setString(3, registro.getNombresEmpleado());
            procedimiento.setString(4, registro.getDireccionEmpleado());
            procedimiento.setString(5, registro.getTelefonoContacto());
            procedimiento.setString(6, registro.getGradoCocinero());
            procedimiento.setInt(7, registro.getCodigoTipoEmpleado());

            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void actualizar() {

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_EditarEmpleado(?,?,?,?,?,?,?)");

            Empleado registro = (Empleado) tblEmpleados.getSelectionModel().getSelectedItem();

            registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
            registro.setApellidosEmpleado(txtApellidosEmpleado.getText());
            registro.setNombresEmpleado(txtNombresEmpleado.getText());
            registro.setDireccionEmpleado(txtDireccionEmpleado.getText());
            registro.setTelefonoContacto(txtTelefonoContactoEmpleado.getText());
            registro.setGradoCocinero(txtGradoCocineroEmpleado.getText());

            procedimiento.setInt(1, registro.getCodigoEmpleado());
            procedimiento.setInt(2, registro.getNumeroEmpleado());
            procedimiento.setString(3, registro.getApellidosEmpleado());
            procedimiento.setString(4, registro.getNombresEmpleado());
            procedimiento.setString(5, registro.getDireccionEmpleado());
            procedimiento.setString(6, registro.getTelefonoContacto());
            procedimiento.setString(7, registro.getGradoCocinero());

            procedimiento.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblEmpleados.getSelectionModel().getSelectedItem() != null) {
                    desactivarBotones();
                    activarControles();
                    tblEmpleados.setDisable(true);
                    tipoDeOperacion = operaciones.EDITAR;
                } else {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento de la tabla.");
                }
                break;
            case NUEVO:
                limpiarControles();
                desactivarControles();
                activarBotones();
                tblEmpleados.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            case EDITAR:
                limpiarControles();
                desactivarControles();
                activarBotones();
                tblEmpleados.setDisable(false);
                tblEmpleados.getSelectionModel().clearSelection();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }

    public void eliminar() {
        if (tblEmpleados.getSelectionModel().getSelectedItem() != null) {
            int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro", "Eliminar Empleados", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (respuesta == JOptionPane.YES_OPTION) {
                try {

                    PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_EliminarEmpleado(?)");
                    procedimiento.setInt(1, ((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado());

                    procedimiento.execute();

                    listaEmpleado.remove(tblEmpleados.getSelectionModel().getSelectedIndex());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (respuesta == JOptionPane.NO_OPTION) {
                limpiarControles();
                tblEmpleados.getSelectionModel().clearSelection();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento de la tabla.");
        }

    }

    public void limpiarControles() {
        txtCodigoEmpleado.clear();
        txtNumeroEmpleado.clear();
        txtNombresEmpleado.clear();
        txtApellidosEmpleado.clear();
        txtDireccionEmpleado.clear();
        txtTelefonoContactoEmpleado.clear();
        txtGradoCocineroEmpleado.clear();
        cmbCodigoTipoEmpleado.getSelectionModel().select(null);
    }

    public void activarControles() {
        txtCodigoEmpleado.setEditable(false);
        txtNumeroEmpleado.setEditable(false);
        txtNombresEmpleado.setEditable(false);
        txtApellidosEmpleado.setEditable(false);
        txtDireccionEmpleado.setEditable(false);
        txtTelefonoContactoEmpleado.setEditable(false);
        txtGradoCocineroEmpleado.setEditable(false);
        cmbCodigoTipoEmpleado.setDisable(true);
    }

    public void desactivarControles() {
        txtCodigoEmpleado.setEditable(false);
        txtNumeroEmpleado.setEditable(false);
        txtNombresEmpleado.setEditable(false);
        txtApellidosEmpleado.setEditable(false);
        txtDireccionEmpleado.setEditable(false);
        txtTelefonoContactoEmpleado.setEditable(false);
        txtGradoCocineroEmpleado.setEditable(false);
        cmbCodigoTipoEmpleado.setDisable(true);
        cmbCodigoTipoEmpleado.setOpacity(100.00);
    }

    public boolean validarCampos() {
        //si los campos estan llenos retorna true, si hay un campo vacio retorna falso
        if (txtNumeroEmpleado.getText().isEmpty() || txtNombresEmpleado.getText().isEmpty() || txtApellidosEmpleado.getText().isEmpty()
                || txtDireccionEmpleado.getText().isEmpty() || txtTelefonoContactoEmpleado.getText().isEmpty() || txtGradoCocineroEmpleado.getText().isEmpty()
                || cmbCodigoTipoEmpleado.getSelectionModel() == null) {
            return false;
        }
        return true;
    }

    public boolean validarNumeroTelefono() {
        try {
            int telefono = Integer.valueOf(txtTelefonoContactoEmpleado.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Ingrese un valor numerico en el teléfono.");
            //e.printStackTrace();
            return true;
        }

        return false;
    }

    public boolean validarNumeroEmpleado() {
        try {
            int numeroEmpleado = Integer.valueOf(txtNumeroEmpleado.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Ingrese un valor numerico en el número de empleado.");
            return true;
        }

        return false;
    }

    public void activarBotones() {
        btnNuevo.setText("NUEVO");
        imgNuevo.setImage(new Image("/org/joshuarealiquez/image/addIcon.png"));
        btnEditar.setText("EDITAR");
        imgEditar.setImage(new Image("/org/joshuarealiquez/image/editIcon.png"));
        btnEliminar.setDisable(false);
        btnReporte.setDisable(false);

    }

    public void desactivarBotones() {
        btnNuevo.setText("GUARDAR");
        imgNuevo.setImage(new Image("/org/joshuarealiquez/image/saveIcon.png"));
        btnEditar.setText("CANCELAR");
        imgEditar.setImage(new Image("/org/joshuarealiquez/image/cancelIcon.png"));
        btnEliminar.setDisable(true);
        btnReporte.setDisable(true);
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

    public void back() {
        escenarioPrincipal.ventanaTipoEmpleado();
    }
}
