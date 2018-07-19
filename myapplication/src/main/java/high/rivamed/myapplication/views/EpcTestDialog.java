package high.rivamed.myapplication.views;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import high.rivamed.myapplication.R;
import high.rivamed.myapplication.adapter.EpcTestAdapter;
import high.rivamed.myapplication.bean.Movie;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/7/19 16:22
 * 描述:        Recyclerview的2个按钮的dialog
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class EpcTestDialog extends Dialog {

   public EpcTestDialog(Context context) {
	super(context);
   }

   public EpcTestDialog(Context context, int theme) {
	super(context, theme);
   }

   public static class Builder {

	ImageView    mDialogCloss;
	EditText     mSearchEt;
	TextView     mEpctestNumberAll;
	RecyclerView mEpctestRecyclerview;

	private Context mContext;

	public Builder(Context context) {
	   this.mContext = context;
	}

	public EpcTestDialog create() {
	   LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
		   Context.LAYOUT_INFLATER_SERVICE);
	   final EpcTestDialog dialog = new EpcTestDialog(mContext, R.style.Dialog);
	   dialog.setCancelable(false);
	   View layout = inflater.inflate(R.layout.dialog_epctest_layout, null);
	   dialog.addContentView(layout, new ViewGroup.LayoutParams(850,
											ViewGroup.LayoutParams.WRAP_CONTENT));

	   mSearchEt = (EditText) layout.findViewById(R.id.search_et);
	   mDialogCloss = (ImageView) layout.findViewById(R.id.dialog_closs);
	   mEpctestNumberAll = (TextView) layout.findViewById(R.id.epctest_number_all);
	   mEpctestRecyclerview = (RecyclerView) layout.findViewById(R.id.epctest_recyclerview);
	   LinearLayout mLinearLayout = (LinearLayout) layout.findViewById(R.id.all_ll);

	   mEpctestRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
	   mEpctestRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));

	   EpcTestAdapter epcTestAdapter = new EpcTestAdapter(R.layout.item_epctest_two_layout,
										genData5());
	   mEpctestRecyclerview.setAdapter(epcTestAdapter);
	   mLinearLayout.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
		   mSearchEt.clearFocus();
		}
	   });
	   ViewGroup.LayoutParams lp = mEpctestRecyclerview.getLayoutParams();
	   if (genData5().size() > 11) {
		lp.height = 62*6;
	   } else {
		lp.height = 62 * genData5().size();
	   }
	   mEpctestRecyclerview.setLayoutParams(lp);
	   mDialogCloss.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
		   dialog.dismiss();
		}
	   });
	   return dialog;
	}
   }


   public static List<Movie> genData5() {

	ArrayList<Movie> list = new ArrayList<>();

	for (int i = 0; i < 15; i++) {

	   String two = "" + i;
	   String one = "1829374289374983274" + i;

	   Movie movie = new Movie(one, two, null, null, null, null, null, null);
	   list.add(movie);
	}
	return list;
   }
}
