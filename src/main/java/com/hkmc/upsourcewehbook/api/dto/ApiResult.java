package com.hkmc.upsourcewehbook.api.dto;

import com.hkmc.upsourcewehbook.exceptions.ErrorMessage;
import lombok.ToString;

@ToString
public class ApiResult<T> {

  private boolean success;

  private T response;

  private T error;

  ApiResult() { /* empty */ }

  ApiResult(boolean success, T response, T error) {
    this.success = success;
    this.response = response;
    this.error = error;
  }

  public static <T> ApiResult<T> succeed(T data) { return new ApiResult<>(true, data, null); }

  public static ApiResult<?> failed(Throwable throwable) { return failed(throwable.getMessage());}

  public static ApiResult<?> failed(String message) {
    return new ApiResult<>(false, null, new ErrorMessage("400", message));
  }

  public boolean isSuccess() { return success; }

  public T getResponse() {
    return response;
  }

  public T getError() {
    return error;
  }
}
