import java.io.File;
public class Main {
    public static void main(String[] args) throws Exception {
//        Warehouse wh=JSONReader.getWarehouse(new File("resource/I3_3_1_5.json"));
        Warehouse wh=JSONReader.getWarehouse(new File("resource/I20_20_2_2_8b2.json"));
//        Warehouse wh=JSONReader.getWarehouse(new File("resource/I10_10_1.json"));
//        Warehouse wh=JSONReader.getWarehouse(new File("resource/I3_3_1.json"));
//        Warehouse wh=JSONReader.getWarehouse(new File("resource/I15_16_1_3.json"));
//        Warehouse wh=JSONReader.getWarehouse(new File("resource/I30_100_1_1_10.json"));
//        Warehouse wh=JSONReader.getWarehouse(new File("resource/I30_100_3_3_10.json"));
//        Warehouse wh=JSONReader.getWarehouse(new File("resource/I30_200_3_3_10.json"));
//        Warehouse wh=JSONReader.getWarehouse(new File("resource/I100_50_2_2_8b2.json"));
//        Warehouse wh=JSONReader.getWarehouse(new File("resource/I100_120_2_2_8b2.json"));

        wh.solve(false);
    }
}
