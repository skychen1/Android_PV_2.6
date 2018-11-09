package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: new2.6.3
 * @Package: high.rivamed.myapplication.bean
 * @ClassName: FindBillOrderBean
 * @Description: 套组领用--根据EPC获取耗材列表
 * @Author: Amos_Bo
 * @CreateDate: 2018/10/31 15:31
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/10/31 15:31
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class FindBillOrderBean implements Serializable {
    /**
     * cstPlan : {"id":"5"}
     * cstInventoryVos : [{"epc":"000000002C10201809040035"},{"epc":"000012341219201809070163"}]
     */

    private CstPlanBean cstPlan;
    private List<CstInventoryVosBean> cstInventoryVos;

    public CstPlanBean getCstPlan() {
        return cstPlan;
    }

    public void setCstPlan(CstPlanBean cstPlan) {
        this.cstPlan = cstPlan;
    }

    public List<CstInventoryVosBean> getCstInventoryVos() {
        return cstInventoryVos;
    }

    public void setCstInventoryVos(List<CstInventoryVosBean> cstInventoryVos) {
        this.cstInventoryVos = cstInventoryVos;
    }

    public static class CstPlanBean {
        /**
         * id : 5
         */

        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public static class CstInventoryVosBean implements Serializable {
        /**
         * epc : 000000002C10201809040035
         */

        private String epc;

        public String getEpc() {
            return epc;
        }

        public void setEpc(String epc) {
            this.epc = epc;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else if (obj instanceof CstInventoryVosBean) {
                CstInventoryVosBean customString = (CstInventoryVosBean) obj;
                return customString.epc.equals(epc);
            } else {
                return false;
            }
        }
    }
}
