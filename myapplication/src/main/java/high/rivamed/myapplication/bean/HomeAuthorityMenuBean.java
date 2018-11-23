package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6.3
 * 创建者:      DanMing
 * 创建时间:    2018/11/23 13:51
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class HomeAuthorityMenuBean implements Serializable{

   /**
    * path : ''
    * title : 耗材操作
    * icon : null
    * parentSeq : null
    * seq : 2
    * menu : null
    * children : [{"path":"\u2018\u2019","title":"选择操作","icon":"\u2018\u2019","parentSeq":2,"seq":7,"menu":null,"children":[{"path":"\u2018\u2019","title":"领用","icon":"\u2018\u2019","parentSeq":7,"seq":8,"menu":null,"children":null,"selected":false},{"path":"\u2018\u2019","title":"入库","icon":"\u2018\u2019","parentSeq":7,"seq":9,"menu":null,"children":null,"selected":false},{"path":"\u2018\u2019","title":"移出","icon":"\u2018\u2019","parentSeq":7,"seq":10,"menu":null,"children":null,"selected":false},{"path":"\u2018\u2019","title":"调拨","icon":"\u2018\u2019","parentSeq":7,"seq":11,"menu":null,"children":null,"selected":false},{"path":"\u2018\u2019","title":"移入","icon":"\u2018\u2019","parentSeq":7,"seq":12,"menu":null,"children":null,"selected":false},{"path":"\u2018\u2019","title":"退回","icon":"\u2018\u2019","parentSeq":7,"seq":13,"menu":null,"children":null,"selected":false},{"path":"\u2018\u2019","title":"退货","icon":"\u2018\u2019","parentSeq":7,"seq":14,"menu":null,"children":null,"selected":false}],"selected":false}]
    * selected : false
    */

   private String path;
   private String              title;
   private Object              icon;
   private Object              parentSeq;
   private int                 seq;
   private Object              menu;
   private boolean             selected;
   private List<ChildrenBeanX> children;

   public String getPath() { return path;}

   public void setPath(String path) { this.path = path;}

   public String getTitle() { return title;}

   public void setTitle(String title) { this.title = title;}

   public Object getIcon() { return icon;}

   public void setIcon(Object icon) { this.icon = icon;}

   public Object getParentSeq() { return parentSeq;}

   public void setParentSeq(Object parentSeq) { this.parentSeq = parentSeq;}

   public int getSeq() { return seq;}

   public void setSeq(int seq) { this.seq = seq;}

   public Object getMenu() { return menu;}

   public void setMenu(Object menu) { this.menu = menu;}

   public boolean isSelected() { return selected;}

   public void setSelected(boolean selected) { this.selected = selected;}

   public List<ChildrenBeanX> getChildren() { return children;}

   public void setChildren(List<ChildrenBeanX> children) { this.children = children;}

   public static class ChildrenBeanX {

	/**
	 * path : ‘’
	 * title : 选择操作
	 * icon : ‘’
	 * parentSeq : 2
	 * seq : 7
	 * menu : null
	 * children : [{"path":"\u2018\u2019","title":"领用","icon":"\u2018\u2019","parentSeq":7,"seq":8,"menu":null,"children":null,"selected":false},{"path":"\u2018\u2019","title":"入库","icon":"\u2018\u2019","parentSeq":7,"seq":9,"menu":null,"children":null,"selected":false},{"path":"\u2018\u2019","title":"移出","icon":"\u2018\u2019","parentSeq":7,"seq":10,"menu":null,"children":null,"selected":false},{"path":"\u2018\u2019","title":"调拨","icon":"\u2018\u2019","parentSeq":7,"seq":11,"menu":null,"children":null,"selected":false},{"path":"\u2018\u2019","title":"移入","icon":"\u2018\u2019","parentSeq":7,"seq":12,"menu":null,"children":null,"selected":false},{"path":"\u2018\u2019","title":"退回","icon":"\u2018\u2019","parentSeq":7,"seq":13,"menu":null,"children":null,"selected":false},{"path":"\u2018\u2019","title":"退货","icon":"\u2018\u2019","parentSeq":7,"seq":14,"menu":null,"children":null,"selected":false}]
	 * selected : false
	 */

	private String path;
	private String             title;
	private String             icon;
	private int                parentSeq;
	private int                seq;
	private Object             menu;
	private boolean            selected;
	private List<ChildrenBean> children;

	public String getPath() { return path;}

	public void setPath(String path) { this.path = path;}

	public String getTitle() { return title;}

	public void setTitle(String title) { this.title = title;}

	public String getIcon() { return icon;}

	public void setIcon(String icon) { this.icon = icon;}

	public int getParentSeq() { return parentSeq;}

	public void setParentSeq(int parentSeq) { this.parentSeq = parentSeq;}

	public int getSeq() { return seq;}

	public void setSeq(int seq) { this.seq = seq;}

	public Object getMenu() { return menu;}

	public void setMenu(Object menu) { this.menu = menu;}

	public boolean isSelected() { return selected;}

	public void setSelected(boolean selected) { this.selected = selected;}

	public List<ChildrenBean> getChildren() { return children;}

	public void setChildren(List<ChildrenBean> children) { this.children = children;}

	public static class ChildrenBean {

	   /**
	    * path : ‘’
	    * title : 领用
	    * icon : ‘’
	    * parentSeq : 7
	    * seq : 8
	    * menu : null
	    * children : null
	    * selected : false
	    */

	   private String path;
	   private String  title;
	   private String  icon;
	   private int     parentSeq;
	   private int     seq;
	   private Object  menu;
	   private Object  children;
	   private boolean selected;

	   public String getPath() { return path;}

	   public void setPath(String path) { this.path = path;}

	   public String getTitle() { return title;}

	   public void setTitle(String title) { this.title = title;}

	   public String getIcon() { return icon;}

	   public void setIcon(String icon) { this.icon = icon;}

	   public int getParentSeq() { return parentSeq;}

	   public void setParentSeq(int parentSeq) { this.parentSeq = parentSeq;}

	   public int getSeq() { return seq;}

	   public void setSeq(int seq) { this.seq = seq;}

	   public Object getMenu() { return menu;}

	   public void setMenu(Object menu) { this.menu = menu;}

	   public Object getChildren() { return children;}

	   public void setChildren(Object children) { this.children = children;}

	   public boolean isSelected() { return selected;}

	   public void setSelected(boolean selected) { this.selected = selected;}
	}
   }
}
