app.controller("typeTemplateController",function ($scope, $controller,typeTemplateService) {

    //继承 baseController
    $controller("baseController", {$scope:$scope});

    //新增修改规格
    $scope.save = function () {
        var obj;
        if ($scope.entity.id != null) {
            obj = typeTemplateService.update($scope.entity);
        }else {
            obj = typeTemplateService.add($scope.entity);
        }
        obj.success(function (response) {
            if (response.success){
                alert(response.message);
                $scope.reloadList();
            }else {
                alert(response.message);
            }
        });
    };
    //初始化规格参数
    $scope.entity = {

    };
    $scope.findOne = function (id) {
        typeTemplateService.findOne(id).success(function (response) {

            $scope.entity.name = response.name;
            $scope.entity.brandIds = JSON.parse(response.brandIds);
            $scope.entity.brandList = {data:JSON.parse(response.brandIds)};
            $scope.entity.specIds = JSON.parse(response.specIds);
            $scope.entity.specificationList = {data:JSON.parse(response.specIds)};
            $scope.entity.customAttributeItems = JSON.parse(response.customAttributeItems);
        })
    };

    $scope.selectedIds = [];
    $scope.delete = function () {
        if ($scope.selectedIds.length < 1){
            alert("请选择要删除的规格");
            return;
        }
        typeTemplateService.delete($scope.selectedIds).success(function (response) {
            if (response.success){
                alert(response.message);
                $scope.reloadList();
            }else {
                alert(response.message);
            }
        })
    };

    $scope.entity = {
        customAttributeItems:[]
    };
    //增加规格选项行
    $scope.addTableRow = function(){
        $scope.entity.customAttributeItems.push({});
    };
    //删除规格行
    $scope.deleteTableRow = function(index){
        $scope.entity.customAttributeItems.splice(index,1);
    };

    $scope.searchEntity = {};
    $scope.search = function (page,rows) {
        typeTemplateService.search($scope.searchEntity,page,rows).success(function (response) {
            $scope.list = response.rows;
            $scope.paginationConf.totalItems = response.total;
        })
    };
    $scope.jsonToString = function (jsonStr, key) {
        //将一个 json 数组格式字符串的某个 key 对应的值串起来显示，使用,分隔
        $scope.jsonToString = function (jsonStr, key) {
            var str = "";
            var jsonArray = JSON.parse(jsonStr);
            for(var i = 0; i < jsonArray.length; i++) {
                if(i > 0) {
                    str += ",";
                }
                str += jsonArray[i][key];
            }
            return str;
        };
    };
    //查找品牌下拉列表
    $scope.selectBrandList = function(){
        typeTemplateService.selectBrandList().success(function (response) {
            $scope.entity.brandList = {data:response};
        });
    };
    //查找规格下拉列表
    $scope.selectSpecList = function(){
        typeTemplateService.selectSpecList().success(function (response) {
            $scope.entity.specificationList = {data:response};
        });
    };
});