package com.flance.web.utils;


import java.util.List;

public interface TreeModel<ID> {

    ID getId();

    ID getParentId();

    Integer getSort();

    List<? extends TreeModel<ID>> getChildren();

    void setChildren(List<? extends TreeModel<ID>> list);

}
