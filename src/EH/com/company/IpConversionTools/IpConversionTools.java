package EH.com.company.IpConversionTools;

import java.util.ArrayList;

/**
 * @author ErkHal
 *
 * Tools for calculating network addresse and its class, given an IP and a prefix.
 */

public class IpConversionTools {

    /**
     * Returns the calculated IP network address results in an array. Returns null if cannot convert.
     * @param oldIpWithPrefix
     * @return Retrieve results from list:
     */
     public static ArrayList<String> calculateAddress(String oldIpWithPrefix) {

         ArrayList<String> results = new ArrayList<String>();

         try {

             String[] unTouchedIpArr = oldIpWithPrefix.split("(/)|(\\.)");
             String[] ipOctets = {unTouchedIpArr[0], unTouchedIpArr[1], unTouchedIpArr[2], unTouchedIpArr[3]};

             //Take the fifth element in an array, because that is the prefix always.
             int prefix = Integer.parseInt(unTouchedIpArr[4]);
             String[] binaryNetMaskAddr = convertPrefixToBinaryNetmask(prefix);

             String[] binaryIpAddress = convertToBinaryAddress(ipOctets);

             //This address is filled with correct bits and then given to convertToDecimalAddress method.
             String[] calculatedNetworkAddress = {"00000000", "00000000", "00000000", "00000000"};

             for (int index = 0; index < binaryIpAddress.length; index++) {

                 char[] bytes = binaryIpAddress[index].toCharArray();

                 for (int i = 0; i < binaryIpAddress[index].length(); i++) {

                     if (binaryIpAddress[index].charAt(i) == '1' && binaryNetMaskAddr[index].charAt(i) == '1') {

                         bytes[i] = '1';

                     } else {

                         bytes[i] = '0';
                     }
                 }


                 calculatedNetworkAddress[index] = new String(bytes);

             }

             String networkAddress = convertToDecimalAddress(calculatedNetworkAddress);
             String addressClass = determineIpClass(networkAddress);

                 /**
                  * * 0 = Decimal Network address
                  * 1 = Binary network address
                  * 2 = Network address class
                  * 3 = Original address in binary
                  * 4 = Subnet mask in decimal
                  * 5 = Subnet mask in binary
                  * 6 = Broadcast address in decimal
                  * 7 = Broadcast address in binary
                  * 8 = First host in this network
                  * 9 = First host in binary
                  * 10 = Last host in this network
                  * 11 = Last host in binary
                  * 12 = Amount of hosts in this network
                    */
             // Results:

             // #0 Network address
             results.add(networkAddress + "/" + prefix);

             // #1 Network address in binary
             results.add(cleanIpRepresentation(calculatedNetworkAddress));

             // #2 IP address class
             results.add(addressClass);

             // #3 Original IP address in binary
             results.add(cleanIpRepresentation(binaryIpAddress));

             // #4 Subnet mask
             results.add(convertToDecimalAddress(binaryNetMaskAddr));

             // #5 Subnet mask in binary
             results.add(cleanIpRepresentation(binaryNetMaskAddr));

             // #6 Broadcast address
             String[] binaryBroadcastAddress = getBroadcastAddress(calculatedNetworkAddress, prefix);
             results.add(convertToDecimalAddress(binaryBroadcastAddress) + "/" + prefix);

             // #7 Broadcast address in BINARY
             results.add(cleanIpRepresentation(binaryBroadcastAddress));

             // #8 First host address in this network in DECIMAL
             String[] firstHostAddress = getMinOrMaxHost(calculatedNetworkAddress, "min");
             results.add(convertToDecimalAddress(firstHostAddress));

             // #9 First host address in this network in BINARY
             results.add(cleanIpRepresentation(firstHostAddress));

             // #10 Last host address in this network in DECIMAL
             String[] lastHostAddress = getMinOrMaxHost(binaryBroadcastAddress, "max");
             results.add(convertToDecimalAddress(lastHostAddress));

             // #11 Last host address in this network in BINARY
             results.add(cleanIpRepresentation(lastHostAddress));

             // #12 Amount of hosts in this network
             results.add(""+getAmountOfHosts(prefix));

             return results;

         } catch (Exception invalidInputException) {

             System.out.println("Invalid input, please try again");
             return null;
         }
     }

    /**
     * Return the broadcast address in BINARY string array
     * @param networkAddress
     * @param prefix
     * @return
     */
     private static String[] getBroadcastAddress(String[] networkAddress, int prefix) {

         String bunchedUpAddress = "";

         for(String octet : networkAddress) {

             bunchedUpAddress += octet;
         }

         int count = 32 - prefix;
         //System.out.println(count);

         String broadcastAddress = bunchedUpAddress.substring(0, prefix);

         for(int currentBit = 0; currentBit < count; currentBit++) {

            broadcastAddress += "1";

         }

         broadcastAddress += "0";

         String[] temp = {broadcastAddress.substring(0,8), broadcastAddress.substring(8, 16), broadcastAddress.substring(16, 24), broadcastAddress.substring(24, 32)};

         return temp;

     }

