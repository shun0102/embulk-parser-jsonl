package org.embulk.parser.jsonl.cast;

import org.embulk.spi.DataException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TestBooleanCast
{
    @Test
    public void asBoolean()
    {
        assertEquals(true, BooleanCast.asBoolean(true));
        assertEquals(false, BooleanCast.asBoolean(false));
    }

    @Test
    public void asLong()
    {
        assertEquals(1, BooleanCast.asLong(true));
        assertEquals(0, BooleanCast.asLong(false));
    }

    @Test
    public void asDouble()
    {
        try {
            BooleanCast.asDouble(true);
            fail();
        }
        catch (Throwable t) {
            assertTrue(t instanceof DataException);
        }
    }

    @Test
    public void asString()
    {
        assertEquals("true", BooleanCast.asString(true));
        assertEquals("false", BooleanCast.asString(false));
    }

    @Test
    public void asTimestamp()
    {
        try {
            BooleanCast.asTimestamp(true);
            fail();
        }
        catch (Throwable t) {
            assertTrue(t instanceof DataException);
        }
    }
}
