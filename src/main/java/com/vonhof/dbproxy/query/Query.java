package com.vonhof.dbproxy.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Henrik Hofmeister <@vonhofdk>
 */
public final class Query {

    public static Field field(String fieldB) {
        return new Field(fieldB);
    }
    
    private Field[] fields;
    private Table table;
    private String[] groupBy;
    private List<OrderBy> orderBy = new ArrayList<OrderBy>();
    
    private List<JoinStatement> joins = new ArrayList<JoinStatement>();
    private List<WhereStatement> where = new ArrayList<WhereStatement>();
    
    private int limit = -1;
    private int offset= -1;
    
    
    public static Query select(String ... fields) {
        return new Query(fields);
    }

    private Query(String[] fields) {
        
        this.fields = new Field[fields.length];
        for(int i = 0; i < fields.length;i++) {
            this.fields[i] = new Field(fields[i]);
        }
    }
    
    public Query from(String table) {
        this.table = new Table(table);
        return this;
    }
    
    public Query from(String table,String alias) {
        this.table = new Table(table,alias);
        return this;
    }
    
    public JoinStatement joinLeft(String table) {
        return joinLeft(table, null);
    }
    
    public JoinStatement joinLeft(String table,String alias) {
        JoinStatement stmt = new JoinStatement(this,new Table(table, alias), JoinType.LEFT);
        joins.add(stmt);
        return stmt;
    }
    
    public JoinStatement joinRight(String table) {
        return joinRight(table, null);
    }
    
    public JoinStatement joinRight(String table,String alias) {
        JoinStatement stmt = new JoinStatement(this,new Table(table, alias), JoinType.RIGHT);
        joins.add(stmt);
        return stmt;
    }
    
    public WhereStatement<WhereStatement<WhereStatement,Query>,Query> where() {
        WhereStatement stmt = new WhereStatement<WhereStatement,Query>(this);
        this.where.add(stmt);
        return stmt;
    }
    
    public Query limit(int limit) {
        this.limit = limit;
        return this;
    }
    
    public Query offset(int offset) {
        this.offset = offset;
        return this;
    }
    
    public Query orderBy(String field) {
        return orderBy(field, true);
    }
    
    public Query orderBy(String field,boolean asc) {
        orderBy.add(new OrderBy(field, asc));
        return this;
    }
    
    
    public Query groupBy(String ... fields) {
        this.groupBy = fields;
        return this;
    }
    
    
    public static final class Field {
        private final String name;
        private final String alias;
        
        private Field(String name) {
            this(name, null);
        }
        
        private Field(String name,String alias) {
            this.name = name;
            this.alias = alias;
        }

        public String getName() {
            return name;
        }

        public String getAlias() {
            return alias;
        }
    }
    
    
    public static final class Table {
        private final String name;
        private final String alias;
        
        private Table(String name) {
            this(name, null);
        }
        
        private Table(String name,String alias) {
            this.name = name;
            this.alias = alias;
        }

        public String getName() {
            return name;
        }

        public String getAlias() {
            return alias;
        }
    }
    
    public class Phrase {
        private final PhraseType type;
        private final Object value;

        public Phrase(PhraseType type, Object value) {
            this.type = type;
            this.value = value;
        }
        
        public PhraseType getType() {
            return type;
        }

        public Object getValue() {
            return value;
        }
        
    }
    
    public class InnerPhrase extends Phrase {
        private final WhereType whereType;

        public InnerPhrase(WhereType whereType, PhraseType type, Object value) {
            super(type, value);
            this.whereType = whereType;
        }
    }
    
    public class FieldPhrase extends Phrase {
        private final String field;

        public FieldPhrase(String field, PhraseType type, Object value) {
            super(type, value);
            this.field = field;
        }

        public String getField() {
            return field;
        }
    }
    
    public class OrderBy {
        private String field;
        private boolean ascending;

        public OrderBy(String field) {
            this(field, true);
        }
                
        public OrderBy(String field, boolean ascending) {
            this.field = field;
            this.ascending = ascending;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public boolean isAscending() {
            return ascending;
        }

        public void setAscending(boolean ascending) {
            this.ascending = ascending;
        }
    }
    
    public class WhereStatement<T extends WhereStatement,P> {
        private P parent;

        public WhereStatement(P parent) {
            this.parent = parent;
        }
        
        private List<Phrase> phrases = new LinkedList<Phrase>();
        
        public P end() {
            return parent;
        }
        
        public WhereStatement<T,P> eq(String field,Object value) {
            return phrase(field, PhraseType.EQ, value);
        }

        public WhereStatement<T,P> neq(String field,Object value) {
            return phrase(field, PhraseType.NEQ, value);
        }

        public WhereStatement<T,P> between(String field,Object from,Object to) {
            return phrase(field, PhraseType.BETWEEN, new Object[]{from,to});
        }

        public T gt(String field,Object value) {
            return phrase(field, PhraseType.GT, value);
        }

        public T gtEq(String field,Object value) {
            return phrase(field, PhraseType.EQ, value);
        }

        public T lt(String field,Object value) {
            return phrase(field, PhraseType.EQ, value);
        }

        public T ltEq(String field,Object value) {
            return phrase(field, PhraseType.EQ, value);
        }

        public T in(String field,Object ... values) {
            return phrase(field, PhraseType.IN, values);
        }
        
        public T exists(Query query) {
            return phrase(PhraseType.EXISTS, query);
        }
        
        public T notExists(Query query) {
            return phrase(PhraseType.NOT_EXISTS, query);
        }
        
        public WhereStatement<WhereStatement<T,P>,WhereStatement<T,P>> and() {
            WhereStatement out = new WhereStatement(this);
            phrases.add(new InnerPhrase(WhereType.AND, PhraseType.INNER, out));
            return out;
        }
        
        public WhereStatement<WhereStatement<T,P>,WhereStatement<T,P>> or() {
            WhereStatement out = new WhereStatement(this);
            phrases.add(new InnerPhrase(WhereType.OR, PhraseType.INNER, out));
            return out;
        }

        private T phrase(PhraseType type,Object value) {
            phrases.add(new Phrase(type, value));
            return (T) this;
        }
        
        private T phrase(String field,PhraseType type,Object value) {
            phrases.add(new FieldPhrase(field, type, value));
            return (T) this;
        }
    }
    
    public class JoinStatement extends WhereStatement<JoinStatement,Query> {
        private Table table;
        private JoinType type;

        public JoinStatement(Query parent,Table table, JoinType type) {
            super(parent);
            this.table = table;
            this.type = type;
        }
    }
    
    public static enum WhereType {
        AND,OR
    }
    
    public static enum JoinType {
        LEFT,RIGHT,INNER,OUTER
    }
    
    public static enum PhraseType {
        EQ,NEQ,GT,GTEQ,LT,LTEQ,BETWEEN,IN,EXISTS,NOT_EXISTS,INNER
    }
}
