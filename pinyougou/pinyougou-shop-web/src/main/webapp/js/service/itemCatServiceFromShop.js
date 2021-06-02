//定义业务服务
app.service("itemCatServiceFromShop",function ($http) {

    //读取一级商品分类列表
    this.findByParentId = function (parentId) {
        return $http.get("../itemCat/findByParentId.do?parentId=" + parentId);
    };
    this.findOne = function(newValue) {
        return $http.get("../itemCat/findOne.do?id=" + newValue);
    }
});