package high.rivamed.myapplication.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者: 钱凯
 * @创建时间: 2018/10/31
 * @功能描述:
 */
public class PendingTaskBean {

    /**
     * id : 0
     * accountId : 1
     * userId : 1
     * messages : [{"id":"1","type":"医嘱单未领取耗材","text":"1","detail":"1","createTime":"1","accountId":"1"},{"id":"2","type":"套组未领取耗材","text":"1","detail":"1","createTime":"1","accountId":"1"}]
     */

    private int id;
    private String accountId;
    private String userId;
    private List<MessagesBean> messages = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<MessagesBean> getMessages() {
        return messages;
    }

    public void setMessages(List<MessagesBean> messages) {
        this.messages = messages;
    }

    public static class MessagesBean {
        /**
         * id : 1
         * type : 医嘱单未领取耗材
         * text : 1
         * detail : 1
         * createTime : 1
         * accountId : 1
         */

        private String id = "";
        private String type = "";
        private String text = "";
        private String detail = "";
        private String createTime;
        private String accountId = "";
        private String title = "";

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }
    }
}
