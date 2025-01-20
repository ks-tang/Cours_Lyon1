import java.util.ArrayList;
import java.util.List;
import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Point;
import io.jbotsim.core.Topology;
import io.jbotsim.core.Link.Orientation;
import io.jbotsim.ui.JViewer;

public class TP3Main {
    public static void main(String[] args){ // On déclare le programme principal
        Topology tp = new Topology(); // Création d’un nouveau système distribué
        tp.setDefaultNodeModel(TP3Noeud.class);  

        Point centre = new Point(tp.getWidth()/2, tp.getHeight()/2);
        double rayon = 40;
        for (Integer i = 0; i < TP3Noeud.TAILLE_ANNEAU; i++) {
            Node noeud = new TP3Noeud();
            double alpha = (2 * Math.PI / TP3Noeud.TAILLE_ANNEAU) * i;
            
            double x = rayon * Math.cos(alpha) + centre.x;
            double y = rayon * Math.sin(alpha) + centre.y;

            tp.addNode(x, y, noeud);
        }

        tp.disableWireless();

        List<Integer> melangeur = new ArrayList<>();
        for (Integer i = 0; i < TP3Noeud.TAILLE_ANNEAU; i++) {
            melangeur.add(i);
        }
        for (Integer i = 0; i < tp.getNodes().size(); i++) {
            // Création du nouveau lien
            for (Integer j = i; j < tp.getNodes().size(); j++) {
                tp.addLink(new Link(
                    tp.getNodes().get(i), 
                    tp.getNodes().get(j), 
                    Orientation.UNDIRECTED
                ));
            }
            
            // Changement de l'identifiant du premier lien
            double rand = Math.random() * melangeur.size();
            tp.getNodes().get(i).setID(melangeur.get((int)rand));
            melangeur.remove((int)rand);
        }
        new JViewer(tp); // On active l’interface graphique de JBotSim
        tp.setTimeUnit(1000);
        tp.start(); // On démarre le tout
    }
}