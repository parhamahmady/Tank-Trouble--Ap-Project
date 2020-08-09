package Model.FIO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import Model.User.*;

/**
 * This class save every thing that we need to files
 */
public class Saver extends FIO implements Serializable {
    private ObjectOutputStream objectWriter;// for write object serielized

    /**
     * The Cousntrctor
     */
    public Saver(String mainFolder) {
        super(mainFolder);// to load the FOI class
        try {
            addToFolders(mainFolder, "../" + mainFolder + "/");// make main dir
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Save to the ./Files/Users
     * 
     * @param user that will save
     */
    public void saveUser(User user) throws IOException {
        addToFolders("Users", "../" + mainFolder + "/Users/");// make user dir
        String newFile = "../" + mainFolder + "/Users/" + user.getUserName();// make file address

        saveObject(user, newFile);
    }

    /**
     * Write any Object to the address
     * 
     * @param o
     * @param address
     * @throws IOException
     */
    public void saveObject(Object o, String address) throws IOException {
        cleanUp(address);// clean existed file
        objectWriter = new ObjectOutputStream(new FileOutputStream(new File(address)));// make the output
        objectWriter.writeObject(o);// write
        objectWriter.close();
    }

    /**
     * delete the existing file
     * 
     * @param address of file
     */
    public void cleanUp(String address) {
        File selected = new File(address);
        if (selected.exists()) {
            selected.delete();
            System.out.println("deleted");
        }
    }

}