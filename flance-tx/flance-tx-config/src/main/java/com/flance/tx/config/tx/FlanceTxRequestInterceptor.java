package com.flance.tx.config.tx;

import com.flance.tx.common.TxConstants;
import com.flance.tx.common.netty.RoomContainer;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FlanceTxRequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String roomId = request.getHeader(TxConstants.FEIGN_HEADER_TX_ROOM_ID);
        if (null != roomId) {
            RoomContainer.putRoomID(roomId);
            RoomContainer.putIsRoomCreator(false);
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
