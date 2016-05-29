package org.embulk.parser.jsonl;

import org.embulk.EmbulkTestRuntime;
import org.embulk.spi.DataException;
import org.embulk.spi.time.Timestamp;
import org.embulk.spi.time.TimestampParser;
import org.joda.time.DateTimeZone;
import org.jruby.embed.ScriptingContainer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.msgpack.value.MapValue;
import org.msgpack.value.Value;
import org.msgpack.value.ValueFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TestColumnCaster
{
    @Rule
    public EmbulkTestRuntime runtime = new EmbulkTestRuntime();
    public MapValue mapValue;
    public DataException thrown;
    public ScriptingContainer jruby;
    public TimestampParser parser;

    @Before
    public void createResource()
    {
        jruby = new ScriptingContainer();
        thrown = new DataException("any");
        Value[] kvs = new Value[2];
        kvs[0] = ValueFactory.newString("k");
        kvs[1] = ValueFactory.newString("v");
        mapValue = ValueFactory.newMap(kvs);
        parser = new TimestampParser(jruby, "%Y-%m-%d %H:%M:%S.%N", DateTimeZone.UTC);
    }

    @Test
    public void asBooleanFromBoolean()
    {
        assertEquals(true, ColumnCaster.asBoolean(ValueFactory.newBoolean(true)));
    }

    @Test
    public void asBooleanFromInteger()
    {
        assertEquals(true, ColumnCaster.asBoolean(ValueFactory.newInteger(1)));
        try {
            ColumnCaster.asBoolean(ValueFactory.newInteger(2));
            fail();
        }
        catch (Throwable t) {
            assertTrue(t instanceof DataException);
        }
    }

    @Test
    public void asBooleanFromFloat()
    {
        try {
            ColumnCaster.asBoolean(ValueFactory.newFloat(1.1));
            fail();
        }
        catch (Throwable t) {
            assertTrue(t instanceof DataException);
        }
    }

    @Test
    public void asBooleanFromString()
    {
        assertEquals(true, ColumnCaster.asBoolean(ValueFactory.newString("true")));
        try {
            ColumnCaster.asBoolean(ValueFactory.newString("foo"));
            fail();
        }
        catch (Throwable t) {
            assertTrue(t instanceof DataException);
        }
    }

    @Test
    public void asBooleanFromJson()
    {
        try {
            ColumnCaster.asBoolean(mapValue);
            fail();
        }
        catch (Throwable t) {
            assertTrue(t instanceof DataException);
        }
    }

    @Test
    public void asLongFromBoolean()
    {
        assertEquals(1, ColumnCaster.asLong(ValueFactory.newBoolean(true)));
    }

    @Test
    public void asLongFromInteger()
    {
        assertEquals(1, ColumnCaster.asLong(ValueFactory.newInteger(1)));
    }

    @Test
    public void asLongFromFloat()
    {
        assertEquals(1, ColumnCaster.asLong(ValueFactory.newFloat(1.5)));
    }

    @Test
    public void asLongFromString()
    {
        assertEquals(1, ColumnCaster.asLong(ValueFactory.newString("1")));
        try {
            ColumnCaster.asLong(ValueFactory.newString("foo"));
            fail();
        }
        catch (Throwable t) {
            assertTrue(t instanceof DataException);
        }
    }

    @Test
    public void asLongFromJson()
    {
        try {
            ColumnCaster.asLong(mapValue);
            fail();
        }
        catch (Throwable t) {
            assertTrue(t instanceof DataException);
        }
    }

    @Test
    public void asDoubleFromBoolean()
    {
        assertEquals(1, ColumnCaster.asLong(ValueFactory.newBoolean(true)));
    }

    @Test
    public void asDoubleFromInteger()
    {
        assertEquals(1, ColumnCaster.asLong(ValueFactory.newInteger(1)));
    }

    @Test
    public void asDoubleFromFloat()
    {
        assertEquals(1, ColumnCaster.asLong(ValueFactory.newFloat(1.5)));
    }

    @Test
    public void asDoubleFromString()
    {
        assertEquals(1, ColumnCaster.asLong(ValueFactory.newString("1")));
        try {
            ColumnCaster.asLong(ValueFactory.newString("foo"));
            fail();
        }
        catch (Throwable t) {
            assertTrue(t instanceof DataException);
        }
    }

    @Test
    public void asDoubleFromJson()
    {
        try {
            ColumnCaster.asLong(mapValue);
            fail();
        }
        catch (Throwable t) {
            assertTrue(t instanceof DataException);
        }
    }

    @Test
    public void asStringFromBoolean()
    {
        assertEquals("true", ColumnCaster.asString(ValueFactory.newBoolean(true)));
    }

    @Test
    public void asStringFromInteger()
    {
        assertEquals("1", ColumnCaster.asString(ValueFactory.newInteger(1)));
    }

    @Test
    public void asStringFromFloat()
    {
        assertEquals("1.5", ColumnCaster.asString(ValueFactory.newFloat(1.5)));
    }

    @Test
    public void asStringFromString()
    {
        assertEquals("1", ColumnCaster.asString(ValueFactory.newString("1")));
    }

    @Test
    public void asStringFromJson()
    {
        assertEquals("{\"k\":\"v\"}", ColumnCaster.asString(mapValue));
    }

    @Test
    public void asTimestampFromBoolean()
    {
        try {
            ColumnCaster.asTimestamp(ValueFactory.newBoolean(true), parser);
            fail();
        }
        catch (Throwable t) {
            assertTrue(t instanceof DataException);
        }
    }

    @Test
    public void asTimestampFromInteger()
    {
        assertEquals(1, ColumnCaster.asTimestamp(ValueFactory.newInteger(1), parser).getEpochSecond());
    }

    @Test
    public void asTimestampFromFloat()
    {
        Timestamp expected = Timestamp.ofEpochSecond(1463084053, 500000000);
        assertEquals(expected, ColumnCaster.asTimestamp(ValueFactory.newFloat(1463084053.5), parser));
    }

    @Test
    public void asTimestampFromString()
    {
        Timestamp expected = Timestamp.ofEpochSecond(1463084053, 500000000);
        assertEquals(expected, ColumnCaster.asTimestamp(ValueFactory.newString("2016-05-12 20:14:13.5"), parser));
    }

    @Test
    public void asTimestampFromJson()
    {
        try {
            ColumnCaster.asTimestamp(mapValue, parser);
            fail();
        }
        catch (Throwable t) {
            assertTrue(t instanceof DataException);
        }
    }
}
