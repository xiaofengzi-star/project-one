app.controller("goodsController", function ($scope, $controller, $location, goodsService, uploadService,
                                            itemCatServiceFromShop, typeTemplateServiceFromShop) {

    //加载baseController控制器并传入1个作用域，与angularJs运行时作用域相同.
    $controller("baseController",{$scope:$scope});

    //读取一级商品分类列表
    $scope.selectItemCat1List = function(){
        itemCatServiceFromShop.findByParentId(0).success(function (response) {
            $scope.itemCat1List = response;
            $scope.itemCat2List = null;
            $scope.itemCat3List = null;
            $scope.entity.goods.typeTemplateId = null;
        });
    };

    //读取二级商品分类列表
    $scope.$watch("entity.goods.category1Id",function (newValue,oldValue) {
        if (newValue!=undefined){
            itemCatServiceFromShop.findByParentId(newValue).success(function (response) {
                $scope.itemCat2List = response;
                $scope.itemCat3List = null;
                $scope.entity.goods.typeTemplateId = null;
            });
        }
    });
    //读取三级级商品分类列表
    $scope.$watch("entity.goods.category2Id",function (newValue,oldValue) {
        if (newValue!=undefined){
            itemCatServiceFromShop.findByParentId(newValue).success(function (response) {
                $scope.itemCat3List = response;
            });
        }
    });
    //读取模板ID
    $scope.$watch("entity.goods.category3Id",function (newValue,oldValue) {
        if (newValue!=undefined){
            itemCatServiceFromShop.findOne(newValue).success(function (response) {
                $scope.entity.goods.typeTemplateId = response.typeId;

                //查找根据模板ID品牌下拉列表
                $scope.findOne(response.typeId);
            });
        }
    });

    //查找根据模板ID品牌下拉列表
    $scope.findOne = function(typeId){
        typeTemplateServiceFromShop.findOne(typeId).success(function (response) {
            $scope.typeTemplate = response;
            $scope.entity.goodsDesc = response;
            $scope.typeTemplate.brandIds = JSON.parse($scope.typeTemplate.brandIds);
            //寻找扩展属性
            console.log(JSON.parse($scope.typeTemplate.customAttributeItems));

            $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.entity.goodsDesc.customAttributeItems);
        });
    };


});