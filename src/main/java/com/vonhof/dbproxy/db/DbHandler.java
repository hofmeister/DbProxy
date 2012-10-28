package com.vonhof.dbproxy.db;

import com.vonhof.dbproxy.cluster.Cluster;
import com.vonhof.dbproxy.cluster.Host;
import com.vonhof.dbproxy.dto.DatabaseDTO;
import com.vonhof.dbproxy.dto.RowDTO;
import com.vonhof.dbproxy.dto.RowSetDTO;
import com.vonhof.dbproxy.dto.TableDTO;
import com.vonhof.dbproxy.dto.ViewDTO;
import com.vonhof.dbproxy.query.Query;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract public class DbHandler {
    private final String type;
    private final Cluster cluster;

    public DbHandler(String type, Cluster cluster) {
        this.type = type;
        this.cluster = cluster;
    }
    

    protected Cluster getCluster() {
        return cluster;
    }
    
    protected Map<String,List<Host>> getHosts() throws MissingDbHostException {
        List<Host> hosts = cluster.getByType(type);
        if (hosts.isEmpty()) {
            throw new MissingDbHostException(type);
        }
        
        Map<String,List<Host>> out = new HashMap<String, List<Host>>();
        for(Host host:hosts) {
            if (out.containsKey(host.getGroup())) {
                out.put(host.getGroup(), new ArrayList<Host>());
            }
            out.get(host.getGroup()).add(host);
        }
        return out;
    }

    public String getType() {
        return type;
    }
    
    /* Db Structure manipulation */
    abstract public void create(DatabaseDTO db) throws MissingDbHostException,InvalidArgumentException;
    abstract public void update(DatabaseDTO oldDb,DatabaseDTO db) throws MissingDbHostException,InvalidArgumentException;
    abstract public void drop(DatabaseDTO db) throws MissingDbHostException,InvalidArgumentException;
    
    abstract public void create(TableDTO table) throws MissingDbHostException,InvalidArgumentException;
    abstract public void update(TableDTO oldTable,TableDTO table) throws MissingDbHostException,InvalidArgumentException;
    abstract public void drop(TableDTO table) throws MissingDbHostException,InvalidArgumentException;
    
    abstract public void create(ViewDTO view) throws MissingDbHostException,InvalidArgumentException;
    abstract public void update(ViewDTO oldView,ViewDTO view) throws MissingDbHostException,InvalidArgumentException;
    abstract public void drop(ViewDTO view) throws MissingDbHostException,InvalidArgumentException;
    
    /* Row manipulation */
    abstract public void insert(String dbName,String tableName,RowDTO row) throws MissingDbHostException,InvalidArgumentException;
    abstract public void bulkInsert(String dbName,String tableName,Collection<RowDTO> rows) throws MissingDbHostException,InvalidArgumentException;
    
    abstract public void updateOne(String dbName,String tableName,RowDTO row) throws MissingDbHostException,InvalidArgumentException;
    abstract public void updateMany(Query q,RowDTO row) throws MissingDbHostException,InvalidArgumentException;
    
    abstract public void deleteOne(String dbName,String tableName,String id) throws MissingDbHostException,InvalidArgumentException;
    abstract public void deleteMany(Query q) throws MissingDbHostException,InvalidArgumentException;
    
    /* Querying */
    abstract public RowDTO queryOne(Query q) throws MissingDbHostException,InvalidArgumentException;
    abstract public RowSetDTO queryAll(Query q) throws MissingDbHostException,InvalidArgumentException;
    
    /* Transactions */
    abstract public Transaction startTransaction(String identifier) throws MissingDbHostException,InvalidArgumentException;
    abstract public void commitTransaction(Transaction transaction) throws MissingDbHostException,InvalidArgumentException;
    abstract public void rollbackTransaction(Transaction transaction) throws MissingDbHostException,InvalidArgumentException;
    
    /**
     * This method is called whenever the db proxy deems is a good time for quick/shallow maintanence
     */
    abstract public void executeQuickMaintanence();
    
    /**
     * This method is called whenever the db proxy determines is a good time for in-depth maintanence
     */
    abstract public void executeHeavyMaintanence();
}
