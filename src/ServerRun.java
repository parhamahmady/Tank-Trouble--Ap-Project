import java.util.Scanner;

import Model.Network.*;

/**
 * ToRun the Server
 */
public class ServerRun {
    public static void main(String[] args) {
        Scanner fScanner = new Scanner(System.in);

        Server server = new Server(fScanner.nextInt());// scan the port first

        System.out.println("enter 0 to ShutDown");
        if (fScanner.nextInt() == 0) {
            server.shutDownServer();
        }
        fScanner.close();
    }
}