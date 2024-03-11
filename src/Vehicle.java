
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

public class Vehicle {
    private final int ID;
    private final String name;

    public final int capacity;
    public int X;
    public int Y;
    private final int corridorX;
    private int previousX;
    private int previousY;
    private int time = 0;
    private int previousTime = 0;
    private final int vehiclespeed;
    private final int loadingduration;
    private final ArrayList<Box> inTransit;
    private Box claimedBox;
    private Location target;
    private BoxStack lockedPickup;
    private int loadingprogress;
    private StateVehicle state = StateVehicle.noTarget;
    private StateVehicle PUorPL = StateVehicle.load;
    private Warehouse wareHouse;
    private boolean annotations;

    public Vehicle(int id, String name, int capacity, int x, int y, int vehiclespeed, int loadingduration) {
        ID = id;
        this.name = name;
        this.capacity = capacity;
        X = x;
        Y = y;
        corridorX = x;
        previousX = x;
        previousY = y;
        this.vehiclespeed = vehiclespeed;
        this.loadingduration = loadingduration;
        this.inTransit = new ArrayList<>(capacity);
    }


    public void tick(int time) {
        this.time = time;
        switch (this.state) {
            case noTarget -> {
                getTarget();
                if(state!=StateVehicle.noTarget)tick(time);
                return;
            }

            case load -> {
                if (this.inTransit.size() >= this.capacity) System.out.println("error vehicle load");
                if (!isInplaceOrMoveVehicle()) return;
                if (loadingprogress == 0) {
                    if (target instanceof BoxStack) {
                        if (((BoxStack) target).occupied) return;
                        if (((BoxStack) target).getLocked() && this.lockedPickup != target)
                            System.out.print("shouldnt be possible");
                        ((BoxStack) target).occupied = true;
                    }
                }

                loadingprogress++;
                if (loadingprogress < loadingduration) {
                    return;
                }

                //load from target and remove if present in the list(happens also in notarget normally)
                if (target instanceof BoxStack) {
                    Box loaded = this.load(target.getTop());
                    if (loaded == claimedBox) {
                        claimedBox = null;
                        ((BoxStack) this.target).setUnLocked();
                        this.lockedPickup=null;
                    } else {
                        wareHouse.getOut().remove(loaded);
                    }
                    ((BoxStack) this.target).occupied = false;
                } else {
                    this.load(claimedBox);
                    claimedBox = null;
                }

                this.target=null;
                this.state = StateVehicle.noTarget;
            }
            case unLoad -> {
                if (this.inTransit.isEmpty()) System.out.println("error vehicle load");
                if (!isInplaceOrMoveVehicle()) return;
                if (loadingprogress == 0) {
                    if (target instanceof BoxStack) {
                        if (((BoxStack) target).occupied) return;
                        if (((BoxStack) target).getLocked()) {
                            System.out.println("should not be possible");
                            return;
                        }
                        ((BoxStack) target).occupied = true;
                    }
                }
                loadingprogress++;
                if (loadingprogress < loadingduration) {
                    return;
                }
                //place box
                if(inTransitContainsFiller()){
                    for (Box box : inTransit) {
                        if (!box.digOutBox) continue;
                        dropOff(box);
                        break;
                    }
                }else{
                    for (Box box : inTransit) {
                        if (box.digOutBox) System.out.println("whuut");
                        if (box.placeLocation != target) continue;
                        dropOff(box);
                        break;
                    }
                }
                if(target instanceof BoxStack) ((BoxStack)target).occupied =false;
                this.target=null;
                this.state = StateVehicle.noTarget;
            }
        }
    }

    private boolean inTransitContainsFiller() {
        for (Box box : this.inTransit) {
            if (box.digOutBox) return true;
        }
        return false;
    }

