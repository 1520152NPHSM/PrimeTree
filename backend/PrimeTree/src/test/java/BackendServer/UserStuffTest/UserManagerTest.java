package BackendServer.UserStuffTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import BackendServer.Exceptions.FavouriteAlreadyExistsException;
import BackendServer.Exceptions.FavouriteNotFoundException;
import BackendServer.Exceptions.UserHadAlreadyTheRightAdminStatusException;
import BackendServer.Exceptions.UserNotFoundException;
import BackendServer.User.User;
import BackendServer.User.Service.UserManager;
import BackendServer.UserData.Repositories.UserDataRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={BackendServer.UserData.Configuration.UserBeanCollection.class,
BackendServer.Listings.ListingBeanCollection.class, BackendServer.ClientDatabaseAccess.Config.EmployeeBeanCollection.class})
public class UserManagerTest {
	
	@Autowired
	UserManager userManager;
	@Autowired
	UserDataRepository userDataRepositoy;
	User mmustermann,
	akessler;
	
	@Before
	public void initTests() throws UserNotFoundException, FavouriteAlreadyExistsException{
		mmustermann=userManager.loadUserByUsername("mmustermann");
		akessler=userManager.loadUserByUsername("akessler");
		userManager.addFavourite(akessler.getId(), 0);
	}
	
	@After
	public void resetDatabase(){
		userDataRepositoy.deleteAll();
	}
	
	@Test
	public void getAllUsersTest(){
		User[] resultSet=userManager.getAllUsers();
		assertEquals(2,resultSet.length);
	}
	
	@Test
	public void loadUserByIdWithValidUserIdTest() throws UserNotFoundException{
		User loadedUser=userManager.loadUserById(mmustermann.getId());
		assertEquals(loadedUser.getAuthorities(),mmustermann.getAuthorities());
		assertEquals(loadedUser.getFirstName(),mmustermann.getFirstName());
		assertEquals(loadedUser.getId(),mmustermann.getId());
		assertEquals(loadedUser.getPassword(),mmustermann.getPassword());
		assertEquals(loadedUser.getPicture(),mmustermann.getPicture());
		assertEquals(loadedUser.getUsername(),mmustermann.getUsername());
		assertEquals(loadedUser.isAccountNonExpired(),mmustermann.isAccountNonExpired());
		assertEquals(loadedUser.isAccountNonLocked(),mmustermann.isAccountNonLocked());
		assertEquals(loadedUser.isAdmin(),mmustermann.isAdmin());
		assertEquals(loadedUser.isCredentialsNonExpired(),mmustermann.isCredentialsNonExpired());
		assertEquals(loadedUser.isEnabled(),mmustermann.isEnabled());
		
	}
	
	@Test(expected=UserNotFoundException.class)
	public void loadUserByIdWithUserIdExistingOnlyInEmployeeDataTest() throws UserNotFoundException{
		userManager.loadUserById(3);
	}
	
	@Test(expected=UserNotFoundException.class)
	public void loadUserByIdWithNotExistingUserIdTest() throws UserNotFoundException{
		userManager.loadUserById(4);
	}
	
	
	@Test
	public void setIsAdminOnUserWithCorrectInputsToTrueAndThenBackToFalseTest() throws UserNotFoundException, UserHadAlreadyTheRightAdminStatusException{
		assertFalse(mmustermann.isAdmin());
		userManager.setIsAdminOnUser(mmustermann.getId(), true);
		mmustermann=userManager.loadUserByUsername("mmustermann");
		assertTrue(mmustermann.isAdmin());
		userManager.setIsAdminOnUser(mmustermann.getId(), false);
		mmustermann=userManager.loadUserByUsername("mmustermann");
		assertFalse(mmustermann.isAdmin());
	}
	
	@Test(expected=UserNotFoundException.class)
	public void setIsAdminOnUserWithUserIdExistingOnlyInEmployeeDataTest() throws UserNotFoundException, UserHadAlreadyTheRightAdminStatusException{
		userManager.setIsAdminOnUser(3, true);
	}
	
