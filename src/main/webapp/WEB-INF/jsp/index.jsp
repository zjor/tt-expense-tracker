<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="security"
		   uri="http://www.springframework.org/security/tags" %>
<c:url var="homeUrl" value="/"/>
<c:url var="loginUrl" value="/login"/>
<c:url var="postLoginUrl" value="/j_spring_security_check"/>
<c:url var="logoutUrl" value="/j_spring_security_logout"/>
<c:set var="baseURL"
	   value="${fn:replace(pageContext.request.requestURL, pageContext.request.requestURI, pageContext.request.contextPath)}"/>

<!DOCTYPE html>
<html>
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
			<h1 class="ui header">Login</h1>
		</div>
	</div>
</div>

<div class="ui centered page grid">
	<div class="ten wide column">
		<div class="ui segment">
			<form class="ui error form" action="${postLoginUrl}" method="post">
				<h4 class="ui dividing header">Please login</h4>

				<c:if test="${not empty param.loginFailed}">
					<div class="ui error message">
						<div class="header">Login failed</div>
						<p>Incorrect email or password was entered. Please try again.</p>
					</div>
				</c:if>

				<div class="field">
					<label>Email</label>
					<input placeholder="Email" type="text" name="j_username">
				</div>
				<div class="field">
					<label>Password</label>
					<input placeholder="Password" type="password" name="j_password">
				</div>
				<div class="ui two column grid">
					<div class="column">
						<input type="submit" class="ui submit button" value="Login">
					</div>
					<div class="right aligned column">
						<a href="#">Create account</a>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>
</body>
</html>