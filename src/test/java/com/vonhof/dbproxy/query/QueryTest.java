/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vonhof.dbproxy.query;

import com.vonhof.dbproxy.query.Query.WhereStatement;
import junit.framework.TestCase;

/**
 *
 * @author Henrik Hofmeister <@vonhofdk>
 */
public class QueryTest extends TestCase {
    
    public QueryTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of select method, of class Query.
     */
    public void testSelect() {
        Query.select("S.FieldA","S.FieldB")
                .from("SomeTable","S")
                .joinLeft("OtherTable","0")
                    .eq("S.FieldA",Query.field("O.FieldA"))
                    .end()
                .joinRight("OtherTable","0")
                    .eq("S.FieldA",Query.field("O.FieldA"))
                    .end()
                .where()
                        .eq("FieldA", Query.field("FieldB"))
                        .gt("FieldA", 5)
                        .between("FieldB", 2,100)
                    .or()
                        .gtEq("FieldA", 100)
                .end();
        
    }

}