	@Test(expected=UserNotFoundException.class)
	public void setIsAdminOnUserWithNotExistingUserIdTest() throws UserNotFoundException, UserHadAlreadyTheRightAdminStatusException{
		userManager.setIsAdminOnUser(4, true);
	}
	
	@Test(expected=UserHadAlreadyTheRightAdminStatusException.class)
	public void setIsAdminOnUserWithFalseWhichIsTheIsAdminBooleanTheUserAlreadyHadTest() throws UserNotFoundException, UserHadAlreadyTheRightAdminStatusException{
		userManager.setIsAdminOnUser(mmustermann.getId(), false);
	}
	
	
	@Test
	public void getFvouriteListWithEmptyFavouriteListTest() throws UserNotFoundException{
		assertEquals(0,userManager.getFavouriteList(mmustermann.getId()).length);
	}
	
	@Test
	public void getFvouriteListWithFilledFavouriteListTest() throws UserNotFoundException{
		Long[] favouriteList=userManager.getFavouriteList(akessler.getId());
		assertEquals(1,favouriteList.length);
		assertEquals(0,(int)(long)favouriteList[0]);
	}
	
	@Test(expected=UserNotFoundException.class)
	public void getFvouriteListWithUserIdExistingOnlyInEmployeeDataTest() throws UserNotFoundException{
		userManager.getFavouriteList(3);
	}
	
	@Test(expected=UserNotFoundException.class)
	public void getFvouriteListWithNotExistingUserIdTest() throws UserNotFoundException{
		userManager.getFavouriteList(4);
	}
	
	
	@Test
	public void addFavouriteWithCorrecctInputTest() throws UserNotFoundException, FavouriteAlreadyExistsException{
		Long[] favouriteList=userManager.getFavouriteList(akessler.getId());
		assertEquals(1,favouriteList.length);
		assertEquals(0,(int)(long)favouriteList[0]);
		userManager.addFavourite(akessler.getId(), 3);
		favouriteList=userManager.getFavouriteList(akessler.getId());
		assertEquals(2,favouriteList.length);
		assertEquals(0,(int)(long)favouriteList[0]);
		assertEquals(3,(int)(long)favouriteList[1]);
	}
	
	@Test(expected=UserNotFoundException.class)
	public void addFavouriteWithUserIdExistingOnlyInEmployeeDataTest() throws UserNotFoundException, FavouriteAlreadyExistsException{
		userManager.addFavourite(3, 0);
	}
	
	@Test(expected=UserNotFoundException.class)
	public void addFavouriteWithNotExistingUserIdTest() throws UserNotFoundException, FavouriteAlreadyExistsException{
		userManager.addFavourite(4, 0);
	}
	
	@Test(expected=FavouriteAlreadyExistsException.class)
	public void addFavouriteWithAlreadyExistingListingIdTest() throws UserNotFoundException, FavouriteAlreadyExistsException{
		userManager.addFavourite(akessler.getId(), 0);
	}
	
	
	@Test
	public void removeFavouriteWithCorrectInputTest() throws UserNotFoundException, FavouriteNotFoundException{
		Long[] favouriteList=userManager.getFavouriteList(akessler.getId());
		assertEquals(1,favouriteList.length);
		assertEquals(0,(int)(long)favouriteList[0]);
		userManager.removeFavourite(akessler.getId(), 0);
		favouriteList=userManager.getFavouriteList(akessler.getId());
		assertEquals(0,favouriteList.length);
	}
	
	@Test(expected=UserNotFoundException.class)
	public void removeFavouriteWithUserIdExistingOnlyInEmployeeDataTest() throws UserNotFoundException, FavouriteNotFoundException{
		userManager.removeFavourite(3, 0);
	}
	
	@Test(expected=UserNotFoundException.class)
	public void removeFavouriteWithNotExistingUserIdTest() throws UserNotFoundException, FavouriteNotFoundException{
		userManager.removeFavourite(4, 0);
	}
	
	@Test(expected=FavouriteNotFoundException.class)
	public void removeFavouriteWithNotExistingListingIdTest() throws UserNotFoundException, FavouriteNotFoundException{
		userManager.removeFavourite(akessler.getId(), 1);
	}

}
