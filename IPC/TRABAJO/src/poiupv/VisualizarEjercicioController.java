/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv;

import DBAccess.NavegacionDAOException;
import static java.awt.Color.RED;
import static java.awt.Color.green;
import static java.awt.Color.red;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Answer;
import model.Problem;
import model.Session;
import model.User;

/**
 * FXML Controller class
 *
 * @author torre
 */
public class VisualizarEjercicioController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;
    
    @FXML
    private Label pregunta;
    @FXML
    private RadioButton aBT;
    @FXML
    private Label opcAtxt;
    @FXML
    private RadioButton bBT;
    @FXML
    private Label opcBtxt;
    @FXML
    private RadioButton cBT;
    @FXML
    private Label opcCtxt;
    @FXML
    private Button atrasBT1;
    @FXML
    private Button comprobarBT;
    @FXML
    private Button siguienteBT;
    
    
    // VARIABLES PROPIAS
    private User usuario;
    private List<Problem> problemas;
    private Problem problema;
    private int tipo;
    private List<Answer> respuestas;
    private Answer respuesta;
    private Label opc;
    private boolean suu;
    private ArrayList<Label> opciones = new ArrayList<Label>();
    private ArrayList<RadioButton> botones = new ArrayList<RadioButton>();
    private int aciertos=0;
    private int fallos=0;
    private int ejercicio;
    
    // tipo = 0 || aleatorio
    // tipo = 1 || elegido
    @FXML
    private RadioButton dBT;
    @FXML
    private Label opcDtxt;
    @FXML
    private ToggleGroup a;
    @FXML
    private Button abrirMapaBT;
    private RadioButton boton;
    private int index = 0;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 1. Ajuste de Botones
        opciones.add(opcAtxt);
        opciones.add(opcBtxt);
        opciones.add(opcCtxt);
        opciones.add(opcDtxt);
        
        botones.add(aBT);
        botones.add(bBT);
        botones.add(cBT);
        botones.add(dBT);
        
        
    }
    
    public void guardarSesion() throws NavegacionDAOException {
        if (this.aciertos + this.fallos != 0) { usuario.addSession( new Session(LocalDateTime.now(), aciertos, fallos)); } 
    }
        

    public void setVariables(User newUser, List<Problem> newProblemas, Problem newProblema, int newTipo, int index, int aciertos, int fallos, Stage newStage) {
        this.usuario = newUser;
        this.problemas = newProblemas;
        this.problema = newProblema;
        this.tipo = newTipo;
        this.index = index;
        this.aciertos = aciertos;
        this.fallos = fallos;
        this.stage = newStage;
        
        
   
        
        pregunta.setText(problema.getText());
        respuestas = problema.getAnswers();
        for(int i=0; i< respuestas.size(); i++){
            
                opc = opciones.get(i);
                opc.setText(respuestas.get(i).getText());
        }
        
        // 2. Al Cerrar Aplicación
        stage.setOnHiding(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            guardarSesion();
                        } catch (NavegacionDAOException ex) {
                            Logger.getLogger(VisualizarEjercicioController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        //System.exit(0);
                    }
                });
            }
        });

    }              
    
    
    @FXML
    private void aAC(ActionEvent event) {
    }

    @FXML
    private void bAC(ActionEvent event) {
    }

    @FXML
    private void cAC(ActionEvent event) {
    }
    
    
    

    @FXML
    private void atrasAC(ActionEvent event) throws IOException, NavegacionDAOException {
        if (aciertos + fallos != 0) { usuario.addSession(new Session(LocalDateTime.now(), aciertos, fallos)); }
        
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("VentanaEjercicios.fxml"));
        Parent principalParent = loader.load();

        Scene principalScene = new Scene(principalParent);

        // Creamos el controller para ejecutar métodos.
        VentanaEjerciciosController controller = loader.getController();

        controller.setUser(usuario);
        
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setTitle("Nauting: Ejercicios");
        stage.setScene(principalScene);
        stage.show();
        
    }

    @FXML
    private void comprobarAC(ActionEvent event) {
        comprobarBT.setDisable(true);
        String correcto;
        int cont = aciertos;
        for(int i=0; i< respuestas.size(); i++){
            
            opc = opciones.get(i);
            boton = botones.get(i);
            boton.setDisable(true);
            suu = respuestas.get(i).getValidity();
            if(suu == true){
                opc.setTextFill(Color.GREEN);
                if(boton.isSelected()){ aciertos++; }
                
            }else{
                opc.setTextFill(Color.RED);
                }      
        }
        if (cont == aciertos){ fallos++; }
    }

    @FXML
    private void siguienteAC(ActionEvent event) throws IOException {
        comprobarBT.setDisable(false);
        for(int i=0; i< respuestas.size(); i++){
            opc = opciones.get(i);
            boton = botones.get(i);
            boton.setDisable(false);
            opc.setTextFill(Color.BLACK);
            if(boton.isSelected()){boton.setSelected(false);}
        }
        if (tipo == 1){
            index++;
            int numProblemas = problemas.size();
            if (index == numProblemas - 1) { siguienteBT.setDisable(true); };
            
        }else{index = (int)(Math. random()*18+1); }
        
        problema = problemas.get(index);
        pregunta.setText(problema.getText());
        respuestas = problema.getAnswers();
        for(int i=0; i< respuestas.size(); i++){
            
                opc = opciones.get(i);
                opc.setText(respuestas.get(i).getText());
        }
        
    }

    @FXML
    private void dAC(ActionEvent event) {
    }

    @FXML
    private void abrirMapaBAC(ActionEvent event) throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("mapa.fxml"));

        Parent root = cargador.load(); // Cambiamos la ventana root.
        // Cambiamos el escenario a VistaVentana. (La ventana que se muestra) 
        Scene scene = new Scene(root);
        // Modificamos los ajustes de la ventana.
        Stage ventana2 = new Stage();
        ventana2.setTitle("Mapa");
        ventana2.initModality(Modality.WINDOW_MODAL);
        ventana2.setScene(scene);
        ventana2.setResizable(true);
        ventana2.show();
    }
    
}
