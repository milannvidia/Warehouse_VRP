import java.util.ArrayList;

public class BufferPoint extends Location {

    public ArrayList<Box> storage = new ArrayList<>();

    public BufferPoint(int id, String name, int x, int y) {
        super(id, name, x, y);
    }

    @Override
    public Box unloadBox(Box box) {
        if (storage.contains(box)) {
            storage.remove(box);
            box.currentLocation=null;
            return box;
        } else {
            System.out.println("cant remove something that's not there");
            return null;
        }
    }

    @Override
    public Box loadBox(Box box) {
        storage.add(box);
        box.currentLocation=this;
        return box;
    }

    @Override
    public boolean containsBox(Box claimedBox) {
        return this.storage.contains(claimedBox);
    }

    @Override
    public boolean boxOnTop(Box claimedBox) {
        return true;
    }

    @Override
    public boolean containsBoxWithTarget() {
        for (Box box : this.storage) {
            if (!box.digOutBox && box.placeLocation != this) return true;
        }
        return false;
    }


    @Override
    public Box getTop() {
        System.out.println("cant get a top box");
        return null;
    }

    @Override
    public boolean canPlace() {
        return true;
    }
}
