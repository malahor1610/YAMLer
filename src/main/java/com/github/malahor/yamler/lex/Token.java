package com.github.malahor.yamler.lex;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Token {

  private TokenType type;
  private String value;

  public static Token indentation(int indentation) {
    return new Token(TokenType.INDENTATION, String.valueOf(indentation));
  }

  public static Token identifier(String identifier) {
    return new Token(TokenType.IDENTIFIER, identifier.strip());
  }

  public static Token value(String value) {
    return new Token(TokenType.VALUE, value.strip());
  }

  public int getIndentation() {
    return Integer.parseInt(value);
  }
}
