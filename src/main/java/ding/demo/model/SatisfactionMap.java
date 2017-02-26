package ding.demo.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

public class SatisfactionMap {
	private TreeMap<PaintType,List<Integer>> map = new TreeMap<PaintType,List<Integer>>();
	public void put(PaintType paint, List<Integer> customerIds) {
		if(map.containsKey(paint))
			map.get(paint).addAll(customerIds);
		else
			map.put(paint, customerIds);
	} 
	
	public void put(PaintType paint, Integer customerId) {
		if(map.containsKey(paint))
			map.get(paint).add(customerId);
		else 
			map.put(paint, new ArrayList<Integer>(Arrays.asList(customerId)));
	} 
	
	public boolean isPaintTypeLocked(PaintType paint) {
		return paint.isMatte() && map.containsKey(paint);
	}
	
	public List<Integer> listCustomerIdsWhoIsSatisfiedByCurrentPaintType(PaintType paint){
		List<Integer> result = map.get(paint);
		if(result == null)
			result = new ArrayList<Integer>();
		
		return result;
	}
	
	public void removeCustomerIdsSatisfiedByCurrentPaintType(PaintType paint){
		map.remove(paint);
	}
	
	public String generateResultString(){
		StringBuffer sb = new StringBuffer();
		for(PaintType p: map.keySet()){
			sb.append( " " + p.getColorType().getValue());
		}
		return sb.toString();
	}
}
