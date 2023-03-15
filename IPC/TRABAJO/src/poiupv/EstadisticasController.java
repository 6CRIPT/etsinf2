/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv;

import DBAccess.NavegacionDAOException;
import java.io.IOException;
import static java.lang.String.valueOf;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import static javafx.scene.input.KeyCode.R;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Session;
import model.User;

/**
 * FXML Controller class
 *
 * @author torre
 */
public class EstadisticasController implements Initializable {

    @FXML
    private Button atrasBT;
    @FXML
    private ListView<String> listViewBT;
    @FXML
    private TextField sesionesTotalesBT;
    @FXML
    private TextField tassaAciertosTotalBT;
    @FXML
    private TextField problemasTotalesBT;
    @FXML
    private TextField aciertosSesionBT;
    @FXML
    private TextField erroresSesionBT;
    @FXML
    private TextField tasaAciertosSesionBT;
    @FXML
    private Label textoBT;
    
    // VARIABLES PROPIAS
    private User usuario;
    private List<Session> sesiones;
    
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Button filtrarBT;
    @FXML
    private DatePicker datePickerBT;
    


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        textoBT.visibleProperty().bind(Bindings.equal(-1,listViewBT.getSelectionModel().selectedIndexProperty()));
        
        listViewBT.getSelectionModel().selectedIndexProperty().
            addListener( (o, oldVal, newVal) -> {
                if (newVal.intValue() == -1) {
                    aciertosSesionBT.setText("");
                    erroresSesionBT.setText("");
                    tasaAciertosSesionBT.setText("");
                } else {
                    int index = newVal.intValue();
                    int aciertos = sesiones.get(index).getHits();
                    int fallos = sesiones.get(index).getFaults();
                    
                    aciertosSesionBT.setText(valueOf(aciertos));
                    erroresSesionBT.setText(valueOf(fallos));
                    double porcent = (double) (aciertos)/(fallos + aciertos) * 100;
                    double tasaAciertos = Math.round(porcent * 100) / 100;
                    tasaAciertosSesionBT.setText(valueOf(tasaAciertos) + "%");
                    
                }
        });
        
        
    }
            

    public void setVariables(User newUser) throws NavegacionDAOException { 
        this.usuario = newUser; 
        
        sesiones = usuario.getSessions();
        if (sesiones.isEmpty()) {
            sesionesTotalesBT.setText(valueOf(0));
            tassaAciertosTotalBT.setText(valueOf(0));
            problemasTotalesBT.setText(valueOf(0));
            
        }
        else {
            int fallosTotales = 0;
            int aciertosTotales = 0;
            int respuestasTotales = 0;
            int i = 0;
            for (i = 0; i<sesiones.size(); i++) {
                Session sesion = sesiones.get(i);
                int fallosSesion = sesion.getFaults();
                int aciertosSesion = sesion.getHits();
                int totales = fallosSesion + aciertosTotales;
            
                fallosTotales += fallosSesion;
                aciertosTotales += aciertosSesion;
                respuestasTotales += totales;
            }    
            
            double porcent = (double) (aciertosTotales)/(respuestasTotales) * 100;
            double tasaAciertos = Math.round(porcent * 100) /100;
        
            sesionesTotalesBT.setText(valueOf(i++));
            tassaAciertosTotalBT.setText(valueOf(tasaAciertos) + "%");
            problemasTotalesBT.setText(valueOf(respuestasTotales));
            
            // Formato
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            List<String> listaArray = new ArrayList<String>();
            ObservableList<String> listaListView = FXCollections.observableList(listaArray);
            
            for (int j = 0; j < sesiones.size(); j++) {
                Session seleccionada = sesiones.get(j);
                LocalDateTime tiempo = seleccionada.getTimeStamp();
                String formatDateTime = tiempo.format(formatter);
                listaListView.add(formatDateTime);
            }
            
            ObservableList<Session> observableList = FXCollections.observableList(sesiones);
            listViewBT.setItems(listaListView);
            
        }
    }
    
    @FXML
    private void atrasAC(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void filtrarAC(ActionEvent event) {
        datePickerBT.setDisable(false);
        datePickerBT.show();
        datePickerBT.setDisable(true);
    }

    @FXML
    private void filtrarPorFecha(ActionEvent event) {
        LocalDate fecha = datePickerBT.getValue();
        System.out.println(fecha.toString());
        this.sesiones = usuario.getSessions();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        List<String> listaArrayString = new ArrayList<String>();
        List<Session> listaArraySession = new ArrayList<Session>();
        ObservableList<String> listaListView = FXCollections.observableList(listaArrayString);
        ObservableList<Session> sesionesFiltradas = FXCollections.observableList(listaArraySession);
        
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            
        for (int i = 0; i < sesiones.size(); i++) {
            
            Session seleccionada = sesiones.get(i);
            
            if (seleccionada.getLocalDate().isAfter(fecha)) {
                sesionesFiltradas.add(seleccionada);
                
                LocalDateTime tiempo = seleccionada.getTimeStamp();
                String formatDateTime = tiempo.format(formatterDate);
                listaListView.add(formatDateTime);
            }
        }
            
        
        listViewBT.setItems(listaListView);
        this.sesiones = sesionesFiltradas;
        
        
        
    }
  
}
