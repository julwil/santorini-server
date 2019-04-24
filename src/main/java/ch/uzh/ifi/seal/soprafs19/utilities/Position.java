package ch.uzh.ifi.seal.soprafs19.utilities;

public class Position {


    private int x;
    private int y;
    private int z;

    public Position (int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Position)) {
            return false;
        }
        Position position = (Position) o;

        return
                this.getX() == position.getX() &&
                this.getY() == position.getY() &&
                this.getZ() == position.getZ();
    }
}
