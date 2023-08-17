package com.flance.thrid.wx.api;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.flance.thrid.wx.model.WxOpenIdResponse;
import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.Date;

@Service
public class WeChatApi {

    @Resource
    private WxMaService wxMaService;

    public WxOpenIdResponse getOpenId(String code) {
        WxOpenIdResponse response = WxOpenIdResponse.builder().build();
        try {
            WxMaJscode2SessionResult sessionInfo = wxMaService.getUserService().getSessionInfo(code);
            String sessionKey = sessionInfo.getSessionKey();
            String openId = sessionInfo.getOpenid();
            String unionId = sessionInfo.getUnionid();

            response.setOpenId(openId);
            response.setUnionId(unionId);
            response.setSessionKey(sessionKey);
            response.setLoginTime(new Date());

        } catch (WxErrorException e) {
            WxError wxError = e.getError();
            response.setWxResultCode(wxError.getErrorCode() + "");
            response.setWxResultMsg(wxError.getErrorMsg());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }




}
