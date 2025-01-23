package App.movement;

import java.util.Collections;
import java.util.*;
import java.util.List;

import App.model.Grid;
import App.model.Node;

public class ShortestPath {
    // There are three kinds of nodes in the grid:
    // -1 normal node
    // > 0 && !=9 obstacle
    // -2 source and destination
    private int[][] nodes;

    public ShortestPath(Grid grid) {
        this.nodes = new int[grid.getWidth()][grid.getHeight()];
        for (int i = 0; i < grid.getWidth(); i++) {
            for (int j = 0; j < grid.getHeight(); j++) {
                this.nodes[i][j] = grid.getGrid()[i][j];
            }
        }
    }

    //Retourne le chemin le plus court pour aller de la source à la destination.
    public List<Node> shortestPath(Position source, Position destination, int toleranceRate) {
        int collisionsOnPath = 0;

        nodes[source.getX()][source.getY()] = -2;
        nodes[destination.getX()][destination.getY()] = -2;

        // key node, value parent
        Map<Node, Node> parents = new HashMap<Node, Node>();
        Node start = new Node(source.getX(), source.getY(), nodes[source.getX()][source.getY()]);
        Node end = new Node(destination.getX(), destination.getY(), nodes[destination.getX()][destination.getY()]);

        List<Node> temp = new ArrayList<Node>();
        temp.add(start);
        parents.put(start, null);

        boolean reachDestination = false;
        while (temp.size() > 0 && !reachDestination) {
            Node currentNode = temp.remove(0);
            List<Node> children = getChildren(currentNode);
            for (Node child : children) {
                // Node can only be visted once
                if (!parents.containsKey(child)) {
                    parents.put(child, currentNode);

                    int value = child.getValue();
                    if (value == -1 || (collisionsOnPath < toleranceRate && value >= 0)) {
                        temp.add(child);
                        if(value >= 0)
                            collisionsOnPath++;
                    } else if (value == -2) {
                        temp.add(child);
                        reachDestination = true;
                        end = child;
                        break;
                    }
                }
            }
        }


        // get the shortest path
        Node node = end;
        List<Node> path = new ArrayList<Node>();
        while (node != null) {
            path.add(0, node);
            node = parents.get(node);
        }

        if(path.get(0).getX() == destination.getX() && path.get(0).getY() == destination.getY())
            Collections.reverse(path);
        return path;
    }

    //Retourne la liste des voisins du node parent qui ne sont pas hors de la grille.
    private List<Node> getChildren(Node parent) {
        List<Node> children = new ArrayList<Node>();
        int x = parent.getX();
        int y = parent.getY();
        if (x - 1 >= 0) {
            Node child = new Node(x - 1, y, nodes[x - 1][y]);
            children.add(child);
        }
        if (y - 1 >= 0) {
            Node child = new Node(x, y - 1, nodes[x][y - 1]);
            children.add(child);
        }
        if (x + 1 < nodes.length) {
            Node child = new Node(x + 1, y, nodes[x + 1][y]);
            children.add(child);
        }
        if (y + 1 < nodes[0].length) {
            Node child = new Node(x, y + 1, nodes[x][y + 1]);
            children.add(child);
        }
        return children;
    }
}
