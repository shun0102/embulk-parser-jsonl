package org.embulk.parser.jsonl;

import org.embulk.parser.jsonl.cast.BooleanCast;
import org.embulk.parser.jsonl.cast.DoubleCast;
import org.embulk.parser.jsonl.cast.JsonCast;
import org.embulk.parser.jsonl.cast.LongCast;
import org.embulk.parser.jsonl.cast.StringCast;
import org.embulk.spi.DataException;
import org.embulk.spi.time.Timestamp;
import org.embulk.spi.time.TimestampParser;
import org.msgpack.value.Value;

class ColumnCaster
{
    ColumnCaster() {}

    public static boolean asBoolean(Value value) throws DataException
    {
        if (value.isBooleanValue()) {
            return value.asBooleanValue().getBoolean();
        }
        else if (value.isIntegerValue()) {
            return LongCast.asBoolean(value.asIntegerValue().asLong());
        }
        else if (value.isFloatValue()) {
            return DoubleCast.asBoolean(value.asFloatValue().toDouble());
        }
        else if (value.isStringValue()) {
            return StringCast.asBoolean(value.asStringValue().asString());
        }
        else {
            return JsonCast.asBoolean(value);
        }
    }

    public static long asLong(Value value) throws DataException
    {
        if (value.isBooleanValue()) {
            return BooleanCast.asLong(value.asBooleanValue().getBoolean());
        }
        else if (value.isIntegerValue()) {
            return value.asIntegerValue().asLong();
        }
        else if (value.isFloatValue()) {
            return DoubleCast.asLong(value.asFloatValue().toDouble());
        }
        else if (value.isStringValue()) {
            return StringCast.asLong(value.asStringValue().asString());
        }
        else {
            return JsonCast.asLong(value);
        }
    }

    public static double asDouble(Value value) throws DataException
    {
        if (value.isBooleanValue()) {
            return BooleanCast.asDouble(value.asBooleanValue().getBoolean());
        }
        else if (value.isIntegerValue()) {
            return LongCast.asDouble(value.asIntegerValue().asLong());
        }
        else if (value.isFloatValue()) {
            return value.asFloatValue().toDouble();
        }
        else if (value.isStringValue()) {
            return StringCast.asDouble(value.asStringValue().asString());
        }
        else {
            return JsonCast.asDouble(value);
        }
    }

    public static String asString(Value value) throws DataException
    {
        return value.toString();
    }

    public static Timestamp asTimestamp(Value value, TimestampParser parser) throws DataException
    {
        if (value.isBooleanValue()) {
            return BooleanCast.asTimestamp(value.asBooleanValue().getBoolean());
        }
        else if (value.isIntegerValue()) {
            return LongCast.asTimestamp(value.asIntegerValue().asLong());
        }
        else if (value.isFloatValue()) {
            return DoubleCast.asTimestamp(value.asFloatValue().toDouble());
        }
        else if (value.isStringValue()) {
            return StringCast.asTimestamp(value.asStringValue().asString(), parser);
        }
        else {
            return JsonCast.asTimestamp(value);
        }
    }
}
