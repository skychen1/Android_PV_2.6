package high.rivamed.myapplication.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * @ProjectName: InterOfficeBilling
 * @Package: com.rivamed.interofficebilling.utils
 * @ClassName: RxUtils
 * @Description: Rxjava工具类
 * @Author: Amos_Bo
 * @CreateDate: 2019/3/20 13:43
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/3/20 13:43
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class RxUtils {
    private List<Disposable> mDisposableList;
    private RxUtils() {
        if (mDisposableList == null) {
            mDisposableList = new ArrayList<>();
        }
    }

    private static class SingletonClassInstance {
        private static final RxUtils instance = new RxUtils();
    }

    public static RxUtils getInstance() {
        return SingletonClassInstance.instance;
    }

//    /**
//     * 输入框监听
//     *
//     * @param editText
//     * @param listener
//     */
//    public void setSeachResultListener(EditText editText, SearchDebounceResultListener listener) {
//        Observable.create((ObservableOnSubscribe<String>) e -> editText.addTextChangedListener(new SingleTextWatchListener() {
//            @Override
//            public void afterTextChanged(Editable s) {
//                e.onNext(s.toString().trim());
//            }
//        })).debounce(500, TimeUnit.MILLISECONDS)
//                .filter((String s) -> s.length() > 0)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<String>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        mDisposableList.add(d);
//                    }
//
//                    @Override
//                    public void onNext(String s) {
//                        listener.goSerach(s);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//    }

    /**
     * EPC扫描监听
     *
     * @param observableOnSubscribe
     * @param listener
     */
    public void setEpcResultListener(BaseEpcObservable observableOnSubscribe,
                                     EpcDebounceResultListener listener) {
        Observable.create(observableOnSubscribe).debounce(1000, TimeUnit.MILLISECONDS)
                .filter((List<String> s) -> s.size() > 0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposableList.add(d);
                    }

                    @Override
                    public void onNext(List<String> s) {
                        listener.goEpcSearch(s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public interface SearchDebounceResultListener {
        /**
         * 回调方法
         *
         * @param searchKey
         */
        void goSerach(String searchKey);
    }

    public interface EpcDebounceResultListener {
        /**
         * 回调方法
         *
         * @param epcs
         */
        void goEpcSearch(List<String> epcs);
    }

    /**
     * 注销监听
     */
    public void unRegister() {
        for (int i = 0, len = mDisposableList.size(); i < len; i++) {
            mDisposableList.get(i).dispose();
        }
        mDisposableList.clear();
    }

    abstract public static class BaseEpcObservable implements ObservableOnSubscribe<List<String>> {
        private List<String> mMyEpcList = new ArrayList<>();
        private ObservableEmitter<List<String>> observableEmitter;
        public BaseEpcObservable() {
            initScan();
        }

        private void addEpc(String epc, ObservableEmitter<List<String>> e) {
            if (!mMyEpcList.contains(epc)) {
                mMyEpcList.add(epc);
                e.onNext(mMyEpcList);
            }
        }

        /**
         * 扫描的EPC
         *
         * @return
         */
        public void getScanEpc(String epc) {
            addEpc(epc, observableEmitter);
        }

        public abstract void initScan();

        @Override
        public void subscribe(ObservableEmitter<List<String>> e) throws Exception {
            observableEmitter = e;
        }
    }
}
