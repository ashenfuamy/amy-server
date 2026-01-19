package site.ashenstation.modules.mda.service;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import net.bramp.ffmpeg.progress.Progress;
import net.bramp.ffmpeg.progress.ProgressListener;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.ashenstation.entity.*;
import site.ashenstation.enums.MdaSummaryType;
import site.ashenstation.enums.MdaTagType;
import site.ashenstation.enums.UploadStatus;
import site.ashenstation.exception.BadRequestException;
import site.ashenstation.mapper.*;
import site.ashenstation.modules.mda.dto.CreateVideoDto;
import site.ashenstation.modules.mda.dto.UploadChunkDto;
import site.ashenstation.modules.mda.dto.VideoTaskDto;
import site.ashenstation.modules.mda.dto.VideoTranCodingDto;
import site.ashenstation.modules.mda.vo.VideoTaskVo;
import site.ashenstation.modules.security.service.WebSocketServer;
import site.ashenstation.properties.FileProperties;
import site.ashenstation.properties.VideoTranscodingTaskMQProperties;
import site.ashenstation.utils.FFmpegUtils;
import site.ashenstation.utils.RedisUtils;
import site.ashenstation.utils.SecurityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final MdaSummaryMapper summaryMapper;
    private final FileProperties fileProperties;
    private final SummaryService summaryService;
    private final MdaTagMapper tagMapper;
    private final RedisUtils redisUtils;
    private final RabbitTemplate rabbitTemplate;
    private final VideoTranscodingTaskMQProperties videoTranscodingTaskMQProperties;
    private final MdaVideoMapper mdaVideoMapper;
    private final MdaSummaryTagMapper summaryTagMapper;
    private final MdaSummaryActorMapMapper mdaSummaryActorMapMapper;
    private final UserResourcePermissionMapper userResourcePermissionMapper;
    private final FFmpegUtils fFmpegUtils;


    @Transactional
    public VideoTaskVo createVideo(CreateVideoDto dto) {

        MdaSummary summary = summaryService.createSummary(dto);
        summary.setType(MdaSummaryType.VIDEO);

        List<MdaTag> tags = dto.getTags();

        tags.forEach(t -> {
            if (t.getId() == null) {
                t.setType(MdaTagType.ADULT);
                tagMapper.insert(t);
            }
        });

        VideoTaskDto videoTaskDto = new VideoTaskDto();
        videoTaskDto.setSummary(summary);
        videoTaskDto.setTags(tags);

        videoTaskDto.setActors(dto.getActors());
        videoTaskDto.setSize(dto.getSize());
        videoTaskDto.setTotalChunk(dto.getTotalChunk());
        videoTaskDto.setExt(dto.getExt());
        videoTaskDto.setSerialId(null);

        String resourceId = IdUtil.fastSimpleUUID();

        videoTaskDto.setResourceId(resourceId);

        String videoName = IdUtil.fastSimpleUUID() + dto.getExt() + ".temp";
        File videoFolderFile = new File(fileProperties.getVideoRoot(), resourceId);

        videoFolderFile.mkdir();

        File videoNameFile = new File(videoFolderFile, videoName);
        videoTaskDto.setTmpPath(videoNameFile.getAbsolutePath());

        videoTaskDto.setUploadStatus(UploadStatus.UPLOADING);

        redisUtils.set(fileProperties.getUploadCacheKey() + resourceId, videoTaskDto);

        return new VideoTaskVo(resourceId, 0);
    }

    public void uploadChunk(UploadChunkDto dto) {
        VideoTaskDto videoTaskDto = redisUtils.get(fileProperties.getUploadCacheKey() + dto.getId(), VideoTaskDto.class);
        if (videoTaskDto == null) {
            throw new BadRequestException("");
        }

        Integer lastChunk = videoTaskDto.getCurrentChunk();

        Integer currentChunk = lastChunk + 1;

        if (currentChunk.equals(dto.getCurrentChunk())) {
            String tmpPath = videoTaskDto.getTmpPath();

            try {
                FileOutputStream fou = new FileOutputStream(tmpPath, true);
                byte[] buffer = new byte[1024];
                int bytesRead;

                InputStream inputStream = dto.getChunk().getInputStream();

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fou.write(buffer, 0, bytesRead);
                }

                inputStream.close();
                fou.close();

                videoTaskDto.setCurrentChunk(dto.getCurrentChunk());
                redisUtils.set(fileProperties.getUploadCacheKey() + dto.getId(), videoTaskDto);
            } catch (IOException e) {
                throw new BadRequestException(e.getMessage());
            }
        }
    }

    public UploadStatus queryUploadStatus(String id) {
        try {
            VideoTaskDto task = redisUtils.get(fileProperties.getUploadCacheKey() + id, VideoTaskDto.class);
            UploadStatus uploadStatus = task.getUploadStatus();

            if (uploadStatus == UploadStatus.FINISH) {
                redisUtils.del(fileProperties.getUploadCacheKey() + id);
            }

            return uploadStatus;
        } catch (Exception e) {
            return UploadStatus.FINISH;
        }
    }


    public void finishUpload(String id) {
        VideoTaskDto task = redisUtils.get(fileProperties.getUploadCacheKey() + id, VideoTaskDto.class);
        task.setUploadStatus(UploadStatus.TRANSCODING);

        redisUtils.set(fileProperties.getUploadCacheKey() + id, task);
        VideoTranCodingDto videoTranCodingDto = new VideoTranCodingDto(id, Integer.valueOf(SecurityUtils.getCurrentUserId()));

        rabbitTemplate.convertAndSend(videoTranscodingTaskMQProperties.getExchangeName(), videoTranscodingTaskMQProperties.getRoutingKey(), JSONObject.toJSONString(videoTranCodingDto));
    }


    @RabbitListener(queues = "transcoding_video-queue")
    @Transactional
    public void receiveProdMessage(String msg) {

        VideoTranCodingDto videoTranCodingDto = JSONObject.parseObject(msg, VideoTranCodingDto.class);

        WebSocketServer webSocketServer = WebSocketServer.webSocketSet.get(String.valueOf(videoTranCodingDto.getUserId()));

        try {
            VideoTaskDto task = redisUtils.get(fileProperties.getUploadCacheKey() + videoTranCodingDto.getTaskId(), VideoTaskDto.class);

            String tempAbsolutePath = task.getTmpPath();
            String videoPath = tempAbsolutePath.replace(".temp", "");
            File videoFile = new File(videoPath);

            new File(tempAbsolutePath).renameTo(videoFile);

            Long duration = fFmpegUtils.getDuration(videoPath);

            ProgressListener progressListener = new ProgressListener() {
                final double duration_ns = duration * TimeUnit.SECONDS.toNanos(1);

                @Override
                public void progress(Progress progress) {
                    double percentage = progress.out_time_ns / duration_ns;

                    if (webSocketServer != null) {
                        try {
                            webSocketServer.sendMessage(JSONObject.toJSONString(new HashMap<>() {{
                                put("event", "upload.transcoding");
                                put("args", videoTranCodingDto.getTaskId() + "," + String.valueOf(percentage * 100));
                            }}));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            };

            File transFileTemp = videoFile;
            if (!FileNameUtil.extName(videoFile.getAbsoluteFile()).equals("mp4")) {
                transFileTemp = fFmpegUtils.conversionToMP4(videoPath, progressListener);
            }

            transFileTemp = fFmpegUtils.conversionToTs(transFileTemp.getAbsolutePath(), progressListener);
            File m38u = fFmpegUtils.conversionToM38u(transFileTemp.getAbsolutePath(), progressListener);


            MdaSummary summary = task.getSummary();

            summaryMapper.insert(summary);

            MdaVideo mdaVideo = new MdaVideo();
            mdaVideo.setSize(task.getSize());
            mdaVideo.setConverted(true);
            mdaVideo.setSeriesId(null);

            mdaVideo.setFilePath(m38u.getAbsolutePath());
            mdaVideo.setParentFilePath(m38u.getParentFile().getAbsolutePath());
            mdaVideo.setRelativePath(fileProperties.getVideoResourcePrefix() + "/" + task.getResourceId() + "/" + m38u.getName());
            mdaVideo.setSummaryId(summary.getId());
            mdaVideo.setDuration(duration);

            mdaVideoMapper.insert(mdaVideo);

            List<MdaTag> tags = task.getTags();
            ArrayList<MdaSummaryTag> mdaSummaryTags = new ArrayList<>();
            tags.forEach(t -> {
                mdaSummaryTags.add(new MdaSummaryTag(summary.getId(), t.getId()));
            });

            summaryTagMapper.insertBatch(mdaSummaryTags);

            ArrayList<MdaSummaryActorMap> mdaSummaryActorMaps = new ArrayList<>();
            List<Integer> actors = task.getActors();
            actors.forEach(a -> {
                mdaSummaryActorMaps.add(new MdaSummaryActorMap(a, summary.getId()));
            });

            mdaSummaryActorMapMapper.insertBatch(mdaSummaryActorMaps);
            userResourcePermissionMapper.insert(new UserResourcePermission(videoTranCodingDto.getUserId(), task.getResourceId()));

            task.setUploadStatus(UploadStatus.FINISH);

            if (webSocketServer != null) {
                webSocketServer.sendMessage(new HashMap<>() {{
                    put("event", "upload.finish");
                    put("args", videoTranCodingDto.getTaskId());
                }});
            }


            redisUtils.del(fileProperties.getUploadCacheKey() + videoTranCodingDto.getTaskId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
