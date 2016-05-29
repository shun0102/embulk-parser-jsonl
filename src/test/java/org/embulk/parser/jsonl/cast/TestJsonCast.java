package org.embulk.parser.jsonl.cast;

import org.embulk.spi.DataException;
import org.junit.Before;
import org.junit.Test;
import org.msgpack.value.Value;
import org.msgpack.value.ValueFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TestJsonCast
{
    public Value value;

    @Before
    public void createResource()
    {
        Value[] kvs = new Value[2];
        kvs[0] = ValueFactory.newString("k");
        kvs[1] = ValueFactory.newString("v");
        value = ValueFactory.newMap(kvs);
    }

    @Test
    public void asBoolean()
    {
        try {
            JsonCast.asBoolean(value);
            fail();
        }
        catch (Throwable t) {
            assertTrue(t instanceof DataException);
        }
    }

    @Test
    public void asLong()
    {
        try {
            JsonCast.asLong(value);
            fail();
        }
        catch (Throwable t) {
            assertTrue(t instanceof DataException);
        }
    }

    @Test
    public void asDouble()
    {
        try {
            JsonCast.asDouble(value);
            fail();
        }
        catch (Throwable t) {
            assertTrue(t instanceof DataException);
        }
    }

    @Test
    public void asString()
    {
        assertEquals("{\"k\":\"v\"}", JsonCast.asString(value));
    }

    @Test
    public void asTimestamp()
    {
        try {
            JsonCast.asTimestamp(value);
            fail();
        }
        catch (Throwable t) {
            assertTrue(t instanceof DataException);
        }
    }
}

