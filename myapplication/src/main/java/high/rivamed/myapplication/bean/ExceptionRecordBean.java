package high.rivamed.myapplication.bean;

import java.io.Serializable;

/**
 * 项目名称：高值
 * 创建者：chenyanling
 * 创建时间：2019/5/13
 * 描述：异常处理-异常记录
 */
public class ExceptionRecordBean implements Serializable {
    private String exceptContent;//测试数据:异常内容

    public String getExceptContent() {
        return exceptContent;
    }

    public void setExceptContent(String exceptContent) {
        this.exceptContent = exceptContent;
    }
}
