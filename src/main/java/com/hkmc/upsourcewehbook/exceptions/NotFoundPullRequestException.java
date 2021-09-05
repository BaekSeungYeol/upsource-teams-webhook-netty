package com.hkmc.upsourcewehbook.exceptions;

public class NotFoundPullRequestException extends RuntimeException {

  public static final String MESSAGE = "존재하지 않는 PR NUMBER 입니다.";

  public NotFoundPullRequestException() {
    super(MESSAGE);
  }

}
