package org.embulk.parser.jsonl.getter;

import org.embulk.parser.jsonl.JsonRecordValidateException;
import org.embulk.spi.Column;
import org.embulk.spi.ColumnVisitor;
import org.embulk.spi.PageBuilder;
import org.embulk.spi.time.TimestampParser;
import org.msgpack.core.MessageTypeException;
import org.msgpack.value.BooleanValue;
import org.msgpack.value.FloatValue;
import org.msgpack.value.IntegerValue;
import org.msgpack.value.Value;

public class CommonColumnGetter implements ColumnVisitor {
    protected final PageBuilder pageBuilder;
    protected final TimestampParser[] timestampParsers;

    protected Value value;

    public CommonColumnGetter(PageBuilder pageBuilder, TimestampParser[] timestampParsers)
    {
        this.pageBuilder = pageBuilder;
        this.timestampParsers = timestampParsers;
    }

    public void setValue(Value value)
    {
        this.value = value;
    }

    @Override
    public void booleanColumn(Column column)
    {
        if (isNil(value)) {
            pageBuilder.setNull(column);
        }
        else {
            try {
                pageBuilder.setBoolean(column, ((BooleanValue) value).getBoolean());
            }
            catch (MessageTypeException e) {
                throw new JsonRecordValidateException(e);
            }
        }
    }

    @Override
    public void longColumn(Column column)
    {
        if (isNil(value)) {
            pageBuilder.setNull(column);
        }
        else {
            try {
                pageBuilder.setLong(column, ((IntegerValue) value).asLong());
            }
            catch (MessageTypeException e) {
                throw new JsonRecordValidateException(e);
            }
        }
    }

    @Override
    public void doubleColumn(Column column)
    {
        if (isNil(value)) {
            pageBuilder.setNull(column);
        }
        else {
            try {
                pageBuilder.setDouble(column, ((FloatValue) value).toDouble());
            }
            catch (MessageTypeException e) {
                throw new JsonRecordValidateException(e);
            }
        }
    }

    @Override
    public void stringColumn(Column column)
    {
        if (isNil(value)) {
            pageBuilder.setNull(column);
        }
        else {
            try {
                pageBuilder.setString(column, value.toString());
            }
            catch (MessageTypeException e) {
                throw new JsonRecordValidateException(e);
            }
        }
    }

    @Override
    public void timestampColumn(Column column)
    {
        if (isNil(value)) {
            pageBuilder.setNull(column);
        }
        else {
            try {
                pageBuilder.setTimestamp(column, timestampParsers[column.getIndex()].parse(value.toString()));
            }
            catch (MessageTypeException e) {
                throw new JsonRecordValidateException(e);
            }
        }
    }

    @Override
    public void jsonColumn(Column column)
    {
        if (isNil(value)) {
            pageBuilder.setNull(column);
        }
        else {
            try {
                pageBuilder.setJson(column, value);
            }
            catch (MessageTypeException e) {
                throw new JsonRecordValidateException(e);
            }
        }
    }

    protected boolean isNil(Value v)
    {
        return v == null || v.isNilValue();
    }
}
