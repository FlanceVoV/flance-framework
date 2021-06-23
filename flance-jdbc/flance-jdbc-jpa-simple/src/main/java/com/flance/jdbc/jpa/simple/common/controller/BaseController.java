package com.flance.jdbc.jpa.simple.common.controller;


import com.flance.jdbc.jpa.simple.common.BaseConstant;
import com.flance.jdbc.jpa.simple.components.user.entity.Account;
import com.flance.jdbc.jpa.simple.entity.BaseEntity;
import com.flance.jdbc.jpa.simple.service.IService;
import com.flance.jdbc.jpa.simple.utils.CurrentUserUtil;
import com.flance.web.common.controller.IBaseController;
import com.flance.web.utils.web.request.WebRequest;
import com.flance.web.utils.web.response.WebResponse;
import com.google.common.collect.Maps;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 接口基类
 * @author jhf
 * @param <T> 实体
 * @param <ID> 主键
 * @param <PAGE> 分页
 */
public class BaseController<T extends BaseEntity<ID>, ID extends Serializable, PAGE> implements IBaseController<T, T, ID, PAGE> {

    private IService<T, ID, PAGE> service;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private SessionRepository<Session> sessionRepository;

    protected void setService(IService<T, ID, PAGE> service) {
        this.service = service;
    }

    @PostMapping("/add")
    @Override
    public WebResponse add(@RequestBody WebRequest<T, ID> request) {
        Account currentUser = CurrentUserUtil.getAccountCurrent();
        T t = request.getSingleParam();
        Assert.notNull(t, "[singleParam]参数不允许为空！");
        createPri(t, currentUser);
        return WebResponse.getSucceed(service.saveOne(t), "新增成功！");
    }

    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/adds")
    @Override
    public WebResponse addBatch(@RequestBody WebRequest<T, ID> request) {
        Account currentUser = CurrentUserUtil.getAccountCurrent();
        List<T> list = request.getMultiParam();
        Assert.notNull(list, "[multiParam]参数不允许为空！");
        list.forEach(item -> createPri(item, currentUser));
        service.saveBatch(request.getMultiParam());
        return WebResponse.getSucceed(null, "新增成功！");
    }

    @PatchMapping("/update")
    @Override
    public WebResponse update(@RequestBody WebRequest<T, ID> request) {
        Account currentUser = CurrentUserUtil.getAccountCurrent();
        Assert.notNull(request.getId(), "[request.Id]参数不允许为空！");
        Assert.notNull(request.getSingleParam(), "[singleParam]参数不允许为空！");
        updatePri(request.getSingleParam(), currentUser);
        return WebResponse.getSucceed(service.updateNotNull(request.getSingleParam(), request.getId()), "编辑成功！");
    }

    @Override
    public WebResponse updateBatch(@RequestBody WebRequest<T, ID> request) {
        return null;
    }

    @DeleteMapping("/delete")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WebResponse delete(@RequestBody WebRequest<T, ID> request) {
        Assert.notNull(request.getId(), "[request.Id]参数不允许为空！");
        Account currentUser = CurrentUserUtil.getAccountCurrent();
        T t = service.findOne(request.getId());
        Assert.notNull(t, "[待删除对象]找不到！");
        updatePri(t, currentUser);
        service.updateNotNull(t, t.getId());
        service.delete(request.getId());
        return WebResponse.getSucceed(null, "删除成功！");
    }

    @Transactional(rollbackFor = Exception.class)
    @DeleteMapping("/deletes")
    @Override
    public WebResponse deleteBatch(@RequestBody WebRequest<T, ID> request) {
        Account currentUser = CurrentUserUtil.getAccountCurrent();
        Assert.notNull(request.getIds(), "[request.Ids]参数不允许为空！");
        request.getIds().forEach(id -> {
            T t = service.findOne(id);
            updatePri(request.getSingleParam(), currentUser);
            service.updateNotNull(t, t.getId());
            service.delete(request.getId());
        });
        return WebResponse.getSucceed(null, "删除成功！");
    }

    @PostMapping("/get")
    @Override
    public WebResponse get(@RequestBody WebRequest<T, ID> request) {
        Assert.notNull(request.getId(), "[request.Id]参数不允许为空！");
        if (null != request.getParamsMap()) {
            request.getParamsMap().put("EQ_id", request.getId());
            return WebResponse.getSucceed(service.findOneByProps(request.getParamsMap()), "查询成功！");
        }
        return WebResponse.getSucceed(service.findOne(request.getId()), "查询成功！");
    }

    @PostMapping("/getByProps")
    public WebResponse getByProps(@RequestBody WebRequest<T, ID> request) {
        if (null == request.getParamsMap()) {
            request.setParamsMap(Maps.newHashMap());
        }
        return WebResponse.getSucceed(service.findOneByProps(request.getParamsMap()), "查询成功");
    }

    @PostMapping("/page")
    @Override
    public WebResponse page(@RequestBody WebRequest<T, ID> request) {
        if (null == request.getParamsMap()) {
            request.setParamsMap(Maps.newHashMap());
        }
        putPage(request.getParamsMap());
        return WebResponse.getSucceed(service.findPage(request.getParamsMap()), "查询成功");
    }

    @PostMapping("/list")
    @Override
    public WebResponse list(@RequestBody WebRequest<T, ID> request) {
        Map<String, Object> searchMap = request.getParamsMap();
        if (null == searchMap) {
            searchMap = Maps.newHashMap();
        }
        List<T> list = service.findAll(searchMap);
        return WebResponse.getSucceed(list, "查询成功");
    }


    protected void updatePri(T t, Account currentUser) {
        t.setLastUpdateTime(new Date());
        t.setLastUpdateUserId(currentUser.getId() + "");
        t.setLastUpdateUserName(currentUser.getUsername());
    }

    protected void createPri(T t, Account currentUser) {
        t.setCreateTime(new Date());
        t.setCreateUserId(currentUser.getId() + "");
        t.setCreateUserName(currentUser.getUsername());
        t.setDeleted(BaseConstant.DELETE_FLAG_NORMAL);
    }

    protected void reloadLogin() {
        Account currentUser = CurrentUserUtil.getAccountCurrent();
        // 刷新登录状态
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(currentUser.getUsername(), currentUser.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList(""));
        Authentication auth = authenticationManager.authenticate(authRequest);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    protected void changeSessionUser(HttpServletRequest request, Account account) {
        Object sessionId = request.getSession().getId();
        Session redisSession = sessionRepository.findById(sessionId.toString());
        SecurityContextImpl context = redisSession.getAttribute("SPRING_SECURITY_CONTEXT");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(account, account.getOpenId(), AuthorityUtils.commaSeparatedStringToAuthorityList(""));
        context.setAuthentication(authenticationToken);
        redisSession.setAttribute("SPRING_SECURITY_CONTEXT",context);
        sessionRepository.save(redisSession);
    }

    private void putPage(Map<String, Object> params) {
        params.putIfAbsent("page", 1);
        params.putIfAbsent("rows", 10);
        if (((Integer)params.get("rows")) >= 50) {
            params.put("rows", 10);
        }
    }
}
