package high.rivamed.myapplication.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者: 钱凯
 * @创建时间: 2018/8/29
 * @功能描述:查询在院患者bean
 */
public class FindInPatientBean {

    /**
     * pageNo : 1
     * pageSize : 10
     * rows : [{"patientId":"180810sl02256931","patientName":"陈桂英","deptName":"120急救","operatingRoomNoName":null,"operationSurgeonName":null,"operationBeginDateTime":null,"updateTime":null,"loperPatsId":null},{"patientId":"180813lia0334681","patientName":"鲍亚靖","deptName":null,"operatingRoomNoName":"骨科手术室","operationSurgeonName":"张凯","operationBeginDateTime":null,"updateTime":null,"loperPatsId":"1"},{"patientId":"180802zyh0827273","patientName":"李彦佳","deptName":"120急救","operatingRoomNoName":null,"operationSurgeonName":null,"operationBeginDateTime":null,"updateTime":null,"loperPatsId":"40288299657a150301657a19df440003"},{"patientId":"180723msy0249714","patientName":"苗淑女","deptName":"120急救","operatingRoomNoName":null,"operationSurgeonName":null,"operationBeginDateTime":null,"updateTime":null,"loperPatsId":"40288299657a150301657a1b8e960004"}]
     * total : 4
     */

    private int pageNo;
    private int pageSize;
    private int total;
    private List<RowsBean> rows = new ArrayList<>();

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

    public static class RowsBean {
        /**
         * patientId : 180810sl02256931
         * patientName : 陈桂英
         * deptName : 120急救
         * operatingRoomNoName : null
         * operationSurgeonName : null
         * operationBeginDateTime : null
         * updateTime : null
         * loperPatsId : null
         */

        private String patientId = "";
        private String patientName = "";
        private String deptName = "";
        private String operatingRoomNoName = "";
        private String operationSurgeonName = "";
        private String operationBeginDateTime = "";
        private String updateTime = "";
        private String loperPatsId = "";
        private boolean isSelected;

        public String getLpatsInId() {
            return lpatsInId;
        }

        public void setLpatsInId(String lpatsInId) {
            this.lpatsInId = lpatsInId;
        }

        private String lpatsInId = "";

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

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public String getOperatingRoomNoName() {
            return operatingRoomNoName;
        }

        public void setOperatingRoomNoName(String operatingRoomNoName) {
            this.operatingRoomNoName = operatingRoomNoName;
        }

        public String getOperationSurgeonName() {
            return operationSurgeonName;
        }

        public void setOperationSurgeonName(String operationSurgeonName) {
            this.operationSurgeonName = operationSurgeonName;
        }

        public String getOperationBeginDateTime() {
            return operationBeginDateTime;
        }

        public void setOperationBeginDateTime(String operationBeginDateTime) {
            this.operationBeginDateTime = operationBeginDateTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getLoperPatsId() {
            return loperPatsId;
        }

        public void setLoperPatsId(String loperPatsId) {
            this.loperPatsId = loperPatsId;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }




    }
}
