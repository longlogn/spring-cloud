package com.cxlsky.utils.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author DELL
 */
@Data
@Builder
@AllArgsConstructor
public class Body<T> implements Serializable {

    private static final String RPC_ERROR_CODE = "RPC_ERROR";

    private boolean success = false;
    /**
     * 返回的数据
     */
    private T data;
    /**
     * 自定义返回的消息
     */
    private String message;

    /**
     * http状态码
     */
    private int status;

    /**
     * 可区分的错误状态码
     */
    private String code;

    private String errors;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date formatDate;

    public Body(T data, String message, int status, String code) {
        this.data = data;
        this.message = message;
        this.status = status;
        this.code = code;
    }

    public Body(T data, String message, int status, String code, String errors) {
        this.data = data;
        this.message = message;
        this.status = status;
        this.code = code;
        this.errors = errors;
    }

    private Body() {
    }

    /**
     * 生成一个rpcError对象
     *
     * @return
     */
    public static Body rpcError() {
        return Body.builder()
                .success(false)
                .code(RPC_ERROR_CODE)
                .message("远程调用出错")
                .build();
    }

    /**
     * 判断对象是否是rpcError对象
     *
     * @return
     */
    @JsonIgnore
    public boolean isRpcErrorObj() {
        return !success && RPC_ERROR_CODE.equals(code);
    }

}
