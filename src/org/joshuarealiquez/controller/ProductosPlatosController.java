package org.joshuarealiquez.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.joshuarealiquez.bean.Plato;
import org.joshuarealiquez.bean.Productos;
import org.joshuarealiquez.bean.ProductoPlato;
import org.joshuarealiquez.db.Conexion;
import org.joshuarealiquez.main.Principal;

public class ProductosPlatosController implements Initializable {

    private Principal escenarioPrincipal;

    private enum operaciones {
        NUEVO, ACTUALIZAR, NINGUNO;
    }

    private operaciones tipoDeOperacion = operaciones.NINGUNO;

    private ObservableList<Productos> listaProducto;
    private ObservableList<Plato> listaPlato;
    private ObservableList<ProductoPlato> listaProductoPlato;

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
    private TextField txtProductosCodigoProducto;

    @FXML
    private ComboBox cmbCodigoPlato;

    @FXML
    private ComboBox cmbCodigoProducto;

    @FXML
    private TableView tblProductosPlatos;

    @FXML
    private TableColumn colProductosCodigoProducto;

    @FXML
    private TableColumn colCodigoPlato;

    @FXML
    private TableColumn colCodigoProducto;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        desactivarControles();
    }

    public void cargarDatos() {
        tblProductosPlatos.setItems(getProductosPlatos());
        colProductosCodigoProducto.setCellValueFactory(new PropertyValueFactory<ProductoPlato, Integer>("Productos_codigoProducto"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<ProductoPlato, Integer>("codigoPlato"));
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<ProductoPlato, Integer>("codigoProducto"));

        cmbCodigoPlato.setItems(getPlatos());
        cmbCodigoProducto.setItems(getProductos());
    }

    public ObservableList<ProductoPlato> getProductosPlatos() {
        ArrayList<ProductoPlato> lista = new ArrayList<ProductoPlato>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_ListarProductosHasPlatos");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new ProductoPlato(resultado.getInt("Productos_codigoProducto"),
                        resultado.getInt("codigoPlato"), resultado.getInt("codigoProducto"))
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaProductoPlato = FXCollections.observableArrayList(lista);
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

    public ObservableList<Productos> getProductos() {
        ArrayList<Productos> lista = new ArrayList<Productos>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_ListarProductos");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Productos(resultado.getInt("codigoProducto"),
                        resultado.getString("nombreProducto"), resultado.getInt("cantidad"))
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProducto = FXCollections.observableArrayList(lista);
    }

    public void seleccionarElemento() {
        if (tblProductosPlatos.getSelectionModel().getSelectedItem() != null) {
            txtProductosCodigoProducto.setText(String.valueOf(((ProductoPlato) tblProductosPlatos.getSelectionModel().getSelectedItem()).getProductos_codigoProducto()));
            cmbCodigoPlato.getSelectionModel().select(buscarPlato(((ProductoPlato) tblProductosPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
            cmbCodigoProducto.getSelectionModel().select(buscarProducto(((ProductoPlato) tblProductosPlatos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
        }
    }

    public Plato buscarPlato(int codigoPlato) {
        Plato registro = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_BuscarPlato(?)");
            procedimiento.setInt(1, codigoPlato);
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                registro = new Plato(resultado.getInt("codigoPlato"), resultado.getInt("cantidad"),
                        resultado.getString("nombrePlato"), resultado.getString("descripcionPlato"),
                        resultado.getDouble("precioPlato"), resultado.getInt("codigoTipoPlato"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return registro;
    }

    public Productos buscarProducto(int codigoProducto) {
        Productos registro = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_BuscarProducto(?)");
            procedimiento.setInt(1, codigoProducto);
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                registro = new Productos(resultado.getInt("codigoProducto"),
                        resultado.getString("nombreProducto"), resultado.getInt("cantidad"));
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
                txtProductosCodigoProducto.setDisable(false);
                limpiarControles();
                desactivarBotones();
                tblProductosPlatos.getSelectionModel().clearSelection();
                tblProductosPlatos.setDisable(true);
                tipoDeOperacion = operaciones.NUEVO;
                break;
            case NUEVO:
                if (validarCampos()) {
                    guardar();
                    activarBotones();
                    limpiarControles();
                    desactivarControles();
                    tblProductosPlatos.setDisable(false);
                    tipoDeOperacion = operaciones.NINGUNO;
                    cargarDatos();
                }else{
                    JOptionPane.showMessageDialog(null, "Hay campos vacios en el formulario");
                }

                break;
        }
    }

    public void editar() {
        switch (tipoDeOperacion) {
            case NUEVO:
                activarBotones();
                desactivarControles();
                limpiarControles();
                tblProductosPlatos.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }

    public void guardar() {
        ProductoPlato registro = new ProductoPlato();
        registro.setProductos_codigoProducto(Integer.valueOf(txtProductosCodigoProducto.getText()));
        registro.setCodigoProducto(((Productos) cmbCodigoProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
        registro.setCodigoPlato(((Plato) cmbCodigoPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("CALL sp_AgregarProductoHasPlato(?,?,?)");
            procedimiento.setInt(1, registro.getProductos_codigoProducto());
            procedimiento.setInt(2, registro.getCodigoPlato());
            procedimiento.setInt(3, registro.getCodigoProducto());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean validarCampos() {
        if (cmbCodigoPlato.getSelectionModel().getSelectedItem() == null || cmbCodigoProducto.getSelectionModel().getSelectedItem() == null) {
            return false;
        }
        return true;
    }

    public void activarControles() {
        cmbCodigoPlato.setDisable(false);
        cmbCodigoProducto.setDisable(false);
        txtProductosCodigoProducto.setEditable(true);
    }

    public void desactivarControles() {
        cmbCodigoPlato.setDisable(true);
        cmbCodigoPlato.setOpacity(100.00);
        cmbCodigoProducto.setDisable(true);
        cmbCodigoProducto.setOpacity(100.00);
        txtProductosCodigoProducto.setEditable(false);
    }

    public void limpiarControles() {
        cmbCodigoPlato.getSelectionModel().select(null);
        cmbCodigoProducto.getSelectionModel().select(null);
        txtProductosCodigoProducto.clear();
    }

    public void activarBotones() {
        btnEliminar.setDisable(false);
        btnReporte.setDisable(false);
        btnNuevo.setText("NUEVO");
        btnEditar.setText("EDITAR");
        imgNuevo.setImage(new Image("/org/joshuarealiquez/image/addIcon.png"));
        imgEditar.setImage(new Image("/org/joshuarealiquez/image/editIcon.png"));
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
