package com.java.crm.settings.mapper;

import com.java.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_value
     *
     * @mbggenerated Sun Jul 10 00:02:18 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_value
     *
     * @mbggenerated Sun Jul 10 00:02:18 CST 2022
     */
    int insert(DicValue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_value
     *
     * @mbggenerated Sun Jul 10 00:02:18 CST 2022
     */
    int insertSelective(DicValue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_value
     *
     * @mbggenerated Sun Jul 10 00:02:18 CST 2022
     */
    DicValue selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_value
     *
     * @mbggenerated Sun Jul 10 00:02:18 CST 2022
     */
    int updateByPrimaryKeySelective(DicValue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_value
     *
     * @mbggenerated Sun Jul 10 00:02:18 CST 2022
     */
    int updateByPrimaryKey(DicValue record);

    /**
     * 根据typeCode查询数据字典值
     * @param typeCode
     * @return
     */
    List<DicValue> selectDicValueByTypeCode(String typeCode);
}