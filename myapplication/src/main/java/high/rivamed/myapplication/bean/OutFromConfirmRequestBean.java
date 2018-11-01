package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: new2.6.3
 * @Package: high.rivamed.myapplication.bean
 * @ClassName: OutFromConfirmRequestBean
 * @Description: 耗材请领-根据EPC获取领用数据
 * @Author: Amos_Bo
 * @CreateDate: 2018/10/29 14:56
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/10/29 14:56
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class OutFromConfirmRequestBean implements Serializable {
    private List<BillStockResultBean.TransReceiveOrderDetailVosBean> transReceiveOrderDetailVos;
    private List<String> epcs;

    public List<BillStockResultBean.TransReceiveOrderDetailVosBean> getTransReceiveOrderDetailVos() {
        return transReceiveOrderDetailVos;
    }

    public void setTransReceiveOrderDetailVos(List<BillStockResultBean.TransReceiveOrderDetailVosBean> transReceiveOrderDetailVos) {
        this.transReceiveOrderDetailVos = transReceiveOrderDetailVos;
    }

    public List<String> getEpcs() {
        return epcs;
    }

    public void setEpcs(List<String> epcs) {
        this.epcs = epcs;
    }

    public static class TransReceiveOrderDetailVosBean implements Serializable {
        /**
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
         * codeArray : null
         */

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
        private Object codeArray;

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

        public Object getCodeArray() {
            return codeArray;
        }

        public void setCodeArray(Object codeArray) {
            this.codeArray = codeArray;
        }
    }
}
