import java.util.*;

public class Warehouse {
    private int loadingduration;
    private int vehiclespeed;
    private int stackcapacity;
    private ArrayList<BoxStack> stacks;
    private ArrayList<BufferPoint> bps;
    private ArrayList<Vehicle> vehicles;
    private ArrayList<Box> boxes;
    private ArrayList<Box> out = new ArrayList<>();

    public ArrayList<BoxStack> getStacks() {
        return stacks;
    }

    public void setStacks(ArrayList<BoxStack> stacks) {
        this.stacks = stacks;
    }

    public ArrayList<BufferPoint> getBps() {
        return bps;
    }

    public void setBps(ArrayList<BufferPoint> bps) {
        this.bps = bps;
    }

    public ArrayList<Box> getBoxes() {
        return boxes;
    }

    public void setBoxes(ArrayList<Box> boxes) {
        this.boxes = boxes;
    }

    public ArrayList<Box> getOut() {
        return out;
    }

    public void setOut(ArrayList<Box> out) {
        this.out = out;
    }

    public ArrayList<Box> getIn() {
        return in;
    }

    public void setIn(ArrayList<Box> in) {
        this.in = in;
    }

    private ArrayList<Box> in = new ArrayList<>();


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
                box.digOutBox=true;
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
            if(!this.out.isEmpty()||!this.in.isEmpty()) continue whileLoop;
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

    //only called when filler box
    public Location getClosestDropOff(int x, int y) {
        Comparator<Location> sorter=new SortByDistance(x,y);
        this.stacks.sort(sorter);
        for(BoxStack stack:stacks){
            if(stack.canPlace()) {
                stack.toBePlaced++;
                return stack;
            }
        }
        System.out.println("error");
        return null;
    }
}
