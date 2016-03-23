package org.embulk.parser.jsonl.getter;

import org.embulk.spi.ColumnConfig;
import org.embulk.spi.PageBuilder;
import org.embulk.spi.time.TimestampParser;

public class ColumnGetterFactory {
    private PageBuilder pageBuilder;
    private TimestampParser[] timestampParsers;

    public ColumnGetterFactory(PageBuilder pageBuilder, TimestampParser[] timestampParsers) {
        this.pageBuilder = pageBuilder;
        this.timestampParsers = timestampParsers;
    }

    public CommonColumnGetter newColumnGetter(ColumnConfig columnConfig) {
        switch (columnConfig.getType().getName()) {
        case "string":
            return new StringColumnGetter(pageBuilder, timestampParsers);
        default:
            return new CommonColumnGetter(pageBuilder, timestampParsers);
        }
    }
}
