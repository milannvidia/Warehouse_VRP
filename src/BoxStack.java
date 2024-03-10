import java.util.Stack;

public class BoxStack extends Location {

    public final int heightLimit;
    Stack<Box> boxes = new Stack<>();

    public int placesReserved = 0;

    public int toBePlaced = 0;
    protected boolean ocupied = false;
    private boolean locked = false;

    public BoxStack(int id, String name, int x, int y, int heightLimit) {
        super(id, name, x, y);
        this.heightLimit = heightLimit;
    }

    @Override
    public Box unloadBox(Box box) {
        if (boxes.peek() == box) {
            box.currentLocation=null;
            return boxes.pop();
        } else {
            if (boxes.contains(box)) {
                System.out.println("box not on top");
            } else {
                System.out.println("box not here anymore");
            }
            return null;
        }
    }

    @Override
    public Box loadBox(Box box) {
        if (boxes.size() < heightLimit) {
            boxes.add(box);
            if (box.placeLocation == this) placesReserved--;
            else toBePlaced--;
            box.currentLocation=this;
            return box;
        } else {
            System.out.println("no place anymore on stack " + this.ID);
            return null;
        }
    }

    @Override
    public boolean containsBox(Box claimedBox) {
        return this.boxes.contains(claimedBox);
    }

    @Override
    public boolean boxOnTop(Box claimedBox) {
        return this.boxes.peek() == claimedBox;
    }

    @Override
    public Box getTop() {
        return this.boxes.peek();
    }

    @Override
    public boolean canPlace() {
        if (toBePlaced + boxes.size() + placesReserved + 1 <= heightLimit && !containsBoxWithTarget()) return true;
//        if(toBePlaced+boxes.size()+1<heightLimit) {
//            System.out.println("accepted placement but no place for reserve anymore");
//            return true;
//        }
        return false;
    }

    @Override
    public boolean containsBoxWithTarget() {
        for (Box box : this.boxes) {
            if (!box.digOutBox && box.placeLocation != this) return true;
        }
        return false;
    }

    public void addBox(Box box) {
        boxes.add(box);
    }

    public void addPlacesReserved() {
        this.placesReserved += 1;
    }

    public boolean enoughPlaceForIn() {
        return this.boxes.size() + this.placesReserved <= this.heightLimit;
    }

    public void setLocked() {
        if (this.locked) System.out.println("already locked");
        else locked = true;
    }

    public void setUnLocked() {
        if (this.locked) locked = false;
        else System.out.println("wasn't locked");
    }

    public boolean getLocked() {
        return locked;
    }


}
