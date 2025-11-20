package site.ashenstation.amyserver.utils.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MosaicType {

    HAS(1),
    NOT(0);

    private final Integer type;

    @EnumValue
    public Integer getType() {
        return type;
    }
}
