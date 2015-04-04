require 'json'

module Embulk
  module Guess

    # TODO implement guess plugin to make this command work:
    #      $ embulk guess -g "jsonl" partial-config.yml
    #
    #      Depending on the file format the plugin uses, you can use choose
    #      one of binary guess (GuessPlugin), text guess (TextGuessPlugin),
    #      or line guess (LineGuessPlugin).

    require "embulk/parser/jsonl.rb"

    #class JsonlParserGuessPlugin < GuessPlugin
    #  Plugin.register_guess("jsonl", self)
    #
    #  def guess(config, sample_buffer)
    #    if sample_buffer[0,2] == GZIP_HEADER
    #      guessed = {}
    #      guessed["type"] = "jsonl"
    #      guessed["property1"] = "guessed-value"
    #      return {"parser" => guessed}
    #    else
    #      return {}
    #    end
    #  end
    #end

    #class JsonlParserGuessPlugin < TextGuessPlugin
    #  Plugin.register_guess("jsonl", self)
    #
    #  def guess_text(config, sample_text)
    #    js = JSON.parse(sample_text) rescue nil
    #    if js && js["mykeyword"] == "keyword"
    #      guessed = {}
    #      guessed["type"] = "jsonl"
    #      guessed["property1"] = "guessed-value"
    #      return {"parser" => guessed}
    #    else
    #      return {}
    #    end
    #  end
    #end

    class JsonlParserGuessPlugin < LineGuessPlugin
      Plugin.register_guess("jsonl", self)

      def guess_lines(config, sample_lines)
        columns = {}
        sample_lines.each do |line|
          hash = JSON.parse(line)
          hash.each do |k, v|
            columns[k] = get_embulk_type(v)
          end
        end
        schema = []
        columns.each do |k,v|
          schema << {'name' => k, 'type' => v}
        end
        guessed = {}
        guessed["type"] = "jsonl"
        guessed["schema"] = schema
        return {"parser" => guessed}
      end

      private

      def get_embulk_type(val)
        case val
        when TrueClass
          return "boolean"
        when FalseClass
          return "boolean"
        when Integer
          return "long"
        when Float
          return "double"
        else
          return "string"
        end
      end
    end
  end
end
