package com.github.malahor.yamler.lex;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Token {

  private TokenType type;
  private String value;
}
