Embulk::JavaPlugin.register_parser(
  "jsonl", "org.embulk.parser.jsonl.JsonlParserPlugin",
  File.expand_path('../../../../classpath', __FILE__))