
ngApp.controller('ruleController', function ($scope, $http) {
                $scope.message = "Hello World!";
                $scope.executeCode = function () {

                    var data = {
                        code: $scope.rule.code
                    };

                    Object.toparams = function ObjecttoParams(obj) {
                        var p = [];
                        for (var key in obj) {
                            p.push(key + '=' + encodeURIComponent(obj[key]));
                        }
                        return p.join('&');
                    };


                    $http({
                            method: "POST",
                            url: "/run",
                            params: {
                            },
                            data: Object.toparams(data),
                            headers: {
                                        'Content-Type': undefined
                            }
                        }).success(function (data) {
                            $scope.rule.result = data;
                        }).error(function () {
                            $scope.result = "内部错误";
                        });
                };
});