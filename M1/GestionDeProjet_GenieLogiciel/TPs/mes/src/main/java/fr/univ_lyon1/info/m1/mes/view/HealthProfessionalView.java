package fr.univ_lyon1.info.m1.mes.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import fr.univ_lyon1.info.m1.mes.utils.Observer;
import fr.univ_lyon1.info.m1.mes.controller.ControllerHealthProfessional;
import fr.univ_lyon1.info.m1.mes.model.HealthProfessional;
import fr.univ_lyon1.info.m1.mes.model.Patient;
//import javafx.collections.ObservableList;
//import javafx.collections.FXCollections;
//import javafx.scene.control.Toggle;
//import javafx.scene.control.ComboBox;

/*!
* \brief Classe Vue du HealthProfessionnal
*/
public class HealthProfessionalView implements Observer {
    private final VBox pane = new VBox();
    private HealthProfessional healthProfessional;
    private String selectedPatientSSID;
    private final VBox prescriptions = new VBox();
    private ControllerHealthProfessional controller;

    private final HBox strategyBox = new HBox();
    //private ComboBox<String> comboBox = new ComboBox<String>();
    private final ToggleGroup strategyGroup = new ToggleGroup();
    private RadioButton buttonSSID = new RadioButton("SSID");
    private RadioButton buttonName = new RadioButton("Nom");

    private final HBox searchBox = new HBox();
    private TextField textSearchPatient = new TextField();
    private Button buttonSearchPatient = new Button("Search");

    private final HBox prescriptionBox = new HBox();
    private final TextField textPrescription = new TextField();
    private final Button buttonPrescription = new Button("Add");

    /*!
    * \brief Constructeur de la vue du HealthProfessionnal
    * \param hp un HealthProfessionnal
    */
    public HealthProfessionalView(
        final HealthProfessional hp,
        final ControllerHealthProfessional c) {
        pane.setStyle("-fx-border-color: gray;\n"
                + "-fx-border-insets: 5;\n"
                + "-fx-padding: 5;\n"
                + "-fx-border-width: 1;\n");

        this.healthProfessional = hp;

        final Label l = new Label(healthProfessional.getName());
        pane.getChildren().add(l);

        final Label s = new Label("Rechercher par : ");
        buttonSSID.setToggleGroup(strategyGroup);
        buttonName.setToggleGroup(strategyGroup);
        buttonSSID.setSelected(true);
        strategyBox.getChildren().addAll(s, buttonSSID, buttonName);
        //ObservableList<String> strategyList = FXCollections.observableArrayList("SSID", "Nom");
        //comboBox.setItems(strategyList);
        //strategyBox.getChildren().addAll(s, comboBox);
        pane.getChildren().add(strategyBox);

        searchBox.getChildren().addAll(textSearchPatient, buttonSearchPatient);
        pane.getChildren().addAll(searchBox, prescriptions);

        pane.getChildren().add(new Label("Prescribe"));
        prescriptionBox.getChildren().addAll(textPrescription, buttonPrescription);
        pane.getChildren().add(prescriptionBox);
        
        this.controller = c;
        controller.setOnActions(this);
    }

    /*!
    * \brief Fonction qui affiche les prescriptions du patient sélectionné
    */
    public void showPrescriptions() {
        prescriptions.getChildren().clear();

        Patient p = controller.controllerGetPatient(selectedPatientSSID);
        if (p == null) {
            prescriptions.getChildren().add(new Label(
                    "Use search above to see prescriptions"));
            return;
        }
        p.addObserver(this);
        
        prescriptions.getChildren().add(new Label(
                "Prescriptions for " + controller.controllerGetName(p)));

        Pane pView = controller.controllerGetPrescriptions(healthProfessional, p);
        prescriptions.getChildren().add(pView);
    }

    /*!
    * \brief Fonction qui met à jour la vue lors d'un changement dans les données
    */
    @Override
    public void update() {
        this.showPrescriptions();
    }

    /*!
    * \brief Fonction qui récupère le panneau de la vue
    */
    public Pane asPane() {
        return pane;
    }

    /*!
    * \brief Fonction qui récupère le HealthProfessionnal
    */
    public HealthProfessional getHealthProfessional() {
        return this.healthProfessional;
    }

    /*!
    * \brief Fonction qui récupère le patient sélectionné
    */
    public String getSelectedPatientSSID() {
        return this.selectedPatientSSID;
    }

    /*!
    * \brief Fonction qui récupère le panneau des prescriptions
    */
    public VBox getPrescriptions() {
        return this.prescriptions;
    }

    /*!
    * \brief Fonction qui récupère la partie de recherche du patient (barre + bouton)
    */
    public HBox getSearchBox() {
        return this.searchBox;
    }

    /*!
    * \brief Fonction qui récupère la barre de recherche du patient
    */
    public TextField getTextSearchPatient() {
        return this.textSearchPatient;
    }

    /*!
    * \brief Fonction qui récupère la bouton de recherche du patient
    */
    public Button getButtonSearchPatient() {
        return this.buttonSearchPatient;
    }

    /*!
    * \brief Fonction qui affecte un patient sélectionné
    */
    public void setSelectedPatientSSID(final String text) {
        this.selectedPatientSSID = text;
    }

    /*!
    * \brief Fonction qui récupère la partie precription du patient (barre + bouton)
    */
    public HBox getPrescriptionBox() {
        return this.prescriptionBox;
    }

    /*!
    * \brief Fonction qui récupère la barre d'insertion texte de la precription
    */
    public TextField getTextPrescription() {
        return this.textPrescription;
    }

    /*!
    * \brief Fonction qui récupère le bouton d'envoi de la prescription
    */
    public Button getButtonPrescription() {
        return this.buttonPrescription;
    }

    public HBox getStrategyBox() {
        return this.strategyBox;
    }

    /*
    public ComboBox getComboBox() {
        return this.comboBox;
    }*/

    public RadioButton getButtonSSID() {
        return this.buttonSSID;
    }

    public RadioButton getButtonName() {
        return this.buttonName;
    }

}
