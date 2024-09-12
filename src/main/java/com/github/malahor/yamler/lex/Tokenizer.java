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
  private int indentation = 0;
  private boolean foldMode;
  private boolean pipeMode;
  private String longValue = "";

  public List<Token> tokenize(InputStream input) {
    try (var in = new BufferedReader(new InputStreamReader(input))) {
      in.lines().forEach(this::tokenizeLine);
      return tokens;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void tokenizeLine(String line) {
    if (foldMode || pipeMode) {
      if (determineIndentation(line) <= this.indentation) {
        closeSpecialMode();
        tokenizeBasic(line);
      } else {
        continueSpecialMode(line);
      }
    } else tokenizeBasic(line);
  }

  private void tokenizeBasic(String line) {
    saveIndentation(line);
    saveIndetifier(line);
    if (line.matches(".*:\s+>")) foldMode = true;
    else if (line.matches(".*:\s+\\|")) pipeMode = true;
    else if (line.matches(".*:.+")) saveValue(line);
  }

  private void continueSpecialMode(String line) {
    longValue += line.strip() + (pipeMode ? "\n" : " ");
  }

  private void closeSpecialMode() {
    foldMode = false;
    pipeMode = false;
    saveSpecialValue();
  }

  private int determineIndentation(String line) {
    var spaces = line.length() - line.stripLeading().length();
    return spaces / 2;
  }

  private void saveIndentation(String line) {
    indentation = determineIndentation(line);
    tokens.add(Token.indentation(indentation));
  }

  private void saveIndetifier(String line) {
    var endIndex = line.contains(SEPARATOR) ? line.indexOf(SEPARATOR) : line.lastIndexOf(":");
    var identifier = line.substring(0, endIndex);
    tokens.add(Token.identifier(identifier));
  }

  private void saveValue(String line) {
    var value = line.substring(line.indexOf(SEPARATOR) + SEPARATOR.length());
    tokens.add(Token.value(value));
  }

  private void saveSpecialValue() {
    tokens.add(Token.value(longValue));
    longValue = "";
  }
}
