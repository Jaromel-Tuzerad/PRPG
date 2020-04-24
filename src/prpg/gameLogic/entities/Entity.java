package prpg.gameLogic.entities;

public abstract class Entity {

    protected String name;
    protected String description;

    public Entity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public String getName() {
        return this.name;
    }

    public abstract String getDisplayName();
    
    public abstract String getActionName();

}
