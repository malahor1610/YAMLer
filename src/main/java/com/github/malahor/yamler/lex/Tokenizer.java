package com.github.malahor.yamler.lex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class Tokenizer {

  private static final String SEPARATOR = ":\s";
  private static final String LINE_END = ":\r";
  private List<Token> tokens = new LinkedList<>();

  public List<Token> tokenize(InputStream input) {
    try (var in = new BufferedReader(new InputStreamReader(input))) {
      in.lines().forEach(this::tokenizeLine);
      return tokens;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void tokenizeLine(String line) {
    if(line.endsWith(":")) {
      var identifier = line.substring(0, line.lastIndexOf(":"));
      if(identifier.startsWith("\s\s")) {
        tokens.add(new Token(TokenType.INDENTATION, "1"));
      }
      tokens.add(new Token(TokenType.IDENTIFIER, identifier.strip()));
    } else {
      var identifier = line.substring(0, line.indexOf(SEPARATOR));
      if(identifier.startsWith("\s\s")) {
        tokens.add(new Token(TokenType.INDENTATION, "1"));
      }
      tokens.add(new Token(TokenType.IDENTIFIER, identifier.strip()));
      var value = line.substring(line.indexOf(SEPARATOR) + SEPARATOR.length());
      tokens.add(new Token(TokenType.VALUE, value));
    }
  }
}
