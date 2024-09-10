package com.github.malahor.yamler;

import com.github.malahor.yamler.lex.Tokenizer;
import com.github.malahor.yamler.parse.Parser;
import java.io.*;

public class YamlParserImpl implements YamlParser {

  public <T> T read(InputStream input, Class<T> resultType) {
    var tokenizer = new Tokenizer();
    var tokens = tokenizer.tokenize(input);
    var parser = new Parser();
    return parser.parse(tokens, resultType);
  }
}
