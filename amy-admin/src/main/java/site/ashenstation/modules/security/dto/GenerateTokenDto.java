package site.ashenstation.modules.security.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class GenerateTokenDto {
    private String title;
    private List<Integer> permissions;
    private Integer expire;
}
