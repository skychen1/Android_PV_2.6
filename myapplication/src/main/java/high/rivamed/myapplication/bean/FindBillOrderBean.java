package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

import high.rivamed.myapplication.dto.vo.InventoryVo;

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
    private List<String>      deviceIds;
    private List<String>      epcs;
    private List<InventoryVo> inventoryVos;
    public List<String> getDeviceIds() {
        return deviceIds;
    }
    private String suiteId;//请领单
    private String orderId;//套组
    private String thingId;

    public String getThingId() {
        return thingId;
    }

    public void setThingId(String thingId) {
        this.thingId = thingId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<String> getEpcs() {
        return epcs;
    }

    public void setEpcs(List<String> epcs) {
        this.epcs = epcs;
    }

    public String getSuiteId() {
        return suiteId;
    }

    public void setSuiteId(String suiteId) {
        this.suiteId = suiteId;
    }

    public void setDeviceIds(List<String> deviceIds) {
        this.deviceIds = deviceIds;
    }



    public List<InventoryVo> getInventoryVos() {
        return inventoryVos;
    }

    public void setInventoryVos(List<InventoryVo> inventoryVos) {
        this.inventoryVos = inventoryVos;
    }


}
