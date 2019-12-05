package resources;

public class Map {
    private Tile[][] tileMap = new Tile[9][9];
    private int difficultyLevel;

    //Generates a new map based on the difficulty level
    public Map(int difficultyLevel) {
        for(int x = 0; x<9; x++) {
            for(int y = 0; y<9; y+=2) {
                this.tileMap[x][y] = new Tile('Δ', "You are in a forest");
            }
            for(int y = 1; y<9; y+=2) {
                this.tileMap[x][y] = new Tile('~', "You are in the plains");
            }
        }
        this.difficultyLevel = difficultyLevel;
    }

    public Tile getTileByCoords(int x, int y) {
        return this.tileMap[x][y];
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }
}
