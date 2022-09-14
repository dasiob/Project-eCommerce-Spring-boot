package com.example.vmo1.service.impl;

import com.example.vmo1.commons.exceptions.ResourceNotFoundException;
import com.example.vmo1.model.entity.Account;
import com.example.vmo1.model.entity.Role;
import com.example.vmo1.model.request.*;
import com.example.vmo1.model.response.AccountInforResponse;
import com.example.vmo1.model.response.AccountResponse;
import com.example.vmo1.model.response.MessageResponse;
import com.example.vmo1.repository.AccountRepository;
import com.example.vmo1.repository.RoleRepository;
import com.example.vmo1.security.service.CustomUserDetails;
import com.example.vmo1.validation.validator.EmailValidator;
import com.example.vmo1.validation.validator.PhoneValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceImplTest {

    @Mock
    private AccountRepository mockAccountRepository;
    @Mock
    private PasswordEncoder mockPasswordEncoder;
    @Mock
    private EmailValidator mockEmailValidator;
    @Mock
    private PhoneValidator mockPhoneValidator;
    @Mock
    private RoleRepository mockRoleRepository;

    @InjectMocks
    private AccountServiceImpl accountServiceImplUnderTest;

    @Test
    public void testAddAccountByAdmin() {
        // Setup
        final SignupRequest request = new SignupRequest("username", "fullname", "email", "password", "phone",
                new HashSet<>(Arrays.asList(
                        new Role(0, "name", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), false))));

        // Configure AccountRepository.findByEmail(...).
        final Account account1 = new Account();
        account1.setId(0L);
        account1.setFullname("fullname");
        account1.setUsername("username");
        account1.setPassword("password");
        account1.setEmail("email");
        account1.setPhone("phone");
        account1.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account1.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account1.setIs_deleted(false);
        account1.setEnable(false);
        final Role role = new Role();
        role.setId(0);
        role.setName("name");
        role.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        role.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account1.setRoles(new HashSet<>(Arrays.asList(role)));
        final Optional<Account> account = Optional.of(account1);
        when(mockAccountRepository.findByEmail("email")).thenReturn(account);

        // Configure AccountRepository.findByUsername(...).
        final Account account3 = new Account();
        account3.setId(0L);
        account3.setFullname("fullname");
        account3.setUsername("username");
        account3.setPassword("password");
        account3.setEmail("email");
        account3.setPhone("phone");
        account3.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account3.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account3.setIs_deleted(false);
        account3.setEnable(false);
        final Role role1 = new Role();
        role1.setId(0);
        role1.setName("name");
        role1.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        role1.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account3.setRoles(new HashSet<>(Arrays.asList(role1)));
        final Optional<Account> account2 = Optional.of(account3);
        when(mockAccountRepository.findByUsername("username")).thenReturn(account2);

        when(mockEmailValidator.test("email")).thenReturn(false);
        when(mockPhoneValidator.test("phone")).thenReturn(false);
        when(mockPasswordEncoder.encode("password")).thenReturn("password");

        // Configure RoleRepository.findByName(...).
        final Optional<Role> role2 = Optional.of(
                new Role(0, "name", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), false));
        when(mockRoleRepository.findByName("ROLE_USER")).thenReturn(role2);

        // Configure AccountRepository.save(...).
        final Account account4 = new Account();
        account4.setId(0L);
        account4.setFullname("fullname");
        account4.setUsername("username");
        account4.setPassword("password");
        account4.setEmail("email");
        account4.setPhone("phone");
        account4.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account4.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account4.setIs_deleted(false);
        account4.setEnable(false);
        final Role role3 = new Role();
        role3.setId(0);
        role3.setName("name");
        role3.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        role3.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account4.setRoles(new HashSet<>(Arrays.asList(role3)));
        when(mockAccountRepository.save(any(Account.class))).thenReturn(account4);

        // Run the test
        final MessageResponse result = accountServiceImplUnderTest.addAccountByAdmin(request);

        // Verify the results
        verify(mockAccountRepository).save(any(Account.class));
    }

    @Test
    public void testAddAccountByAdmin_AccountRepositoryFindByEmailReturnsAbsent() {
        // Setup
        final SignupRequest request = new SignupRequest("username", "fullname", "email", "password", "phone",
                new HashSet<>(Arrays.asList(
                        new Role(0, "name", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), false))));
        when(mockAccountRepository.findByEmail("email")).thenReturn(Optional.empty());

        // Configure AccountRepository.findByUsername(...).
        final Account account1 = new Account();
        account1.setId(0L);
        account1.setFullname("fullname");
        account1.setUsername("username");
        account1.setPassword("password");
        account1.setEmail("email");
        account1.setPhone("phone");
        account1.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account1.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account1.setIs_deleted(false);
        account1.setEnable(false);
        final Role role = new Role();
        role.setId(0);
        role.setName("name");
        role.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        role.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account1.setRoles(new HashSet<>(Arrays.asList(role)));
        final Optional<Account> account = Optional.of(account1);
        when(mockAccountRepository.findByUsername("username")).thenReturn(account);

        when(mockEmailValidator.test("email")).thenReturn(false);
        when(mockPhoneValidator.test("phone")).thenReturn(false);
        when(mockPasswordEncoder.encode("password")).thenReturn("password");

        // Configure RoleRepository.findByName(...).
        final Optional<Role> role1 = Optional.of(
                new Role(0, "name", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), false));
        when(mockRoleRepository.findByName("ROLE_USER")).thenReturn(role1);

        // Configure AccountRepository.save(...).
        final Account account2 = new Account();
        account2.setId(0L);
        account2.setFullname("fullname");
        account2.setUsername("username");
        account2.setPassword("password");
        account2.setEmail("email");
        account2.setPhone("phone");
        account2.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account2.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account2.setIs_deleted(false);
        account2.setEnable(false);
        final Role role2 = new Role();
        role2.setId(0);
        role2.setName("name");
        role2.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        role2.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account2.setRoles(new HashSet<>(Arrays.asList(role2)));
        when(mockAccountRepository.save(any(Account.class))).thenReturn(account2);

        // Run the test
        final MessageResponse result = accountServiceImplUnderTest.addAccountByAdmin(request);

        // Verify the results
        verify(mockAccountRepository).save(any(Account.class));
    }

    @Test
    public void testAddAccountByAdmin_AccountRepositoryFindByUsernameReturnsAbsent() {
        // Setup
        final SignupRequest request = new SignupRequest("username", "fullname", "email", "password", "phone",
                new HashSet<>(Arrays.asList(
                        new Role(0, "name", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), false))));

        // Configure AccountRepository.findByEmail(...).
        final Account account1 = new Account();
        account1.setId(0L);
        account1.setFullname("fullname");
        account1.setUsername("username");
        account1.setPassword("password");
        account1.setEmail("email");
        account1.setPhone("phone");
        account1.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account1.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account1.setIs_deleted(false);
        account1.setEnable(false);
        final Role role = new Role();
        role.setId(0);
        role.setName("name");
        role.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        role.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account1.setRoles(new HashSet<>(Arrays.asList(role)));
        final Optional<Account> account = Optional.of(account1);
        when(mockAccountRepository.findByEmail("email")).thenReturn(account);

        when(mockAccountRepository.findByUsername("username")).thenReturn(Optional.empty());
        when(mockEmailValidator.test("email")).thenReturn(false);
        when(mockPhoneValidator.test("phone")).thenReturn(false);
        when(mockPasswordEncoder.encode("password")).thenReturn("password");

        // Configure RoleRepository.findByName(...).
        final Optional<Role> role1 = Optional.of(
                new Role(0, "name", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), false));
        when(mockRoleRepository.findByName("ROLE_USER")).thenReturn(role1);

        // Configure AccountRepository.save(...).
        final Account account2 = new Account();
        account2.setId(0L);
        account2.setFullname("fullname");
        account2.setUsername("username");
        account2.setPassword("password");
        account2.setEmail("email");
        account2.setPhone("phone");
        account2.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account2.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account2.setIs_deleted(false);
        account2.setEnable(false);
        final Role role2 = new Role();
        role2.setId(0);
        role2.setName("name");
        role2.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        role2.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account2.setRoles(new HashSet<>(Arrays.asList(role2)));
        when(mockAccountRepository.save(any(Account.class))).thenReturn(account2);

        // Run the test
        final MessageResponse result = accountServiceImplUnderTest.addAccountByAdmin(request);

        // Verify the results
        verify(mockAccountRepository).save(any(Account.class));
    }

    @Test
    public void testAddAccountByAdmin_RoleRepositoryReturnsAbsent() {
        // Setup
        final SignupRequest request = new SignupRequest("username", "fullname", "email", "password", "phone",
                new HashSet<>(Arrays.asList(
                        new Role(0, "name", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), false))));

        // Configure AccountRepository.findByEmail(...).
        final Account account1 = new Account();
        account1.setId(0L);
        account1.setFullname("fullname");
        account1.setUsername("username");
        account1.setPassword("password");
        account1.setEmail("email");
        account1.setPhone("phone");
        account1.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account1.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account1.setIs_deleted(false);
        account1.setEnable(false);
        final Role role = new Role();
        role.setId(0);
        role.setName("name");
        role.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        role.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account1.setRoles(new HashSet<>(Arrays.asList(role)));
        final Optional<Account> account = Optional.of(account1);
        when(mockAccountRepository.findByEmail("email")).thenReturn(account);

        // Configure AccountRepository.findByUsername(...).
        final Account account3 = new Account();
        account3.setId(0L);
        account3.setFullname("fullname");
        account3.setUsername("username");
        account3.setPassword("password");
        account3.setEmail("email");
        account3.setPhone("phone");
        account3.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account3.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account3.setIs_deleted(false);
        account3.setEnable(false);
        final Role role1 = new Role();
        role1.setId(0);
        role1.setName("name");
        role1.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        role1.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account3.setRoles(new HashSet<>(Arrays.asList(role1)));
        final Optional<Account> account2 = Optional.of(account3);
        when(mockAccountRepository.findByUsername("username")).thenReturn(account2);

        when(mockEmailValidator.test("email")).thenReturn(false);
        when(mockPhoneValidator.test("phone")).thenReturn(false);
        when(mockPasswordEncoder.encode("password")).thenReturn("password");
        when(mockRoleRepository.findByName("ROLE_USER")).thenReturn(Optional.empty());

        // Configure AccountRepository.save(...).
        final Account account4 = new Account();
        account4.setId(0L);
        account4.setFullname("fullname");
        account4.setUsername("username");
        account4.setPassword("password");
        account4.setEmail("email");
        account4.setPhone("phone");
        account4.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account4.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account4.setIs_deleted(false);
        account4.setEnable(false);
        final Role role2 = new Role();
        role2.setId(0);
        role2.setName("name");
        role2.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        role2.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account4.setRoles(new HashSet<>(Arrays.asList(role2)));
        when(mockAccountRepository.save(any(Account.class))).thenReturn(account4);

        // Run the test
        final MessageResponse result = accountServiceImplUnderTest.addAccountByAdmin(request);

        // Verify the results
        verify(mockAccountRepository).save(any(Account.class));
    }

    @Test
    public void testUpdatePassword() {
        // Setup
        final CustomUserDetails customUserDetails = new CustomUserDetails(0L, "username", "email", "password",
                Arrays.asList());
        final UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest();
        updatePasswordRequest.setOldPassword("oldPassword");
        updatePasswordRequest.setNewPassword("newPassword");

        // Configure AccountRepository.findByEmail(...).
        final Account account1 = new Account();
        account1.setId(0L);
        account1.setFullname("fullname");
        account1.setUsername("username");
        account1.setPassword("password");
        account1.setEmail("email");
        account1.setPhone("phone");
        account1.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account1.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account1.setIs_deleted(false);
        account1.setEnable(false);
        final Role role = new Role();
        role.setId(0);
        role.setName("name");
        role.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        role.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account1.setRoles(new HashSet<>(Arrays.asList(role)));
        final Optional<Account> account = Optional.of(account1);
        when(mockAccountRepository.findByEmail("email")).thenReturn(account);

        when(mockPasswordEncoder.matches("oldPassword", "password")).thenReturn(false);
        when(mockPasswordEncoder.encode("newPassword")).thenReturn("password");

        // Configure AccountRepository.save(...).
        final Account account2 = new Account();
        account2.setId(0L);
        account2.setFullname("fullname");
        account2.setUsername("username");
        account2.setPassword("password");
        account2.setEmail("email");
        account2.setPhone("phone");
        account2.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account2.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account2.setIs_deleted(false);
        account2.setEnable(false);
        final Role role1 = new Role();
        role1.setId(0);
        role1.setName("name");
        role1.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        role1.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account2.setRoles(new HashSet<>(Arrays.asList(role1)));
        when(mockAccountRepository.save(any(Account.class))).thenReturn(account2);

        // Run the test
        final MessageResponse result = accountServiceImplUnderTest.updatePassword(customUserDetails,
                updatePasswordRequest);

        // Verify the results
        verify(mockAccountRepository).save(any(Account.class));
    }

    @Test
    public void testUpdatePassword_AccountRepositoryFindByEmailReturnsAbsent() {
        // Setup
        final CustomUserDetails customUserDetails = new CustomUserDetails(0L, "username", "email", "password",
                Arrays.asList());
        final UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest();
        updatePasswordRequest.setOldPassword("oldPassword");
        updatePasswordRequest.setNewPassword("newPassword");

        when(mockAccountRepository.findByEmail("email")).thenReturn(Optional.empty());

        // Run the test
        assertThrows(NoSuchElementException.class,
                () -> accountServiceImplUnderTest.updatePassword(customUserDetails, updatePasswordRequest));
    }

    @Test
    public void testGetAllAccount() {
        // Setup
        final AccountResponse expectedResult = new AccountResponse(Arrays.asList(
                new AccountDto(0L, "username", "fullname", "email", "phone", false,
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), Arrays.asList(
                        new Role(0, "name", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), false)))), 0, 0, 0L, 0,
                false);

        // Configure AccountRepository.findByRoles_name(...).
        final Account account = new Account();
        account.setId(0L);
        account.setFullname("fullname");
        account.setUsername("username");
        account.setPassword("password");
        account.setEmail("email");
        account.setPhone("phone");
        account.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account.setIs_deleted(false);
        account.setEnable(false);
        final Role role = new Role();
        role.setId(0);
        role.setName("name");
        role.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        role.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account.setRoles(new HashSet<>(Arrays.asList(role)));
        final Page<Account> accounts = new PageImpl<>(Arrays.asList(account));
        when(mockAccountRepository.findByRoles_name(eq("name"), any(Pageable.class))).thenReturn(accounts);

        // Run the test
        final AccountResponse result = accountServiceImplUnderTest.getAllAccount("name", 0, 0);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetAllAccount_AccountRepositoryReturnsNoItems() {
        // Setup
        final AccountResponse expectedResult = new AccountResponse(Arrays.asList(
                new AccountDto(0L, "username", "fullname", "email", "phone", false,
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), Arrays.asList(
                        new Role(0, "name", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), false)))), 0, 0, 0L, 0,
                false);
        when(mockAccountRepository.findByRoles_name(eq("name"), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        // Run the test
        final AccountResponse result = accountServiceImplUnderTest.getAllAccount("name", 0, 0);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testUpdateProfile() {
        // Setup
        final CustomUserDetails customUserDetails = new CustomUserDetails(0L, "username", "email", "password",
                Arrays.asList());
        final UpdateAccountRequest request = new UpdateAccountRequest();
        request.setFullname("fullname");
        request.setPhone("phone");

        final AccountInforResponse expectedResult = new AccountInforResponse("fullname", "username", "email", "phone",
                false, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());

        // Configure AccountRepository.findByEmail(...).
        final Account account1 = new Account();
        account1.setId(0L);
        account1.setFullname("fullname");
        account1.setUsername("username");
        account1.setPassword("password");
        account1.setEmail("email");
        account1.setPhone("phone");
        account1.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account1.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account1.setIs_deleted(false);
        account1.setEnable(false);
        final Role role = new Role();
        role.setId(0);
        role.setName("name");
        role.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        role.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account1.setRoles(new HashSet<>(Arrays.asList(role)));
        final Optional<Account> account = Optional.of(account1);
        when(mockAccountRepository.findByEmail("email")).thenReturn(account);

        when(mockPhoneValidator.test("phone")).thenReturn(false);

        // Configure AccountRepository.save(...).
        final Account account2 = new Account();
        account2.setId(0L);
        account2.setFullname("fullname");
        account2.setUsername("username");
        account2.setPassword("password");
        account2.setEmail("email");
        account2.setPhone("phone");
        account2.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account2.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account2.setIs_deleted(false);
        account2.setEnable(false);
        final Role role1 = new Role();
        role1.setId(0);
        role1.setName("name");
        role1.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        role1.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account2.setRoles(new HashSet<>(Arrays.asList(role1)));
        when(mockAccountRepository.save(any(Account.class))).thenReturn(account2);

        // Run the test
        final AccountInforResponse result = accountServiceImplUnderTest.updateProfile(customUserDetails, request);

        // Verify the results
        assertEquals(expectedResult, result);
        verify(mockAccountRepository).save(any(Account.class));
    }

    @Test
    public void testUpdateProfile_AccountRepositoryFindByEmailReturnsAbsent() {
        // Setup
        final CustomUserDetails customUserDetails = new CustomUserDetails(0L, "username", "email", "password",
                Arrays.asList());
        final UpdateAccountRequest request = new UpdateAccountRequest();
        request.setFullname("fullname");
        request.setPhone("phone");

        when(mockAccountRepository.findByEmail("email")).thenReturn(Optional.empty());

        // Run the test
        assertThrows(NoSuchElementException.class,
                () -> accountServiceImplUnderTest.updateProfile(customUserDetails, request));
    }

    @Test
    public void testUpdateAccountByAdmin() {
        // Setup
        final UpdateAccountByAdminRequest request = new UpdateAccountByAdminRequest("fullname", "username", "email",
                "password", "phone", false);
        final AccountInforResponse expectedResult = new AccountInforResponse("fullname", "username", "email", "phone",
                false, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());

        // Configure AccountRepository.findById(...).
        final Account account1 = new Account();
        account1.setId(0L);
        account1.setFullname("fullname");
        account1.setUsername("username");
        account1.setPassword("password");
        account1.setEmail("email");
        account1.setPhone("phone");
        account1.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account1.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account1.setIs_deleted(false);
        account1.setEnable(false);
        final Role role = new Role();
        role.setId(0);
        role.setName("name");
        role.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        role.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account1.setRoles(new HashSet<>(Arrays.asList(role)));
        final Optional<Account> account = Optional.of(account1);
        when(mockAccountRepository.findById(0L)).thenReturn(account);

        when(mockPasswordEncoder.encode("password")).thenReturn("password");
        when(mockPhoneValidator.test("phone")).thenReturn(false);

        // Configure AccountRepository.save(...).
        final Account account2 = new Account();
        account2.setId(0L);
        account2.setFullname("fullname");
        account2.setUsername("username");
        account2.setPassword("password");
        account2.setEmail("email");
        account2.setPhone("phone");
        account2.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account2.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account2.setIs_deleted(false);
        account2.setEnable(false);
        final Role role1 = new Role();
        role1.setId(0);
        role1.setName("name");
        role1.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        role1.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account2.setRoles(new HashSet<>(Arrays.asList(role1)));
        when(mockAccountRepository.save(any(Account.class))).thenReturn(account2);

        // Run the test
        final AccountInforResponse result = accountServiceImplUnderTest.updateAccountByAdmin(request, 0L);

        // Verify the results
        assertEquals(expectedResult, result);
        verify(mockAccountRepository).save(any(Account.class));
    }

    @Test
    public void testUpdateAccountByAdmin_AccountRepositoryFindByIdReturnsAbsent() {
        // Setup
        final UpdateAccountByAdminRequest request = new UpdateAccountByAdminRequest("fullname", "username", "email",
                "password", "phone", false);
        when(mockAccountRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThrows(ResourceNotFoundException.class,
                () -> accountServiceImplUnderTest.updateAccountByAdmin(request, 0L));
    }

    @Test
    public void testDeleteAccountByAdmin() {
        // Setup
        // Configure AccountRepository.findById(...).
        final Account account1 = new Account();
        account1.setId(0L);
        account1.setFullname("fullname");
        account1.setUsername("username");
        account1.setPassword("password");
        account1.setEmail("email");
        account1.setPhone("phone");
        account1.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account1.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account1.setIs_deleted(false);
        account1.setEnable(false);
        final Role role = new Role();
        role.setId(0);
        role.setName("name");
        role.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        role.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account1.setRoles(new HashSet<>(Arrays.asList(role)));
        final Optional<Account> account = Optional.of(account1);
        when(mockAccountRepository.findById(0L)).thenReturn(account);

        // Configure AccountRepository.save(...).
        final Account account2 = new Account();
        account2.setId(0L);
        account2.setFullname("fullname");
        account2.setUsername("username");
        account2.setPassword("password");
        account2.setEmail("email");
        account2.setPhone("phone");
        account2.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account2.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account2.setIs_deleted(false);
        account2.setEnable(false);
        final Role role1 = new Role();
        role1.setId(0);
        role1.setName("name");
        role1.setCreated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        role1.setUpdated_at(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        account2.setRoles(new HashSet<>(Arrays.asList(role1)));
        when(mockAccountRepository.save(any(Account.class))).thenReturn(account2);

        // Run the test
        accountServiceImplUnderTest.deleteAccountByAdmin(0L);

        // Verify the results
        verify(mockAccountRepository).save(any(Account.class));
    }

    @Test
    public void testDeleteAccountByAdmin_AccountRepositoryFindByIdReturnsAbsent() {
        // Setup
        when(mockAccountRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThrows(ResourceNotFoundException.class, () -> accountServiceImplUnderTest.deleteAccountByAdmin(0L));
    }
}
