<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<c:url var="search" value="/search"/> 

<div class="row">
	<!-- in clasa "row" de la StrapBoot exista 12 coloane -->
	<div class="col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
		<div class="homepage-status">${statusUpdate.text}</div>
	</div>
</div>

<div class="status-row">
	<div class="col-md-8 col-md-offset-2">
		<form method="get" action="${search}">
			<%-- <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> --%><!-- if method is : POST/PUT/DELETE we need csrf -->
			<div class="input-group input-group-lg">
				<input type="text" class="form-control" name="interest" placeHolder="Search on Interests"/>
				<span class="input-group-btn">
					<button id="search-button" class="btn btn-primary" type="submit">Find People</button>
				</span>
			</div>
		</form>
	</div>
</div>
