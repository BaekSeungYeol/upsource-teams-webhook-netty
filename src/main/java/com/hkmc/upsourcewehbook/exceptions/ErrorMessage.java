package com.hkmc.upsourcewehbook.exceptions;

import lombok.Getter;
import lombok.ToString;

@Getter @ToString
public class ErrorMessage {

  private String status;

  private String message;

  public ErrorMessage(String status, String message) {
    this.status = status;
    this.message = message;
  }

}
