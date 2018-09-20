package high.rivamed.myapplication.bean;

/**
 * @作者: 钱凯
 * @创建时间: 2018/8/14
 * @功能描述:
 */
public class SelectBean {
    private String id = "";
    private String content = "";

    public SelectBean(String content,String id) {
        this.content = content;
        this.id = id;
    }

    public SelectBean(String content) {
        this.content = content;

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
