package site.ashenstation.modules.mda.dto;

import lombok.Data;
import lombok.ToString;
import site.ashenstation.entity.MdaSummary;
import site.ashenstation.entity.MdaTag;
import site.ashenstation.enums.UploadStatus;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class VideoTaskDto implements Serializable {
    private MdaSummary summary;
    private List<MdaTag> tags;

    private List<Integer> actors;

    private Long size;
    private Integer serialId;
    private String ext;
    private UploadTask uploadTask;
    private UploadStatus uploadStatus;

    private String resourceId;

    public VideoTaskDto() {
        this.uploadTask = new UploadTask();
        this.uploadTask.setCurrentChunk(0);
    }

    public void setTotalChunk(Integer totalChunk) {
        this.uploadTask.setTotalChunk(totalChunk);
    }

    public void setTmpPath(String tmpPath) {
        this.uploadTask.setTmpPath(tmpPath);
    }

    public void setCurrentChunk(Integer currentChunk) {
        this.uploadTask.setCurrentChunk(currentChunk);
    }

    public Integer getCurrentChunk() {
        return this.uploadTask.getCurrentChunk();
    }

    public String getTmpPath() {
        return this.uploadTask.getTmpPath();
    }

    @Data
    @ToString
    static class UploadTask implements Serializable {
        private String tmpPath;
        private Integer totalChunk;
        private Integer currentChunk;
    }
}
