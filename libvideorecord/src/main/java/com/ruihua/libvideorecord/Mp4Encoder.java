package com.ruihua.libvideorecord;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Build;

import com.rivamed.libdevicesbase.utils.LogUtils;
import com.ruihua.libvideorecord.callback.OnRecordCallback;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

/**
 * describe ：
 *
 * @author : Yich
 * date: 2019/4/9
 */
public class Mp4Encoder {

    private MediaCodec mediaCodec;
    private MediaMuxer mediaMuxer;
    private boolean isRunning;
    private boolean mMuxerStarted;
    private int mFrameRate;
    private int mTrackIndex;
    private ArrayBlockingQueue<byte[]> picData = new ArrayBlockingQueue<>(10);
    private ScheduledThreadPoolExecutor scheduled;
    private OnRecordCallback mRecordCallback;


    public Mp4Encoder(int width, int height, int frameRate, int colorFormat, String filePath) {
        mFrameRate = frameRate;
        MediaFormat mediaFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, width, height);
        mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, colorFormat);
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, width * height);
        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, frameRate);
        mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 10);
        try {
            mediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC);
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            //创建生成MP4初始化对象
            mediaMuxer = new MediaMuxer(file.getAbsolutePath(), MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        } catch (IOException e) {
            //nothing
        }
        mediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        mediaCodec.start();
    }

    public void putDate(byte[] data) {
        if (picData.size() >= 10) {
            picData.poll();
            LogUtils.e("录制视频，丢帧了");
        }
        picData.add(data);
    }

    public void startEncode() {
        ThreadFactory namedThreadFactory = new BasicThreadFactory.Builder().namingPattern("video-record-pool-%d").daemon(true).build();
        if (scheduled == null) {
            scheduled = new ScheduledThreadPoolExecutor(2, namedThreadFactory);
        }
        scheduled.execute(new Runnable() {
            @Override
            public void run() {
                start();
            }
        });
    }

    private void start() {
        final int timeOutUse = 10000;
        isRunning = true;
        long generateIndex = 0;
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        ByteBuffer[] buffers = null;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            buffers = mediaCodec.getInputBuffers();
        }
        byte[] input = null;
        while (isRunning) {
            //有照片数据就录制操作
            if (!picData.isEmpty()) {
                //有数据就进行录制操作
                int inputBufferIndex = mediaCodec.dequeueInputBuffer(timeOutUse);
                if (inputBufferIndex >= 0) {
                    long ptsUsec = computePresentationTime(generateIndex);
                    input = picData.poll();
                    //有效的空的缓存区
                    ByteBuffer inputBuffer;
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                        inputBuffer = buffers[inputBufferIndex];
                    } else {
                        inputBuffer = mediaCodec.getInputBuffer(inputBufferIndex);
                    }
                    if (inputBuffer != null) {
                        inputBuffer.clear();
                        inputBuffer.put(input);
                    }
                    //将数据放到编码队列
                    mediaCodec.queueInputBuffer(inputBufferIndex, 0, input.length, ptsUsec, 0);
                    drainEncoder(false, info);
                    generateIndex++;
                } else {
                    try {
                        Thread.sleep(50);
                    } catch (Exception e) {
                        //nothing
                    }
                }
            } else {
                //如果没有数据就等待50ms再继续
                try {
                    Thread.sleep(50);
                } catch (Exception e) {
                    //nothing
                }
            }
        }
        //结束视频录制
        int inputBuffer;
        while (true) {
            inputBuffer = mediaCodec.dequeueInputBuffer(timeOutUse);
            if (inputBuffer >= 0) {
                break;
            }
            try {
                Thread.sleep(50);
            } catch (Exception e) {
                //nothing
            }
        }
        //输入视屏结尾标识；
        mediaCodec.queueInputBuffer(inputBuffer, 0, 0, computePresentationTime(generateIndex), MediaCodec.BUFFER_FLAG_END_OF_STREAM);
        drainEncoder(true, info);
        //释放资源
        if (mediaCodec != null) {
            mediaCodec.stop();
            mediaCodec.release();
        }
        if (mediaMuxer != null) {
            try {
                if (mMuxerStarted) {
                    mediaMuxer.stop();
                    mediaMuxer.release();
                }
            } catch (Exception e) {
                //nothing
            }
        }
        //录制结束，回调录制成功
        if (mRecordCallback != null) {
            mRecordCallback.onRecordResult(true);
        }
    }

    private void drainEncoder(boolean endOfStream, MediaCodec.BufferInfo bufferInfo) {
        final int timeoutUse = 10000;
        ByteBuffer[] buffers = null;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            buffers = mediaCodec.getOutputBuffers();
        }
        if (endOfStream) {
            try {
                mediaCodec.signalEndOfInputStream();
            } catch (Exception e) {
                //nothing
            }
        }
        while (true) {
            int encoderStatus = mediaCodec.dequeueOutputBuffer(bufferInfo, timeoutUse);
            if (encoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {
                if (!endOfStream) {
                    break;
                }
            } else if (encoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                if (mMuxerStarted) {
                    throw new RuntimeException("format changed twice");
                }
                MediaFormat mediaFormat = mediaCodec.getOutputFormat();
                mTrackIndex = mediaMuxer.addTrack(mediaFormat);
                mediaMuxer.start();
                mMuxerStarted = true;
            } else if (encoderStatus < 0) {
                //nothing
                LogUtils.e("encoderStatus < 0");
            } else {
                ByteBuffer outputBuffer = null;
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                    outputBuffer = buffers[encoderStatus];
                } else {
                    outputBuffer = mediaCodec.getOutputBuffer(encoderStatus);
                }
                if (outputBuffer == null) {
                    throw new RuntimeException("encoderOutputBuffer " + encoderStatus + " was null");
                }
                if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
                    bufferInfo.size = 0;
                }
                if (bufferInfo.size != 0) {
                    if (!mMuxerStarted) {
                        throw new RuntimeException("muxer hasn't started");
                    }
                    outputBuffer.position(bufferInfo.offset);
                    outputBuffer.limit(bufferInfo.offset + bufferInfo.size);
                    try {
                        mediaMuxer.writeSampleData(mTrackIndex, outputBuffer, bufferInfo);
                    } catch (Exception e) {
                        //nothing
                    }
                }
                mediaCodec.releaseOutputBuffer(encoderStatus, false);
                if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    break;
                }
            }
        }
    }

    private long computePresentationTime(long frameIndex) {
        return 132 + frameIndex * 1000000 / mFrameRate;
    }

    /**
     * 停止录制
     */
    public void stopEncode() {
        isRunning = false;
    }

    public void registerOnRecordCallback(OnRecordCallback callback) {
        this.mRecordCallback = callback;
    }


}
