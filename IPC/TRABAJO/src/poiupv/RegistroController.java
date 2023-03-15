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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Navegacion;
import model.User;

/**
 * FXML Controller class
 *
 * @author torre
 */
public class RegistroController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField usuario;
    @FXML
    private TextField correo;
    @FXML
    private PasswordField contrasena;
    @FXML
    private PasswordField contrasena2;
    @FXML
    private DatePicker fecha;
    @FXML
    private Button crearBT;
    @FXML
    private Label error;
    @FXML
    private Tooltip hintUsuario;
    @FXML
    private Tooltip hintContraseña;
    @FXML
    private Tooltip hintConfirmar;
    
    private Navegacion navegacion;
    
    public String rutaImagen = "resources/avatars/default.png";
    
    public Image avatar;
    
    @FXML
    private Button avatarBT1;
    @FXML
    private ImageView imagen;
    @FXML
    private Button botonAtras;
    
    private User newUser;
    
    private LocalDate today;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        fecha.setDayCellFactory(picker -> new DateCell() {
        public void updateItem(LocalDate date, boolean empty) {
            super.updateItem(date, empty);
            today = LocalDate.now();

            setDisable(empty || date.compareTo(today) > 0 );
        }});
        hintUsuario.setText("Debe tener entre 6 y 15 caracteres. \nPuede contener mayúsculas y minúsculas, \"-\" y \"_\".");
        hintContraseña.setText("Debe contener entre 8 y 20 caracteres. \nContiene al menos una letra mayúscula. \nContiene al menos una letra minúscula. \nContiene al menos un número. \nContiene un caracter especial (!@#$%&*()-+=). \nNo contiene ningún espacio en blanco.");
    
        try {
            Navegacion naveg = Navegacion.getSingletonNavegacion();
            navegacion = naveg;
        } catch (NavegacionDAOException ex) {
            Logger.getLogger(RegistroController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        avatar = new Image(rutaImagen); 
        imagen.setImage(avatar);
        
    }
    
    public User getLoggedUser() { return newUser; }    
    

    @FXML
    private void crearAC(ActionEvent event) throws NavegacionDAOException, IOException {
        
        
        String errores = "";
        
        String nombreUsuario = usuario.getText();
        String emailUsuario = correo.getText();
        String contrasenaUsuario = contrasena.getText();
        String confirmarContrasena = contrasena2.getText();
        LocalDate date = fecha.getValue();
        
        
        if (navegacion.exitsNickName(nombreUsuario)) { 
            if (errores.length() > 0) { errores += "\nEl nombre de usuario ya existe. Pruebe con otro distinto.";}
            else { errores += "El nombre de usuario ya existe. Pruebe con otro distinto."; } 
        }
        
        if ( !User.checkNickName(nombreUsuario) ) {
            if (errores.length() > 0) { errores += "\nEl nombre de usuario no es válido. Compruebe los requisitos manteniendo el ratón por encima de la casilla.";}
            else { errores += "El nombre de usuario ya existe. Pruebe con otro distinto."; }
        }
        
        if ( !User.checkEmail(emailUsuario) ) {
            if (errores.length() > 0) { errores += "\nEl correo introducido no es válido. Pruebe con otro distinto.";}
            else { errores += "El correo introducido no es válido. Pruebe con otro distinto."; }
        }
        
        if ( !User.checkPassword(contrasenaUsuario) ) {
            if (errores.length() > 0) { errores += "\nLa contraseña no es válida. Compruebe los requisitos manteniendo el ratón por encima de la casilla.";}
            else { errores += "La contraseña no es válida. Compruebe los requisitos manteniendo el ratón por encima de la casilla."; }
        }
        
        if ( !contrasenaUsuario.equals(confirmarContrasena) ) {
            if (errores.length() > 0) { errores += "\nLas contraseñas no coinciden.";}
            else { errores += "Las contraseñas no coinciden."; }
        }
        
        if ( fecha.getValue() == null ) {
            if (errores.length() > 0) { errores += "\nDebes introducir tu fecha de nacimiento.";}
            else { errores += "Debes introducir tu fecha de nacimiento."; }
        } else {
        
            if (this.today == null) { today = LocalDate.now(); }

            int dia = today.getDayOfYear(); //actual
            int year = today.getYear(); //actual
            int diap = fecha.getValue().getDayOfYear();
            int añop = fecha.getValue().getYear();
            if(añop > year-16){
                if (errores.length() > 0) { errores += "\nDebes ser mayor de 16 años.";}
                else { errores += "Debes ser mayor de 16 años."; }
            } else if (añop == year-16){
                if(diap > dia){
                    if (errores.length() > 0) { errores += "\nDebes ser mayor de 16 años.";}
                    else { errores += "Debes ser mayor de 16 años."; }
                }
        }
    }
        
        
        if (errores.length() > 0) { error.setText(errores); }
        else {
            newUser = navegacion.registerUser(nombreUsuario, emailUsuario, contrasenaUsuario, avatar, date);
            

            
            
            
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.close();

        }
        
    }


    @FXML
    private void comprobarValidezUsuario(KeyEvent event) {
        String nombreUsuario = usuario.getText();
        
        if ( !User.checkNickName(nombreUsuario) ) {
            error.setText("El nombre de usuario no es válido. Compruebe los requisitos manteniendo el ratón por encima de la casilla.");
        } else {
            if (!navegacion.exitsNickName(nombreUsuario)) { error.setText(""); }
            else { error.setText("Ya existe ese usuario. Pruebe con otro."); }
        }    
    }
    

    @FXML
    private void comprobarValidezCorreo(KeyEvent event) {
        String emailUsuario = correo.getText();
        if ( !User.checkEmail(emailUsuario) ) {
            error.setText("El correo introducido no es válido. Pruebe con otro distinto.");
            
        } else { error.setText(""); }
    }

    @FXML
    private void comprobarValidezContraseña(KeyEvent event) {
        String contrasenaUsuario = contrasena.getText();
        if ( !User.checkPassword(contrasenaUsuario) ) {
            error.setText("La contraseña no es válida. Compruebe los requisitos manteniendo el ratón por encima de la casilla.");
        } else { error.setText(""); }
    }

    @FXML
    private void comprobarValidezConfirmar(KeyEvent event) {
        String contrasenaUsuario = contrasena.getText();
        String confirmarContrasena = contrasena2.getText();
        
        if (contrasenaUsuario.equals(confirmarContrasena)) { error.setText(""); }
        else { error.setText("Las contraseñas no coinciden."); }
    }

    @FXML
    private void elegirAvatar(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir fichero");
        fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.gif"),
        new FileChooser.ExtensionFilter("Sonidos", "*.wav", "*.mp3", "*.aac"),
        new FileChooser.ExtensionFilter("Todos", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(
        ((Node)event.getSource()).getScene().getWindow());
        if (selectedFile != null) {
        Image nuevoAvatar = new Image(selectedFile.toURI().toString());
        imagen.setImage(nuevoAvatar);
        avatar = nuevoAvatar;
        }
    }

    

    @FXML
    private void atrasAC(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
         
    }
    

