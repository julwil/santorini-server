package ch.uzh.ifi.seal.soprafs19.utilities;

import ch.uzh.ifi.seal.soprafs19.constant.Axis;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Position {


    private int x;
    private int y;
    private int z;

    public Position(){}

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

    @JsonIgnore
    public ArrayList<Position> getAdjacentPositions()
    {
        ArrayList<Position> candidates = getCandidateAdjacentPositions();
        ArrayList<Position> neighbours = new ArrayList<>();
        for (Position candiate : candidates) {
            if (candiate.hasValidAxis()) {
                neighbours.add(candiate);
            }
        }

        return neighbours;
    }

    @JsonIgnore
    public boolean hasValidAxis()
    {
        return Axis.XYAXIS.contains(this.x) &&
               Axis.XYAXIS.contains(this.y) &&
               Axis.ZAXIS.contains(this.z);
    }

    @JsonIgnore
    private ArrayList<Position> getCandidateAdjacentPositions()
    {
        ArrayList<Position> candidatePositions = new ArrayList<>();

        for (int dx = -1; dx <= 1; ++dx) {
            for (int dy = -1; dy <= 1; ++dy) {
                for (int dz = -1; dz <=1; ++dz) {
                    if (dx != 0 || dy != 0 || dz != 0) {
                        Position tmp = new Position(this.x + dx, this.y + dy, this.z + dz);
                        candidatePositions.add(tmp);
                    }
                }
            }
        }

        return candidatePositions;
    }

    @JsonIgnore
    public boolean isFloor() {
        return this.getZ() == 0;
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


    @Override
    public int hashCode() {
        int x = this.getX();
        int y = this.getY();
        int z = this.getZ();

        String hash = Integer.toString(x) + Integer.toString(y) + Integer.toString(z);

        return Integer.parseInt(hash);
    }
}
