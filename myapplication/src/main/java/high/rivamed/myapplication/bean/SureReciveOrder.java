package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: new2.6.3
 * @Package: high.rivamed.myapplication.bean
 * @ClassName: SureReciveOrder
 * @Description: 确认领用耗材--返回
 * @Author: Amos_Bo
 * @CreateDate: 2018/10/29 18:11
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/10/29 18:11
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class SureReciveOrder  implements Serializable{
    /**
     * operateSuccess : true
     * id : 0
     * msg : 您还有耗材尚未领取，请前往:2号柜,1号柜领取耗材
     * transReceiveOrderDetailVos : [{"orderDetailId":"40288293667aa53e01667aa5fcf30001","isHaveNum":1,"counts":2,"cstId":"106","cstName":"晶体植入镊","cstSpec":"直平台","receivedStatus":"未领取","receiveNum":1,"needNum":2,"patientName":"马羊焕","thingCode":"40288293669e921601669fa48062003f","thingName":"1号柜","thingStore":null,"codeArray":null},{"orderDetailId":"40288293667aa53e01667aa5fcf30002","isHaveNum":1,"counts":2,"cstId":"104","cstName":"角膜剪","cstSpec":"1.5m","receivedStatus":"未领取","receiveNum":1,"needNum":2,"patientName":"马羊焕","thingCode":"40288293668bfb3601668bfdb3350000","thingName":"2号柜","thingStore":null,"codeArray":null},{"orderDetailId":"40288293667aa53e01667aa5fcf30003","isHaveNum":0,"counts":3,"cstId":"108","cstName":"结膜剪","cstSpec":"单头","receivedStatus":"未领取","receiveNum":1,"needNum":3,"patientName":"马羊焕","thingCode":"ff80818166861cb001668622ab080000,40288293669e921601669fa48062003f","thingName":"3号柜,1号柜","thingStore":"ff80818166861cb001668622ab080000:2,40288293669e921601669fa48062003f:1","codeArray":["ff80818166861cb001668622ab080000","40288293669e921601669fa48062003f"]}]
     * transReceiveOrder : {"id":"40288293667aa53e01667aa5fc610000","userName":"高级管理员","accountId":"8a80cb8164d9b3940164da1bff760005","patientId":"577404","patientName":"马羊焕","operationScheduleId":"180917sl02788238","optRoomId":null,"operationRoomName":"手术室5","createTime":"2018-10-16 10:13:10"}
     * tcstInventoryOrderVos : [{"cstName":"角巩膜咬切器","cstId":"104","cstSpec":"1.5m","epc":"00021720180412000336","expirationTime":"2020-01-05 00:00:00","thingName":"2号柜","thingCode":"40288293668bfb3601668bfdb3350000","isContain":false},{"cstName":"缝线结扎镊","cstId":"106","cstSpec":"直平台","epc":"00021720180409000045","expirationTime":"2020-01-05 00:00:00","thingName":"1号柜","thingCode":"40288293669e921601669fa48062003f","isContain":false},{"cstName":"虹膜恢复器","cstId":"108","cstSpec":"单头","epc":"00021020180921000002","expirationTime":"2020-01-05 00:00:00","thingName":"1号柜","thingCode":"40288293669e921601669fa48062003f","isContain":false}]
     */

    private boolean operateSuccess;
    private int id;
    private String msg;
    private TransReceiveOrderBean transReceiveOrder;
    private List<TransReceiveOrderDetailVosBean> transReceiveOrderDetailVos;
    private List<TcstInventoryOrderVosBean> tcstInventoryOrderVos;

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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public TransReceiveOrderBean getTransReceiveOrder() {
        return transReceiveOrder;
    }

    public void setTransReceiveOrder(TransReceiveOrderBean transReceiveOrder) {
        this.transReceiveOrder = transReceiveOrder;
    }

    public List<TransReceiveOrderDetailVosBean> getTransReceiveOrderDetailVos() {
        return transReceiveOrderDetailVos;
    }

    public void setTransReceiveOrderDetailVos(List<TransReceiveOrderDetailVosBean> transReceiveOrderDetailVos) {
        this.transReceiveOrderDetailVos = transReceiveOrderDetailVos;
    }

    public List<TcstInventoryOrderVosBean> getTcstInventoryOrderVos() {
        return tcstInventoryOrderVos;
    }

    public void setTcstInventoryOrderVos(List<TcstInventoryOrderVosBean> tcstInventoryOrderVos) {
        this.tcstInventoryOrderVos = tcstInventoryOrderVos;
    }

    public static class TransReceiveOrderBean {
        /**
         * id : 40288293667aa53e01667aa5fc610000
         * userName : 高级管理员
         * accountId : 8a80cb8164d9b3940164da1bff760005
         * patientId : 577404
         * patientName : 马羊焕
         * operationScheduleId : 180917sl02788238
         * optRoomId : null
         * operationRoomName : 手术室5
         * createTime : 2018-10-16 10:13:10
         */

        private String id;
        private String userName;
        private String accountId;
        private String patientId;
        private String patientName;
        private String operationScheduleId;
        private Object optRoomId;
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

        public Object getOptRoomId() {
            return optRoomId;
        }

        public void setOptRoomId(Object optRoomId) {
            this.optRoomId = optRoomId;
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

    public static class TransReceiveOrderDetailVosBean implements Serializable{
        /**
         * orderDetailId : 40288293667aa53e01667aa5fcf30001
         * isHaveNum : 1
         * counts : 2
         * cstId : 106
         * cstName : 晶体植入镊
         * cstSpec : 直平台
         * receivedStatus : 未领取
         * receiveNum : 1
         * needNum : 2
         * patientName : 马羊焕
         * thingCode : 40288293669e921601669fa48062003f
         * thingName : 1号柜
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
        private Object thingStore;
        private Object codeArray;

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

        public Object getThingStore() {
            return thingStore;
        }

        public void setThingStore(Object thingStore) {
            this.thingStore = thingStore;
        }

        public Object getCodeArray() {
            return codeArray;
        }

        public void setCodeArray(Object codeArray) {
            this.codeArray = codeArray;
        }
    }

    public static class TcstInventoryOrderVosBean implements Serializable{
        /**
         * cstName : 角巩膜咬切器
         * cstId : 104
         * cstSpec : 1.5m
         * epc : 00021720180412000336
         * expirationTime : 2020-01-05 00:00:00
         * thingName : 2号柜
         * thingCode : 40288293668bfb3601668bfdb3350000
         * isContain : false
         */

        private String cstName;
        private String cstId;
        private String cstSpec;
        private String epc;
        private String expirationTime;
        private String thingName;
        private String thingCode;
        private boolean isContain;

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
}
