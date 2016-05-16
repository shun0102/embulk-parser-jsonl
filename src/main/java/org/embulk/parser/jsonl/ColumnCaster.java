package org.embulk.parser.jsonl;

import org.embulk.parser.jsonl.cast.BooleanCast;
import org.embulk.parser.jsonl.cast.DoubleCast;
import org.embulk.parser.jsonl.cast.JsonCast;
import org.embulk.parser.jsonl.cast.LongCast;
import org.embulk.parser.jsonl.cast.StringCast;
import org.embulk.spi.time.Timestamp;
import org.embulk.spi.time.TimestampParser;
import org.msgpack.value.Value;

class ColumnCaster
{
    ColumnCaster() {}

    public boolean asBoolean(Value value)
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

    public long asLong(Value value)
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

    public double asDouble(Value value)
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

    public String asString(Value value)
    {
        return value.toString();
    }

    public Timestamp asTimestamp(Value value, TimestampParser parser)
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