    /**
     * Returns the last or first host address in the network in BINARY.
     * Give either a network or a broadcast address in binary as a parameter.
     * @param address
     * @param minOrMax
     * @return
     */
     private static String[] getMinOrMaxHost(String[] address, String minOrMax) {

         String[] minOrMaxHostAddr = address;

         //Returns the first host address when given the network address.
         if(minOrMax.equals("min")) {

             String lastOctet = minOrMaxHostAddr[3].substring(0, 7);
             String modifiedLastOctet = lastOctet += "1";
             minOrMaxHostAddr[3] = modifiedLastOctet;

         }

         //Returns the last host address when given the broadcast address.
         if(minOrMax.equals("max")) {

             String lastOctet = minOrMaxHostAddr[3].substring(0, 7);
             String modifiedLastOctet = lastOctet += "0";
             minOrMaxHostAddr[3] = modifiedLastOctet;

         }

         return minOrMaxHostAddr;
     }

    /**
     * Returns the amount of hosts in this subnet
     * @param prefix
     * @return
     */
     private static int getAmountOfHosts(int prefix) {

         double hosts = 32 - (double)prefix;
         return (int)Math.pow(2, hosts) - 2;
     }

    /**
     * Determines the class of given IP address.
     * @param ipAddr
     * @return
     */
     public static String determineIpClass(String ipAddr) {

         String ipClass = "";

         String[] trimmedIpAddr = chopUpStringIpAddress(ipAddr);
         String[] binAddress = convertToBinaryAddress(trimmedIpAddr);

         int bitCountInFirstOctet = 0;
         boolean firstZeroNoticed = false;

         //Counts all the ones in the first octet
         for(int index = 0; index < binAddress[0].length(); index++) {

             if(binAddress[0].charAt(index) == '1') {

                 if(!firstZeroNoticed) {
                     bitCountInFirstOctet++;

                 }
             } else {
                 firstZeroNoticed = true;
             }
         }

         if(bitCountInFirstOctet < 5) {

             switch(bitCountInFirstOctet) {

                 case 4:
                     ipClass = "Class E";
                     break;

                 case 3:
                     ipClass = "Class D";
                     break;

                 case 2:
                     ipClass = "Class C";
                     break;

                 case 1:
                     ipClass = "Class B";
                     break;

                 case 0:
                     ipClass = "Class A";

             }
         } else {

             ipClass = "Class Unknown";
         }

         return ipClass;
     }

    /**
     * Chops up decimal ip address into a string array. Throws away the prefix.
     * @param address
     * @return
     */
     private static String[] chopUpStringIpAddress(String address) {

         String[] unTouchedIpArr = address.split("(/)|(\\.)");

         String[] trimmedIpAddress = {unTouchedIpArr[0], unTouchedIpArr[1], unTouchedIpArr[2], unTouchedIpArr[3]};

         return trimmedIpAddress;
     }


    /**
     * Cleans up given string without punctuations.
     * @param ipToBeCleaned
     * @return
     */
    private static String cleanIpRepresentation(String[] ipToBeCleaned){

        String cleanedBinaryIp = ipToBeCleaned[0] + "." + ipToBeCleaned[1] + "." + ipToBeCleaned[2] + "." + ipToBeCleaned[3];

        return cleanedBinaryIp;
    }

    /**
     * Convert given decimal IP address to binary representation.
     * @param octets
     * @return
     */
     private static String[] convertToBinaryAddress(String[] octets) {

         for(int i = 0; i < octets.length; i++) {

             try {
                 int temp = Integer.parseInt(octets[i]);
                 String binaryRep = Integer.toBinaryString(temp);
                 if(binaryRep.length() < 8) {

                     int zeroesToBeAdded = 8 - binaryRep.length();
                     String zeroes = "";
                     for(int index = 0; index < zeroesToBeAdded; index++) {
                         zeroes += "0";
                     }

                     binaryRep = zeroes.concat(binaryRep);

                 }

                 octets[i] = binaryRep;

             } catch(Exception p) {
                 p.printStackTrace();
             }
         }

         return octets;
     }

    /**
     * Converts given binary IP address into a decimal one.
     * @param binOctets
     * @return
     */
     private static String convertToDecimalAddress(String[] binOctets) {

         String decimalIp = "";

         for(int index = 0; index < binOctets.length; index++) {

               int decimalInteger = Integer.parseInt(binOctets[index], 2);

               decimalIp += decimalInteger;

               if(!(index == binOctets.length - 1)) {
                   decimalIp += ".";
               }
         }

         return decimalIp;
     }

    /**
     * Convert given prefix mask to BINARY netmask address.
     * @param prefix
     * @return
     */
     private static String[] convertPrefixToBinaryNetmask(int prefix) {

         String temp = "00000000000000000000000000000000";

         int changedBits = 0;

         for(;changedBits < prefix;) {

             temp = temp.replaceFirst("0", "1");

             changedBits++;
         }

         String[] netMask = {temp.substring(0, 8), temp.substring(8,16), temp.substring(16,24), temp.substring(24,32)};

         return netMask;
     }
}
