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
     * pageModel : {"pageNo":1,"pageSize":10,"rows":[{"orderDetailId":"40288293667aa53e01667aa5fcf30001","isHaveNum":1,"counts":2,"cstId":"106","cstName":"晶体植入镊","cstSpec":"直平台","receivedStatus":"部分领取","receiveNum":1,"needNum":2,"patientName":"马羊焕","deviceCode":"40288293669e921601669fa480630040","deviceName":"1柜","deviceStore":null,"deviceCodes":["40288293669e921601669fa480630040"]},{"orderDetailId":"40288293667aa53e01667aa5fcf30002","isHaveNum":1,"counts":1,"cstId":"104","cstName":"角膜剪","cstSpec":"1.5m","receivedStatus":"已领取","receiveNum":2,"needNum":2,"patientName":"马羊焕","deviceCode":"4028829366d31b010166d34ac8af0010","deviceName":"3号柜子","deviceStore":null,"deviceCodes":[]},{"orderDetailId":"40288293667aa53e01667aa5fcf30002","isHaveNum":1,"counts":2,"cstId":"104","cstName":"角膜剪","cstSpec":"1.5m","receivedStatus":"1","receiveNum":2,"needNum":2,"patientName":"马羊焕","deviceCode":"40288293669e921601669fa480630040","deviceName":"1柜","deviceStore":null,"deviceCodes":[]},{"orderDetailId":"40288293667aa53e01667aa5fcf30003","isHaveNum":0,"counts":2,"cstId":"108","cstName":"结膜剪","cstSpec":"单头","receivedStatus":"部分领取","receiveNum":1,"needNum":3,"patientName":"马羊焕","deviceCode":"4028829366d31b010166d34ac8af0010,40288293669e921601669fa480630040","deviceName":"3号柜子,1柜","deviceStore":"4028829366d31b010166d34ac8af0010:1,40288293669e921601669fa480630040:1","deviceCodes":["4028829366d31b010166d34ac8af0010","40288293669e921601669fa480630040"]},{"orderDetailId":"40288293667aa53e01667aa5fcf30003","isHaveNum":0,"counts":1,"cstId":"108","cstName":"结膜剪","cstSpec":"单头","receivedStatus":"2","receiveNum":1,"needNum":3,"patientName":"马羊焕","deviceCode":"40288293669e921601669fa480630040","deviceName":"1柜","deviceStore":null,"deviceCodes":[]}],"total":5}
     * id : 0
     * transReceiveOrderDetailVos : [{"orderDetailId":"40288293667aa53e01667aa5fcf30001","isHaveNum":1,"counts":2,"cstId":"106","cstName":"晶体植入镊","cstSpec":"直平台","receivedStatus":"部分领取","receiveNum":1,"needNum":2,"patientName":"马羊焕","deviceCode":"40288293669e921601669fa480630040","deviceName":"1柜","deviceStore":null,"deviceCodes":["40288293669e921601669fa480630040"]},{"orderDetailId":"40288293667aa53e01667aa5fcf30002","isHaveNum":1,"counts":1,"cstId":"104","cstName":"角膜剪","cstSpec":"1.5m","receivedStatus":"已领取","receiveNum":2,"needNum":2,"patientName":"马羊焕","deviceCode":"4028829366d31b010166d34ac8af0010","deviceName":"3号柜子","deviceStore":null,"deviceCodes":[]},{"orderDetailId":"40288293667aa53e01667aa5fcf30003","isHaveNum":0,"counts":2,"cstId":"108","cstName":"结膜剪","cstSpec":"单头","receivedStatus":"部分领取","receiveNum":1,"needNum":3,"patientName":"马羊焕","deviceCode":"4028829366d31b010166d34ac8af0010,40288293669e921601669fa480630040","deviceName":"3号柜子,1柜","deviceStore":"4028829366d31b010166d34ac8af0010:1,40288293669e921601669fa480630040:1","deviceCodes":["4028829366d31b010166d34ac8af0010","40288293669e921601669fa480630040"]}]
     * orderId : 40288293667aa53e01667aa5fc610000
     * cstCount : 7
     * cstTypes : 3
     */

    private PageModelBean pageModel;
    private int id;
    private String orderId;
    private int cstCount;
    private int cstTypes;
    private List<TransReceiveOrderDetailVosBean> transReceiveOrderDetailVos;

    public PageModelBean getPageModel() {
        return pageModel;
    }

    public void setPageModel(PageModelBean pageModel) {
        this.pageModel = pageModel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public static class PageModelBean implements Serializable {
        /**
         * pageNo : 1
         * pageSize : 10
         * rows : [{"orderDetailId":"40288293667aa53e01667aa5fcf30001","isHaveNum":1,"counts":2,"cstId":"106","cstName":"晶体植入镊","cstSpec":"直平台","receivedStatus":"部分领取","receiveNum":1,"needNum":2,"patientName":"马羊焕","deviceCode":"40288293669e921601669fa480630040","deviceName":"1柜","deviceStore":null,"deviceCodes":["40288293669e921601669fa480630040"]},{"orderDetailId":"40288293667aa53e01667aa5fcf30002","isHaveNum":1,"counts":1,"cstId":"104","cstName":"角膜剪","cstSpec":"1.5m","receivedStatus":"已领取","receiveNum":2,"needNum":2,"patientName":"马羊焕","deviceCode":"4028829366d31b010166d34ac8af0010","deviceName":"3号柜子","deviceStore":null,"deviceCodes":[]},{"orderDetailId":"40288293667aa53e01667aa5fcf30002","isHaveNum":1,"counts":2,"cstId":"104","cstName":"角膜剪","cstSpec":"1.5m","receivedStatus":"1","receiveNum":2,"needNum":2,"patientName":"马羊焕","deviceCode":"40288293669e921601669fa480630040","deviceName":"1柜","deviceStore":null,"deviceCodes":[]},{"orderDetailId":"40288293667aa53e01667aa5fcf30003","isHaveNum":0,"counts":2,"cstId":"108","cstName":"结膜剪","cstSpec":"单头","receivedStatus":"部分领取","receiveNum":1,"needNum":3,"patientName":"马羊焕","deviceCode":"4028829366d31b010166d34ac8af0010,40288293669e921601669fa480630040","deviceName":"3号柜子,1柜","deviceStore":"4028829366d31b010166d34ac8af0010:1,40288293669e921601669fa480630040:1","deviceCodes":["4028829366d31b010166d34ac8af0010","40288293669e921601669fa480630040"]},{"orderDetailId":"40288293667aa53e01667aa5fcf30003","isHaveNum":0,"counts":1,"cstId":"108","cstName":"结膜剪","cstSpec":"单头","receivedStatus":"2","receiveNum":1,"needNum":3,"patientName":"马羊焕","deviceCode":"40288293669e921601669fa480630040","deviceName":"1柜","deviceStore":null,"deviceCodes":[]}]
         * total : 5
         */

        private int pageNo;
        private int pageSize;
        private int total;
        private List<RowsBean> rows;

        public int getPageNo() {
            return pageNo;
        }

        public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<RowsBean> getRows() {
            return rows;
        }

        public void setRows(List<RowsBean> rows) {
            this.rows = rows;
        }

        public static class RowsBean implements Serializable{
            /**
             * orderDetailId : 40288293667aa53e01667aa5fcf30001
             * isHaveNum : 1
             * counts : 2
             * cstId : 106
             * cstName : 晶体植入镊
             * cstSpec : 直平台
             * receivedStatus : 部分领取
             * receiveNum : 1
             * needNum : 2
             * patientName : 马羊焕
             * deviceCode : 40288293669e921601669fa480630040
             * deviceName : 1柜
             * deviceStore : null
             * deviceCodes : ["40288293669e921601669fa480630040"]
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
            private String deviceCode;
            private String deviceName;
            private String deviceStore;
            private List<String> deviceCodes;

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

            public String getDeviceCode() {
                return deviceCode;
            }

            public void setDeviceCode(String deviceCode) {
                this.deviceCode = deviceCode;
            }

            public String getDeviceName() {
                return deviceName;
            }

            public void setDeviceName(String deviceName) {
                this.deviceName = deviceName;
            }

            public String getDeviceStore() {
                return deviceStore;
            }

            public void setDeviceStore(String deviceStore) {
                this.deviceStore = deviceStore;
            }

            public List<String> getDeviceCodes() {
                return deviceCodes;
            }

            public void setDeviceCodes(List<String> deviceCodes) {
                this.deviceCodes = deviceCodes;
            }
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
         * receivedStatus : 部分领取
         * receiveNum : 1
         * needNum : 2
         * patientName : 马羊焕
         * deviceCode : 40288293669e921601669fa480630040
         * deviceName : 1柜
         * deviceStore : null
         * deviceCodes : ["40288293669e921601669fa480630040"]
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
        private String deviceCode;
        private String deviceName;
        private String deviceStore;
        private List<String> deviceCodes;

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

        public String getDeviceCode() {
            return deviceCode;
        }

        public void setDeviceCode(String deviceCode) {
            this.deviceCode = deviceCode;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getDeviceStore() {
            return deviceStore;
        }

        public void setDeviceStore(String deviceStore) {
            this.deviceStore = deviceStore;
        }

        public List<String> getDeviceCodes() {
            return deviceCodes;
        }

        public void setDeviceCodes(List<String> deviceCodes) {
            this.deviceCodes = deviceCodes;
        }
    }
}
