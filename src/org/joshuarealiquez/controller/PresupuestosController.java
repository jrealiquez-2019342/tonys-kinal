package org.joshuarealiquez.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.joshuarealiquez.main.Principal;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.joshuarealiquez.bean.Empresa;
import org.joshuarealiquez.bean.Presupuestos;
import org.joshuarealiquez.db.Conexion;
import org.joshuarealiquez.report.GenerarReporte;

public class PresupuestosController implements Initializable {

    private Principal escenarioPrincipal;

    private enum operaciones {
        NUEVO, ACTUALIZAR, NINGUNO, ELIMINAR
    }

    private operaciones tipoDeOperacion = operaciones.NINGUNO;

    private ObservableList<Presupuestos> listaPresupuesto;
    private ObservableList<Empresa> listaEmpresa;

    @FXML
    private DatePicker fecha;

    @FXML
    private Button btnHome;

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
    private TextField txtCodigoPresupuesto;

    @FXML
    private TextField txtCantidadPresupuesto;

    @FXML
    private ComboBox cmbCodigoEmpresa;

    @FXML
    private GridPane grpFecha;

    @FXML
    private TableColumn colCodigoPresupuesto;

    @FXML
    private TableColumn colFechaSolicitud;

    @FXML
    private TableColumn colCantidadPresupuesto;

    @FXML
    private TableColumn colCodigoEmpresa;

    @FXML
    private TableView tblPresupuestos;

    @FXML
    private ImageView imgNuevo;

    @FXML
    private ImageView imgEditar;

    @FXML
    private ImageView imgEliminar;

    @FXML
    private ImageView imgReporte;

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        //el anio tiene 54 semanas
        fecha.getCalendarView().setShowWeeks(true);
        grpFecha.add(fecha, 0, 3);//gridpane

