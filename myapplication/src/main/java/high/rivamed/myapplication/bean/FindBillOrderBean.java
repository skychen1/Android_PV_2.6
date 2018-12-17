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
     * inventoryVos : [{"epc":"000000002C10201809040035"},{"epc":"000012341219201809070163"}]
     */
    private List<String>       deviceIds;
    private List<InventoryVos> inventoryVos;
    public List<String> getDeviceIds() {
        return deviceIds;
    }
    private String suiteId;

    public String getSuiteId() {
        return suiteId;
    }

    public void setSuiteId(String suiteId) {
        this.suiteId = suiteId;
    }

    public void setDeviceIds(List<String> deviceIds) {
        this.deviceIds = deviceIds;
    }



    public List<InventoryVos> getInventoryVos() {
        return inventoryVos;
    }

    public void setInventoryVos(List<InventoryVos> inventoryVos) {
        this.inventoryVos = inventoryVos;
    }

    public static class InventoryVos implements Serializable {
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
            } else if (obj instanceof InventoryVos) {
                InventoryVos customString = (InventoryVos) obj;
                return customString.epc.equals(epc);
            } else {
                return false;
            }
        }
    }
}
