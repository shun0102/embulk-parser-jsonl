package org.embulk.parser.jsonl.cast;

import com.google.common.collect.ImmutableSet;
import org.embulk.spi.DataException;
import org.embulk.spi.time.Timestamp;
import org.embulk.spi.time.TimestampParseException;
import org.embulk.spi.time.TimestampParser;

public class StringCast
{
    // copy from csv plugin
    private static final ImmutableSet<String> TRUE_STRINGS =
            ImmutableSet.of(
                    "true", "True", "TRUE",
                    "yes", "Yes", "YES",
                    "t", "T", "y", "Y",
                    "on", "On", "ON",
                    "1");

    private static final ImmutableSet<String> FALSE_STRINGS =
            ImmutableSet.of(
                    "false", "False", "FALSE",
                    "no", "No", "NO",
                    "f", "F", "n", "N",
                    "off", "Off", "OFF",
                    "0");

    private StringCast() {}

    private static String buildErrorMessage(String as, String value)
    {
        return String.format("cannot cast String to %s: \"%s\"", as, value);
    }

    public static boolean asBoolean(String value) throws DataException
    {
        if (TRUE_STRINGS.contains(value)) {
            return true;
        }
        else if (FALSE_STRINGS.contains(value)) {
            return false;
        }
        else {
            throw new DataException(buildErrorMessage("boolean", value));
        }
    }

    public static long asLong(String value) throws DataException
    {
        try {
            return Long.parseLong(value);
        }
        catch (NumberFormatException ex) {
            throw new DataException(buildErrorMessage("long", value), ex);
        }
    }

    public static double asDouble(String value) throws DataException
    {
        try {
            return Double.parseDouble(value);
        }
        catch (NumberFormatException ex) {
            throw new DataException(buildErrorMessage("double", value), ex);
        }
    }

    public static String asString(String value) throws DataException
    {
        return value;
    }

    public static Timestamp asTimestamp(String value, TimestampParser parser) throws DataException
    {
        try {
            return parser.parse(value);
        }
        catch (TimestampParseException ex) {
            throw new DataException(buildErrorMessage("timestamp", value), ex);
        }
    }
}
