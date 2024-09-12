package com.github.malahor.yamler;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Result {

  private String name;
  private Challenge challenge;
  private String solution;
}
