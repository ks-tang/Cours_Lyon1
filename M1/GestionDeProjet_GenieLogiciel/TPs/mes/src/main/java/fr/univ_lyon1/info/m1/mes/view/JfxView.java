package fr.univ_lyon1.info.m1.mes.view;

import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import fr.univ_lyon1.info.m1.mes.controller.ControllerJfxView;
import fr.univ_lyon1.info.m1.mes.controller.ControllerHealthProfessional;
import fr.univ_lyon1.info.m1.mes.controller.ControllerPatient;
import fr.univ_lyon1.info.m1.mes.model.Patient;
import fr.univ_lyon1.info.m1.mes.model.HealthProfessional;
import fr.univ_lyon1.info.m1.mes.model.MES;
import fr.univ_lyon1.info.m1.mes.utils.Observer;

/*!
\brief Classe JfxView
*/
public class JfxView implements Observer {
    private final HBox root = new HBox(10);
    private Pane patients = new VBox();
    private Pane healthPro = new VBox();
    private final MES mes;
    private ControllerJfxView cJfxView;
    private ControllerHealthProfessional cHealthProfessional;
    private ControllerPatient cPatient;
    private final Scene scene1;
    private final Scene scene0;

    /**
     * Create the main view of the application.
     */
    public JfxView(final MES m, final Stage stage,
            final int width, final int height) {
        this.mes = m;
        this.cJfxView = new ControllerJfxView(mes);
        this.cHealthProfessional = new ControllerHealthProfessional(mes);
        this.cPatient = new ControllerPatient();
        
        stage.setTitle("Mon Espace Santé");

        //Scene1
        createPatientsWidget();
        addPatientWidget();
        root.getChildren().add(patients);

        createHPWidget();
        root.getChildren().add(healthPro); 

        HBox.setHgrow(patients, Priority.SOMETIMES);
        HBox.setHgrow(healthPro, Priority.ALWAYS);
        this.scene1 = new Scene(root, width, height);

        //Scene0
        VBox welcomePage = new VBox();
        welcomePage.setAlignment(Pos.CENTER);

        final Label welcomeTitle = new Label("Bienvenue sur la plateforme Mon Espace Santé");
        Button welcomeButton = new Button("Entrer");
        welcomeButton.setOnAction(e -> stage.setScene(this.scene1));

        welcomePage.getChildren().addAll(welcomeTitle, welcomeButton);
        this.scene0 = new Scene(welcomePage, width, height);

        //Affichage de la scene
        stage.setScene(scene0);
        stage.show();
    }

    /*!
    * \brief Fonction qui met à jour la vue lors d'un changement dans les données
    */
    @Override
    public void update() {
        createPatientsWidget();
        addPatientWidget();
        createHPWidget();
    }

    
    /*!
    * \brief Fonction qui crée un widget pour chaque HealthProfessionnal
    */
    private void createHPWidget() {
        for (HealthProfessional hp : cJfxView.controllerGetHPs()) {
            HealthProfessionalView hpv = new HealthProfessionalView(hp, cHealthProfessional); 
            healthPro.getChildren().add(hpv.asPane());
        }
    }

    /*!
    * \brief Fonction qui crée un widget pour chaque Patient
    */
    private void createPatientsWidget() {
        patients.getChildren().clear(); 
        for (Patient p : cJfxView.controllerGetPatients()) {
            final PatientView pv = new PatientView(p, cPatient); 
            patients.getChildren().add(pv.asPane()); 
        }
    }

    /*!
    * \brief Fonction qui crée un widget pour créer un nouveau Patient
    */
    private void addPatientWidget() {
        final Pane newPatientPane = new VBox();
        newPatientPane.setStyle("-fx-border-color: gray;\n"
                + "-fx-border-insets: 5;\n"
                + "-fx-padding: 5;\n"
                + "-fx-border-width: 1;\n");

        final Label ajoutPatient = new Label("Ajouter un patient :");
        final Label nameL = new Label("Name: ");
        final TextField nameT = new TextField();
        final Label ssIDL = new Label("ssID: ");
        final TextField ssIDT = new TextField();
        final Button newP = new Button("New");
        newPatientPane.getChildren().addAll(
                new HBox(ajoutPatient),
                new HBox(nameL, nameT),
                new HBox(ssIDL, ssIDT),
                newP);
        cJfxView.setActionOnButton(newP, nameT, ssIDT, this);
        patients.getChildren().add(newPatientPane);
    }

    


}
