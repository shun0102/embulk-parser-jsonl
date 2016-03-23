package org.embulk.parser.jsonl;

import org.embulk.config.Config;
import org.embulk.config.ConfigDefault;
import org.embulk.config.Task;
import org.embulk.spi.type.Type;

import com.google.common.base.Optional;

public interface JsonlColumnOption
    extends Task
{
    @Config("type")
    @ConfigDefault("null")
    Optional<Type> getType();
}
