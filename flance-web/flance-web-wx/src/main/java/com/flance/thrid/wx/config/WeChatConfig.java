package com.flance.thrid.wx.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeChatConfig {

    @Value("${wx.app.id}")
    public String wxAppId;

    @Value("${wx.secret}")
    public String wxSecret;

    @Value("${wx.mch.id}")
    public String wxMchId;

    @Value("${wx.mch.key}")
    public String wxMchKey;

    @Value("${wx.payNotifyUrl}")
    public String payNotifyUrl;

    @Value("${wx.refundNotifyUrl}")
    public String refundNotifyUrl;

    @Value("${wx.pay.certPath}")
    public String wxPayCertPath;

    @Bean("wxMaConfig")
    public WxMaConfig WxMaConfig() {
        WxMaDefaultConfigImpl wxMaConfig = new WxMaDefaultConfigImpl();
        wxMaConfig.setAppid(wxAppId);
        wxMaConfig.setSecret(wxSecret);
        return wxMaConfig;
    }

    @Bean("wxMaService")
    public WxMaService wxMpService(@Qualifier("wxMaConfig") WxMaConfig wxMaConfig) {
        WxMaService wxMpService = new WxMaServiceImpl();
        wxMpService.setWxMaConfig(wxMaConfig);
        return wxMpService;
    }

    @Bean
    public WxPayConfig wxPayConfig() {
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setAppId(wxAppId);
        wxPayConfig.setMchId(wxMchId);
        wxPayConfig.setMchKey(wxMchKey);
        wxPayConfig.setNotifyUrl(payNotifyUrl);
        wxPayConfig.setTradeType("JSAPI");
        wxPayConfig.setKeyPath(wxPayCertPath);
        return wxPayConfig;
    }

    @Bean
    public WxPayService wxPayService(WxPayConfig wxPayConfig) {
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(wxPayConfig);
        return wxPayService;
    }

}
