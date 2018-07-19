package high.rivamed.myapplication.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import static high.rivamed.myapplication.adapter.RegisteTestAdapter.TYPE_TEST_CONTEXT;
import static high.rivamed.myapplication.adapter.RegisteTestAdapter.TYPE_TEST_TITLE;
import static high.rivamed.myapplication.adapter.RegisteTestAdapter.TYPE_TEST_TITLE_BOX;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/18 17:13
 * 描述:        工程模式中
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class RegisteTestBean extends AbstractExpandableItem<RegisteTestBean.RegisteTestTitleBean>
	implements MultiItemEntity {

   public List<RegisteTestTitleBean>   listtitle;
   public List<RegisteTestContextBean> listcontext;
	public String namebox;
   public RegisteTestBean(
	   String nametitle) {
	this.namebox = nametitle;

   }

   @Override
   public int getLevel() {
	return 0;
   }

   @Override
   public int getItemType() {
	return TYPE_TEST_TITLE_BOX;
   }

   public static class RegisteTestTitleBean extends AbstractExpandableItem<RegisteTestBean.RegisteTestContextBean>implements MultiItemEntity {

	public String nametitle;
	public String testtitle;
	public String msgtitle;
	public String commentstitle;

	public RegisteTestTitleBean(
		String nametitle, String testtitle, String msgtitle, String commentstitle) {
	   this.nametitle = nametitle;
	   this.testtitle = testtitle;
	   this.msgtitle = msgtitle;
	   this.commentstitle = commentstitle;
	}

	@Override
	public int getItemType() {
	   return TYPE_TEST_TITLE;
	}

	@Override
	public int getLevel() {
	   return 0;
	}
   }

   public static class RegisteTestContextBean implements MultiItemEntity {

	public String name;
	public String test;
	public String comments;
	public String type;

	public RegisteTestContextBean(
		String name, String test, String type,String comments) {
	   this.name = name;
	   this.test = test;
	   this.type = type;
	   this.comments = comments;
	}

	@Override
	public int getItemType() {
	   return TYPE_TEST_CONTEXT;
	}
   }
}


