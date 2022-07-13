package com.flance.tx.demo1;

import com.flance.tx.common.utils.ThreadUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test {

    public static void main(String[] args) {
        TestIn testIn = new TestIn();
        new Thread(testIn::run).start();
        for (int i = 0; i< 100000; i++) {
            log.info(i + "");
        }
        testIn.setFlag(false);
    }



}
