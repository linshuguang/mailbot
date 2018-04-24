<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title>Sample App Sign-up Page</title>
    <script src="/js/angular.min.js"></script>
</head>
<body ng-app="myNgApp">

<h1>First Demo</h1>

<!--div ng-controller="ruleController">
    <div><span>Enter Code:</span></div>
    <div>  <textarea type="text"  rows="24" cols="80" ng-model="rule.code" ></textarea> </div>
    <div> result: <pre><span >{{rule.result}}</span></pre></div>
    <button ng-click="executeCode()">execute code</button>
</div-->

<!--div class="editor-container">
    <div id="editor1"></div>
</div-->


<div ng-controller="mailbotController" >
    <div><span>Enter Code:</span></div>

    <table>
        <tr>
            <td>
                <!--div class="editor-container">
                    <div id="editor1"></div>
                </div-->
                <div>  <textarea type="text"  rows="24" cols="80" ng-model="mailbot.code" ></textarea> </div>
            </td>
            <td>
                <!--div> result: <div ng-bind-html="mailbot.result"></div></div-->
                <!--div> result: <pre><span >{{mailbot.result}}</span></pre></div-->
                <!--div> result: <iframe id="iframe-projects" ng-src="{{mailbot.trustSrc}}" frameborder="0"></iframe></div-->
                <div> result: <screen ></screen></div>

            </td>
    </tr>
    </table>
    <button ng-click="executeCode()">execute code</button>

    <!--iframe id="iframe-projects" ng-src="{{mailbot.trustSrc}}" frameborder="0"></iframe-->

    <!--div> another result:
        <div ng-repeat="record in mailbot.records" >
            <div ng-model="mailbot.records">{{record}}</div>
        </div>
    </div-->


</div>




<script src="/js/main.js"></script>
<!--script src="/js/rule.js"></script-->
<script src="/js/mailbot.js"></script>

<script src="/js/doctored/doctored.js"></script>
<script>
        var callback = function(){console.log("Doctored.js: initialized " + this.id + "!")};
        doctored.init("#editor1", {
            onload: callback,
            id:     "editor1" // unique id per domain used for saving data (localStorage key)
        });
</script>
</body>
</html>