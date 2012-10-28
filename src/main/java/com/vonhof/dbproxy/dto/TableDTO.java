package com.vonhof.dbproxy.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Henrik Hofmeister <@vonhofdk>
 */
public class TableDTO  {
    private String name;
    private String db;
    
    private List<IndexDTO> indices = new ArrayList<IndexDTO>();
    private Map<String,FieldOptions> fields = new HashMap<String, FieldOptions>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public List<IndexDTO> getIndices() {
        return indices;
    }

    public void setIndices(List<IndexDTO> indices) {
        this.indices = indices;
    }

    public Map<String, FieldOptions> getFields() {
        return fields;
    }

    public void setFields(Map<String, FieldOptions> fields) {
        this.fields = fields;
    }

    public static class FieldOptions {
        private FieldType type;
        private int length;
        private boolean notNull;

        public FieldOptions() {
            this(FieldType.UNKNOWN);
        }
        
        public FieldOptions(FieldType type) {
            this(type, false);
        }
        
        public FieldOptions(FieldType type, boolean notNull) {
            this(type, -1, notNull);
        }
        
        public FieldOptions(FieldType type, int length, boolean notNull) {
            this.type = type;
            this.length = length;
            this.notNull = notNull;
        }
        

        public FieldType getType() {
            return type;
        }

        public void setType(FieldType type) {
            this.type = type;
        }

        public boolean isNotNull() {
            return notNull;
        }

        public void setNotNull(boolean notNull) {
            this.notNull = notNull;
        }
    }
    
    public static enum FieldType {
        CHAR,
        VARCHAR,
        BLOB,
        TEXT,
        FILE,
        INT,
        BIGINT,
        FLOAT,
        DOUBLE,
        BOOL,
        OBJECT,
        ARRAY,
        UNKNOWN
    }
}
