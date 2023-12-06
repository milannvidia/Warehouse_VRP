import java.util.ArrayList;

public class BufferPoint extends PickUp{

    public ArrayList<Box> storage=new ArrayList<>();

    public BufferPoint(int id, String name, int x, int y) {
        super(id,name,x,y);
    }

    @Override
    public Box unloadBox(Box box) {
        if (storage.contains(box)){
            storage.remove(box);
            return box;
        }else{
            System.out.println("cant remove something that's not there");
            return null;
        }
    }

    @Override
    public Box loadBox(Box box) {
        storage.add(box);
        return box;
    }
}
