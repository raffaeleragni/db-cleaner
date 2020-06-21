package logger;

public final class Logger {
  static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(Logger.class.getName());

  private Logger() {
  }

  public static void log(String message) {
    LOG.fine(message);
  }
}
