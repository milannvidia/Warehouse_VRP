
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Vehicle {
    private final int ID;
    private final String name;

    public final int capacity;
    public int X;
    public int Y;
    private final int corridorX;
    private int startX;
    private int startY;
    private int startTime = 0;
    private final int vehiclespeed;
    private final int loadingduration;
    private final ArrayList<Box> inTransit;
    private Queue<MoveRequest> taskOrder=new LinkedList<>();
    private MoveRequest currentMove;
    private PickUp target;
    private int loadingprogress;
    private Warehouse wareHouse;
    private boolean annotations;

    public Vehicle(int id, String name, int capacity, int x, int y, int vehiclespeed, int loadingduration) {
        ID = id;
        this.name = name;
        this.capacity = capacity;
        X = x;
        Y = y;
        corridorX = X;
        startX = x;
        startY = y;
        this.vehiclespeed = vehiclespeed;
        this.loadingduration = loadingduration;
        this.inTransit = new ArrayList<>(capacity);
    }

//    public void tickV2(int time) {
//
//        if (!hasJob()) {
////            System.out.println("no job");
//            return;
//        }
//        if (current == null) {
//            current = toMove.get(0);
//            target = current.currentLocation;
//        }
//
//        //moves the vehicle if necessary
//        if (!isInplaceOrMoveVehicle()) {
//            return;
//        }
//
//        //loading progress
//        if (loadingprogress == 0) {
//            if (target.ocupied && target instanceof BoxStack) {
//                System.out.println("target is ocupied: " + target.name);
//                return;
//            } else {
//                target.ocupied = true;
//            }
//        }
//
//
//        if (loadingprogress < loadingduration) {
//            loadingprogress++;
//            return;
//        }
//
//        //loadoff or loadon once loading operation is over
//        if (target == current.currentLocation) {
//            if (!annotations) print(current, time, "PU");
//            else System.out.print(time + ": ");
//
//            loadOn(current);
//            toMove.remove(current);
//            inTransit.add(current);
//        } else {
//            if (!annotations) print(current, time, "PL");
//            else System.out.print(time + ": ");
//            loadOff(current);
//            inTransit.remove(current);
//        }
//
//        target.ocupied = false;
//        loadingprogress = 0;
//
//        //next target based on capacity and to pickup
//        //fork is full so empty some
//
//        if (inTransit.size() >= capacity) {
//            current = inTransit.get(inTransit.size() - 1);
//            target = current.placeLocation;
//        }
//        //if fork not full add some more
//        else if (!toMove.isEmpty()) {
//            current = toMove.get(0);
//            target = current.currentLocation;
//        }
//        //fork not full but no jobs left so clean of remaining fork
//        else if (!inTransit.isEmpty()) {
//            current = inTransit.get(inTransit.size() - 1);
//            target = current.placeLocation;
//        }
//        //fork not full, no jobs left, no things left on fork
//        else {
//            current = null;
//            target = null;
//        }
//
//    }

    public void tick(int time) {

        if (taskOrder.isEmpty()) {
            this.taskOrder=this.wareHouse.getTasks(this);
        }
        if (currentMove == null) {
            currentMove = taskOrder.poll();
            switch (currentMove.moveType){
                case Pick_Up -> target=currentMove.box.currentLocation;
                case Put_Down -> target=currentMove.box.placeLocation;
            }
        }

        //moves the vehicle if necessary
        if (!isInplaceOrMoveVehicle()) {
            return;
        }

        //loading progress
        if (loadingprogress == 0) {
            if (target.ocupied && target instanceof BoxStack) {
                System.out.println("target is ocupied: " + target.name);
                return;
            } else {
                target.ocupied = true;
            }
        }


        if (loadingprogress < loadingduration) {
            loadingprogress++;
            return;
        }

        //loadoff or loadon once loading operation is over
        switch (currentMove.moveType){
            case Pick_Up -> {
                if (!annotations) print(currentMove.box, time, "PU");
                else System.out.print(time + ": ");
                loadOn(currentMove.box);
                inTransit.add(currentMove.box);
            }
            case Put_Down -> {
                if (!annotations) print(currentMove.box, time, "PL");
                else System.out.print(time + ": ");
                loadOff(currentMove.box);
                inTransit.remove(currentMove.box);
            }
        }

        target.ocupied = false;
        loadingprogress = 0;

    }
    private void print(Box box, int time, String operation) {
        System.out.println(this.name
                + ";" + startX
                + ";" + startY
                + ";" + startTime
                + ";" + this.X
                + ";" + this.Y
                + ";" + time
                + ";" + box.name
                + ";" + operation
        );
        startX = this.X;
        startY = this.Y;
        startTime = time;
    }

    //
    private boolean isInplaceOrMoveVehicle() {

        int goalX = target.X;
        int goalY = target.Y;

        if (goalX == X && goalY == Y) return true;
        //if within same row directrly go to right y axis otherwise back to startX
        //if on right row move along it
        if (Y == goalY) {
            if (X > goalX) {
                X -= vehiclespeed;
            } else {
                X += vehiclespeed;
            }
            return false;
        }
        //if in corridor change Y
        if (X == corridorX) {
            if (Y > goalY) {
                Y -= vehiclespeed;
            } else {
                Y += vehiclespeed;
            }
            return false;
        }
        //if not in right row or in corridor change X
        if (X > corridorX) {
            X -= vehiclespeed;
        } else {
            X += vehiclespeed;
        }
        return false;
    }

    private void loadOff(Box box) {
        if (target != box.placeLocation) System.out.println("vehicle not in right location");
        PickUp pl = box.placeLocation;
        pl.loadBox(box);

        if (annotations)
            System.out.printf("%s has dropped off box %s on location %s %n", this.name, box.name, target.name);

    }

    private void loadOn(Box box) {
        if (target != box.currentLocation) System.out.println("vehicle not in right location");

        PickUp pu = box.currentLocation;
        //capacity 3: 0 boxes max index 2, 1 max index 1, 2 max index 0
        pu.unloadBox(box);

        if (annotations)
            System.out.printf("%s has picked up box %s on location %s %n", this.name, box.name, target.name);

    }

    public void setWarehouse(Warehouse warehouse) {
        this.wareHouse = warehouse;
    }

    public boolean hasJob() {
        return !(inTransit.isEmpty() && this.taskOrder.isEmpty());
    }

    public void setAnnotations(boolean annotations) {
        this.annotations = annotations;
    }

}
