package com.learning.mono_basics.chapters;

import java.util.Optional;

import com.learning.mono_basics.utils.ThreadUtils;

//orElse is eager -> will get created even when data is present in optional
//orlElseget is lzay -> will create obect only wen data is empty in optional
public class OptionalOrElseLaziness {

	public static void main(String[] args) {
		//Optional<String> data = Optional.of("sita ram");
		Optional<String> data = Optional.ofNullable(null);
		
		
		
		//even though data is not empty it still calls method and create object for empty case
		//eager even if data is present or epty always try to cal the empty case
		//System.out.println("data found "+data.orElse(getDefault()));
		
		
		//lazy , only calls empty scneario when ptional data isa ctually empty otherwise not
		System.out.println("data found "+data.orElseGet(OptionalOrElseLaziness :: getDefault));
	}
	
	public static String getDefault() {
		System.out.println("getDefault called");
		//lets say taking default value is time consuming
		ThreadUtils.sleep(2000);
		return "lekshmi narayan";
	}
}
