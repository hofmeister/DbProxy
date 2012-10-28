package com.vonhof.dbproxy.cluster;

/**
 *
 * @author Henrik Hofmeister <@vonhofdk>
 */
public class Host {
    private String type;
    private String host;
    private String group;
    private int port;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 89 * hash + (this.host != null ? this.host.hashCode() : 0);
        hash = 89 * hash + (this.group != null ? this.group.hashCode() : 0);
        hash = 89 * hash + this.port;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Host other = (Host) obj;
        if ((this.type == null) ? (other.type != null) : !this.type.equals(other.type)) {
            return false;
        }
        if ((this.host == null) ? (other.host != null) : !this.host.equals(other.host)) {
            return false;
        }
        if ((this.group == null) ? (other.group != null) : !this.group.equals(other.group)) {
            return false;
        }
        if (this.port != other.port) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s:%s",host,port);
    }
    
    
}
