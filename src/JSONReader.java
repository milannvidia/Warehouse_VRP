import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

public class JSONReader {
    public static Warehouse getWarehouse(File instance) throws IOException {
        String JsonString = Files.readString(instance.toPath());
        JSONObject objTarget = new JSONObject(JsonString);

        ArrayList<Box> boxes = new ArrayList<>();
        ArrayList<Vehicle> vehicles = getVehicles(objTarget);
        ArrayList<BufferPoint> BPs = getBPs(objTarget);
        ArrayList<BoxStack> BSs = getBSs(objTarget, boxes);
        ArrayList<Location> locations = new ArrayList<>();
        locations.addAll(BPs);
        locations.addAll(BSs);

        analyseRequests(objTarget, locations, boxes);

        return new Warehouse(
                BSs,
                BPs,
                boxes,
                vehicles);

    }

    private static ArrayList<Vehicle> getVehicles(JSONObject objTarget) {
        ArrayList<Vehicle> vehicles = new ArrayList<>();

        JSONArray vehiclesJSON = objTarget.getJSONArray("vehicles");
        int vehicleSpeed = objTarget.getInt("vehiclespeed");
        int loadingDuration = objTarget.getInt("loadingduration");

        for (int i = 0; i < vehiclesJSON.length(); i++) {
            JSONObject o = vehiclesJSON.getJSONObject(i);
            int x;
            int y;
            if (o.has("xCoordinate")) {
                x = o.getInt("xCoordinate");
                y = o.getInt("yCoordinate");
            } else {
                x = o.getInt("x");
                y = o.getInt("y");
            }
            vehicles.add(new Vehicle(
                    o.getInt("ID"),
                    o.getString("name"),
                    o.getInt("capacity"),
                    x,
                    y,
                    vehicleSpeed,
                    loadingDuration));
        }
        return vehicles;
    }

    private static ArrayList<BufferPoint> getBPs(JSONObject objTarget) {
        ArrayList<BufferPoint> BPs = new ArrayList<>();
        JSONArray bufferPointsJSON = objTarget.getJSONArray("bufferpoints");
        for (int i = 0; i < bufferPointsJSON.length(); i++) {
            JSONObject o = bufferPointsJSON.getJSONObject(i);
            BPs.add(new BufferPoint(
                    o.getInt("ID"),
                    o.getString("name"),
                    o.getInt("x"),
                    o.getInt("y")));
        }
        return BPs;
    }

    private static ArrayList<BoxStack> getBSs(JSONObject objTarget, ArrayList<Box> boxes) {
        ArrayList<BoxStack> BPs = new ArrayList<>();
        JSONArray bufferPointsJSON = objTarget.getJSONArray("stacks");
        int stackCapacity = objTarget.getInt("stackcapacity");

        for (int i = 0; i < bufferPointsJSON.length(); i++) {
            JSONObject o = bufferPointsJSON.getJSONObject(i);
            BoxStack temp = new BoxStack(
                    o.getInt("ID"),
                    o.getString("name"),
                    o.getInt("x"),
                    o.getInt("y"),
                    stackCapacity);
            BPs.add(temp);

            JSONArray boxArray = o.getJSONArray("boxes");
            for (int j = 0; j < boxArray.length(); j++) {
                Box box = new Box(boxArray.get(j).toString(), temp);
                temp.addBox(box);
                boxes.add(box);
            }
        }
        return BPs;
    }

    private static void analyseRequests(JSONObject objTarget, ArrayList<Location> locations, ArrayList<Box> boxes) {
        JSONArray requestsJSON = objTarget.getJSONArray("requests");
        HashMap<String, Box> boxMap = new HashMap<>();
        HashMap<String, Location> locationMap = new HashMap<>();

        for (Box box : boxes) {
            boxMap.put(box.getName(), box);
        }
        for (Location location : locations) {
            locationMap.put(location.name, location);
        }

        for (int i = 0; i < requestsJSON.length(); i++) {
            JSONObject o = requestsJSON.getJSONObject(i);

            int id = o.getInt("ID");
            String puString = o.get("pickupLocation").toString().replaceAll("[\\[\\]\"]", "");
            String plString = o.get("placeLocation").toString().replaceAll("[\\[\\]\"]", "");
            Location pu = locationMap.get(puString);
            Location pl = locationMap.get(plString);
            Box box = boxMap.get(o.getString("boxID"));

            if (pu == null || pl == null) {
                throw null;
            }

            if (pu instanceof BufferPoint) {
                box = new Box(o.getString("boxID"), pu);
                boxMap.put(box.getName(), box);
                boxes.add(box);
                pu.loadBox(box);
            }

            box.setIDRequest(id);
            box.setPlaceLocation(pl);

        }

    }
}
