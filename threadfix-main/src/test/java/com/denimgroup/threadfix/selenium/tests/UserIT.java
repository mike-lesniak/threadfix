////////////////////////////////////////////////////////////////////////
//
//     Copyright (c) 2009-2014 Denim Group, Ltd.
//
//     The contents of this file are subject to the Mozilla Public License
//     Version 2.0 (the "License"); you may not use this file except in
//     compliance with the License. You may obtain a copy of the License at
//     http://www.mozilla.org/MPL/
//
//     Software distributed under the License is distributed on an "AS IS"
//     basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
//     License for the specific language governing rights and limitations
//     under the License.
//
//     The Original Code is ThreadFix.
//
//     The Initial Developer of the Original Code is Denim Group, Ltd.
//     Portions created by Denim Group, Ltd. are Copyright (C)
//     Denim Group, Ltd. All Rights Reserved.
//
//     Contributor(s): Denim Group, Ltd.
//
////////////////////////////////////////////////////////////////////////
package com.denimgroup.threadfix.selenium.tests;

import com.denimgroup.threadfix.CommunityTests;
import com.denimgroup.threadfix.selenium.pages.DashboardPage;
import com.denimgroup.threadfix.selenium.pages.UserChangePasswordPage;
import com.denimgroup.threadfix.selenium.pages.UserIndexPage;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Category(CommunityTests.class)
public class UserIT extends BaseIT {

	@Test
	public void testCreateUser() {
		String userName = "testCreateUser" + getRandomString(3);
        String password = "testCreateUser";
		UserIndexPage userIndexPage = loginPage.login("user", "password")
												.clickManageUsersLink();

		userIndexPage.clickAddUserLink()
                    .enterName(userName,null)
                    .enterPassword(password,null)
                    .enterConfirmPassword(password,null)
                    .clickAddNewUserBtn();
        assertTrue("User name was not present in the table.", userIndexPage.isUserNamePresent(userName));
		assertTrue("Success message was not displayed.", userIndexPage.isSuccessDisplayed(userName));
	}

    @Test
    public void testUserFieldValidation() {
        UserIndexPage userIndexPage = loginPage.login("user", "password")
                .clickManageUsersLink()
                .clickAddUserLink();

        userIndexPage.enterName("        ",null);
        userIndexPage.enterPassword("  ",null);
        userIndexPage.enterConfirmPassword("  ",null);

        userIndexPage = userIndexPage.clickAddNewUserBtnInvalid();
        sleep(5000);

        assertTrue("Name is required error was not present.",
                userIndexPage.getRequiredNameError().equals("Name is required."));
        assertTrue("Password is required error was not present.",
                userIndexPage.getPasswordRequiredError().equals("Password is required."));
        assertTrue("Confirm Password is required error was not present.",
                userIndexPage.getConfirmPasswordRequiredError().equals("Confirm Password is required."));

        // Test length
        userIndexPage.enterName("Test User",null);
        userIndexPage.enterPassword("test",null);
        userIndexPage.enterConfirmPassword("test",null);


        userIndexPage = userIndexPage.clickAddNewUserBtnInvalid();

        assertTrue("Password length error not present", userIndexPage.getPasswordLengthError().equals("8 characters needed"));

        // Test non-matching passwords
        userIndexPage.enterName("new name",null);
        userIndexPage.enterPassword("lengthy password 1",null);
        userIndexPage.enterConfirmPassword("lengthy password 2",null);
        userIndexPage = userIndexPage.clickAddNewUserBtnInvalid();
        assertTrue("Password matching error is not correct.", userIndexPage.getPasswordMatchError().equals("Passwords do not match."));
    }

    @Test
    public void testCreateDuplicateUser(){
        String userName = "testDuplicateUser" + getRandomString(3);
        // Create a user
        UserIndexPage userIndexPage = loginPage.login("user", "password")
                .clickManageUsersLink()
                .clickAddUserLink();
        userIndexPage.enterName(userName,null);
        userIndexPage.enterPassword(userName,null);
        userIndexPage.enterConfirmPassword(userName,null);

        userIndexPage = userIndexPage.clickAddNewUserBtn();

        assertTrue("User name was not present in the table.", userIndexPage.isUserNamePresent(userName));
        assertTrue("Success message was not displayed.", userIndexPage.isSuccessDisplayed(userName));

        DashboardPage dashboardPage = userIndexPage.logout()
                .login(userName, userName);

        assertTrue("user: "+userName+" was not logged in.",dashboardPage.isLoggedInUser(userName));

        userIndexPage = dashboardPage.logout()
                .login("user", "password")
                .clickManageUsersLink()
                .clickAddUserLink();
        // Test name uniqueness check

        userIndexPage.enterName(userName,null);
        userIndexPage.enterPassword("dummy password",null);
        userIndexPage.enterConfirmPassword("dummy password",null);

        userIndexPage = userIndexPage.clickAddNewUserBtnInvalid();
        sleep(5000);
        assertTrue("Name uniqueness error is not correct.", userIndexPage.getNameError().equals("That name is already taken."));
    }

