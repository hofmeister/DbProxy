package com.vonhof.dbproxy.db.impl;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.ServerAddress;
import com.vonhof.dbproxy.cluster.Cluster;
import com.vonhof.dbproxy.cluster.Host;
import com.vonhof.dbproxy.db.DbHandler;
import com.vonhof.dbproxy.db.InvalidArgumentException;
import com.vonhof.dbproxy.db.MissingDbHostException;
import com.vonhof.dbproxy.db.Transaction;
import com.vonhof.dbproxy.dto.DatabaseDTO;
import com.vonhof.dbproxy.dto.IndexDTO;
import com.vonhof.dbproxy.dto.RowDTO;
import com.vonhof.dbproxy.dto.RowSetDTO;
import com.vonhof.dbproxy.dto.TableDTO;
import com.vonhof.dbproxy.dto.ViewDTO;
import com.vonhof.dbproxy.query.Query;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Henrik Hofmeister <@vonhofdk>
 */
public class MongoHandler extends DbHandler {
    private static final Logger LOG = Logger.getLogger(MongoHandler.class.getName());
    public static final String TYPE = "MongoDb";
    
    private final List<Mongo> mongos = new ArrayList<Mongo>();
    
    private final Map<String, List<DB>> dbs = new HashMap<String, List<DB>>();

    public MongoHandler(Cluster cluster) {
        super(TYPE, cluster);
        
    }
    
    private void initMongos() throws MissingDbHostException {
        synchronized(mongos) {
            if (!mongos.isEmpty()) {
                return;
            }
            
            Map<String, List<Host>> hosts = getHosts();
            for(String group:hosts.keySet()) {
                
                final List<ServerAddress> addrs = new ArrayList<ServerAddress>();
                for(Host host:hosts.get(group)) {
                    try {
                        addrs.add(new ServerAddress(host.getHost(),host.getPort()));
                    } catch (UnknownHostException ex) {
                        LOG.log(Level.SEVERE, null, ex);
                        throw new MissingDbHostException(TYPE,host, ex);
                    }
                }
                
                if (addrs.isEmpty()) {
                    continue;
                }
                
                mongos.add(new Mongo(addrs));
            }
        }
    }
    
    private List<DB> getDBs(String name) throws MissingDbHostException {
        initMongos();
        
        synchronized(dbs) {
            if (dbs.containsKey(name)) {
                return dbs.get(name);
            }

            List<DB> out = new ArrayList<DB>();

            for(Mongo mongo:mongos) {
                out.add(mongo.getDB(name));
            }
            dbs.put(name, Collections.unmodifiableList(out));
            return out;
        }
    }
    
    private List<DBCollection> getCollections(String dbName,String tableName) throws MissingDbHostException {
        List<DB> dBs = getDBs(dbName);
        
        List<DBCollection> out = new ArrayList<DBCollection>();
        for(DB db:dBs) {
            DBCollection coll = db.getCollection(tableName);
            out.add(coll);
        }
        return out;
    }

    @Override
    public void create(DatabaseDTO db) throws MissingDbHostException, InvalidArgumentException {
        //Getting them creates them
        getDBs(db.getName());
    }

    @Override
    public void update(DatabaseDTO oldDb, DatabaseDTO db) throws MissingDbHostException, InvalidArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void drop(DatabaseDTO db) throws MissingDbHostException, InvalidArgumentException {
        List<DB> dBs = getDBs(db.getName());
        for(DB mongoDb:dBs) {
            mongoDb.dropDatabase();
        }
    }

    @Override
    public void create(TableDTO table) throws MissingDbHostException, InvalidArgumentException {
        List<DBCollection> colls = getCollections(table.getDb(),table.getName());
        for(DBCollection coll:colls) {
            //@TODO: Check if deprecated indices exists and if so - remove them
            for(IndexDTO index:table.getIndices()) {
                BasicDBObject fields = new BasicDBObject();
                for(Entry<String,IndexDTO.FieldIndexOptions> field:index.getFields().entrySet()) {
                    fields.append(field.getKey(), field.getValue().isAscending() ? 1 : -1);
                }
                coll.ensureIndex(fields,index.getName(), index.isUnique());
            }
        }
    }

    @Override
    public void update(TableDTO oldTable, TableDTO table) throws MissingDbHostException, InvalidArgumentException {
        if (!oldTable.getName().equals(table.getName())) {
            List<DBCollection> collections = getCollections(oldTable.getDb(), oldTable.getName());
            for(DBCollection coll:collections) {
                coll.rename(table.getName());
            }
        }
        create(table);
    }

    @Override
    public void drop(TableDTO table) throws MissingDbHostException, InvalidArgumentException {
        List<DB> dBs = getDBs(table.getDb());
        for(DB db:dBs) {
            DBCollection coll = db.getCollection(table.getName());
            coll.drop();
        }
    }

    @Override
    public void create(ViewDTO view) throws MissingDbHostException, InvalidArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(ViewDTO oldView, ViewDTO view) throws MissingDbHostException, InvalidArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void drop(ViewDTO view) throws MissingDbHostException, InvalidArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void insert(String dbName,String tableName,RowDTO row) throws MissingDbHostException, InvalidArgumentException {
        List<DBCollection> colls = getCollections(dbName,tableName);
        for(DBCollection coll:colls) {
            coll.insert(rowToDb(row));
        }
    }
    
    private DBObject rowToDb(RowDTO row) {
        BasicDBObject dbRow = new BasicDBObject(row);
        dbRow.append("_id", row.getId());
        return dbRow;
    }
    
    private List<DBObject> rowsToDb(Collection<RowDTO> rows) {
        List<DBObject> dbRows = new ArrayList<DBObject>();
        
        for(RowDTO row:rows) {
            dbRows.add(rowToDb(row));
        }
        return dbRows;
    }

    @Override
    public void bulkInsert(String dbName,String tableName,Collection<RowDTO> rows) throws MissingDbHostException, InvalidArgumentException {
        List<DBObject> dbRows = rowsToDb(rows);
        
        List<DBCollection> colls = getCollections(dbName, tableName);
        for(DBCollection coll:colls) {
            coll.insert(dbRows);
        }        
    }

    @Override
    public void updateOne(String dbName,String tableName,RowDTO row) throws MissingDbHostException, InvalidArgumentException {
        List<DBCollection> colls = getCollections(dbName,tableName);
        for(DBCollection coll:colls) {
            coll.update(new BasicDBObject("_id",row.getId()),rowToDb(row));
        }
    }

    @Override
    public void updateMany(Query q, RowDTO row) throws MissingDbHostException, InvalidArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteOne(String dbName, String tableName, String id) throws MissingDbHostException, InvalidArgumentException {
        List<DBCollection> colls = getCollections(dbName,tableName);
        for(DBCollection coll:colls) {
            coll.remove(new BasicDBObject("_id",id));
        }
    }

    @Override
    public void deleteMany(Query q) throws MissingDbHostException, InvalidArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RowDTO queryOne(Query q) throws MissingDbHostException, InvalidArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RowSetDTO queryAll(Query q) throws MissingDbHostException, InvalidArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Transaction startTransaction(String identifier) throws MissingDbHostException, InvalidArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void commitTransaction(Transaction transaction) throws MissingDbHostException, InvalidArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void rollbackTransaction(Transaction transaction) throws MissingDbHostException, InvalidArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void executeQuickMaintanence() {
        //Nothing to do
    }

    @Override
    public void executeHeavyMaintanence() {
        //Nothing to do
    }

    
}
