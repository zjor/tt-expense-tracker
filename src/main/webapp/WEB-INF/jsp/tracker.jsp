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
<div class="ui three column grid">
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
	<div class="column">
		<div class="ui right aligned basic segment">
			<div class="ui label">
				Welcome, <security:authentication property="principal.email"/>
				<a class="detail" href="${logoutUrl}"><i class="sign out icon"></i></a>
			</div>
		</div>
	</div>
</div>

<div class="ui centered page grid">
	<div class="ui eight wide column">
		<h2 class="ui header">Expenses</h2>

		<div class="ui fluid icon input" ng-controller="filterController">
			<input type="text" placeholder="Search..." ng-change="change()" ng-model="filter">
			<i class="search icon"></i>
		</div>

		<div class="ui basic segment">
			<div class="ui basic button" id="showAddDialog">
				<i class="add circle icon"></i>
				Add Expense
			</div>
		</div>
		<div class="ui selection divided list" ng-controller="expensesListController">
			<div class="item" ng-repeat="expense in storage.expenses" ng-click="onClick(expense)">
				<div class="right floated compact ui">{{expense.amount | currency:"$":2}}</div>

				<div class="content">
					<div class="header">{{expense.description}}</div>
					<div class="description">{{expense.timestamp | date:"MM/dd/yy h:mma"}} {{expense.comment}}</div>
				</div>

			</div>
		</div>
	</div>
	<div class="ui six wide column" ng-controller="reportController">
		<h2 class="ui header">Weekly Report</h2>

		<div class="ui icon button" ng-click="left()">
			<i class="chevron left icon"></i>
		</div>

		<div class="ui large label">{{report.weekStart | date:"yyyy-MM-dd"}}</div>

		<div class="ui icon button" ng-click="right()">
			<i class="chevron right icon"></i>
		</div>

		<h3 class="ui header">Total: {{report.total | currency:"$":2}}</h3>

		<h3 class="ui header">Average: {{report.average | currency:"$":2}}</h3>

		<div class="ui selection divided list">
			<div class="item" ng-repeat="expense in expenses">
				<div class="right floated compact ui">{{expense.amount | currency:"$":2}}</div>

				<div class="content">
					<div class="header">[{{expense.timestamp | date:"MMM dd"}}] {{expense.description}}</div>
				</div>

			</div>
		</div>

	</div>
</div>

<%-- Add Expense Dialog--%>
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

					<div class="field">
						<input placeholder="Amount" type="text" name="amount" ng-model="expense.amount">
					</div>
				</div>
				<div class="two fields">
					<div class="required field">
						<label>Date</label>
						<input placeholder="yyyy-MM-dd" type="text" name="date" ng-model="expense.date">
					</div>
					<div class="required field">
						<label>Time</label>
						<input placeholder="HH:mm:SS" type="text" name="time" ng-model="expense.time">
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

