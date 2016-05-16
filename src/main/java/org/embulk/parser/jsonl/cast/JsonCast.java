package org.embulk.parser.jsonl.cast;

import org.embulk.spi.DataException;
import org.embulk.spi.time.Timestamp;
import org.msgpack.value.Value;

public class JsonCast
{
    private JsonCast() {}

    private static String buildErrorMessage(String as, Value value)
    {
        return String.format("cannot cast Json to %s: \"%s\"", as, value);
    }

    public static boolean asBoolean(Value value) throws DataException
    {
        throw new DataException(buildErrorMessage("boolean", value));
    }

    public static long asLong(Value value) throws DataException
    {
        throw new DataException(buildErrorMessage("long", value));
    }

    public static double asDouble(Value value) throws DataException
    {
        throw new DataException(buildErrorMessage("double", value));
    }

    public static String asString(Value value) throws DataException
    {
        return value.toString();
    }

    public static Timestamp asTimestamp(Value value) throws DataException
    {
        throw new DataException(buildErrorMessage("timestamp", value));
    }
}
