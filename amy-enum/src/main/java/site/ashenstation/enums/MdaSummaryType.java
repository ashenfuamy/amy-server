package site.ashenstation.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public enum MdaSummaryType {
    VIDEO(1),
    SERIAL(2);

    private final Integer type;

    @EnumValue
    @JsonValue
    public Integer getType() {
        return type;
    }

    public static MdaSummaryType find(Integer type) {
        for (MdaSummaryType value : MdaSummaryType.values()) {
            if (Objects.equals(value.getType(), type)) {
                return value;
            }
        }
        return null;
    }
}