<%-- Edit & Remove Expense Dialog --%>
<div class="ui modal" id="editDialog" ng-controller="editExpenseController">
	<i class="close icon"></i>

	<div class="header">
		Edit Expense
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

					<div class="field">
						<input placeholder="Amount" type="text" name="amount" ng-model="expense.amount">
					</div>
				</div>
				<div class="two fields">
					<div class="required field">
						<label>Date</label>
						<input placeholder="yyyy-MM-dd" type="text" name="date" ng-model="expense.date">
					</div>
					<div class="required field">
						<label>Time</label>
						<input placeholder="HH:mm:SS" type="text" name="time" ng-model="expense.time">
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
		<div class="ui negative button" ng-click="delete(expense.id)">
			Delete
		</div>
		<div class="ui positive button" ng-click="save()">
			Save
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
				var selection = {};
				var filter = '';

				function prepareParams(obj) {
					var params = {};

					for (var attr in obj) {
						if (obj.hasOwnProperty(attr)) params[attr] = obj[attr];
					}
					params.timestamp = Date.parse(obj.date + "T" + obj.time + "+01:00");
					return params;
				}

				function load() {
					var params = '?';
					if (filter.length > 0) {
						params = '?filter=' + encodeURIComponent(filter);
					}

					$http.get('${baseURL}/api/expenses' + params).success(function (expensesResponse) {
						if (expensesResponse) {
							expenses.length = 0;
							expensesResponse.forEach(function (item) {
								expenses.push(item)
							});
						}
					});
				}

				function add(expense, callback) {
					var params = {};

					for (var attr in expense) {
						if (expense.hasOwnProperty(attr)) params[attr] = expense[attr];
					}
					params.timestamp = Date.parse(expense.date + "T" + expense.time);

					$http.post('${baseURL}/api/expenses', prepareParams(expense))
							.success(function () {
								callback();
								load();
							});
				}

				function saveSelection(callback) {
					$http.post('${baseURL}/api/expenses/' + selection.id, prepareParams(selection))
							.success(function () {
								callback();
								load();
							});
				}

				function remove(id, callback) {
					$http.delete('${baseURL}/api/expenses/' + id).success(function () {
						for (var attr in selection) {
							if (selection.hasOwnProperty(attr)) {
								delete selection[attr];
							}
						}
						callback();
						load();
					});
				}

				function select(expense) {
					for (var attr in expense) {
						if (expense.hasOwnProperty(attr)) selection[attr] = expense[attr];
					}
					if (selection.hasOwnProperty('timestamp')) {
						var d = (new Date(selection.timestamp));
						selection.time = getTimeString(d);
						selection.date = getDateString(d);
					}
				}

				function setFilter(value) {
					filter = value;
				}

				return {
					expenses: expenses,
					selection: selection,
					setFilter: setFilter,
					select: select,
					load: load,
					add: add,
					remove: remove,
					saveSelection: saveSelection
				}
			}])
			.factory('reportStorage', ['$http', function ($http) {
				var params = {weekStart: (new Date()).getTime(), total: 0, average: 0};
				var expenses = [];

				function load() {
					$http.get('${baseURL}/api/weekly?date=' + params.weekStart).success(function(report) {
						params.total = report.total;
						params.average = report.average;
						params.weekStart = report.weekStart;

						expenses.length = 0;
						report.expenses.forEach(function (item) {
							expenses.push(item)
						});

					});
				}

				return {
					expenses: expenses,
					params: params,
					load: load
				}
			}])
			.controller('expensesListController', ['$scope', 'expensesStorage', function ($scope, expensesStorage) {
				$scope.storage = expensesStorage;
				expensesStorage.load();
				$scope.onClick = function (expense) {
					expensesStorage.select(expense);
					$('#editDialog').modal({onApprove: function () {
						return false;
					}, onDeny: function () {
						return false;
					}}).modal('show');
				}

			}])
			.controller('addExpenseController', ['$scope', 'expensesStorage', 'reportStorage', function ($scope, expensesStorage, reportStorage) {
				var d = new Date();

				$scope.expense = {
					time: getTimeString(d),
					date: getDateString(d)
				};

				$scope.add = function () {
					expensesStorage.add($scope.expense, function (response) {
						reportStorage.load();
						$('#addDialog').modal('hide');
						$scope.expense = {};
					});
				}
			}])
			.controller('editExpenseController', ['$scope', 'expensesStorage', 'reportStorage', function ($scope, expensesStorage, reportStorage) {
				$scope.expense = expensesStorage.selection;
				$scope.save = function () {
					expensesStorage.saveSelection(function () {
						reportStorage.load();
						$('#editDialog').modal('hide');
					});
				}
				$scope.delete = function (id) {
					expensesStorage.remove(id, function () {
						reportStorage.load();
						$('#editDialog').modal('hide');
					});
				}
			}])
			.controller('filterController', ['$scope', 'expensesStorage', function ($scope, expensesStorage) {
				$scope.filter = '';
				$scope.change = function () {
					expensesStorage.setFilter($scope.filter);
					expensesStorage.load();
					console.log($scope.filter);
				}

			}])
			.controller('reportController', ['$scope', 'reportStorage', function($scope, reportStorage) {
				reportStorage.load();

				$scope.report = reportStorage.params;
				$scope.expenses = reportStorage.expenses;

				$scope.left = function() {
					reportStorage.params.weekStart -= 1;
					reportStorage.load();
				};
				$scope.right = function() {
					reportStorage.params.weekStart += 8*24*3600*1000;
					reportStorage.load();
				};

			}]);

	$(function () {
		$('#showAddDialog').click(function () {
			$('#addDialog').modal({onApprove: function () {
				return false;
			}}).modal('show');
		});
		$('select.dropdown').dropdown();
	});

	function zeroPrefix(n) {
		if (n < 10) {
			return "0" + n;
		}
		return n;
	}

	function getTimeString(d) {
		return zeroPrefix(d.getHours()) + ":" + zeroPrefix(d.getMinutes()) + ":" + zeroPrefix(d.getSeconds());
	}

	function getDateString(d) {
		return d.getFullYear() + "-" + zeroPrefix(d.getMonth() + 1) + "-" + zeroPrefix(d.getDate());
	}

</script>
</body>
</html>