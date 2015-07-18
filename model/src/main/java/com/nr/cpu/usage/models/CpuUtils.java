package com.nr.cpu.usage.models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Set of utils for gathering information about CPU usage
 */
public final class CpuUtils {
    private static final String CPU_INFO = "proc/cpuinfo";
    private static final String PROC_STAT = "/proc/stat";
    private static final String PARAM_DELIMITER = " ";
    private static final int PROC_STAT_PARAMS_COUNT = 7;
    private static final int VALUABLE_PROCSTAT_PARAMETERS_COUNT = 3;

    private static final String ID_CPU = "cpu";
    private static final String ID_PROCESSOR = "processor";

    private static Integer cpu_count = null;

    private CpuUtils(){}

    /**
     * Returns number of CPUs in the system
     * @return number of CPUs in the system
     */
    public static final int getCpuCount(){
        if(cpu_count == null) {
            innerGetCpuCount();
        }
        return cpu_count;
    }

    private static void innerGetCpuCount() {
        cpu_count = 0;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(CPU_INFO));

            String line;
            while ((line = br.readLine()) != null) {
                if (line.toLowerCase().contains(ID_PROCESSOR)) {
                    cpu_count++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Returns raw load output per CPU
     * @return list of raw load outputs per CPU
     *  (index of the element in the list is the index of CPU)
     */
    public static final List<long[]> getCpuUsageRaw(){
        List<long[]> result = new ArrayList<>(getCpuCount());

        Map<String, String[]> lines = new HashMap<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(PROC_STAT));

            String line;
            while ((line = br.readLine()) != null) {
                if (line.toLowerCase().contains(ID_CPU)) {
                    String[] tokens = line.split(PARAM_DELIMITER);
                    if(tokens != null && tokens.length > 0){
                        lines.put(tokens[0], tokens);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        for(int i=0;i<getCpuCount();i++){
            String[] tokens = lines.get(ID_CPU+i);
            if(tokens == null){
                result.add(null);
            }else{
                long[] tmp = new long[PROC_STAT_PARAMS_COUNT];
                for(int j=0;j<PROC_STAT_PARAMS_COUNT;j++){
                    try {
                        tmp[j] = Long.parseLong(tokens[j+1]);
                    }catch (Exception e){
                        e.printStackTrace();
                        tmp[j] = 0L;
                    }
                }
                result.add(tmp);
            }
        }

        return result;
    }

    /**
     * Returns usage per CPU in percents from 0 to 100
     * @param prev last CPU usage values array
     * @param now fresh CPUU usage values array
     * @return usage per CPU in percents from 0 to 100
     */
    public static final int[] getCpuUsage(List<long[]> prev, List<long[]> now){
        int cpuCount = getCpuCount();
        int[] result = new int[cpuCount];
        if(prev != null && now != null &&
                prev.size() == cpuCount && now.size() == cpuCount){
            for(int i=0;i<cpuCount;i++) {
                long[] prevArr = prev.get(i);
                long[] nowArr = now.get(i);

                if(prevArr == null || nowArr == null){
                    result[i] = 0;
                }else {
                    long prevTotal = getTotalLoad(prevArr);
                    long nowTotal = getTotalLoad(nowArr);
                    long prevWork = getWorkLoad(prevArr);
                    long nowWork = getWorkLoad(nowArr);
                    double cpuResult = 0.0d;
                    if(nowTotal - prevTotal != 0){
                        cpuResult = Math.abs(((nowWork - prevWork) * 100.0d)/
                                (double)(nowTotal - prevTotal));
                    }
                    result[i] = (int)cpuResult;
                }
            }
        }

        return result;
    }

    private static long getTotalLoad(long[] args){
        long result = 0;
        for(int i=0;i<args.length;i++){
            result += args[i];
        }
        return result;
    }

    private static long getWorkLoad(long[] args){
        long result = 0;
        for(int i=0;i<VALUABLE_PROCSTAT_PARAMETERS_COUNT;i++){
            result += args[i];
        }
        return result;
    }
}