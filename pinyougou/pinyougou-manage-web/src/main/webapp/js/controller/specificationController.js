app.controller("specificationController",function ($scope, $controller,specificationService) {

    //继承 baseController
    $controller("baseController", {$scope:$scope});

    this.findAll = function () {
        specificationService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
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
        $scope.search($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
    };

    $scope.searchEntity = {};
    $scope.search = function (page,rows) {
        specificationService.search($scope.searchEntity,page,rows).success(function (response) {
            $scope.list = response.rows;
            $scope.paginationConf.totalItems = response.total;
        })
    };

    //初始化规格参数
    $scope.entity = {
        specificationOptionList:[],
        specification:{}
    };
    $scope.findOne = function (id) {
        specificationService.findOne(id).success(function (response) {
            $scope.entity.specificationOptionList = response.tbSpecificationOptions;
            $scope.entity.specification = response.tbSpecification;
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

    //新增修改规格
    $scope.save = function () {
        specificationService.save($scope.entity).success(function (response) {
            alert("成功")
        })
    };
});