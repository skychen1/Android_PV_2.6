package high.rivamed.myapplication.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ruihua.libconsumables.ConsumableManager;

import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.bean.Event;
import high.rivamed.myapplication.utils.EventBusUtils;

/**
 * 项目名称:    Android_PV_2.6.6_416D
 * 创建者:      DanMing
 * 创建时间:    2019/4/22 17:54
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class RegistLockAdapter  extends BaseQuickAdapter<String, BaseViewHolder> {

   public RegistLockAdapter(int layoutResId, @Nullable List<String> data) {
      super(layoutResId, data);
   }
   private TextView mIdText;
   private TextView mItemDoorOpen0;
   private TextView mItemDoorOpen1;
   private TextView mItemSettingCloss;
   private TextView mItemSettingStats;
   private TextView mItemLightOpen2;
   private TextView mItemLightOpen3;
   private TextView mItemLightCloss;



   @Override
   protected void convert(BaseViewHolder helper, String item) {
      mIdText = (TextView) helper.getView(R.id.id_text);
      mItemDoorOpen0 = (TextView) helper.getView(R.id.item_door_open0);
      mItemDoorOpen1 = (TextView) helper.getView(R.id.item_door_open1);
      mItemSettingStats = (TextView) helper.getView(R.id.item_setting_stats);
      mItemLightOpen2 = (TextView) helper.getView(R.id.item_light_open2);
      mItemLightOpen3 = (TextView) helper.getView(R.id.item_light_open3);
      mItemLightCloss = (TextView) helper.getView(R.id.item_light_closs);
      mIdText.setText(item);
      mItemDoorOpen0.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            int ret = ConsumableManager.getManager().openDoor(item,0);
            EventBusUtils.post(new Event.lockType(1,ret,item,1));

         }
      });
      mItemDoorOpen1.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            int ret = ConsumableManager.getManager().openDoor(item,1);
            EventBusUtils.post(new Event.lockType(1,ret,item,1));

         }
      });
      mItemSettingStats.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            int ret = ConsumableManager.getManager().checkDoorState(item);
            EventBusUtils.post(new Event.lockType(2,ret,item));

         }
      });
      mItemLightOpen2.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            int i = ConsumableManager.getManager().openLight(item, 2);
            EventBusUtils.post(new Event.lockType(3,i,item,2));
         }
      });
      mItemLightOpen3.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            int i = ConsumableManager.getManager().openLight(item, 3);
            EventBusUtils.post(new Event.lockType(3,i,item,3));
         }
      });
      mItemLightCloss.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            int i = ConsumableManager.getManager().closeLight(item);
            EventBusUtils.post(new Event.lockType(4,i,item));
         }
      });
   }
}
