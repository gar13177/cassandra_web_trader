<%@include file="include/header.jsp" %>

<div class="panel panel-deafult">
	<div class="panel-heading">
		<h3 class="panel-title">Forma de Registro</h3>
	</div>
	
	<div class="panel-body">
		<form:form class="form" modelAttribute="registerForm" role="form">
			<div class="form-group">
				<form:label path="username">Username</form:label>
				<form:input path="username" class="form-control" placeholder="Your username"/>
				<form:errors cssClass="error" path="username"></form:errors>
			</div>
			<div class="form-group">
				<form:label path="email">Email address</form:label>
				<form:input path="email" type="email" class="form-control" placeholder="Your email address"/>
				<form:errors cssClass="error" path="email"></form:errors>
			</div>
			<div class="form-group">
				<form:label path="fullname">Full name</form:label>
				<form:input path="fullname" class="form-control" placeholder="Your full name"/>
				<form:errors cssClass="error" path="fullname"></form:errors>
			</div>
			<div class="form-group">
				<form:label path="password">Password</form:label>
				<form:password path="password" class="form-control" placeholder="Your username"/>
				<form:errors cssClass="error" path="password"></form:errors>
			</div>
			<div>
				<button type="submit">Register</button>
				<button type="reset">Clear</button>
			</div>
		</form:form>
	</div>
</div>

<%@include file="include/footer.jsp" %>