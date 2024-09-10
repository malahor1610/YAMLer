package com.github.malahor.yamler.parse;

import com.github.malahor.yamler.lex.Token;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Parser {

  public <T> T parse(List<Token> tokens, Class<T> resultType) {
    try {
      var result = resultType.getConstructor().newInstance();
      var setters =
          Arrays.stream(resultType.getMethods())
              .filter(method -> method.getName().startsWith("set"))
              .toList();
      Optional<Method> setter = Optional.empty();
      for (var token : tokens) {
        switch (token.getType()) {
          case IDENTIFIER -> setter = setter(setters, token.getValue());
          default ->
              setter.ifPresent(
                  s -> {
                    try {
                      s.invoke(result, token.getValue());
                    } catch (IllegalAccessException | InvocationTargetException e) {
                      throw new RuntimeException(e);
                    }
                  });
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

  private Optional<Method> setter(List<Method> setters, String field) {
    return setters.stream()
        .filter(setter -> setter.getName().equalsIgnoreCase("set" + field))
        .findFirst();
  }
}
