package App.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import App.GUI;
import App.messages.Mailbox;
import App.messages.Message;
import App.messages.MessageTypes;
import App.movement.Directions;
import App.movement.Position;
import App.movement.PositionGenerator;

public class Grid {
    private int [][] grid;
    private static ArrayList<Agent> agentsList;
    private static ArrayList<Thread> threadsList;
    private int width;
    private int height;
    private GUI gui;
    private PositionGenerator positionGenerator;
    private boolean agentsCommunication;

    public Grid(int w, int h, boolean com) {
        width = w;
        height = h;
        grid = new int[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                grid[i][j] = -1;
            }
        }

        agentsList = new ArrayList<>();
        agentsCommunication = com;
        threadsList = new ArrayList<>();
        positionGenerator = new PositionGenerator(this);
        gui = new GUI(this.getWidth(), this.getHeight(), this);
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[][] getGrid() {
        return grid;
    }

    public ArrayList<Agent> getAgentsList() {
        return agentsList;
    }

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public synchronized boolean isCaseFree(int x, int y) {
        return grid[x][y] == -1;
    }

    //retourne si l'agent est à sa position finale.
    public synchronized boolean isAgentFinalState(int x, int y) {
        for (Agent agent : agentsList) {
            if(agent.getPosFinale().getX() == x && agent.getPosFinale().getY() == y)
                return true;
        }
        return false;
    }

    //retourne si le symbole de l'agent est égal à celui donné.
    public synchronized boolean isAgentLetter(String letter) {
        for(Agent agent : agentsList) {
            if (agent.getLetter().equals(letter))
                return true;
        }
        return false;
    }

    //Ajoute l'agent dans la grille.
    public void addAgent(Agent agent) {
        agentsList.add(agent);
        placeAgent(agent);
        Mailbox.addMailbox(agent);
    }

    //retourne l'agent à la position (x,y)
    private synchronized Agent getAgent(int x, int y) {
        if (grid[x][y] != -1)
            return agentsList.get(grid[x][y]);
        else return null;
    }


    public static ArrayList<Agent> getAgents() {
        return agentsList;
    }


