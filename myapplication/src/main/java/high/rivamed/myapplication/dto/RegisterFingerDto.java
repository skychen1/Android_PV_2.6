package high.rivamed.myapplication.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者: 钱凯
 * @创建时间: 2018/8/2
 * @功能描述:绑定指纹
 */
public class RegisterFingerDto {

    private List<UserFeatureInfosBean> userFeatureInfos = new ArrayList<>();

    public List<UserFeatureInfosBean> getUserFeatureInfos() {
        return userFeatureInfos;
    }

    public void setUserFeatureInfos(List<UserFeatureInfosBean> userFeatureInfos) {
        this.userFeatureInfos = userFeatureInfos;
    }

    public static class UserFeatureInfosBean {
        /**
         * userId : 8a80cb8164d9b3940164da1bff760003
         * data : 1111111111111111111111
         */

        private String differentThings;
        private String userId;
        private String data;

        public String getDifferentThings() {
            return differentThings;
        }

        public void setDifferentThings(String differentThings) {
            this.differentThings = differentThings;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        private String type;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
