import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

public class Warehouse {
    private static int loadingduration;
    private static int vehiclespeed;
    private static int stackcapacity;
    static HashMap<String, BoxStack> stacks;
    static HashMap<String, BufferPoint> bps;
    static ArrayList<Vehicle> vehicles;
    static ArrayList<MoveRequest> requests;
    static ArrayList<Box> toMoveBox = new ArrayList<>();

    static int time = 0;

    public Warehouse(int loadingduration, int vehiclespeed, int stackcapacity, HashMap<String, BoxStack> stacks, HashMap<String, BufferPoint> bps, ArrayList<Vehicle> vehicles, ArrayList<MoveRequest> requests) {
        this.loadingduration = loadingduration;
        this.vehiclespeed = vehiclespeed;
        this.stackcapacity = stackcapacity;
        this.stacks = stacks;
        this.bps = bps;
        this.vehicles = vehicles;
        this.requests = requests;

        for (MoveRequest r : requests) {
            toMoveBox.add(r.box);
        }
        for (Vehicle v : vehicles) {
            v.addWarehouse(this);
        }
    }

    public void solve(boolean annotations) throws Exception {
        for (Vehicle v : vehicles) {
            v.setAnnotations(annotations);
        }
        while (true) {
            if (requests.isEmpty()) {
                boolean stillBusy = false;
                for (Vehicle v : vehicles) {
                    if (v.hasJob()) stillBusy = true;
                }
                if (!stillBusy) break;
            }
            //find an operation that's possible
            nextVehicle:
            for (Vehicle v : vehicles) {
                if(v.hasJob())continue;
                requests.sort((o1,o2)->new SortByDistance(v.X,v.Y).compare(o1.currentLocation, o2.currentLocation));
                //fill the stack
                ArrayList<MoveRequest> moveRequests=new ArrayList<>(v.capacity);
                //simple move
                for (Iterator<MoveRequest> Itr = requests.iterator(); Itr.hasNext(); ) {
                    MoveRequest r = Itr.next();
                    //can the box be moved without moving boxes or impeding other requests
                    if (simpleMove(r)) {
                        moveRequests.add(r);
                        Itr.remove();
                        if(moveRequests.size()>=v.capacity)continue nextVehicle;
                    }

                }
                if(!moveRequests.isEmpty())continue nextVehicle;
                //dig out
                for (Iterator<MoveRequest> Itr = requests.iterator(); Itr.hasNext(); ) {
                    MoveRequest r = Itr.next();
                    //can the box be moved without moving boxes or impeding other requests
                    if (digOut(r,1)) {
                        moveRequests.add(r);
                        Itr.remove();
                        if(moveRequests.size()>=v.capacity)continue nextVehicle;
                    }

                }
                System.out.println("implement more functions");

                v.giveJobs(moveRequests);
            }


            tick();
        }
    }

    private void tick() throws Exception {
        for (Vehicle v : vehicles) {
            v.tick(time);
        }
        time++;
    }
    private boolean digOut(MoveRequest r, int i) {
        if(r.currentLocation instanceof BoxStack){
            BoxStack current = (BoxStack) r.currentLocation;
            if(current.boxes.search(r.box)>1+1){
                return false;
            }
        }
    }
    private boolean simpleMove(MoveRequest r) {

        //can the box be moved without moving boxes or impeding other requests

        if (r.currentLocation instanceof BoxStack) {
            BoxStack current = (BoxStack) r.currentLocation;
            if (current.boxes.peek() != r.box) {
                return false;
            }
        }

        if (r.placeLocation instanceof BoxStack) {
            BoxStack current = (BoxStack) r.placeLocation;
            for (Box box : current.boxes) {
                if (toMoveBox.contains(box)) {
                    return false;
                }
            }
        }
        return true;

    }

    public void removeTomoveBox(Box box) {
        toMoveBox.remove(box);
    }
}
