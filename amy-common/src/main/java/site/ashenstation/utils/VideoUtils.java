package site.ashenstation.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.stereotype.Component;
import site.ashenstation.properties.FFmpegProperties;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Component

@RequiredArgsConstructor
public class VideoUtils {
    private final FFmpegProperties fFmpegProperties;

    public Long getDuration(String videoPath) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(byteArrayOutputStream, System.err);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setStreamHandler(pumpStreamHandler);

        CommandLine cmdLine2 = new CommandLine(fFmpegProperties.getFfprobeExecutorPath());

        System.out.println(cmdLine2);

        cmdLine2.addArgument("-v");
        cmdLine2.addArgument("error");
        cmdLine2.addArgument("-show_entries");
        cmdLine2.addArgument("format=duration");
        cmdLine2.addArgument("-of");
        cmdLine2.addArgument("default=noprint_wrappers=1:nokey=1");
        cmdLine2.addArgument(videoPath);

        executor.execute(cmdLine2);

        String string = byteArrayOutputStream.toString("GBK");

        double v = Double.parseDouble(string);

        return Math.round(v);
    }

    public File conversionToMP4(String filePath) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(byteArrayOutputStream, System.err);

        CommandLine cmdLine = new CommandLine(fFmpegProperties.getFfmpegExecutorPath());

        File file = new File(filePath);
        String targetFileName = FileNameUtil.mainName(file) + ".mp4";

        DefaultExecutor executor = new DefaultExecutor();
        executor.setWorkingDirectory(file.getParentFile());

        executor.setStreamHandler(pumpStreamHandler);

        cmdLine.addArgument("-i");
        cmdLine.addArgument(file.getName());
        cmdLine.addArgument("-c:v");
        cmdLine.addArgument("libx264");
        cmdLine.addArgument("-b:v");
        cmdLine.addArgument("2M");
        cmdLine.addArgument("-preset");
        cmdLine.addArgument("medium");
        cmdLine.addArgument("-crf");
        cmdLine.addArgument("18");
        cmdLine.addArgument("-c:a");
        cmdLine.addArgument("aac");
        cmdLine.addArgument("-b:a");
        cmdLine.addArgument("320k");

        cmdLine.addArgument(targetFileName);

        executor.execute(cmdLine);

        return new File(file.getParentFile(), targetFileName);
    }

    public File conversionToTs(String filePath) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(byteArrayOutputStream, System.err);

        File file = new File(filePath);

        DefaultExecutor executor = new DefaultExecutor();
        executor.setWorkingDirectory(file.getParentFile());
        executor.setStreamHandler(pumpStreamHandler);

        CommandLine cmdLine = new CommandLine(fFmpegProperties.getFfmpegExecutorPath());

        File tsFile = new File(file.getParentFile(), FileUtil.mainName(file) + ".ts");

        cmdLine.addArgument("-y");
        cmdLine.addArgument("-i");
        cmdLine.addArgument(file.getName());
        cmdLine.addArgument("-vcodec");
        cmdLine.addArgument("copy");
        cmdLine.addArgument("-acodec");
        cmdLine.addArgument("copy");
        cmdLine.addArgument(tsFile.getName());

        executor.execute(cmdLine);

        return tsFile;
    }

    public File conversionToM38u(String filePath) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(byteArrayOutputStream, System.err);


        CommandLine cmdLine = new CommandLine(fFmpegProperties.getFfmpegExecutorPath());

        String targetFileName = FileNameUtil.mainName(filePath) + ".m3u8";
        File file = new File(filePath);

        cmdLine.addArgument("-i");
        cmdLine.addArgument(file.getName());
        cmdLine.addArgument("-c");
        cmdLine.addArgument("copy");
        cmdLine.addArgument("-map");
        cmdLine.addArgument("0");
        cmdLine.addArgument("-f");
        cmdLine.addArgument("segment");
        cmdLine.addArgument("-segment_list");
        cmdLine.addArgument(targetFileName);
        cmdLine.addArgument("-segment_time");
        cmdLine.addArgument("10");
        cmdLine.addArgument("15s_%3d.ts");

        DefaultExecutor executor = new DefaultExecutor();
        executor.setWorkingDirectory(file.getParentFile());

        executor.setStreamHandler(pumpStreamHandler);

        executor.execute(cmdLine);

        return new File(file.getParentFile(), targetFileName);
    }
}
