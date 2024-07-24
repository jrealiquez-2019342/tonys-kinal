package org.joshuarealiquez.controller;

import com.jfoenix.controls.JFXTimePicker;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.awt.Toolkit;
import javafx.fxml.Initializable;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.joshuarealiquez.main.Principal;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.joshuarealiquez.bean.Servicios;
import org.joshuarealiquez.bean.Empresa;
import org.joshuarealiquez.db.Conexion;
import org.joshuarealiquez.report.GenerarReporte;

public class ServiciosController implements Initializable {

    private Principal escenarioPrincipal;

    private enum operaciones {
        NUEVO, NINGUNO, ACTUALIZAR, ELIMINAR
    }

    private operaciones tipoDeOperacion = operaciones.NINGUNO;

    private ObservableList<Servicios> listaServicio;
    private ObservableList<Empresa> listaEmpresa;

    @FXML
    private ComboBox cmbCodigoEmpresa;

    @FXML
    private GridPane grpFecha;

    @FXML
    private JFXTimePicker tpkHoraServicio;

    @FXML
    private DatePicker fecha;

    @FXML
    private Button btnNuevo;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnReporte;

    @FXML
    private Button btnBack;

    @FXML
    private ImageView imgNuevo;

    @FXML
    private ImageView imgEditar;

    @FXML
    private ImageView imgEliminar;

    @FXML
    private ImageView imgReporte;

    @FXML
    private TableView tblServicios;

    @FXML
    private TableColumn colCodigoServicio;

    @FXML
    private TableColumn colFechaServicio;

    @FXML
    private TableColumn colHoraServicio;

    @FXML
    private TableColumn colTipoServicio;

    @FXML
    private TableColumn colLugarServicio;

    @FXML
    private TableColumn colTelefonoContacto;

    @FXML
    private TableColumn colCodigoEmpresa;

    @FXML
    private TextField txtCodigoServicio;

    @FXML
    private TextField txtTipoServicio;

    @FXML
    private TextField txtLugarServicio;

    @FXML
    private TextField txtTelefonoContacto;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //cargamos las propiedades de nuestra fecha
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(true);
        //agregar margen en el css
        fecha.getStylesheets().add("/org/joshuarealiquez/resource/TonysKinal.css");
        fecha.setDisable(true);

        //agregarmos el Date al GridPane
        grpFecha.add(fecha, 2, 1);

        cargarDatos();

