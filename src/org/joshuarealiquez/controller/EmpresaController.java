package org.joshuarealiquez.controller;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.joshuarealiquez.bean.Empresa;
import org.joshuarealiquez.db.Conexion;
import org.joshuarealiquez.main.Principal;
import org.joshuarealiquez.report.GenerarReporte;

public class EmpresaController implements Initializable {

    private Principal escenarioPrincipal;

    private enum operaciones {
        NUEVO, GUARDAR, GUARDARNUEVO, ELIMINAR, ACTUALIZAR, ACTUALIZAREDITAR, CANCELAR, NINGUNO
    };
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Empresa> listaEmpresa;

    @FXML
    private TextField txtCodigoEmpresa;

    @FXML
    private TextField txtNombreEmpresa;

    @FXML
    private TextField txtDireccionEmpresa;

    @FXML
    private TextField txtTelefonoEmpresa;

    @FXML
    private TableView tblEmpresas;

    @FXML
    private TableColumn colCodigoEmpresa;

    @FXML
    private TableColumn colNombreEmpresa;

    @FXML
    private TableColumn colDireccionEmpresa;

    @FXML
    private TableColumn colTelefonoEmpresa;

    @FXML
    private Button btnNuevo;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnReporte;

    @FXML
    private Button btnPresupuestos;

    @FXML
    private Button btnServicios;

    @FXML
    private ImageView imgNuevo;

    @FXML
    private ImageView imgEditar;

    @FXML
    private ImageView imgEliminar;

    @FXML
    private ImageView imgReporte;

    @FXML
    private Label lbCampoVacioDir;

    @FXML
    private Label lbCampoVacioNom;

    @FXML
    private Label lbCampoVacioTel;

    //Primero que se ejecuta en la clase
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        volverInvisible();

