// 服务层
app.service("specificationService", function ($http) {
    // 查询所有列表数据
    this.findAll = function () {
        return $http.get("../specification/findAll.do");
    };
    // 搜索
    this.search = function (searchEntity, page, rows) {
        return $http.post("../specification/search.do?page="+page+"&rows="+rows,searchEntity);
    };
    //点击【修改】查找某个规格详情
    this.findOne = function (id) {
        return $http.post("../specification/findOne.do?id="+id);
    };
    //新增修改规格
    this.save = function (entity) {
        return $http.post("../specification/save.do",entity);
    };
    //更新
    this.update = function (entity) {
        return $http.post("../specification/update.do",entity);
    };
    // 新增
    this.add = function (entity) {
        return $http.post("../specification/add.do", entity);
    };
    // 删除
    this.delete = function (selectedIds) {
        return $http.get("../specification/delete.do?ids="+selectedIds);
    };
});