package EH.com.company;

import EH.com.company.IpConversionTools.IpConversionTools;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * @Author ErkHal
 *
 * Main for running the IPmachine.
 */

public class Main {

    public static void main(String[] args) {

        System.out.println("############################################################################################");
        System.out.println("############################################################################################");
        System.out.println("################################## IPMACHINE ###############################################");
        System.out.println("############################################################################################");
        System.out.println("############################################################################################");

        try {
            Thread.sleep(1000);
        } catch(InterruptedException i) {
            i.printStackTrace();
        }

        System.out.flush();

        boolean running = true;

        ArrayList<String> newIpResults = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);

        String oldIp = "";

        while(running) {

            System.out.println("\r\nPlease input the IP address with a prefix in format XXXXXXXX.XXXXXXXX.XXXXXXXX.XXXXXXXX/XX ");
            System.out.println(" To exit program, type exit.");

            boolean gotCorrectInput = false;
            while (!gotCorrectInput) {

                oldIp = scanner.nextLine();
                if(!oldIp.equals("EXIT") && !oldIp.equals("exit")) {
                    newIpResults = IpConversionTools.calculateAddress(oldIp);
                } else {
                    System.exit(1);
                }

            if(newIpResults != null) {
                gotCorrectInput = true;
            }

        }
            //Retrieve original IP address in binary from results
            System.out.println("########################################################################################");
            System.out.println("\r\nAddress :       " + oldIp + "   " + newIpResults.get(3));
            System.out.println("Netmask :       " + newIpResults.get(4) + "   " + newIpResults.get(5));

            System.out.println("----------------------------------------------------------------------------------------");

            //Retrieve the decimal network address result, Binary network address result and address class from the results array.
             System.out.println("Network address:        " + newIpResults.get(0) + "     " + newIpResults.get(1) + "     " + newIpResults.get(2));

             //Retrieve the decimal representation of the broadcast address.
            System.out.println("Broadcast address:      " + newIpResults.get(6) + "     " + newIpResults.get(7) + "\r\n");

            //Retrieve the first host in this network and its decimal and binary representations
            System.out.println("First host address:     " + newIpResults.get(8) + "     " + newIpResults.get(9));

            //Retrieve the last host in this network in decimal and binary representation
            System.out.println("Last host address:      " + newIpResults.get(10) + "     " + newIpResults.get(11) + "\r\n");

            //Retrieve the amount of hosts in this network
            System.out.println("Hosts in this network:  " + newIpResults.get(12) + "\r\n");

            System.out.println("########################################################################################");


        }
    }
}
