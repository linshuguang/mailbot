
ngApp.controller('mailbotController', function ($scope, $http) {
                $scope.mailbot = {};
                $scope.mailbot.result = "None";
                $scope.mailbot.records = [];
                $scope.message = "Hello World!";
                $scope.executeCode = function () {

                    var data = {
                        code: $scope.mailbot.code
                    };

                    //alert($scope.mailbot.code);
                    Object.toparams = function ObjecttoParams(obj) {
                        var p = [];
                        for (var key in obj) {
                            p.push(key + '=' + encodeURIComponent(obj[key]));
                        }
                        return p.join('&');
                    };

                    $http({
                            method: "POST",
                            url: "/mailbot/run",
                            params: {
                            },
                            data: Object.toparams(data),
                            headers: {
                                'Content-Type': undefined
                            }
                        }).success(function (data) {
                            //json = JSON.parse(data);
                            //alert(data["records"]);
                            records = data["content"]["records"];

                            html = "<div>";
                            for (var i = 0; i < records.length; i++) {
                              record=records[i];
                              html += "<div>"+record+"</div>";
                            }
                            html +="</div>";
                            $scope.mailbot.result = html;
                        }).error(function () {
                            $scope.result = "内部错误";
                        });
                };



                $scope.echoCode = function () {

                                    var data = {
                                        code: $scope.mailbot.code
                                    };

                                    //alert($scope.mailbot.code);
                                    Object.toparams = function ObjecttoParams(obj) {
                                        var p = [];
                                        for (var key in obj) {
                                            p.push(key + '=' + encodeURIComponent(obj[key]));
                                        }
                                        return p.join('&');
                                    };

                                    $http({
                                            method: "POST",
                                            url: "/mailbot/echo",
                                            params: {
                                            },
                                            data: Object.toparams(data),
                                            headers: {
                                                'Content-Type': undefined
                                            }
                                        }).success(function (data) {

                                            $scope.mailbot.result = data;
                                        }).error(function () {
                                            $scope.result = "内部错误";
                                        });
                                };
});

ngApp.directive("screen", function () {

   //define the directive object
   var directive = {};

   //restrict = E, signifies that directive is Element directive
   directive.restrict = 'E';

   //template replaces the complete element with its text.
   //directive.template = "Student: <b>{{student.name}}</b> , Roll No: <b>{{student.rollno}}</b>";

   //scope is used to distinguish each student element based on criteria.
   //directive.scope = {
   //   student : "=name"
   //}

   //compile is called during application initialization. AngularJS calls it once when html page is loaded.

   directive.compile = function(element, attributes) {
      //element.css("border", "1px solid #cccccc");

      //linkFunction is linked with each element with scope to get the element specific data.
        var linkFunction = function($scope, element, attributes) {
            var iframe = document.createElement('iframe');
            iframe.width=1024;
            iframe.height=768;
            iframe.scrolling="yes";
            var element0 = element[0];
            element0.appendChild(iframe);
            //var iframe = document.createElement('iframe');
            //element[0].appendChild(iframe);
            //var body = iframe.contentDocument.body;
            //body.innerHTML = "<html><body><h1>hello world</h1></body></html>";
            $scope.$watch('mailbot.result', function () {
                    //body.innerHTML = scope.mailbot_result;
                    //body.innerHTML = "<html><body><h1>hello world hahah</h1></body></html>";
                    iframe.src = "data:text/html;charset=utf-8,"+escape($scope.mailbot.result);
                    //iframe.innerHTML = "data:text/html;charset=utf-8,"+escape("<html><body><h1>hello world hahah</h1></body></html>");
                    //iframe.innerHTML = "<html><body><h1>hello world hahaha</h1></body></html>";
                    //console.log('pl2222');
                    //iframe.src = "/run";
                    //alert(body.src);
                    //$scope.mailbot.trustSrc ="data:text/html;charset=utf-8,"+"<html><body><h1>hello world hahah</h1></body></html>";
                    //alert("ok2");
                });
            //element.html("Student: <b>"+$scope.student.name +"</b> , Roll No: <b>"+$scope.student.rollno+"</b><br/>");
            //element.css("background-color", "#ff00ff");
        }
      return linkFunction;
   }
   return directive;
});

/*
ngApp.directive("screen", function () {
  function link(scope, element) {
    var iframe = document.createElement('iframe');
    var element0 = element[0];
    element0.appendChild(iframe);

    var body = iframe.contentDocument.body;
    iframe.innerHTML = "<html><body><h1>hello world</h1></body></html>";

    scope.$watch('scope.mailbot_result', function () {
        //body.innerHTML = scope.mailbot_result;
        body.innerHTML = "<html><body><h1>hello world</h1></body></html>";
        //alert("ok2");
    });
  }

  return {
    link: link,
    restrict: 'E',
    scope: {
      mailbot_result: '='
    }
  };
});
*/