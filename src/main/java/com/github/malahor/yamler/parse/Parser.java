package com.github.malahor.yamler.parse;

import com.github.malahor.yamler.lex.Token;
import com.github.malahor.yamler.lex.TokenType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Parser {

  private int currentToken = 0;
  private int indentation = 0;

  public <T> T parse(List<Token> tokens, Class<T> resultType) {
    try {
      var result = resultType.getConstructor().newInstance();
      var setters =
          Arrays.stream(resultType.getMethods())
              .filter(method -> method.getName().startsWith("set"))
              .toList();
      Optional<Method> setter = Optional.empty();
      Token previous = null;
      boolean out = false;
      for (; currentToken < tokens.size(); currentToken++) {
        var token = tokens.get(currentToken);
        switch (token.getType()) {
          case IDENTIFIER -> setter = setter(setters, token.getValue());
          case VALUE ->
              setter.ifPresent(
                  s -> {
                    try {
                      s.invoke(result, token.getValue());
                    } catch (IllegalAccessException | InvocationTargetException e) {
                      throw new RuntimeException(e);
                    }
                  });
          case INDENTATION -> {
            if (previous != null
                && previous.getType().equals(TokenType.IDENTIFIER)
                && indentation < Integer.parseInt(token.getValue())) {
              setter.ifPresent(
                  s -> {
                    try {
                      indentation = Integer.parseInt(token.getValue());
                      var subResult = parse(tokens, s.getParameterTypes()[0]);
                      s.invoke(result, subResult);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                      throw new RuntimeException(e);
                    }
                  });
            } else if(indentation > Integer.parseInt(token.getValue())){
              indentation = Integer.parseInt(token.getValue());
              out = true;
            }
          }
        }
        previous = token;
        if(out) break;
      }
      return result;
    } catch (InstantiationException
        | IllegalAccessException
        | InvocationTargetException
        | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  private Optional<Method> setter(List<Method> setters, String field) {
    return setters.stream()
        .filter(setter -> setter.getName().equalsIgnoreCase("set" + field))
        .findFirst();
  }
}
