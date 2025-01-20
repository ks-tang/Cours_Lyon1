package fr.univ_lyon1.info.m1.mes.controller;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.util.ArrayList;
import java.util.List;
import fr.univ_lyon1.info.m1.mes.utils.EasyAlert;
import fr.univ_lyon1.info.m1.mes.model.HealthProfessional;
import fr.univ_lyon1.info.m1.mes.model.Patient;
import fr.univ_lyon1.info.m1.mes.model.Prescription;
import fr.univ_lyon1.info.m1.mes.view.HealthProfessionalView;
import fr.univ_lyon1.info.m1.mes.model.MES;

/*!
* \brief Classe Controller pour HealthProfessionnal
*/
public class ControllerHealthProfessional {
    private final MES mes;

    /*!
    * \brief Constructeur de la classe
    * \param hpView la vue du HealthProfessionnal
    */
    public ControllerHealthProfessional(final MES mes) {
        this.mes = mes;
    }

    /*!
    * \brief Affecte les actions aux boutons de la vue 
    * \param hpView la Vue du HealthProfessional
    */
    public void setOnActions(final HealthProfessionalView hpView) {
        hpView.getButtonSearchPatient().setOnAction(search(hpView));
        hpView.getButtonPrescription().setOnAction(addPrescription(hpView));
        addPredefPrescriptions(hpView);
    }

    /*!
    * \brief Fonction qui recherche un patient à partir du SSID et affiche ses prescriptions
    * \param hpView la vue du HealthProfessionnal
    */
    public EventHandler<ActionEvent> search(final HealthProfessionalView hpView) {
        final EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent event) {
                String text = hpView.getTextSearchPatient().getText().trim();
                if (text.equals("")) {
                    return; 
                }

                if (hpView.getButtonName().isSelected()) {
                    text = mes.getSSIDbyName(text);
                } 
                /*
                if (hpView.getComboBox().getValue() == "Nom") {
                    text = mes.getSSIDbyName(text);
                    hpView.setSelectedPatientSSID(text);
                } else if (hpView.getComboBox().getValue() == "SSID") {
                    hpView.setSelectedPatientSSID(text);
                } else {
                    return;
                }
                if (text.equals("")) {
                    return; 
                }*/ 
                hpView.setSelectedPatientSSID(text);
                hpView.update();
                hpView.getTextSearchPatient().setText("");
                hpView.getTextSearchPatient().requestFocus();
            }
        };
        return handler;
    }

    /*!
    * \brief Fonction qui ajoute une prescription à un patient
    * \param prescription la Prescription
    * \param hpView la vue du HealthProfessionnal
    */
    public void prescribe(final String prescription, final HealthProfessionalView hpView) {
        if (hpView.getSelectedPatientSSID() == null) {
            EasyAlert.alert("Please select a patient first");
            return;
        }
        mes.getPatient(hpView.getSelectedPatientSSID())
            .addPrescription(hpView.getHealthProfessional(), prescription);
        
        hpView.showPrescriptions();
    }
  
    /*!
    * \brief Fonction qui ajoute une prescription à un patient
    * \param hpView la vue du HealthProfessionnal
    */
    public EventHandler<ActionEvent> addPrescription(final HealthProfessionalView hpView) {
        final EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent event) {
                final String text = hpView.getTextPrescription().getText().trim();
                if (text.equals("")) {
                    return; 
                }
                hpView.getTextPrescription().setText(""); 
                hpView.getTextPrescription().requestFocus(); 
                prescribe(text, hpView);
            }
        };
        return handler;
    }

    /*!
    * \brief Fonction qui ajoute les prescriptions prédéfinies aux professionnels
    * \param hpView la vue du HealthProfessionnal
    */
    public void addPredefPrescriptions(final HealthProfessionalView hpView) {
        List<String> predefPrescr = new ArrayList<>();
        for (final String p : hpView.getHealthProfessional().getPredefPrescr()) {
            predefPrescr.add(p);
        }
        for (final String p : predefPrescr) {
            final Button predefPrescrB = new Button(p);
            predefPrescrB.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent event) {
                    prescribe(p, hpView);
                }
            });
            hpView.asPane().getChildren().add(predefPrescrB);
        }
    }

    /*!
    * \brief Fonction qui récupère le nom d'un patient
    * \param p un Patient
    */
    public String controllerGetName(final Patient p) {
        return p.getName();
    }

    /*!
    * \brief Fonction qui récupère les informations d'un patient 
    * \param ssID numéro d'identification d'un patient
    */
    public Patient controllerGetPatient(final String ssID) {
        return mes.getPatient(ssID);
    }

    /*!
    * \brief Fonction qui récupère le nom d'un professionnel
    * \param hp un HealthProfessionnal
    */
    public String controllerGetName(final HealthProfessional hp) {
        return hp.getName();
    }

    /*!
    * \brief Fonction qui récupère les prescriptions d'un professionnel pour un patient
    * \param hp un HealthProfessionnal
    * \param p un Patient
    */
    public Pane controllerGetPrescriptions(final HealthProfessional hp, final Patient p) {
        Pane pane = new VBox();

        for (final Prescription pr : p.getPrescriptions(hp)) {
            final HBox pView = new HBox(); 
            final Label content = new Label(
                    "- " + pr.getContent());
            final Button removeBtn = new Button("x");
            removeBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent event) {
                    p.removePrescription(pr);
                    pView.getChildren().remove(content);
                    pView.getChildren().remove(removeBtn);
                }
            });
            pView.getChildren().add(content);
            pView.getChildren().add(removeBtn);
            pane.getChildren().add(pView);
        }

        return pane;
    }
}
