package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import com.pinyougou.vo.PageResult;

import java.io.Serializable;
import java.util.List;

public interface BaseService<T> {

    /**
     * 根据主键查询
     * @param id  主键
     * @return  实体对象
     */
    public T findOne(Serializable id);

    /**
     *  查询全部
     * @return  实体对象集合
     */
    public List<T> findAll();

    public T findByWhere(T t);

    public PageResult findPage(Integer page, Integer rows);

    /**
     *  新增
     * @param t  实体对象
     */
    public void add(T t);

    /**
     *  根据主键更新
     * @param t  实体对象
     */
    public void update(T t);

    /**
     *  批量删除
     * @param ids  主键集合
     */
    public void deleteByIds(Serializable[] ids);

    public PageResult search(T t, Integer page, Integer rows);
}
