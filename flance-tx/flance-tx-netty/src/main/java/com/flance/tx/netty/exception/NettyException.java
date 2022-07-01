package com.flance.tx.netty.exception;

import com.flance.web.utils.AssertException;

public class NettyException extends AssertException {

    private final String msg;

    private final String code;

    public NettyException(String msg, String code) {
        super(code, msg);
        this.msg = msg;
        this.code = code;
    }

    public NettyException(NettyErrorEnum errorEnum) {
        super(errorEnum.getCode(), errorEnum.getMsg());
        this.code = errorEnum.getCode();
        this.msg = errorEnum.getMsg();
    }

    public enum NettyErrorEnum {

        NETTY_ERROR_TX_CANNOT_CONNECTION("000001", "netty事务中心无法连接"),
        NETTY_ERROR_TX_EXEC_SQL_ERROR("000002", "事务中心 sql 执行失败"),
        ;

        private final String msg;

        private final String code;

        NettyErrorEnum(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }

        public String getCode() {
            return code;
        }
    }

}
