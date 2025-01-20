import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Point;
import io.jbotsim.core.Topology;
import io.jbotsim.core.Link.Orientation;

public class TP2MainItaiRodeh100Al {
    public static void main(String[] args){ // On déclare le programme principal
        Topology tp = new Topology(); // Création d’un nouveau système distribué
        tp.disableWireless();

        tp.setDefaultNodeModel(TP2NoeudItaiRodeh100Al.class);  
        tp.setOrientation(Link.Orientation.DIRECTED);

        Point centre = new Point(tp.getWidth()/2, tp.getHeight()/2);
        double rayon = 40;
        for (Integer i = 0; i < TP2NoeudItaiRodeh100Al.TAILLE_ANNEAU; i++) {
            Node noeud = new TP2NoeudItaiRodeh100Al();
            double alpha = (2 * Math.PI / TP2NoeudItaiRodeh100Al.TAILLE_ANNEAU) * i;
            
            double x = rayon * Math.cos(alpha) + centre.x;
            double y = rayon * Math.sin(alpha) + centre.y;

            tp.addNode(x, y, noeud);
        }

        for (Integer i = 0; i < tp.getNodes().size(); i++) {
            // Création du nouveau lien
            tp.addLink(new Link(
                tp.getNodes().get(i), 
                tp.getNodes().get((i+1) % TP2NoeudItaiRodeh100Al.TAILLE_ANNEAU), 
                Orientation.DIRECTED
            ));
        }       
        
        // new JViewer(tp); // On active l’interface graphique de JBotSim
        // tp.setTimeUnit(1000);
        tp.start(); // On démarre le tout
    }
}