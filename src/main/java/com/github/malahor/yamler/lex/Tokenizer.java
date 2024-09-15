package com.github.malahor.yamler.lex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class Tokenizer {

  private final TokensBuilder builder = new TokensBuilder();
  private TokenizeMode mode = TokenizeMode.BASIC;
  private int indentation = 0;

  public List<Token> tokenize(InputStream input) {
    try (var in = new BufferedReader(new InputStreamReader(input))) {
      in.lines().forEach(this::tokenizeLine);
      return builder.getTokens();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void tokenizeLine(String line) {
    if (mode.isSpecial()) tokenizeSpecial(line);
    else tokenizeBasic(line);
  }

  private void tokenizeSpecial(String line) {
    if (builder.determineIndentation(line) <= this.indentation) {
      closeSpecialMode();
      tokenizeBasic(line);
    } else {
      if (mode.equals(TokenizeMode.PIPE)) builder.appendToPipe(line);
      else builder.appendToFold(line);
    }
  }

  private void tokenizeBasic(String line) {
    indentation = builder.determineIndentation(line);
    builder.saveIndentation(indentation);
    if (line.matches("\s+-.*")) {
      builder.saveArrayElement(line);
      return;
    }
    builder.saveIndetifier(line);
    if (line.matches(".*:\s+>")) mode = TokenizeMode.FOLD;
    else if (line.matches(".*:\s+\\|")) mode = TokenizeMode.PIPE;
    else if (line.matches(".*:\s+\\[.*]")) builder.saveArray(line);
    else if (line.matches(".*:.+")) builder.saveValue(line);
  }

  private void closeSpecialMode() {
    mode = TokenizeMode.BASIC;
    builder.saveSpecialValue();
  }
}
