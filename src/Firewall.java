import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Firewall {

	private HashMap<Long, Rule> networkRules;

	public static void main(String [] args) {
		Firewall fw = new Firewall("illumioTest.csv");
		System.out.println(fw.acceptPacket("inbound", "tcp", 80, "192.168.1.2"));
		System.out.println(fw.acceptPacket("inbound", "udp", 53, "192.168.2.1"));
		System.out.println(fw.acceptPacket("outbound", "tcp", 10234, "192.168.10.11"));
		System.out.println(fw.acceptPacket("inbound", "tcp", 81, "192.168.1.2"));
		System.out.println(fw.acceptPacket("inbound", "udp", 24, "52.12.48.92"));
		System.out.println(fw.acceptPacket("outbound", "udp", 2, "255.255.255.255"));
		System.out.println(fw.acceptPacket("inbound", "tcp", 0, "52.12.48.92"));
		System.out.println(fw.acceptPacket("inbound", "tcp", 582, "0.0.0.0"));
	}

	public Firewall(String filePath) {
		File file = new File(filePath);
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			networkRules = new HashMap<Long, Rule>();
			String line;
			while((line = br.readLine()) != null) {
				String [] ruleInfo = line.split(",");
				String direction = ruleInfo[0];
				String protocol = ruleInfo[1];

				if(ruleInfo[2].contains("-")) {
					String [] portAddresses = ruleInfo[2].split("-");
					int lowPort = Integer.parseInt(portAddresses[0]);
					int highPort = Integer.parseInt(portAddresses[1]);
					if(ruleInfo[3].contains("-")) {
						String [] ipAddresses = ruleInfo[3].split("-");
						long lowIp = ipToLong(InetAddress.getByName(ipAddresses[0]));
						long highIp = ipToLong(InetAddress.getByName(ipAddresses[1]));
						for(int portNum = lowPort; portNum <= highPort; portNum++) {
							for(long ipNum = lowIp; ipNum <= highIp; ipNum++) {
								Rule rule = new Rule(direction, protocol, portNum, ipNum);
								networkRules.put(rule.hashCode, rule);
							}
						}
					} else {
						long ipNum = ipToLong(InetAddress.getByName(ruleInfo[3]));
						for(int portNum = lowPort; portNum <= highPort; portNum++) {
							Rule rule = new Rule(direction, protocol, portNum, ipNum);
							networkRules.put(rule.hashCode, rule);
						}
					}
				} else if(ruleInfo[3].contains("-")) {
					String [] ipAddresses = ruleInfo[3].split("-");
					long lowIp = ipToLong(InetAddress.getByName(ipAddresses[0]));
					long highIp = ipToLong(InetAddress.getByName(ipAddresses[1]));
					int portNum = Integer.parseInt(ruleInfo[2]);
					for(long ipNum = lowIp; ipNum <= highIp; ipNum++) {
						Rule rule = new Rule(direction, protocol, portNum, ipNum);
						networkRules.put(rule.hashCode, rule);
					}
				} else {
					Rule rule = new Rule(direction, protocol, Integer.parseInt(ruleInfo[2]), ipToLong(InetAddress.getByName(ruleInfo[3])));
					networkRules.put(rule.hashCode, rule);
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Cannot find file "+ filePath );
		}
		catch(Exception e) {
			System.out.println("Exeption occured " + e.getMessage());
		}
	}

	public boolean acceptPacket(String direction, String protocol, int portAddress, String ipAddress) {
		try {
			Rule rule = new Rule(direction, protocol, portAddress, ipToLong(InetAddress.getByName(ipAddress)));
			if(networkRules.containsKey(rule.hashCode)) {
				return true;
			}
		} catch (UnknownHostException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	/* 
	// helper function that leverages the fact that IP addresses are a sequence of 4 numbers between 0-255 to convert
	// each InetAddress into a long 
	// (inspired by StackOverflow: https://stackoverflow.com/questions/12057853/how-to-convert-string-ip-numbers-to-integer-in-java)
	*/
	private static long ipToLong(InetAddress ip) {
		byte[] octets = ip.getAddress();
		long result = 0;
		for (byte eightBit : octets) {
			result <<= 8;
			result = result | (eightBit & 0xff);
		}
		return result;
	}
}