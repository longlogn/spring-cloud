package com.cxlsky.utils.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

/**
 * 响应对象工具
 *
 * @author CXL
 */
@Data
public class ResponseUtil<T> implements Serializable {

    /**
     * 请求成功
     *
     * @param <T>
     * @param data
     * @return
     */
    public static <T> ResponseEntity<Body<T>> ok(T data) {
        return ok(data, "ok");
    }

    /**
     * 请求成功，但无返回内容
     *
     * @return
     */
    public static <T> ResponseEntity<Body<T>> ok() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    /**
     * 请求成功
     *
     * @param <T>
     * @param data
     * @param message
     * @return
     */
    public static <T> ResponseEntity<Body<T>> ok(T data, String message) {
        return ResponseEntity.ok(Body.<T>builder().data(data).success(true).status(HttpStatus.OK.value()).formatDate(new Date()).message(message).build());
    }


    /**
     * 请求方式错误
     *
     * @param message
     * @return
     */
    public static <T> ResponseEntity<Body<T>> badRequest(String message) {
        return badRequest(message, null);
    }

    /**
     * 请求方式错误(自定义code)
     *
     * @param message
     * @return
     */
    public static <T> ResponseEntity<Body<T>> badRequest(String message, String code) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Body.<T>builder().formatDate(new Date()).status(HttpStatus.BAD_REQUEST.value())
                .code(code)
                .message(message).build());
    }

    /**
     * 参数异常
     *
     * @param message
     * @param errors
     * @param <T>
     * @return
     */
    public static <T> ResponseEntity<Body<T>> badRequestWithErrors(String message, String errors) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Body.<T>builder().formatDate(new Date()).status(HttpStatus.BAD_REQUEST.value())
                .errors(errors)
                .message(message).build());
    }

    /**
     * 正常(自定义status_code)
     *
     * @param message
     * @return
     */
    public static <T> ResponseEntity<Body<T>> ok(String message, String code) {
        return ResponseEntity.status(HttpStatus.OK).body(Body.<T>builder().formatDate(new Date())
                .status(HttpStatus.OK.value())
                .success(true)
                .code(code).message(message).build());
    }


    /**
     * 接口逻辑出错，接口异常
     * 通常是runtimeException
     *
     * @param message
     * @return
     */
    public static <T> ResponseEntity<Body<T>> internalError(String message) {
        return internalError(message, null);
    }

    /**
     * 接口逻辑出错，接口异常
     * 通常是runtimeException
     *
     * @param message
     * @return
     */
    public static <T> ResponseEntity<Body<T>> internalError(String message, String errors) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Body.<T>builder().message(message).formatDate(new Date())
                        .errors(errors)
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value()).build());
    }

    /**
     * 新增对象创建成功
     *
     * @param data    新增的对象
     * @param message
     * @param <T>
     * @return
     */
    public static <T> ResponseEntity<Body<T>> created(T data, String message) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Body.<T>builder().formatDate(new Date()).data(data).status(HttpStatus.CREATED.value()).message(message).build());
    }

    /**
     * 未登录
     *
     * @return
     */
    public static <T> ResponseEntity<Body<T>> unauthorized() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Body.<T>builder().formatDate(new Date()).status(HttpStatus.UNAUTHORIZED.value()).message("未登录认证").build());
    }

    /**
     * 无权限
     *
     * @return
     */
    public static <T> ResponseEntity<Body<T>> forbidden() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Body.<T>builder().formatDate(new Date()).status(HttpStatus.FORBIDDEN.value()).message("无权限").build());
    }


    /**
     * 自定义
     *
     * @return
     */
    public static ResponseEntity<Body> withBody(Body responseBody) {
        return ResponseEntity.status(responseBody.getStatus()).body(responseBody);
    }


    /**
     * 自定义返回状态
     *
     * @return
     */
    public static <T> ResponseEntity<Body<T>> withStatus(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Body.<T>builder().formatDate(new Date()).status(status.value()).message(message).code(status.name()).build());
    }

    /**
     * 服务下线，
     * 服务断连
     *
     * @return
     */
    public static <T> ResponseEntity<Body<T>> serviceUnavailable(String applicationName) {
        return ResponseEntity.status(SERVICE_UNAVAILABLE)
                .body(Body.<T>builder().formatDate(new Date()).status(SERVICE_UNAVAILABLE.value()).message(applicationName + " " + SERVICE_UNAVAILABLE.getReasonPhrase()).build());
    }

    public static <T> ResponseEntity<Body<T>> notAcceptable(String message) {
        return ResponseEntity.status(NOT_ACCEPTABLE)
                .body(Body.<T>builder().formatDate(new Date()).status(NOT_ACCEPTABLE.value()).message(message).build());
    }

    private static ErrorJson parseErrorMessage(String message) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        int i = message.indexOf("\n");
        if (i > 0) {
            return mapper.readValue(message.substring(i), ErrorJson.class);
        }
        return null;
    }

    private static <T> ResponseEntity<Body<T>> formErrorJson(ErrorJson errorJson) {
        return ResponseEntity.status(errorJson.getStatus())
                .body(Body.<T>builder().formatDate(new Date()).status(errorJson.getStatus().value()).message(errorJson.getMessage()).build());
    }


    private static <T> ResponseEntity<Body<T>> timeOut(String message) {
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                .body(Body.<T>builder().formatDate(new Date()).status(HttpStatus.REQUEST_TIMEOUT.value()).message(message).build());
    }

}
