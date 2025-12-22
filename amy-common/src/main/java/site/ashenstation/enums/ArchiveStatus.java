package site.ashenstation.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ArchiveStatus {
    RELEASE(1),
    PRERELEASE(2);

    private final Integer type;

    @EnumValue
    public Integer getType() {
        return type;
    }
}
