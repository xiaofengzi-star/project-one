package com.pinyougou.content.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.ContentMapper;
import com.pinyougou.pojo.TbContent;
import com.pinyougou.content.service.ContentService;
import com.pinyougou.service.impl.BaseServiceImpl;
import com.pinyougou.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Service(interfaceClass = ContentService.class)
public class ContentServiceImpl extends BaseServiceImpl<TbContent> implements ContentService {

    @Autowired
    private ContentMapper contentMapper;

    private static final String REDIS_CONTENT = "content";

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void add(TbContent content) {
        super.add(content);

        updateContentInRedisInContentId(content.getCategoryId());
    }


    @Override
    public void update(TbContent content) {
        TbContent oldContent = super.findOne(content.getId());

        super.update(content);

        //如果更新了内容分类，需要更新新旧内容分类
        if (oldContent.getCategoryId()!=null&&!oldContent.getCategoryId().equals(content.getCategoryId())){
            updateContentInRedisInContentId(oldContent.getCategoryId());
        }
        updateContentInRedisInContentId(content.getCategoryId());
    }

    @Override
    public void deleteByIds(Serializable[] ids){
        Example example = new Example(TbContent.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", Arrays.asList(ids));
        List<TbContent> tbContents = contentMapper.selectByExample(example);
        if (tbContents!=null&&tbContents.size()>0){
            for (TbContent tbContent : tbContents) {
                updateContentInRedisInContentId(tbContent.getCategoryId());
            }
        }

        super.deleteByIds(ids);
    }

    private void updateContentInRedisInContentId(Long categoryId) {
        try {
            redisTemplate.boundHashOps(REDIS_CONTENT).delete(categoryId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public PageResult search(TbContent content,Integer page, Integer rows) {
        PageHelper.startPage(page, rows);

        Example example = new Example(TbContent.class);
        Example.Criteria criteria = example.createCriteria();
        /*if(!StringUtils.isEmpty(content.get***())){
            criteria.andLike("***", "%" + content.get***() + "%");
        }*/

        List<TbContent> list = contentMapper.selectByExample(example);
        PageInfo<TbContent> pageInfo = new PageInfo<>(list);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public List<TbContent> findContentListByCategoryId(Long categoryId) {
        List<TbContent> list = null;
        try {
            //查看缓冲里面是否有广告内容
            list = (List<TbContent>)redisTemplate.boundHashOps(REDIS_CONTENT).get(categoryId);
            if (list!=null){
                return list;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Example example = new Example(TbContent.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("categoryId",categoryId);
        criteria.andEqualTo("status",1);
        example.orderBy("sortOrder").desc();
        List<TbContent> tbContents = contentMapper.selectByExample(example);
        try {
            //第一次寻找广告内容  存进缓冲
            redisTemplate.boundHashOps(REDIS_CONTENT).put(categoryId,tbContents);
        }catch (Exception e){
            e.printStackTrace();
        }
        return tbContents;
    }
}
