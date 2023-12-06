
import java.util.ArrayList;

public class Vehicle {
    private final int ID;
    private final String name;
    public final int capacity;
    public int X;
    public int Y;
    private final int corridorX;
    private int startX;
    private int startY;
    private int startTime=0;
    private final int vehiclespeed;
    private final int loadingduration;

    //for vehicle itself
    private final ArrayList<MoveRequest> toPickup;
    private final ArrayList<MoveRequest> inTransit;
    private MoveRequest current;
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
        corridorX =X;
        startX=x;
        startY=y;
        this.vehiclespeed=vehiclespeed;
        this.loadingduration=loadingduration;
        this.toPickup=new ArrayList<>(capacity);
        this.inTransit=new ArrayList<>(capacity);
    }

    public void tick(int time) throws Exception {

        if(!hasJob()){
            throw new Exception("no job");
        }
        if(current==null){
            current=toPickup.get(0);
            target=current.currentLocation;
        }

        //moves the vehicle if necessary
        if(!isInplaceOrMoveVehicle()){
            return;
        }

        //loading progress
        if(loadingprogress==0) {
            if(target.ocupied){
                return;
            }else{
                target.ocupied = true;
            }
        }

        if(loadingprogress<loadingduration){
            loadingprogress++;
            return;
        }

        //loadoff or loadon once loading operation is over
        if(!toPickup.isEmpty()){
            if(!annotations)print(current,time,"PU");
            else System.out.print(time+": ");
            loadOn(current);

            toPickup.remove(current);
            inTransit.add(current);

        }else{
            if(!annotations)print(current,time,"PL");
            else System.out.print(time+": ");
            loadOff(current);
            inTransit.remove(current);
        }

        target.ocupied=false;
        loadingprogress=0;

        //next target
        if(!(toPickup.isEmpty())){
            current=toPickup.get(0);
            target=current.currentLocation;
        }else if(!inTransit.isEmpty()){
            current=inTransit.get(inTransit.size()-1);
            target=current.placeLocation;
        }else{
            current=null;
            target=null;
        }
    }

    private void print(MoveRequest moveRequest, int time, String operation) {
        System.out.println(this.name
                +";"+startX
                +";"+startY
                +";"+startTime
                +";"+this.X
                +";"+this.Y
                +";"+time
                +";"+moveRequest.box.name
                +";"+operation
        );
        startX=this.X;
        startY=this.Y;
        startTime=time;
    }

    private boolean isInplaceOrMoveVehicle() {

        int goalX= target.X;
        int goalY= target.Y;

        if(goalX==X && goalY==Y) return true;
        //if within same row directrly go to right y axis otherwise back to startX
        //if on right row move along it
        if(Y==goalY){
            if (X>goalX){
                X-=vehiclespeed;
            }else{
                X+=vehiclespeed;
            }
            return false;
        }
        //if in corridor change Y
        if(X== corridorX){
            if (Y>goalY){
                Y-=vehiclespeed;
            }else{
                Y+=vehiclespeed;
            }
            return false;
        }
        //if not in right row or in corridor change X
        if (X> corridorX){
            X-=vehiclespeed;
        }else{
            X+=vehiclespeed;
        }
        return false;
    }
    private void loadOff(MoveRequest moveRequest) {
        if(target !=moveRequest.placeLocation) System.out.println("vehicle not in right locationp");

        wareHouse.removeTomoveBox(moveRequest.box);

        PickUp pl=moveRequest.placeLocation;
        pl.loadBox(moveRequest.box);

        if(annotations)System.out.printf("%s has dropped off box %s on location %s %n",this.name,moveRequest.box.name,target.name);

    }

    private void loadOn(MoveRequest moveRequest) {
        if(toPickup.isEmpty()) System.out.println("nothing to pickup anymore");
        if(target!=moveRequest.currentLocation)System.out.println("vehicle not in right location");

        PickUp pu=moveRequest.currentLocation;
        pu.unloadBox(moveRequest.box);

        if(annotations)System.out.printf("%s has picked up box %s on location %s %n",this.name,moveRequest.box.name,target.name);

    }
    public void giveJobs(ArrayList<MoveRequest> r){
        toPickup.addAll(r);
    }

    public void addWarehouse(Warehouse warehouse) {
        this.wareHouse=warehouse;
    }

    public boolean hasJob() {
        return !(inTransit.isEmpty()&&toPickup.isEmpty());
    }

    public void setAnnotations(boolean annotations) {
        this.annotations=annotations;
    }
}
