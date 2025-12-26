package site.ashenstation.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public enum MdaTagType {
    ADULT(0),
    OTHER(1);

    private final Integer type;

    @EnumValue
    public Integer getType() {
        return type;
    }

    public static MdaTagType find(Integer type) {
        for (MdaTagType value : MdaTagType.values()) {
            if (Objects.equals(value.getType(), type)) {
                return value;
            }
        }
        return null;
    }
}
