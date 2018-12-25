package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: new2.6.3
 * @Package: high.rivamed.myapplication.bean
 * @ClassName: OrderSheetBean
 * @Description: 医嘱单领用-从消息页面跳转来的嘱单列表数据
 * @Author: Amos_Bo
 * @CreateDate: 2018/10/26 13:43
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/10/26 13:43
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class OrderSheetFromMsgBean implements Serializable {

    /**
     * pageModel : {"pageNo":1,"pageSize":10,"rows":[{"id":"40288293667aa53e01667aa5fc610016","userName":"高级管理员","accountId":"8a80cb8164d9b3940164da1bff760005","patientId":"577404","patientName":"马羊焕","operationScheduleId":"180907ysl0469663","deptId":null,"operationRoomName":"导管手术室4","createTime":"2018-11-01 10:13:10","status":"3"},{"id":"40288293667aa53e01667aa5fc610017","userName":"高级管理员","accountId":"8a80cb8164d9b3940164da1bff760005","patientId":"576542","patientName":"张培珅","operationScheduleId":"180907ysl0469663","deptId":null,"operationRoomName":"导管手术室4","createTime":"2018-11-02 10:13:10","status":"3"}],"total":2}
     * id : 0
     * transReceiveOrderDetailVos : []
     * transReceiveOrders : [{"id":"40288293667aa53e01667aa5fc610016","userName":"高级管理员","accountId":"8a80cb8164d9b3940164da1bff760005","patientId":"577404","patientName":"马羊焕","operationScheduleId":"180907ysl0469663","deptId":null,"operationRoomName":"导管手术室4","createTime":"2018-11-01 10:13:10","status":"3"},{"id":"40288293667aa53e01667aa5fc610017","userName":"高级管理员","accountId":"8a80cb8164d9b3940164da1bff760005","patientId":"576542","patientName":"张培珅","operationScheduleId":"180907ysl0469663","deptId":null,"operationRoomName":"导管手术室4","createTime":"2018-11-02 10:13:10","status":"3"}]
     * countNum : 0
     * kindsOfCst : 0
     */

    private PageModelBean                pageModel;
    private int                          id;
    private int                          countNum;
    private int                          kindsOfCst;
    private List<?>                      transReceiveOrderDetailVos;
    private List<TransReceiveOrdersBean> transReceiveOrders;

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

    public List<?> getTransReceiveOrderDetailVos() {
        return transReceiveOrderDetailVos;
    }

    public void setTransReceiveOrderDetailVos(List<?> transReceiveOrderDetailVos) {
        this.transReceiveOrderDetailVos = transReceiveOrderDetailVos;
    }

    public List<TransReceiveOrdersBean> getTransReceiveOrders() {
        return transReceiveOrders;
    }

    public void setTransReceiveOrders(List<TransReceiveOrdersBean> transReceiveOrders) {
        this.transReceiveOrders = transReceiveOrders;
    }

    public static class PageModelBean {
        /**
         * pageNo : 1
         * pageSize : 10
         * rows : [{"id":"40288293667aa53e01667aa5fc610016","userName":"高级管理员","accountId":"8a80cb8164d9b3940164da1bff760005","patientId":"577404","patientName":"马羊焕","operationScheduleId":"180907ysl0469663","deptId":null,"operationRoomName":"导管手术室4","createTime":"2018-11-01 10:13:10","status":"3"},{"id":"40288293667aa53e01667aa5fc610017","userName":"高级管理员","accountId":"8a80cb8164d9b3940164da1bff760005","patientId":"576542","patientName":"张培珅","operationScheduleId":"180907ysl0469663","deptId":null,"operationRoomName":"导管手术室4","createTime":"2018-11-02 10:13:10","status":"3"}]
         * total : 2
         */

        private int pageNo;
        private int pageSize;
        private int total;
        private List<OrderSheetBean.RowsBean> rows = new ArrayList<>();

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

        public List<OrderSheetBean.RowsBean> getRows() {
            return rows;
        }

        public void setRows(List<OrderSheetBean.RowsBean> rows) {
            this.rows = rows;
        }

    }

    public static class TransReceiveOrdersBean {
        /**
         * id : 40288293667aa53e01667aa5fc610016
         * userName : 高级管理员
         * accountId : 8a80cb8164d9b3940164da1bff760005
         * patientId : 577404
         * patientName : 马羊焕
         * operationScheduleId : 180907ysl0469663
         * deptId : null
         * operationRoomName : 导管手术室4
         * createTime : 2018-11-01 10:13:10
         * status : 3
         */

        private String id;
        private String userName;
        private String accountId;
        private String patientId;
        private String patientName;
        private String operationScheduleId;
        private Object deptId;
        private String operationRoomName;
        private String createTime;
        private String status;

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

        public Object getDeptId() {
            return deptId;
        }

        public void setDeptId(Object deptId) {
            this.deptId = deptId;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
