package org.example.insurance_policy_system.util;

public class Util {

    public static <T> void test(T ...data){
        System.out.println("count of data: "+ data.length);
        for(T t:data){
            System.out.println(data);
        }
    }
}
