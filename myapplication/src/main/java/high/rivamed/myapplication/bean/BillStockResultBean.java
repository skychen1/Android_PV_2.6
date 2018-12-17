package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: new2.6.3
 * @Package: high.rivamed.myapplication.bean
 * @ClassName: BillStockResultBean
 * @Description: 医嘱单-耗材柜库存
 * @Author: Amos_Bo
 * @CreateDate: 2018/10/29 11:23
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/10/29 11:23
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class BillStockResultBean implements Serializable {

    /**
     * id : 0
     * transReceiveOrderDetailVos : [{"orderDetailId":"40288293667aa53e01667aa5fcf30001","isHaveNum":1,"counts":1,"cstId":"116","cstName":"晶体植入镊","cstSpec":"直平台","receivedStatus":"部分领取","receiveNum":1,"needNum":2,"patientName":"马羊焕","deviceId":"4028829366d31b010166d34ac8af0010","deviceName":["3号柜子"],"deviceStore":null,"deviceIds":["4028829366d31b010166d34ac8af0010"],"dname":"3号柜子"},{"orderDetailId":"40288293667aa53e01667aa5fcf30002","isHaveNum":1,"counts":1,"cstId":"117","cstName":"角膜剪","cstSpec":"1.5m","receivedStatus":"部分领取","receiveNum":1,"needNum":2,"patientName":"马羊焕","deviceId":"4028829366d31b010166d34ac8af0010","deviceName":["3号柜子"],"deviceStore":null,"deviceIds":["4028829366d31b010166d34ac8af0010"],"dname":"3号柜子"}]
     * receiveOrderId : 40288293667aa53e01667aa5fc610016
     * cstCount : 4
     * cstTypes : 2
     * thingId : 4028829366d31b010166d34ac8af000f
     */

    private int                                  id;
    private String                               receiveOrderId;
    private int                                  cstCount;
    private int                                  cstTypes;
    private String                               thingId;
    private boolean                              operateSuccess;
    private String                               msg;
    private List<TransReceiveOrderDetailVosBean> transReceiveOrderDetailVos;

   public boolean isOperateSuccess() {
	return operateSuccess;
   }

   public void setOperateSuccess(boolean operateSuccess) {
	this.operateSuccess = operateSuccess;
   }

   public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getId() { return id;}

    public void setId(int id) { this.id = id;}

    public String getReceiveOrderId() { return receiveOrderId;}

    public void setReceiveOrderId(String receiveOrderId) { this.receiveOrderId = receiveOrderId;}

    public int getCstCount() { return cstCount;}

    public void setCstCount(int cstCount) { this.cstCount = cstCount;}

    public int getCstTypes() { return cstTypes;}

    public void setCstTypes(int cstTypes) { this.cstTypes = cstTypes;}

    public String getThingId() { return thingId;}

    public void setThingId(String thingId) { this.thingId = thingId;}

    public List<TransReceiveOrderDetailVosBean> getTransReceiveOrderDetailVos() { return transReceiveOrderDetailVos;}

    public void setTransReceiveOrderDetailVos(
          List<TransReceiveOrderDetailVosBean> transReceiveOrderDetailVos) { this.transReceiveOrderDetailVos = transReceiveOrderDetailVos;}

    public static class TransReceiveOrderDetailVosBean implements Serializable{

        /**
         * orderDetailId : 40288293667aa53e01667aa5fcf30001
         * isHaveNum : 1
         * counts : 1
         * cstId : 116
         * cstName : 晶体植入镊
         * cstSpec : 直平台
         * receivedStatus : 部分领取
         * receiveNum : 1
         * needNum : 2
         * patientName : 马羊焕
         * deviceId : 4028829366d31b010166d34ac8af0010
         * deviceName : ["3号柜子"]
         * deviceStore : null
         * deviceIds : ["4028829366d31b010166d34ac8af0010"]
         * dname : 3号柜子
         */

        private String       orderDetailId;
        private int          isHaveNum;
        private int          counts;
        private String       cstId;
        private String       cstName;
        private String       cstSpec;
        private String       receivedStatus;
        private int          receiveNum;
        private int          needNum;
        private String       patientName;
        private String       deviceId;
        private Object       deviceStore;
        private String       dname;
        private List<String> deviceName;
        private List<String> deviceIds;

        public String getOrderDetailId() { return orderDetailId;}

        public void setOrderDetailId(String orderDetailId) { this.orderDetailId = orderDetailId;}

        public int getIsHaveNum() { return isHaveNum;}

        public void setIsHaveNum(int isHaveNum) { this.isHaveNum = isHaveNum;}

        public int getCounts() { return counts;}

        public void setCounts(int counts) { this.counts = counts;}

        public String getCstId() { return cstId;}

        public void setCstId(String cstId) { this.cstId = cstId;}

        public String getCstName() { return cstName;}

        public void setCstName(String cstName) { this.cstName = cstName;}

        public String getCstSpec() { return cstSpec;}

        public void setCstSpec(String cstSpec) { this.cstSpec = cstSpec;}

        public String getReceivedStatus() { return receivedStatus;}

        public void setReceivedStatus(
              String receivedStatus) { this.receivedStatus = receivedStatus;}

        public int getReceiveNum() { return receiveNum;}

        public void setReceiveNum(int receiveNum) { this.receiveNum = receiveNum;}

        public int getNeedNum() { return needNum;}

        public void setNeedNum(int needNum) { this.needNum = needNum;}

        public String getPatientName() { return patientName;}

        public void setPatientName(String patientName) { this.patientName = patientName;}

        public String getDeviceId() { return deviceId;}

        public void setDeviceId(String deviceId) { this.deviceId = deviceId;}

        public Object getDeviceStore() { return deviceStore;}

        public void setDeviceStore(Object deviceStore) { this.deviceStore = deviceStore;}

        public String getDname() { return dname;}

        public void setDname(String dname) { this.dname = dname;}

        public List<String> getDeviceName() { return deviceName;}

        public void setDeviceName(List<String> deviceName) { this.deviceName = deviceName;}

        public List<String> getDeviceIds() { return deviceIds;}

        public void setDeviceIds(List<String> deviceIds) { this.deviceIds = deviceIds;}
    }
}
