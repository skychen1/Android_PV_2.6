package high.rivamed.myapplication.bean;

import java.io.Serializable;

/**
 * 项目名称：高值
 * 创建者：chenyanling
 * 创建时间：2019/5/13
 * 描述：异常处理-操作人数据
 */
public class ExceptionOperatorBean implements Serializable {
    private String operator;
    private String Id;
    private boolean isCheck;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
