import java.util.*;

public class Warehouse {
    private int loadingduration;
    private int vehiclespeed;
    private int stackcapacity;
    private ArrayList<BoxStack> stacks;
    private ArrayList<BufferPoint> bps;
    private ArrayList<Vehicle> vehicles;
    private ArrayList<Box> boxes;
    ArrayList<Box> out = new ArrayList<>();
    ArrayList<Box> in = new ArrayList<>();


    static int time = 0;

    public Warehouse(int loadingduration, int vehiclespeed, int stackcapacity,
                     ArrayList<BoxStack> boxStacks,
                     ArrayList<BufferPoint> bufferPoints,
                     ArrayList<Box> boxes,
                     ArrayList<Vehicle> vehicles) {

        this.loadingduration = loadingduration;
        this.vehiclespeed = vehiclespeed;
        this.stackcapacity = stackcapacity;
        this.stacks = boxStacks;
        this.boxes = boxes;
        this.vehicles = vehicles;
        this.bps = bufferPoints;
    }

    public void solve(boolean annotations) {


        //init
        for (Box box : this.boxes) {
            if (box.placeLocation == null) {
                continue;
            }
            if (box.placeLocation instanceof BoxStack) {
                ((BoxStack) box.placeLocation).addPlacesReserved();
                if (box.currentLocation instanceof BoxStack) System.out.println("well fuck");
                else {
                    in.add(box);
                }
            } else {
                out.add(box);
            }
        }
        for (Vehicle v : vehicles) {
            v.setAnnotations(annotations);
            v.setWarehouse(this);
        }

        //first move everything out

//        while (!out.isEmpty()) {
//            for (Vehicle v : vehicles) {
//                if (v.hasJob()) continue;
//
//                //find job
//                ArrayList<Box> toMove = new ArrayList<>(v.capacity);
//                SortByDistance sorter = new SortByDistance(v.X, v.Y);
//                out.sort((o1, o2) -> sorter.compare(o1.currentLocation, o2.currentLocation));
//
//                //first try to pick up difficult cases
//                for (int i = v.capacity - 1; i >= 0; i--) {
//                    for (Iterator<Box> iterator = out.iterator(); iterator.hasNext(); ) {
//                        Box box = iterator.next();
//                        if (box.getDepth() == i) {
//                            toMove.add(box);
//                            iterator.remove();
//                            break;
//                        }
//                    }
//                }
//
//                //if not full digout a box in need
//                if (toMove.size() >= v.capacity || out.isEmpty()) {
//                    v.giveJobs(toMove);
//                    continue;
//                }
//
//                int placesLeftVehicle = v.capacity - toMove.size();
//                BoxStack toDigOut = null;
//                BoxStack digoutCatch = null;
//
//                for (Box box : out) {
//                    //only digout if help needed
//                    //TODO: can expand to digout even when no help needed but so that other vehicle can reach more easily
//                    if (box.getDepth() < v.capacity) continue;
//                    toDigOut = (BoxStack) box.currentLocation;
//                    break;
//                }
//
//
//                stacks.sort(sorter);
//                whileLoop:
//                while(toMove.size()<v.capacity){
//                    //if both places reserved and digging in are respected
//                    for(BoxStack stack : stacks){
//                        //don't bury the boxes
//                        if(stack.containsToMoveBox())continue;
//                        int placesLeftStack= stack.heightLimit - stack.boxes.size() - stack.placesReserved;
//                        if(placesLeftStack<=0) continue;
//                        for (int i = 0; i < Math.min(placesLeftStack,placesLeftVehicle); i++) {
//                            Box onTop = toDigOut.boxes.get(i);
//                            onTop.placeLocation = digoutCatch;
//                            onTop.digOutBox = true;
//                            toMove.add(onTop);
//                        }
//                        continue whileLoop;
//                    }
//                    for(BoxStack stack : stacks){
//                        //don't bury the boxes
//                        if(stack.containsToMoveBox())continue;
//                        int placesLeftStack= stack.heightLimit - stack.boxes.size();
//                        if(placesLeftStack<=0) continue;
//                        for (int i = 0; i < Math.min(placesLeftStack,placesLeftVehicle); i++) {
//                            Box onTop = toDigOut.boxes.get(i);
//                            onTop.placeLocation = digoutCatch;
//                            onTop.digOutBox = true;
//                            toMove.add(onTop);
//                        }
//                        continue whileLoop;
//                    }
//
//
//
//
//                }
//
//                //check if can place on stack with no to move box
//                stacks.sort(sorter);
//                for (BoxStack stack : stacks) {
//                    if (stack.placesReserved + stack.boxes.size() + placesLeftVehicle >= stack.heightLimit) continue;
//                    if (stack.containsToMoveBox()) continue;
//                    digoutCatch = stack;
//
//                    for (int i = 0; i < placesLeftVehicle; i++) {
//                        Box onTop = toDigOut.boxes.get(i);
//                        onTop.placeLocation = digoutCatch;
//                        onTop.digOutBox = true;
//                        toMove.add(onTop);
//                    }
//                    v.giveJobs(toMove);
//                    break;
//                }
//
//                if (digoutCatch != null) {
//                    continue;
//                }
//                //check if can place on stack with so the stack remains free for later
//                for (BoxStack stack : stacks) {
//                    if (stack.placesReserved + stack.boxes.size() + placesLeftVehicle >= stack.heightLimit) continue;
//                    digoutCatch = stack;
//
//                    for (int i = 0; i < placesLeftVehicle; i++) {
//                        Box onTop = toDigOut.boxes.get(i);
//                        onTop.placeLocation = digoutCatch;
//                        onTop.digOutBox = true;
//                        toMove.add(onTop);
//                    }
//                    v.giveJobs(toMove);
//                    break;
//                }
//                if (digoutCatch != null) {
//                    continue;
//                }
//
//                //just place somewhere
//                for (BoxStack stack : stacks) {
//                    if (stack.placesReserved + stack.boxes.size() >= stack.heightLimit) continue;
//                    digoutCatch = stack;
//
//                    int placesLeftStack = stack.heightLimit - stack.boxes.size() - stack.placesReserved;
//                    for (int i = 0; i < Math.min(placesLeftStack, placesLeftVehicle); i++) {
//                        Box onTop = toDigOut.boxes.get(i);
//                        onTop.placeLocation = digoutCatch;
//                        onTop.digOutBox = true;
//                        toMove.add(onTop);
//                        placesLeftVehicle--;
//                    }
//                    if (placesLeftVehicle == 0) break;
//                }
//                v.giveJobs(toMove);
//
//
//                assert digoutCatch != null : "No digoutCatch";
//
//
//            }
//            this.tick();
//        }

        //check if enough room on stack
//        while(!out.isEmpty()){
//            for (Vehicle v: vehicles) {
//                if(v.hasJob())continue;
//
//
//            }
//        }
//
//
//        clearOverFull:
//        while (true) {
//            break;
//        }
//
//
//        //then place all boxes on the right stack
//        while (!in.isEmpty()) {
//            for (Vehicle v : vehicles) {
//                if (v.hasJob()) continue;
//
//                //find job
//                ArrayList<Box> toMove = new ArrayList<>(v.capacity);
//                out.sort((o1, o2) -> new SortByDistance(v.X, v.Y).compare(o1.placeLocation, o2.placeLocation));
//                //first try to pick up difficult cases
//
//                for (Iterator<Box> iterator = in.iterator(); iterator.hasNext(); ) {
//                    Box box = iterator.next();
//                    if (toMove.size() < v.capacity) {
//                        toMove.add(box);
//                        iterator.remove();
//                    } else {
//                        break;
//                    }
//                }
//
//                v.giveJobs(toMove);
//            }
//            this.tick();
//        }

        //let program finish
        whileLoop:
        while (true) {
            tick();
            for (Vehicle v : vehicles) {
                if (v.hasJob()) continue whileLoop;
            }
            break;
        }
    }


