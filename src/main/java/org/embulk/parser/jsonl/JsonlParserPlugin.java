package org.embulk.parser.jsonl;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.embulk.config.Config;
import org.embulk.config.ConfigDefault;
import org.embulk.config.ConfigException;
import org.embulk.config.ConfigSource;
import org.embulk.config.Task;
import org.embulk.config.TaskSource;
import org.embulk.spi.Column;
import org.embulk.spi.ColumnVisitor;
import org.embulk.spi.DataException;
import org.embulk.spi.Exec;
import org.embulk.spi.FileInput;
import org.embulk.spi.PageBuilder;
import org.embulk.spi.PageOutput;
import org.embulk.spi.ParserPlugin;
import org.embulk.spi.Schema;
import org.embulk.spi.SchemaConfig;
import org.embulk.spi.json.JsonParser;
import org.embulk.spi.time.TimestampParser;
import org.embulk.spi.util.LineDecoder;
import org.embulk.spi.util.Timestamps;
import org.msgpack.core.MessageTypeException;
import org.msgpack.value.BooleanValue;
import org.msgpack.value.FloatValue;
import org.msgpack.value.IntegerValue;
import org.msgpack.value.Value;
import org.slf4j.Logger;

import java.util.Map;

import static org.msgpack.value.ValueFactory.newString;

