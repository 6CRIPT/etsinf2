/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv;

import DBAccess.NavegacionDAOException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import model.Navegacion;
import model.Problem;
import model.Session;
import model.User;

/**
 * FXML Controller class
 *
 * @author cesar
 */
public class VentanaEjerciciosController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button atrasBT;
    @FXML
    private Button aleatorioBT;
    @FXML
    private ComboBox<String> elegirBT;
    
    // VARIABLES EXTRA
    private Navegacion navegacion;
    private List<Problem> problemas ;
    private Problem problemaElegido;
    private User usuario;
    private int index;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Objeto Navegación
        try { this.navegacion = Navegacion.getSingletonNavegacion(); } 
        catch (NavegacionDAOException ex) { Logger.getLogger(VentanaEjerciciosController.class.getName()).log(Level.SEVERE, null, ex); }
        
        // 1. Obtener Ejercicios
        problemas = navegacion.getProblems();
        
        // 2. Elegir el Problema Aleatorio
        Random rand = new Random();
        Problem randomProblem = problemas.get(rand.nextInt(problemas.size()));
        problemaElegido = randomProblem;
        
        // 3. Llenar el desplegable con los Problemas.
        
        for (int i = 1; i <= problemas.size(); i++) {
            elegirBT.getItems().add("Problema " + i);   
        }
        
        
    }

    public void setUser(User newUser) { this.usuario = newUser;}

    @FXML
    private void atrasAC(ActionEvent event)throws IOException {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();

    }

    @FXML
    private void aleatorioBT(ActionEvent event) throws IOException, NavegacionDAOException {
        index = (int)(Math. random()*17+1);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("VisualizarEjercicio.fxml"));
        Parent principalParent = loader.load();
        
        Scene principalScene = new Scene(principalParent);
        
        // Creamos el controller para ejecutar métodos.
        VisualizarEjercicioController controller = loader.getController();
        
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setTitle("Nauting: Ejercicio");
        stage.setScene(principalScene);
        
        controller.setVariables(usuario, problemas, problemaElegido, 0, index, 0, 0, stage);
        
        
        
        
        stage.show();
        //if (aciertos + fallos != 0) { usuario.addSession(new Session(LocalDateTime.now(), aciertos, fallos)); }
                            
        
        
    }

    @FXML
    private void elegirAC(ActionEvent event) throws IOException {
        index = elegirBT.getSelectionModel().getSelectedIndex();
        Problem problema = problemas.get(index);
        
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("VisualizarEjercicio.fxml"));
        Parent principalParent = loader.load();

        Scene principalScene = new Scene(principalParent);

        // Creamos el controller para ejecutar métodos.
        VisualizarEjercicioController controller = loader.getController();

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setTitle("Nauting: Ejercicio");
        stage.setScene(principalScene);
        
        controller.setVariables(usuario, problemas, problema, 1,index, 0, 0, stage); //,index, 0, 0

        stage.show();
        
        
    }
    
}
