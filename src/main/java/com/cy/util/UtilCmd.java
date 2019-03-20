package com.cy.util;

import com.cy.data.UtilString;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by cy on 2017/6/22.
 */
public class UtilCmd {
    public static String exec(String cmd) {

        String[] cmds= new String[3];

        if (UtilEnv.isLinux() || UtilEnv.isMac()) {
            cmds = new String[]{"/bin/sh","-c",cmd};
        }else if (UtilEnv.isWindows()){
            cmds = new String[]{"cmd","/C",cmd};
        }

        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = null;
        boolean hasSuccessMsg=false;
        StringBuilder errorMsg = null;
        boolean hasErrorMsg=false;

        try {
            process = Runtime.getRuntime().exec(cmds);

            successMsg = new StringBuilder();
            errorMsg = new StringBuilder();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;

            while ((s = successResult.readLine()) != null) {
                successMsg.append(s).append("\n");
                hasSuccessMsg=true;
            }
            if (hasSuccessMsg){
                successMsg.deleteCharAt(successMsg.length()-1);
            }


            while ((s = errorResult.readLine()) != null) {
                errorMsg.append(s).append("\n");
                hasErrorMsg=true;
            }
            if (hasErrorMsg){
                errorMsg.deleteCharAt(errorMsg.length()-1);
            }
//            InputStreamReader ir = new InputStreamReader(process.getInputStream());
//            InputStreamReader er = new InputStreamReader(process.getErrorStream());
//            LineNumberReader input = new LineNumberReader(ir);

//            String line;
//            while ((line = input.readLine()) != null) {
//                results.add(line);
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (process != null) {
                process.destroy();
            }

        }
        return UtilString.isEmpty(successMsg.toString())?errorMsg.toString():successMsg.toString();
    }
}
