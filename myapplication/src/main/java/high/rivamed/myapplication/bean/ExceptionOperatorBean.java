package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称：高值
 * 创建者：chenyanling
 * 创建时间：2019/5/13
 * 描述：异常处理-操作人数据
 */
public class ExceptionOperatorBean implements Serializable {

    /**
     * operateSuccess : true
     * pageModel : {"pageNo":1,"pageSize":10,"rows":[{"name":"初广秀","accountId":"ff80818169473ecf016951c38f7200f5","dbCenterUserId":"001101","roleName":"护士长（诊间计费）,管理员（普通库房）,管理员（一级库盘点）,高值耗材基础服务（管理员）,护士长（高值耗材柜终端）"},{"name":"测试账号","accountId":"ff808181695c79ec016974b0394a0005","dbCenterUserId":"ceshi","roleName":"管理员（普通库房）,管理员（一级库盘点）"},{"name":"系统管理员","accountId":"10000000000000000000000000000001","dbCenterUserId":"001","roleName":"系统管理员,护士长（诊间计费）,管理员（普通库房）,管理员（一级库盘点）,护士长（高值耗材管理系统）,高值耗材基础服务（管理员）,护士长（高值耗材柜终端）"},{"name":"超级管理员","accountId":"10000000000000000000000000000000"},{"name":"陈旭豪","accountId":"ff80818169473ecf016951ed005a00fb","dbCenterUserId":"002","roleName":"护士长（诊间计费）,护士长（高值耗材管理系统）,高值耗材基础服务（管理员）,护士长（高值耗材柜终端）"},{"name":"魏小波","accountId":"ff80818167e886340167e8fe1c4a0002","dbCenterUserId":"11111111"}],"total":6}
     * id : 0
     * opFlg : 200
     * pageNo : 1
     * pageSize : 20
     * term :
     */

    private boolean operateSuccess;
    private PageModelBean pageModel;
    private int           id;
    private String        opFlg;
    private int           pageNo;
    private int           pageSize;
    private String        term;

    public boolean isOperateSuccess() { return operateSuccess;}

    public void setOperateSuccess(boolean operateSuccess) { this.operateSuccess = operateSuccess;}

    public PageModelBean getPageModel() { return pageModel;}

    public void setPageModel(PageModelBean pageModel) { this.pageModel = pageModel;}

    public int getId() { return id;}

    public void setId(int id) { this.id = id;}

    public String getOpFlg() { return opFlg;}

    public void setOpFlg(String opFlg) { this.opFlg = opFlg;}

    public int getPageNo() { return pageNo;}

    public void setPageNo(int pageNo) { this.pageNo = pageNo;}

    public int getPageSize() { return pageSize;}

    public void setPageSize(int pageSize) { this.pageSize = pageSize;}

    public String getTerm() { return term;}

    public void setTerm(String term) { this.term = term;}

    public static class PageModelBean {

        /**
         * pageNo : 1
         * pageSize : 10
         * rows : [{"name":"初广秀","accountId":"ff80818169473ecf016951c38f7200f5","dbCenterUserId":"001101","roleName":"护士长（诊间计费）,管理员（普通库房）,管理员（一级库盘点）,高值耗材基础服务（管理员）,护士长（高值耗材柜终端）"},{"name":"测试账号","accountId":"ff808181695c79ec016974b0394a0005","dbCenterUserId":"ceshi","roleName":"管理员（普通库房）,管理员（一级库盘点）"},{"name":"系统管理员","accountId":"10000000000000000000000000000001","dbCenterUserId":"001","roleName":"系统管理员,护士长（诊间计费）,管理员（普通库房）,管理员（一级库盘点）,护士长（高值耗材管理系统）,高值耗材基础服务（管理员）,护士长（高值耗材柜终端）"},{"name":"超级管理员","accountId":"10000000000000000000000000000000"},{"name":"陈旭豪","accountId":"ff80818169473ecf016951ed005a00fb","dbCenterUserId":"002","roleName":"护士长（诊间计费）,护士长（高值耗材管理系统）,高值耗材基础服务（管理员）,护士长（高值耗材柜终端）"},{"name":"魏小波","accountId":"ff80818167e886340167e8fe1c4a0002","dbCenterUserId":"11111111"}]
         * total : 6
         */

        private int pageNo;
        private int pageSize;
        private int total;
        private List<RowsBean> rows;

        public int getPageNo() { return pageNo;}

        public void setPageNo(int pageNo) { this.pageNo = pageNo;}

        public int getPageSize() { return pageSize;}

        public void setPageSize(int pageSize) { this.pageSize = pageSize;}

        public int getTotal() { return total;}

        public void setTotal(int total) { this.total = total;}

        public List<RowsBean> getRows() { return rows;}

        public void setRows(List<RowsBean> rows) { this.rows = rows;}

        public static class RowsBean {

            /**
             * name : 初广秀
             * accountId : ff80818169473ecf016951c38f7200f5
             * dbCenterUserId : 001101
             * roleName : 护士长（诊间计费）,管理员（普通库房）,管理员（一级库盘点）,高值耗材基础服务（管理员）,护士长（高值耗材柜终端）
             */

            private String name;
            private String accountId;
            private String dbCenterUserId;
            private String roleName;
            private boolean isSelected;

            public boolean isSelected() {
                return isSelected;
            }

            public void setSelected(boolean selected) {
                isSelected = selected;
            }

            public String getName() { return name;}

            public void setName(String name) { this.name = name;}

            public String getAccountId() { return accountId;}

            public void setAccountId(String accountId) { this.accountId = accountId;}

            public String getDbCenterUserId() { return dbCenterUserId;}

            public void setDbCenterUserId(
                  String dbCenterUserId) { this.dbCenterUserId = dbCenterUserId;}

            public String getRoleName() { return roleName;}

            public void setRoleName(String roleName) { this.roleName = roleName;}
        }
    }
}
