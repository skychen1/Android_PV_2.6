package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: new2.6.3
 * @Package: high.rivamed.myapplication.bean
 * @ClassName: BillOrderResultBean
 * @Description: 套装领用-根据EPC查询返回耗材数据
 * @Author: Amos_Bo
 * @CreateDate: 2018/10/31 15:51
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/10/31 15:51
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class BillOrderResultBean implements Serializable {

    /**
     * id : 0
     * cstInventoryVos : [{"accountId":null,"cstName":"真空采血管2","cstCode":null,"cstId":"117","cstSpec":"3ML(454334) 蓝管","epc":"00021720180412000350","expirationTime":"2020-01-05 00:00:00","productionDate":null,"expiration":null,"deviceName":"3号柜子","status":"3","jounalStatus":null,"deptId":null,"deptName":null,"thingName":null,"thingCode":null,"alias":null,"batchNumber":null,"vendorCode":null,"vendorName":null,"manuFactory":null,"days":null,"barcode":null,"sheetId":null,"stopFlag":1,"storehouseCode":"ff80818165b1fb680165b35e320d0056","storehouseName":null,"deviceCode":"4028829366d31b010166d34ac8af0010","operation":null,"storehouseRemark":null,"countStock":0,"countActual":0,"operationStatus":0,"count":0,"lastUpdateDate":null,"userName":null,"remark":"1","statusStr":null,"unit":null,"isErrorOperation":0,"patientId":null,"patientName":null,"tempPatientId":null,"operationScheduleId":null,"operationBeginDateTime":null,"operatingRoomNoName":null,"operatingRoomNo":null,"name":null,"patientType":null,"idNo":null,"deleteCount":0,"scheduleDateTime":null,"sex":null,"isCreate":null},{"accountId":null,"cstName":"真空采血管2","cstCode":null,"cstId":"117","cstSpec":"3ML(454334) 蓝管","epc":"00021720180412000351","expirationTime":"2020-01-05 00:00:00","productionDate":null,"expiration":null,"deviceName":"3号柜子","status":"3","jounalStatus":null,"deptId":null,"deptName":null,"thingName":null,"thingCode":null,"alias":null,"batchNumber":null,"vendorCode":null,"vendorName":null,"manuFactory":null,"days":null,"barcode":null,"sheetId":null,"stopFlag":1,"storehouseCode":"ff80818165b1fb680165b35e320d0056","storehouseName":null,"deviceCode":"4028829366d31b010166d34ac8af0010","operation":null,"storehouseRemark":null,"countStock":0,"countActual":0,"operationStatus":0,"count":0,"lastUpdateDate":null,"userName":null,"remark":"1","statusStr":null,"unit":null,"isErrorOperation":0,"patientId":null,"patientName":null,"tempPatientId":null,"operationScheduleId":null,"operationBeginDateTime":null,"operatingRoomNoName":null,"operatingRoomNo":null,"name":null,"patientType":null,"idNo":null,"deleteCount":0,"scheduleDateTime":null,"sex":null,"isCreate":null},{"accountId":null,"cstName":"真空采血管1","cstCode":null,"cstId":"116","cstSpec":"4ml(454204) 红管","epc":"00021720180412000345","expirationTime":"2020-01-05 00:00:00","productionDate":null,"expiration":null,"deviceName":"3号柜子","status":"3","jounalStatus":null,"deptId":null,"deptName":null,"thingName":null,"thingCode":null,"alias":null,"batchNumber":null,"vendorCode":null,"vendorName":null,"manuFactory":null,"days":null,"barcode":null,"sheetId":null,"stopFlag":1,"storehouseCode":"ff80818165b1fb680165b35e320d0056","storehouseName":null,"deviceCode":"4028829366d31b010166d34ac8af0010","operation":null,"storehouseRemark":null,"countStock":0,"countActual":0,"operationStatus":0,"count":0,"lastUpdateDate":null,"userName":null,"remark":"1","statusStr":null,"unit":null,"isErrorOperation":0,"patientId":null,"patientName":null,"tempPatientId":null,"operationScheduleId":null,"operationBeginDateTime":null,"operatingRoomNoName":null,"operatingRoomNo":null,"name":null,"patientType":null,"idNo":null,"deleteCount":0,"scheduleDateTime":null,"sex":null,"isCreate":null},{"accountId":null,"cstName":"开睑器","cstCode":null,"cstId":"100","cstSpec":"钢丝","epc":"00021720180412000333","expirationTime":"2020-01-05 00:00:00","productionDate":null,"expiration":null,"deviceName":"1柜","status":"3","jounalStatus":null,"deptId":null,"deptName":null,"thingName":null,"thingCode":null,"alias":null,"batchNumber":null,"vendorCode":null,"vendorName":null,"manuFactory":null,"days":null,"barcode":null,"sheetId":null,"stopFlag":1,"storehouseCode":"ff80818165b1fb680165b35e320d0056","storehouseName":null,"deviceCode":"40288293669e921601669fa480630040","operation":null,"storehouseRemark":null,"countStock":0,"countActual":0,"operationStatus":0,"count":0,"lastUpdateDate":null,"userName":null,"remark":null,"statusStr":null,"unit":null,"isErrorOperation":0,"patientId":null,"patientName":null,"tempPatientId":null,"operationScheduleId":null,"operationBeginDateTime":null,"operatingRoomNoName":null,"operatingRoomNo":null,"name":null,"patientType":null,"idNo":null,"deleteCount":0,"scheduleDateTime":null,"sex":null,"isCreate":null}]
     * cstPlan : {"id":2,"createTime":null,"creatorId":null,"creatorName":null,"deptId":null,"planName":null,"pym":null,"remark":null,"status":null,"updateTime":null}
     * countNum : 4
     * countKind : 3
     */

    private String msg;

    private int id;
    private CstPlanBean cstPlan;
    private int countNum;
    private int countKind;

    private List<String> errorEpcs;
    private List<CstInventoryVosBean> cstInventoryVos;

    public List<String> getErrorEpcs() {
        return errorEpcs;
    }

    public void setErrorEpcs(List<String> errorEpcs) {
        this.errorEpcs = errorEpcs;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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

    public int getCountNum() {
        return countNum;
    }

    public void setCountNum(int countNum) {
        this.countNum = countNum;
    }

    public int getCountKind() {
        return countKind;
    }

    public void setCountKind(int countKind) {
        this.countKind = countKind;
    }

    public List<CstInventoryVosBean> getCstInventoryVos() {
        return cstInventoryVos;
    }

    public void setCstInventoryVos(List<CstInventoryVosBean> cstInventoryVos) {
        this.cstInventoryVos = cstInventoryVos;
    }

    public static class CstPlanBean implements Serializable {
        /**
         * id : 2
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

    public static class CstInventoryVosBean implements Serializable {
        /**
         * accountId : null
         * cstName : 真空采血管2
         * cstCode : null
         * cstId : 117
         * cstSpec : 3ML(454334) 蓝管
         * epc : 00021720180412000350
         * expirationTime : 2020-01-05 00:00:00
         * productionDate : null
         * expiration : null
         * deviceName : 3号柜子
         * status : 3
         * jounalStatus : null
         * deptId : null
         * deptName : null
         * thingName : null
         * thingCode : null
         * alias : null
         * batchNumber : null
         * vendorCode : null
         * vendorName : null
         * manuFactory : null
         * days : null
         * barcode : null
         * sheetId : null
         * stopFlag : 1
         * storehouseCode : ff80818165b1fb680165b35e320d0056
         * storehouseName : null
         * deviceCode : 4028829366d31b010166d34ac8af0010
         * operation : null
         * storehouseRemark : null
         * countStock : 0
         * countActual : 0
         * operationStatus : 0
         * count : 0
         * lastUpdateDate : null
         * userName : null
         * remark : 1
         * statusStr : null
         * unit : null
         * isErrorOperation : 0
         * patientId : null
         * patientName : null
         * tempPatientId : null
         * operationScheduleId : null
         * operationBeginDateTime : null
         * operatingRoomNoName : null
         * operatingRoomNo : null
         * name : null
         * patientType : null
         * idNo : null
         * deleteCount : 0
         * scheduleDateTime : null
         * sex : null
         * isCreate : null
         */

        private String accountId;
        private String cstName;
        private String cstCode;
        private String cstId;
        private String cstSpec;
        private String epc;
        private String expirationTime;
        private String productionDate;
        private String expiration;
        private String deviceName;
        private String status;
        private String jounalStatus;
        private String deptId;
        private String deptName;
        private String thingName;
        private String thingCode;
        private String alias;
        private String batchNumber;
        private String vendorCode;
        private String vendorName;
        private String manuFactory;
        private String days;
        private String barcode;
        private String sheetId;
        private int stopFlag;
        private String storehouseCode;
        private String storehouseName;
        private String deviceCode;
        private String operation;
        private String storehouseRemark;
        private int countStock;
        private int countActual;
        private int operationStatus;
        private int count;
        private String lastUpdateDate;
        private String userName;
        private String remark;
        private String statusStr;
        private String unit;
        private int isErrorOperation;
        private String patientId;
        private String patientName;
        private String tempPatientId;
        private String operationScheduleId;
        private String operationBeginDateTime;
        private String operatingRoomNoName;
        private String operatingRoomNo;
        private String name;
        private String patientType;
        private String idNo;
        private int deleteCount;
        private String scheduleDateTime;
        private String sex;
        private String isCreate;

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public String getCstName() {
            return cstName;
        }

        public void setCstName(String cstName) {
            this.cstName = cstName;
        }

        public String getCstCode() {
            return cstCode;
        }

        public void setCstCode(String cstCode) {
            this.cstCode = cstCode;
        }

        public String getCstId() {
            return cstId;
        }

        public void setCstId(String cstId) {
            this.cstId = cstId;
        }

        public String getCstSpec() {
            return cstSpec;
        }

        public void setCstSpec(String cstSpec) {
            this.cstSpec = cstSpec;
        }

        public String getEpc() {
            return epc;
        }

        public void setEpc(String epc) {
            this.epc = epc;
        }

        public String getExpirationTime() {
            return expirationTime;
        }

        public void setExpirationTime(String expirationTime) {
            this.expirationTime = expirationTime;
        }

        public String getProductionDate() {
            return productionDate;
        }

        public void setProductionDate(String productionDate) {
            this.productionDate = productionDate;
        }

        public String getExpiration() {
            return expiration;
        }

        public void setExpiration(String expiration) {
            this.expiration = expiration;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getJounalStatus() {
            return jounalStatus;
        }

        public void setJounalStatus(String jounalStatus) {
            this.jounalStatus = jounalStatus;
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

        public String getThingName() {
            return thingName;
        }

        public void setThingName(String thingName) {
            this.thingName = thingName;
        }

        public String getThingCode() {
            return thingCode;
        }

        public void setThingCode(String thingCode) {
            this.thingCode = thingCode;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public String getBatchNumber() {
            return batchNumber;
        }

        public void setBatchNumber(String batchNumber) {
            this.batchNumber = batchNumber;
        }

        public String getVendorCode() {
            return vendorCode;
        }

        public void setVendorCode(String vendorCode) {
            this.vendorCode = vendorCode;
        }

        public String getVendorName() {
            return vendorName;
        }

        public void setVendorName(String vendorName) {
            this.vendorName = vendorName;
        }

        public String getManuFactory() {
            return manuFactory;
        }

        public void setManuFactory(String manuFactory) {
            this.manuFactory = manuFactory;
        }

        public String getDays() {
            return days;
        }

        public void setDays(String days) {
            this.days = days;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getSheetId() {
            return sheetId;
        }

        public void setSheetId(String sheetId) {
            this.sheetId = sheetId;
        }

        public int getStopFlag() {
            return stopFlag;
        }

        public void setStopFlag(int stopFlag) {
            this.stopFlag = stopFlag;
        }

        public String getStorehouseCode() {
            return storehouseCode;
        }

        public void setStorehouseCode(String storehouseCode) {
            this.storehouseCode = storehouseCode;
        }

        public String getStorehouseName() {
            return storehouseName;
        }

        public void setStorehouseName(String storehouseName) {
            this.storehouseName = storehouseName;
        }

        public String getDeviceCode() {
            return deviceCode;
        }

        public void setDeviceCode(String deviceCode) {
            this.deviceCode = deviceCode;
        }

        public String getOperation() {
            return operation;
        }

        public void setOperation(String operation) {
            this.operation = operation;
        }

        public String getStorehouseRemark() {
            return storehouseRemark;
        }

        public void setStorehouseRemark(String storehouseRemark) {
            this.storehouseRemark = storehouseRemark;
        }

        public int getCountStock() {
            return countStock;
        }

        public void setCountStock(int countStock) {
            this.countStock = countStock;
        }

        public int getCountActual() {
            return countActual;
        }

        public void setCountActual(int countActual) {
            this.countActual = countActual;
        }

        public int getOperationStatus() {
            return operationStatus;
        }

        public void setOperationStatus(int operationStatus) {
            this.operationStatus = operationStatus;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getLastUpdateDate() {
            return lastUpdateDate;
        }

        public void setLastUpdateDate(String lastUpdateDate) {
            this.lastUpdateDate = lastUpdateDate;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getStatusStr() {
            return statusStr;
        }

        public void setStatusStr(String statusStr) {
            this.statusStr = statusStr;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public int getIsErrorOperation() {
            return isErrorOperation;
        }

        public void setIsErrorOperation(int isErrorOperation) {
            this.isErrorOperation = isErrorOperation;
        }

        public String getPatientId() {
            return patientId;
        }

        public void setPatientId(String patientId) {
            this.patientId = patientId;
        }

        public String getPatientName() {
            return patientName;
        }

        public void setPatientName(String patientName) {
            this.patientName = patientName;
        }

        public String getTempPatientId() {
            return tempPatientId;
        }

        public void setTempPatientId(String tempPatientId) {
            this.tempPatientId = tempPatientId;
        }

        public String getOperationScheduleId() {
            return operationScheduleId;
        }

        public void setOperationScheduleId(String operationScheduleId) {
            this.operationScheduleId = operationScheduleId;
        }

        public String getOperationBeginDateTime() {
            return operationBeginDateTime;
        }

        public void setOperationBeginDateTime(String operationBeginDateTime) {
            this.operationBeginDateTime = operationBeginDateTime;
        }

        public String getOperatingRoomNoName() {
            return operatingRoomNoName;
        }

        public void setOperatingRoomNoName(String operatingRoomNoName) {
            this.operatingRoomNoName = operatingRoomNoName;
        }

        public String getOperatingRoomNo() {
            return operatingRoomNo;
        }

        public void setOperatingRoomNo(String operatingRoomNo) {
            this.operatingRoomNo = operatingRoomNo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPatientType() {
            return patientType;
        }

        public void setPatientType(String patientType) {
            this.patientType = patientType;
        }

        public String getIdNo() {
            return idNo;
        }

        public void setIdNo(String idNo) {
            this.idNo = idNo;
        }

        public int getDeleteCount() {
            return deleteCount;
        }

        public void setDeleteCount(int deleteCount) {
            this.deleteCount = deleteCount;
        }

        public String getScheduleDateTime() {
            return scheduleDateTime;
        }

        public void setScheduleDateTime(String scheduleDateTime) {
            this.scheduleDateTime = scheduleDateTime;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getIsCreate() {
            return isCreate;
        }

        public void setIsCreate(String isCreate) {
            this.isCreate = isCreate;
        }
    }
}
