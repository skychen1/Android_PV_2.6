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
     * pageModel : {"pageNo":1,"pageSize":10,"rows":[{"orderDetailId":"40288293667aa53e01667aa5fcf30001","isHaveNum":1,"counts":2,"cstId":"106","cstName":"晶体植入镊","cstSpec":"直平台","receivedStatus":"未领取","receiveNum":0,"needNum":2,"patientName":"马羊焕","thingCode":"40288293669e921601669fa48062003f","thingName":"1号柜","thingStore":null,"codeArray":null},{"orderDetailId":"40288293667aa53e01667aa5fcf30002","isHaveNum":1,"counts":2,"cstId":"104","cstName":"角膜剪","cstSpec":"1.5m","receivedStatus":"未领取","receiveNum":0,"needNum":2,"patientName":"马羊焕","thingCode":"40288293668bfb3601668bfdb3350000","thingName":"2号柜","thingStore":null,"codeArray":null},{"orderDetailId":"40288293667aa53e01667aa5fcf30003","isHaveNum":0,"counts":3,"cstId":"108","cstName":"结膜剪","cstSpec":"单头","receivedStatus":"未领取","receiveNum":0,"needNum":3,"patientName":"马羊焕","thingCode":"ff80818166861cb001668622ab080000,40288293669e921601669fa48062003f","thingName":"3号柜,1号柜","thingStore":"ff80818166861cb001668622ab080000:2,40288293669e921601669fa48062003f:1","codeArray":["ff80818166861cb001668622ab080000","40288293669e921601669fa48062003f"]},{"orderDetailId":"40288293667aa53e01667aa5fcf30003","isHaveNum":0,"counts":1,"cstId":"108","cstName":"结膜剪","cstSpec":"单头","receivedStatus":"3","receiveNum":0,"needNum":3,"patientName":"马羊焕","thingCode":"40288293669e921601669fa48062003f","thingName":"1号柜","thingStore":null,"codeArray":null},{"orderDetailId":"40288293667aa53e01667aa5fcf30001","isHaveNum":0,"counts":1,"cstId":"106","cstName":"晶体植入镊","cstSpec":"直平台","receivedStatus":"3","receiveNum":0,"needNum":2,"patientName":"马羊焕","thingCode":"ff80818166861cb001668622ab080000","thingName":"3号柜","thingStore":null,"codeArray":null},{"orderDetailId":"40288293667aa53e01667aa5fcf30002","isHaveNum":0,"counts":1,"cstId":"104","cstName":"角膜剪","cstSpec":"1.5m","receivedStatus":"3","receiveNum":0,"needNum":2,"patientName":"马羊焕","thingCode":"40288293669e921601669fa48062003f","thingName":"1号柜","thingStore":null,"codeArray":null}],"total":6}
     * id : 0
     * transReceiveOrderDetailVos : [{"orderDetailId":"40288293667aa53e01667aa5fcf30001","isHaveNum":1,"counts":2,"cstId":"106","cstName":"晶体植入镊","cstSpec":"直平台","receivedStatus":"未领取","receiveNum":0,"needNum":2,"patientName":"马羊焕","thingCode":"40288293669e921601669fa48062003f","thingName":"1号柜","thingStore":null,"codeArray":null},{"orderDetailId":"40288293667aa53e01667aa5fcf30002","isHaveNum":1,"counts":2,"cstId":"104","cstName":"角膜剪","cstSpec":"1.5m","receivedStatus":"未领取","receiveNum":0,"needNum":2,"patientName":"马羊焕","thingCode":"40288293668bfb3601668bfdb3350000","thingName":"2号柜","thingStore":null,"codeArray":null},{"orderDetailId":"40288293667aa53e01667aa5fcf30003","isHaveNum":0,"counts":3,"cstId":"108","cstName":"结膜剪","cstSpec":"单头","receivedStatus":"未领取","receiveNum":0,"needNum":3,"patientName":"马羊焕","thingCode":"ff80818166861cb001668622ab080000,40288293669e921601669fa48062003f","thingName":"3号柜,1号柜","thingStore":"ff80818166861cb001668622ab080000:2,40288293669e921601669fa48062003f:1","codeArray":["ff80818166861cb001668622ab080000","40288293669e921601669fa48062003f"]}]
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
         * rows : [{"orderDetailId":"40288293667aa53e01667aa5fcf30001","isHaveNum":1,"counts":2,"cstId":"106","cstName":"晶体植入镊","cstSpec":"直平台","receivedStatus":"未领取","receiveNum":0,"needNum":2,"patientName":"马羊焕","thingCode":"40288293669e921601669fa48062003f","thingName":"1号柜","thingStore":null,"codeArray":null},{"orderDetailId":"40288293667aa53e01667aa5fcf30002","isHaveNum":1,"counts":2,"cstId":"104","cstName":"角膜剪","cstSpec":"1.5m","receivedStatus":"未领取","receiveNum":0,"needNum":2,"patientName":"马羊焕","thingCode":"40288293668bfb3601668bfdb3350000","thingName":"2号柜","thingStore":null,"codeArray":null},{"orderDetailId":"40288293667aa53e01667aa5fcf30003","isHaveNum":0,"counts":3,"cstId":"108","cstName":"结膜剪","cstSpec":"单头","receivedStatus":"未领取","receiveNum":0,"needNum":3,"patientName":"马羊焕","thingCode":"ff80818166861cb001668622ab080000,40288293669e921601669fa48062003f","thingName":"3号柜,1号柜","thingStore":"ff80818166861cb001668622ab080000:2,40288293669e921601669fa48062003f:1","codeArray":["ff80818166861cb001668622ab080000","40288293669e921601669fa48062003f"]},{"orderDetailId":"40288293667aa53e01667aa5fcf30003","isHaveNum":0,"counts":1,"cstId":"108","cstName":"结膜剪","cstSpec":"单头","receivedStatus":"3","receiveNum":0,"needNum":3,"patientName":"马羊焕","thingCode":"40288293669e921601669fa48062003f","thingName":"1号柜","thingStore":null,"codeArray":null},{"orderDetailId":"40288293667aa53e01667aa5fcf30001","isHaveNum":0,"counts":1,"cstId":"106","cstName":"晶体植入镊","cstSpec":"直平台","receivedStatus":"3","receiveNum":0,"needNum":2,"patientName":"马羊焕","thingCode":"ff80818166861cb001668622ab080000","thingName":"3号柜","thingStore":null,"codeArray":null},{"orderDetailId":"40288293667aa53e01667aa5fcf30002","isHaveNum":0,"counts":1,"cstId":"104","cstName":"角膜剪","cstSpec":"1.5m","receivedStatus":"3","receiveNum":0,"needNum":2,"patientName":"马羊焕","thingCode":"40288293669e921601669fa48062003f","thingName":"1号柜","thingStore":null,"codeArray":null}]
         * total : 6
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

        public static class RowsBean implements Serializable {
            /**
             * orderDetailId : 40288293667aa53e01667aa5fcf30001
             * isHaveNum : 1
             * counts : 2
             * cstId : 106
             * cstName : 晶体植入镊
             * cstSpec : 直平台
             * receivedStatus : 未领取
             * receiveNum : 0
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
    }

    public static class TransReceiveOrderDetailVosBean implements Serializable {
        /**
         * orderDetailId : 40288293667aa53e01667aa5fcf30001
         * isHaveNum : 1
         * counts : 2
         * cstId : 106
         * cstName : 晶体植入镊
         * cstSpec : 直平台
         * receivedStatus : 未领取
         * receiveNum : 0
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
        private String thingStore;
        private List<String> codeArray;
        public String OpName = "打开柜门";

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
}
