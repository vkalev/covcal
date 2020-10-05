package edu.brown.cs.student.Utilities;

import com.google.gson.Gson;
import spark.ResponseTransformer;

/**
 * Utility class for Json conversion.
 */
public final class JsonUtil {

  /**
   * Private constructor for JsonUtil.
   */
  private JsonUtil() {
  }

  /**
   * Using Gson instance to covert java object to Json String representation.
   * @param o - java object to convert
   * @return Gson String representation of o
   */
  public static String toJson(Object o) {
    return new Gson().toJson(o);
  }

  /**
   * Get ResponseTransformer instance for Json String conversion.
   * @return the created ResponseTransformer
   */
  public static ResponseTransformer json() {
    return JsonUtil::toJson;
  }
}
