package site.steve5634.dockerManageServer.domain;

import com.github.dockerjava.api.model.SearchItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "搜索镜像")
public class SearchImage {

    @Schema(description = "镜像名称")
    private String name;

    @Schema(description = "是否为官方镜像")
    private Integer official;

    @Schema(description = "是否受信任")
    private Integer trusted;

    @Schema(description = "star数")
    private Integer starCount;

    @Schema(description = "镜像描述")
    private String description;

    public SearchImage(SearchItem item){
        this.name = item.getName();
        this.description = item.getDescription();
        this.starCount = item.getStarCount();
        this.official = item.isOfficial() ? 1 : 0;
    }

}
