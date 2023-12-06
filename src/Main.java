import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;


public class Main {
    public static void main(String[] args) throws Exception {
        Warehouse wh=JSONReader.getWarehouse(new File("resource/I20_20_2_2_8b2.json"));
//        Warehouse wh=JSONReader.getWarehouse(new File("resource/I15_16_1_3.json"));


        wh.solve(true);

//        JFrame frame =new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.getContentPane().add(wh);
//        frame.setSize(1000,500);
//        frame.setVisible(true);
    }
}
