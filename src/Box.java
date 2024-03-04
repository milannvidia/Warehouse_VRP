public class Box {
    public String name;
    public PickUp currentLocation;
    public PickUp placeLocation;
    public boolean digOutBox=false;

    public int IDRequest;


    public Box(String o, PickUp currentLocation) {
        name = o;
        this.currentLocation = currentLocation;

        this.placeLocation = null;
    }

    public void setPlaceLocation(PickUp placeLocation) {
        this.placeLocation = placeLocation;
    }

    public void setIDRequest(int IDRequest) {
        this.IDRequest = IDRequest;
    }

    public int getDepth(){
        if(currentLocation instanceof BoxStack){
            return ((BoxStack)this.currentLocation).boxes.indexOf(this);
        }else{
            return 0;
        }
    }


}
