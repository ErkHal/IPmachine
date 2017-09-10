package EH.com.company.IpConversionTool;

import EH.com.company.IpConversionTools.IpConversionTools;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * @author ErkHal
 *
 * Test class for testing IP conversion using IpConversionTools package
 */

public class IpConversionToolsTest {

    String ip1 = "128.138.243.100/18";
    String ip2 = "168.138.128.100/23";
    String ip3 = "255.64.128.100/22";
    String ip4 = "240.64.168.100/16";
    String ip5 = "240.64.168.100/8";

    @Test
    public void calculateAddress() throws Exception {

        ArrayList<String> results = IpConversionTools.calculateAddress(ip1);
        assertEquals("128.138.192.0", results.get(0));

        ArrayList<String> results1 = IpConversionTools.calculateAddress(ip2);
        assertEquals("168.138.128.0", results1.get(0));

        ArrayList<String> results2 = IpConversionTools.calculateAddress(ip3);
        assertEquals("255.64.128.0", results2.get(0));

        ArrayList<String> results3 = IpConversionTools.calculateAddress(ip4);
        assertEquals("240.64.0.0", results3.get(0));

        ArrayList<String> results4 = IpConversionTools.calculateAddress(ip5);
        assertEquals("240.0.0.0", results4.get(0));

    }

    @Test
    public void testAddressClass() throws Exception {

        assertEquals("Class B", IpConversionTools.determineIpClass(ip1));
        assertEquals("Class B", IpConversionTools.determineIpClass(ip2));
        assertEquals("Class Unknown", IpConversionTools.determineIpClass(ip3));
        assertEquals("Class E", IpConversionTools.determineIpClass(ip4));
        assertEquals("Class E", IpConversionTools.determineIpClass(ip5));

    }
}