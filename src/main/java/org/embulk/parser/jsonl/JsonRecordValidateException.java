package org.embulk.parser.jsonl;

import org.embulk.spi.DataException;

public class JsonRecordValidateException
        extends DataException {
    JsonRecordValidateException(String message) {
        super(message);
    }

    public JsonRecordValidateException(Throwable cause) {
        super(cause);
    }
}
