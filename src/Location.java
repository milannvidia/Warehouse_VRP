public abstract class Location {
    public int ID;
    protected final String name;
    protected final int X;
    protected final int Y;
    public Location(int id, String name, int x, int y) {
        this.ID = id;
        this.name = name;
        this.X = x;
        this.Y = y;

    }

    public abstract Box unloadBox(Box box);

    public abstract Box loadBox(Box box);

    public abstract boolean containsBox(Box claimedBox);
    public abstract boolean boxOnTop(Box claimedBox);

    public abstract boolean containsBoxWithTarget();

    public abstract Box getTop();
    public abstract boolean canPlace();

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }
}
