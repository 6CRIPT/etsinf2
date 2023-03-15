/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv;

import DBAccess.NavegacionDAOException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.stage.Stage;
import model.User;

/**
 * FXML Controller class
 *
 * @author Candel
 */
public class PrincipalController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    
    @FXML
    private Button perfilBT;
    @FXML
    private Button cerrarSesionBT;

    private User usuario;
    @FXML
    private Button estadisticasBT;
    @FXML
    private Button ejerciciosBT;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // TODO
    }    

    public void setUser(User user) { this.usuario = user; }

    @FXML
    private void perfilAC(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Perfil.fxml"));
        Parent principalParent = loader.load();

        Scene principalScene = new Scene(principalParent);

        // Creamos el controller para ejecutar métodos.
        PerfilController controller = loader.getController();

        controller.setUser(usuario);

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setTitle("Perfil");
        stage.setScene(principalScene);
        stage.show();
        
    }

    @FXML
    private void cerrarSesionAC(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Inicio.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Registro");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void estadisticasAC(ActionEvent event) throws IOException, NavegacionDAOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Estadisticas.fxml"));
        Parent principalParent = loader.load();

        Scene principalScene = new Scene(principalParent);

        // Creamos el controller para ejecutar métodos.
        EstadisticasController controller = loader.getController();

        controller.setVariables(usuario);

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setTitle("Estadísticas");
        stage.setScene(principalScene);
        stage.show();
    }

    @FXML
    private void ejerciciosAC(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("VentanaEjercicios.fxml"));
        Parent principalParent = loader.load();

        Scene principalScene = new Scene(principalParent);

        // Creamos el controller para ejecutar métodos.
        VentanaEjerciciosController controller = loader.getController();

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setTitle("VentanaEjercicios");
        stage.setScene(principalScene);
        
        controller.setUser(usuario);

        
        stage.show();
    }
        
    }
    

