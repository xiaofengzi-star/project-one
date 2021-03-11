app.controller("brandController",function ($scope, $controller,brandService) {
    //继承 baseController
    $controller("baseController", {$scope:$scope});

    $scope.save = function () {
        alert($scope.entity.id);
        var obj;
        if ($scope.entity.id != null) {
            obj = brandService.update($scope.entity);
        }else {
            obj = brandService.add($scope.entity);
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

    $scope.findOne = function (id) {
        brandService.findOne(id).success(function (response) {
            $scope.entity = response;
        })
    };

    // 定义一个放置选择了 id 的数组
    $scope.selectedIds = [];
    $scope.updateSelection = function ($event, id) {
        if ($event.target.checked){
            $scope.selectedIds.push(id);
        }else {
            var index = $scope.selectedIds.indexOf(id);
            $scope.selectedIds.splice(index,1);
        }
    };

    $scope.delete = function () {
        if ($scope.selectedIds.length < 1){
            alert("请选择要删除的品牌");
            return;
        }
        brandService.delete($scope.selectedIds).success(function (response) {
            if (response.success){
                alert(response.message);
                $scope.reloadList();
            }else {
                alert(response.message);
            }
        })
    };

    $scope.searchEntity = {};
    $scope.search = function (page,rows) {
        brandService.search($scope.searchEntity,page,rows).success(function (response) {
            $scope.list = response.rows;
            $scope.paginationConf.totalItems = response.total;
        })
    };
});