public class JsonlParserPlugin
        implements ParserPlugin
{
    public interface PluginTask
            extends Task, LineDecoder.DecoderTask, TimestampParser.Task
    {
        @Config("columns")
        @ConfigDefault("null")
        Optional<SchemaConfig> getSchemaConfig();

        @Config("schema")
        @ConfigDefault("null")
        @Deprecated
        Optional<SchemaConfig> getOldSchemaConfig();

        @Config("stop_on_invalid_record")
        @ConfigDefault("false")
        boolean getStopOnInvalidRecord();
    }

    private final Logger log;

    private String line = null;
    private long lineNumber = 0;
    private Map<String, Value> columnNameValues;

    public JsonlParserPlugin()
    {
        this.log = Exec.getLogger(JsonlParserPlugin.class);
    }

    @Override
    public void transaction(ConfigSource configSource, Control control)
    {
        PluginTask task = configSource.loadConfig(PluginTask.class);
        control.run(task.dump(), getSchemaConfig(task).toSchema());
    }

    // this method is to keep the backward compatibility of 'schema' option.
    private SchemaConfig getSchemaConfig(PluginTask task)
    {
        if (task.getOldSchemaConfig().isPresent()) {
            log.warn("Please use 'columns' option instead of 'schema' because the 'schema' option is deprecated. The next version will stop 'schema' option support.");
        }

        if (task.getSchemaConfig().isPresent()) {
            return task.getSchemaConfig().get();
        }
        else if (task.getOldSchemaConfig().isPresent()) {
            return task.getOldSchemaConfig().get();
        }
        else {
            throw new ConfigException("Attribute 'columns' is required but not set");
        }
    }

    @Override
    public void run(TaskSource taskSource, Schema schema, FileInput input, PageOutput output)
    {
        PluginTask task = taskSource.loadTask(PluginTask.class);

        setColumnNameValues(schema);

        final TimestampParser[] timestampParsers = Timestamps.newTimestampColumnParsers(task, getSchemaConfig(task));
        final LineDecoder decoder = newLineDecoder(input, task);
        final JsonParser jsonParser = newJsonParser();
        final boolean stopOnInvalidRecord = task.getStopOnInvalidRecord();

        try (final PageBuilder pageBuilder = new PageBuilder(Exec.getBufferAllocator(), schema, output)) {
            while (decoder.nextFile()) { // TODO this implementation should be improved with new JsonParser API on Embulk v0.8.3
                lineNumber = 0;

                while ((line = decoder.poll()) != null) {
                    lineNumber++;

                    try {
                        Value value = jsonParser.parse(line);

                        if (!value.isMapValue()) {
                            throw new JsonRecordValidateException("Json string is not representing map value.");
                        }

                        final Map<Value, Value> record = value.asMapValue().map();

                        schema.visitColumns(new ColumnVisitor() {
                            @Override
                            public void booleanColumn(Column column)
                            {
                                Value v = record.get(getColumnNameValue(column));
                                if (isNil(v)) {
                                    pageBuilder.setNull(column);
                                }
                                else {
                                    try {
                                        pageBuilder.setBoolean(column, ((BooleanValue) v).getBoolean());
                                    }
                                    catch (MessageTypeException e) {
                                        throw new JsonRecordValidateException(e);
                                    }
                                }
                            }

                            @Override
                            public void longColumn(Column column)
                            {
                                Value v = record.get(getColumnNameValue(column));
                                if (isNil(v)) {
                                    pageBuilder.setNull(column);
                                }
                                else {
                                    try {
                                        pageBuilder.setLong(column, ((IntegerValue) v).asLong());
                                    }
                                    catch (MessageTypeException e) {
                                        throw new JsonRecordValidateException(e);
                                    }
                                }
                            }

                            @Override
                            public void doubleColumn(Column column)
                            {
                                Value v = record.get(getColumnNameValue(column));
                                if (isNil(v)) {
                                    pageBuilder.setNull(column);
                                }
                                else {
                                    try {
                                        pageBuilder.setDouble(column, ((FloatValue) v).toDouble());
                                    }
                                    catch (MessageTypeException e) {
                                        throw new JsonRecordValidateException(e);
                                    }
                                }
                            }

                            @Override
                            public void stringColumn(Column column)
                            {
                                Value v = record.get(getColumnNameValue(column));
                                if (isNil(v)) {
                                    pageBuilder.setNull(column);
                                }
                                else {
                                    try {
                                        pageBuilder.setString(column, v.toString());
                                    }
                                    catch (MessageTypeException e) {
                                        throw new JsonRecordValidateException(e);
                                    }
                                }
                            }

                            @Override
                            public void timestampColumn(Column column)
                            {
                                Value v = record.get(getColumnNameValue(column));
                                if (isNil(v)) {
                                    pageBuilder.setNull(column);
                                }
                                else {
                                    try {
                                        pageBuilder.setTimestamp(column, timestampParsers[column.getIndex()].parse(v.toString()));
                                    }
                                    catch (MessageTypeException e) {
                                        throw new JsonRecordValidateException(e);
                                    }
                                }
                            }

                            @Override
                            public void jsonColumn(Column column)
                            {
                                Value v = record.get(getColumnNameValue(column));
                                if (isNil(v)) {
                                    pageBuilder.setNull(column);
                                }
                                else {
                                    try {
                                        pageBuilder.setJson(column, v);
                                    }
                                    catch (MessageTypeException e) {
                                        throw new JsonRecordValidateException(e);
                                    }
                                }
                            }

                            private boolean isNil(Value v)
                            {
                                return v == null || v.isNilValue();
                            }
                        });

                        pageBuilder.addRecord();
                    }
                    catch (JsonRecordValidateException e) {
                        if (stopOnInvalidRecord) {
                            throw new DataException(String.format("Invalid record at line %d: %s", lineNumber, line), e);
                        }
                        log.warn(String.format("Skipped line %d (%s): %s", lineNumber, e.getMessage(), line));
                    }
                }
            }

            pageBuilder.finish();
        }
    }

    private void setColumnNameValues(Schema schema)
    {
        ImmutableMap.Builder<String, Value> builder = ImmutableMap.builder();
        for (Column column : schema.getColumns()) {
            String name = column.getName();
            builder.put(name, newString(name));
        }
        columnNameValues = builder.build();
    }

    private Value getColumnNameValue(Column column)
    {
        return columnNameValues.get(column.getName());
    }

    public LineDecoder newLineDecoder(FileInput input, PluginTask task)
    {
        return new LineDecoder(input, task);
    }

    public JsonParser newJsonParser()
    {
        return new JsonParser();
    }

    static class JsonRecordValidateException
            extends DataException
    {
        JsonRecordValidateException(String message)
        {
            super(message);
        }

        JsonRecordValidateException(Throwable cause)
        {
            super(cause);
        }
    }
}
