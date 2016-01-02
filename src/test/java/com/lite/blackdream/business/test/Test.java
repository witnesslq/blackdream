package com.lite.blackdream.business.test;

import com.lite.blackdream.framework.util.FileUtil;
import com.lite.blackdream.framework.util.ZipUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author LaineyC
 * @date 2015-11-13
 */
public class Test {

    public static void main(String[] args){
/*
        File f = new File("D:\\BlackDream\\Codebase\\2634484800816128");
        try {
            ZipUtils.compress(f);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        List<Integer> a = new ArrayList<>();
        for(int i = 0 ; i < 1 ; i++){
            a.add(i);
        }
        System.out.println(a.subList(0, 1));

        String[] array = new String[]{"a", "b", "c"};
        for(String bb : Arrays.copyOf(array, 4)){
            System.out.println(bb);
        }
*/
        try {
            try {
                int a = 1 / 0;
            }
            catch (Exception e){
                throw new RuntimeException("dadadae",e);
            }
        }
        catch (Exception e){

            List<String> messageList = new ArrayList<>();
            LinkedList<Throwable> exceptionStack = new LinkedList<>();
            exceptionStack.push(e);
            while (!exceptionStack.isEmpty()) {
                Throwable exception = exceptionStack.pop();
                messageList.add(exception.toString());
                if(exception.getCause() != null){
                    exceptionStack.push(exception.getCause());
                }
                //exception.get
                //dataModel.getChildren().forEach(stack :: push);
            }
           for(String message : messageList){
               System.out.println(message);
           }

        }

    }

}
