package high.rivamed.myapplication.dto;

import android.accounts.Account;

import java.util.List;

import high.rivamed.myapplication.dto.entity.TCstInventory;
import high.rivamed.myapplication.dto.vo.CstExpirationVo;
import high.rivamed.myapplication.dto.vo.DeviceInventoryVo;
import high.rivamed.myapplication.dto.vo.TCstInventoryVo;

/**
 * 
 * 描述: TODO<br/>
 * 公司: 北京瑞华康源科技有限公司<br/>
 * 版权: rivamed2018<br/>
 *
 * @author 魏小波
 * @date 2018-07-12 12:14:19
 * @version V1.0
 */
public class TCstInventoryDto {

	private static final long serialVersionUID = 1L;
	int id;
	private TCstInventory           tCstInventory;
	private List<TCstInventory>     tCstInventorys;
	private List<DeviceInventoryVo> deviceInventoryVos;
	private List<TCstInventoryVo>   tCstInventoryVos;
	
	//效期数量监控
	private List<CstExpirationVo> cstExpirationVos;
	
	private String  thingCode;
	private String  deviceCode;
	private int     operation;
	private String  requestResult;
	private Integer type;	//0 放入 1取出
	private String  cstSpec;
	private Account account;
	//名称及规格查询条件
	private String  nameOrSpecQueryCon;
	private Integer add;	// 库存情况
	private Integer reduce;	// 扫描出来的库存
	private String  cstCode;
	private String  remake;	//退货备注
	private String  storehouseRemark;	//移出备注
	private String  storehouseCode;		//调拨库房
	private int     stopFlag;		//效期情况 0过期 1-3近效期 4正常

	public String getRemake() {
		return remake;
	}

	public void setRemake(String remake) {
		this.remake = remake;
	}

	public String getStorehouseRemark() {
		return storehouseRemark;
	}

	public void setStorehouseRemark(String storehouseRemark) {
		this.storehouseRemark = storehouseRemark;
	}

	public String getStorehouseCode() {
		return storehouseCode;
	}

	public void setStorehouseCode(String storehouseCode) {
		this.storehouseCode = storehouseCode;
	}

	public int getOperation() {
		return operation;
	}

	public void setOperation(int operation) {
		this.operation = operation;
	}

	public TCstInventory getTCstInventory() {
		return tCstInventory;
	}

	public void setTCstInventory(TCstInventory tCstInventory) {
		this.tCstInventory = tCstInventory;
	}

	public List<TCstInventory> getTCstInventorys() {
		return tCstInventorys;
	}

	public void setTCstInventorys(List<TCstInventory> tCstInventorys) {
		this.tCstInventorys = tCstInventorys;
	}

	public String getThingCode() {
		return this.thingCode;
	}

	public void setThingCode(String thingCode) {
		this.thingCode = thingCode;
	}

	public List<TCstInventoryVo> gettCstInventoryVos() {
		return tCstInventoryVos;
	}

	public void settCstInventoryVos(List<TCstInventoryVo> tCstInventoryVos) {
		this.tCstInventoryVos = tCstInventoryVos;
	}

	public String getRequestResult() {
		return requestResult;
	}

	public void setRequestResult(String requestResult) {
		this.requestResult = requestResult;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public TCstInventory gettCstInventory() {
		return tCstInventory;
	}

	public void settCstInventory(TCstInventory tCstInventory) {
		this.tCstInventory = tCstInventory;
	}


	public String getNameOrSpecQueryCon() {
		return nameOrSpecQueryCon;
	}

	public List<CstExpirationVo> getCstExpirationVos() {
		return cstExpirationVos;
	}

	public void setCstExpirationVos(List<CstExpirationVo> cstExpirationVos) {
		this.cstExpirationVos = cstExpirationVos;
	}
	public void setNameOrSpecQueryCon(String nameOrSpecQueryCon) {
		this.nameOrSpecQueryCon = nameOrSpecQueryCon;
	}

	public String getCstSpec() {
		return cstSpec;
	}

	public void setCstSpec(String cstSpec) {
		this.cstSpec = cstSpec;
	}

	public Integer getAdd() {
		return add;
	}

	public void setAdd(Integer add) {
		this.add = add;
	}

	public Integer getReduce() {
		return reduce;
	}

	public void setReduce(Integer reduce) {
		this.reduce = reduce;
	}

	public List<DeviceInventoryVo> getDeviceInventoryVos() {
		return deviceInventoryVos;
	}

	public void setDeviceInventoryVos(List<DeviceInventoryVo> deviceInventoryVos) {
		this.deviceInventoryVos = deviceInventoryVos;
	}
	public List<TCstInventory> gettCstInventorys() {
		return tCstInventorys;
	}

	public void settCstInventorys(List<TCstInventory> tCstInventorys) {
		this.tCstInventorys = tCstInventorys;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	public String getCstCode() {
		return cstCode;
	}

	public void setCstCode(String cstCode) {
		this.cstCode = cstCode;
	}

	public int getStopFlag() {
		return stopFlag;
	}

	public void setStopFlag(int stopFlag) {
		this.stopFlag = stopFlag;
	}


}
