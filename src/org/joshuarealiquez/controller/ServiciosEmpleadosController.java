package org.joshuarealiquez.controller;

import com.jfoenix.controls.JFXTimePicker;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

import javafx.scene.control.ComboBox;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import javafx.collections.FXCollections;
import javafx.scene.control.TextField;
import org.joshuarealiquez.main.Principal;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.joshuarealiquez.bean.Empleado;
import org.joshuarealiquez.bean.ServicioEmpleado;
import org.joshuarealiquez.bean.Servicios;
import org.joshuarealiquez.db.Conexion;

public class ServiciosEmpleadosController implements Initializable {

    Principal escenarioPrincipal;

    private enum operaciones {
        NUEVO, ACTUALIZAR, NINGUNO;
    }

    private operaciones tipoDeOperacion = operaciones.NINGUNO;

    private ObservableList<Servicios> listaServicio;

    private ObservableList<Empleado> listaEmpleado;

    private ObservableList<ServicioEmpleado> listaServicioEmpleado;

    @FXML
    private Button btnNuevo;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnReporte;

    @FXML
    private TableView tblServiciosEmpleados;

    @FXML
    private TableColumn colServiciosCodigoServicio;

    @FXML
    private TableColumn colCodigoServicio;

    @FXML
    private TableColumn colCodigoEmpleado;

    @FXML
    private TableColumn colFechaEvento;

    @FXML
    private TableColumn colHoraEvento;

    @FXML
    private TableColumn colLugarEvento;

    @FXML
    private ComboBox cmbCodigoServicio;

    @FXML
    private ComboBox cmbCodigoEmpleado;

    @FXML
    private TextField txtServiciosCodigoServicio;

    @FXML
    private TextField txtLugarEvento;

    @FXML
    private ImageView imgNuevo;

    @FXML
    private ImageView imgEditar;

    @FXML
    private GridPane grpFecha;

    @FXML
    private JFXTimePicker tpkHoraEvento;

    @FXML
    private DatePicker fecha;

    @Override
    public void initialize(URL location, ResourceBundle bundle) {
        cargarDatos();
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().setTodayButtonText("Today");
        fecha.getCalendarView().setShowWeeks(true);
        fecha.getStylesheets().add("/org/joshuarealiquez/resource/TonysKinal.css");
        fecha.setDisable(true);

        grpFecha.add(fecha, 0, 3);

        desactivarControles();
    }

