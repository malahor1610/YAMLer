package com.github.malahor.yamler.lex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class Tokenizer {

  private static final String SEPARATOR = ":\s";
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
    switch (line){
      case String s when s.matches(".*:") -> {
        saveIndentation(line);
        saveIndetifier(line);
      }
      case String s when s.matches(".*:.+") -> {
        saveIndentation(line);
        saveIndetifier(line);
        saveValue(line);
      }
      default -> throw new RuntimeException();
    }
  }

  private void saveIndentation(String line) {
    var spaces = line.length() - line.stripLeading().length();
    var indentation = spaces / 2;
    tokens.add(Token.indentation(indentation));
  }

  private void saveIndetifier(String line) {
    var identifier = line.substring(0, line.lastIndexOf(":"));
    tokens.add(Token.identifier(identifier.strip()));
  }

  private void saveValue(String line) {
    var value = line.substring(line.indexOf(SEPARATOR) + SEPARATOR.length());
    tokens.add(Token.value(value));
  }

}
