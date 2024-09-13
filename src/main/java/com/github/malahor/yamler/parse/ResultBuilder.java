package com.github.malahor.yamler.parse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;

public class ResultBuilder<T> {

  @Getter private final T result;
  private final List<Method> setters;

  public ResultBuilder(Class<T> resultType) {
    try {
      this.result = resultType.getConstructor().newInstance();
    } catch (InstantiationException
        | IllegalAccessException
        | InvocationTargetException
        | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
    this.setters = getSetters(resultType);
  }

  public void setValue(String field, String value) {
    try {
      var setter = setter(setters, field);
      var typedValue = ValueProvider.typedValue(value);
      setter.invoke(result, typedValue);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  public void setNestedValue(String field, Object nestedValue) {
    try {
      var setter = setter(setters, field);
      setter.invoke(result, nestedValue);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  public Class<?> determineClass(String field) {
    var setter = setter(setters, field);
    return setter.getParameterTypes()[0];
  }

  private Method setter(List<Method> setters, String field) {
    return setters.stream()
        .filter(setter -> setter.getName().equalsIgnoreCase("set" + field))
        .findFirst()
        .orElseThrow();
  }

  private List<Method> getSetters(Class<T> resultType) {
    return Arrays.stream(resultType.getMethods())
        .filter(method -> method.getName().startsWith("set"))
        .toList();
  }
}
