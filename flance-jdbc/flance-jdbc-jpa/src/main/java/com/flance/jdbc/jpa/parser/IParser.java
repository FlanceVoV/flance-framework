package com.flance.jdbc.jpa.parser;

import java.util.List;

/**
 * 转换器
 * @author jhf
 * @param <PO>  持久化
 * @param <DTO> service交互
 * @param <VO> 视图
 * @param <DO> 领域业务
 */
public interface IParser<PO, DTO, VO, DO> {

    /**
     * dto转entity
     * @param dto   dto对象
     * @return  po    po对象
     */
    PO parseDto2Po(DTO dto);

    /**
     * entity转dto
     * @param po    po对象
     * @return  dto  dto对象
     */
    DTO parsePo2Dto(PO po);

    /**
     * dto转vo
     * @param dto   dto对象
     * @return   vo  vo对象
     */
    VO parseDto2Vo(DTO dto);

    /**
     * po转do
     * @param po    po对象
     * @return do    do对象
     */
    DO parsePo2Do(PO po);

    /**
     * dto转do
     * @param dto   dto对象
     * @return  do    do对象
     */
    DO parseDto2Do(DTO dto);

    /**
     * do转dto
     * @param domainObj    do对象
     * @return dto   dto对象
     */
    DTO parseDo2Dto(DO domainObj);

    /**
     * vo集合转换
     * @param dtos  dto集合
     * @return  返回vo集合
     */
    List<VO> parseListDto2Vo(List<DTO> dtos);

    /**
     * po集合转换
     * @param dtos  dto集合
     * @return  返回po集合
     */
    List<PO> parseListDto2Po(List<DTO> dtos);

    /**
     * dto集合转换
     * @param pos   po集合
     * @return  返回dto集合
     */
    List<DTO> parseListPo2Dto(List<PO> pos);

}
