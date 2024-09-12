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
    saveIndentation(line);
    saveIndetifier(line);
    if (line.matches(".*:.+")) saveValue(line);
  }

  private void saveIndentation(String line) {
    var spaces = line.length() - line.stripLeading().length();
    var indentation = spaces / 2;
    tokens.add(Token.indentation(indentation));
  }

  private void saveIndetifier(String line) {
    var endIndex = line.contains(SEPARATOR) ? line.indexOf(SEPARATOR) : line.lastIndexOf(":");
    var identifier = line.substring(0, endIndex);
    tokens.add(Token.identifier(identifier.strip()));
  }

  private void saveValue(String line) {
    var value = line.substring(line.indexOf(SEPARATOR) + SEPARATOR.length());
    tokens.add(Token.value(value));
  }
}
