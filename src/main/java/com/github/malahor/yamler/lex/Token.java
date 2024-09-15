package com.github.malahor.yamler.lex;

import lombok.Data;

@Data
public class Token {

  private TokenType type;
  private String value;

  public Token(TokenType type, String value) {
    this.type = type;
    this.value = value;
    System.out.println(this);
  }

  public static Token indentation(int indentation) {
    return new Token(TokenType.INDENTATION, String.valueOf(indentation));
  }

  public static Token identifier(String identifier) {
    return new Token(TokenType.IDENTIFIER, identifier.strip());
  }

  public static Token value(String value) {
    return new Token(TokenType.VALUE, value.strip());
  }

  public static Token arrayElement(String element) {
    return new Token(TokenType.ARRAY_ELEMENT, element.strip());
  }

  public int getIndentation() {
    return Integer.parseInt(value);
  }
}
