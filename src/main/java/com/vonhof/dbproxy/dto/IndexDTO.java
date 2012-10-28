package com.vonhof.dbproxy.dto;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Henrik Hofmeister <@vonhofdk>
 */
public class IndexDTO {
    private String name;
    private boolean unique;
    
    private Map<String,FieldIndexOptions> fields = new HashMap<String, FieldIndexOptions>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }
    
    public Map<String, FieldIndexOptions> getFields() {
        return fields;
    }

    public void setFields(Map<String, FieldIndexOptions> fields) {
        this.fields = fields;
    }
    
    public void addField(String field,FieldIndexOptions options) {
        fields.put(field, options);
    }

    public static class FieldIndexOptions {
        private boolean ascending;
        

        public FieldIndexOptions(boolean ascending) {
            this.ascending = ascending;
        }
        

        public FieldIndexOptions() {
            this(true);
        }
        

        public boolean isAscending() {
            return ascending;
        }

        public void setAscending(boolean ascending) {
            this.ascending = ascending;
        }
    }
}
