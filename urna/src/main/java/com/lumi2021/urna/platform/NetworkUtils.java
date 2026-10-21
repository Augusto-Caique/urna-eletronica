package com.lumi2021.urna.platform;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class NetworkUtils {
    public static Inet4Address[] getLocalIPv4Addresses()
            throws SocketException {

        List<Inet4Address> addresses = new ArrayList<>();

        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();

            if (!networkInterface.isUp()
                    || networkInterface.isLoopback()
                    || networkInterface.isVirtual()) continue;

            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();

            while (inetAddresses.hasMoreElements()) {
                InetAddress address = inetAddresses.nextElement();

                if (address instanceof Inet4Address
                        && !address.isLoopbackAddress()
                        && !address.isLinkLocalAddress()) {
                    addresses.add((Inet4Address) address);
                }
            }
        }

        var buffer = new Inet4Address[addresses.size()];
        return addresses.toArray(buffer);
    }
}