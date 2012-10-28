package com.vonhof.dbproxy.cluster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Henrik Hofmeister <@vonhofdk>
 */
public class Cluster {
    private final Set<Host> hosts = new HashSet<Host>();
    
    public void add(Host host) {
        hosts.add(host);
    }
    
    public List<Host> getByType(String type) {
        List<Host> out = new ArrayList<Host>();
        for(Host host:hosts) {
            if (host.getType().equalsIgnoreCase(type)) {
                out.add(host);
            }
        }
        return out;
    }
    
    public List<Host> getByHost(String hostName) {
        List<Host> out = new ArrayList<Host>();
        for(Host host:hosts) {
            if (host.getHost().equalsIgnoreCase(hostName)) {
                out.add(host);
            }
        }
        return out;
    }

    public Set<Host> getHosts() {
        return hosts;
    }
}
