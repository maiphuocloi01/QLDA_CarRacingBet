package uit.dayxahoi.racingbet.controller;

import uit.dayxahoi.racingbet.util.ResourceFile;

import java.io.*;

public class CommonController {

    private static CommonController instance;

    private CommonController() {

    }

    public static CommonController getInstance() {
        if (instance == null)
            instance = new CommonController();
        return instance;
    }

    public void writeObjectToFile(Object serObj, String username) {
        File file = new File("data/user", username + ".dat");
        File fileParent = file.getParentFile();
        if (fileParent.mkdirs()) {
            System.out.println("success");
        } else {
            System.out.println("failed");
        }
        try {
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(serObj);
            objectOut.close();
            System.out.println("The Object  was succesfully written to a file");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Object readObjectFromFile(String username) {
        File file = new File("data/user", username + ".dat");
        Object result = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            result = ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public boolean isExistData(String username) {
        File file = new File("data/user", username + ".dat");
        return file.exists();
    }

}
