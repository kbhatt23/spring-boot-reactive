package com.learning.mono_flux_advanced.sinks.challenges;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

public class UserRateService {

	private static final Map<String, UserRateData> USER_DATA;
	
	public static final String USERNAME = "username";
	static {
		USER_DATA = new HashMap<>();

		USER_DATA.put("kanishk", new UserRateData(UserType.PREMIUM, 0));
		
		USER_DATA.put("khokhla", new UserRateData(UserType.STANDARD, 0));
	}

	// assume we always get into this method if user is correct
	public static void increaseCount(String userName) {
		if (UserRateService.userExists(userName)) {
			System.out.println("increasing count for "+userName);

			UserRateData userRateData = USER_DATA.get(userName);
			userRateData.setCount(userRateData.getCount() + 1);
		}

	}

	public static boolean userExists(String userName) {
		return USER_DATA.containsKey(userName);

	}
	
	public static boolean isUserCountWithinLimit(String userName) {
		if(userExists(userName)) {
			
			UserRateData userRateData = USER_DATA.get(userName);
			int maxCount = userRateData.getUserType().getMaxCount();
			int currentCount = userRateData.getCount();
			
			return maxCount > currentCount;
		}
		
		
		return false;
	}

}

@Data
class UserRateData {
	private UserType userType;

	private int count;

	public UserRateData(UserType userType, int count) {
		super();
		this.userType = userType;
		this.count = count;
	}

}

//for premium we can call 10 times
//for standard 5 times
enum UserType {

	PREMIUM {
		@Override
		public int getMaxCount() {
			return 4;
		}
	}, STANDARD {
		@Override
		public int getMaxCount() {
			return 2;
		}
	};
	
	public abstract int getMaxCount();

}
