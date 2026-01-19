package site.ashenstation.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UploadStatus {
    UPLOADING("uploading"),
    FINISH("finish"),
    TRANSCODING("transcoding");

    private final String type;

    @JsonValue
    public String getType() {
        return type;
    }

    public static UploadStatus find(String type) {
        for (UploadStatus value : UploadStatus.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }
}
