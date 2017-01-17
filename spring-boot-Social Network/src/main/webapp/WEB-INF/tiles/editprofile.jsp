<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	<!-- JSP - Standard Tag Library (JSTL) -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> <!-- data format tags  -->
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<div class="row">
	<div class="col-md-8 col-md-offset-2">
		  		
		<div class="panel panel-default">
			<div class="panel-heading">
				<div class="panel-title">Edit your profile</div>
			</div>
			<div class="panel-body">

				<form:form modelAttribute="profile"> 
				
					<div class="errors">
						<form:errors path="about"/> <!-- corespunde cu adnotarea @Size si message a variabilei "about" din clasa Profile -->
					</div> <!-- daca punem acest div in afara form-ului trebuie mentionat in path si  modelAttribute, respectiv path="profile.about"-->
						
					<div class="form-group">
					<!-- in path, "text" invoca metoda setAbout (din clasa Profile)  -->
						<form:textarea path="about" name="text" rows="10" cols="50"></form:textarea>
					</div>

					<input type="submit" name="submit" value="Save" />
				</form:form>
			</div>
		</div>

	</div>
</div>
<!-- Java script open source pentru text editor, gasit pe net -->
<script src='//cdn.tinymce.com/4/tinymce.min.js'></script>
  <script>
  tinymce.init({
    selector: 'textarea', /*  selectorul este cel cel din form:textarea...... */
    plugins: "link"
  });
  </script>