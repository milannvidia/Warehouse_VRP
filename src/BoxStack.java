import java.util.Stack;

public class BoxStack extends PickUp{

    private final int heightLimit;
    Stack<Box> boxes=new Stack<>();

    public BoxStack(int id, String name, int x, int y,int heightLimit) {
        super(id,name,x,y);
        this.heightLimit=heightLimit;
    }

    @Override
    public Box unloadBox(Box box) {
        if(boxes.peek()==box){
            boxes.pop();
            return box;
        }
        else{
            if(boxes.contains(box)){

                System.out.println("box not here anymore");
            }else{
                System.out.println("box not on top");
            }
            return null;
        }
    }

    @Override
    public Box loadBox(Box box) {
        if(boxes.size()<heightLimit){
            boxes.add(box);
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

    public boolean isOnTop(Box box) {
        return boxes.peek()==box;
    }
}
