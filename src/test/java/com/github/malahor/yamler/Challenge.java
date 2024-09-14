package com.github.malahor.yamler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Challenge {

  private String name;
  private Tags tags;
  private int stepsNumber;
  private ArrayList<String> stepsDescriptions;
  private ArrayList<String> steps;
  private boolean finished;
  private String introduction;
  private String description;
}
