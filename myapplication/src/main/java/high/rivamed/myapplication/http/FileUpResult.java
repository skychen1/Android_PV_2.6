package high.rivamed.myapplication.http;

import com.lzy.okgo.model.Progress;

/**
 * 文件上传
 */
public interface FileUpResult extends NetResult {
    void uploadProgress(Progress progress);
}
