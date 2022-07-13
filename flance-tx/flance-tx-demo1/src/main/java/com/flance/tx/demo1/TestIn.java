package com.flance.tx.demo1;

import com.flance.tx.common.utils.ThreadUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j

public class TestIn {

    private boolean flag = true;

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void run() {
        while (flag) {
            for (int i = 100000; i< 200000; i++) {
                log.info(i + "");
            }
        }
    }

}