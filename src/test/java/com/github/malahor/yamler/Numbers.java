package com.github.malahor.yamler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Numbers {

  private Integer decimal;
  private Integer octal;
  private Integer hexadecimal;
  private Float floating;
  private Float alsoFloating;
  private Float infinite;
  private Float nan;
  private Float maybe;
}
