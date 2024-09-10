package com.github.malahor.yamler;

import java.io.InputStream;

public interface YamlParser {

  <T> T read(InputStream input, Class<T> resultType);
}