        desactivarControles();
    }

    public void cargarDatos() {

        tblServicios.setItems(getServicio());
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicios, Integer>("codigoServicio"));
        //El date debe ser de Java.util no de sql.
        colFechaServicio.setCellValueFactory(new PropertyValueFactory<Servicios, Date>("fechaServicio"));
        colHoraServicio.setCellValueFactory(new PropertyValueFactory<Servicios, Time>("horaServicio"));
        colTipoServicio.setCellValueFactory(new PropertyValueFactory<Servicios, String>("tipoServicio"));

        colTelefonoContacto.setCellValueFactory(new PropertyValueFactory<Servicios, String>("telefonoContacto"));
        colLugarServicio.setCellValueFactory(new PropertyValueFactory<Servicios, String>("lugarServicio"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Servicios, Integer>("codigoEmpresa"));

        cmbCodigoEmpresa.setItems(getEmpresa());
    }

    public ObservableList<Servicios> getServicio() {
        ArrayList<Servicios> lista = new ArrayList<Servicios>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_ListarServicios;");
            ResultSet resultado = procedimiento.executeQuery();

            while (resultado.next()) {
                lista.add(new Servicios(resultado.getInt("codigoServicio"),
                        resultado.getDate("fechaServicio"),
                        resultado.getString("tipoServicio"),
                        resultado.getTime("horaServicio"),
                        resultado.getString("lugarServicio"),
                        resultado.getString("telefonoContacto"),
                        resultado.getInt("codigoEmpresa")
                )
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaServicio = FXCollections.observableArrayList(lista);
    }

    public ObservableList<Empresa> getEmpresa() {
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_ListarEmpresas;");
            ResultSet resultado = procedimiento.executeQuery();

            while (resultado.next()) {
                lista.add(new Empresa(resultado.getInt("codigoEmpresa"),
                        resultado.getString("nombreEmpresa"),
                        resultado.getString("direccion"),
                        resultado.getString("telefono"))
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaEmpresa = FXCollections.observableArrayList(lista);
    }

    public void seleccionarElementos() {
        if (tblServicios.getSelectionModel().getSelectedItem() != null) {
            txtCodigoServicio.setText(String.valueOf(((Servicios) tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio()));
            txtTipoServicio.setText(((Servicios) tblServicios.getSelectionModel().getSelectedItem()).getTipoServicio());
            txtLugarServicio.setText(((Servicios) tblServicios.getSelectionModel().getSelectedItem()).getLugarServicio());
            txtTelefonoContacto.setText(((Servicios) tblServicios.getSelectionModel().getSelectedItem()).getTelefonoContacto());
            cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Servicios) tblServicios.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
            fecha.selectedDateProperty().set(((Servicios) tblServicios.getSelectionModel().getSelectedItem()).getFechaServicio());
            tpkHoraServicio.setValue((((Servicios) tblServicios.getSelectionModel().getSelectedItem()).getHoraServicio()).toLocalTime());
        }
    }

    public Empresa buscarEmpresa(int codigoEmpresa) {
        Empresa resultado = null;

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_BuscarEmpresa(?)");
            procedimiento.setInt(1, codigoEmpresa);
            ResultSet registro = procedimiento.executeQuery();

            while (registro.next()) {
                resultado = new Empresa(
                        registro.getInt("codigoEmpresa"),
                        registro.getString("nombreEmpresa"),
                        registro.getString("direccion"),
                        registro.getString("telefono")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
    }

    public void nuevo() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                limpiarControles();
                activarControles();
                desactivarBotones();
                tblServicios.getSelectionModel().clearSelection();
                tblServicios.setDisable(true);
                tipoDeOperacion = operaciones.NUEVO;
                break;
            case NUEVO:

                if (txtTipoServicio.getText().isEmpty() || txtLugarServicio.getText().isEmpty() || txtTelefonoContacto.getText().isEmpty()
                        || fecha.getSelectedDate() == null || tpkHoraServicio.getValue() == null || cmbCodigoEmpresa.getSelectionModel().getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null, "Hay campos vacios en el formulario");
                } else {

                    try {
                        int numero = Integer.valueOf(txtTelefonoContacto.getText());

                        if (txtTelefonoContacto.getText().length() > 8) {
                            JOptionPane.showMessageDialog(null, "Solo se aceptan 8 carácteres en el teléfono.");
                        } else {
                            guardar();
                            limpiarControles();
                            activarBotones();
                            desactivarControles();
                            tblServicios.setDisable(false);
                            tipoDeOperacion = operaciones.NINGUNO;
                            cargarDatos();
                        }

                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Ingrese un valor numerico en el teléfono.");
                    }
                }

                break;
            case ACTUALIZAR:
                if (validarCampos()) {
                    actualizar();
                    limpiarControles();
                    activarBotones();
                    desactivarControles();
                    tblServicios.setDisable(false);
                    tblServicios.getSelectionModel().clearSelection();
                    tipoDeOperacion = operaciones.NINGUNO;
                    cargarDatos();
                } else {
                    JOptionPane.showMessageDialog(null, "Hay campos vacios en el formulario");
                }

                break;
        }
    }

    public boolean validarCampos() {
        if (txtTipoServicio.getText().isEmpty() || txtLugarServicio.getText().isEmpty() || txtTelefonoContacto.getText().isEmpty()
                || fecha.getSelectedDate() == null || tpkHoraServicio.getValue() == null || cmbCodigoEmpresa.getSelectionModel().getSelectedItem() == null) {
            return false;
        }
        return true;
    }

    public void actualizar() {

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_EditarServicio(?,?,?,?,?,?)");

            Servicios registro = (Servicios) tblServicios.getSelectionModel().getSelectedItem();

            registro.setFechaServicio(fecha.getSelectedDate());
            registro.setTipoServicio(txtTipoServicio.getText());
            registro.setHoraServicio(Time.valueOf(tpkHoraServicio.getValue()));
            registro.setLugarServicio(txtLugarServicio.getText());
            registro.setTelefonoContacto(txtTelefonoContacto.getText());

            procedimiento.setInt(1, registro.getCodigoServicio());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaServicio().getTime()));
            procedimiento.setString(3, registro.getTipoServicio());
            procedimiento.setTime(4, registro.getHoraServicio());
            procedimiento.setString(5, registro.getLugarServicio());
            procedimiento.setString(6, registro.getTelefonoContacto());

            procedimiento.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void guardar() {
        Servicios registro = new Servicios();

        registro.setFechaServicio(fecha.getSelectedDate());
        registro.setHoraServicio(Time.valueOf(tpkHoraServicio.getValue()));
        registro.setLugarServicio(txtLugarServicio.getText());
        registro.setTelefonoContacto(txtTelefonoContacto.getText());
        registro.setTipoServicio(txtTipoServicio.getText());
        registro.setCodigoEmpresa(((Empresa) cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_AgregarServicio(?,?,?,?,?,?)");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaServicio().getTime()));
            procedimiento.setString(2, registro.getTipoServicio());
            procedimiento.setTime(3, registro.getHoraServicio());
            procedimiento.setString(4, registro.getLugarServicio());
            procedimiento.setString(5, registro.getTelefonoContacto());
            procedimiento.setInt(6, registro.getCodigoEmpresa());

            procedimiento.execute();

            listaServicio.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblServicios.getSelectionModel().getSelectedItem() != null) {
                    desactivarBotones();
                    activarControles();
                    tblServicios.setDisable(true);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                } else {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento de la tabla.");
                }
                break;
            case NUEVO:
                limpiarControles();
                activarBotones();
                desactivarControles();
                tblServicios.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            case ACTUALIZAR:
                activarBotones();
                limpiarControles();
                desactivarControles();
                tblServicios.setDisable(false);
                tblServicios.getSelectionModel().clearSelection();

                tipoDeOperacion = operaciones.NINGUNO;
                break;

        }
    }

    public void eliminar() {

        if (tblServicios.getSelectionModel().getSelectedItem() != null) {
            Toolkit.getDefaultToolkit().beep();
            int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Servicio", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (respuesta == JOptionPane.YES_OPTION) {
                try {
                    PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_EliminarServicio(?)");
                    procedimiento.setInt(1, ((Servicios) tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio());
                    procedimiento.execute();
                    listaServicio.remove(tblServicios.getSelectionModel().getSelectedIndex());
                    limpiarControles();
                    tblServicios.getSelectionModel().clearSelection();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (respuesta == JOptionPane.NO_OPTION) {
                limpiarControles();
                tblServicios.getSelectionModel().clearSelection();
            }

        } else {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento de la tabla.");
        }
    }

    public void reporte() {
        if (tblServicios.getSelectionModel().getSelectedItem() != null) {
            Map parametros = new HashMap();
            int codEmpresa = Integer.valueOf(((Empresa) cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
            parametros.put("codEmpresa", codEmpresa);
            parametros.put("imagenEncabezado", GenerarReporte.class.getResource("/org/joshuarealiquez/image/encabezado.png"));
            parametros.put("imagenPie", GenerarReporte.class.getResource("/org/joshuarealiquez/image/piedePagina.png"));
            parametros.put("dirSubreportePlatos", GenerarReporte.class.getResource("/org/joshuarealiquez/report/SubReporteServicioPlatos.jasper"));
            parametros.put("dirSubreporteProductos", GenerarReporte.class.getResource("/org/joshuarealiquez/report/SubReporteServicioProductos.jasper"));
            parametros.put("dirSubreporteEmpleados", GenerarReporte.class.getResource("/org/joshuarealiquez/report/SubReporteServicioEmpleados.jasper"));
            GenerarReporte.mostrarReporte("reporteServicios.jasper", "Reporte Servicio", parametros);
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento de la tabla.");
        }

    }

    public void limpiarControles() {
        txtCodigoServicio.clear();
        txtTipoServicio.clear();
        txtLugarServicio.clear();
        txtTelefonoContacto.clear();
        fecha.setSelectedDate(null);
        cmbCodigoEmpresa.getSelectionModel().select(null);
        tpkHoraServicio.setValue(null);
    }

    public void activarBotones() {
        btnEliminar.setDisable(false);
        btnReporte.setDisable(false);

        btnNuevo.setText("NUEVO");
        imgNuevo.setImage(new Image("/org/joshuarealiquez/image/addIcon.png"));
        btnEditar.setText("EDITAR");
        imgEditar.setImage(new Image("/org/joshuarealiquez/image/editIcon.png"));
    }

    public void desactivarBotones() {
        btnEliminar.setDisable(true);
        btnReporte.setDisable(true);

        btnNuevo.setText("GUARDAR");
        imgNuevo.setImage(new Image("/org/joshuarealiquez/image/saveIcon.png"));
        btnEditar.setText("CANCELAR");
        imgEditar.setImage(new Image("/org/joshuarealiquez/image/cancelIcon.png"));

    }

    public void activarControles() {
        txtCodigoServicio.setEditable(false);
        txtTipoServicio.setEditable(true);
        txtLugarServicio.setEditable(true);
        txtTelefonoContacto.setEditable(true);
        fecha.setDisable(false);
        tpkHoraServicio.setDisable(false);
        cmbCodigoEmpresa.setDisable(false);
    }

    public void desactivarControles() {
        txtCodigoServicio.setEditable(false);
        txtTipoServicio.setEditable(false);
        txtLugarServicio.setEditable(false);
        txtTelefonoContacto.setEditable(false);
        fecha.setDisable(true);
        fecha.setOpacity(100.00);
        tpkHoraServicio.setDisable(true);
        tpkHoraServicio.setOpacity(100.00);
        cmbCodigoEmpresa.setDisable(true);
        cmbCodigoEmpresa.setOpacity(100.00);
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    //Funcion a los botones
    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }

    public void back() {
        escenarioPrincipal.ventanaEmpresa();
    }

}