	@Test
	public void testEditUserPasswordChange() {
		String userName = getRandomString(8);
        String password = getRandomString(15);
        String editedPassword = getRandomString(15);

		UserIndexPage userIndexPage = loginPage.login("user", "password")
                .clickManageUsersLink();

		assertFalse("User was already in the table.", userIndexPage.isUserNamePresent(userName));

        UserChangePasswordPage userChangePasswordPage = userIndexPage.clickAddUserLink()
				.enterName(userName)
				.enterPassword(password)
				.enterConfirmPassword(password)
				.clickAddNewUserBtn()
				.logout()
				.login(userName, password)
                .clickChangePasswordLink()
                .setCurrentPassword(password)
                .setNewPassword(editedPassword)
                .setConfirmPassword(editedPassword)
                .clickUpdate();


		DashboardPage dashboardPage = userChangePasswordPage.logout()
                .login(userName, editedPassword);

        assertTrue("Edited user could not login.", dashboardPage.isLoggedin());
	}

    @Test
    public void testEditUserAdministrator() {
        String userName = getRandomString(8);
        String editedUserName = getRandomString(8);
        String password = getRandomString(15);
        String editedPassword = getRandomString(15);

        UserIndexPage userIndexPage = loginPage.login("user", "password")
                .clickManageUsersLink()
                .clickAddUserLink()
                .enterName(userName)
                .enterPassword(password)
                .enterConfirmPassword(password)
                .clickAddNewUserBtn();

        DashboardPage dashboardPage= userIndexPage.logout()
                .login(userName, password);

        assertTrue("New user was not able to login.", dashboardPage.isLoggedin());

        userIndexPage = dashboardPage.logout()
                .login("user", "password")
                .clickManageUsersLink()
                .clickEditLink(userName)
                .enterName(editedUserName)
                .enterPassword(editedPassword)
                .enterConfirmPassword(editedPassword)
                .clickModalSubmit();

        dashboardPage = userIndexPage.logout()
                .login(editedUserName, editedPassword);

        assertTrue("Edited user was not able to login.", dashboardPage.isLoggedin());
    }

	@Test 
	public void testEditUserFieldValidation() {
		String baseUserName = "testEditUser" + getRandomString(3);
		String userNameDuplicateTest = "duplicate-user" + getRandomString(3);

		// Set up the two User objects for the test

		UserIndexPage userIndexPage = loginPage.login("user", "password")
                .clickManageUsersLink()
                .clickAddUserLink()
                .enterName(baseUserName)
                .enterPassword("lengthy password 2", null)
                .enterConfirmPassword("lengthy password 2", null)
                .clickAddNewUserBtn();

        userIndexPage = userIndexPage.clickOrganizationHeaderLink()
                .clickManageUsersLink()
                .clickAddUserLink()
                .enterName(userNameDuplicateTest)
                .enterPassword("lengthy password 2", null)
                .enterConfirmPassword("lengthy password 2", null)
                .clickAddNewUserBtn();

		// Test submission with no changes
		userIndexPage = userIndexPage.clickManageUsersLink()
                .clickEditLink(baseUserName)
                .clickUpdateUserBtn(baseUserName);
		assertTrue("User name was not present in the table.",userIndexPage.isUserNamePresent(baseUserName));

        userIndexPage = userIndexPage.clickManageUsersLink();

		// Test Empty
		userIndexPage = userIndexPage.clickEditLink(baseUserName)
                .enterName("")
                .enterPassword("", baseUserName)
                .enterConfirmPassword("", baseUserName)
                .clickUpdateUserBtnInvalid(baseUserName);

		assertTrue("Name error not present", userIndexPage.isSaveChangesButtonClickable(baseUserName));

        userIndexPage.clickManageUsersLink();
    }

