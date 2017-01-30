<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!-- JSP - Standard Tag Library (JSTL) -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!-- data format tags  -->
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<c:url var="profilePhoto" value="/profilephoto/${userId}" />
<c:url var="editProfileAbout" value="/edit-profile-about" />
<c:url var="saveInterest" value="/save-interest" />
<c:url var="deleteInterest" value="/delete-interest" />

<div class="row">
	<div class="col-md-10 col-md-offset-1">
	
	<div id="profile-photo-status"></div>
	
		<div>
			<a></a>
		</div>
		<div id="interestDiv">
			<ul id="interestList">
				<c:choose>
					<c:when test="${empty profile.interests}">
						<li>Add your interests here (ex: karate)</li>
					</c:when>
					<c:otherwise>
						<c:forEach var="interest" items="${profile.interests}">
							<li>
								<c:out value="${interest}"></c:out>
							</li>
						</c:forEach>
					</c:otherwise>
					
				</c:choose>
				
			</ul> 
		</div>

		<div class="profile-about">

			<div class="profile-img">
			<div>
				<img id=profilePhotoImage src="${profilePhoto}" />
			</div>
			<div class="profile-text-name">
				<c:out value="${profile.surName}"></c:out>
				<c:out value="${profile.firstName}"></c:out>
			</div>
			<div class="text-center">
				<c:if test="${ownProfile == true}">
					<a href="#" id=upload_link>Upload photo</a>
				</c:if>
			</div>
			</div>

			<div class="profile-text">

				<c:choose>
					<c:when test="${profile.about == null}">
						No Profile set yet.							
					</c:when>
					<c:otherwise>
						${profile.about}
					</c:otherwise>
				</c:choose>
			</div>

		</div>
		<div class="profile-about-edit">
			<c:if test="${ownProfile == true}">
				<a href="${editProfileAbout}">edit</a>
			</c:if>
		</div>
		
		<c:url var="uploadPhotoLink" value="/upload-profile-photo"/>
		<form method="post" enctype="multipart/form-data" action="${uploadPhotoLink}" id="photoUploadForm">
		
			<input type="file" accept="image/*" name="file" id="photoFileInput"/>
			<input type="submit" value="upload" style="margin-top:20px"/>
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
							
		</form>
	</div>
</div>
<script>

function setUploadStatusText(text){
	$("#profile-photo-status").text(text);
	window.setTimeout(function() {
		$("#profile-photo-status").text("");
		},2000);
}

function uploadSuccess(data){
	$("#profilePhotoImage").attr("src", "${profilePhoto};t=" + new Date());
	
	$("#photoFileInput").val("");

	setUploadStatusText(data.message);
}


function uploadPhoto(event){

	$.ajax({
		url: $(this).attr("action"),
		type: 'POST',
		data: new FormData(this),
		processData: false,
		contentType: false,
		success: uploadSuccess,
		error: setUploadStatusText("Server unreachable")
			
	});

	event.preventDefault();
}

	function deleteInterest(text){
		editInterest(text, "${deleteInterest}");
	}

	function saveInterest(text){
		editInterest(text, "${saveInterest}");
	}

	function editInterest(text, actionUrl){

		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");

		$.ajaxPrefilter(function(options, originalOptions, jqXHR){
			jqXHR.setRequestHeader(header, token);
			});

		$.ajax({
			url: actionUrl,
			data: {
				'name': text
				},
			type: 'POST',
			success: function(){
				alert("ok");
				},
			error: function(){
				alert("ok");
				}
			})
		
		}

	$(document).ready(function(){

		$("#interestList").tagit({
		
			afterTagAdded: function(event, ui){
				if (ui.duringInitialization != true)
					saveInterest(ui.tagLabel);
		},

			afterTagRemoved: function(event, ui){
				deleteInterest(ui.tagLabel);
		},

			caseSensitive : false,
			allowSpaces : true,
			tagLimit : 10,
			readOnly: '${ownProfile}' == 'false' //we edit only if is our own profile

		});

	$("#upload_link").click(function(event){
		event.preventDefault();
		$("#photoFileInput").trigger('click');
	});
	 
	$("#photoFileInput").change(function() {
		$("#photoUploadForm").submit();
	});	
		 
	$("#photoUploadForm").on("submit", uploadPhoto);
}); 

</script>