    private void getTarget() {
        if(this.target!=null) System.out.println("why target");

        //sort everthing!!!
        Comparator<Location> sort = new SortByDistance(this.X, this.Y);
        ArrayList<Box> out = this.wareHouse.getOut();
        out.sort((o1, o2) -> sort.compare(o1.currentLocation, o2.currentLocation));
        ArrayList<Box> in = this.wareHouse.getIn();
        in.sort(((o1, o2) -> sort.compare(o1.placeLocation, o2.placeLocation)));
        this.inTransit.sort(((o1, o2) -> sort.compare(o1.placeLocation, o2.placeLocation)));

        //boolean collection
        boolean hasPlace = this.inTransit.size() < this.capacity;
        boolean isEmpty = this.inTransit.isEmpty();
        boolean hasLockedStack = this.lockedPickup != null;
        boolean hasFiller = this.inTransitContainsFiller();


        if ((hasPlace && PUorPL == StateVehicle.load) || isEmpty) {
            if (hasLockedStack) {
                if (this.claimedBox.currentLocation.getX() != lockedPickup.getX()) System.out.println();
                this.target = lockedPickup;
            } else {
                if (!out.isEmpty()) {
                    for(Iterator<Box> itr = out.iterator(); itr.hasNext();){
                        Box box=itr.next();
                        if(((BoxStack)box.currentLocation).getLocked()){
                            continue;
                        }
                        this.claimedBox = box;
                        this.target = claimedBox.currentLocation;
                        ((BoxStack)this.target).setLocked();
                        this.lockedPickup= (BoxStack) this.target;
                        itr.remove();
                        break;
                    }
                } else if (!in.isEmpty()) {
                    this.claimedBox = in.removeFirst();
                    this.target = claimedBox.currentLocation;
                }
            }
            if(this.target!=null){
                this.state = StateVehicle.load;
                this.PUorPL = StateVehicle.load;
            }
        } else {
            if(hasFiller){
                this.target=this.wareHouse.getClosestDropOff(X,Y);
            }else{
                this.target=this.inTransit.getFirst().placeLocation;
            }
            this.state=StateVehicle.unLoad;
            this.PUorPL=StateVehicle.unLoad;
        }
        if(in.isEmpty()&&out.isEmpty()&&claimedBox==null&&!inTransit.isEmpty()){
            if(hasFiller){
                this.target=this.wareHouse.getClosestDropOff(X,Y);
            }else{
                this.target=this.inTransit.getFirst().placeLocation;
            }
            this.state=StateVehicle.unLoad;
            this.PUorPL=StateVehicle.unLoad;
        }

    }

    private void print(Box box, String operation) {
        System.out.println(this.name
                + ";" + previousX
                + ";" + previousY
                + ";" + previousTime
                + ";" + this.X
                + ";" + this.Y
                + ";" + (time+1)
                + ";" + box.name
                + ";" + operation
        );
        previousX = this.X;
        previousY = this.Y;
        previousTime = time+1;
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

    private Box dropOff(Box box) {
        Box temp;
        if(box.digOutBox){
            temp=target.loadBox(box);
        }else{
            if (target != box.placeLocation) System.out.println("vehicle not in right location");
            temp = box.placeLocation.loadBox(box);
        }

        this.inTransit.remove(temp);
        loadingprogress = 0;
        if (annotations)
            System.out.printf("%s: %s has dropped off box %s on location %s %n", this.time, this.name, box.name, target.name);
        else print(temp, "PL");

        return temp;
    }

    private Box load(Box box) {
        if (target != box.currentLocation) System.out.println("vehicle not in right location");

        Box temp = box.currentLocation.unloadBox(box);
        this.inTransit.add(temp);
        loadingprogress = 0;

        if (annotations)
            System.out.printf("%s: %s has picked up box %s on location %s %n", this.time, this.name, box.name, target.name);
        else print(temp, "PU");

        return temp;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.wareHouse = warehouse;
    }

    public boolean hasJob() {
        return !(inTransit.isEmpty() && this.claimedBox == null);
    }

    public void setAnnotations(boolean annotations) {
        this.annotations = annotations;
    }

}
