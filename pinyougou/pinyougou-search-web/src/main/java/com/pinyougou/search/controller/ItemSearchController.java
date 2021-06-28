package com.pinyougou.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.search.service.ItemSearchService;
import com.pinyougou.pojo.TbContent;
import com.pinyougou.pojo.TbItem;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequestMapping("/itemSearch")
@RestController
public class ItemSearchController {
    @Reference
    private ItemSearchService itemSearchService;

    @PostMapping("search")
    public Map<String, Object> search(@RequestBody Map<String, Object> searchMap){
        return  itemSearchService.search(searchMap);
    }
}
