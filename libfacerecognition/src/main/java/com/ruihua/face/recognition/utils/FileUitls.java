/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.ruihua.face.recognition.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

public class FileUitls {

    /**
     * Checks if is sd card available.
     *
     * @return true, if is sd card available
     */
    public static boolean isSdCardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * Gets the SD root file.
     *
     * @return the SD root file
     */
    public static File getSDRootFile() {
        if (isSdCardAvailable()) {
            return Environment.getExternalStorageDirectory();
        } else {
            return null;
        }
    }

    public static File getFaceDirectory() {
        File sdRootFile = getSDRootFile();
        File file = null;
        if (sdRootFile != null && sdRootFile.exists()) {
            file = new File(sdRootFile, "faces");
            if (!file.exists()) {
                boolean success = file.mkdirs();
            }
        }

        return file;
    }

    public static File getBatchFaceDirectory(String batchDir) {
        File sdRootFile = getSDRootFile();
        File file = null;
        if (sdRootFile != null && sdRootFile.exists()) {
            file = new File(sdRootFile, batchDir);
            if (!file.exists()) {
                boolean success = file.mkdirs();
            }
        }

        return file;
    }

    public static boolean saveFile(File file, Bitmap bitmap) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean checklicense(Context context, String licenseName) {
//        String filePath = context.getFilesDir().getParent() + "/" + licenseName;
//        File file = new File(filePath);
//        if (file.exists()) {
//            return true;
//        }

        String[] content = d(context, licenseName);
        if (content == null || content.length == 0) {
            return false;
        } else  {
            return true;
        }
    }

    public static String[] d(Context var1, String licenseName) {
        ArrayList var2 = null;
        InputStream var3 = null;

        String var7;
        label163:
        {
            try {
                BufferedReader var5 = null;
                InputStreamReader var6 = null;
                var3 = a(var1, licenseName);
                Log.e("Face-SDK", "open license file path " + licenseName);
                if (null != var3) {
                    var6 = new InputStreamReader(var3);
                    var5 = new BufferedReader(var6);
                    var2 = new ArrayList();

                    while (true) {
                        String var4;
                        if ((var4 = var5.readLine()) == null) {
                            break label163;
                        }

                        Log.e("Face-SDK", "readLine " + var4);
                        var2.add(var4);
                    }
                }

                Log.e("Face", "open license file error.");
                var7 = null;
            } catch (FileNotFoundException var22) {
                var22.printStackTrace();
                break label163;
            } catch (IOException var23) {
                var23.printStackTrace();
                break label163;
            } catch (Exception var24) {
                var24.printStackTrace();
                break label163;
            } finally {
                if (var3 != null) {
                    try {
                        var3.close();
                    } catch (IOException var21) {
                        var21.printStackTrace();
                    }
                }

            }
        }

        String[] var26 = null;
        if(var2 != null && var2.size() > 0) {
            c(var1, licenseName, var2);
            var26 = new String[var2.size()];
            int var27 = 0;

            for(Iterator var28 = var2.iterator(); var28.hasNext(); ++var27) {
                var7 = (String)var28.next();
                var26[var27] = var7;
                Log.e("License-SDK", "license file info =" + var7);
            }
        }

        return var26;
    }


    public static InputStream a(Context var0, String var1) {
        if(var0 == null) {
            return null;
        } else {
            Object var2 = b(var0, var1);
            Log.e("Face", "read_license_from_data");
            if(var2 == null) {
                Log.e("Face", "read_license_from_asset");
                // var2 = c(var0, var1);
            }

            return (InputStream)var2;
        }
    }

    private static FileInputStream b(Context var0, String var1) {
        if(var0 == null) {
            return null;
        } else {
            FileInputStream var2 = null;

            try {
                File var3 = var0.getDir(var1, 0);
                if(var3 == null || !var3.exists() || !var3.isFile()) {
                    Log.e("Face", "read_license_from_data file not found");
                    return null;
                }

                var2 = new FileInputStream(var3);
            } catch (FileNotFoundException var4) {
                Log.e("Face", "read_license_from_data FileNotFoundException");
                var4.printStackTrace();
            } catch (Exception var5) {
                Log.e("Face", "read_license_from_data Exception " + var5.getMessage());
                var5.printStackTrace();
            }

            return var2;
        }
    }

    public static boolean c(Context var0, String var1, ArrayList<String> var2) {
        Log.e("Face", "write_license_content");
        if(var2 != null && var2.size() != 0 && var0 != null) {
            boolean var3 = true;
            File var4 = var0.getDir(var1, 0);
            if(var4 != null && var4.exists()) {
                var4.delete();
            }

            if(var4 != null && !var4.exists()) {
                try {
                    var4.createNewFile();
                } catch (IOException var19) {
                    Log.e("Face", "write_license_content IOException");
                    var19.printStackTrace();
                }
            }

            FileOutputStream var5 = null;

            try {
                var5 = new FileOutputStream(var4);
                Iterator var6 = var2.iterator();

                while(var6.hasNext()) {
                    String var7 = (String)var6.next();
                    var5.write(var7.getBytes());
                    var5.write(10);
                }
            } catch (FileNotFoundException var20) {
                var3 = false;
                Log.e("Face", "write_license_content FileNotFoundException");
                var20.printStackTrace();
            } catch (IOException var21) {
                var3 = false;
                Log.e("Face", "write_license_content IOException");
                var21.printStackTrace();
            } finally {
                if(var5 != null) {
                    try {
                        var5.close();
                    } catch (IOException var18) {
                        var3 = false;
                        var18.printStackTrace();
                    }
                }
            }
            return var3;
        } else {
            return false;
        }
    }

    public static void deleteLicense(Context context, String licenseName) {
        String filePath = context.getFilesDir().getParent() + "/" + licenseName;
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }

        File var4 = context.getDir(licenseName, 0);
        if(var4 != null && var4.exists()) {
            var4.delete();
        }
    }

}
