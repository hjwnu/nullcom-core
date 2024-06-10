package org.nullcom.nullcomcore.exception;

import lombok.Getter;

/**
 * Exception을 상속받지 않고 RuntimeException을 상속받는 이유는 트랜잭션 처리를 적용하기 위함
 */
@Getter
public class NullComException extends RuntimeException {

  private final String[] args;

  private final String defaultMessage;

  public NullComException() {
    super("");
    this.args = null;
    this.defaultMessage = null;
  }

  public NullComException(Exception e) {
    super(e);
    this.args = null;
    this.defaultMessage = null;
  }

  public NullComException(String message) {
    super(message);
    this.args = null;
    this.defaultMessage = null;
  }

  public NullComException(String message, String[] args) {
    super(message);
    this.args = args;
    this.defaultMessage = null;
  }

  public NullComException(String message, String defaultMessage) {
    super(message);
    this.args = null;
    this.defaultMessage = defaultMessage;
  }

  public NullComException(String message, String[] args, String defaultMessage) {
    super(message);
    this.args = args;
    this.defaultMessage = defaultMessage;
  }
}
