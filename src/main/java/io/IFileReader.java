package io;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * basic io function
 * java.io
 * */
public class IFileReader {
    public static void main(String[] args) {
        try {
            long startTime = System.currentTimeMillis();
            FileReader fr = new FileReader("src/main/resources/db.data");
            int chRead;
            while((chRead=fr.read()) != -1){
                 System.out.print((char) chRead);
            }
            System.out.println("waste time: " + (System.currentTimeMillis()-startTime));
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
