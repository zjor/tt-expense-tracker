<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:url var="logoutUrl" value="/j_spring_security_logout"/>
<c:set var="baseURL"
	   value="${fn:replace(pageContext.request.requestURL, pageContext.request.requestURI, pageContext.request.contextPath)}"/>

<!DOCTYPE html>
<html ng-app="tt-main">
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
			<h1 class="ui header">Expenses</h1>
		</div>
	</div>
</div>

<h1>Welcome, <security:authentication property="principal.email"/></h1>
<a href="${logoutUrl}">Logout</a>

<div class="ui centered page grid">
	<div class="ui twelve wide column">
		<div class="ui basic button" id="showAddDialog">
			<i class="add circle icon"></i>
			Add Expense
		</div>
		<div class="ui selection divided list" ng-controller="expensesListController">
			<div class="item" ng-repeat="expense in storage.expenses">
				<div class="right floated compact ui">{{expense.amount | currency:"$":2}}</div>
				<div class="content">
					<div class="header">{{expense.description}}</div>
					<div class="description">{{expense.timestamp | date:"MM/dd/yy h:mma"}} - {{expense.comment}}</div>
				</div>

			</div>
		</div>
	</div>
</div>

<div class="ui modal" id="addDialog" ng-controller="addExpenseController">
	<i class="close icon"></i>

	<div class="header">
		Add Expense
	</div>
	<div class="content">
		<div class="description">
			<div class="ui form">
				<div class="required field">
					<label>Description</label>
					<input placeholder="Description" type="text" name="description" ng-model="expense.description">
				</div>

				<div class="required field">
					<label>Amount</label>

					<div class="two fields">
						<div class="field">
							<input placeholder="Amount" type="text" name="amount" ng-model="expense.amount">
						</div>
						<div class="field">
							<select class="ui dropdown" ng-model="expense.currency">
								<option value="usd">USD</option>
								<option value="eur">EUR</option>
								<option value="rub">RUB</option>
								<option value="czk" selected>CZK</option>
							</select>
						</div>
					</div>
				</div>
				<div class="two fields">
					<div class="required field">
						<label>Date</label>
						<input placeholder="MM/dd/yyyy" type="text" name="date" ng-model="expense.date">
					</div>
					<div class="required field">
						<label>Time</label>
						<input placeholder="HH:mm" type="text" name="time" ng-model="expense.time">
					</div>
				</div>

				<div class="field">
					<label>Comment</label>
					<input placeholder="Comment" type="text" name="comment" ng-model="expense.comment">
				</div>
			</div>
		</div>
	</div>
	<div class="actions">
		<div class="ui black button">
			Cancel
		</div>
		<div class="ui positive left labeled icon button" ng-click="add()">
			Add
			<i class="add circle icon"></i>
		</div>
	</div>
</div>

<script src="${baseURL}/static/js/angular.min.js"></script>
<script>
	angular.module('tt-main', [])
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
			.factory('expensesStorage', ['$http', function ($http) {
				var expenses = [];

				function load() {
					$http.get('${baseURL}/api/expenses').success(function (expensesResponse) {
						if (expensesResponse) {
							expenses.length = 0;
							expensesResponse.forEach(function (item) {
								expenses.push(item)
							});
						}
					});
				}

				function add(expense, callback) {
					$http.post('${baseURL}/api/expenses', {description: expense.description, amount: expense.amount, timestamp: (new Date()).getTime(), comment: expense.comment}).success(function(data) {
						callback();
						load();
					});

				}

				return {
					expenses: expenses,
					load: load,
					add: add
				}
			}])
			.controller('expensesListController', ['$scope', 'expensesStorage', function ($scope, expensesStorage) {
				$scope.storage = expensesStorage;
				expensesStorage.load();
			}])
			.controller('addExpenseController', ['$scope', 'expensesStorage', function ($scope, expensesStorage) {
				$scope.expense = {};
				$scope.add = function() {
					expensesStorage.add($scope.expense, function(response) {
						$('#addDialog').modal('hide');
						$scope.expense = {};
					});
				}

			}]);
	$(function () {
		$('#showAddDialog').click(function () {
			$('#addDialog').modal({onApprove: function() {return false;}}).modal('show');
		});
		$('select.dropdown').dropdown();

	});
</script>
</body>
</html>