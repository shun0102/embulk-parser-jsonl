# Jsonl parser plugin for Embulk

TODO: Write short description here and embulk-parser-jsonl.gemspec file.

## Overview

* **Plugin type**: parser
* **Guess supported**: yes

## Configuration

- **type**: specify this parser as jsonl
- **columns**: specify column name and type (array, required)

## Example

```yaml
in:
  type: any file input plugin type
  parser:
    type: jsonl
    columns:
      - {name: first_name, type: string}
      - {name: last_name, type: string}
      - {name: age, type: long}
```

(If guess supported) you don't have to write `parser:` section in the configuration file. After writing `in:` section, you can let embulk guess `parser:` section using this command:

```
$ embulk install embulk-parser-jsonl
$ embulk guess -g jsonl config.yml -o guessed.yml
```

## Build

```
$ ./gradlew gem classpath
```
