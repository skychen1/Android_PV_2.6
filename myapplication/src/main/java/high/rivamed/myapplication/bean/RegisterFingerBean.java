package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @作者: 钱凯
 * @创建时间: 2018/8/2
 * @功能描述:
 */
public class RegisterFingerBean implements Serializable {

    /**
     * operateSuccess : true
     * id : 0
     * msg : registerFinger
     * userFeatureInfos : [{"userId":"8a80cb8164d9b3940164da1bff760003","type":"1","data":"1111111111111111111111"},{"userId":"8a80cb8164d9b3940164da1bff760003","type":"1","data":"1111111111111111111113"},{"userId":"8a80cb8164d9b3940164da1bff760003","type":"1","data":"1111111111111111111112"}]
     */

    private boolean operateSuccess;
    private int id;
    private String msg;
    private List<UserFeatureInfosBean> userFeatureInfos;

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

    public List<UserFeatureInfosBean> getUserFeatureInfos() {
        return userFeatureInfos;
    }

    public void setUserFeatureInfos(List<UserFeatureInfosBean> userFeatureInfos) {
        this.userFeatureInfos = userFeatureInfos;
    }

    public static class UserFeatureInfosBean {
        /**
         * userId : 8a80cb8164d9b3940164da1bff760003
         * type : 1
         * data : 1111111111111111111111
         */

        private String userId;
        private String type;
        private String data;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
