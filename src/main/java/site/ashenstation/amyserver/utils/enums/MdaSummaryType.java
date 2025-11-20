package site.ashenstation.amyserver.utils.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MdaSummaryType {
    VIDEO(1),
    SERIES(2);

    private final Integer type;

    @EnumValue
    public Integer getType() {
        return type;
    }
}
