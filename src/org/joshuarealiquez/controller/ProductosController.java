package org.joshuarealiquez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.joshuarealiquez.bean.Productos;
import org.joshuarealiquez.db.Conexion;
import org.joshuarealiquez.main.Principal;

public class ProductosController implements Initializable {

    private Principal escenarioPrincipal;

    private boolean avanzar = true;

    private enum operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    }

    private operaciones tipoDeOperacion = operaciones.NINGUNO;

    @FXML
    private Button btnNuevo;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnReporte;

    @FXML
    private Label lbCampoVacioNomProduct;

    @FXML
    private Label lbCampoVacioCantProduct;

    @FXML
    private ImageView imgNuevo;

    @FXML
    private ImageView imgEditar;

    @FXML
    private ImageView imgEliminar;

    @FXML
    private ImageView imgReporte;

    @FXML
    private TextField txtCodigoProducto;

    @FXML
    private TextField txtNombreProducto;

    @FXML
    private TextField txtCantidadProducto;

    @FXML
    private TableView tblProductos;

    @FXML
    private TableColumn colCodigoProducto;

    @FXML
    private TableColumn colNombreProducto;

    @FXML
    private TableColumn colCantidadProducto;

    ObservableList<Productos> listaProducto;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        volverInvisible();
    }

    public void cargarDatos() {
        tblProductos.setItems(getProductos());

        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("codigoProducto"));
        colNombreProducto.setCellValueFactory(new PropertyValueFactory<Productos, String>("nombreProducto"));
        colCantidadProducto.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("cantidad"));

    }

    public void volverInvisible() {
        lbCampoVacioNomProduct.setVisible(false);
        lbCampoVacioCantProduct.setVisible(false);
        lbCampoVacioCantProduct.setText("*Campo vacio");
        lbCampoVacioNomProduct.setText("*Campo vacio");
    }

    public ObservableList<Productos> getProductos() {
        ArrayList<Productos> lista = new ArrayList<>();

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_ListarProductos");
            ResultSet resultado = procedimiento.executeQuery();

            while (resultado.next()) {
                lista.add(new Productos(
                        resultado.getInt("codigoProducto"),
                        resultado.getString("nombreProducto"),
                        resultado.getInt("cantidad")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaProducto = FXCollections.observableArrayList(lista);
    }

    public void nuevo() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                limpiarControles();
                desactivarBotones();
                activarControles();
                tblProductos.setDisable(true);
                tblProductos.getSelectionModel().clearSelection();
                tipoDeOperacion = operaciones.NUEVO;
                break;
            case NUEVO:
                if (txtNombreProducto.getText().isEmpty() || txtCantidadProducto.getText().isEmpty() || validarCasteo()) {
                    if (txtNombreProducto.getText().isEmpty()) {
                        lbCampoVacioNomProduct.setVisible(true);
                    } else {
                        lbCampoVacioNomProduct.setVisible(false);
                    }
                    if (txtCantidadProducto.getText().isEmpty()) {
                        lbCampoVacioCantProduct.setVisible(true);
                    } else {
                        lbCampoVacioCantProduct.setVisible(false);
                    }
                    
                } else {

                    guardar();
                    activarBotones();
                    desactivarControles();
                    limpiarControles();
                    tblProductos.setDisable(false);
                    tipoDeOperacion = operaciones.NINGUNO;
                    volverInvisible();
                    cargarDatos();

                }

                break;
            case ACTUALIZAR:

                if (txtNombreProducto.getText().isEmpty() || txtCantidadProducto.getText().isEmpty()) {
                    if (txtNombreProducto.getText().isEmpty()) {
                        lbCampoVacioNomProduct.setVisible(true);
                    } else {
                        lbCampoVacioNomProduct.setVisible(false);
                    }
                    if (txtCantidadProducto.getText().isEmpty()) {
                        lbCampoVacioCantProduct.setVisible(true);
                    } else {
                        lbCampoVacioCantProduct.setVisible(false);
                    }
                } else {
                    actualizar();
                    if (avanzar) {
                        activarBotones();
                        desactivarControles();
                        limpiarControles();
                        volverInvisible();

                        cargarDatos();
                        //tblProductos.setDisable(false);
                        tipoDeOperacion = operaciones.NINGUNO;
                    }

                }

                break;
        }
    }
    
    public boolean validarCasteo(){
        try{
            int cantidad = Integer.valueOf(txtCantidadProducto.getText());
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Ingrese un valor numerico en el teléfono.");
            return true;
        }
        
        return false;
    }

    public void actualizar() {
        Productos registro = (Productos) tblProductos.getSelectionModel().getSelectedItem();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_EditarProducto(?,?,?)");

            registro.setNombreProducto(txtNombreProducto.getText());
            registro.setCantidad(Integer.parseInt(txtCantidadProducto.getText()));

            procedimiento.setInt(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getNombreProducto());
            procedimiento.setInt(3, registro.getCantidad());

            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void seleccionarElemento() {
        if (tblProductos.getSelectionModel().getSelectedItem() != null) {
            txtCodigoProducto.setText(String.valueOf(((Productos) tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
            txtNombreProducto.setText(((Productos) tblProductos.getSelectionModel().getSelectedItem()).getNombreProducto());
            txtCantidadProducto.setText(String.valueOf(((Productos) tblProductos.getSelectionModel().getSelectedItem()).getCantidad()));
        }
    }

    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblProductos.getSelectionModel().getSelectedItem() != null) {
                    desactivarBotones();
                    activarControles();

                    tipoDeOperacion = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento de la tabla.");
                }

                break;
            case NUEVO:
                desactivarControles();
                limpiarControles();
                volverInvisible();
                activarBotones();
                tblProductos.getSelectionModel().clearSelection();
                tblProductos.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            case ACTUALIZAR:

                desactivarControles();
                limpiarControles();
                volverInvisible();
                activarBotones();
                tblProductos.getSelectionModel().clearSelection();

                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }

    public void eliminar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblProductos.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Producto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_EliminarProducto(?)");
                            procedimiento.setInt(1, ((Productos) tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
                            procedimiento.execute();

                            listaProducto.remove(tblProductos.getSelectionModel().getSelectedIndex());

                            limpiarControles();
                            tblProductos.getSelectionModel().clearSelection();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (respuesta == JOptionPane.NO_OPTION) {
                        limpiarControles();
                        tblProductos.getSelectionModel().clearSelection();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento de la tabla.");
                }

                break;
        }
    }

    public void guardar() {
        Productos registro = new Productos();

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_AgregarProducto(?,?)");

            //registro.setCodigoProducto(Integer.parseInt(txtCodigoProducto.getText()));
            registro.setNombreProducto(txtNombreProducto.getText());
            registro.setCantidad(Integer.parseInt(txtCantidadProducto.getText()));

            procedimiento.setString(1, registro.getNombreProducto());
            procedimiento.setInt(2, registro.getCantidad());

            procedimiento.execute();
            //try esta ejecutando todo sin importar encontrar una excepcion.
            //avanzar = true;

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Solo se permiten numeros.");
            avanzar = false;
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    public void limpiarControles() {
        txtCodigoProducto.clear();
        txtNombreProducto.clear();
        txtCantidadProducto.clear();
    }

    public void desactivarControles() {
        txtCodigoProducto.setEditable(false);
        txtNombreProducto.setEditable(false);
        txtCantidadProducto.setEditable(false);
    }

    public void activarControles() {
        txtCodigoProducto.setEditable(false);
        txtNombreProducto.setEditable(true);
        txtCantidadProducto.setEditable(true);
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    //funcionamiento a los botones
    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }
}
