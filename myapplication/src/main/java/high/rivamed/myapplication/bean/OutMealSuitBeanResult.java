package high.rivamed.myapplication.bean;

import java.io.Serializable;

/**
 * @ProjectName: new2.6.3
 * @Package: high.rivamed.myapplication.bean
 * @ClassName: OutMealSuitBeanResult
 * @Description: 套组列表返回
 * @Author: Amos_Bo
 * @CreateDate: 2018/10/30 14:03
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/10/30 14:03
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class OutMealSuitBeanResult  implements Serializable{


    /**
     * id : 1
     * createTime : 2018-09-17 10:28:43
     * creatorId : null
     * creatorName : null
     * deptId : 1025
     * planName : 套组1
     * pym : null
     * remark : null
     * status : 1
     * updateTime : 2018-09-17 10:28:43
     */

    private int id;
    private String createTime;
    private Object creatorId;
    private Object creatorName;
    private String deptId;
    private String planName;
    private Object pym;
    private Object remark;
    private int status;
    private String updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Object getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Object creatorId) {
        this.creatorId = creatorId;
    }

    public Object getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(Object creatorName) {
        this.creatorName = creatorName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public Object getPym() {
        return pym;
    }

    public void setPym(Object pym) {
        this.pym = pym;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
