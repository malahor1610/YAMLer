package com.github.malahor.yamler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Challenge {

  private String name;
  private Tags tags;
  private int steps;
  private boolean finished;
  private String introduction;
  private String description;
}
