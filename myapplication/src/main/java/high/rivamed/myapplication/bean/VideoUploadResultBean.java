package high.rivamed.myapplication.bean;

/**
 * 项目名称:    Android_PV_2.6.6 425c
 * 创建者:      Chenyanling
 * 创建时间:    2019/3/6 14:16
 * 描述:        视频上传结果
 * 包名:        high.rivamed.myapplication.bean
 */
public class VideoUploadResultBean {

    /**
     * operateSuccess : true
     * id : 0
     * msg : 上传成功
     * video : {"videoId":"40288ba96955b250016955d907840001","videoName":"VID_20190307_095114.mp4","thingId":"40288ba9692c936901692cfb6e52000a","sthId":"40288b5f6710d706016710f3a65b0001","deviceId":"40288ba9692c936901692cfb72d0000f","startDate":"2019-03-07 09:51","endDate":"2019-03-07 09:51"}
     */

    private boolean operateSuccess;
    private int id;
    private String msg;
    private VideoBean video;

    public boolean isOperateSuccess() {
        return operateSuccess;
    }

    public void setOperateSuccess(boolean operateSuccess) {
        this.operateSuccess = operateSuccess;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public VideoBean getVideo() {
        return video;
    }

    public void setVideo(VideoBean video) {
        this.video = video;
    }

    public static class VideoBean {
        /**
         * videoId : 40288ba96955b250016955d907840001
         * videoName : VID_20190307_095114.mp4
         * thingId : 40288ba9692c936901692cfb6e52000a
         * sthId : 40288b5f6710d706016710f3a65b0001
         * deviceId : 40288ba9692c936901692cfb72d0000f
         * startDate : 2019-03-07 09:51
         * endDate : 2019-03-07 09:51
         */

        private String videoId;
        private String videoName;
        private String thingId;
        private String sthId;
        private String deviceId;
        private String startDate;
        private String endDate;

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }

        public String getVideoName() {
            return videoName;
        }

        public void setVideoName(String videoName) {
            this.videoName = videoName;
        }

        public String getThingId() {
            return thingId;
        }

        public void setThingId(String thingId) {
            this.thingId = thingId;
        }

        public String getSthId() {
            return sthId;
        }

        public void setSthId(String sthId) {
            this.sthId = sthId;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }
    }
}
