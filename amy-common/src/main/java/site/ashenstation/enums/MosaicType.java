package site.ashenstation.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public enum MosaicType {
    MOSAIC(1),
    WITHOUT_MOSAIC(0);

    private final Integer type;

    @EnumValue
    public Integer getType() {
        return type;
    }

    public static MosaicType find(Integer type) {
        for (MosaicType value : MosaicType.values()) {
            if (Objects.equals(value.getType(), type)) {
                return value;
            }
        }
        return null;
    }
}
