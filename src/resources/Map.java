package resources;

public class Map {
    private Tile[][] tileMap = new Tile[9][9];
    private int difficultyLevel;

    public Map(int difficultyLevel) {
        for(int x = 0; x<9; x++) {
            for(int y = 0; y<9; y++) {
                this.tileMap[x][y] = new Tile('Δ');
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
