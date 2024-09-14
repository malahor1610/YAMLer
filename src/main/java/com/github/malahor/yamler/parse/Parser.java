package com.github.malahor.yamler.parse;

import com.github.malahor.yamler.lex.Token;
import java.util.List;

public class Parser {

  private int currentToken = 0;
  private int indentation = 0;

  public <T> T parse(List<Token> tokens, Class<T> resultType) {
    var resultBuilder = new ResultBuilder<T>(resultType);
    var field = "";
    for (; currentToken < tokens.size(); currentToken++) {
      var token = tokens.get(currentToken);
      switch (token.getType()) {
        case IDENTIFIER -> field = token.getValue();
        case VALUE -> resultBuilder.setValue(field, token.getValue());
        case ARRAY_ELEMENT -> resultBuilder.addValue(token.getValue());
        case INDENTATION -> {
          var newIndentation = token.getIndentation();
          if (indentation < newIndentation) {
            indentation = newIndentation;
            var nestedValue = parse(tokens, resultBuilder.determineClass(field));
            resultBuilder.setNestedValue(field, nestedValue);
          } else if (indentation > newIndentation) {
            indentation = newIndentation;
            return resultBuilder.getResult();
          }
        }
      }
    }
    return resultBuilder.getResult();
  }

}