        colTelefonoEmpresa.setStyle("-fx-alignment: CENTER-RIGHT;");
    }

    public void cargarDatos() {
        tblEmpresas.setItems(getEmpresa());
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, Integer>("codigoEmpresa"));
        colNombreEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("nombreEmpresa"));
        colDireccionEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("direccion"));
        colTelefonoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("telefono"));
    }

    public void volverInvisible() {
        lbCampoVacioDir.setVisible(false);
        lbCampoVacioNom.setVisible(false);
        lbCampoVacioTel.setVisible(false);
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
                activarControles();
                tblEmpresas.getSelectionModel().clearSelection();
                btnNuevo.setText("GUARDAR");
                btnEditar.setText("CANCELAR");
                btnEliminar.setVisible(false);
                btnReporte.setVisible(false);

                imgNuevo.setImage(new Image("/org/joshuarealiquez/image/saveIcon.png"));
                imgEditar.setImage(new Image("/org/joshuarealiquez/image/cancelIcon.png"));

                tblEmpresas.setDisable(true);
                tblEmpresas.getSelectionModel().clearSelection();

                tipoDeOperacion = operaciones.GUARDARNUEVO;
                break;
            case GUARDARNUEVO:

                if (txtNombreEmpresa.getText().isEmpty() || txtDireccionEmpresa.getText().isEmpty() || txtTelefonoEmpresa.getText().isEmpty()) {
                    if (txtNombreEmpresa.getText().isEmpty()) {
                        lbCampoVacioNom.setVisible(true);
                    }
                    if (txtDireccionEmpresa.getText().isEmpty()) {
                        lbCampoVacioDir.setVisible(true);
                    }
                    if (txtTelefonoEmpresa.getText().isEmpty()) {
                        lbCampoVacioTel.setVisible(true);
                    }

                } else {
                    try {
                        int numero = Integer.valueOf(txtTelefonoEmpresa.getText());
                        if (validarTamanioCampos()) {
                            guardar();
                            limpiarControles();
                            ocultarErrores();
                            desactivarControles();
                            btnNuevo.setText("NUEVO");
                            btnEditar.setText("EDITAR");
                            btnEliminar.setVisible(true);
                            btnReporte.setVisible(true);
                            imgNuevo.setImage(new Image("/org/joshuarealiquez/image/addIcon.png"));
                            imgEditar.setImage(new Image("/org/joshuarealiquez/image/editIcon.png"));
                            tblEmpresas.setDisable(false);
                            tipoDeOperacion = operaciones.NINGUNO;
                            cargarDatos();
                        } else {
                            if (txtTelefonoEmpresa.getText().length() > 8) {
                                JOptionPane.showMessageDialog(null, "Solo se aceptan 8 carácteres en el teléfono.");
                            }
                            if (txtNombreEmpresa.getText().length() > 150) {
                                JOptionPane.showMessageDialog(null, "Solo se aceptan 150 carácteres en el nombre de la empresa.");
                            }
                            if (txtDireccionEmpresa.getText().length() > 150) {
                                JOptionPane.showMessageDialog(null, "Solo se aceptan 150 carácteres en la dirección de la empresa.");
                            }
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Ingrese un valor numerico en el teléfono.");
                    }

                }
                break;
            case ACTUALIZAREDITAR:
                //falta capturar que en la actualizacion de informacion no hayan campos vacios
                if (validarCampos()) {
                    actualizar();
                    limpiarControles();
                    desactivarControles();
                    btnReporte.setDisable(false);
                    btnEliminar.setDisable(false);
                    btnNuevo.setText("NUEVO");
                    btnEditar.setText("EDITAR");
                    imgEditar.setImage(new Image("/org/joshuarealiquez/image/editIcon.png"));
                    imgNuevo.setImage(new Image("/org/joshuarealiquez/image/addIcon.png"));
                    tipoDeOperacion = operaciones.NINGUNO;
                    cargarDatos();
                } else {
                    if (txtNombreEmpresa.getText().isEmpty()) {
                        lbCampoVacioNom.setVisible(true);
                    }
                    if (txtDireccionEmpresa.getText().isEmpty()) {
                        lbCampoVacioDir.setVisible(true);
                    }
                    if (txtTelefonoEmpresa.getText().isEmpty()) {
                        lbCampoVacioTel.setVisible(true);
                    }
                }

                break;
        }
    }

    public void seleccionarElemento() {
        if (tblEmpresas.getSelectionModel().getSelectedItem() != null) {
            txtCodigoEmpresa.setText(String.valueOf(((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
            txtNombreEmpresa.setText(((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getNombreEmpresa());
            txtDireccionEmpresa.setText(((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getDireccion());
            txtTelefonoEmpresa.setText(((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getTelefono());
        }

    }

    public void eliminar() {
        switch (tipoDeOperacion) {
            case GUARDARNUEVO:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("NUEVO");
                btnEditar.setText("EDITAR");
                btnEliminar.setVisible(true);
                btnReporte.setVisible(true);
                imgNuevo.setImage(new Image("/org/joshuarealiquez/image/addIcon.png"));
                imgEditar.setImage(new Image("/org/joshuarealiquez/image/editIcon.png"));
                ocultarErrores();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if (tblEmpresas.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Empresa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_ELiminarEmpresa(?)");
                            procedimiento.setInt(1, ((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa());

                            procedimiento.execute();

                            listaEmpresa.remove(tblEmpresas.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            tblEmpresas.getSelectionModel().clearSelection();
                        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
                            JOptionPane.showMessageDialog(null, "No se puede eliminar la empresa porque se encuentra en uso.");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (respuesta == JOptionPane.NO_OPTION) {
                        limpiarControles();
                        tblEmpresas.getSelectionModel().clearSelection();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento de la tabla.");
                }
        }

    }

    public boolean validarTamanioCampos() {

        if (txtNombreEmpresa.getText().length() > 150 || txtTelefonoEmpresa.getText().length() > 8 || txtDireccionEmpresa.getText().length() > 150) {
            return false;
        }

        return true;
    }

    public void ocultarErrores() {
        lbCampoVacioNom.setVisible(false);
        lbCampoVacioDir.setVisible(false);
        lbCampoVacioTel.setVisible(false);
    }

    public void guardar() {
        Empresa registro = new Empresa();
        //registro.setCodigoEmpresa(Integer.parseInt(txtCodigoEmpresa.getText()));
        registro.setNombreEmpresa(txtNombreEmpresa.getText());
        registro.setDireccion(txtDireccionEmpresa.getText());
        registro.setTelefono(txtTelefonoEmpresa.getText());

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_AgregarEmpresa(?, ?, ?);");

            procedimiento.setString(1, registro.getNombreEmpresa());
            procedimiento.setString(2, registro.getDireccion());
            procedimiento.setString(3, registro.getTelefono());

            procedimiento.execute();

            //listaEmpresa.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblEmpresas.getSelectionModel().getSelectedItem() != null) {
                    btnReporte.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnNuevo.setText("ACTUALIZAR");
                    btnEditar.setText("CANCELAR");
                    imgNuevo.setImage(new Image("/org/joshuarealiquez/image/saveIcon.png"));
                    imgEditar.setImage(new Image("/org/joshuarealiquez/image/cancelIcon.png"));
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAREDITAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento de la tabla.");
                }
                break;
            case ACTUALIZAR:
                //actualizar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("EDITAR");
                btnReporte.setText("REPORTE");
                imgEditar.setImage(new Image("/org/joshuarealiquez/image/editIcon.png"));
                imgReporte.setImage(new Image("/org/joshuarealiquez/image/reportIcon.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
            case GUARDARNUEVO:
                //Al darle clic en nuevo, el boton editar se convierte en cancelar, por ende cancelara la accion.
                cancelarOpciones();
                tblEmpresas.setDisable(false);
                break;
            case ACTUALIZAREDITAR:
                //en este caso el boton de editar se debe convertir en la opcion de cancelar
                cancelarOpciones();
                tblEmpresas.setDisable(false);
                break;
        }
    }

    public boolean validarCampos() {
        if (txtNombreEmpresa.getText().isEmpty() || txtDireccionEmpresa.getText().isEmpty() || txtTelefonoEmpresa.getText().isEmpty()) {
            return false;
        }

        return true;
    }

    public void cancelarOpciones() {
        limpiarControles();
        desactivarControles();
        btnNuevo.setText("NUEVO");
        btnEditar.setText("EDITAR");
        btnEliminar.setVisible(true);
        btnReporte.setVisible(true);
        btnEliminar.setDisable(false);
        btnReporte.setDisable(false);
        imgNuevo.setImage(new Image("/org/joshuarealiquez/image/addIcon.png"));
        imgEditar.setImage(new Image("/org/joshuarealiquez/image/editIcon.png"));
        ocultarErrores();
        tblEmpresas.getSelectionModel().clearSelection();
        tipoDeOperacion = operaciones.NINGUNO;
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_EditarEmpresa(?,?,?,?)");
            Empresa registro = (Empresa) tblEmpresas.getSelectionModel().getSelectedItem();
            registro.setNombreEmpresa(txtNombreEmpresa.getText());
            registro.setDireccion(txtDireccionEmpresa.getText());
            registro.setTelefono(txtTelefonoEmpresa.getText());

            procedimiento.setInt(1, registro.getCodigoEmpresa());
            procedimiento.setString(2, registro.getNombreEmpresa());
            procedimiento.setString(3, registro.getDireccion());
            procedimiento.setString(4, registro.getTelefono());

            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reporte() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblEmpresas.getSelectionModel().getSelectedItem() != null) {
                    //imprimirReporteEmpresa(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
                    JOptionPane.showMessageDialog(null, "Opcion no disponible por el momento.");
                } else {
                    imprimirReporte();
                }

                break;
        }
    }

    public void imprimirReporte() {
        Map parametros = new HashMap();
        parametros.put("codigoEmpresa", null);
        parametros.put("imagenPie", GenerarReporte.class.getResource("/org/joshuarealiquez/image/piedePagina.png"));
        parametros.put("imagenEncabezado", GenerarReporte.class.getResource("/org/joshuarealiquez/image/encabezado.png"));
        //nombreReporte es el documento .jasper
        GenerarReporte.mostrarReporte("reporteEmpresas.jasper", "Reporte de Empresas", parametros);
    }

    public void imprimirReporteEmpresa(int codigoEmpresa) {
        Map parametros = new HashMap();
        parametros.put("codEmpresaReporte", codigoEmpresa);

    }

    public void desactivarControles() {
        txtCodigoEmpresa.setEditable(false);
        txtNombreEmpresa.setEditable(false);
        txtDireccionEmpresa.setEditable(false);
        txtTelefonoEmpresa.setEditable(false);
    }

    public void activarControles() {
        txtCodigoEmpresa.setEditable(false);
        txtNombreEmpresa.setEditable(true);
        txtDireccionEmpresa.setEditable(true);
        txtTelefonoEmpresa.setEditable(true);
    }

    public void limpiarControles() {
        txtCodigoEmpresa.clear();
        txtNombreEmpresa.clear();
        txtDireccionEmpresa.clear();
        txtTelefonoEmpresa.clear();
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

    public void presupuestos() {
        escenarioPrincipal.ventanaPresupuestos();
    }

    public void servicios() {
        escenarioPrincipal.ventanaServicios();
    }

}
