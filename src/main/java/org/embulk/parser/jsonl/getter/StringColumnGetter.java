package org.embulk.parser.jsonl.getter;

import org.embulk.parser.jsonl.JsonRecordValidateException;
import org.embulk.spi.Column;
import org.embulk.spi.PageBuilder;
import org.embulk.spi.time.TimestampParser;
import org.msgpack.core.MessageTypeException;

public class StringColumnGetter extends CommonColumnGetter {

    public StringColumnGetter(PageBuilder pageBuilder, TimestampParser[] timestampParsers)
    {
        super(pageBuilder, timestampParsers);
    }

    private String getValueAsString()
    {
        return value.toString();
    }

    @Override
    public void booleanColumn(Column column)
    {
        if (isNil(value)) {
            pageBuilder.setNull(column);
        }
        else {
            try {
                pageBuilder.setBoolean(column, Boolean.valueOf(getValueAsString()));
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
                pageBuilder.setLong(column, Long.valueOf(getValueAsString()));
            }
            catch (MessageTypeException | NumberFormatException e) {
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
                pageBuilder.setDouble(column, Double.valueOf(getValueAsString()));
            }
            catch (MessageTypeException | NumberFormatException e) {
                throw new JsonRecordValidateException(e);
            }
        }
    }
}
