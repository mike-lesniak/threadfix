<script type="text/ng-template" id="editUserForm.html">
    <div class="modal-header">
        <h4 id="myModalLabel">
            {{ pageTitle }}
            <span ng-show="user.id" class="delete-span">
                <input class="btn btn-danger" id="delete{{ user.name }}" type="submit" value="Delete" ng-click="clickedDeleteButton()"/>
            </span>
        </h4>
    </div>

    <div ng-form="form" class="modal-body">
        <table class="modal-form-table dataTable">
            <tbody>
                <tr>
                    <td>User</td>
                    <td class="inputValue">
                        <input ng-model="user.name" required type="text" name="name" id="name"/>
                        <span id="name.errors.required" class="errors" ng-show="form.name.$dirty && form.name.$error.required">Name is required.</span>
                        <span id="name.errors" class="errors" ng-show="user.name_error"> {{ user.name_error }}</span>
                    </td>
                </tr>
                <tr ng-if="!user.isLdapUser">
                    <td>Password</td>
                    <td class="inputValue">
                        <input password-validate="{{ user.passwordConfirm }}" id="password" ng-model="user.unencryptedPassword" type="password" id="passwordInput" name="unencryptedPassword" size="30"/>
                        <span id="password.error.length" class="errors" ng-show="lengthRemaining">{{ lengthRemaining }} characters needed</span>
                        <span id="password.error.match" class="errors" ng-show="form.unencryptedPassword.$dirty && form.unencryptedPassword.$error.matches">Passwords do not match.</span>
                        <span id="password.error" class="errors" ng-show="user.password_error"> {{ user.password_error }}</span>
                    </td>
                </tr>
                <tr ng-if="!user.isLdapUser">
                    <td>Confirm Password</td>
                    <td class="inputValue">
                        <input ng-model="user.passwordConfirm" id="confirm" type="password" style="margin-bottom:0" id="passwordConfirmInput" name="passwordConfirm" size="30" />
                    </td>
                </tr>
                <c:if test="${ ldap_plugin }">
                <tr>
                    <td class="no-color">LDAP user</td>
                    <td class="no-color" style="text-align: left;">
                        <input type="checkbox" class="ldapCheckbox"
                            id="isLdapUserCheckbox"
                            name="isLdapUser"
                            ng-model="user.isLdapUser"/>
                    </td>
                </tr>
                </c:if>
                <security:authorize ifAllGranted="ROLE_ENTERPRISE">
                <tr>
                    <td class="no-color">Global Access</td>
                    <td class="no-color" style="text-align: left;">
                        <input type="checkbox"
                            id="hasGlobalGroupAccessCheckbox"
                            class="globalAccessCheckBox"
                            name="hasGlobalGroupAccess"
                            ng-model="user.hasGlobalGroupAccess"/>
                    </td>
                </tr>
                <tr ng-show="user.hasGlobalGroupAccess">
                    <td class="no-color">Global Role</td>
                    <td class="no-color" style="text-align: left;">
                        <select id="roleSelect" name="globalRole.id" ng-model="user.globalRole.id">
                            <option value="0" label="Read Access">Read Access</option>
                            <option ng-selected="role.id === user.globalRole.id" ng-repeat="role in roles" value="{{ role.id }}">
                                {{ role.displayName }}
                            </option>
                        </select>
                    </td>
                    <td class="no-color" style="border: 0 solid black; background-color: white; padding-left: 5px">
                        <errors id="hasGlobalGroupAccessErrors" path="hasGlobalGroupAccess" cssClass="errors" />
                    </td>
                </tr>
                </security:authorize>
            </tbody>
        </table>
    </div>
    <%@ include file="/WEB-INF/views/modal/footer.jspf" %>
</script>
