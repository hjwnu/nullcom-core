package org.nullcom.nullcomcore.component;

import lombok.extern.slf4j.Slf4j;
import org.nullcom.nullcomcore.constant.CONST;

@Slf4j
abstract public class AbstractComponent {

  protected void info(String msg) {
    if (log.isInfoEnabled()) {
      log.info(this.getLogger() + " " + msg);
    }
  }

  protected void info(String format, Object... arguments) {
    if (log.isInfoEnabled()) {
      log.info(format, arguments);
    }
  }

  protected void debug(String msg) {
    if (log.isDebugEnabled()) {
      log.debug(this.getLogger() + " " + msg);
    }
  }

  protected void debugWithoutClassAndLine(String msg) {
    if (log.isDebugEnabled()) {
      log.debug(msg);
    }
  }

  protected void debug(String format, Object... arguments) {
    if (log.isDebugEnabled()) {
      log.debug(format, arguments);
    }
  }

  protected void debug(String msg, Throwable t) {
    if (log.isDebugEnabled()) {
      log.debug(msg, t);
    }
  }

  protected void error(String msg) {
    if (log.isErrorEnabled()) {
      log.error(this.getLogger() + " " + msg);
    }
  }

  protected void error(String format, Object... arguments) {
    if (log.isErrorEnabled()) {
      log.error(format, arguments);
    }
  }

  protected void error(Exception e) {
    if (log.isErrorEnabled()) {
      log.error("ERROR", e);
    }
  }

  protected void error(String msg, Throwable t) {
    if (log.isErrorEnabled()) {
      log.error(msg, t);
    }
  }

  protected void trace(String format, Object... arguments) {
    if (log.isTraceEnabled()) {
      log.trace(format, arguments);
    }
  }

  protected void trace(String msg, Throwable t) {
    if (log.isTraceEnabled()) {
      log.trace(msg, t);
    }
  }

  protected boolean isDebugEnabled() {
    return log.isDebugEnabled();
  }

  private String getLogger() {
    StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
    String className = stackTraceElements[3].getClassName();
    className = className.replace(CONST.BASE_PACKAGES + ".", "");
    className = className.replace("framework.", "");
    return "[" + className + "." + stackTraceElements[3].getMethodName() + ":" + stackTraceElements[3].getLineNumber()
        + "]:";
  }

}
