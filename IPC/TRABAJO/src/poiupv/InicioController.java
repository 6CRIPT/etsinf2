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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Navegacion;
import model.User;

/**
 * FXML Controller class
 *
 * @author torre
 */
public class InicioController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;
    
    @FXML
    private TextField usuario;
    @FXML
    private PasswordField contrasena;
    @FXML
    private Label error;
    @FXML
    private Button entrarBT;
    @FXML
    private Button registrarBT;
    private User usuarioObj;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    public void setStage(Stage newStage) { this.stage = newStage;}
    @FXML
    private void entrarAC(ActionEvent event) throws NavegacionDAOException, IOException {
        Navegacion navegacion = Navegacion.getSingletonNavegacion();
        
        String nombreUsuario = usuario.getText();
        String contrasenaUsuario = contrasena.getText();
        
        if (navegacion.exitsNickName(nombreUsuario)) {
            User loginUser = navegacion.loginUser(nombreUsuario, contrasenaUsuario);
            if (loginUser != null) {
                // Inicio Correcto
                //Parent root = FXMLLoader.load(getClass().getResource("Principal.fxml"));
                
                // Guardar
               usuarioObj = loginUser;
               this.stage = (Stage)((Node) event.getSource()).getScene().getWindow();
               this.stage.close();
                
            } else {
                // Inicio Incorrecto
                error.setText("La contraseña introducida no coincide con la del usuario.");
            }
        } else {
            error.setText("El nombre de usuario no es válido.");
        }
        
        
        
    }

    @FXML
    private void registrarAC(ActionEvent event) throws IOException {
        
        
        Parent root = FXMLLoader.load(getClass().getResource("Registro.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Registro");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void enterAC(ActionEvent event) throws IOException, NavegacionDAOException {
        Navegacion navegacion = Navegacion.getSingletonNavegacion();
        
        String nombreUsuario = usuario.getText();
        String contrasenaUsuario = contrasena.getText();
        
        if (navegacion.exitsNickName(nombreUsuario)) {
            User loginUser = navegacion.loginUser(nombreUsuario, contrasenaUsuario);
            if (loginUser != null) {
                // Inicio Correcto
                //Parent root = FXMLLoader.load(getClass().getResource("Principal.fxml"));
                
                // Guardar
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("Principal.fxml"));
                Parent principalParent = loader.load();
                
                Scene principalScene = new Scene(principalParent);
                
                // Creamos el controller para ejecutar métodos.
                PrincipalController controller = loader.getController();
        
                controller.setUser(loginUser);
                
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                stage.setTitle("Principal");
                stage.setScene(principalScene);
                stage.show();
                
            } else {
                // Inicio Incorrecto
                error.setText("La contraseña introducida no coincide con la del usuario.");
            }
        } else {
            error.setText("El nombre de usuario no es válido.");
        }
    }

    public User getLoggedUser() { return usuarioObj; }

    
}
