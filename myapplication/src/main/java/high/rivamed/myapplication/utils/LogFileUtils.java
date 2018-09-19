package high.rivamed.myapplication.utils;

import java.io.File;

public class LogFileUtils {

    public static void reMoveLogFile(String path) {
        File file = new File(path);
        if (!file.exists())
            file.mkdirs();
        long size = getFileSize(file) / 1024 / 1024;//单位 M
        if (size > 100) {
            deleteFiles(file);
        }
    }

    private static long getFileSize(File dir) {
        long fileSize = 0;
        if (!dir.isDirectory()) {
            fileSize += dir.length();
        } else {
            for (File file : dir.listFiles()) {
                if (!file.isDirectory()) {
                    fileSize += file.length();
                } else {
                    getFileSize(file);
                }
            }
        }
        return fileSize;
    }

    private static boolean deleteFiles(File dir) {
        if (dir == null && !dir.exists()) {
            return false;
        }
        if (dir.isFile() || dir.listFiles() == null) {
            dir.delete();
            return true;
        } else {
            int MAX_DELETE = 5;
            for (File file : dir.listFiles()) {
                if (file.isFile()) {
                    if (MAX_DELETE == 0) {
                        break;
                    } else {
                        file.delete();
                        MAX_DELETE--;
                    }
                } else if (file.isDirectory()) {
                    deleteFiles(file);
                }
            }
        }
        return true;
    }
}
