package fr.univ_lyon1.info.m1.mes.controller;

import fr.univ_lyon1.info.m1.mes.model.MES;
import fr.univ_lyon1.info.m1.mes.model.Patient;
import fr.univ_lyon1.info.m1.mes.model.HealthProfessional;
import fr.univ_lyon1.info.m1.mes.view.JfxView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.util.List;

/*!
* \brief Classe Controller de JfxView
*/
public class ControllerJfxView {
    private final MES mes;

    /*!
    * \brief Constructeur du controller de JfxView
    */
    public ControllerJfxView(final MES m) {
        this.mes = m;
    }

    /*!
    * \brief Affecte les actions aux boutons de la vue 
    * \param button le bouton d'action
    * \param nameT le champ texte du nom
    * \param ssidT le champ texte du ssid
    * \param view la vue 
    */
    public void setActionOnButton(
        final Button button, 
        final TextField nameT, 
        final TextField ssidT, 
        final JfxView view) {
            mes.addObserver(view);
            button.setOnAction(createP(nameT, ssidT, view));
    }

    /*!
    * \brief Fonction de creation d'un patient si action
    * \param nameT le champ texte du nom
    * \param ssidT le champ texte du ssid
    * \param view la vue 
    */
    public EventHandler<ActionEvent> createP(
        final TextField name, 
        final TextField ssid, 
        final JfxView view) {
        EventHandler<ActionEvent> creation = new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent event) {
                mes.createPatient(name.getText(), ssid.getText());
            }
        };
        return creation;  
    }

    /*!
    * \brief Fonction qui récupère un patient par son SSID
    */
    public Patient controllerGetPatientById(final String ssID) {
        return mes.getPatient(ssID);
    }

    /*!
    * \brief Fonction qui récupère la liste des patients
    */
    public List<Patient> controllerGetPatients() {
        return mes.getPatients();
    }

    /*!
    * \brief Fonction qui récupère la liste des HealthProfessionnal
    */
    public List<HealthProfessional> controllerGetHPs() {
        return mes.getHealthProfessional();
    }
}
