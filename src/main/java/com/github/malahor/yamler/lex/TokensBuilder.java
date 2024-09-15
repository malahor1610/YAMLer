package com.github.malahor.yamler.lex;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import lombok.Getter;

public class TokensBuilder {

  private static final String SEPARATOR = ":\s";
  @Getter private final List<Token> tokens = new LinkedList<>();
  private String longValue = "";

  public void appendToFold(String line) {
    longValue += line.strip() + " ";
  }

  public void appendToPipe(String line) {
    longValue += line.strip() + "\n";
  }

  public void saveIndentation(int indentation) {
    tokens.add(Token.indentation(indentation));
  }

  public void saveIndetifier(String line) {
    var endIndex = line.contains(SEPARATOR) ? line.indexOf(SEPARATOR) : line.lastIndexOf(":");
    var identifier = line.substring(0, endIndex);
    tokens.add(Token.identifier(identifier));
  }

  public void saveValue(String line) {
    var value = line.substring(line.indexOf(SEPARATOR) + SEPARATOR.length());
    tokens.add(Token.value(value));
  }

  public void saveSpecialValue() {
    tokens.add(Token.value(longValue));
    longValue = "";
  }

  public void saveArray(String line) {
    var indentation = determineIndentation(line) + 1;
    var value = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
    Arrays.stream(value.split(","))
        .forEach(
            element -> {
              tokens.add(Token.indentation(indentation));
              tokens.add(Token.arrayElement(element));
            });
  }

  public void saveArrayElement(String line) {
    var value = line.substring(line.indexOf("-") + 1);
    tokens.add(Token.arrayElement(value));
  }

  public int determineIndentation(String line) {
    var spaces = line.length() - line.stripLeading().length();
    return spaces / 2;
  }
}
