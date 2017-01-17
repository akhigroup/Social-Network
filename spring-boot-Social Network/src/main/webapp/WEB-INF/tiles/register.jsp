<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!-- JSP - Standard Tag Library (JSTL) -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!-- data format tags  -->
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<div class="row">
	<div class="col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
				
		<%-- <form:errors path="email"/>
		<form:errors path="password"/> --%>
		<div class="login-error">
			<form:errors path="userDemo.*"/> <!-- Pt. a afisa erroarea -->
		</div>

		<div class="panel panel-default">

			<div class="panel-heading">
				<div class="panel-title">Create an Account</div>
			</div>

			<div class="panel-body">
				<form:form method="post" modelAttribute="userDemo" class="login-form"> <!-- Folosim Spring form tags -->

					<div class="input-group">
						<form:input type="text" path="firstname" placeholder="First Name" 
							class="form-control" />
						<span class="input-group-btn" style="width: 20px"></span>
						<form:input type="text" path="surname" placeholder="Surname" 
							class="form-control" />
					</div>

					<div class="input-group">
						<form:input type="text" path="email" placeholder="Email" 
							class="form-control" /><%-- path-ul tine numele variabilei "email" din clasa SiteUser --%>
					</div>

					<div class="input-group">
						<form:input type="password" path="plainPassword" placeholder="Password" 
							class="form-control" /><%-- path-ul tine numele varibilei "password" din clasa SiteUser --%>
							<%-- Punem parola in "plainPassword" si o validam inainte de a o encoda--%>
					</div>

					<div class="input-group">
						<form:input type="password" path="repeatPassword" placeholder="Repeat Password" class="form-control" />
					</div>

					<div class="input-group">
						<button type="submit" class="btn-primary pull-right">Register</button>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</div>