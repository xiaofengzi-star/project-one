app.controller("indexController",function ($scope, $controller,loginService) {

    //继承 baseController
    $controller("baseController", {$scope:$scope});


    $scope.getUsername = function () {
        loginService.getUsername().success(function (response) {
            $scope.username = response;
        });
    };

});