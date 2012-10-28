/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vonhof.dbproxy.query;

/**
 *
 * @author Henrik Hofmeister <@vonhofdk>
 */
public interface QueryReader {
    public Query read(String query) throws InvalidQueryException;
}
