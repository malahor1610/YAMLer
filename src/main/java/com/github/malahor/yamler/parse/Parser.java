package com.github.malahor.yamler.parse;

import com.github.malahor.yamler.lex.Token;
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
      var setters = getSetters(resultType);
      Optional<Method> setter = Optional.empty();
      for (; currentToken < tokens.size(); currentToken++) {
        var token = tokens.get(currentToken);
        switch (token.getType()) {
          case IDENTIFIER -> setter = setter(setters, token.getValue());
          case VALUE -> setter.ifPresent(s -> setValue(result, s, token.getValue()));
          case INDENTATION -> {
            var newIndentation = token.getIndentation();
            if (indentation < newIndentation) {
              indentation = newIndentation;
              setter.ifPresent(s -> setNestedValue(result, s, tokens));
            } else if (indentation > newIndentation) {
              indentation = newIndentation;
              return result;
            }
          }
        }
      }
      return result;
    } catch (InstantiationException
        | IllegalAccessException
        | InvocationTargetException
        | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  private <T> List<Method> getSetters(Class<T> resultType) {
    return Arrays.stream(resultType.getMethods())
        .filter(method -> method.getName().startsWith("set"))
        .toList();
  }

  private <T> void setValue(T result, Method setter, String value) {
    try {
      setter.invoke(result, value);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  private <T> void setNestedValue(T result, Method setter, List<Token> tokens) {
    try {
      var nestedValue = parse(tokens, setter.getParameterTypes()[0]);
      setter.invoke(result, nestedValue);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  private Optional<Method> setter(List<Method> setters, String field) {
    return setters.stream()
        .filter(setter -> setter.getName().equalsIgnoreCase("set" + field))
        .findFirst();
  }
}
