package org.embulk.parser.jsonl.cast;

import org.embulk.EmbulkTestRuntime;
import org.embulk.spi.DataException;
import org.embulk.spi.time.Timestamp;
import org.embulk.spi.time.TimestampParser;
import org.joda.time.DateTimeZone;
import org.jruby.embed.ScriptingContainer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TestStringCast
{
    @Rule
    public EmbulkTestRuntime runtime = new EmbulkTestRuntime();
    public ScriptingContainer jruby;

    @Before
    public void createResource()
    {
        jruby = new ScriptingContainer();
    }

    @Test
    public void asBoolean()
    {
        for (String str : StringCast.TRUE_STRINGS) {
            assertEquals(true, StringCast.asBoolean(str));
        }
        for (String str : StringCast.FALSE_STRINGS) {
            assertEquals(false, StringCast.asBoolean(str));
        }
        try {
            StringCast.asBoolean("foo");
        }
        catch (Throwable t) {
            assertTrue(t instanceof DataException);
        }
    }

    @Test
    public void asLong()
    {
        assertEquals(1, StringCast.asLong("1"));
        try {
            StringCast.asLong("1.5");
            fail();
        }
        catch (Throwable t) {
            assertTrue(t instanceof DataException);
        }
        try {
            StringCast.asLong("foo");
            fail();
        }
        catch (Throwable t) {
            assertTrue(t instanceof DataException);
        }
    }

    @Test
    public void asDouble()
    {
        assertEquals(1.0, StringCast.asDouble("1"), 0.0);
        assertEquals(1.5, StringCast.asDouble("1.5"), 0.0);
        try {
            StringCast.asDouble("foo");
            fail();
        }
        catch (Throwable t) {
            assertTrue(t instanceof DataException);
        }
    }

    @Test
    public void asString()
    {
        assertEquals("1", StringCast.asString("1"));
        assertEquals("1.5", StringCast.asString("1.5"));
        assertEquals("foo", StringCast.asString("foo"));
    }

    @Test
    public void asTimestamp()
    {
        Timestamp expected = Timestamp.ofEpochSecond(1463084053, 123456000);
        TimestampParser parser = new TimestampParser(jruby, "%Y-%m-%d %H:%M:%S.%N", DateTimeZone.UTC);
        assertEquals(expected, StringCast.asTimestamp("2016-05-12 20:14:13.123456", parser));

        try {
            StringCast.asTimestamp("foo", parser);
            fail();
        }
        catch (Throwable t) {
            assertTrue(t instanceof DataException);
        }
    }
}
