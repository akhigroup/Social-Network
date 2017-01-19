<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<c:if test="${page.totalPages > 1}">

<div class="pagination">

	<c:forEach var="pageNumberDemo" begin="1" end="${page.totalPages}">
		<c:choose>
			<c:when test="${pageNumberDemo - 1 != page.number}">
				<a href="${url}?interest=${interest}&p=${pageNumberDemo}">
					<c:out value="${pageNumberDemo}" />
				</a>
			</c:when>
			<c:otherwise>
					<strong><c:out value="${pageNumberDemo}" /></strong>
			</c:otherwise>
		</c:choose>
		<c:if test="${pageNumberDemo != page.totalPages}"> | </c:if>
	</c:forEach>
</div>
</c:if>
<div>
	<c:choose>
		<c:when test="${not empty interests}">
			<p class="profile-results-name">Result search for: "<c:out value="${interest}"></c:out>"</p>
			<c:if test="${fn:length(interests) gt 1 || interests[0] != interest}">
				<p class="profile-results-interest-list">found these related <c:out value="${interests}"/></p>
			</c:if>
		</c:when>
		<c:otherwise>
			<p class="profile-results-name">No results found</p>
		</c:otherwise>
	</c:choose>
</div>

<table class="table">
	<thead>
		<tr>
			<th></th>
		</tr>
		</thead>
	<c:forEach var="searchedProfile" items="${page.content}">
		<tbody>
			<tr>
				<td>
					<div class="profile-results">
						<c:url var="profilePhoto" value="/profilephoto/${searchedProfile.userId}" /> <!-- Accesam url-ul mapat: /profilephoto/{id}  pt a primii poza-->
						<c:url var="profileLink" value="/profile/${searchedProfile.userId}" /> <!-- url-ul profilului -->
						<a href="${profileLink}">
							<img src="${profilePhoto}" />
						</a>
					</div>
					<div class="profile-results">
						<a href="${profileLink}" class="profile-results-name">
							<c:out value="${searchedProfile.surname}"></c:out>
							<c:out	value="${searchedProfile.firstname}"></c:out>
						</a>
						<div>
							<c:forEach var="interest" items="${searchedProfile.interests}" varStatus="index">
								<a href="/search?interest=${interest}">
									<span class="profile-results-interests">${interest}</span>
								</a>
								 <c:if test="${!index.last}"> | </c:if>
							</c:forEach>
						</div>
					</div>
				<div>
				</div>
				</td>
			</tr>
		</tbody>
	</c:forEach >
