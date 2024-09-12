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
    var expectedResult = buildExpectedResult();
    Assertions.assertEquals(expectedResult, result);
  }

  private Result buildExpectedResult() {
    return Result.builder()
        .name("Coding Challenges")
        .solution("Java")
        .challenge(
            Challenge.builder()
                .name("YAML:parser")
                .steps(5)
                .finished(false)
                .tags(Tags.builder().difficulty("intermediate").type("parsing").build())
                .build())
        .link(null)
        .author(null)
        .numbers(
            Numbers.builder()
                .decimal(-15)
                .octal(7)
                .hexadecimal(58)
                .floating(17.3f)
                .alsoFloating(.53f)
                .infinite(Float.POSITIVE_INFINITY)
                .nan(Float.NaN)
                .maybe(null)
                .build())
        .build();
  }

  private Result executeParsing(String yamlName) {
    try (var input = ClassLoader.getSystemClassLoader().getResourceAsStream(yamlName)) {
      return parser.read(input, Result.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
