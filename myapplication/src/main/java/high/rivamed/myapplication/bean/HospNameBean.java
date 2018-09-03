package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/28 19:22
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class HospNameBean extends Movie implements Serializable {

   /**
    * id : 0
    * hospName : 协和
    * tbaseHospitals : [{"hospId":"1","address":null,"authorizeStatus":null,"bedNum":0,"businessNature":null,"creditCode":null,"displayOrder":0,"hospAliasName":null,"hospBitMap":null,"hospName":"北京协和医院","hospType":null,"level1":null,"level2":null,"licenseExpire":null,"licenseNo":null,"location":null,"reserve":null,"status":null,"tenantId":null,"verifyUnit":null}]
    */

   private List<TbaseHospitalsBean> tbaseHospitals;
   private List<StoreHousesBean> storeHouses;

   public HospNameBean(String one) {
	super(one);
   }

   public List<TbaseHospitalsBean> getTbaseHospitals() { return tbaseHospitals;}

   public void setTbaseHospitals(
	   List<TbaseHospitalsBean> tbaseHospitals) { this.tbaseHospitals = tbaseHospitals;}

   public List<StoreHousesBean> getStoreHouses() { return storeHouses;}

   public void setStoreHouses(List<StoreHousesBean> storeHouses) { this.storeHouses = storeHouses;}



   public static class TbaseHospitalsBean extends Movie{

	/**
	 * hospId : 1
	 * address : null
	 * authorizeStatus : null
	 * bedNum : 0
	 * businessNature : null
	 * creditCode : null
	 * displayOrder : 0
	 * hospAliasName : null
	 * hospBitMap : null
	 * hospName : 北京协和医院
	 * hospType : null
	 * level1 : null
	 * level2 : null
	 * licenseExpire : null
	 * licenseNo : null
	 * location : null
	 * reserve : null
	 * status : null
	 * tenantId : null
	 * verifyUnit : null
	 */

	private String hospId;
	private String hospName;

	public TbaseHospitalsBean(String one) {
	   super(one);
	}

	public String getHospId() { return hospId;}

	public void setHospId(String hospId) { this.hospId = hospId;}

	public String getHospName() { return hospName;}

	public void setHospName(String hospName) { this.hospName = hospName;}
   }



   /**
    * id : 0
    * tbaseInfo : [{"branchAlias":"骨科院区","branchCode":"22","deptName":null,"deptId":null},{"branchAlias":"脑科院区","branchCode":"233","deptName":null,"deptId":null}]
    * hospIds : ["1"]
    */


   private List<TbaseInfoBean> tbaseInfo;
   private List<String>                   hospIds;

   public List<TbaseInfoBean> getTbaseInfo() { return tbaseInfo;}

   public void setTbaseInfo(List<TbaseInfoBean> tbaseInfo) { this.tbaseInfo = tbaseInfo;}

   public List<String> getHospIds() { return hospIds;}

   public void setHospIds(List<String> hospIds) { this.hospIds = hospIds;}

   public static class TbaseInfoBean extends Movie{

	/**
	 * branchAlias : 骨科院区
	 * branchCode : 22
	 * deptName : null
	 * deptId : null
	 */

	private String branchAlias;
	private String branchCode;
	private String deptName;
	private String deptId;

	public TbaseInfoBean(String one) {
	   super(one);
	}

	public String getDeptName() { return deptName;}

	public void setDeptName(String deptName) { this.deptName = deptName;}

	public String getDeptCode() { return deptId;}

	public void setDeptCode(String deptId) { this.deptId = deptId;}

	public String getBranchAlias() { return branchAlias;}

	public void setBranchAlias(String branchAlias) { this.branchAlias = branchAlias;}

	public String getBranchCode() { return branchCode;}

	public void setBranchCode(String branchCode) { this.branchCode = branchCode;}
   }

   /**
    * id : 0
    * tbaseInfo : [{"branchAlias":null,"branchCode":null,"deptName":"骨科","deptId":"22"},{"branchAlias":null,"branchCode":null,"deptName":"谷志科 ","deptId":"44"},{"branchAlias":null,"branchCode":null,"deptName":"骨外科","deptId":"55"}]
    * branchCode : 22
    */

   private String                        branchCode;
   /**
    * id : 0
    * deptId : 22
    * tbaseOperationRooms : [{"createDateTime":"2018-07-22 11:12:59","roomNo":"1","status":0,"roomNoName":"骨科手术间1","updateDateTime":"2018-07-22 11:13:02","accountId":null,"deptNamesStr":null,"depts":[],"accountName":null},{"createDateTime":"2018-07-22 11:14:20","roomNo":"2","status":0,"roomNoName":"骨科手术间2","updateDateTime":"2018-07-22 11:14:23","accountId":null,"deptNamesStr":null,"depts":[],"accountName":null},{"createDateTime":"2018-07-22 11:14:46","roomNo":"3","status":0,"roomNoName":"脑科","updateDateTime":"2018-07-25 17:25:04","accountId":null,"deptNamesStr":null,"depts":[],"accountName":null}]
    */

   private int                           id;
   private String                        deptId;
   private List<TbaseOperationRoomsBean> tbaseOperationRooms;

   public int getId() { return id;}

   public void setId(int id) { this.id = id;}

   public String getDeptCode() { return deptId;}

   public void setDeptCode(String deptId) { this.deptId = deptId;}

   public List<TbaseOperationRoomsBean> getTbaseOperationRooms() { return tbaseOperationRooms;}

   public void setTbaseOperationRooms(
	   List<TbaseOperationRoomsBean> tbaseOperationRooms) { this.tbaseOperationRooms = tbaseOperationRooms;}

   public static class TbaseOperationRoomsBean extends Movie{

	/**
	 * createDateTime : 2018-07-22 11:12:59
	 * roomNo : 1
	 * status : 0
	 * roomNoName : 骨科手术间1
	 * updateDateTime : 2018-07-22 11:13:02
	 * accountId : null
	 * deptNamesStr : null
	 * depts : []
	 * accountName : null
	 */

	private String createDateTime;
	private String roomNo;
	private int    status;
	private String roomNoName;

	public TbaseOperationRoomsBean(String one) {
	   super(one);
	}

	public String getCreateDateTime() { return createDateTime;}

	public void setCreateDateTime(String createDateTime) { this.createDateTime = createDateTime;}

	public String getRoomNo() { return roomNo;}

	public void setRoomNo(String roomNo) { this.roomNo = roomNo;}

	public int getStatus() { return status;}

	public void setStatus(int status) { this.status = status;}

	public String getRoomNoName() { return roomNoName;}

	public void setRoomNoName(String roomNoName) { this.roomNoName = roomNoName;}
   }

   /**
    * id : 0
    * deptId : 22
    * tcstBaseStorehouses : [{"storehouseCode":"25","deptNamesStr":null,"name":"物资1库房","stopFlag":1,"storeType":1,"depts":[],"accountName":null,"lastupdateTime":null,"accountId":null},{"storehouseCode":"23","deptNamesStr":null,"name":"内科一库房","stopFlag":1,"storeType":1,"depts":[],"accountName":null,"lastupdateTime":null,"accountId":null},{"storehouseCode":"24","deptNamesStr":null,"name":"胸外科库房","stopFlag":1,"storeType":1,"depts":[],"accountName":null,"lastupdateTime":null,"accountId":null}]
    */

   private List<TcstBaseStorehousesBean> tcstBaseStorehouses;


   public List<TcstBaseStorehousesBean> getTcstBaseStorehouses() { return tcstBaseStorehouses;}

   public void setTcstBaseStorehouses(
	   List<TcstBaseStorehousesBean> tcstBaseStorehouses) { this.tcstBaseStorehouses = tcstBaseStorehouses;}

   public static class TcstBaseStorehousesBean extends Movie{

	/**
	 * storehouseCode : 25
	 * deptNamesStr : null
	 * name : 物资1库房
	 * stopFlag : 1
	 * storeType : 1
	 * depts : []
	 * accountName : null
	 * lastupdateTime : null
	 * accountId : null
	 */

	private String storehouseCode;
	private String name;

	public TcstBaseStorehousesBean(String one) {
	   super(one);
	}

	public String getStorehouseCode() { return storehouseCode;}

	public void setStorehouseCode(String storehouseCode) { this.storehouseCode = storehouseCode;}

	public String getName() { return name;}

	public void setName(String name) { this.name = name;}
   }

   public static class StoreHousesBean {

	/**
	 * deptNamesStr : 谷志科 胸外科库房
	 * storehouseCode : 24
	 */

	private String deptNamesStr;
	private String storehouseCode;

	public String getDeptNamesStr() { return deptNamesStr;}

	public void setDeptNamesStr(String deptNamesStr) { this.deptNamesStr = deptNamesStr;}

	public String getStorehouseCode() { return storehouseCode;}

	public void setStorehouseCode(String storehouseCode) { this.storehouseCode = storehouseCode;}
   }
}
