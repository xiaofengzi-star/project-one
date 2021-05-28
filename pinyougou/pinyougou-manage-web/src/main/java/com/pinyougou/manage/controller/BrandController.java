package com.pinyougou.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import com.pinyougou.vo.PageResult;
import com.pinyougou.vo.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/brand")
@RestController
public class BrandController {

    /**
     * 从注册中心获取该对象；在配置文件中已经指定了注册中心
     */
    @Reference
    private BrandService brandService;

    /**
     * 获取并输出所有品牌列表
     * @return
     */
    @GetMapping("/findAll")
    //@RequestMapping(value = "/findAll", method =  RequestMethod.GET)
    //@ResponseBody
    public List<TbBrand> findAll(){
        return brandService.queryAll();
    }

    @GetMapping("/findPage")
    //@ResponseBody
    public PageResult testPage(Integer page, Integer rows){
        return brandService.findPage(page,rows);
    }

    @PostMapping("/add")
    //@ResponseBody
    public Result add(@RequestBody TbBrand tbBrand){
        try {
            brandService.add(tbBrand);
            return Result.ok("新增成功");
        }catch (Exception e){
            return Result.fail("新增失败");
        }
    }

    @GetMapping("/findOne")
    public TbBrand findOne(Long id){
        return brandService.findOne(id);
    }

    @PostMapping("/update")
    public Result update(@RequestBody TbBrand tbBrand){
        try {
            brandService.update(tbBrand);
            return Result.ok("更新成功");
        }catch (Exception e){
            return Result.fail("更新失败");
        }
    }

    @GetMapping("/delete")
    public Result delete(Long[] ids){
        try {
            brandService.deleteByIds(ids);
            return Result.ok("删除成功");
        }catch (Exception e){
            return Result.fail("删除失败");
        }
    }

    @PostMapping("/search")
    public PageResult search(@RequestBody TbBrand tbBrand, Integer page, Integer rows){
          return brandService.search(tbBrand,page,rows);
    }

    @GetMapping("/selectBrandList")
    public List<Map<String,TbBrand>> selectBrandList(){
        return brandService.selectBrandList();
    }
}
