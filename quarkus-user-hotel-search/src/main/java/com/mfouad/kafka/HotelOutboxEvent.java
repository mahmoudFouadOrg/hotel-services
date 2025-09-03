package com.mfouad.kafka;

import java.util.List;
import java.util.Map;

import io.vertx.core.json.JsonObject;

public class HotelOutboxEvent {

    private Payload payload;
    private Schema schema;

    // Getters and Setters
    public Payload getPayload() { return payload; }
    public void setPayload(Payload payload) { this.payload = payload; }
    public Schema getSchema() { return schema; }
    public void setSchema(Schema schema) { this.schema = schema; }

    // Nested classes for the Debezium CDC structure
    public static class Payload {
        private Record before;
        private Record after;
        private Source source;
        private Transaction transaction;
        private String op;  // "c"=create, "u"=update, "d"=delete
        private Long ts_ms;
        private Long ts_us;
        private Long ts_ns;

        // Getters and Setters
        public Record getBefore() { return before; }
        public void setBefore(Record before) { this.before = before; }
        public Record getAfter() { return after; }
        public void setAfter(Record after) { this.after = after; }
        public Source getSource() { return source; }
        public void setSource(Source source) { this.source = source; }
        public Transaction getTransaction() { return transaction; }
        public void setTransaction(Transaction transaction) { this.transaction = transaction; }
        public String getOp() { return op; }
        public void setOp(String op) { this.op = op; }
        public Long getTs_ms() { return ts_ms; }
        public void setTs_ms(Long ts_ms) { this.ts_ms = ts_ms; }
        public Long getTs_us() { return ts_us; }
        public void setTs_us(Long ts_us) { this.ts_us = ts_us; }
        public Long getTs_ns() { return ts_ns; }
        public void setTs_ns(Long ts_ns) { this.ts_ns = ts_ns; }
    }


    public static class Source {
        private String version;
        private String connector;
        private String name;
        private Long ts_ms;
        private String snapshot;
        private String db;
        private String sequence;
        private Long ts_us;
        private Long ts_ns;
        private String schema;
        private String table;
        private Long txId;
        private Long lsn;
        private Long xmin;

        // Getters and Setters
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public String getConnector() { return connector; }
        public void setConnector(String connector) { this.connector = connector; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Long getTs_ms() { return ts_ms; }
        public void setTs_ms(Long ts_ms) { this.ts_ms = ts_ms; }
        public String getSnapshot() { return snapshot; }
        public void setSnapshot(String snapshot) { this.snapshot = snapshot; }
        public String getDb() { return db; }
        public void setDb(String db) { this.db = db; }
        public String getSequence() { return sequence; }
        public void setSequence(String sequence) { this.sequence = sequence; }
        public Long getTs_us() { return ts_us; }
        public void setTs_us(Long ts_us) { this.ts_us = ts_us; }
        public Long getTs_ns() { return ts_ns; }
        public void setTs_ns(Long ts_ns) { this.ts_ns = ts_ns; }
        public String getSchema() { return schema; }
        public void setSchema(String schema) { this.schema = schema; }
        public String getTable() { return table; }
        public void setTable(String table) { this.table = table; }
        public Long getTxId() { return txId; }
        public void setTxId(Long txId) { this.txId = txId; }
        public Long getLsn() { return lsn; }
        public void setLsn(Long lsn) { this.lsn = lsn; }
        public Long getXmin() { return xmin; }
        public void setXmin(Long xmin) { this.xmin = xmin; }
    }

    public static class Transaction {
        private String id;
        private Long total_order;
        private Long data_collection_order;

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public Long getTotal_order() { return total_order; }
        public void setTotal_order(Long total_order) { this.total_order = total_order; }
        public Long getData_collection_order() { return data_collection_order; }
        public void setData_collection_order(Long data_collection_order) { this.data_collection_order = data_collection_order; }
    }

    public static class Schema {
        private String type;
        private List<Field> fields;
        private boolean optional;
        private String name;
        private int version;

        // Getters and Setters
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public List<Field> getFields() { return fields; }
        public void setFields(List<Field> fields) { this.fields = fields; }
        public boolean isOptional() { return optional; }
        public void setOptional(boolean optional) { this.optional = optional; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getVersion() { return version; }
        public void setVersion(int version) { this.version = version; }
    }

    public static class Field {
        private String type;
        private boolean optional;
        private String field;
        private String name;
        private Map<String, String> parameters;
        private String defaultValue;

        // Getters and Setters
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public boolean isOptional() { return optional; }
        public void setOptional(boolean optional) { this.optional = optional; }
        public String getField() { return field; }
        public void setField(String field) { this.field = field; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Map<String, String> getParameters() { return parameters; }
        public void setParameters(Map<String, String> parameters) { this.parameters = parameters; }
        public String getDefaultValue() { return defaultValue; }
        public void setDefaultValue(String defaultValue) { this.defaultValue = defaultValue; }
    }
}
