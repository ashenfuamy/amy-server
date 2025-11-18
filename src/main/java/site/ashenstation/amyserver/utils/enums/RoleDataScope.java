package site.ashenstation.amyserver.utils.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum RoleDataScope {
    APP("App"),
    ADMIN("Admin"),
    All("All");

    final String type;

    @EnumValue
    public String getType() {
        return type;
    }

    public static RoleDataScope find(String type) {
        for (RoleDataScope value : RoleDataScope.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return All;
    }
}
