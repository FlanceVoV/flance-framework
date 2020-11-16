package com.flance.jdbc.jpa.parser;

import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * 类型转换
 * @author jhf
 */
public abstract class BaseParser<PO, DTO, VO, DO> implements IParser<PO, DTO, VO, DO> {

    @Override
    public PO parseDto2Po(DTO dto) {
        ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        Class<PO> type = (Class<PO>) superClass.getActualTypeArguments()[0];
        PO po = null;
        try {
            po = type.newInstance();
            BeanUtils.copyProperties(dto, po);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return po;
    }

    @Override
    public DTO parsePo2Dto(PO po) {
        ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        Class<DTO> type = (Class<DTO>) superClass.getActualTypeArguments()[1];
        DTO dto = null;
        try {
            dto = type.newInstance();
            BeanUtils.copyProperties(po, dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }

    @Override
    public VO parseDto2Vo(DTO dto) {
        ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        Class<VO> type = (Class<VO>) superClass.getActualTypeArguments()[2];
        VO vo = null;
        try {
            vo = type.newInstance();
            BeanUtils.copyProperties(dto, vo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vo;
    }

    @Override
    public DO parsePo2Do(PO po) {
        ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        Class<DO> type = (Class<DO>) superClass.getActualTypeArguments()[3];
        DO doObj = null;
        try {
            doObj = type.newInstance();
            BeanUtils.copyProperties(po, doObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doObj;
    }

    @Override
    public DO parseDto2Do(DTO dto) {
        ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        Class<DO> type = (Class<DO>) superClass.getActualTypeArguments()[3];
        DO doObj = null;
        try {
            doObj = type.newInstance();
            BeanUtils.copyProperties(dto, doObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doObj;
    }

    @Override
    public DTO parseDo2Dto(DO doObj) {
        ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        Class<DTO> type = (Class<DTO>) superClass.getActualTypeArguments()[1];
        DTO dto = null;
        try {
            dto = type.newInstance();
            BeanUtils.copyProperties(doObj, dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }

    @Override
    public List<VO> parseListDto2Vo(List<DTO> dtos) {
        assert null != dtos;
        List<VO> list = Lists.newArrayList();
        dtos.forEach(dto -> list.add(parseDto2Vo(dto)));
        return list;
    }

    @Override
    public List<PO> parseListDto2Po(List<DTO> dtos) {
        assert null != dtos;
        List<PO> list = Lists.newArrayList();
        dtos.forEach(dto -> list.add(parseDto2Po(dto)));
        return list;
    }

    @Override
    public List<DTO> parseListPo2Dto(List<PO> pos) {
        assert null != pos;
        List<DTO> list = Lists.newArrayList();
        pos.forEach(po -> list.add(parsePo2Dto(po)));
        return list;
    }

}
