<%@ include file="/common/taglibs.jsp"%>

<head>
	<title>Remote Providers</title>
</head>

<body id="config">
	<form:form modelAttribute="error" name="formErrors">
		<form:errors cssClass="errors" />
	</form:form>

	<h2>Remote Provider <c:out value="${ remoteProviderType.name }"/></h2>
	
	<div id="helpText">
		Remote Providers are links to services which
		import vulnerability data into ThreadFix.
	</div>

	<!-- TODO make this smarter by only showing relevant fields -->
	<spring:url value="" var="emptyUrl"/>
	<form:form modelAttribute="remoteProviderType" method="post" autocomplete="off" action="${ fn:escapeXml( emptyUrl) }">
	<table class="dataTable">
		<tbody>
			<c:if test="${remoteProviderType.hasUserNamePassword }">
				<tr>
					<td>Username:</td>
					<td class="inputValue">
						<c:if test="${ not empty remoteProviderType.username }">
							<script>
								initialUsername = '<c:out value="${ remoteProviderType.username }"/>';
							</script>
						</c:if>
						<form:input id="usernameInput" path="username" size="50" maxlength="60" />
					</td>
					<td style="padding-left:5px">
						<form:errors path="username" cssClass="errors" />
					</td>
				</tr>
				<tr>
					<td>Password:</td>
					<td class="inputValue">
						<form:input id="passwordInput" type="password" path="password" size="50" maxlength="60" />
					</td>
					<td style="padding-left:5px">
						<form:errors path="password" cssClass="errors" />
					</td>
				</tr>
			</c:if>
			<c:if test="${remoteProviderType.hasApiKey}">
				<tr>
					<td>API Key:</td>
					<td class="inputValue">
						<c:if test="${ not empty remoteProviderType.apiKey }">
							<script>
								initialApiKey = '<c:out value="${ remoteProviderType.apiKey }"/>';
							</script>
						</c:if>
						<form:input id="apiKeyInput" path="apiKey" size="50" maxlength="60" />
					</td>
					<td style="padding-left:5px">
						<form:errors path="apiKey" cssClass="errors" />
					</td>
				</tr>
			</c:if>
			<c:if test="${ isQualys }">
				<tr>
					<td>Region:</td>
					<td class="inputValue">
						<form:radiobutton path="isEuropean" value="false"/> US 
						<form:radiobutton path="isEuropean" value="true"/> EU
					</td>
				</tr>
			</c:if>
		</tbody>
	</table>
	<c:if test="${remoteProviderType.hasUserNamePassword }">
		<button style="margin-top:10px;" id="submitButton" class="btn btn-primary" type="submit" onclick="if (initialUsername && initialUsername !== $('#usernameInput').val()) { return confirm('Warning: You have changed your username, all existing ${ remoteProviderType.name } apps will be deleted.') }">Save</button>
	</c:if>
	
	<c:if test="${not remoteProviderType.hasUserNamePassword }">
		<button style="margin-top:10px;" id="submitButton" class="btn btn-primary" type="submit" onclick="if (initialApiKey && initialApiKey !== $('#apiKeyInput').val()) { return confirm('Warning: You have changed your API key, all existing ${ remoteProviderType.name } apps will be deleted.') }">Save</button>
	</c:if>
	
	<span style="padding-left:10px"><a id="backToIndexLink" href="<spring:url value="/configuration/remoteproviders" htmlEscape="true"/>">Back to Index</a></span>
		
	</form:form>
	
</body>