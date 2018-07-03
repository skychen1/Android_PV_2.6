package high.rivamed.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/19 17:17
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public  class Movie implements Serializable {

   public  String         one;
   public  String         two;
   public  String         three;
   public  String         four;
   public  String         five;
   public  String         six;
   public  String         seven;
   public  String         eight;
   private List<ListBean> list;
   public  String         mString;
   public List<ListBean> getList() { return list;}

   public void setList(List<ListBean> list) { this.list = list;}
   public String getString() { return mString;}

   public void setString(String title) { this.mString = title;}

   public int number1;
   public int number2;
   public int number3;

   public Movie(
	   String one, String two, String three, String four, String five, String six, String seven,
	   String eight) {
	this.one = one;
	this.two = two;
	this.three = three;
	this.four = four;
	this.five = five;
	this.six = six;
	this.seven = seven;
	this.eight = eight;

   }
   public Movie(
	   int one, int two, int three) {
	this.number1 = one;
	this.number2 = two;
	this.number3 = three;

   }
   public Movie(String one) {
	this.mString = one;


   }
   public static class ListBean {
	public String mString;

	public String getString() { return mString;}

	public void setString(String title) { this.mString = title;}

   }
}
