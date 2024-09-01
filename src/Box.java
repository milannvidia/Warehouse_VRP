public class Box {
    private String name;
    private Location currentLocation;
    private Location placeLocation;
    private boolean digOutBox = false;
    private int IDRequest;

    public Box(String o, Location currentLocation) {
        name = o;
        this.currentLocation = currentLocation;
        this.placeLocation = null;
    }

    public boolean isDigOutBox() {
        return digOutBox;
    }

    public void setDigOutBox(boolean digOutBox) {
        this.digOutBox = digOutBox;
    }

    public Location getPlaceLocation() {
        return placeLocation;
    }

    /**
     * only to be used for initialization
     */
    public void setPlaceLocation(Location placeLocation) {
        this.placeLocation = placeLocation;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    /**
     * only to be used for initialization
     */
    public void setIDRequest(int IDRequest) {
        this.IDRequest = IDRequest;
    }

    public int getIDRequest() {
        return IDRequest;
    }

    public String getName() {
        return name;
    }

}
