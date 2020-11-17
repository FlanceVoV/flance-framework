package com.flance.components.form.infrastructure.utils;


import com.flance.components.form.domain.dform.model.vo.ServiceModelBindVo;

import java.util.HashMap;
import java.util.Map;

public class TableCodeUtil {

    private static Map<String, ServiceModelBindVo> map = new HashMap<>();

    static {
        ServiceModelBindVo serviceModelBind = new ServiceModelBindVo();
        serviceModelBind.setModelName("com.konyo.form.common.domain.JsDformSdbBaseinfo");
        serviceModelBind.setServiceName("jsDformSdbBaseinfoServiceImpl");
        map.put("BASE_INFO", serviceModelBind);
    }

    public static ServiceModelBindVo getServiceModelBind(String tableCode) {
        ServiceModelBindVo serviceModelBind = map.get(tableCode);
        return serviceModelBind;
    }


}
