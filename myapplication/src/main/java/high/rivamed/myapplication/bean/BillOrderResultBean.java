package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

import high.rivamed.myapplication.dto.vo.InventoryVo;

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
     * inventoryVos : [{"accountId":null,"cstName":"真空采血管2","cstCode":null,"cstId":"117","cstSpec":"3ML(454334) 蓝管","epc":"00021720180412000350","expiryDate":"2020-01-05 00:00:00","productionDate":null,"expiration":null,"deviceName":"3号柜子","status":"3","jounalStatus":null,"deptId":null,"deptName":null,"thingName":null,"thingId":null,"alias":null,"batchNumber":null,"vendorCode":null,"vendorName":null,"manuFactory":null,"days":null,"barcode":null,"sheetId":null,"stopFlag":1,"storehouseCode":"ff80818165b1fb680165b35e320d0056","storehouseName":null,"deviceId":"4028829366d31b010166d34ac8af0010","operation":null,"storehouseRemark":null,"countStock":0,"countActual":0,"operationStatus":0,"count":0,"lastUpdateDate":null,"userName":null,"remark":"1","statusStr":null,"unit":null,"isErrorOperation":0,"patientId":null,"patientName":null,"tempPatientId":null,"operationScheduleId":null,"operationBeginDateTime":null,"operatingRoomNoName":null,"operatingRoomNo":null,"name":null,"patientType":null,"idNo":null,"deleteCount":0,"scheduleDateTime":null,"sex":null,"isCreate":null},{"accountId":null,"cstName":"真空采血管2","cstCode":null,"cstId":"117","cstSpec":"3ML(454334) 蓝管","epc":"00021720180412000351","expiryDate":"2020-01-05 00:00:00","productionDate":null,"expiration":null,"deviceName":"3号柜子","status":"3","jounalStatus":null,"deptId":null,"deptName":null,"thingName":null,"thingId":null,"alias":null,"batchNumber":null,"vendorCode":null,"vendorName":null,"manuFactory":null,"days":null,"barcode":null,"sheetId":null,"stopFlag":1,"storehouseCode":"ff80818165b1fb680165b35e320d0056","storehouseName":null,"deviceId":"4028829366d31b010166d34ac8af0010","operation":null,"storehouseRemark":null,"countStock":0,"countActual":0,"operationStatus":0,"count":0,"lastUpdateDate":null,"userName":null,"remark":"1","statusStr":null,"unit":null,"isErrorOperation":0,"patientId":null,"patientName":null,"tempPatientId":null,"operationScheduleId":null,"operationBeginDateTime":null,"operatingRoomNoName":null,"operatingRoomNo":null,"name":null,"patientType":null,"idNo":null,"deleteCount":0,"scheduleDateTime":null,"sex":null,"isCreate":null},{"accountId":null,"cstName":"真空采血管1","cstCode":null,"cstId":"116","cstSpec":"4ml(454204) 红管","epc":"00021720180412000345","expiryDate":"2020-01-05 00:00:00","productionDate":null,"expiration":null,"deviceName":"3号柜子","status":"3","jounalStatus":null,"deptId":null,"deptName":null,"thingName":null,"thingId":null,"alias":null,"batchNumber":null,"vendorCode":null,"vendorName":null,"manuFactory":null,"days":null,"barcode":null,"sheetId":null,"stopFlag":1,"storehouseCode":"ff80818165b1fb680165b35e320d0056","storehouseName":null,"deviceId":"4028829366d31b010166d34ac8af0010","operation":null,"storehouseRemark":null,"countStock":0,"countActual":0,"operationStatus":0,"count":0,"lastUpdateDate":null,"userName":null,"remark":"1","statusStr":null,"unit":null,"isErrorOperation":0,"patientId":null,"patientName":null,"tempPatientId":null,"operationScheduleId":null,"operationBeginDateTime":null,"operatingRoomNoName":null,"operatingRoomNo":null,"name":null,"patientType":null,"idNo":null,"deleteCount":0,"scheduleDateTime":null,"sex":null,"isCreate":null},{"accountId":null,"cstName":"开睑器","cstCode":null,"cstId":"100","cstSpec":"钢丝","epc":"00021720180412000333","expiryDate":"2020-01-05 00:00:00","productionDate":null,"expiration":null,"deviceName":"1柜","status":"3","jounalStatus":null,"deptId":null,"deptName":null,"thingName":null,"thingId":null,"alias":null,"batchNumber":null,"vendorCode":null,"vendorName":null,"manuFactory":null,"days":null,"barcode":null,"sheetId":null,"stopFlag":1,"storehouseCode":"ff80818165b1fb680165b35e320d0056","storehouseName":null,"deviceId":"40288293669e921601669fa480630040","operation":null,"storehouseRemark":null,"countStock":0,"countActual":0,"operationStatus":0,"count":0,"lastUpdateDate":null,"userName":null,"remark":null,"statusStr":null,"unit":null,"isErrorOperation":0,"patientId":null,"patientName":null,"tempPatientId":null,"operationScheduleId":null,"operationBeginDateTime":null,"operatingRoomNoName":null,"operatingRoomNo":null,"name":null,"patientType":null,"idNo":null,"deleteCount":0,"scheduleDateTime":null,"sex":null,"isCreate":null}]
     * cstPlan : {"id":2,"createTime":null,"creatorId":null,"creatorName":null,"deptId":null,"planName":null,"pym":null,"remark":null,"status":null,"updateTime":null}
     * countNum : 4
     * kindsOfCst : 3
     */

    private String msg;
    private String suiteId;
    private String thingId;
    private String deptId;
    private String orderId;
    private String medicalId;
    private String surgeryId;

    private int id;
    private int countNum;
    private int kindsOfCst;

    private List<String>       errorEpcs;
    private List<String>       deviceIds;
    private List<InventoryVo> inventoryVos;

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getMedicalId() {
        return medicalId;
    }

    public void setMedicalId(String medicalId) {
        this.medicalId = medicalId;
    }

    public String getSurgeryId() {
        return surgeryId;
    }

    public void setSurgeryId(String surgeryId) {
        this.surgeryId = surgeryId;
    }

    public String getThingId() {
        return thingId;
    }

    public void setThingId(String thingId) {
        this.thingId = thingId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<String> getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(List<String> deviceIds) {
        this.deviceIds = deviceIds;
    }

    public String getSuiteId() {
        return suiteId;
    }

    public void setSuiteId(String suiteId) {
        this.suiteId = suiteId;
    }

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

    public List<InventoryVo> getInventoryVos() {
        return inventoryVos;
    }

    public void setInventoryVos(List<InventoryVo> inventoryVos) {
        this.inventoryVos = inventoryVos;
    }



}
