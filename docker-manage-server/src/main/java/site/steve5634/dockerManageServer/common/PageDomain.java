package site.steve5634.dockerManageServer.common;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PageDomain {

    @NotNull(message = "pageNum 不能为空")
    @Min(message = "pageNum 最小为1", value = 1)
    private Integer pageNum;

    @NotNull(message = "pageSize 不能为空")
    @Min(message = "pageSize 最小为1", value = 1)
    private Integer pageSize;

}
