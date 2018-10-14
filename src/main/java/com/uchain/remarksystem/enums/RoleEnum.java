package com.uchain.remarksystem.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;

@Getter
@AllArgsConstructor
public enum RoleEnum {
    ANON(0,"anon"),
    REMARK(1,"remark"),
    INSPECTOR(2,"inspector"),
    EXAMINER(3,"examiner"),
    ADMIN(4,"admin")
    ;
    private Integer value;
    private String role;

    public static String getRole(Integer integer){
        HashMap<Integer,String> hashMap = new HashMap();
        hashMap.put(ANON.getValue(),ANON.getRole());
        hashMap.put(REMARK.getValue(),REMARK.getRole());
        hashMap.put(INSPECTOR.getValue(),INSPECTOR.getRole());
        hashMap.put(EXAMINER.getValue(),EXAMINER.getRole());
        hashMap.put(ADMIN.getValue(),ADMIN.getRole());
        return hashMap.get(integer);
    }

    public static Integer getValue(String role){
        HashMap<String,Integer> hashMap = new HashMap();
        hashMap.put(ANON.getRole(),ANON.getValue());
        hashMap.put(REMARK.getRole(),REMARK.getValue());
        hashMap.put(INSPECTOR.getRole(),INSPECTOR.getValue());
        hashMap.put(EXAMINER.getRole(),EXAMINER.getValue());
        hashMap.put(ADMIN.getRole(),ADMIN.getValue());
        return hashMap.get(role);
    }
}