        fecha.getStylesheets().add("/org/joshuarealiquez/resource/TonysKinal.css");
        fecha.setDisable(true);
        cmbCodigoEmpresa.setDisable(true);
        cargarDatos();
    }

    public void cargarDatos() {
        tblPresupuestos.setItems(getPresupuesto());
        colCodigoPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuestos, Integer>("codigoPresupuesto"));
        colFechaSolicitud.setCellValueFactory(new PropertyValueFactory<Presupuestos, Date>("fechaSolicitud"));
        colCantidadPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuestos, Double>("cantidadPresupuesto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Presupuestos, Integer>("codigoEmpresa"));

        cmbCodigoEmpresa.setItems(getEmpresa());
    }

    public ObservableList<Presupuestos> getPresupuesto() {
        ArrayList<Presupuestos> lista = new ArrayList<Presupuestos>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_ListarPresupuestos");
            ResultSet resultado = procedimiento.executeQuery();

            while (resultado.next()) {
                lista.add(new Presupuestos(resultado.getInt("codigoPresupuesto"),
                        resultado.getDate("fechaSolicitud"),
                        resultado.getDouble("cantidadPresupuesto"),
                        resultado.getInt("codigoEmpresa")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaPresupuesto = FXCollections.observableArrayList(lista);
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

    public void nuevo() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                limpiarControles();
                desactivarBotones();
                activarControles();
                tipoDeOperacion = operaciones.NUEVO;
                break;
            case NUEVO:

                //JOptionPane.showMessageDialog(null, "Opcion aun no disponible");
                if (txtCantidadPresupuesto.getText().isEmpty() || fecha.getSelectedDate() == null || cmbCodigoEmpresa.getSelectionModel().getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null, "Hay campos vacios en el formulario");
                } else {
                    guardar();

                    limpiarControles();
                    desactivarControles();
                    activarBotones();

                    cargarDatos();

                    tipoDeOperacion = operaciones.NINGUNO;
                }

                break;
            case ACTUALIZAR:

                if (txtCantidadPresupuesto.getText().isEmpty() || fecha.getSelectedDate() == null || cmbCodigoEmpresa.getSelectionModel().getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null, "Hay campos vacios en el formulario");
                } else {
                    actualizar();

                    limpiarControles();
                    desactivarControles();
                    activarBotones();
                    tblPresupuestos.getSelectionModel().clearSelection();
                    cargarDatos();

                    tipoDeOperacion = operaciones.NINGUNO;
                }

                break;
        }
    }

    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblPresupuestos.getSelectionModel().getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento de la tabla.");
                } else {
                    activarControles();
                    desactivarBotones();

                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }
                break;
            case NUEVO:

                limpiarControles();
                desactivarControles();
                activarBotones();
                tblPresupuestos.getSelectionModel().clearSelection();
                //cmbCodigoEmpresa.getSelectionModel().select(-1);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            case ACTUALIZAR:

                desactivarControles();
                activarBotones();
                limpiarControles();
                //cmbCodigoEmpresa.getSelectionModel().select(-1);
                cmbCodigoEmpresa.setDisable(true);
                tblPresupuestos.getSelectionModel().clearSelection();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }

    public void guardar() {
        Presupuestos registro = new Presupuestos();
        registro.setFechaSolicitud(fecha.getSelectedDate());
        registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
        registro.setCodigoEmpresa(((Empresa) cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_AgregarPresupuesto(?,?,?)");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaSolicitud().getTime()));
            procedimiento.setDouble(2, registro.getCantidadPresupuesto());
            procedimiento.setInt(3, registro.getCodigoEmpresa());

            procedimiento.execute();

            listaPresupuesto.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_EditarPresupuesto(?,?,?)");

            Presupuestos registro = (Presupuestos) tblPresupuestos.getSelectionModel().getSelectedItem();

            registro.setFechaSolicitud(fecha.getSelectedDate());
            registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));

            procedimiento.setInt(1, registro.getCodigoPresupuesto());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaSolicitud().getTime()));
            procedimiento.setDouble(3, registro.getCantidadPresupuesto());

            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblPresupuestos.getSelectionModel().getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento de la tabla.");
                } else {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Presupuesto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_EliminarPresupuesto(?)");
                            procedimiento.setInt(1, ((Presupuestos) tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoPresupuesto());

                            listaPresupuesto.remove(tblPresupuestos.getSelectionModel().getSelectedItem());
                            limpiarControles();
                            tblPresupuestos.getSelectionModel().clearSelection();
                            procedimiento.execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (respuesta == JOptionPane.NO_OPTION) {
                        limpiarControles();
                        tblPresupuestos.getSelectionModel().clearSelection();
                    }
                }
                break;
        }
    }

    public void limpiarControles() {
        txtCodigoPresupuesto.clear();
        txtCantidadPresupuesto.clear();
        fecha.setSelectedDate(null);
        cmbCodigoEmpresa.getSelectionModel().select(null);
    }

    public void activarControles() {
        txtCodigoPresupuesto.setEditable(false);
        txtCantidadPresupuesto.setEditable(true);
        fecha.setDisable(false);
        cmbCodigoEmpresa.setDisable(false);
    }

    public void desactivarControles() {
        txtCantidadPresupuesto.setEditable(false);
        txtCodigoPresupuesto.setEditable(false);
        fecha.setDisable(true);
        fecha.setOpacity(100.00);
        cmbCodigoEmpresa.setDisable(true);
        cmbCodigoEmpresa.setOpacity(100.00);
    }

    public void activarBotones() {
        btnNuevo.setText("NUEVO");
        btnEditar.setText("EDITAR");
        btnEliminar.setDisable(false);
        btnReporte.setDisable(false);
        imgNuevo.setImage(new Image("/org/joshuarealiquez/image/addIcon.png"));
        imgEditar.setImage(new Image("/org/joshuarealiquez/image/editIcon.png"));

    }

    public void desactivarBotones() {
        btnNuevo.setText("GUARDAR");
        btnEditar.setText("CANCELAR");
        imgNuevo.setImage(new Image("/org/joshuarealiquez/image/saveIcon.png"));
        imgEditar.setImage(new Image("/org/joshuarealiquez/image/cancelIcon.png"));

        btnEliminar.setDisable(true);
        btnReporte.setDisable(true);
    }

    public void seleccionarElementos() {
        if (tblPresupuestos.getSelectionModel().getSelectedItem() != null) {
            txtCodigoPresupuesto.setText(String.valueOf(((Presupuestos) tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoPresupuesto()));
            fecha.selectedDateProperty().set(((Presupuestos) tblPresupuestos.getSelectionModel().getSelectedItem()).getFechaSolicitud());
            txtCantidadPresupuesto.setText(String.valueOf(((Presupuestos) tblPresupuestos.getSelectionModel().getSelectedItem()).getCantidadPresupuesto()));
            cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Presupuestos) tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
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

    public void reporte() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                imprimirReporte();
                break;
        }
    }

    public void imprimirReporte() {
        if (tblPresupuestos.getSelectionModel().getSelectedItem() != null) {
            Map parametros = new HashMap();
            int codEmpresa = Integer.valueOf(((Empresa) cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
            parametros.put("codEmpresa", codEmpresa);
            parametros.put("imagenPie", GenerarReporte.class.getResource("/org/joshuarealiquez/image/piedePagina.png"));
            parametros.put("imagenEncabezado", GenerarReporte.class.getResource("/org/joshuarealiquez/image/encabezado.png"));
            parametros.put("dirBackgroundDatosEmpresa", GenerarReporte.class.getResource("/org/joshuarealiquez/image/bgDatosEmpresa.png"));
            parametros.put("subconsulta1", GenerarReporte.class.getResource("/org/joshuarealiquez/report/SubReportePresupuesto.jasper"));
            GenerarReporte.mostrarReporte("ReportePresupuesto.jasper", "Reporte de Presupuesto", parametros);
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento de la tabla.");
        }

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
        escenarioPrincipal.ventanaEmpresa();
    }
}
