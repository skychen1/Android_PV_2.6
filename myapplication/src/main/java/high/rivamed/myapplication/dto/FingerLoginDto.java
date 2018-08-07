package high.rivamed.myapplication.dto;

/**
 * @作者: 钱凯
 * @创建时间: 2018/8/2
 * @功能描述:
 */
public class FingerLoginDto {

    /**
     * userFeatureInfo : {"data":"1111111111111111111111"}
     * thingCode : 402882a064e4f6e20164e52508ff0024
     */

    private UserFeatureInfoBean userFeatureInfo;
    private String thingCode;

    public UserFeatureInfoBean getUserFeatureInfo() {
        return userFeatureInfo;
    }

    public void setUserFeatureInfo(UserFeatureInfoBean userFeatureInfo) {
        this.userFeatureInfo = userFeatureInfo;
    }

    public String getThingCode() {
        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public static class UserFeatureInfoBean {
        /**
         * data : 1111111111111111111111
         */

        private String data;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
