package legends.world;

import java.util.Random;

public class World {

    private final int size;
    private final Tile[][] grid;
    private int partyRow;
    private int partyCol;
    private final Random random = new Random();

    // ================================================================
    // CONSTRUCTOR (EXPECTED BY LegendsGame)
    // ================================================================
    public World(int size) {
        this.size = size;
        this.grid = new Tile[size][size];
        generateWorld();
    }

    // ================================================================
    // WORLD GENERATION
    // ================================================================
    private void generateWorld() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {

                double roll = random.nextDouble();

                if (roll < 0.15) {
                    grid[r][c] = new InaccessibleTile();
                } else if (roll < 0.28) {
                    grid[r][c] = new MarketTile();
                } else {
                    grid[r][c] = new CommonTile();
                }
            }
        }
    }

    // ================================================================
    // PLACE PARTY AT BOTTOM-LEFT
    // ================================================================
    public void placePartyAtStart() {
        this.partyRow = size - 1;
        this.partyCol = 0;

        // If bottom-left is inaccessible, move right until valid
        while (grid[partyRow][partyCol] instanceof InaccessibleTile) {
            partyCol++;
            if (partyCol >= size) {
                // fallback: top-left corner
                partyCol = 0;
                partyRow = 0;
                break;
            }
        }
    }

    // ================================================================
    // GET CURRENT TILE
    // ================================================================
    public Tile getCurrentTile() {
        return grid[partyRow][partyCol];
    }

    // ================================================================
    // MOVE PARTY
    // ================================================================
    public boolean move(char direction) {
        int newR = partyRow;
        int newC = partyCol;

        switch (direction) {
            case 'W' -> newR--;
            case 'S' -> newR++;
            case 'A' -> newC--;
            case 'D' -> newC++;
        }

        // Boundary check
        if (newR < 0 || newR >= size || newC < 0 || newC >= size)
            return false;

        // Inaccessible tile check
        if (grid[newR][newC] instanceof InaccessibleTile)
            return false;

        // move is valid
        partyRow = newR;
        partyCol = newC;
        return true;
    }

    // ================================================================
    // PRINT WORLD MAP (Used by LegendsGame)
    // ================================================================
    public void printWorld() {
        System.out.println("\nLegend:");
        System.out.println("· = Common   $ = Market   # = Inaccessible   H = Hero\n");

        System.out.print("      ");
        for (int c = 0; c < size; c++)
            System.out.printf("%2d  ", c);
        System.out.println("\n   " + "-".repeat(size * 4));

        for (int r = 0; r < size; r++) {
            System.out.printf("%2d| ", r);
            for (int c = 0; c < size; c++) {

                if (r == partyRow && c == partyCol) {
                    System.out.print(" H  ");
                } else if (grid[r][c] instanceof InaccessibleTile) {
                    System.out.print(" #  ");
                } else if (grid[r][c] instanceof MarketTile) {
                    System.out.print(" $  ");
                } else {
                    System.out.print(" ·  ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
