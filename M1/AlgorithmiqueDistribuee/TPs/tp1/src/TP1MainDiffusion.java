import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

public class TP1MainDiffusion {
    public static void main(String[] args){ // On déclare le programme principal
        Topology tp = new Topology(); // Création d’un nouveau système distribué
        tp.setTimeUnit(1500);
        tp.setDefaultNodeModel(NoeudDiffusion.class);
        new JViewer(tp); // On active l’interface graphique de JBotSim
        tp.start(); // On démarre le tout
    }
}