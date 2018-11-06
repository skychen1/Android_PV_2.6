package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: new2.6.3
 * @Package: high.rivamed.myapplication.bean
 * @ClassName: OutFormConfirmResultBean
 * @Description: 请领单领用-根据EPC查询回来的结果
 * @Author: Amos_Bo
 * @CreateDate: 2018/10/29 16:29
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/10/29 16:29
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class OutFormConfirmResultBean implements Serializable {

    /**
     * id : 0
     * transReceiveOrderDetailVos : [{"orderDetailId":null,"isHaveNum":1,"counts":8,"cstId":"108","cstName":"结膜剪","cstSpec":"单头","receivedStatus":"未领取","receiveNum":0,"needNum":3,"patientName":"马羊焕","thingCode":"4028829965f9f6e80165fa1a09530000","thingName":"//////=","thingStore":null,"codeArray":null},{"orderDetailId":null,"isHaveNum":1,"counts":9,"cstId":"106","cstName":"晶体植入镊","cstSpec":"直平台","receivedStatus":"未领取","receiveNum":0,"needNum":2,"patientName":"马羊焕","thingCode":"4028829965fa23790165fa2b79c30002","thingName":"后天","thingStore":null,"codeArray":null},{"orderDetailId":null,"isHaveNum":0,"counts":2,"cstId":"104","cstName":"角膜剪","cstSpec":"1.5m","receivedStatus":"未领取","receiveNum":0,"needNum":2,"patientName":"马羊焕","thingCode":"4028829965fa23790165fa2b79c30002,ff80818165d1bb160165d1bdfdca0000","thingName":"后天,Rivamedg","thingStore":null,"codeArray":["4028829965fa23790165fa2b79c30002","ff80818165d1bb160165d1bdfdca0000"]}]
     * epcs : ["00021720180412000336","00021720180409000045","00021020180921000002"]
     * cstCount : 3
     * cstTypes : 3
     * tcstInventoryOrderVos : [{"cstName":"角巩膜咬切器","cstId":"104","cstSpec":"1.5m","epc":"00021720180412000336","expirationTime":"2020-01-05 00:00:00","thingName":"2号柜","thingCode":"40288293668bfb3601668bfdb3350000","isContain":false},{"cstName":"缝线结扎镊","cstId":"106","cstSpec":"直平台","epc":"00021720180409000045","expirationTime":"2020-01-05 00:00:00","thingName":"1号柜","thingCode":"40288293669e921601669fa48062003f","isContain":false},{"cstName":"虹膜恢复器","cstId":"108","cstSpec":"单头","epc":"00021020180921000002","expirationTime":"2020-01-05 00:00:00","thingName":"1号柜","thingCode":"40288293669e921601669fa48062003f","isContain":false}]
     */

    private int id;
    private int cstCount;
    private int cstTypes;
    private List<TransReceiveOrderDetailVosBean> transReceiveOrderDetailVos;
    private List<String> epcs;
    private List<TcstInventoryOrderVosBean> tcstInventoryOrderVos;
    private OrderSheetBean.RowsBean transReceiveOrder;

    public OrderSheetBean.RowsBean getTransReceiveOrder() {
        return transReceiveOrder;
    }

    public void setTransReceiveOrder(OrderSheetBean.RowsBean transReceiveOrder) {
        this.transReceiveOrder = transReceiveOrder;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCstCount() {
        return cstCount;
    }

    public void setCstCount(int cstCount) {
        this.cstCount = cstCount;
    }

    public int getCstTypes() {
        return cstTypes;
    }

    public void setCstTypes(int cstTypes) {
        this.cstTypes = cstTypes;
    }

    public List<TransReceiveOrderDetailVosBean> getTransReceiveOrderDetailVos() {
        return transReceiveOrderDetailVos;
    }

    public void setTransReceiveOrderDetailVos(List<TransReceiveOrderDetailVosBean> transReceiveOrderDetailVos) {
        this.transReceiveOrderDetailVos = transReceiveOrderDetailVos;
    }

    public List<String> getEpcs() {
        return epcs;
    }

    public void setEpcs(List<String> epcs) {
        this.epcs = epcs;
    }

    public List<TcstInventoryOrderVosBean> getTcstInventoryOrderVos() {
        return tcstInventoryOrderVos;
    }

    public void setTcstInventoryOrderVos(List<TcstInventoryOrderVosBean> tcstInventoryOrderVos) {
        this.tcstInventoryOrderVos = tcstInventoryOrderVos;
    }

    public static class TransReceiveOrderDetailVosBean implements Serializable {
        /**
         * orderDetailId : null
         * isHaveNum : 1
         * counts : 8
         * cstId : 108
         * cstName : 结膜剪
         * cstSpec : 单头
         * receivedStatus : 未领取
         * receiveNum : 0
         * needNum : 3
         * patientName : 马羊焕
         * thingCode : 4028829965f9f6e80165fa1a09530000
         * thingName : //////=
         * thingStore : null
         * codeArray : null
         */

        private String orderDetailId;
        private int isHaveNum;
        private int counts;
        private String cstId;
        private String cstName;
        private String cstSpec;
        private String receivedStatus;
        private int receiveNum;
        private int needNum;
        private String patientName;
        private String thingCode;
        private String thingName;
        private String thingStore;
        private List<String> codeArray;

        public String getOrderDetailId() {
            return orderDetailId;
        }

        public void setOrderDetailId(String orderDetailId) {
            this.orderDetailId = orderDetailId;
        }

        public int getIsHaveNum() {
            return isHaveNum;
        }

        public void setIsHaveNum(int isHaveNum) {
            this.isHaveNum = isHaveNum;
        }

        public int getCounts() {
            return counts;
        }

        public void setCounts(int counts) {
            this.counts = counts;
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

        public String getReceivedStatus() {
            return receivedStatus;
        }

        public void setReceivedStatus(String receivedStatus) {
            this.receivedStatus = receivedStatus;
        }

        public int getReceiveNum() {
            return receiveNum;
        }

        public void setReceiveNum(int receiveNum) {
            this.receiveNum = receiveNum;
        }

        public int getNeedNum() {
            return needNum;
        }

        public void setNeedNum(int needNum) {
            this.needNum = needNum;
        }

        public String getPatientName() {
            return patientName;
        }

        public void setPatientName(String patientName) {
            this.patientName = patientName;
        }

        public String getThingCode() {
            return thingCode;
        }

        public void setThingCode(String thingCode) {
            this.thingCode = thingCode;
        }

        public String getThingName() {
            return thingName;
        }

        public void setThingName(String thingName) {
            this.thingName = thingName;
        }

        public String getThingStore() {
            return thingStore;
        }

        public void setThingStore(String thingStore) {
            this.thingStore = thingStore;
        }

        public List<String> getCodeArray() {
            return codeArray;
        }

        public void setCodeArray(List<String> codeArray) {
            this.codeArray = codeArray;
        }
    }

    public static class TcstInventoryOrderVosBean implements Serializable {
        /**
         * cstName : 角巩膜咬切器
         * cstId : 104
         * cstSpec : 1.5m
         * epc : 00021720180412000336
         * expirationTime : 2020-01-05 00:00:00
         * thingName : 2号柜
         * thingCode : 40288293668bfb3601668bfdb3350000
         * isContain : false
         * deviceName": 柜子
         * deviceCode:4028829366e7fad30166e82a8f860008
         */

        private String cstName;
        private String cstId;
        private String cstSpec;
        private String epc;
        private String expirationTime;
        private String thingName;
        private String thingCode;
        private String deviceName;
        private String deviceCode;
        private boolean isContain;

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getDeviceCode() {
            return deviceCode;
        }

        public void setDeviceCode(String deviceCode) {
            this.deviceCode = deviceCode;
        }

        public String getCstName() {
            return cstName;
        }

        public void setCstName(String cstName) {
            this.cstName = cstName;
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

        public boolean isIsContain() {
            return isContain;
        }

        public void setIsContain(boolean isContain) {
            this.isContain = isContain;
        }
    }

    public static class TransReceiveOrder implements Serializable {
        /**
         * id : 40288293667aa53e01667aa5fc610000
         * userName : adminUM
         * accountId : null
         * patientId : null
         * patientName : 573076/马羊焕
         * operationScheduleId : null
         * operationRoomNo : null
         * operationRoomNoName : null
         * createTime : 2018-10-16 10:13:10
         */

        private String id;
        private String userName;
        private String accountId;
        private String patientId;
        private String patientName;
        private String operationScheduleId;
        private String operationRoomNo;
        private String operationRoomName;
        private String createTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
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

        public String getOperationScheduleId() {
            return operationScheduleId;
        }

        public void setOperationScheduleId(String operationScheduleId) {
            this.operationScheduleId = operationScheduleId;
        }

        public String getOperationRoomNo() {
            return operationRoomNo;
        }

        public void setOperationRoomNo(String operationRoomNo) {
            this.operationRoomNo = operationRoomNo;
        }

        public String getOperationRoomName() {
            return operationRoomName;
        }

        public void setOperationRoomName(String operationRoomName) {
            this.operationRoomName = operationRoomName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
