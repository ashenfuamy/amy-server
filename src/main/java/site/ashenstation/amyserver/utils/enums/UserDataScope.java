package site.ashenstation.amyserver.utils.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserDataScope {
    APP("App"),
    ADMIN("Admin"),
    All("All");

    final String type;

    @EnumValue
    public String getType() {
        return type;
    }

    public static UserDataScope find(String type) {
        for (UserDataScope value : UserDataScope.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return All;
    }
}
