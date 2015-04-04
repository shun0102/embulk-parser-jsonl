
Gem::Specification.new do |spec|
  spec.name          = "embulk-parser-jsonl"
  spec.version       = "0.0.1"
  spec.authors       = ["Shunsuke Mikami"]
  spec.summary       = "Jsonl parser plugin for Embulk"
  spec.description   = "Parses Jsonl files read by other file input plugins."
  spec.email         = ["shun0102@gmail.com"]
  spec.licenses      = ["MIT"]
  spec.homepage      = "https://github.com/shun0102/embulk-parser-jsonl"

  spec.files         = `git ls-files`.split("\n") + Dir["classpath/*.jar"]
  spec.test_files    = spec.files.grep(%r{^(test|spec)/})
  spec.require_paths = ["lib"]

  spec.add_development_dependency 'bundler', ['~> 1.0']
  spec.add_development_dependency 'rake', ['~> 10.0']
end
