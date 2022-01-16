package com.example.demo;

import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public abstract class CommonUtil {
    public static <T> boolean canUpdate(T to, T from) {
        if(from != null && !from.equals(to)) {
            return true;
        }
        return false;
    }

    public static <T> boolean canUpdateAlbeitNull(T to, T from) {
        if(from == null) {
            return to != null;
        }
        return !from.equals(to);
    }

    public static <T> boolean merge(T targetObj, T sourceObj) {
        if (sourceObj == null) {
            return false;
        }

        if(targetObj.getClass() != sourceObj.getClass()) {
            throw new IllegalArgumentException("The two parameter objects should be the same class");
        }
        //Lambda 안에서 updated 를 접근하기 위해 AtomicReference 를 사용했습니다.
        final AtomicReference<Boolean> updated = new AtomicReference<>(false);

        List<String> mergedList = new LinkedList<>();

        // 바로 이 메소드! package org.springframework.util 에 존재
        ReflectionUtils.doWithFields(targetObj.getClass(),
                field -> {
                    // 필드에 어떤 작업을 매번 수행할지 정의하는 부분. 기존 코드에서와 동일하게 적용
                    field.setAccessible(true);
                    Object oldValue = field.get(targetObj);
                    Object newValue = field.get(sourceObj);
                    boolean canMerge = false;
                    final Annotation annotation = field.getAnnotation(Merge.class);
                    if(((Merge)annotation).ignoreNull()) {
                        // 기본형 ( source 필드 null 은 무시)
                        if(canUpdate(oldValue, newValue)) {
                            canMerge = true;
                        }
                    } else {
                        // null 이 올라오면 null 로 변경
                        if(canUpdateAlbeitNull(oldValue, newValue)) {
                            canMerge = true;
                        }
                    }

                    if(canMerge) {
                        field.set(targetObj, newValue);
                        updated.set(true);
                        mergedList.add(String.format("%s : %s -> %s", field.getName(), oldValue, newValue));
                    }

                },
                field -> {
                    // 애초에 위 코드를 수행할 필드를 필터해서 가져올수 있게 정의하는 부분. merge 어노테이션 있는 애들만 가져오게 함
                    final Annotation annotation = field.getAnnotation(Merge.class);
                    return annotation != null ;
                });

        if(updated.get()) {
            System.out.println(mergedList);
        }
        return updated.get();
    }

    public abstract boolean merge(Object sourceObj) throws Exception;
}
