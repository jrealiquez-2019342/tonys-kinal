package org.joshuarealiquez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
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
import org.joshuarealiquez.bean.TipoEmpleado;
import org.joshuarealiquez.db.Conexion;
import org.joshuarealiquez.main.Principal;

public class TipoEmpleadoController implements Initializable {

    private Principal escenarioPrincipal;

    private enum operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    };

    private operaciones tipoDeOperacion = operaciones.NINGUNO;

    private ObservableList<TipoEmpleado> listaTipoEmpleado;

    @FXML
    private Button btnNuevo;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnReporte;

    @FXML
    private Button btnHome;

    @FXML
    private TextField txtCodigoTipoEmpleado;

    @FXML
    private TextField txtDescripcionTipoEmpleado;

    @FXML
    private TableView tblTiposEmpleados;

    @FXML
    private TableColumn colCodigoTipoEmpleado;

    @FXML
    private TableColumn colDescripcionTipoEmpleado;

    @FXML
    private Label lbCampoVacioDescri;

    @FXML
    private ImageView imgNuevo;

    @FXML
    private ImageView imgEditar;

    @FXML
    private ImageView imgEliminar;

    @FXML
    private ImageView imgReporte;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        volverInvisible();
    }

    public void volverInvisible() {
        lbCampoVacioDescri.setVisible(false);
    }

    public void cargarDatos() {
        tblTiposEmpleados.setItems(getTipoEmpleado());
        //lo que va en comillas es el nombre tal cual esta en la base de datos
        colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<TipoEmpleado, Integer>("codigoTipoEmpleado"));
        colDescripcionTipoEmpleado.setCellValueFactory(new PropertyValueFactory<TipoEmpleado, String>("descripcion"));
    }

    public ObservableList<TipoEmpleado> getTipoEmpleado() {
        ArrayList<TipoEmpleado> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_ListarTiposEmpleados");
            ResultSet resultado = procedimiento.executeQuery();

            while (resultado.next()) {
                lista.add(new TipoEmpleado(
                        resultado.getInt("codigoTipoEmpleado"),
                        resultado.getString("descripcion"))
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTipoEmpleado = FXCollections.observableArrayList(lista);
    }

    public void nuevo() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                tblTiposEmpleados.getSelectionModel().clearSelection();
                limpiarControles();
                habilitarControles();

                btnNuevo.setText("GUARDAR");
                btnEditar.setText("CANCELAR");
                imgNuevo.setImage(new Image("/org/joshuarealiquez/image/saveIcon.png"));
                imgEditar.setImage(new Image("/org/joshuarealiquez/image/cancelIcon.png"));

                btnEliminar.setDisable(true);
                btnReporte.setDisable(true);

                tipoDeOperacion = operaciones.NUEVO;
                break;
            case NUEVO:
                if (txtDescripcionTipoEmpleado.getText().isEmpty()) {
                    lbCampoVacioDescri.setVisible(true);
                } else {
                    lbCampoVacioDescri.setVisible(false);
                    guardar();
                    limpiarControles();
                    desactivarControles();
                    btnNuevo.setText("NUEVO");
                    btnEditar.setText("EDITAR");
                    imgNuevo.setImage(new Image("/org/joshuarealiquez/image/addIcon.png"));
                    imgEditar.setImage(new Image("/org/joshuarealiquez/image/editIcon.png"));

                    btnEliminar.setDisable(false);
                    btnReporte.setDisable(false);

                    tipoDeOperacion = operaciones.NINGUNO;

                    cargarDatos();
                }

                break;
            case ACTUALIZAR:
                if (txtDescripcionTipoEmpleado.getText().isEmpty()) {
                    lbCampoVacioDescri.setVisible(true);
                } else {
                    lbCampoVacioDescri.setVisible(false);
                    actualizar();
                    limpiarControles();
                    desactivarControles();
                    btnNuevo.setText("NUEVO");
                    btnEditar.setText("EDITAR");
                    imgNuevo.setImage(new Image("/org/joshuarealiquez/image/addIcon.png"));
                    imgEditar.setImage(new Image("/org/joshuarealiquez/image/editIcon.png"));
                    btnEliminar.setDisable(false);
                    btnReporte.setDisable(false);
                    cargarDatos();
                    tipoDeOperacion = operaciones.NINGUNO;
                }
                break;
        }
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_EditarTipoEmpleado(?,?)");
            TipoEmpleado registro = (TipoEmpleado) tblTiposEmpleados.getSelectionModel().getSelectedItem();

            //registro.setCodigoTipoEmpleado(Integer.parseInt(txtCodigoTipoEmpleado.getText()));
            registro.setDescripcion(txtDescripcionTipoEmpleado.getText());

            procedimiento.setInt(1, registro.getCodigoTipoEmpleado());
            procedimiento.setString(2, registro.getDescripcion());

            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void guardar() {
        TipoEmpleado registro = new TipoEmpleado();
        //registro.setCodigoTipoEmpleado(Integer.parseInt(txtCodigoTipoEmpleado.getText()));
        registro.setDescripcion(txtDescripcionTipoEmpleado.getText());

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_AgregarTipoEmpleado(?)");
            procedimiento.setString(1, registro.getDescripcion());

            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * *
     * Este metodo nos sirve para que el objeto selecionado en el TableView lo
     * mostremos en los TextField;
     */
    public void seleccionarElemento() {
        if (tblTiposEmpleados.getSelectionModel().getSelectedItem() != null) {
            txtCodigoTipoEmpleado.setText(String.valueOf(((TipoEmpleado) tblTiposEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
            txtDescripcionTipoEmpleado.setText(((TipoEmpleado) tblTiposEmpleados.getSelectionModel().getSelectedItem()).getDescripcion());
        }
    }

    public void editar() {

        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblTiposEmpleados.getSelectionModel().getSelectedItem() != null) {
                    habilitarControles();

                    btnEliminar.setDisable(true);
                    btnReporte.setDisable(true);

                    btnNuevo.setText("GUARDAR");
                    btnEditar.setText("CANCELAR");

                    imgNuevo.setImage(new Image("/org/joshuarealiquez/image/saveIcon.png"));
                    imgEditar.setImage(new Image("/org/joshuarealiquez/image/cancelIcon.png"));
                    //tblTiposEmpleados.getSelectionModel().clearSelection();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento de la tabla.");
                }
                break;
            case NUEVO:
                limpiarControles();
                desactivarControles();
                lbCampoVacioDescri.setVisible(false);
                tblTiposEmpleados.getSelectionModel().clearSelection();
                btnNuevo.setText("NUEVO");
                btnEditar.setText("EDITAR");

                imgNuevo.setImage(new Image("/org/joshuarealiquez/image/addIcon.png"));
                imgEditar.setImage(new Image("/org/joshuarealiquez/image/editIcon.png"));

                btnEliminar.setDisable(false);
                btnReporte.setDisable(false);
                
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            case ACTUALIZAR:
                //se debe cancelar la funcion
                limpiarControles();
                desactivarControles();
                lbCampoVacioDescri.setVisible(false);
                tblTiposEmpleados.getSelectionModel().clearSelection();
                btnEliminar.setDisable(false);
                btnReporte.setDisable(false);

                btnNuevo.setText("NUEVO");
                btnEditar.setText("EDITAR");

                imgNuevo.setImage(new Image("/org/joshuarealiquez/image/addIcon.png"));
                imgEditar.setImage(new Image("/org/joshuarealiquez/image/editIcon.png"));

                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }

    }

    public void eliminar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblTiposEmpleados.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Tipo Empleado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_EliminarTipoEmpleado(?)");
                            procedimiento.setInt(1, ((TipoEmpleado) tblTiposEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
                            procedimiento.execute();

                            listaTipoEmpleado.remove(tblTiposEmpleados.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            tblTiposEmpleados.getSelectionModel().clearSelection();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (respuesta == JOptionPane.NO_OPTION) {
                        limpiarControles();
                        tblTiposEmpleados.getSelectionModel().clearSelection();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento de la tabla.");
                }
                break;
        }
    }

    public void limpiarControles() {
        txtCodigoTipoEmpleado.clear();
        txtDescripcionTipoEmpleado.clear();
    }

    public void habilitarControles() {
        txtCodigoTipoEmpleado.setEditable(false);
        txtDescripcionTipoEmpleado.setEditable(true);
    }

    public void desactivarControles() {
        txtCodigoTipoEmpleado.setEditable(false);
        txtDescripcionTipoEmpleado.setEditable(false);
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

    public void empleados(){
        escenarioPrincipal.ventanaEmpleados();
    }
    
}
