// 服务层
app.service("itemCatService", function ($http) {
    //获得全部itemCat
    this.findAll = function () {
        return $http.post("../itemCat/findAll.do");
    };
    //通过父类id查询下级
    this.findByParentId = function (parentId) {
        return $http.get("../itemCat/findByParentId.do?parentId="+parentId);
    };


});