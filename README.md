# Jsonl parser plugin for Embulk

[JSONL (JSON Lines)](http://jsonlines.org/) parser plugin for Embulk

## Overview

* **Plugin type**: parser
* **Guess supported**: yes

## Configuration

- **type**: Specify this parser as jsonl
- **columns**: Specify column name and type. See below (array, required)
* **stop_on_invalid_record**: Stop bulk load transaction if a file includes invalid record (such as invalid timestamp) (boolean, default: false)
* **default_timezone**: Default timezone of the timestamp (string, default: UTC)
* **default_timestamp_format**: Default timestamp format of the timestamp (string, default: `%Y-%m-%d %H:%M:%S.%N %z`)
* **newline**: Newline character (CRLF, LF or CR) (string, default: CRLF)
* **charset**: Character encoding (eg. ISO-8859-1, UTF-8) (string, default: UTF-8)
* **default_typecast**: Specify whether to cast values automatically to the specified types or not (boolean, default: true)
* **min_rows_for_guess**: Minimum rows for guess (integer, default: 4)

### columns

* **name**: Name of the column (string, required)
* **type**: Type of the column (string, required)
* **timezone**: Timezone of the timestamp if type is timestamp (string, default: default_timestamp)
* **format**: Format of the timestamp if type is timestamp (string, default: default_format)
* **typecast**: Whether cast values or not (boolean, default: default_typecast)

## Guess

(If guess supported) you don't have to write `parser:` section in the configuration file. After writing `in:` section, you can let embulk guess `parser:` section using this command:

```
$ embulk gem install embulk-parser-jsonl
$ embulk guess -g jsonl config.yml -o guessed.yml
```

## Build

```
$ ./gradlew gem classpath
```
