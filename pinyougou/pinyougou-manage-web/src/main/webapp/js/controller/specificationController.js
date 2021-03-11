app.controller("specificationController",function ($scope, $controller,specificationService) {

    //继承 baseController
    $controller("baseController", {$scope:$scope});

    //新增修改规格
    $scope.save = function () {
        var obj;
        if ($scope.entity.id != null) {
            alert($scope.entity.id+"!=null");
            obj = specificationService.update($scope.entity);
        }else {
            alert($scope.entity.id+"=null");
            obj = specificationService.add($scope.entity);
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
        specificationOptionList:[]
    };
    $scope.findOne = function (id) {
        specificationService.findOne(id).success(function (response) {
            $scope.entity.specificationOptionList = response.specificationOptionList;
            $scope.entity.specification = response.specification;
        })
    };

    $scope.selectedIds = [];
    $scope.delete = function () {
        if ($scope.selectedIds.length < 1){
            alert("请选择要删除的规格");
            return;
        }
        specificationService.delete($scope.selectedIds).success(function (response) {
            if (response.success){
                alert(response.message);
                $scope.reloadList();
            }else {
                alert(response.message);
            }
        })
    };

    //增加规格选项行
    $scope.addTableRow = function(){
        $scope.entity.specificationOptionList.push({});
    };
    //删除规格行
    $scope.deleteTableRow = function(index){
        $scope.entity.specificationOptionList.splice(index,1);
    };

    $scope.searchEntity = {};
    $scope.search = function (page,rows) {
        specificationService.search($scope.searchEntity,page,rows).success(function (response) {
            $scope.list = response.rows;
            $scope.paginationConf.totalItems = response.total;
        })
    };
});