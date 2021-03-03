app.controller("brandController",function ($scope, $controller,brandService) {
    //继承 baseController
    $controller("baseController", {$scope:$scope});

    $scope.findAll = function () {
        brandService.findAll().success(function (response) {
            $scope.list = response;
        });
    };
    // 初始化分页参数
    $scope.paginationConf = {
        currentPage:1,// 当前页号
        totalItems:10,// 总记录数
        itemsPerPage:10,// 页大小
        perPageOptions:[10, 20, 30, 40, 50],// 可选择的每页大小
        onChange: function () {// 当上述的参数发生变化了后触发
            $scope.reloadList();
        }
    };

    $scope.reloadList =function () {
        //$scope.findPage($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
        $scope.search($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
    };

    $scope.findPage = function (page, rows) {
        brandService.findPage(page,rows).success(function (response) {
            $scope.list = response.rows;
            $scope.paginationConf.totalItems = response.total;
        });
    };

    $scope.save = function () {
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