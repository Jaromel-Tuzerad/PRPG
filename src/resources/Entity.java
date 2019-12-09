package resources;

public abstract class Entity {

    private static int numberOfEntities;
    private int id;
    private String description;
    private int[] position;
    private char icon;
    private float health;
    private final int MAX_HEALTH = 100;

    public Entity(int x, int y, String description, char icon) {
        this.description = description;
        this.position = new int[2];
        this.position[0] = x;
        this.position[1] = y;
        this.icon = icon;
        this.id = numberOfEntities;
        numberOfEntities+=1;
        this.health = MAX_HEALTH;
    }

    public String getDescription() {
        return description;
    }

    public int getX() {
        return this.position[0];
    }

    public int getY() {
        return this.position[1];
    }

    public char getIcon() {
        return this.icon;
    }

    public int getId() {
        return this.id;
    }

    public float getHealth() {
        return health;
    }

    public void move(int dX, int dY) {
        this.position[0] += dX;
        this.position[1] += dY;
    }

}
