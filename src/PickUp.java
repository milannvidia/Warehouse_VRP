import java.util.Comparator;

public abstract class PickUp {
    public int ID;
    protected final String name;
    protected final int X;
    protected final int Y;

    protected boolean ocupied=false;

    public PickUp(int id, String name, int x, int y) {
        this.ID=id;
        this.name=name;
        this.X=x;
        this.Y=y;

    }

    public abstract Box unloadBox(Box box);

    public abstract Box loadBox(Box box);

}