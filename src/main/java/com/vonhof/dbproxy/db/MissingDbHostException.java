package com.vonhof.dbproxy.db;

import com.vonhof.dbproxy.cluster.Host;

/**
 *
 * @author Henrik Hofmeister <@vonhofdk>
 */
public class MissingDbHostException extends Exception {

    public MissingDbHostException(String type) {
        super(type);
    }
    public MissingDbHostException(String type,Host host,Throwable ex) {
        super(String.format("%s: %s not found",type,host), ex);
    }

    public MissingDbHostException(String name, Throwable ex) {
        super(name, ex);
    }

}
