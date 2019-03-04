package Backend;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

public class CSVReader {

    public ArrayList<String> fetchInfo(String path){

        BufferedReader br = null;
        String line;
        
        ArrayList<String> info = new ArrayList<String>();

        try {
            br = new BufferedReader(new FileReader(path));
            while ((line = br.readLine()) != null) {
                info.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return info;
    }
}















