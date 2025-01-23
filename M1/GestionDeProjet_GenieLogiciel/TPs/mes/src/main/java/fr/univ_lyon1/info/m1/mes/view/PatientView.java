package fr.univ_lyon1.info.m1.mes.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import fr.univ_lyon1.info.m1.mes.utils.Observer;
import fr.univ_lyon1.info.m1.mes.controller.ControllerPatient;
import fr.univ_lyon1.info.m1.mes.model.Patient;

/*!
* \brief Classe Vue du Patient
*/
public class PatientView implements Observer {
    private final Pane pane = new VBox();
    private Pane prescriptionPane = new VBox();
    private final Button bSSID = new Button("📋");
    //private final Button bReload = new Button("🗘");
    private final Patient patient;
    private ControllerPatient controller;

    /*!
    * \brief Constructeur de la vue du Patient
    * \param p le Patient
    */
    public PatientView(final Patient p, final ControllerPatient c) {
        this.patient = p;
        this.controller = c;
        controller.setOnActions(this);
        p.addObserver(this);

        final Label namePatient = new Label(p.getName());
        
        final HBox nameBox = new HBox();
        nameBox.getChildren().addAll(namePatient, bSSID/* , bReload*/);

        pane.getChildren().addAll(nameBox, prescriptionPane);
        showPrescriptions();

        pane.setStyle("-fx-border-color: gray;\n"
                + "-fx-border-insets: 5;\n"
                + "-fx-padding: 5;\n"
                + "-fx-border-width: 1;\n");
    }

    /*!
    * \brief Fonction qui affiche les prescriptions du Patient
    */
    public void showPrescriptions() {
        prescriptionPane.getChildren().clear();

        prescriptionPane.getChildren().add(new Label("Prescriptions:\n"));
        prescriptionPane.getChildren().add(controller.controllerGetPrescriptions(patient)); 
    }

    /*!
    * \brief Fonction qui met à jour la vue lors d'un changement dans les données
    */
    @Override
    public void update() {
        this.showPrescriptions();
    }

    /*!
    * \brief Fonction qui récupère le panneau
    */
    public Pane asPane() {
        return pane;
    }

    /*!
    * \brief Fonction qui récupère le Patient
    */
    public Patient getPatient() {
        return this.patient;
    }

    /*!
    * \brief Fonction qui récupère le panneau des prescriptions
    */
    public Pane getPrescriptionPane() {
        return this.prescriptionPane;
    }

    /*!
    * \brief Fonction qui récupère le bouton de copie du SSID
    */
    public Button getbSSID() {
        return this.bSSID;
    }

    /*!
    * \brief Fonction qui récupère le bouton de mise à jour de la vue
    */
    /* 
    public Button getbReload() {
        return this.bReload;
    }
    */
}
