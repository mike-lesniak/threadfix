<%@ include file="/common/taglibs.jsp"%>

	<h3 style="padding-top:8px">Unmapped Findings:</h3>

	<spring:url value="/login.jsp" var="loginUrl"/>
	<spring:url value="{scanId}/table" var="tableUrl">
		<spring:param name="scanId" value="${ scan.id }"/>
	</spring:url>
	<c:if test="${ numFindings > 100 }">
	<div style="padding-bottom:8px">	
		<c:if test="${ page > 4 }">
			<a href="javascript:refillElement('#toReplace2', '${tableUrl}', 1, '<c:out value="${ loginUrl }"/>')">First</a>
		</c:if>
	
		<c:if test="${ page >= 4 }">
			<a href="javascript:refillElement('#toReplace2', '${tableUrl}', ${ page - 3 }, '<c:out value="${ loginUrl }"/>')"><c:out value="${ page - 3 }"/></a>
		</c:if>
	
		<c:if test="${ page >= 3 }">
			<a href="javascript:refillElement('#toReplace2', '${tableUrl}', ${ page - 2 }, '<c:out value="${ loginUrl }"/>')"><c:out value="${ page - 2 }"/></a>
		</c:if>
		
		<c:if test="${ page >= 2 }">
			<a href="javascript:refillElement('#toReplace2', '${tableUrl}', ${ page - 1 }, '<c:out value="${ loginUrl }"/>')"><c:out value="${ page - 1 }"/></a>
		</c:if>
		
		<c:out value="${ page }"/>
	
		<c:if test="${ page <= numPages }">
			<a href="javascript:refillElement('#toReplace2', '${tableUrl}', ${ page + 1 }, '<c:out value="${ loginUrl }"/>')"><c:out value="${ page + 1 }"/></a>
		</c:if>
		
		<c:if test="${ page <= numPages - 1 }">
			<a href="javascript:refillElement('#toReplace2', '${tableUrl}', ${ page + 2 }, '<c:out value="${ loginUrl }"/>')"><c:out value="${ page + 2 }"/></a>
		</c:if>
		
		<c:if test="${ page <= numPages - 2 }">
			<a href="javascript:refillElement('#toReplace2', '${tableUrl}', ${ page + 3 }, '<c:out value="${ loginUrl }"/>')"><c:out value="${ page + 3 }"/></a>
		</c:if>
		
		<c:if test="${ page < numPages - 2 }">
			<a href="javascript:refillElement('#toReplace2', '${tableUrl}', ${ numPages + 1 }, '<c:out value="${ loginUrl }"/>')">Last (<c:out value="${ numPages + 1}"/>)</a>
		</c:if>
		
		<input type="text" class="refillElementOnEnter2" id="pageInput" />
		<a href="javascript:refillElementDropDownPage('#toReplace2', '${ tableUrl }', '<c:out value="${ loginUrl }"/>')">Go to page</a>
	</div>
	</c:if>

	<table class="table auto table-striped sortable" id="1">
		<thead>
			<tr>
				<th class="first">Severity</th>
				<th>Vulnerability Type</th>
				<th>Path</th>
				<th>Parameter</th>
				<th>Vulnerability Link</th>
				<th class="last">Number Merged Results</th>
			</tr>
		</thead>
		<tbody>
	<c:choose>
		<c:when test="${ empty findingList }">
			<tr class="bodyRow">
				<td colspan="6" style="text-align: center;"> All Findings were mapped to vulnerabilities.</td>
			</tr>
		</c:when>
		<c:otherwise>
		<c:forEach var="finding" items="${ findingList }" varStatus="status">
			<tr class="bodyRow">
				<td id="unmappedSeverity${ status.count }">
					<c:out value="${ finding.channelSeverity.name }"/>
				</td>
				<td id="unmappedVulnerability${ status.count }">
					<spring:url value="{scanId}/findings/{findingId}" var="findingUrl">
					<spring:param name="scanId" value="${ scan.id }" />
						<spring:param name="findingId" value="${ finding.id }" />
					</spring:url>
					<a href="${ fn:escapeXml(findingUrl) }">
					    <c:out value="${ finding.channelVulnerability.name }"/>
					</a>
				</td>
				<td id="unmappedPath${ status.count }">
					<c:out value="${ finding.surfaceLocation.path }"/>
				</td>
				<td id="unmappedParameter${ status.count }">
					<c:out value="${ finding.surfaceLocation.parameter }"/>
				</td>
				<td>
					<spring:url value="../vulnerabilities/{vulnerabilityId}" var="vulnerabilityUrl">
				    	<spring:param name="vulnerabilityId" value="${ finding.vulnerability.id }" />
			    	</spring:url>
			    	<a href="${ fn:escapeXml(vulnerabilityUrl) }">
						<c:out value="${ finding.vulnerability.id }"/>
					</a>
				</td>
				<td id="unmappedMergedResults${ status.count }">
					<c:out value="${ finding.numberMergedResults }"/>
				</td>
			</tr>
		</c:forEach>
		</c:otherwise>
	</c:choose>
		</tbody>
	</table>
	
	<script>
	$('.refillElementOnEnter2').keypress(function(e) {
		if (e.which == 13) {
			refillElementDropDownPage('#toReplace2', '<c:out value="${ tableUrl }"/>', '<c:out value="${ loginUrl }"/>');
			return false;
		}
	});
	</script>