    //Affiche la grille dans le terminal.
    public synchronized void print() {
        for (int[] aMap : grid) {
            for (int x = 0; x < grid[0].length; x++) {
                if (aMap[x] != -1) {
                    if (agentsList.get(aMap[x]).isToFinalState()) {
                        System.out.print("[ " + agentsList.get(aMap[x]).getLetter() + " ]" + "\t");
                    } else {
                        System.out.print(" " + agentsList.get(aMap[x]).getLetter() + " " + "\t");
                    }
                } else {
                    System.out.print(" - " + "\t");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    //Ajoute et démarre les threads.
    public static void runAllAgents() {
        for (Agent agent : agentsList) {
            Thread thread = new Thread(agent);
            threadsList.add(thread);
            thread.start();
        }
    }


    //Arrête les threads et renvoie le nb d'agent à l'état final.
    public static void stopAllAgent() {
        for (Thread thread : threadsList) {
            thread.interrupt();
        }

        System.out.println("Number of agents arrived to destination (" + totalInFinalState() + "/" + Grid.getAgents().size() + ")");
        System.exit(0);
    }

    //Renvoie true si tous les agents sont à l'état final.
    public synchronized boolean isFinished() {
        for (Agent agent : agentsList) {
            if (!agent.isToFinalState())
                return false;
        }
        return true;
    }

    //Déplace l'agent dans la position (prevX, prevY) à la position (nextX, nextY). Retourne true si elle a réussie, sinon false au cas où elle est bloquée par un autre agent.
    public synchronized boolean goToCase(int prevX, int prevY, int nextX, int nextY, Directions direction) {
        Agent agent = getAgent(prevX, prevY);

        if (agent != null) {
            if (grid[nextX][nextY] != -1) {
                if (agentsCommunication) {
                    //Envoie un message aux autres agents qu'il est bloqué afin qu'ils libèrent la case.
                    Message message = new Message(grid[prevX][prevY], grid[nextX][nextY], new Date());
                    message.setContent(MessageTypes.MOVE, direction, new Position(nextX, nextY), agent.getPos());
                    Mailbox.dropAMessage(message);
                    try {
                        TimeUnit.MILLISECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // restore interrupted status
                    }
                    return false;
                } else {
                    //Si la communication n'est pas en place, essaie de contourner le pb en générant une nouvelle pos aléatoire.
                    Position nextPos = positionGenerator.generateRandomMovement(agent.getPos());
                    if (nextPos != null) {
                        changeCase(agent, (int) nextPos.getX(), (int) nextPos.getY());
                        return true;
                    } else {
                        return false;
                    }
                }

            } else {
                changeCase(agent, nextX, nextY);
                return true;
            }
        }

        return false;
    }

    public synchronized boolean goToCase(int prevX, int prevY, int nextX, int nextY) {
        return goToCase(prevX, prevY, nextX, nextY, null);
    }

    //Met à jour la position de l'agent à la position (nextX, nextY) et affiche la nouvelle grille.
    private synchronized void changeCase(Agent agent, int nextX, int nextY) {
        int prevX = agent.getPos().getX();
        int prevY = agent.getPos().getY();
        log(agent, "is moving to [x=" + nextX + ", y=" + nextY + "]" + " " +
                " (Final pos : [x=" + agent.getPosFinale().getX() + ", y=" + agent.getPosFinale().getY() + "])");
        int element = grid[prevX][prevY];
        grid[prevX][prevY] = -1;
        gui.remove(prevX, prevY);
        grid[nextX][nextY] = element;

        agent.setPos(new Position(nextX, nextY));
        gui.move(nextX, nextY, agent);

        if (agent.isToFinalState()) {
            log(agent, "is arrived to destination (" + this.totalInFinalState() + "/" + agentsList.size() + ")");
        }

        print();
    }

    //Renvoie le nombre d'agent à l'état final.
    public synchronized static int totalInFinalState() {
        int count = 0;
        for (int i = 0; i < agentsList.size(); i++) {
            if (agentsList.get(i).isToFinalState())
                count++;
        }
        return count;
    }

    //place l'agent dans la grille et initialise la grille graphique initiale et finale.
    private synchronized void placeAgent(Agent agent) {
        grid[(int) agent.getPosInitiale().getX()][(int) agent.getPosInitiale().getY()] = agent.getId();
        gui.init((int) agent.getPosInitiale().getX(), (int) agent.getPosInitiale().getY(), agent);
        gui.finale((int) agent.getPosFinale().getX(), (int) agent.getPosFinale().getY(), agent);
    }

    //Retourne la direction selon l'angle.
    public Directions getDirectionFromAngle(double angle) {
        if (angle < 45) {
            return Directions.RIGHT;
        } else if (angle < 135) {
            return Directions.DOWN;
        } else if (angle < 225) {
            return Directions.LEFT;
        } else if (angle < 315) {
            return Directions.UP;
        } else {
            return Directions.RIGHT;
        }
    }

    //Retourne l'angle entre les 2 vecteurs target et base en degré de 0 à 360.
    public double getAngleBetweenVectors(Position target, Position base) {
        double angle = (float) Math.toDegrees(Math.atan2(target.getX() - base.getX(), target.getY() - base.getY()));

        if (angle < 0) {
            angle += 360;
        }

        return angle;
    }

    //Vérfie si la case est occupée. Retourne true si il y a un agent sur la case (x,y)
    public boolean isBlocked(double x, double y) {
        int i = grid[(int) x][(int) y];
        return i >= 0;
    }

    //Vérifie si les coordonnées (x,y) sont hors de la grille. Retourne vraie si c'est le cas.
    public boolean isOutOfMap(double x, double y) {
        if (x < 0 || y < 0) {
            return true;
        }
        return grid.length <= x || grid[0].length <= y;
    }

    private synchronized void log(Agent agent, String message) {
        System.out.println(agent.getLetter() + " " + message);
    }
}
