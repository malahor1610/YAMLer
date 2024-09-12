package com.github.malahor.yamler;

import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class YamlParserTest {

  private YamlParserImpl parser;

  @BeforeEach
  public void setup() {
    parser = new YamlParserImpl();
  }

  @Test
  public void validTest() {
    var result = executeParsing("example.yml");
    Assertions.assertEquals("Coding Challenges", result.getName());
    Assertions.assertEquals("YAML parser", result.getChallenge().getName());
  }

  private Result executeParsing(String yamlName) {
    try (var input = ClassLoader.getSystemClassLoader().getResourceAsStream(yamlName)) {
      return parser.read(input, Result.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
