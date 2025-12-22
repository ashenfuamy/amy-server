package site.ashenstation.modules.security.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class SetPermissionDto {
    private Integer adminId;
    private List<Integer> permissions;
}
