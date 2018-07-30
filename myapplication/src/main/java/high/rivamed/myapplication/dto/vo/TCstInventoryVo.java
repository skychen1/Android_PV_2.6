package high.rivamed.myapplication.dto.vo;

import java.io.Serializable;
import java.util.Date;

public class TCstInventoryVo implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String expiration_first = "001";	//近效期第一阶段
	public static final String expiration_second = "002";	//近效期第二阶段
	public static final String expiration_third = "003";	//近效期第三阶段	
	public static final String noComfirm_day = "004";
	
	
	private String cstName;
	private String epc;
	private String cstSpec;
	private Date expirationTime;
	private String expiration;
	private String deviceName;
	private String status;

	// 0 已过期
	private Integer stopFlag = 1;
	private String storehouseCode;
	private String deviceCode;
	private String cstCode;
	private String operation;
	private String storehouseRemark;
	private String remake;
	private Long countStock = 0l; // 库存情况
	private Long countActual = 0l; // 扫描出来的库存

	private Long count = 0l;
	private Date lastUpdateDate;
	private String userName;
	// 前端状态显示字段

	private String statusStr;

	public TCstInventoryVo() {

	}
	public TCstInventoryVo(String cstName,Date expirationTime) {
		super();
		this.cstName = cstName;
		this.expirationTime = expirationTime;
	}


	public TCstInventoryVo(String cstSpec, String deviceName, Long countActual) {
		super();
		this.cstSpec = cstSpec;
		this.deviceName = deviceName;
		this.countActual = countActual;
	}
	public TCstInventoryVo(String cstName, String cstSpec,String cstCode, Date expirationTime, String deviceName, String deviceCode,
			Long countStock) {
		super();
		this.cstName = cstName;
		this.cstSpec = cstSpec;
		this.cstCode=cstCode;
		this.expirationTime = expirationTime;
		this.deviceName = deviceName;
		this.deviceCode = deviceCode;
		this.countStock = countStock;
	}
	public TCstInventoryVo(String cstName, String cstSpec,String cstCode, Date expirationTime, String deviceName, Long countActual,String deviceCode
			) {
		super();
		this.cstName = cstName;
		this.cstSpec = cstSpec;
		this.cstCode=cstCode;
		this.expirationTime = expirationTime;
		this.deviceName = deviceName;
		this.deviceCode = deviceCode;
		this.countActual = countActual;
	}
	public TCstInventoryVo(String cstName, String cstSpec, String epc, Date expirationTime, String deviceName,
			String deviceCode) {
		super();
		this.cstName = cstName;
		this.cstSpec = cstSpec;
		this.expirationTime = expirationTime;
		this.deviceName = deviceName;
		this.deviceCode = deviceCode;
		this.epc = epc;
	}
//
//	public TCstInventoryVo(String cstName, String epc, String cstSpec, Date expirationTime, String deviceName,
//			String status) {
//		super();
//		this.cstName = cstName;
//		this.epc = epc;
//		this.cstSpec = cstSpec;
//		this.expirationTime = expirationTime;
//		this.deviceName = deviceName;
//		this.status = status;
//	}
	
	public TCstInventoryVo(String cstName, String cstSpec,  String cstCode,String epc, Date expirationTime, String deviceName,
			String deviceCode) {
		super();
		this.cstName = cstName;
		this.cstSpec = cstSpec;
		this.cstSpec = cstSpec;
		this.expirationTime = expirationTime;
		this.deviceName = deviceName;
		this.deviceCode = deviceCode;
		this.epc = epc;
		this.cstCode=cstCode;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TCstInventoryVo tCstInventoryVo = (TCstInventoryVo) obj;
		if (cstCode == null) {
			if (tCstInventoryVo.cstCode != null)
				return false;
		} else if (!cstCode.equals(tCstInventoryVo.cstCode))
			return false;
		if (cstName == null) {
			if (tCstInventoryVo.cstName != null)
				return false;
		} else if (!cstName.equals(tCstInventoryVo.cstName))
			return false;
		if (cstSpec == null) {
			if (tCstInventoryVo.cstSpec != null)
				return false;
		} else if (!cstSpec.equals(tCstInventoryVo.cstSpec))
			return false;
		if (epc == null) {
			if (tCstInventoryVo.epc != null)
				return false;
		} else if (!epc.equals(tCstInventoryVo.epc))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	public TCstInventoryVo(String cstName,String epc, Date lastUpdateDate, String status, Date expirationTime) {
		super();
		this.cstName = cstName;
		this.epc = epc;
		this.expirationTime = expirationTime;
		this.status = status;
		this.lastUpdateDate = lastUpdateDate;
	}

	public TCstInventoryVo(String cstName, String epc, String cstSpec, Date expirationTime, String deviceName,
			String status, String storehouseCode, String deviceCode, String cstCode) {
		super();
		this.cstName = cstName;
		this.epc = epc;
		this.cstSpec = cstSpec;
		this.expirationTime = expirationTime;
		this.deviceName = deviceName;
		this.status = status;
		this.storehouseCode = storehouseCode;
		this.deviceCode = deviceCode;
		this.cstCode = cstCode;
	}

	public TCstInventoryVo(String cstName, String cstSpec, Date expirationTime, String deviceName, String deviceCode,
			String cstCode, Long count) {
		super();
		this.cstName = cstName;
		this.cstSpec = cstSpec;
		this.expirationTime = expirationTime;
		this.deviceName = deviceName;
		this.deviceCode = deviceCode;
		this.cstCode = cstCode;
		this.count = count;
	}


	public TCstInventoryVo(String cstName, String epc, String cstSpec, Date expirationTime, String deviceName,
			Date lastUpdateDate, String userName) {
		this.cstName = cstName;
		this.epc = epc;
		this.cstSpec = cstSpec;
		this.expirationTime = expirationTime;
		this.deviceName = deviceName;
		this.epc = epc;
		this.expirationTime = expirationTime;
		this.lastUpdateDate = lastUpdateDate;
		this.userName = userName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getExpiration() {
		return expiration;
	}

	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}

	public Integer getStopFlag() {
		return stopFlag;
	}

	public void setStopFlag(Integer stopFlag) {
		this.stopFlag = stopFlag;
	}

	public String getStorehouseCode() {
		return storehouseCode;
	}

	public void setStorehouseCode(String storehouseCode) {
		this.storehouseCode = storehouseCode;
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

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getStorehouseRemark() {
		return storehouseRemark;
	}

	public void setStorehouseRemark(String storehouseRemark) {
		this.storehouseRemark = storehouseRemark;
	}

	public String getRemake() {
		return remake;
	}

	public void setRemake(String remake) {
		this.remake = remake;
	}

	public Long getCountStock() {
		return countStock;
	}

	public void setCountStock(Long countStock) {
		this.countStock = countStock;
	}

	public Long getCountActual() {
		return countActual;
	}

	public void setCountActual(Long countActual) {
		this.countActual = countActual;
	}

}
