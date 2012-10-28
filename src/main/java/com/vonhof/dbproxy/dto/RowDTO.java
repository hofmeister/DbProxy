package com.vonhof.dbproxy.dto;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Henrik Hofmeister <@vonhofdk>
 */
public class RowDTO extends HashMap<String,Object> {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
