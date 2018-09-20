package high.rivamed.myapplication.dto.vo;

import java.io.Serializable;

public class TCstInventoryVo implements Serializable {




    private String cstName;
    private String epc;
    private String cstSpec;
    private String expirationTime;
    private String expiration;
    private String deviceName;
    private String status;

    // 0 已过期
    private Integer stopFlag = 1;
    private String storehouseCode;
    private String deviceCode;
    private String cstId;
    private String operation;
    private String storehouseRemark;
    private String remark;
    private int countStock ; // 库存情况  账面
    private int countActual ; // 扫描出来的库存

    private int count ;
    private String lastUpdateDate;
    private String userName;
    // 前端状态显示字段

    private String statusStr;
    private String patientName;
    private String patientId;
    private int    size;
    private String type;
    private String operationStatus;
    private int deletetatus;//0不移除    1移除

    private String name;//名字
    private String idNo;//身份证
    private String deptId;//科室ID
    private String operatingRoomNo;//手术间ID
    private String operatingRoomNoName;//手术间名字
    private String scheduleDateTime;//手术时间
    private String sex;//性别

    public String getOperatingRoomNoName() {
        return operatingRoomNoName;
    }

    public void setOperatingRoomNoName(String operatingRoomNoName) {
        this.operatingRoomNoName = operatingRoomNoName;
    }

    public String getCstId() {
        return cstId;
    }

    public void setCstId(String cstId) {
        this.cstId = cstId;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getOperatingRoomNo() {
        return operatingRoomNo;
    }

    public void setOperatingRoomNo(String operatingRoomNo) {
        this.operatingRoomNo = operatingRoomNo;
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

    public String getOperationScheduleId() {
        return operationScheduleId;
    }

    public void setOperationScheduleId(String operationScheduleId) {
        this.operationScheduleId = operationScheduleId;
    }

    private String operationScheduleId;

    public int getDeletetatus() {
        return deletetatus;
    }

    public void setDeletetatus(int deletetatus) {
        this.deletetatus = deletetatus;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public  boolean         isDelete;
    public String getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(String operationStatus) {
        this.operationStatus = operationStatus;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCstName() {
        return cstName;
    }

    public void setCstName(String cstName) {
        this.cstName = cstName;
    }

    public String getEpc() {
        return epc;
    }

    public void setEpc(String epc) {
        this.epc = epc;
    }

    public String getCstSpec() {
        return cstSpec;
    }

    public void setCstSpec(String cstSpec) {
        this.cstSpec = cstSpec;
    }

    public String getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
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

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }


    //
    //	public TCstInventoryVo(String cstName, String epc, String cstSpec, Date expirationTime, String deviceName,
    //			String status) {
    //		super();
    //		this.cstName = cstName;
    //		this.epc = epc;
    //		this.cstSpec = cstSpec;
    //		this.expirationTime = expirationTime;
    //		this.deviceName = deviceName;
    //		this.status = status;
    //	}




    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TCstInventoryVo tCstInventoryVo = (TCstInventoryVo) obj;
        if (cstId == null) {
            if (tCstInventoryVo.cstId != null)
                return false;
        } else if (!cstId.equals(tCstInventoryVo.cstId))
            return false;
        if (cstName == null) {
            if (tCstInventoryVo.cstName != null)
                return false;
        } else if (!cstName.equals(tCstInventoryVo.cstName))
            return false;
        if (cstSpec == null) {
            if (tCstInventoryVo.cstSpec != null)
                return false;
        } else if (!cstSpec.equals(tCstInventoryVo.cstSpec))
            return false;
        if (epc == null) {
            if (tCstInventoryVo.epc != null)
                return false;
        } else if (!epc.equals(tCstInventoryVo.epc))
            return false;
        return true;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public Integer getStopFlag() {
        return stopFlag;
    }

    public void setStopFlag(Integer stopFlag) {
        this.stopFlag = stopFlag;
    }

    public String getStorehouseCode() {
        return storehouseCode;
    }

    public void setStorehouseCode(String storehouseCode) {
        this.storehouseCode = storehouseCode;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getCstCode() {
        return cstId;
    }

    public void setCstCode(String cstId) {
        this.cstId = cstId;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

}
