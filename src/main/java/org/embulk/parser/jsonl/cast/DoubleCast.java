package org.embulk.parser.jsonl.cast;

import org.embulk.spi.DataException;
import org.embulk.spi.time.Timestamp;

public class DoubleCast
{
    private DoubleCast() {}

    private static String buildErrorMessage(String as, double value)
    {
        return String.format("cannot cast double to %s: \"%s\"", as, value);
    }

    public static boolean asBoolean(double value) throws DataException
    {
        throw new DataException(buildErrorMessage("boolean", value));
    }

    public static long asLong(double value) throws DataException
    {
        return (long) value;
    }

    public static double asDouble(double value) throws DataException
    {
        return value;
    }

    public static String asString(double value) throws DataException
    {
        return String.valueOf(value);
    }

    public static Timestamp asTimestamp(double value) throws DataException
    {
        long epochSecond = (long) value;
        long nanoAdjustMent = (long) ((value - epochSecond) * 1000000000);
        return Timestamp.ofEpochSecond(epochSecond, nanoAdjustMent);
    }
}
