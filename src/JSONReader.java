import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

public class JSONReader {
    public static Warehouse getWarehouse(File instance) throws IOException {
        String JsonString= Files.readString(instance.toPath());
        JSONObject objTarget= new JSONObject(JsonString);
        
        int loadingduration=objTarget.getInt("loadingduration");
        int vehiclespeed=objTarget.getInt("vehiclespeed");
        int stackcapacity=objTarget.getInt("stackcapacity");




        JSONArray requestsJSON = objTarget.getJSONArray("requests");
        






        HashMap<String,Box> boxMap=new HashMap<>();
        HashMap<String,BoxStack> stacks=new HashMap<>();
        JSONArray stacksJSON = objTarget.getJSONArray("stacks");
        for (int i = 0; i < stacksJSON.length(); i++) {
            JSONObject o = stacksJSON.getJSONObject(i);
            BoxStack temp=getBoxStack(o,boxMap,stackcapacity);
            stacks.put(temp.name,temp);
        }

        HashMap<String,BufferPoint> bps=new HashMap<>();
        JSONArray bufferpointsJSON = objTarget.getJSONArray("bufferpoints");
        for (int i = 0; i < bufferpointsJSON.length(); i++) {
            JSONObject o = bufferpointsJSON.getJSONObject(i);
            BufferPoint temp=getBP(o);
            bps.put(temp.name,temp);
        }

        ArrayList<Vehicle> vehicles= new ArrayList<>();
        JSONArray vehiclesJSON = objTarget.getJSONArray("vehicles");
        for (int i = 0; i < vehiclesJSON.length(); i++) {
            JSONObject o = vehiclesJSON.getJSONObject(i);
            vehicles.add(getVehicle(o, vehiclespeed, loadingduration));
        }

        HashMap<String, Location> locations=new HashMap<>();
        locations.putAll(bps);
        locations.putAll(stacks);

        for (int i = 0; i < requestsJSON.length(); i++) {
            JSONObject o = requestsJSON.getJSONObject(i);

            getRequest(o,boxMap,locations);
        }

        return  new Warehouse(
                new ArrayList<>(stacks.values()),
                new ArrayList<>(bps.values()),
                new ArrayList<>(boxMap.values()),
                vehicles
        );

    }

    private static void getRequest(JSONObject o, HashMap<String,Box> boxMap, HashMap<String, Location> locations) {
        int id=o.getInt("ID");
        String puString=o.get("pickupLocation").toString().replaceAll("[\\[\\]\"]","");
        String plString=o.get("placeLocation").toString().replaceAll("[\\[\\]\"]","");

        Location pu= locations.get(puString);
        Location pl= locations.get(plString);
        Box box=boxMap.get(o.getString("boxID"));

        if(pu==null||pl==null){
            throw null;
        }

        if(pu instanceof BufferPoint){
            box=new Box(o.getString("boxID"),pu);
            boxMap.put(box.name,box);
            locations.get(pu.name).loadBox(box);
        }

        box.setIDRequest(id);
        box.setPlaceLocation(pl);
    }

    private static Vehicle getVehicle(JSONObject o,int speed,int loadingDuration) {
        int x;
        int y;
        if(o.has("xCoordinate")){
            x=o.getInt("xCoordinate");
            y=o.getInt("yCoordinate");
        }else{
            x=o.getInt("x");
            y=o.getInt("y");
        }
        return new Vehicle(
                o.getInt("ID"),
                o.getString("name"),
                o.getInt("capacity"),
                x,
                y,
                speed,
                loadingDuration
        );
    }

    private static BufferPoint getBP(JSONObject o) {
        return new BufferPoint(
                o.getInt("ID"),
                o.getString("name"),
                o.getInt("x"),
                o.getInt("y")
        );
    }

    private static BoxStack getBoxStack(JSONObject o, HashMap<String,Box> boxStack, int stackcapacity) {
        BoxStack temp= new BoxStack(
                o.getInt("ID"),
                o.getString("name"),
                o.getInt("x"),
                o.getInt("y"),
                stackcapacity
        );
        JSONArray boxes=o.getJSONArray("boxes");
        for (int i = 0; i <boxes.length(); i++) {
            Box box=new Box(boxes.get(i).toString(),temp);
            temp.addBox(box);
            boxStack.put(boxes.get(i).toString(),box);
        }
        return temp;
    }
}
