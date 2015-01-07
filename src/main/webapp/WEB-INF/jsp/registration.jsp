<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="security"
		   uri="http://www.springframework.org/security/tags" %>
<c:set var="baseURL"
	   value="${fn:replace(pageContext.request.requestURL, pageContext.request.requestURI, pageContext.request.contextPath)}"/>

<!DOCTYPE html>
<html ng-app="tt-registration">
<head>
	<meta charset="utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

	<title>Expense Tracker</title>

	<link rel="stylesheet" type="text/css" href="${baseURL}/static/css/semantic.css">

	<script src="${baseURL}/static/js/jquery-2.0.3.js"></script>
	<script src="${baseURL}/static/js/semantic.js"></script>

</head>
<body>
<div class="ui two column grid">
	<div class="three wide column">
		<div class="ui horizontal inverted basic segment">
			<h1 class="ui right aligned header">ExpenseTracker</h1>
		</div>
	</div>
	<div class="column">
		<div class="ui horizontal basic segment">
			<h1 class="ui header">Registration</h1>
		</div>
	</div>
</div>

<div class="ui centered page grid">
	<div class="ten wide column">
		<div class="ui segment">
			<form name="form" class = "ui error form" method="post" ng-controller="registrationController" novalidate>
				<h4 class="ui dividing header">Creating an account</h4>

				<div class="ui error message" ng-show="error.show">
					<div class="header">{{error.message}}</div>
				</div>

				<div class="required field">
					<label>Email</label>
					<input placeholder="Email" type="email" name="email" ng-model="data.email" required>
				</div>
				<div class="required field">
					<label>Password</label>
					<input placeholder="Password" type="password" name="password" ng-model="data.password" required>
				</div>
				<div class="required field">
					<label>Confirm password</label>
					<input placeholder="Confirm password" type="password" ng-model="data.confirm" required>
				</div>
				<input type="submit" class="ui submit button" value="Register" ng-click="submit(data)"/>
			</form>
		</div>
	</div>
</div>

<script src="${baseURL}/static/js/angular.min.js"></script>
<script>
	angular.module('tt-registration', [])
		.config(['$httpProvider', '$locationProvider', function ($httpProvider, $locationProvider) {
			$locationProvider.html5Mode(true);
			$httpProvider.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded";
			$httpProvider.defaults.transformRequest.unshift(function (data, headersGetter) {
				var key, result = [];
				for (key in data) {
					if (data.hasOwnProperty(key)) {
						result.push(encodeURIComponent(key) + "=" + encodeURIComponent(data[key]));
					}
				}
				return result.join("&");
			});
		}])
		.controller('registrationController', ['$scope', '$http', function($scope, $http) {
				var submissionURL = '${baseURL}/api/register';

				$scope.error = {show: false, message:''};
				$scope.data = {password: '', confirm: ''};

				$scope.submit = function(data) {
					if (data.password.length == 0 || data.password != data.confirm) {
						$scope.error = {show: true, message:"Passwords doesn't match"};
						return;
					}

					$http.post(submissionURL, {email: data.email, password: data.password})
							.success(function(data, status) {
								window.location = "${baseURL}";
							})
							.error(function(data) {
								$scope.error = {show: true, message: data};
							});
					return false;
				}

	}]);

</script>
</body>
</html>