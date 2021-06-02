app.controller("itemCatController",function ($scope, $controller,itemCatService) {

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

    $scope.searchEntity = {};
    $scope.search = function (page,rows) {
        itemCatService.search($scope.searchEntity,page,rows).success(function (response) {
        })
    };
    //自动获取所有列表
    $scope.findAll= function () {
        itemCatService.findAll().success(function (response) {
            $scope.list = response;
        })
    };
    //通过父类id查询下级
    $scope.findByParentId = function (parentId) {
        itemCatService.findByParentId(parentId).success(function (response) {
            $scope.list = response;
        });
    };

    //设置grade默认为1  因为是三级分类  点击查询下级+1
    $scope.grade = 1;
    $scope.selectList = function (garde,entity) {
        itemCatService.findByParentId(entity.id).success(function (response) {
            $scope.grade = garde;
            if (garde == 2){
                $scope.entity_1 = entity;
                $scope.entity_2 = null;
            }else if (garde == 1) {
                $scope.entity_1 = null;
                $scope.entity_2 = null;
            }else {
                $scope.entity_2 = entity;
            }
            $scope.list = response;
        });
    }
});