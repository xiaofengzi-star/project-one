app.service("sellerService", function ($http) {
    // 搜索
    this.search = function (searchEntity, page, rows) {
        return $http.post("../seller/search.do?page="+page+"&rows="+rows,searchEntity);
    };

    this.updateStatus = function (sellerId, status) {
        return $http.post("../seller/updateStatus.do?sellerId="+sellerId+"&status="+status);
    };

    this.findOne = function (sellerId) {
        return $http.get("../seller/findOne.do?sellerId="+sellerId);
    };
});