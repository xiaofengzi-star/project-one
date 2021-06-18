package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.SpecificationMapper;
import com.pinyougou.mapper.SpecificationOptionMapper;
import com.pinyougou.pojo.Specification;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.sellergoods.service.SpecificationService;
import com.pinyougou.service.impl.BaseServiceImpl;
import com.pinyougou.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service(interfaceClass = SpecificationService.class)
public class SpecificationServiceImpl extends BaseServiceImpl<TbSpecification> implements SpecificationService {

    @Autowired
    private SpecificationMapper specificationMapper;

    @Autowired
    private SpecificationOptionMapper specificationOptionMapper;


    /*
    * 查询规格名称
    * */
    @Override
    public PageResult search(TbSpecification specification, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);

        Example example = new Example(TbSpecification.class);
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(specification.getSpecName())){
            criteria.andLike("specName", "%" + specification.getSpecName() + "%");
        }

        List<TbSpecification> list = specificationMapper.selectByExample(example);
        PageInfo<TbSpecification> pageInfo = new PageInfo<>(list);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public void add(Specification specification) {
        //新增规格
        specificationMapper.insertSelective(specification.getSpecification());

        insertSpecificationOption(specification);
    }

    /**
     * 更新规格
     * @param specification
     */
    @Override
    public void update(Specification specification) {
        //更新规格
        specificationMapper.updateByPrimaryKeySelective(specification.getSpecification());

        //更新规格选项,先删除后新增
        TbSpecificationOption param = new TbSpecificationOption();
        param.setSpecId(specification.getSpecification().getId());
        specificationOptionMapper.delete(param);

        insertSpecificationOption(specification);
    }

    @Override
    public Specification findOneById(Long id) {
        //获取规格
        TbSpecification tbSpecification = specificationMapper.selectByPrimaryKey(id);
        //获取规格选项
        List<TbSpecificationOption> specificationOptionList = specificationOptionMapper.selectBySpecId(tbSpecification.getId());

        Specification specification = new Specification();
        specification.setSpecification(tbSpecification);
        specification.setSpecificationOptionList(specificationOptionList);
        return specification;
    }

    @Override
    public void deleteSpecAndOps(Long[] ids) {
        if (ids.length>0){
            for (Long id : ids) {
                //新增规格和规格选项  用来删除
                TbSpecificationOption tbSpecificationOption = new TbSpecificationOption();
                tbSpecificationOption.setSpecId(id);
                TbSpecification tbSpecification = new TbSpecification();
                tbSpecification.setId(id);
                //删除
                specificationOptionMapper.delete(tbSpecificationOption);
                specificationMapper.delete(tbSpecification);
            }
        }
    }

    /**
     * 插入规格选项
     * @param specification
     */
    private void insertSpecificationOption(Specification specification){
        //新增规格选项
        if (specification.getSpecificationOptionList() != null && specification.getSpecificationOptionList().size() > 0){
            for (TbSpecificationOption tbSpecificationOption : specification.getSpecificationOptionList()) {
                //需要把spec_id绑定到规格id
                tbSpecificationOption.setSpecId(specification.getSpecification().getId());
                specificationOptionMapper.insertSelective(tbSpecificationOption);
            }
        }
    }

    @Override
    public List<Map<String, TbSpecification>> selectSpecificationList() {
        return specificationMapper.selectSpecificationList();
    }
}
