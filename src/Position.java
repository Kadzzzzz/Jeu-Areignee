import java.util.Objects;

public class Position {
    private final int x;
    private final int y;
    
    public Position(int x, int y) {
        if (x < 0 || x > 2 || y < 0 || y > 2) {
            throw new IllegalArgumentException("Position invalide. Les coordonnées doivent être entre 0 et 2.");
        }
        this.x = x;
        this.y = y;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public boolean estValide() {
        return x >= 0 && x <= 2 && y >= 0 && y <= 2;
    }
    
    public boolean estAdjacente(Position autre) {
        if (autre == null) {
            return false;
        }
        int deltaX = Math.abs(this.x - autre.x);
        int deltaY = Math.abs(this.y - autre.y);
        return (deltaX == 1 && deltaY == 0) || (deltaX == 0 && deltaY == 1);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Position position = (Position) obj;
        return x == position.x && y == position.y;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
    
    @Override
    public String toString() {
        return "Position(" + x + ", " + y + ")";
    }
}