    private void tick() {
        for (Vehicle v : vehicles) {
            v.tick(time);
        }
        time++;
    }

    public Queue<MoveRequest> getTasks(Vehicle v) {
        Queue<MoveRequest> temp=new LinkedList<>();
        if(!out.isEmpty()){
            return getOutTasks(v);
        }
        for(BoxStack boxStack:stacks){
            int places= boxStack.placesReserved+ boxStack.boxes.size()-this.stackcapacity;
            if(places<0) continue;
            return getEvenTasks(v);
        }
        if(!in.isEmpty()){
            return getInTasks(v);
        }

        return null;
    }

    private Queue<MoveRequest> getInTasks(Vehicle v) {
        Comparator<PickUp> sorter=new SortByDistance(v.X,v.Y);
        in.sort((o1, o2) -> sorter.compare(o1.placeLocation,o2.placeLocation));
        //TODO:place all boxes in there correct space
        Queue<MoveRequest> temp=new LinkedList<>();
        for (int i = 0; i < v.capacity; i++) {
            temp.add(new MoveRequest(in.removeFirst(),MoveType.Pick_Up));
        }
        for (MoveRequest move:temp){
            temp.add(new MoveRequest(move.box,MoveType.Put_Down));
        }
        return temp;
    }

    private Queue<MoveRequest> getEvenTasks(Vehicle v) {
        //TODO: Move all boxes which are to much in there stack
    }

    private Queue<MoveRequest> getOutTasks(Vehicle v) {
        Comparator sorter=new SortByDistance(v.X,v.Y);
        out.sort((o1, o2) -> sorter.compare(o1.currentLocation,o2.currentLocation));
        //TODO: first all boxes reachable like that
        for(Box box:out){

        }
        //TODO: Digout
    }
}
