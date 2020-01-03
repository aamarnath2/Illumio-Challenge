public class Rule {
	String direction;
	String protocol;
	int portAddress;
	long ipAddress;
	long hashCode; //member variable that represents a unique identifier for a rule based on the direction, protocol, portaddress and ipaddress

	public Rule (String dir, String prot, int port, long ip) {
		this.direction = dir;
		this.protocol = prot;
		this.portAddress = port;
		this.ipAddress = ip;
		this.hashCode = dir.hashCode() + prot.hashCode() + port + ip;
	}

	@Override
    public boolean equals(Object otherRule) {
        if (this == otherRule) return true;
        if (!(otherRule instanceof Rule)) return false;
        Rule networkRule = (Rule) otherRule;
        return  this.direction.equals(networkRule.direction) && this.protocol.equals(networkRule.protocol) && this.portAddress == networkRule.portAddress && this.ipAddress == networkRule.ipAddress;
    }
}