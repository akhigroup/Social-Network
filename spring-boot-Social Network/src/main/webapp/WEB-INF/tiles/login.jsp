<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	<!-- JSP - Standard Tag Library (JSTL) -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> <!-- data format tags  -->
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<c:url var="loginUrl" value="/login"/>

<div class="row">
	<div class="col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
	
	<div class="register-prompt">
		Please log in or
		<a href="${contextRootDemo}/register"> register to create an account</a>
	</div>
		  		
	<c:if test="${param.error != null}"> <!-- daca exista erroare la logare afisam un mesaj -->
				<div class="login-error">Incorrect username or password.</div> 
			</c:if>
		  		
		<div class="panel panel-default">		
		
			<div class="panel-heading">
				<div class="panel-title">User Log In </div>
			</div>			
			
			<div class="panel-body">
				<form method="post" action="${loginUrl}" class="login-form">
			
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> <!-- este implementat de Spring "Cross-site request forgery" -->
			<!-- Adica se genereaza un token unic pentru fiecare sesiune, token care trebuie sa fie acelasi in momentul cand se incearca Login -->
				<div class="input-group"> <!-- clase din bootStrap pentru estetica -->
					<input type="text" name="username" placeholder="Username" class="form-control"/> <!-- name="username" este un nume predefinit care trebuie folosit-->
				</div>
				<div class="input-group">
					<input type="password" name="password" placeholder="Password" class="form-control"/>
				</div>
				<div class="input-group">
					<button type="submit" class="btn-primary pull-right">Sign In</button> <!-- clasa BootStrap -->
				</div>
				
			</form>
			</div>
		</div>
	</div>
</div>