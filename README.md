# Jsonl parser plugin for Embulk

TODO: Write short description here and embulk-parser-jsonl.gemspec file.

## Overview

* **Plugin type**: parser
* **Guess supported**: no

## Configuration

- **property1**: description (string, required)
- **property2**: description (integer, default: default-value)

## Example

```yaml
in:
  type: any file input plugin type
  parser:
    type: jsonl
    property1: example1
    property2: example2
```

(If guess supported) you don't have to write `parser:` section in the configuration file. After writing `in:` section, you can let embulk guess `parser:` section using this command:

```
$ embulk install embulk-parser-jsonl
$ embulk guess -g jsonl config.yml -o guessed.yml
```

## Build

```
$ rake
```
