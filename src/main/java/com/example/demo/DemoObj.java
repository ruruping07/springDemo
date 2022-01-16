package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class DemoObj extends CommonUtil{
    public enum POSITION {
        MANAGER, SOFTWARE_ENGINEER, CEO
    }

    public String userId;
    @Merge
    public String userName;
    @Merge
    public boolean isActive;
    @Merge
    public int userLevel;
    @Merge
    public POSITION skilltree;

    @Override
    public boolean merge(Object sourceObj) throws Exception {
        return CommonUtil.merge(this, sourceObj);
    }
}
