package Model.FIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.print.attribute.HashPrintServiceAttributeSet;

import java.awt.image.BufferedImage;
import Model.User.*;

/**
 * This class Load eveery thing we need from SavedFiles
 */
public class Loader extends FIO {

    private ObjectInputStream objectReader;
    private Scanner textReader;

    /**
     * 
     * @param mainFolder is the address of main folder
     */
    public Loader(String mainFolder) {
        super(mainFolder);
    }

    /**
     * Load Saved users
     * 
     * @return the loaded Users
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public synchronized HashMap<String, User> loadSavedUsers() throws IOException, ClassNotFoundException {
        HashMap<String, User> loadedUsers = new HashMap<String, User>();

        Path path = Paths.get("../" + mainFolder + "/Users");// load user Folder

        DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path);// load all file at folder
        for (Path path2 : directoryStream) {// iterate through all files
            User temp = (User) loadObject(path2.toString());
            loadedUsers.put(temp.getUserName(), temp);

        }
        return loadedUsers;
    }

    /**
     * Load any kind of object from address
     * 
     * @param address
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public synchronized Object loadObject(String address) throws IOException, ClassNotFoundException {
        objectReader = new ObjectInputStream(new FileInputStream(new File(address)));
        Object temp = objectReader.readObject();
        objectReader.close();
        return temp;
    }

    public synchronized String loadMapFile(String mapname) throws IOException {
        File mapFile = new File("./View/MapFiles/" + mapname + "/");// make a file to read from

        String mapPattern = "";

        textReader = new Scanner(mapFile);
        while (textReader.hasNext()) {
            mapPattern += textReader.next();
            mapPattern += "\n";
        }
        textReader.close();
        ;
        return mapPattern;
    }

    public HashMap<String, BufferedImage> loadGameImages() throws IOException {

        HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>();

        Path picturesFolder = Paths.get("./View/GamePictures/");
        DirectoryStream<Path> directoryStream = Files.newDirectoryStream(picturesFolder);// load all file at folder

        for (Path path2 : directoryStream) {// iterate through all files

            if (!path2.toFile().isFile()) {// load every single folder inside the GamePictures
                DirectoryStream<Path> picStream = Files.newDirectoryStream(path2);
                Iterator<Path> it = picStream.iterator();
                while (it.hasNext()) {// load every pic File inside the folder
                    Path tempPath = it.next();
                    BufferedImage image = ImageIO.read(tempPath.toFile());
                    String picName = tempPath.toFile().getName();
                    images.put(picName.substring(0, picName.indexOf(".")), image);
                }
            }

        }
        return images;
    }

    /**
     * Load SereversList
     * 
     * @return the HashMap of Servers
     * @throws FileNotFoundException
     */
    public synchronized HashMap<String, HashMap<String, Integer>> loadServerList() throws FileNotFoundException {
        HashMap<String, HashMap<String, Integer>> servers = new HashMap<String, HashMap<String, Integer>>();

        File file = new File("./Model/Network/servers.txt/");
        textReader = new Scanner(file);

        while (textReader.hasNext()) {
            String serverName = textReader.next();
            String serverIP = textReader.next();
            int serverPort = Integer.parseInt(textReader.next());
            HashMap<String, Integer> temp = new HashMap<String, Integer>(1);
            temp.put(serverIP, serverPort);
            servers.put(serverName, temp);
        }

        textReader.close();
        return servers;
    }
}