package prpg.gameLogic.entities;

public class Entity {

    protected static int numberOfEntities;
    protected int id;
    protected String description;
    protected String displayName;

    public Entity(String displayName, String description) {
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

}
