package edu.binghamton.cs.crabby;

public class Circ {

    int x, y, radius;

    Circ(int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public static boolean intersects(Circ a, Circ b) {
        int distanceX = a.x - b.x;
        int distanceY = a.y - b.y;
        int distanceSquared = distanceX * distanceX + distanceY * distanceY;

        int radiiSum = a.radius + b.radius;
        int radiiSumSquared = radiiSum * radiiSum;

        return distanceSquared < radiiSumSquared;
    }
}
