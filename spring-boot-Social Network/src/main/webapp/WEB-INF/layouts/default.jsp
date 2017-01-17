<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<!-- Pentru Context Root -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- For tags -->
<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>

<title><tiles:insertAttribute name="title" /></title>

<!-- adugam Context Root pentru ca linkurile care ex: css sa functioneze si cand dam deploy la un war pe server -->
<c:set var="contextRootDemo" value="${pageContext.request.contextPath}" />

<!-- Bootstrap -->
<link href="${contextRootDemo}/css/bootstrap.min.css" rel="stylesheet">
<link href="${contextRootDemo}/css/main.css" rel="stylesheet">

<!-- Only For JavaScript tagging (on the profile) -->
<link href="${contextRootDemo}/css/jquery.tagit.css" rel="stylesheet">

<script
		src="${contextRootDemo}/js/jquery-ui.min.js"></script>
<script
		src="${contextRootDemo}/js/tag-it.min.js"></script>

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>
<body>

	<!-- Static navbar -->
	<nav class="navbar navbar-default navbar-static-top">
	<div class="container">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#navbar" aria-expanded="false"
				aria-controls="navbar">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span> <span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="${contextRootDemo}/">Spring Boot Tutorial</a>
		</div>
		<div id="navbar" class="navbar-collapse collapse">
			<ul class="nav navbar-nav">
				<li class="active"><a href="${contextRootDemo}/">Home</a></li>
				<li><a href="${contextRootDemo}/about">About</a></li>
			</ul>
			<ul class="nav navbar-nav navbar-right">
			
			<sec:authorize access="!isAuthenticated()"> <!-- Doar daca userul nu este logat se va vedea "Login" -->
				<li><a href="${contextRootDemo}/login">Login</a></li>
				<li><a href="${contextRootDemo}/register">Register</a></li>
			</sec:authorize>
				
				<sec:authorize access="isAuthenticated()"> <!-- Doar daca userul este autentificat putem vedea ce e mai jos -->
					<li><a href="${contextRootDemo}/profile">Profile</a></li>
					<li><a href="javascript:$('#logoutForm').submit();">Logout</a></li> <!-- Logout link cu Jquery-->
				</sec:authorize>
				
				<sec:authorize access="hasRole('ROLE_ADMIN')">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown" role="button" aria-haspopup="true"
						aria-expanded="false">Status <span class="caret"></span></a>
						<ul class="dropdown-menu">
							<li><a href="${contextRootDemo}/addstatus">Add Status</a></li>
							<li><a href="${contextRootDemo}/viewstatus">View Status Update</a></li>
						</ul></li>
				</sec:authorize>
			</ul>
		</div>
		<!--/.nav-collapse -->
	</div>
	</nav>

	<c:url var="logoutLink" value="/logout"/>
	<form id="logoutForm" method="post" action="${logoutLink}"> <!-- Pt a ne deloga trebuie sa trimitem un form -->
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	</form>

	<div class="container">
		<tiles:insertAttribute name="content" />
	</div>

	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script src="${contextRootDemo}/js/bootstrap.min.js"></script>


</body>
</html>