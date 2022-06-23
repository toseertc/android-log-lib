package com.toseertc.log.logger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.toseertc.log.logger.Utils.checkNotNull;


/**
 * <pre>
 *  ┌────────────────────────────────────────────
 *  │ LOGGER
 *  ├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄
 *  │ Standard logging mechanism
 *  ├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄
 *  │ But more pretty, simple and powerful
 *  └────────────────────────────────────────────
 * </pre>
 *
 * <h3>How to use it</h3>
 * Initialize it first
 * <pre><code>
 *   Logger.addLogAdapter(new AndroidLogAdapter());
 * </code></pre>
 *
 * And use the appropriate static Logger methods.
 *
 * <pre><code>
 *   Logger.d("debug");
 *   Logger.e("error");
 *   Logger.w("warning");
 *   Logger.v("verbose");
 *   Logger.i("information");
 *   Logger.wtf("What a Terrible Failure");
 * </code></pre>
 *
 * <h3>String format arguments are supported</h3>
 * <pre><code>
 *   Logger.d("hello %s", "world");
 * </code></pre>
 *
 * <h3>Collections are support ed(only available for debug logs)</h3>
 * <pre><code>
 *   Logger.d(MAP);
 *   Logger.d(SET);
 *   Logger.d(LIST);
 *   Logger.d(ARRAY);
 * </code></pre>
 *
 * <h3>Json and Xml support (output will be in debug level)</h3>
 * <pre><code>
 *   Logger.json(JSON_CONTENT);
 *   Logger.xml(XML_CONTENT);
 * </code></pre>
 *
 * <h3>Customize Logger</h3>
 * Based on your needs, you can change the following settings:
 * <ul>
 *   <li>Different {@link LogAdapter}</li>
 *   <li>Different {@link FormatStrategy}</li>
 *   <li>Different {@link LogStrategy}</li>
 * </ul>
 *
 * @see LogAdapter
 * @see FormatStrategy
 * @see LogStrategy
 */
public final class Logger {

  public static final int VERBOSE = 2;
  public static final int DEBUG = 3;
  public static final int INFO = 4;
  public static final int WARN = 5;
  public static final int ERROR = 6;
  public static final int ASSERT = 7;

  @NonNull private  Printer printer = new LoggerPrinter();

  public Logger() {
    //no instance
  }

  public static void printer(@NonNull Printer printer) {
    printer = checkNotNull(printer);
  }

  public void addLogAdapter(@NonNull LogAdapter adapter) {
    printer.addAdapter(checkNotNull(adapter));
  }

  public  void clearLogAdapters() {
    printer.clearLogAdapters();
  }

  /**
   * Given tag will be used as tag only once for this method call regardless of the tag that's been
   * set during initialization. After this invocation, the general tag that's been set will
   * be used for the subsequent log calls
   */
  public  Printer t(@Nullable String tag) {
    return printer.t(tag);
  }

  /**
   * General log function that accepts all configurations as parameter
   */
  public void log(int priority, @Nullable String tag, @Nullable String message, @Nullable Throwable throwable) {
    printer.log(priority, tag, message, throwable);
  }

  public  void d(@NonNull String message, @Nullable Object... args) {
    printer.d(message, args);
  }

  public  void d(@Nullable Object object) {
    printer.d(object);
  }

  public  void e(@NonNull String message, @Nullable Object... args) {
    printer.e(null, message, args);
  }

  public  void e(@Nullable Throwable throwable, @NonNull String message, @Nullable Object... args) {
    printer.e(throwable, message, args);
  }

  public  void i(@NonNull String message, @Nullable Object... args) {
    printer.i(message, args);
  }

  public  void v(@NonNull String message, @Nullable Object... args) {
    printer.v(message, args);
  }

  public  void w(@NonNull String message, @Nullable Object... args) {
    printer.w(message, args);
  }

  /**
   * Tip: Use this for exceptional situations to log
   * ie: Unexpected errors etc
   */
  public  void wtf(@NonNull String message, @Nullable Object... args) {
    printer.wtf(message, args);
  }

  /**
   * Formats the given json content and print it
   */
  public  void json(@Nullable String json) {
    printer.json(json);
  }

  /**
   * Formats the given xml content and print it
   */
  public  void xml(@Nullable String xml) {
    printer.xml(xml);
  }

}
