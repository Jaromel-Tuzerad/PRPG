package resources;

public class Player extends Entity {

    private String username;

    public Player(String username, int x, int y) {
        super(x, y, "", '@');
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

}
