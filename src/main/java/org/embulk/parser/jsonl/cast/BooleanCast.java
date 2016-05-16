package org.embulk.parser.jsonl.cast;

import org.embulk.spi.DataException;
import org.embulk.spi.time.Timestamp;

public class BooleanCast
{
    private BooleanCast() {}

    private static String buildErrorMessage(String as, boolean value)
    {
        return String.format("cannot cast boolean to %s: \"%s\"", as, value);
    }

    public static boolean asBoolean(boolean value) throws DataException
    {
        return value;
    }

    public static long asLong(boolean value) throws DataException
    {
        return value ? 1 : 0;
    }

    public static double asDouble(boolean value) throws DataException
    {
        throw new DataException(buildErrorMessage("double", value));
    }

    public static String asString(boolean value) throws DataException
    {
        return value ? "true" : "false";
    }

    public static Timestamp asTimestamp(boolean value) throws DataException
    {
        throw new DataException(buildErrorMessage("timestamp", value));
    }
}
