package App.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import App.messages.Mailbox;
import App.messages.Message;
import App.messages.MessageTypes;
import App.movement.Directions;
import App.movement.Movements;
import App.movement.Position;
import App.movement.PositionGenerator;
import App.movement.ShortestPath;

public class Agent implements Runnable{
    private int id;
    private Position posInitiale;
    private Position pos;
    private Position posFinale;

    private String letter;

    private Grid grid;

    private PositionGenerator positionGenerator;

    private Movements lastMovements = new Movements();

    public Agent(Grid grid, int id, String letter) {
        this.id = id;
        this.grid = grid;
        this.letter = letter;

        positionGenerator = new PositionGenerator(grid);
        this.setPosInitiale(positionGenerator.rand(true));
        this.setPosFinale(positionGenerator.rand(false));
        this.setPos(this.getPosInitiale());

        grid.addAgent(this);
    }

    public Agent(Grid grid, int id) {
        this(grid, id, new LetterGenerator(grid).rand());
    }

    public int getId() {
        return id;
    }

    public Position getPosInitiale() {
        return posInitiale;
    }

    public Position getPos() {
        return pos;
    }

    public Position getPosFinale() {
        return posFinale;
    }

    public void setPosInitiale(Position p) {
        this.posInitiale = p;
    }

    public void setPos(Position p) {
        this.pos = p;
    }

    public void setPosFinale(Position p) {
        this.posFinale = p;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String l) {
        this.letter = l;
    }

    //Retourne si l'agent est à sa position finale.
    public synchronized boolean isToFinalState() {
        return this.pos.getX() == this.posFinale.getX()
                && this.pos.getY() == this.posFinale.getY();
    }

    //Chaque thread lance cette fonction et qui tourne tant que tous les agents ne sont pas dans l'état fini.
    public void run() {
        log("Thread " + Thread.currentThread().getId() + " is running : " + this.letter);

        while (!grid.isFinished()) {
            communicate();
            if(!this.isToFinalState()) {
                Directions dir = thinkDirection();
                Position nextPos = chooseDirection(dir);
                move(nextPos, dir);
            }
        }

        log("Thread " + Thread.currentThread().getId() + " is stopping : " + this.letter);
    }

    private void log(String message) {
        System.out.println(this.letter + " " + message);
    }

    //Retourne la direction dans laquelle l'agent doit aller pour rejoindre sa destination.
    private Directions thinkDirection() {
        if (lastMovements.detectLoop()) {
            log("Found repeating sequence");
            //generate random position event if another agent is present
            Position newPos = positionGenerator.generateRandomNotFreeMovement(this.pos, new ArrayList<>());
            if (newPos != null)
                return grid.getDirectionFromAngle(grid.getAngleBetweenVectors(newPos, pos));
        }

        return grid.getDirectionFromAngle(grid.getAngleBetweenVectors(posFinale, pos));
    }

    //Retourne la position voisine en fonction du voisin.
    private Position getPositionFromDirection(Directions direction) {
        int vx;
        int vy;

        if (direction == Directions.UP) {
            vx = -1;
            vy = 0;
        } else if (direction == Directions.DOWN) {
            vx = 1;
            vy = 0;
        } else if (direction == Directions.RIGHT) {
            vx = 0;
            vy = 1;
        } else if (direction == Directions.LEFT) {
            vx = 0;
            vy = -1;
        } else {
            vx = 0;
            vy = 0;
        }

        Position prevPos = this.getPos();
        return new Position(prevPos.getX() + vx, prevPos.getY() + vy);
    }

    //Déplace l'agent dans la position donnée en paramètre. Si l'agent a bien bougé, on ajoute le déplacement à la liste des mouvements.
    private void move(Position position, Directions dir) {
        Position origin = new Position(this.pos.getX(), this.pos.getY());
        boolean hasMoved = grid.goToCase(origin.getX(), origin.getY(), position.getX(), position.getY(), dir);
        if (hasMoved) {
            lastMovements.add(origin, position);
        }
    }

    //Vérifie qu'il n'y a pas de messages avec l'id de l'agent dans la mailbox. s'il y en a, l'agent se déplace.
    private void communicate() {
        while (Mailbox.containMessages(this.id)) {

            Message message = Mailbox.getMessage(this.id);
            //Si l'agent reçoit le message de se déplacer à un endroit qui n'est pas sa position actuelle.
            if (message.getType().equals(MessageTypes.MOVE.getValue()) && this.pos != message.getPosition()) {
                    Position nextPos = positionGenerator.generateRandomMovement(this.pos);
                    if (nextPos != null) {
                        boolean hasMoved = grid.goToCase((int) this.pos.getX(), (int) this.pos.getY(), (int) nextPos.getX(), (int) nextPos.getY());
                        if (hasMoved) {
                            //Si le déplacement a réussi, supprime le message de la mailbox.
                            Mailbox.deleteMessage(this.id);
                            try {
                                TimeUnit.MILLISECONDS.sleep(5);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
            } else if (!grid.isFinished()) {
                    ArrayList<Position> blacklist = new ArrayList<Position>();
                    //L'agent ne peut pas se déplacer sur la case envoyée par celui qui a envoyé le message.
                    blacklist.add(message.getSenderPosition());
                    //L'agent cherche une nouvelle position pour libérer le passage à l'envoyeur.
                    Position nextPosNotFree = positionGenerator.generateRandomNotFreeMovement(this.pos, blacklist);
                    boolean hasMoved = grid.goToCase((int) this.pos.getX(), (int) this.pos.getY(), (int) nextPosNotFree.getX(), (int) nextPosNotFree.getY());
                    if (hasMoved) {
                        Mailbox.deleteMessage(this.id);
                    }
                }
            }
        }
    }


    //Choisit la direction selon laquelle l'agent se déplace en fonction de certaines conditions.
    private Position chooseDirection(Directions direction) {

        //si il existe un chemin libre
        ShortestPath pathFinder = new ShortestPath(this.grid);
        List<Node> shortestPath = pathFinder.shortestPath(this.pos, this.posFinale, 0);
        if (shortestPath != null && shortestPath.size() > 1) {
            return new Position(shortestPath.get(1).getX(), shortestPath.get(1).getY());
        }

        //si il existe un chemin bloqué que par 1 seul élement
        List<Node> shortestBlockingPath = pathFinder.shortestPath(this.pos, this.posFinale, 1);
        if(shortestBlockingPath != null && shortestBlockingPath.size() > 1){
            return new Position(shortestBlockingPath.get(1).getX(), shortestBlockingPath.get(1).getY());
        }

        //sinon on bouge dans la direction souhaitée quitte a demander a un autre de bouger
        return this.getPositionFromDirection(direction);
    }
}
