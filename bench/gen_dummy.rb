File::open('bench/sample.jsonl', 'w') { |f|
  (1..1000000).each {
    f.puts(%Q[{"foo":"foo","bool":true,"bool_str":"true","int":10,"int_str":"20","double":1.5,"double_str":"2.5","array":[1,2,3]}])
  }
}