    @Test
    public void testEditUserValidationWhiteSpace (){
        String userName = "userName"+ getRandomString(5);
        String passWord = "passWord"+ getRandomString(5);

        UserIndexPage userIndexPage = loginPage.login("user", "password")
                .clickManageUsersLink()
                .clickAddUserLink()
                .enterName(userName, null)
                .enterPassword(passWord, null)
                .enterConfirmPassword(passWord, null)
                .clickAddNewUserBtn();

		// Test White Space
		userIndexPage = userIndexPage.clickManageUsersLink()
                .clickAddUserLink()
                .enterName("        ", null)
                .enterPassword("             ", null)
                .enterConfirmPassword("             ", null)
                .clickAddNewUserBtn();

        sleep(5000);
		assertTrue("Name error not present", userIndexPage.getRequiredNameError().equals("Name is required."));
    }

    @Test
    public void testEditUserValidationNoMatching(){
        String userName = "userName"+ getRandomString(5);

        UserIndexPage userIndexPage = loginPage.login("user", "password").clickManageUsersLink();
		// Test non-matching passwords
		userIndexPage = userIndexPage.clickAddUserLink()
                .enterName(userName, null)
                .enterPassword("lengthy password 1", null)
                .enterConfirmPassword("lengthy password 2", null)
                .clickAddNewUserBtn();

        sleep(5000);
		assertTrue("Password matching error is not correct.", userIndexPage.getPasswordMatchError().equals("Passwords do not match."));

    }

    @Test
    public void testEditUserValidationLength(){

        UserIndexPage userIndexPage = loginPage.login("user", "password")
                .clickManageUsersLink();


		// Test length
		userIndexPage = userIndexPage.clickAddUserLink()
                .enterName("Test User", null)
                .enterPassword("test", null)
                .enterConfirmPassword("test", null)
                .clickAddNewUserBtn();

        sleep(5000);
		assertTrue("Password length error not present", userIndexPage.getPasswordLengthError().equals("8 characters needed"));

    }

    @Test
    public void testEditUserValidationUnique(){
        DashboardPage dashboardPage;
        String userName = "userName"+ getRandomString(5);
        String passWord = "passWord"+ getRandomString(5);

        UserIndexPage userIndexPage = loginPage.login("user", "password")
                .clickManageUsersLink()
                .clickAddUserLink()
                .enterName(userName, null)
                .enterPassword(passWord, null)
                .enterConfirmPassword(passWord, null)
                .clickAddNewUserBtn();
	
		// Test name uniqueness check
		userIndexPage = userIndexPage
				.clickManageUsersLink()
				.clickAddUserLink()
				.enterName(userName,null)
				.enterPassword("lengthy password 2",null)
				.enterConfirmPassword("lengthy password 2",null)
				.clickAddNewUserBtn();

        sleep(5000);
		assertTrue("Name uniqueness error is not correct.", userIndexPage.getNameError().equals("That name is already taken."));
		
	}

	@Test
	public void testNavigation() {
		loginPage.login("user", "password")
                 .clickManageUsersLink();
        assertTrue("Could not navigate to User Index Page.",driver.findElements(By.id("newUserModalLink")).size() != 0);
		}

	@Test
	public void testChangePasswordValidation() {
        UserChangePasswordPage changePasswordPage = loginPage.login("user", "password")
				.clickChangePasswordLink()
				.setCurrentPassword(" ")
				.setNewPassword("password1234")
				.setConfirmPassword("password1234")
				.clickUpdateInvalid();

        assertTrue("Password is required error was not present.",
                changePasswordPage.getPasswordRequiredError().equals("Password is required."));

		changePasswordPage = changePasswordPage.setCurrentPassword("password")
				.setNewPassword("                     ")
				.setConfirmPassword("password1234")
				.clickUpdateInvalid();

		assertTrue("Password match error not present",
                changePasswordPage.getErrorText("passwordMatchError").contains("Passwords do not match."));

		changePasswordPage = changePasswordPage.setCurrentPassword("password")
				.setConfirmPassword("                  ")
				.setNewPassword("password1234")
				.clickUpdateInvalid();

		assertTrue("Password match error not present",
				changePasswordPage.getErrorText("passwordMatchError").contains("Passwords do not match."));

		changePasswordPage = changePasswordPage.setCurrentPassword("password")
				.setConfirmPassword("      ")
				.setNewPassword("password124")
				.clickUpdateInvalid();

		assertTrue("Field required error missing",
				changePasswordPage.getErrorText("confirmRequiredError").contains("This field is required."));

        changePasswordPage.logout();
	}

