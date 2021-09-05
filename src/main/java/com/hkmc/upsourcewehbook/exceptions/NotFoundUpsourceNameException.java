package com.hkmc.upsourcewehbook.exceptions;

public class NotFoundUpsourceNameException extends RuntimeException {

  public static final String MESSAGE = "해당 리뷰에 관련된 이름중 정상적이지 않은 값이 있습니다.";

  public NotFoundUpsourceNameException() {
    super(MESSAGE);
  }

}
