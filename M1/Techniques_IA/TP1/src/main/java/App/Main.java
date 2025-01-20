package App;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import App.model.Agent;
import App.model.Grid;

public class Main {
    private static final float GRID_WIDTH = 5;
    private static final float GRID_HEIGHT = 5;
    private static final float AGENTS = 10;
    private static final int TIMEOUT = 380;

    public static void main(String[] args) {
        Grid grid = new Grid((int) GRID_WIDTH, (int) GRID_HEIGHT, true);
        System.out.println("Grid initialisation");

        for(int i = 0; i < AGENTS; i++) {
            new Agent(grid, i);
            grid.print();
        }

        System.out.println("Run agent : " + (AGENTS/(GRID_HEIGHT*GRID_WIDTH))*100 + "%");
        System.out.println("___________________");
        System.out.println();
        Grid.runAllAgents();

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        executorService.schedule(Grid::stopAllAgent, TIMEOUT, TimeUnit.SECONDS);


    }
}