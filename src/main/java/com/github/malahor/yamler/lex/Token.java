package com.github.malahor.yamler.lex;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Token {

  private TokenType type;
  private String value;
}
