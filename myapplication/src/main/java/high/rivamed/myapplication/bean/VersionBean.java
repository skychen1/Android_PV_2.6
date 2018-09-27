package high.rivamed.myapplication.bean;

import java.io.Serializable;

/**
 * 项目名称:    Android_PV_2.6.1
 * 创建者:      DanMing
 * 创建时间:    2018/9/26 18:17
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class VersionBean implements Serializable{

   /**
    * id : 4028829966136ddd016613b543dd0000
    * version : 2.6.2.0.1
    * desc : 1
    * apkFileName : myapplication-release.apk
    */

   private String id;
   private String version;
   private String desc;
   private String apkFileName;

   public String getId() { return id;}

   public void setId(String id) { this.id = id;}

   public String getVersion() { return version;}

   public void setVersion(String version) { this.version = version;}

   public String getDesc() { return desc;}

   public void setDesc(String desc) { this.desc = desc;}

   public String getApkFileName() { return apkFileName;}

   public void setApkFileName(String apkFileName) { this.apkFileName = apkFileName;}
}
