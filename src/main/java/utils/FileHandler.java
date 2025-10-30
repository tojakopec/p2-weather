package utils;

import models.Location;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    @SuppressWarnings("unchecked")
    public static <T> List<T> readFromFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) return new ArrayList<>();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (List<T>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static <T> void writeToFile(String filename, List<T> items) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
