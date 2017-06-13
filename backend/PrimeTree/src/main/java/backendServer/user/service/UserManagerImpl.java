package backendServer.user.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backendServer.clientDatabaseAccess.entities.EmployeeData;
import backendServer.exceptions.FavouriteAlreadyExistsException;
import backendServer.exceptions.FavouriteNotFoundException;
import backendServer.exceptions.UserHadAlreadyTheRightAdminStatusException;
import backendServer.exceptions.UserNotFoundException;
import backendServer.listings.SimpleMethods;
import backendServer.user.User;
import backendServer.userData.entities.UserData;

/**This class helps both ListingControllers to read and write data from and to the users by either creating 
 * user-Objects or by writing and reading directly to the tables in the databases. No write-access to 
 * employeeData is given.
 * @author Florian Kutz
 *
 */
@Service
@Transactional
public class UserManagerImpl extends MyUserDetailsServiceImpl implements UserManager {

	@Override
	public Long[] getFavouriteList(long userId) throws UserNotFoundException {
		UserData user=userDataRepository.findOne(userId);
		if(user==null){
			throw new UserNotFoundException();
		}else{
			List<Long> favouritesAsList=user.getFavouriteList();
			Long[] favouritesAsIntegerArray=new Long[favouritesAsList.size()];
			favouritesAsList.toArray(favouritesAsIntegerArray);
			return favouritesAsIntegerArray;
		}
	}

	@Override
	public User loadUserById(long userId) throws UserNotFoundException{
		EmployeeData employeeData=employeeDataRepository.findOne((long) userId);
		UserData userData=userDataRepository.findOne((long) userId);
		if(userData==null || employeeData==null){
			throw new UserNotFoundException("No user or employee with id " + userId);
		}
		return new User(employeeData, userData);
	}

	@Override
	public void addFavourite(long userId, long listingId) throws UserNotFoundException, FavouriteAlreadyExistsException {
		UserData userData=userDataRepository.findOne(userId);
		if(userData==null){
			throw new UserNotFoundException();
		}else if(userData.getFavouriteList().contains(listingId)){
			throw new FavouriteAlreadyExistsException();
		}
		userData.addFavourite(listingId);
		userDataRepository.save(userData);
	}

	@Override
	public void removeFavourite(long userId, long listingId) throws UserNotFoundException, FavouriteNotFoundException {
		UserData userData=userDataRepository.findOne(userId);
		if(userData==null){
			throw new UserNotFoundException();
		}else if(!userData.getFavouriteList().contains(listingId)){
			throw new FavouriteNotFoundException(userData.getFavouriteList().toString());
		}
		userData.removeFavourite(listingId);
		userDataRepository.save(userData);
	}

	@Override
	public User[] getAllUsers() {
		UserData[] allUserDatas=SimpleMethods.parseObjectArrayToUserDataArray(userDataRepository.findAll().toArray());
		EmployeeData[] allMatchingEmployeeDatas=new EmployeeData[allUserDatas.length];
		for(int index=0;index<allUserDatas.length;index++){
			allMatchingEmployeeDatas[index]=employeeDataRepository.findOne(allUserDatas[index].getId());
		}
		User[] allUsers=new User[allUserDatas.length];
		for(int index=0;index<allUserDatas.length;index++){
			allUsers[index]=new User(allMatchingEmployeeDatas[index], allUserDatas[index]);
		}
		return allUsers;
	}

	@Override
	public void setIsAdminOnUser(long userId, boolean shouldBeAdmin) throws UserNotFoundException, UserHadAlreadyTheRightAdminStatusException {
		UserData dataOfEditedUser=userDataRepository.findOne((long) userId);
		if(dataOfEditedUser==null){
			throw new UserNotFoundException();
		}else if(dataOfEditedUser.isInAdminRole()==shouldBeAdmin){
			throw new UserHadAlreadyTheRightAdminStatusException();
		}
		dataOfEditedUser.setInAdminRole(shouldBeAdmin);
		userDataRepository.save(dataOfEditedUser);
	}

}
