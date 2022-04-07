package com.flance.web.utils;


import java.util.List;

public interface TreeModel<T, ID> {

    ID getId();

    ID getParentId();

    Integer getSort();

    List<T> getChildren();

    void setChildren(List<T> list);

}
