package com.flance.jdbc.jpa.web.controller;

import com.flance.jdbc.jpa.page.PageResponse;
import com.flance.jdbc.jpa.parser.BaseParser;
import com.flance.jdbc.jpa.web.service.BaseWebDomainService;
import com.flance.jdbc.jpa.web.service.IBaseWebDomainService;
import com.flance.web.common.controller.BaseController;
import com.flance.web.common.request.WebRequest;
import com.flance.web.common.request.WebResponse;
import com.flance.web.common.utils.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 继承web模块的controller，整合web jpa
 * @author jhf
 * @param <PO>  po
 * @param <DTO> dto
 * @param <VO>  vo
 * @param <DO>  do
 * @param <ID>  主键
 */
@RestController
public abstract class BaseWebController<PO, DTO, VO, DO, ID extends Serializable> extends BaseController<DTO, VO, ID, PageResponse> {

    @Resource
    private EntityManagerFactory entityManagerFactory;

    private IBaseWebDomainService<PO, DTO, ID> baseWebDomainService;

    private BaseParser<PO, DTO, VO, DO> baseParser;

    public void setBaseWebDomainService(IBaseWebDomainService baseWebDomainService) {
        this.baseWebDomainService = baseWebDomainService;
        super.setBaseService(baseWebDomainService);
    }

    public void setBaseParser(BaseParser baseParser) {
        this.baseParser = baseParser;
    }

    protected EntityManagerFactory getEntityManagerFactory() {
        return this.entityManagerFactory;
    }

    @Override
    @PostMapping("/get")
    public WebResponse get(@RequestBody WebRequest<DTO, ID> request) {
        ID id = request.getId();
        DTO dto = baseWebDomainService.findOne(id);
        return ResponseBuilder.getSuccess(WebResponse.builder().singleResult(baseParser.parseDto2Vo(dto)).build());
    }

    @Override
    @PostMapping("/list")
    public WebResponse list(@RequestBody WebRequest<DTO, ID> request) {
        Map<String, Object> searchMap = request.getParamsMap();
        List<DTO> dtos = baseWebDomainService.findAll(searchMap);
        return ResponseBuilder.getSuccess(WebResponse.builder().multiResult((List<Object>) baseParser.parseListDto2Vo(dtos)).build());
    }

    @Override
    @PostMapping("/addBatch")
    public WebResponse<DTO, VO, ID, PageResponse> addBatch(@RequestBody WebRequest<DTO, ID> request) {
        List<DTO> dtos = request.getMultiParam();
        baseWebDomainService.saveBatch(dtos);
        return ResponseBuilder.getSuccess(WebResponse.builder().build());
    }

    @Override
    @PatchMapping("/updateBatch")
    public WebResponse<DTO, VO, ID, PageResponse> updateBatch(@RequestBody WebRequest<DTO, ID> request) {
        List<DTO> dtos = request.getMultiParam();
        baseWebDomainService.updateBatch(entityManagerFactory, dtos);
        return ResponseBuilder.getSuccess(WebResponse.builder().build());
    }

    @Override
    public WebResponse<DTO, VO, ID, PageResponse> page(@RequestBody WebRequest<DTO, ID> request) {
        WebResponse WebResponse = super.page(request);
        PageResponse pageResponse = (PageResponse) WebResponse.getPageResult();
        List<DTO> dtos = pageResponse.getData();
        pageResponse.setData(baseParser.parseListDto2Vo(dtos));
        WebResponse.setPageResult(pageResponse);
        return WebResponse;
    }
}
