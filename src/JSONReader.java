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

        JSONArray stacksJSON = objTarget.getJSONArray("stacks");
        JSONArray bufferpointsJSON = objTarget.getJSONArray("bufferpoints");
        JSONArray vehiclesJSON = objTarget.getJSONArray("vehicles");
        JSONArray requestsJSON = objTarget.getJSONArray("requests");
        
        HashMap<String,BoxStack> stacks=new HashMap<>();
        HashMap<String,BufferPoint> bps=new HashMap<>();
        HashMap<String,PickUp> locations=new HashMap<>();

        ArrayList<Vehicle> vehicles= new ArrayList<>();
        ArrayList<MoveRequest> requests= new ArrayList<>();

        HashMap<String,Box> boxMap=new HashMap<>();
        
        for (int i = 0; i < stacksJSON.length(); i++) {
            JSONObject o = stacksJSON.getJSONObject(i);
            stacks.put(o.getString("name"),getBoxStack(o,boxMap,stackcapacity));
        }
        for (int i = 0; i < bufferpointsJSON.length(); i++) {
            JSONObject o = bufferpointsJSON.getJSONObject(i);
            bps.put(o.getString("name"),getBP(o));
        }
        for (int i = 0; i < vehiclesJSON.length(); i++) {
            JSONObject o = vehiclesJSON.getJSONObject(i);
            vehicles.add(getVehicle(o, vehiclespeed, loadingduration));
        }
        locations.putAll(bps);
        locations.putAll(stacks);

        for (int i = 0; i < requestsJSON.length(); i++) {
            JSONObject o = requestsJSON.getJSONObject(i);
            requests.add(getRequest(o,boxMap,locations));
        }
        return  new Warehouse(
                loadingduration,
                vehiclespeed,
                stackcapacity,
                stacks,
                bps,
                vehicles,
                requests
        );

    }

    private static MoveRequest getRequest(JSONObject o, HashMap<String,Box> boxMap, HashMap<String, PickUp> locations) {
        int id=o.getInt("ID");
        String puString=o.get("pickupLocation").toString().replaceAll("[\\[\\]\"]","");
        String plString=o.get("placeLocation").toString().replaceAll("[\\[\\]\"]","");

        PickUp pu= locations.get(puString);
        PickUp pl= locations.get(plString);
        Box box=boxMap.get(o.getString("boxID"));
        if(pu==null||pl==null){
            throw null;
        }
        MoveRequest temp;
        if(pu instanceof BufferPoint){
            box=new Box(o.getString("boxID"));
            boxMap.put(box.name,box);
            locations.get(pu.name).loadBox(box);
            temp=new MoveRequest(
                    id,
                    pu,
                    pl,
                    box
                    );
        }else{
            temp=new MoveRequest(id,pu,pl,box);
        }


        return temp;

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
        Vehicle temp= new Vehicle(
                o.getInt("ID"),
                o.getString("name"),
                o.getInt("capacity"),
                x,
                y,
                speed,
                loadingDuration
        );
        return temp;
    }

    private static BufferPoint getBP(JSONObject o) {
        BufferPoint temp= new BufferPoint(
                o.getInt("ID"),
                o.getString("name"),
                o.getInt("x"),
                o.getInt("y")
        );
        return temp;
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
            Box box=new Box(boxes.get(i).toString());
            temp.addBox(box);
            boxStack.put(boxes.get(i).toString(),box);
        }
        return temp;
    }
}
