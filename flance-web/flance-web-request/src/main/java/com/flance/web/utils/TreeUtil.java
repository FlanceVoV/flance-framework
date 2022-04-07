package com.flance.web.utils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TreeUtil {

    public static <T extends TreeModel<Object>> List<T> getRoot(List<T> list, Object rootValue) {
        return list.stream()
                .filter(item -> rootValue.equals(item.getParentId()))
                .peek(tree -> tree.setChildren(parseTree(tree, list)))
                .sorted(Comparator.comparingInt(node -> (node.getSort() == null ? 0 : node.getSort())))
                .collect(Collectors.toList());
    }

    private static <T extends TreeModel<Object>> List<T> parseTree(T t, List<T> list) {
        return list.stream()
                .filter(item -> t.getId().toString().equals(item.getParentId().toString()))
                .peek(item -> item.setChildren(parseTree(item, list)))
                .sorted(Comparator.comparingInt(node -> (node.getSort() == null ? 0 : node.getSort())))
                .collect(Collectors.toList());
    }

}
