package com.github.malahor.yamler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result {

  private String name;
  private Challenge challenge;
  private String solution;
  private String link;
  private String author;
  private Numbers numbers;
}
