package org.mcsg.bot.skype.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Arguments {

	private HashMap<String, String> values = new HashMap<>();
	private String[] args;

	public Arguments(String input[], String ... args){
		ArrayList<String> list = new ArrayList<>(Arrays.asList(input));

		for(String arg : args){
			String split[] = arg.split(" ");
			List<String> swi = Arrays.asList(split[0].split("/"));

			System.out.println();

			System.out.println("split (search)"+Arrays.toString(split));
			System.out.println("Args" +list.toString());
			int index = -1;
			int a = 0;
			for(String str : list){
				System.out.println(a +" "+ str);
				if(swi.contains(str.replace("-", ""))){
					index = a;
					System.out.println("Found at "+a);
					break;
				}
				a++;
			}

			if(index != -1){
				if(split.length == 1){
					values.put(swi.get(0), null);
					list.remove(index);
				} else {
					list.remove(index);
					values.put(swi.get(0), list.remove(index));
				}
			}
		}
		
		System.out.println(list);
		this.args = list.toArray(new String[0]);
		System.out.println(Arrays.toString(args));
	}

	public HashMap<String, String> getValues(){
		return values;
	}

	public String[] getArgs(){
		return args;
	}


}