    public void cargarDatos() {
        tblServiciosEmpleados.setItems(getListaServiciosEmpleados());
        colServiciosCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServicioEmpleado, Integer>("Servicios_codigoServicio"));
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServicioEmpleado, Integer>("codigoServicio"));
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<ServicioEmpleado, Integer>("codigoEmpleado"));
        colFechaEvento.setCellValueFactory(new PropertyValueFactory<ServicioEmpleado, Date>("fechaEvento"));
        colHoraEvento.setCellValueFactory(new PropertyValueFactory<ServicioEmpleado, Time>("horaEvento"));
        colLugarEvento.setCellValueFactory(new PropertyValueFactory<ServicioEmpleado, String>("lugarEvento"));

        cmbCodigoServicio.setItems(getServicios());
        cmbCodigoEmpleado.setItems(getEmpleados());
    }
    
    public void seleccionarElemento() {
        if(tblServiciosEmpleados.getSelectionModel().getSelectedItem() != null){
            cmbCodigoEmpleado.getSelectionModel().select(buscarEmpleado((((ServicioEmpleado) tblServiciosEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado())));
            cmbCodigoServicio.getSelectionModel().select(buscarServicio(((ServicioEmpleado)tblServiciosEmpleados.getSelectionModel().getSelectedItem()).getCodigoServicio()));
            txtServiciosCodigoServicio.setText(String.valueOf(((ServicioEmpleado)tblServiciosEmpleados.getSelectionModel().getSelectedItem()).getServicios_codigoServicio()));
            txtLugarEvento.setText(((ServicioEmpleado)tblServiciosEmpleados.getSelectionModel().getSelectedItem()).getLugarEvento());
            fecha.selectedDateProperty().set(((ServicioEmpleado)tblServiciosEmpleados.getSelectionModel().getSelectedItem()).getFechaEvento());
            tpkHoraEvento.setValue(((ServicioEmpleado)tblServiciosEmpleados.getSelectionModel().getSelectedItem()).getHoraEvento().toLocalTime());
        }
        
    }

    public ObservableList<ServicioEmpleado> getListaServiciosEmpleados() {
        ArrayList<ServicioEmpleado> lista = new ArrayList<ServicioEmpleado>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_ListarServiciosHasEmpleados");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new ServicioEmpleado(resultado.getInt("Servicios_codigoServicio"),
                        resultado.getInt("codigoServicio"), resultado.getInt("codigoEmpleado"),
                        resultado.getDate("fechaEvento"), resultado.getTime("horaEvento"),
                        resultado.getString("lugarEvento"))
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaServicioEmpleado = FXCollections.observableArrayList(lista);
    }

    public ObservableList<Servicios> getServicios() {
        ArrayList<Servicios> lista = new ArrayList<Servicios>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_ListarServicios");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Servicios(resultado.getInt("codigoServicio"),
                        resultado.getDate("fechaServicio"), resultado.getString("tipoServicio"),
                        resultado.getTime("horaServicio"), resultado.getString("lugarServicio"),
                        resultado.getString("telefonoContacto"), resultado.getInt("codigoEmpresa"))
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaServicio = FXCollections.observableArrayList(lista);
    }

    public ObservableList<Empleado> getEmpleados() {
        ArrayList<Empleado> lista = new ArrayList<Empleado>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_ListarEmpleados");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Empleado(resultado.getInt("codigoEmpleado"),
                        resultado.getInt("numeroEmpleado"), resultado.getString("apellidosEmpleado"),
                        resultado.getString("nombresEmpleado"), resultado.getString("direccionEmpleado"),
                        resultado.getString("telefonoContacto"), resultado.getString("gradoCocinero"),
                        resultado.getInt("codigoTipoEmpleado"))
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaEmpleado = FXCollections.observableArrayList(lista);
    }

    public void nuevo() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                activarControles();
                desactivarBotones();
                limpiarControles();
                tblServiciosEmpleados.getSelectionModel().clearSelection();
                tblServiciosEmpleados.setDisable(true);
                tipoDeOperacion = operaciones.NUEVO;
                break;
            case NUEVO:
                if (validarCampos()) {
                    guardar();
                    desactivarControles();
                    limpiarControles();
                    activarBotones();
                    tblServiciosEmpleados.setDisable(false);
                    tipoDeOperacion = operaciones.NINGUNO;
                    cargarDatos();
                } else {
                    JOptionPane.showMessageDialog(null, "Hay campos vacios en el formulario.");
                }

                break;
            case ACTUALIZAR:
                if (validarCampos()) {
                    actualizar();
                    limpiarControles();
                    activarBotones();
                    desactivarControles();
                    tblServiciosEmpleados.setDisable(false);
                    tblServiciosEmpleados.getSelectionModel().clearSelection();
                    tipoDeOperacion = operaciones.NINGUNO;
                    cargarDatos();
                } else {
                    JOptionPane.showMessageDialog(null, "Hay campos vacios en el formulario.");
                }

                break;
        }
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_EditarServicioHasEmpleado(?,?,?,?)");

            ServicioEmpleado registro = (ServicioEmpleado) tblServiciosEmpleados.getSelectionModel().getSelectedItem();
            registro.setFechaEvento(fecha.getSelectedDate());
            registro.setHoraEvento(Time.valueOf(tpkHoraEvento.getValue()));
            registro.setLugarEvento(txtLugarEvento.getText());

            procedimiento.setInt(1, Integer.valueOf(txtServiciosCodigoServicio.getText()));
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaEvento().getTime()));
            procedimiento.setTime(3, registro.getHoraEvento());
            procedimiento.setString(4, registro.getLugarEvento());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void guardar() {
        ServicioEmpleado registro = new ServicioEmpleado();
        
        registro.setServicios_codigoServicio(Integer.parseInt(txtServiciosCodigoServicio.getText()));
        registro.setCodigoServicio(((Servicios) cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
        registro.setCodigoEmpleado(((Empleado) cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
        registro.setFechaEvento(fecha.getSelectedDate());
        registro.setHoraEvento(Time.valueOf(tpkHoraEvento.getValue()));
        registro.setLugarEvento(txtLugarEvento.getText());

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_AgregarServicioHasEmpleado(?,?,?,?,?,?)");
            procedimiento.setInt(1, registro.getServicios_codigoServicio());
            procedimiento.setInt(2, registro.getCodigoServicio());
            procedimiento.setInt(3, registro.getCodigoEmpleado());
            procedimiento.setDate(4, new java.sql.Date(registro.getFechaEvento().getTime()));
            procedimiento.setTime(5, registro.getHoraEvento());
            procedimiento.setString(6, registro.getLugarEvento());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblServiciosEmpleados.getSelectionModel().getSelectedItem() != null) {
                    activarControles();
                    cmbCodigoEmpleado.setDisable(true);
                    cmbCodigoServicio.setDisable(true);
                    txtServiciosCodigoServicio.setDisable(true);
                    desactivarBotones();
                    tblServiciosEmpleados.getSelectionModel().getSelectedItem();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento de la tabla.");
                }
                break;
            case NUEVO:
                desactivarControles();
                limpiarControles();
                activarBotones();
                tblServiciosEmpleados.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                activarBotones();
                tblServiciosEmpleados.getSelectionModel().clearSelection();
                tblServiciosEmpleados.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }

    public boolean validarCampos() {
        if (cmbCodigoServicio.getSelectionModel().getSelectedItem() == null || cmbCodigoEmpleado.getSelectionModel().getSelectedItem() == null
                || fecha.getSelectedDate() == null || tpkHoraEvento.getValue() == null || txtLugarEvento.getText().isEmpty()) {
            return false;
        }
        return true;
    }

    

    public Empleado buscarEmpleado(int codigoEmpleado) {
        Empleado registro = null;

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_BuscarEmpleado(?)");
            procedimiento.setInt(1, codigoEmpleado);
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                registro = new Empleado(resultado.getInt("codigoEmpleado"),
                        resultado.getInt("numeroEmpleado"), resultado.getString("apellidosEmpleado"), 
                        resultado.getString("nombresEmpleado"), resultado.getString("direccionEmpleado"),
                        resultado.getString("telefonoContacto"), resultado.getString("gradoCocinero"),
                        resultado.getInt("codigoTipoEmpleado"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return registro;
    }
    
    public Servicios buscarServicio(int codigoServicio){
        Servicios registro = null;
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_BuscarServicio(?)");
            procedimiento.setInt(1, codigoServicio);
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                registro = new Servicios(resultado.getInt("codigoServicio"),
                    resultado.getDate("fechaServicio"), resultado.getString("tipoServicio"),
                    resultado.getTime("horaServicio"), resultado.getString("lugarServicio"),
                    resultado.getString("telefonoContacto"), resultado.getInt("codigoEmpresa")
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return registro;
    }

    public void activarControles() {
        txtServiciosCodigoServicio.setEditable(true);
        txtLugarEvento.setEditable(true);
        fecha.setDisable(false);
        tpkHoraEvento.setDisable(false);
        cmbCodigoServicio.setDisable(false);
        cmbCodigoEmpleado.setDisable(false);
    }

    public void desactivarControles() {
        txtServiciosCodigoServicio.setEditable(false);
        txtLugarEvento.setEditable(false);
        fecha.setDisable(true);
        fecha.setOpacity(100.00);
        tpkHoraEvento.setDisable(true);
        tpkHoraEvento.setOpacity(100.00);
        cmbCodigoServicio.setDisable(true);
        cmbCodigoServicio.setOpacity(100.00);
        cmbCodigoEmpleado.setDisable(true);
        cmbCodigoEmpleado.setOpacity(100.00);
    }

    public void limpiarControles() {
        txtServiciosCodigoServicio.clear();
        txtLugarEvento.clear();
        cmbCodigoServicio.getSelectionModel().select(null);
        cmbCodigoEmpleado.getSelectionModel().select(null);
        fecha.setSelectedDate(null);
        tpkHoraEvento.setValue(null);
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
        btnEliminar.setDisable(true);
        btnReporte.setDisable(true);
        btnNuevo.setText("GUARDAR");
        btnEditar.setText("CANCELAR");
        imgNuevo.setImage(new Image("/org/joshuarealiquez/image/saveIcon.png"));
        imgEditar.setImage(new Image("/org/joshuarealiquez/image/cancelIcon.png"));
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

}
