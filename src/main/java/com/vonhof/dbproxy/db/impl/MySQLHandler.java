package com.vonhof.dbproxy.db.impl;

import com.vonhof.dbproxy.cluster.Cluster;
import com.vonhof.dbproxy.db.DbHandler;
import com.vonhof.dbproxy.db.InvalidArgumentException;
import com.vonhof.dbproxy.db.MissingDbHostException;
import com.vonhof.dbproxy.db.Transaction;
import com.vonhof.dbproxy.dto.DatabaseDTO;
import com.vonhof.dbproxy.dto.RowDTO;
import com.vonhof.dbproxy.dto.RowSetDTO;
import com.vonhof.dbproxy.dto.TableDTO;
import com.vonhof.dbproxy.dto.ViewDTO;
import com.vonhof.dbproxy.query.Query;
import java.net.ConnectException;
import java.util.Collection;

/**
 *
 * @author Henrik Hofmeister <@vonhofdk>
 */
public class MySQLHandler extends DbHandler {
    public static final String TYPE = "MySQL";
    
    public MySQLHandler(Cluster cluster) {
        super(TYPE, cluster);
    }

    @Override
    public void create(DatabaseDTO db) throws MissingDbHostException, InvalidArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(DatabaseDTO oldDb, DatabaseDTO db) throws MissingDbHostException, InvalidArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void drop(DatabaseDTO db) throws MissingDbHostException, InvalidArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void create(TableDTO table) throws MissingDbHostException, InvalidArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(TableDTO oldTable, TableDTO table) throws MissingDbHostException, InvalidArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void drop(TableDTO table) throws MissingDbHostException, InvalidArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
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
    public void insert(RowDTO row) throws MissingDbHostException, InvalidArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void bulkInsert(Collection<RowDTO> rows) throws MissingDbHostException, InvalidArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateOne(RowDTO row) throws MissingDbHostException, InvalidArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateMany(Query q, RowDTO row) throws MissingDbHostException, InvalidArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteOne(String id) throws MissingDbHostException, InvalidArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void executeHeavyMaintanence() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