    @Test
    public void testDeleteUser(){
        String userName = "testDeleteUser" + getRandomString(3);
        String password = "testDeleteUser";
        UserIndexPage userIndexPage = loginPage.login("user", "password")
                .clickManageUsersLink();

        userIndexPage.clickAddUserLink()
                .enterName(userName,null)
                .enterPassword(password,null)
                .enterConfirmPassword(password,null)
                .clickAddNewUserBtn()
                .clickEditLink(userName)
                .clickDelete(userName);
        assertTrue("Deletion Message not displayed.", userIndexPage.isSuccessDisplayed(userName));
        assertFalse("User still present in user table.", userIndexPage.isUserNamePresent(userName));
    }

	@Test
	public void testChangePassword() {
        String userName = "testChangePassword" + getRandomString(3);
        String password = "testChangePassword";
        String editedPassword = getRandomString(13);

		DashboardPage dashboardPage = loginPage.login("user", "password")
				.clickManageUsersLink()
				.clickAddUserLink()
				.enterName(userName, null)
				.enterPassword(password, null)
				.enterConfirmPassword(password, null)
				.clickAddNewUserBtn()
				.logout()
				.login(userName, password)
				.clickChangePasswordLink()
				.setConfirmPassword(editedPassword)
				.setNewPassword(editedPassword)
				.setCurrentPassword(password)
				.clickUpdate()
				.logout()
				.login(userName, editedPassword);

		assertTrue("Unable to login with new Password.", dashboardPage.isLoggedin());

	}
	
	@Test
	public void testPasswordChangeValidation(){
		String baseUserName = "passwordChangeValidation";
		DashboardPage dashboardPage = loginPage.login("user", "password")
				.clickManageUsersLink()
				.clickAddUserLink()
				.enterName(baseUserName,null)
				.enterPassword("lengthy password 2",null)
				.enterConfirmPassword("lengthy password 2",null)
				.clickAddNewUserBtn()
				.logout()
				.login(baseUserName, "lengthy password 2");
		
		assertTrue(baseUserName + "is not logged in",dashboardPage.isLoggedInUser(baseUserName));
		//wrong current password
		UserChangePasswordPage passwordChangePage = dashboardPage.clickChangePasswordLink()
                .setCurrentPassword("WRONGPASSWORD!!!!")
                .setNewPassword("lengthy password 3")
                .setConfirmPassword("lengthy password 3")
                .clickUpdateInvalid();
		
		assertTrue("Wrong current PW error not displayed",
				passwordChangePage.getErrorText("currentPasswordMismatchError").equals("That was not the correct password."));
		
		//blank new password
		passwordChangePage = passwordChangePage.setCurrentPassword("lengthy password 2")
                .setNewPassword(" ")
                .setConfirmPassword(" ")
                .clickUpdateInvalid();
		
		assertTrue("Blank new PW error not displayed",
				passwordChangePage.getErrorText("confirmRequiredError").equals("This field is required."));

		//different confirm and new passwords
		passwordChangePage = passwordChangePage.setCurrentPassword("lengthy password 2")
                .setNewPassword("lengthy password 3")
                .setConfirmPassword("lengthy password 34")
                .clickUpdateInvalid();
		
		assertTrue("Blank confirm PW error not displayed",
				passwordChangePage.getErrorText("passwordMatchError").equals("Passwords do not match."));

		//short password
		passwordChangePage = passwordChangePage.setCurrentPassword("lengthy password 2")
                .setNewPassword("password")
                .setConfirmPassword("password")
                .clickUpdateInvalid();
		
		assertTrue("Short PW error not displayed",
				passwordChangePage.getErrorText("charactersRequiredError").equals("4 characters needed"));

		//can you still change password
		passwordChangePage = passwordChangePage.setCurrentPassword("lengthy password 2")
                .setNewPassword("lengthy password 3")
                .setConfirmPassword("lengthy password 3")
                .clickUpdate();
		
		assertTrue("Password change did not save",passwordChangePage.isSaveSuccessful());

        //TODO FIGURE OUT HOW TO GET THE ERRORS FROM PAGE
		loginPage = passwordChangePage.logout().loginInvalid(baseUserName,"password");

		assertTrue("Incorrect password and no error message shown.",loginPage.isloginError());
		
		dashboardPage = loginPage.login(baseUserName,"lengthy password 3");
		
		assertTrue(baseUserName + "is not logged in",dashboardPage.isLoggedInUser(baseUserName));
		
		dashboardPage.logout()
					.login("user", "password")
					.clickManageUsersLink()
					.clickDeleteButton(baseUserName)
					.logout();

	}

}