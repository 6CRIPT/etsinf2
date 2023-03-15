/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import DBAccess.NavegacionDAOException;
import java.awt.Paint;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.User;
import poiupv.Poi;

/**
 *
 * @author jsoler
 */
public class mapaController implements Initializable {

    //=======================================
    // hashmap para guardar los puntos de interes POI
    
    // ======================================
    // la variable zoomGroup se utiliza para dar soporte al zoom
    // el escalado se realiza sobre este nodo, al escalar el Group no mueve sus nodos
    private Group zoomGroup;

    
    @FXML
    private ScrollPane map_scrollpane;
    @FXML
    private Slider zoom_slider;
    private MenuButton map_pin;
    @FXML
    private Label posicion;
    private ChoiceBox<String> herramientasBT;
    @FXML
    private Slider grosorSL;
    @FXML
    private ColorPicker colorPicker;
    Group contentGroup = new Group();
    @FXML
    private ToggleGroup botones;
    @FXML
    private ToggleButton rectaBT;
    @FXML
    private ImageView transportador;
    @FXML
    private ToggleButton puntoBT;
    @FXML
    private ToggleButton arcoBT;
    @FXML
    private ToggleButton textoBT;
    private Circle circle1;
    private Line linea;
    private double iniXArc;
    @FXML
    private ToggleButton transportadorBT;
    private double inicioXTrans;
    private double inicioYTrans;
    private double baseX;
    private double baseY;
    private User usuario;
    @FXML
    private ToggleButton punteroBT;
    @FXML
    private ToggleGroup herramientas;
    @FXML
    private ToggleButton perpendicularesBT;
    @FXML
    private ImageView cartaNautica;
    private Line lineaV;
    private Line lineaH;
    @FXML
    private StackPane stackPane;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private boolean logged = false;
    @FXML
    private Button iniciarSesionBT;
    @FXML
    private MenuItem salirBT;
    @FXML
    private MenuItem estadisticasBT;
    @FXML
    private MenuItem cerrarSesionBT;
    @FXML
    private Button registroBT;
    
    private  BooleanProperty loggedProperty = new SimpleBooleanProperty(Boolean.TRUE);
    @FXML
    private MenuItem ejerciciosBT;
    
    public User getLoggedUser() { return usuario; }
    
