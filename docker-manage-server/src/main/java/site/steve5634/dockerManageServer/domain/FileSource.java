package site.steve5634.dockerManageServer.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("file_source")
@Schema(description = "文件名")
public class FileSource {

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键id")
    private String id;

    @Schema(description = "文件名")
    private String name;

    @Schema(description = "文件路径")
    private String path;

    @Schema(description = "大小")
    private BigDecimal size;

    @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyy-MM-dd HH:mm:ss")
    @Schema(name = "创建时间")
    private Date createTime;

    @TableLogic
    @Schema(name = "逻辑删除")
    private Integer delFlag;

}
