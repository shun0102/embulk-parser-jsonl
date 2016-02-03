require 'json'

module Embulk
  module Parser

    class JsonlParserPlugin < ParserPlugin
      Plugin.register_parser("jsonl", self)

      def self.transaction(config, &control)
        parser_task = config.load_config(Java::LineDecoder::DecoderTask)
        task = {
          "decoder_task" => DataSource.from_java(parser_task.dump),
          "schema" => config.param("schema", :array)
        }
        columns = task["schema"].each_with_index.map do |c, i|
          Column.new(i, c["name"], c["type"].to_sym)
        end
        yield(task, columns)
      end

      def init
        @decoder_task = task.param("decoder_task", :hash).load_task(Java::LineDecoder::DecoderTask)
      end

      def run(file_input)
        decoder = Java::LineDecoder.new(file_input.instance_eval { @java_file_input }, @decoder_task)
        schema = @task["schema"]

        while decoder.nextFile
          while line = decoder.poll
            begin
              hash = JSON.parse(line)
              @page_builder.add(make_record(schema, hash))
            rescue
              # TODO: logging
            end
          end
        end
        page_builder.finish
      end

      private

      def make_record(schema, e)
        schema.map do |c|
          val = e[c["name"]]
          v = val.nil? ? "" : val
          case c["type"]
          when "string"
            v
          when "long"
            v.to_i
          when "double"
            v.to_f
          when "boolean"
            if v.nil?
              nil
            elsif v.kind_of?(String)
              ["yes", "true", "1"].include?(v.downcase)
            elsif v.kind_of?(Numeric)
              !v.zero?
            else
              !!v
            end
          when "timestamp"
            v.empty? ? nil : Time.strptime(v, c["time_format"])
          else
            raise "Unsupported type #{c['type']}"
          end
        end
      end
    end

  end
end
