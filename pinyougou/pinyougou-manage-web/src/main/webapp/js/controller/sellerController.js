app.controller("sellerController",function ($scope, $controller,sellerService) {
    //继承 baseController
    $controller("baseController", {$scope:$scope});

    $scope.searchEntity = {};
    $scope.search = function (page,rows) {
        sellerService.search($scope.searchEntity,page,rows).success(function (response) {
            $scope.list = response.rows;
            $scope.paginationConf.totalItems = response.total;
        })
    };

    $scope.findOne = function (sellerId) {
        sellerService.findOne(sellerId).success(function (response) {
            $scope.entity = response;
        })
    };

    $scope.updateStatus = function (sellerId,status) {
        sellerService.updateStatus(sellerId,status).success(function (response) {
            if (response.success){
                alert(response.message);
                $scope.reloadList();
            }else {
                alert(response.message);
            }
        })
    };
});