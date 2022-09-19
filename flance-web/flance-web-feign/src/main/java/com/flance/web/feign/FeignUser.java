package com.flance.web.feign;

import lombok.Data;

@Data
public class FeignUser {

    public static final String HEADER_FEIGN_PASS = "feign_pass";

    public static final String HEADER_FEIGN_USER = "feign_user";

    private String user;

    private String pass;

}
