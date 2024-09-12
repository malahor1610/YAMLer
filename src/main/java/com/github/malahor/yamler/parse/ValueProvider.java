package com.github.malahor.yamler.parse;

public class ValueProvider {

  public static <V> V typedValue(String value){
    return (V) switch (value) {
      case String v when v.matches("null|Null|NULL|~") || v.isEmpty() -> null;
      case String v when v.matches("true|True|TRUE|false|False|FALSE") -> Boolean.valueOf(v.toLowerCase());
      case String v when v.matches("[-+]?[0-9]+") -> Integer.parseInt(v);
      case String v when v.matches("0o[0-7]+") -> Integer.parseInt(v.substring(2), 8);
      case String v when v.matches("0x[0-9a-fA-F]+") -> Integer.parseInt(v.substring(2), 16);
      case String v when v.matches("[-+]?([0-9]*\\.[0-9]+)") -> Float.parseFloat(v);
      case String v when v.matches("-(\\.inf|\\.Inf|\\.INF)") -> Float.NEGATIVE_INFINITY;
      case String v when v.matches("[+]?(\\.inf|\\.Inf|\\.INF)") -> Float.POSITIVE_INFINITY;
      case String v when v.matches("\\.nan|\\.NaN|\\.NAN") -> Float.NaN;
      default -> handleQuotedString(value);
    };
  }

  private static String handleQuotedString(String value) {
    if (value.matches("\".*\"")) return value.replaceAll("\"", "").translateEscapes();
    return value;
  }
}
