package site.steve5634.dockerManageServer.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Result<T> {

    private static final Integer SUCCESS_CODE = 200;

    private static final Integer NOT_FOUND_CODE = 404;

    private static final Integer ERROR_CODE = 500;

    private static final String ERROR_MSG = "操作失败";

    private static final String SUCCESS_MSG = "操作成功";

    @Schema(description = "响应数据")
    private T data;

    @Schema(description = "响应代码")
    private Integer code;

    @Schema(description = "响应信息")
    private String message;

    private Result(){}

    public static <T> Result<T> success(){
        return success(SUCCESS_MSG);
    }

    public static <T> Result<T> success(T data){
        return success(data,SUCCESS_MSG);
    }

    public static <T> Result<T> success(String message){
        return success(null,message);
    }

    public static <T> Result<T> success(T data, String message){
        Result<T> result = new Result<>();
        result.setCode(SUCCESS_CODE);
        result.setData(data);
        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> error(){
        return success(ERROR_MSG);
    }

    public static <T> Result<T> error(T data){
        return success(data,ERROR_MSG);
    }

    public static <T> Result<T> error(String message){
        return success(null,message);
    }

    public static <T> Result<T> error(T data, String message){
        Result<T> result = new Result<>();
        result.setCode(ERROR_CODE);
        result.setData(data);
        result.setMessage(message);
        return result;
    }

}
