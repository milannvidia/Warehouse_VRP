import java.util.Comparator;

public class MoveRequest {
    public final int ID;
    public PickUp currentLocation;
    public final PickUp placeLocation;
    public final Box box;
    public boolean Assigned;

    public MoveRequest(int id, PickUp pickupLocation, PickUp placeLocation, Box box) {
        ID = id;
        this.currentLocation = pickupLocation;
        this.placeLocation = placeLocation;
        this.box = box;
    }

}

