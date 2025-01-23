package fr.univ_lyon1.info.m1.mes.controller;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import fr.univ_lyon1.info.m1.mes.model.Patient;
import fr.univ_lyon1.info.m1.mes.model.Prescription;
import fr.univ_lyon1.info.m1.mes.view.PatientView;
//import fr.univ_lyon1.info.m1.mes.model.MES;
import fr.univ_lyon1.info.m1.mes.utils.EasyClipboard;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/*!
* \brief Classe Controller du Patient
*/
public class ControllerPatient {
    //private final MES mes;

    /*!
    * \brief Constructeur du controllerPatient
    * \param pView la Vue du Patient
    */
    public ControllerPatient() {
        //this.mes = m;
    }

    /*!
    * \brief Affecte les actions aux boutons de la vue
    * \param pView la Vue du Patient
    */
    public void setOnActions(final PatientView pView) {
        pView.getbSSID().setOnAction(copySSID(pView.getPatient()));
        //pView.getbReload().setOnAction(reloadPrescriptions(pView));
    }

    /*!
    * \brief Fonction qui récupère le SSID du Patient via le bouton
    * \param p le Patient
    */
    public EventHandler<ActionEvent> copySSID(final Patient p) {
        EventHandler<ActionEvent> copy = new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent event) {
                EasyClipboard.copy(p.getSSID());
            }
        };
        return copy;  
    }

    /*!
    * \brief Fonction qui récupère les prescriptions du Patient
    * \param p le Patient
    */
    public Pane controllerGetPrescriptions(final Patient p) {
        Pane pane = new VBox();
        
        for (final Prescription pr : p.getPrescriptions()) {

            final HBox pView = new HBox(); 

            final Label content = new Label("- From "
                            + pr.getHealthProfessional().getName()
                            + ": " + pr.getContent());

            final Button removeBtn = new Button("x");
            removeBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent event) {
                    p.removePrescription(pr);
                    pane.getChildren().remove(content);
                    pane.getChildren().remove(removeBtn);
                }
            });
            pView.getChildren().add(content);
            pView.getChildren().add(removeBtn);
            pane.getChildren().add(pView);
        }
        return pane;
    }

    /*!
    * \brief Fonction qui met à jour les prescriptions du Patient via le bouton
    * \param pView la vue du Patient
    */
    /* 
    public EventHandler<ActionEvent> reloadPrescriptions(final PatientView pView) {
        EventHandler<ActionEvent> reload = new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent event) {
                pView.showPrescriptions();
            }
        };
        return reload;  
    }
    */
}
