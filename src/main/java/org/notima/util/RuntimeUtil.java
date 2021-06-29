package org.notima.util;

import java.beans.PropertyChangeSupport;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class RuntimeUtil
{
    public static int runExternalCmd(final List<String> cmd, final PropertyChangeSupport swingWorker, final Properties customEnv) throws Exception {
        final ProcessBuilder pb = new ProcessBuilder(cmd);
        final Map<String, String> env = pb.environment();
        if (customEnv != null && !customEnv.isEmpty()) {
            for (final Object key : customEnv.keySet()) {
                env.put(key.toString(), customEnv.getProperty(key.toString()));
            }
        }
        final Process p = pb.start();
        final BufferedInputStream is = new BufferedInputStream(p.getInputStream());
        final BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        final BufferedInputStream es = new BufferedInputStream(p.getErrorStream());
        final BufferedReader er = new BufferedReader(new InputStreamReader(es, "UTF-8"));
        final Runnable errReaderT = new Runnable() {
            @Override
            public void run() {
                try {
                    String line;
                    while ((line = er.readLine()) != null) {
                        if (swingWorker != null) {
                            swingWorker.firePropertyChange("stderr", null, line);
                        }
                        else {
                            System.err.println(line);
                        }
                    }
                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        };
        final Runnable outReaderT = new Runnable() {
            @Override
            public void run() {
                try {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (swingWorker != null) {
                            swingWorker.firePropertyChange("stdout", null, line);
                        }
                        else {
                            System.out.println(line);
                        }
                    }
                }
                catch (IOException ioe1) {
                    ioe1.printStackTrace();
                }
            }
        };
        final Runnable extRunnerT = new Runnable() {
            @Override
            public void run() {
                try {
                    p.waitFor();
                }
                catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        };
        final Thread errReaderThread = new Thread(errReaderT, "errReader");
        final Thread outReaderThread = new Thread(outReaderT, "outReader");
        final Thread extRunnerThread = new Thread(extRunnerT, "runner");
        errReaderThread.start();
        outReaderThread.start();
        extRunnerThread.start();
        while (extRunnerThread.isAlive()) {
            extRunnerThread.join(5000L);
        }
        return p.exitValue();
    }
}
