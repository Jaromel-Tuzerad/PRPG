package prpg.gameLogic.entities;

import java.io.Serializable;

public abstract class Entity implements Serializable {

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
