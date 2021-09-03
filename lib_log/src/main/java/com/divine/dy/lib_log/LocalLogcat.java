package com.divine.dy.lib_log;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author: Divine
 * CreateDate: 2021/4/30
 * Describe:
 * log日志统计保存
 */
public class LocalLogcat {
    private static final String TAG = "DY";
    private static LocalLogcat INSTANCE = null;
    private static String PATH_LOGCAT;
    private LogDumper mLogDumper = null;
    private int mPId;
    private Context context;

    /**
     * 初始化目录
     */
    private void init(Context context) {
        this.context = context;
        PATH_LOGCAT = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator + "DY-LOG";
        File file = new File(PATH_LOGCAT);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static LocalLogcat getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LocalLogcat(context);
        }
        return INSTANCE;
    }

    private LocalLogcat(Context context) {
        init(context);
        mPId = android.os.Process.myPid();
    }

    public void start() {
        if (mLogDumper == null) {
            mLogDumper = new LogDumper(String.valueOf(mPId), PATH_LOGCAT);
        }
        mLogDumper.start();
    }

    public void stop() {
        if (mLogDumper != null) {
            mLogDumper.stopLogs();
            mLogDumper = null;
        }
    }

    private class LogDumper extends Thread {

        private Process logcatProc;
        private BufferedReader mReader = null;
        private boolean mRunning = true;
        String cmds = null;
        private String mPID;
        private FileOutputStream out = null;
        private String fileName;

        public LogDumper(String pid, String dir) {
            Date now = new Date(); // 创建一个Date对象，获取当前时间
            // 指定格式化格式
            SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
            fileName = f.format(now) + ".log";
            Log.e("print", fileName);
            mPID = pid;
            try {
                File file = new File(dir, TAG + "-" + fileName);
                Log.e("print", file.getAbsolutePath() + " 文件");
                File dirFile = new File(dir);
                if (!dirFile.exists()) {
                    boolean dirCreated = dirFile.mkdirs();
                    if (dirCreated) {
                        if (!file.exists()) {
                            //                            Toast.makeText(context, "GMT-" + fileName + "文件创建中！", Toast.LENGTH_SHORT).show();
                            Log.e("print", TAG + "-" + fileName + "文件创建中！");
                            boolean created = file.createNewFile();
                            if (created) {
                                //                                Toast.makeText(context, "GMT-" + fileName + "文件创建成功！", Toast.LENGTH_SHORT).show();
                                Log.e("print", TAG + "-" + fileName + "文件创建成功！");
                                out = new FileOutputStream(file);
                            } else {
                                //                                Toast.makeText(context, "GMT-" + fileName + "文件创建失败！", Toast.LENGTH_SHORT).show();
                                Log.e("print", TAG + "-" + fileName + "文件创建失败！");
                            }
                        } else {
                            //                            Toast.makeText(context, "GMT-" + fileName + "文件已存在！", Toast.LENGTH_SHORT).show();
                            Log.e("print", TAG + "-" + fileName + "文件已存在！");
                        }
                    } else {
                        Log.e("print", TAG + "-" + fileName + "文件目录失败！");

                    }
                } else {
                    //                    Toast.makeText(context, "GMT-" + fileName + "文件目录存在！", Toast.LENGTH_SHORT).show();
                    Log.e("print", TAG + "-" + fileName + "文件目录存在！");
                    if (!file.exists()) {
                        //                        Toast.makeText(context, "GMT-" + fileName + "文件创建中！", Toast.LENGTH_SHORT).show();
                        Log.e("print", TAG + "-" + fileName + "文件创建中！");
                        boolean created = file.createNewFile();
                        if (created) {
                            //                            Toast.makeText(context, "GMT-" + fileName + "文件创建成功！", Toast.LENGTH_SHORT).show();
                            Log.e("print", TAG + "-" + fileName + "文件创建成功！");
                            out = new FileOutputStream(file);
                        } else {
                            //                            Toast.makeText(context, "GMT-" + fileName + "文件创建失败！", Toast.LENGTH_SHORT).show();
                            Log.e("print", TAG + "-" + fileName + "文件创建失败！");
                        }
                    } else {
                        //                        Toast.makeText(context, "GMT-" + fileName + "文件已存在！", Toast.LENGTH_SHORT).show();
                        Log.e("print", TAG + "-" + fileName + "文件已存在！");
                    }
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                // TODO Auto-generated catch block
                Log.e("print:catch", e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("print:catch", e.getMessage());
            }

            /**
             *
             * 日志等级：*:v , *:d , *:w , *:e , *:f , *:s
             *
             * 显示当前mPID程序的 E和W等级的日志.
             *
             * */

            // cmds = "logcat *:e *:w | grep \"(" + mPID + ")\"";
            // cmds = "logcat  | grep \"(" + mPID + ")\"";//打印所有日志信息
            // cmds = "logcat -s way";//打印标签过滤信息
            cmds = "logcat *:e *:i | grep \"(" + mPID + ")\"";

        }

        public void stopLogs() {
            mRunning = false;
        }

        @Override
        public void run() {
            try {
                logcatProc = Runtime.getRuntime().exec(cmds);
                mReader = new BufferedReader(new InputStreamReader(
                        logcatProc.getInputStream()), 1024);
                String line = null;
                while (mRunning && (line = mReader.readLine()) != null) {
                    if (!mRunning) {
                        break;
                    }
                    if (line.length() == 0) {
                        continue;
                    }
                    if (out != null && line.contains(mPID)) {
                        out.write((new Date().getTime() + "  " + line + "\n")
                                          .getBytes());
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (logcatProc != null) {
                    logcatProc.destroy();
                    logcatProc = null;
                }
                if (mReader != null) {
                    try {
                        mReader.close();
                        mReader = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    out = null;
                }

            }

        }

    }

}