    @FXML
    void zoomIn(ActionEvent event) {
        //================================================
        // el incremento del zoom dependerá de los parametros del 
        // slider y del resultado esperado
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal += 0.1);
    }

    @FXML
    void zoomOut(ActionEvent event) {
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal + -0.1);
    }
    
    // esta funcion es invocada al cambiar el value del slider zoom_slider
    private void zoom(double scaleValue) {
        //===================================================
        //guardamos los valores del scroll antes del escalado
        double scrollH = map_scrollpane.getHvalue();
        double scrollV = map_scrollpane.getVvalue();
        //===================================================
        // escalamos el zoomGroup en X e Y con el valor de entrada
        zoomGroup.setScaleX(scaleValue);
        zoomGroup.setScaleY(scaleValue);
        //===================================================
        // recuperamos el valor del scroll antes del escalado
        map_scrollpane.setHvalue(scrollH);
        map_scrollpane.setVvalue(scrollV);
    }

 
  

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       transportador.setVisible(false);
        //==========================================================
        // inicializamos el slider y enlazamos con el zoom
        zoom_slider.setMin(0.5);
        zoom_slider.setMax(1.5);
        zoom_slider.setValue(1.0);
        zoom_slider.valueProperty().addListener((o, oldVal, newVal) -> zoom((Double) newVal));
        
        //=========================================================================
        //Envuelva el contenido de scrollpane en un grupo para que 
        //ScrollPane vuelva a calcular las barras de desplazamiento tras el escalado
      
        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(map_scrollpane.getContent());
        map_scrollpane.setContent(contentGroup);
        
        cerrarSesionBT.disableProperty().bind(loggedProperty);
        estadisticasBT.disableProperty().bind(loggedProperty);
        ejerciciosBT.disableProperty().bind(loggedProperty);
        
    }
    public void setUser(User user) { this.usuario = user; logged=true; iniciarSesionBT.setText("Perfil");}
    

    @FXML
    private void muestraPosicion(MouseEvent event) {
        posicion.setText("sceneX: " + (int) event.getSceneX() + ", sceneY: " + (int) event.getSceneY() + "\n"
                + "         X: " + (int) event.getX() + ",          Y: " + (int) event.getY());
    }

    private void cerrarAplicacion(ActionEvent event) {
        ((Stage)zoom_slider.getScene().getWindow()).close();
    }

    private void acercaDe(ActionEvent event) {
        Alert mensaje= new Alert(Alert.AlertType.INFORMATION);
        mensaje.setTitle("Acerca de");
        mensaje.setHeaderText("César Martínez Chico\nJaime Candel Martínez\nCarlos Torregrosa Navarrete");
        mensaje.showAndWait();
    }

    @FXML
    private void iniciarSesionAC(ActionEvent event) throws IOException {
        if (logged) {
            // Perfil
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource("Perfil.fxml"));
            Parent root = myLoader.load();
            PerfilController myPerfilController = myLoader.<PerfilController>getController();
            myPerfilController.setUser(usuario);
            
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("Nauting: Perfil");
            stage.setScene(scene);
            stage.showAndWait();
            
            
            
        } else {
            //Inicio Sesión
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource("Inicio.fxml"));
            Parent root = myLoader.load();
            InicioController myInicioController = myLoader.<InicioController>getController();


            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("Nauting: Iniciar Sesión");
            stage.setScene(scene);
            stage.showAndWait();
            User usuarioRec = myInicioController.getLoggedUser();
            if (usuarioRec != null ) { 
                this.usuario = usuarioRec; 
                this.logged = true;
                iniciarSesionBT.setText("Perfil");
                registroBT.setDisable(true);
                loggedProperty.setValue(false);
                
                
            }
        }
        
        
        
    }

    @FXML
    private void salirAC(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void limpiarAC(ActionEvent event) {
        while(zoomGroup.getChildren().size()>1){
            zoomGroup.getChildren().remove(1);
                 
        }
    }


    @FXML
    private void dragAC(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();
        if(rectaBT.isSelected()){
            linea.setEndX(x);
            linea.setEndY(y);
            linea.setStrokeWidth(grosorSL.getValue());
            linea.setOnContextMenuRequested(e->{
                ContextMenu menuContext = new  ContextMenu();
                MenuItem borrar = new MenuItem("borrar");
                menuContext.getItems().add(borrar);
                borrar.setOnAction(e1->{
                    zoomGroup.getChildren().remove((Node)e.getSource());
                    e1.consume();
                });
                MenuItem cambiarColor= new MenuItem("Cambiar Color");
                menuContext.getItems().add(cambiarColor);
                cambiarColor.setOnAction(e2->{
                    linea.setStroke(colorPicker.getValue());
                    
                    e2.consume();
                });
            menuContext.show(linea, e.getSceneX(), e.getSceneY());
            e.consume();
            });
        }
        
        else if (arcoBT.isSelected()){
            double radio = Math.abs(x - iniXArc);
            circle1.setRadius(radio);
            event.consume();
        }
    }

    @FXML
    private void pressAC(MouseEvent event) {
        Color color = colorPicker.getValue();
        double x = event.getX();
        double y = event.getY();
        if (puntoBT.isSelected()){
            textoBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
            arcoBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
            rectaBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
            puntoBT.setStyle("-fx-background-color:teal;-fx-text-fill:white");
            map_scrollpane.setPannable(false);
            circle1 = new Circle(1);
            circle1.setStroke(color);
            circle1.setFill(color);
            circle1.setCenterX(x);
            circle1.setCenterY(y);
            circle1.setRadius(grosorSL.getValue());
            zoomGroup.getChildren().add(circle1);
            event.consume();
            circle1.setOnContextMenuRequested(e->{
                ContextMenu menuContext = new  ContextMenu();
                MenuItem borrar = new MenuItem("borrar");
                menuContext.getItems().add(borrar);
                borrar.setOnAction(e1->{
                    zoomGroup.getChildren().remove((Node)e.getSource());
                    e1.consume();
                });
                MenuItem cambiarColor= new MenuItem("Cambiar Color");
                menuContext.getItems().add(cambiarColor);
                cambiarColor.setOnAction(e2->{
                    circle1.setStroke(colorPicker.getValue());
                    circle1.setFill(colorPicker.getValue());
                    e2.consume();
                });
            menuContext.show(circle1, e.getSceneX(), e.getSceneY());
            e.consume();
            });
           
            
         //   <>
        }
        else if (textoBT.isSelected()){
            puntoBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
            arcoBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
            rectaBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
            textoBT.setStyle("-fx-background-color:teal;-fx-text-fill:white");
           map_scrollpane.setPannable(false);
           TextField text = new TextField();
           text.setLayoutX(x);
           text.setLayoutY(y);
           zoomGroup.getChildren().add(text);
           text.requestFocus();
           text.setOnAction(e->{ //retocar
               Text texto = new Text(text.getText());
               texto.setX(x);
               texto.setY(y);
               text.setStyle("-fx-font-family:Gafata;-fx-font-size:40");
               texto.setFill(color);
               zoomGroup.getChildren().add(texto);
               zoomGroup.getChildren().remove(text);
               event.consume();
               
            });
           
        }
        else if(rectaBT.isSelected()){
            puntoBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
            arcoBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
            textoBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
            rectaBT.setStyle("-fx-background-color:teal;-fx-text-fill:white");
            map_scrollpane.setPannable(false);
            linea = new Line (x,y,x,y);
            linea.setStroke(color);
            zoomGroup.getChildren().add(linea);
            
           
        }
        else if (arcoBT.isSelected()){
            puntoBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
            rectaBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
            textoBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
            arcoBT.setStyle("-fx-background-color:teal;-fx-text-fill:white");
            circle1 = new Circle(1);
            circle1.setStroke(color);
            circle1.setFill(Color.TRANSPARENT);
            circle1.setStrokeWidth(grosorSL.getValue());
            zoomGroup.getChildren().add(circle1);
            circle1.setCenterX(x);
            circle1.setCenterY(y);
            iniXArc = x;
            circle1.setOnContextMenuRequested(e->{
                ContextMenu menuContext = new  ContextMenu();
                MenuItem borrar = new MenuItem("borrar");
                menuContext.getItems().add(borrar);
                borrar.setOnAction(e1->{
                    zoomGroup.getChildren().remove((Node)e.getSource());
                    e1.consume();
                });
                MenuItem cambiarColor= new MenuItem("Cambiar Color");
                menuContext.getItems().add(cambiarColor);
                cambiarColor.setOnAction(e2->{
                    circle1.setStroke(colorPicker.getValue());
                    e2.consume();
                });
                
            menuContext.show(circle1, e.getSceneX(), e.getSceneY());
            e.consume();
            });
           
        }else if (perpendicularesBT.isSelected()){
             map_scrollpane.setPannable(false);
             lineaV = new Line (event.getX(),0,event.getX(),stackPane.getWidth()+3960);
             lineaH = new Line (0,event.getY(),stackPane.getWidth()+6760,event.getY());
             lineaV.setStroke(color);
             lineaH.setStroke(color);
             lineaV.setStrokeWidth(grosorSL.getValue());
             lineaH.setStrokeWidth(grosorSL.getValue());
             zoomGroup.getChildren().add(lineaV);
             zoomGroup.getChildren().add(lineaH);
             System.out.println("Hola, el netbeans no quiere poner la cruz en el mapa por alguna razon, porque todo se ejecuta bien simplemente no lo hace, lina 363 del controller");
             
             lineaV.setOnContextMenuRequested(e->{
                ContextMenu menuContext = new  ContextMenu();
                MenuItem borrar = new MenuItem("borrar");
                menuContext.getItems().add(borrar);
                borrar.setOnAction(e1->{
                    zoomGroup.getChildren().remove(lineaV);
                    zoomGroup.getChildren().remove(lineaH);
                    e1.consume();
                });
                MenuItem cambiarColor= new MenuItem("Cambiar Color");
                menuContext.getItems().add(cambiarColor);
                cambiarColor.setOnAction(e2->{
                    lineaH.setStroke(colorPicker.getValue());
                    lineaV.setStroke(colorPicker.getValue());
                    e2.consume();
                });
            menuContext.show(lineaV, e.getSceneX(), e.getSceneY());
            e.consume();
            });
        }
    }

    
    
    

    @FXML
    private void dragTransAC(MouseEvent event) {
        double despX = event.getSceneX() - inicioXTrans;
        double despY = event.getSceneY() - inicioYTrans;
        transportador.setTranslateX(baseX + despX);
        transportador.setTranslateY(baseY + despY);
        event.consume();
    }

    @FXML
    private void pressTransAC(MouseEvent event) {
        inicioXTrans = event.getSceneX();
        inicioYTrans = event.getSceneY();
        baseX = transportador.getTranslateX();
        baseY = transportador.getTranslateY();
        event.consume();
    }

    @FXML
    private void transportadorAC(ActionEvent event) {
        transportadorBT.getStyleClass().add("button-raised");
        
        if(transportadorBT.isSelected()){
        transportadorBT.setStyle("-fx-background-color:teal;-fx-text-fill:white");
        transportador.setVisible(true);
        }
        else{transportador.setVisible(false);
            transportadorBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
            
        }
       
        
        }
    

    @FXML
    private void punteroAC(ActionEvent event) {
        
        if(punteroBT.isSelected()){
            textoBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
            arcoBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
            rectaBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
            puntoBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
            perpendicularesBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
            map_scrollpane.setPannable(true);
            punteroBT.setStyle("-fx-background-color:teal;-fx-text-fill:white");
            punteroBT.setVisible(true);
        }
        else{
            punteroBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
            
        }
        
    }

    @FXML
    private void releaseTransAC(MouseEvent event) {
    }

    @FXML
    private void puntoAC(ActionEvent event) {
        if(puntoBT.isSelected()){
        puntoBT.setStyle("-fx-background-color:teal;-fx-text-fill:white");
        textoBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
        arcoBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
        rectaBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
        punteroBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
        perpendicularesBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
        }
    }

    @FXML
    private void releaseAC(MouseEvent event) {
    }

    @FXML
    private void rectaAC(ActionEvent event) {
        if(rectaBT.isSelected()){
        rectaBT.setStyle("-fx-background-color:teal;-fx-text-fill:white");
        textoBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
        arcoBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
        puntoBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
        punteroBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
        perpendicularesBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
        }
    }

    @FXML
    private void arcoAC(ActionEvent event) {
        if(arcoBT.isSelected()){
        arcoBT.setStyle("-fx-background-color:teal;-fx-text-fill:white");
        textoBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
        puntoBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
        rectaBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
        punteroBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
        perpendicularesBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
        }
    }

    @FXML
    private void textoAC(ActionEvent event) {
        if(textoBT.isSelected()){
        textoBT.setStyle("-fx-background-color:teal;-fx-text-fill:white");
        arcoBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
        puntoBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
        rectaBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
        punteroBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
        perpendicularesBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
        }
    }

    @FXML
    private void perpendicularesAC(ActionEvent event) {
        if(perpendicularesBT.isSelected()){
        perpendicularesBT.setStyle("-fx-background-color:teal;-fx-text-fill:white");
        arcoBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
        puntoBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
        rectaBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
        punteroBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
        textoBT.setStyle("-fx-background-color:white;-fx-text-fill:black");
        }
    }

    @FXML
    private void cerrarSesionAC(ActionEvent event) {
        iniciarSesionBT.setText("Iniciar Sesión");
        this.logged = false;
        registroBT.setDisable(false);
        loggedProperty.setValue(true);
    }

    @FXML
    private void ejerciciosAC(ActionEvent event) throws IOException {
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("VentanaEjercicios.fxml"));
        Parent root = myLoader.load();
        VentanaEjerciciosController myVentanaEjerciciosController = myLoader.<VentanaEjerciciosController>getController();
        
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setTitle("Nauting: Seleccionar Ejercicio");
        stage.setScene(scene);
        myVentanaEjerciciosController.setUser(usuario);
        stage.showAndWait();
    }

    @FXML
    private void estadisticasAC(ActionEvent event) throws IOException, NavegacionDAOException {
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("Estadisticas.fxml"));
        Parent root = myLoader.load();
        EstadisticasController myEstadisticasController = myLoader.<EstadisticasController>getController();
        
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setTitle("Nauting: Seleccionar Ejercicio");
        stage.setScene(scene);
        myEstadisticasController.setVariables(usuario);
        stage.showAndWait();
    }

    @FXML
    private void registroAC(ActionEvent event) throws IOException {
        //Inicio Sesión
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource("Registro.fxml"));
            Parent root = myLoader.load();
            RegistroController myRegistroController = myLoader.<RegistroController>getController();


            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("Nauting: Registrarse");
            stage.setScene(scene);
            stage.showAndWait();
            User usuarioRec = myRegistroController.getLoggedUser();
            if (usuarioRec != null ) { 
                this.usuario = usuarioRec; 
                this.logged = true;
                iniciarSesionBT.setText("Perfil");
                registroBT.setDisable(true);
                loggedProperty.setValue(false);
            }
    }

   

}
