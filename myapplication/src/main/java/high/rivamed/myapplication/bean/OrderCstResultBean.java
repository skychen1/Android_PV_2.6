package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: new2.6.3
 * @Package: high.rivamed.myapplication.bean
 * @ClassName: OrderCstResultBean
 * @Description: 套组领用--套餐所包含耗材
 * @Author: Amos_Bo
 * @CreateDate: 2018/10/30 15:53
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/10/30 15:53
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class OrderCstResultBean implements Serializable {


    /**
     * id : 0
     * cstPlan : {"id":5,"createTime":null,"creatorId":null,"creatorName":null,"deptId":null,"planName":null,"pym":null,"remark":null,"status":null,"updateTime":null}
     * thingId : ff80818165cdf1810165d0e4a8fe082b
     * suiteVos : [{"id":null,"deptId":null,"deptName":null,"planName":null,"status":null,"updateTime":null,"createTime":null,"cstId":"4028829265f49c6d0165f4a7ff25019e","cstName":"颅内支架系统(LVIS)","cstSpec":"213041-CAS","udiCode":"要得就是长，要得就是长，要得就是长，要得就是长，要得就是长，要得就是长，","manuName":"法国 MicroVention Europe","vendorName":null,"suiteNum":2,"thingId":null,"deviceId":null,"deviceIds":[],"deviceName":null,"deviceNames":[],"oneDeviceCount":null,"totalCount":0,"inventoryEnough":false},{"id":null,"deptId":null,"deptName":null,"planName":null,"status":null,"updateTime":null,"createTime":null,"cstId":"4028829265f49c6d0165f4a7ff25019d","cstName":"一次性针灸针","cstSpec":"0.25*40","udiCode":"要得就是长，要得就是长，要得就是长，要得就是长，要得就是长，要得就是长，","manuName":"北京中研太和医疗器械公司","vendorName":null,"suiteNum":5,"thingId":null,"deviceId":null,"deviceIds":[],"deviceName":null,"deviceNames":[],"oneDeviceCount":null,"totalCount":0,"inventoryEnough":false},{"id":null,"deptId":null,"deptName":null,"planName":null,"status":null,"updateTime":null,"createTime":null,"cstId":"4028829265f49c6d0165f4a7ff2501bf","cstName":"一次性针灸针","cstSpec":"0.35*100","udiCode":"要得就是长，要得就是长，要得就是长，要得就是长，要得就是长，要得就是长，","manuName":"北京中研太和医疗器械公司","vendorName":null,"suiteNum":2,"thingId":null,"deviceId":null,"deviceIds":[],"deviceName":null,"deviceNames":[],"oneDeviceCount":null,"totalCount":0,"inventoryEnough":false}]
     * countNum : 9
     * kindsOfCst : 3
     */

    private int                id;
    private CstPlanBean        cstPlan;
    private String             msg;
    private String             suiteId;
    private String             thingId;
    private int                countNum;
    private int                kindsOfCst;
    private boolean            operateSuccess;
    private List<SuiteVosBean> suiteVos;

    public String getSuiteId() {
        return suiteId;
    }

    public void setSuiteId(String suiteId) {
        this.suiteId = suiteId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

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

    public CstPlanBean getCstPlan() {
        return cstPlan;
    }

    public void setCstPlan(CstPlanBean cstPlan) {
        this.cstPlan = cstPlan;
    }

    public String getThingId() {
        return thingId;
    }

    public void setThingId(String thingId) {
        this.thingId = thingId;
    }

    public int getCountNum() {
        return countNum;
    }

    public void setCountNum(int countNum) {
        this.countNum = countNum;
    }

    public int getKindsOfCst() {
        return kindsOfCst;
    }

    public void setKindsOfCst(int kindsOfCst) {
        this.kindsOfCst = kindsOfCst;
    }

    public List<SuiteVosBean> getSuiteVos() {
        return suiteVos;
    }

    public void setSuiteVos(List<SuiteVosBean> suiteVos) {
        this.suiteVos = suiteVos;
    }

    public static class CstPlanBean {
        /**
         * id : 5
         * createTime : null
         * creatorId : null
         * creatorName : null
         * deptId : null
         * planName : null
         * pym : null
         * remark : null
         * status : null
         * updateTime : null
         */

        private int id;
        private String createTime;
        private String creatorId;
        private String creatorName;
        private String deptId;
        private String planName;
        private String pym;
        private String remark;
        private String status;
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

        public String getCreatorId() {
            return creatorId;
        }

        public void setCreatorId(String creatorId) {
            this.creatorId = creatorId;
        }

        public String getCreatorName() {
            return creatorName;
        }

        public void setCreatorName(String creatorName) {
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

        public String getPym() {
            return pym;
        }

        public void setPym(String pym) {
            this.pym = pym;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }

    public static class SuiteVosBean {
        /**
         * id : null
         * deptId : null
         * deptName : null
         * planName : null
         * status : null
         * updateTime : null
         * createTime : null
         * cstId : 4028829265f49c6d0165f4a7ff25019e
         * cstName : 颅内支架系统(LVIS)
         * cstSpec : 213041-CAS
         * udiCode : 要得就是长，要得就是长，要得就是长，要得就是长，要得就是长，要得就是长，
         * manuName : 法国 MicroVention Europe
         * vendorName : null
         * suiteNum : 2
         * thingId : null
         * deviceId : null
         * deviceIds : []
         * deviceName : null
         * deviceNames : []
         * oneDeviceCount : null
         * totalCount : 0
         * inventoryEnough : false
         */

        private String       id;
        private String       deptId;
        private String       deptName;
        private String       planName;
        private String       status;
        private String       updateTime;
        private String       createTime;
        private String       cstId;
        private String       cstName;
        private String       cstSpec;
        private String       udiCode;
        private String       manuName;
        private String       vendorName;
        private int          suiteNum;
        private String       thingId;
        private String       deviceId;
        private String       deviceName;
        private String       oneDeviceCount;
        private int          totalCount;
        private boolean      inventoryEnough;
        private List<String> deviceIds;
        private List<String> deviceNames;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDeptId() {
            return deptId;
        }

        public void setDeptId(String deptId) {
            this.deptId = deptId;
        }

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public String getPlanName() {
            return planName;
        }

        public void setPlanName(String planName) {
            this.planName = planName;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getCstId() {
            return cstId;
        }

        public void setCstId(String cstId) {
            this.cstId = cstId;
        }

        public String getCstName() {
            return cstName;
        }

        public void setCstName(String cstName) {
            this.cstName = cstName;
        }

        public String getCstSpec() {
            return cstSpec;
        }

        public void setCstSpec(String cstSpec) {
            this.cstSpec = cstSpec;
        }

        public String getUdiCode() {
            return udiCode;
        }

        public void setUdiCode(String udiCode) {
            this.udiCode = udiCode;
        }

        public String getManuName() {
            return manuName;
        }

        public void setManuName(String manuName) {
            this.manuName = manuName;
        }

        public String getVendorName() {
            return vendorName;
        }

        public void setVendorName(String vendorName) {
            this.vendorName = vendorName;
        }

        public int getSuiteNum() {
            return suiteNum;
        }

        public void setSuiteNum(int suiteNum) {
            this.suiteNum = suiteNum;
        }

        public String getThingId() {
            return thingId;
        }

        public void setThingId(String thingId) {
            this.thingId = thingId;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getOneDeviceCount() {
            return oneDeviceCount;
        }

        public void setOneDeviceCount(String oneDeviceCount) {
            this.oneDeviceCount = oneDeviceCount;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public boolean isInventoryEnough() {
            return inventoryEnough;
        }

        public void setInventoryEnough(boolean inventoryEnough) {
            this.inventoryEnough = inventoryEnough;
        }

        public List<String> getDeviceIds() {
            return deviceIds;
        }

        public void setDeviceIds(List<String> deviceIds) {
            this.deviceIds = deviceIds;
        }

        public List<String> getDeviceNames() {
            return deviceNames;
        }

        public void setDeviceNames(List<String> deviceNames) {
            this.deviceNames = deviceNames;
        }
    }
}
