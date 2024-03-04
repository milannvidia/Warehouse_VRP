import java.util.Stack;

public class BoxStack extends PickUp{

    public final int heightLimit;
    Stack<Box> boxes=new Stack<>();

    public int placesReserved=0;

    public BoxStack(int id, String name, int x, int y,int heightLimit) {
        super(id,name,x,y);
        this.heightLimit=heightLimit;
    }

    @Override
    public Box unloadBox(Box box) {
        if(boxes.peek()==box){
            return boxes.pop();
        }
        else{
            if(boxes.contains(box)){
                System.out.println("box not on top");
            }else{
                System.out.println("box not here anymore");
            }
            return null;
        }
    }

    @Override
    public Box loadBox(Box box) {
        if(boxes.size()<heightLimit){
            boxes.add(box);
            if(box.digOutBox)box.digOutBox=false;
            return box;
        }else{
            System.out.println("no place anymore on stack "+this.ID);
            return null;
        }
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }
    public void addBox(Box box) {
        boxes.add(box);
    }

    public void addPlacesReserved() {
        this.placesReserved +=1;
    }

    public boolean isOnTop(Box box) {
        return boxes.peek()==box;
    }

    public boolean containsToMoveBox(){
        for (Box box:boxes){
            if (box.placeLocation!=null) return true;
        }
        return false;
    }
}
