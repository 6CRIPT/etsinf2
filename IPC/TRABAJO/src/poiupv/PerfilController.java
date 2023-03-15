/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv;

import DBAccess.NavegacionDAOException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import model.User;

/**
 * FXML Controller class
 *
 * @author Candel
 */
public class PerfilController implements Initializable {
    
    private Stage stage;
    private Scene scene;
    private Parent root;
    private User usuario;

    @FXML
    private ImageView imagen;
    @FXML
    private Button btImgCambiar;
    @FXML
    private TextField nomUsuario;
    @FXML
    private TextField correoActual;
    @FXML
    private TextField contrasenaActual;
    @FXML
    private DatePicker fechaActual;
    @FXML
    private Button bteditar;
    @FXML
    private Button btGuardar;
    @FXML
    private Button atras;
    @FXML
    private Label error;
    
    private LocalDate today;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btGuardar.setDisable(true);
        
        
        
        // TODO
    }
    
    public static final LocalDate LOCAL_DATE (String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        return localDate;
    }
    
    public void setUser(User user) { 
        
        this.usuario = user;
    
        nomUsuario.setText(usuario.getNickName());
        correoActual.setText(usuario.getEmail());
        contrasenaActual.setText(usuario.getPassword());
        
        String fecha = usuario.getBirthdate().toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(fecha, formatter);
        fechaActual.setValue(usuario.getBirthdate());
        
        imagen.setImage(usuario.getAvatar());
        
        fechaActual.setDayCellFactory(picker -> new DateCell() {
        public void updateItem(LocalDate date, boolean empty) {
            super.updateItem(date, empty);
            today = LocalDate.now();

            setDisable(empty || date.compareTo(today) > 0 );
        }});
    
    }

    

    @FXML
    private void btVolverAC(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void btImgCambiarAC(ActionEvent event) throws IOException, NavegacionDAOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir fichero");
        fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.gif"),
        new ExtensionFilter("Sonidos", "*.wav", "*.mp3", "*.aac"),
        new ExtensionFilter("Todos", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(
        ((Node)event.getSource()).getScene().getWindow());
        if (selectedFile != null) {
        Image nuevoAvatar = new Image(selectedFile.toURI().toString());
        usuario.setAvatar(nuevoAvatar);
        }
        
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
    private void bteditarAC(ActionEvent event) {
        correoActual.setEditable(true);
        contrasenaActual.setEditable(true);
        fechaActual.setEditable(true);
        btGuardar.setDisable(false);
        
        
    }

    @FXML
    private void btGuardarAC(ActionEvent event) throws NavegacionDAOException, IOException { 
        String errores = "";
        error.setText("");
        
        if(correoActual.getText() != ""){
        
            String emailUsuario = correoActual.getText();
            if ( User.checkEmail(emailUsuario) ) {
                usuario.setEmail(emailUsuario);   
            }else{ errores = errores + "El correo no parece un correo electrónico.";
            }
        }
        
        if(contrasenaActual.getText() != ""){
            
            String contrasenaUsuario = contrasenaActual.getText();
            if ( User.checkPassword(contrasenaUsuario) ) {

                usuario.setPassword(contrasenaUsuario);
            }else if(errores.length()==0){ errores = errores + "La contraseña no cumple los requisitos.";
                    }else {errores = errores + "\nLa contraseña no cumple los requisitos.";}
        
        }
        
        LocalDate date = fechaActual.getValue();
        if ( date != null ) {  
            int dia = today.getDayOfYear(); //actual
            int year = today.getYear(); //actual
            int diap = fechaActual.getValue().getDayOfYear();
            int añop = fechaActual.getValue().getYear();
            if(añop > year-16){
                if (errores.length() > 0) { errores += "\nDebes ser mayor de 16 años.";}
                else { errores += "Debes ser mayor de 16 años."; }
            } else if (añop == year-16){
                if(diap > dia){
                    if (errores.length() > 0) { errores += "\nDebes ser mayor de 16 años.";}
                    else { errores += "Debes ser mayor de 16 años."; }
                }
            } else { usuario.setBirthdate(date); }
            

        }
        error.setText(errores);
        if(errores.length()==0){
        //cambia ventana
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
            
    }
        
        }
        
        
        
        
        
        
        
        



