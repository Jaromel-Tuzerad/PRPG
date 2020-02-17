package gameLogic.entities;

public class Entity {

    protected static int numberOfEntities;
    protected int id;
    protected String description;
    protected String displayName;
    protected int posX;
    protected int posY;

    public Entity(int x, int y, String displayName, String description) {
        this.posX = x;
        this.posY = y;
        this.description = description;
        this.displayName = displayName;
        this.id = numberOfEntities;
        numberOfEntities+=1;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public int getX() {
        return posX;
    }

    public int getY() {
        return posY;
    }

    public void moveBy(int dX, int dY) {
        this.posX += dX;
        this.posY += dY;
    }

}
