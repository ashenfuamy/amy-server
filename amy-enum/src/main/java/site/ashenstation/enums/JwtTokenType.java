package site.ashenstation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtTokenType {
    LOGIN("1"),
    Authorization("2");

    private final String type;

    public static JwtTokenType find(String type) {
        for (JwtTokenType value : JwtTokenType.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }
}
