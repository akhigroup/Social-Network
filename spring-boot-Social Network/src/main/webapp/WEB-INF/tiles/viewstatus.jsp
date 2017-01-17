<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%><!-- data format tags  -->

<c:url var="urlDemo" value="/viewstatus"/> <!-- copiem in variabila urlDemo "context-root" al aplicatiei de unde ruleaza -->

<c:if test="${page.totalPages > 1}"> <!-- Afisam numerotarea paginilor doar daca sunt mai mult de 2 -->

<div class="pagination">

	<c:forEach var="pageNumberDemo" begin="1" end="${page.totalPages}"><!-- Din clasa Page apelam metoda getTotalPages() -->
		<c:choose>
			<c:when test="${pageNumberDemo - 1 != page.number}">
				<a href="${urlDemo}?p=${pageNumberDemo}">
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

<c:forEach var="varDemo" items="${page.content}">

	<c:url var="editlink" value="/editstatus?id=${varDemo.id}"/> <!-- statusUpdate.id apeleaza getId() dn clasa StatusUpdate -->
	<c:url var="deletelink" value="/deletestatus?id=${varDemo.id}"/>
	
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="panel-title">
				Status update added on
				<fmt:formatDate pattern="EEEE d MMMM Y 'at' H:mm:s"
					value="${varDemo.added}" />
			</div>
			<!-- .added apeleaza de fapt getAdded() -->
		</div>
		<div class="panel-body">

			<%-- <c:out value="${varDemo.text}" />  Pentru a nu mai vedea de ex: <bold> text </bold> afisam exact ce vedem--%>
			
			
			<div>${varDemo.text}</div>
			<div class="edit-links pull-right"> <!-- Punem linkurile pentru edit si delete -->
				<a href="${editlink}">edit</a> | <a href="${deletelink}" onclick="return confirm('Want to delete this status?')">delete</a> <!-- "editlink si "deletekink" contin url+id-->
			</div>

		</div>
	</div>

</c:forEach>