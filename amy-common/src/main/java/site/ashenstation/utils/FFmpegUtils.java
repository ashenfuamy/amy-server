package site.ashenstation.utils;

import cn.hutool.core.io.file.FileNameUtil;
import lombok.RequiredArgsConstructor;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.progress.ProgressListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import site.ashenstation.properties.FFmpegProperties;

import java.io.File;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FFmpegUtils implements InitializingBean {

    private final FFmpegProperties fFmpegProperties;
    private FFmpeg ffmpeg;
    private FFprobe ffprobe;
    private FFmpegExecutor executor;


    @Override
    public void afterPropertiesSet() throws Exception {
        ffprobe = new FFprobe(fFmpegProperties.getFfprobeExecutorPath());
        ffmpeg = new FFmpeg(fFmpegProperties.getFfmpegExecutorPath());

        executor = new FFmpegExecutor(ffmpeg, ffprobe);
    }

    public Long getDuration(String videoPath) throws IOException {
        FFmpegProbeResult probe = ffprobe.probe(videoPath);

        return Math.round(probe.getFormat().duration);
    }


    public File conversionToMP4(String videoPath, ProgressListener progressListener) {

        File videoFile = new File(videoPath);
        String mainName = FileNameUtil.mainName(videoFile);

        String absolutePath = videoFile.getParentFile().getAbsolutePath();
        File resFile = new File(absolutePath, mainName + ".mp4");

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(videoPath)
                .addOutput(resFile.getAbsolutePath())
                .addExtraArgs("-c:v", "libx264", "-b:v", "2M", "-preset", "medium", "-crf", "18", "-c:a", "aac", "-b:a", "320k")
                .done();

        executor.createJob(builder, progressListener).run();
        return resFile;
    }


    public File conversionToTs(String videoPath, ProgressListener progressListener) {

        File videoFile = new File(videoPath);
        String mainName = FileNameUtil.mainName(videoFile);

        String absolutePath = videoFile.getParentFile().getAbsolutePath();
        File tsFile = new File(absolutePath, mainName + ".ts");

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(videoPath)
                .addOutput(tsFile.getAbsolutePath())
                .addExtraArgs("-y", "-vcodec", "copy", "-acodec", "copy")
                .done();

        executor.createJob(builder, progressListener).run();
        return tsFile;
    }


    public File conversionToM38u(String videoPath, ProgressListener progressListener) {
        File videoFile = new File(videoPath);
        String mainName = FileNameUtil.mainName(videoFile);

        String absolutePath = videoFile.getParentFile().getAbsolutePath();
        File m3u8File = new File(absolutePath, mainName + ".m3u8");

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(videoPath)
                .overrideOutputFiles(true)
                .addOutput(m3u8File.getAbsolutePath())
                .setFormat("hls")
                .addExtraArgs("-c", "copy")
                .addExtraArgs("-map", "0")
                .addExtraArgs("-hls_time", "10") // 每个TS切片的目标时长（单位：秒），例如10秒
                .addExtraArgs("-hls_list_size", "0")
                .addExtraArgs("-hls_segment_filename", m3u8File.getAbsolutePath().replace(".m3u8", "_%03d.ts"))
                .done();

        executor.createJob(builder, progressListener).run();
        return m3u8File;
    }
}
