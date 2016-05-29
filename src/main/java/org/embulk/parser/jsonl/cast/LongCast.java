package org.embulk.parser.jsonl.cast;

import org.embulk.spi.DataException;
import org.embulk.spi.time.Timestamp;

public class LongCast
{
    private LongCast() {}

    private static String buildErrorMessage(String as, long value)
    {
        return String.format("cannot cast long to %s: \"%s\"", as, value);
    }

    public static boolean asBoolean(long value) throws DataException
    {
        if (value == 1) {
            return true;
        }
        else if (value == 0) {
            return false;
        }
        else {
            throw new DataException(buildErrorMessage("boolean", value));
        }
    }

    public static long asLong(long value) throws DataException
    {
        return value;
    }

    public static double asDouble(long value) throws DataException
    {
        return (double) value;
    }

    public static String asString(long value) throws DataException
    {
        return String.valueOf(value);
    }

    public static Timestamp asTimestamp(long value) throws DataException
    {
        return Timestamp.ofEpochSecond(value);
    }
}
