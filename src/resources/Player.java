package resources;

public class Player {

    private String username;
    private int[] position;
    private char icon;

    public Player(String username) {
        this.position = new int[2];
        this.position[0] = 4;
        this.position[1] = 4;
        this.username = username;
        this.icon = '@';
    }

    public int getX() {
        return this.position[0];
    }

    public int getY() {
        return this.position[1];
    }

    public String getUsername() {
        return this.username;
    }

    public void move(int dX, int dY) {
        this.position[0] += dX;
        this.position[1] += dY;
    }

    public char getIcon() {
        return this.icon;
    }

}
