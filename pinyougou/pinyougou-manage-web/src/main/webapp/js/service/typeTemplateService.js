// 服务层
app.service("typeTemplateService", function ($http) {
    // 查询所有列表数据
    this.findAll = function () {
        return $http.get("../typeTemplate/findAll.do");
    };
    // 搜索
    this.search = function (searchEntity, page, rows) {
        return $http.post("../typeTemplate/search.do?page="+page+"&rows="+rows,searchEntity);
    };
    //点击【修改】查找某个规格详情
    this.findOne = function (id) {
        return $http.get("../typeTemplate/findOne.do?id="+id);
    };
    //新增修改规格
    this.save = function (entity) {
        return $http.post("../typeTemplate/save.do",entity);
    };
    //更新
    this.update = function (entity) {
        return $http.post("../typeTemplate/update.do",entity);
    };
    // 新增
    this.add = function (entity) {
        return $http.post("../typeTemplate/add.do", entity);
    };
    // 删除
    this.delete = function (selectedIds) {
        return $http.get("../typeTemplate/delete.do?ids="+selectedIds);
    };

    // 品牌多选列表
    this.selectBrandList = function () {
        return $http.get("../brand/selectBrandList.do?random="+Math.random());
    };

    // 规格多选列表
    this.selectSpecList = function () {
        return $http.get("../specification/selectSpecList.do?random="+Math.random());
    };


});