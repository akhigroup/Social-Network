<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<c:if test="${page.totalPages > 1}"> <!-- Afisam numerotarea paginilor doar daca sunt mai mult de 2 -->

<div class="pagination">

	<c:forEach var="pageNumberDemo" begin="1" end="${page.totalPages}"><!-- Din clasa Page apelam metoda getTotalPages() -->
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
	<p class="profile-results-name">Result search for: "<c:out value="${interest}"></c:out>"</p>
</div>


<c:if test="${empty page}">
	<strong>No results were found</strong>
</c:if>

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
