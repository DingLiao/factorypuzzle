package ding.demo.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import ding.demo.model.Customer;
import ding.demo.model.ImpossibleException;
import ding.demo.model.PaintType;
import ding.demo.model.SatisfactionMap;
import ding.demo.model.TestCase;

public class PaintBatchManager {
	private int testCaseId = 0;
	private List<PaintType> allGlossyPaintTypes;
	private SatisfactionMap satisfactionMap;
	private HashMap<Integer, Customer> customerMap;
	private List<String> solutions = new ArrayList<String>();

	public static PaintBatchManager getInstance() {
		return new PaintBatchManager();
	}
	
	public void analyzeTestCase(TestCase testCase){
		System.out.println("******** analyzeTestCase[id:"+testCase.getId() +"] *********");
		try {
			initialParameters(testCase);
			analyzeSolutionCanSatisfyAllCustomer();
			solutions.add(getPossiblePaintBatchSolution()); //e.g., Case #1: 1 0 0 0 0 0 0 
		} catch (ImpossibleException e) {
			solutions.add(getImpossiblePaintBatchSolution()); // Case #1: IMPOSSIBLE
		} catch (Exception e) {
			solutions.add(getErrorSolution(e));// Case #1: Error: NullPointerException
		}
	}
	
	public List<String> getSolutionsForAllTestCase(){
		return solutions;
	}
	
	private String getPossiblePaintBatchSolution(){
		return "Case #" + testCaseId + ":" + satisfactionMap.generateResultString();
	}
	
	private String getImpossiblePaintBatchSolution(){
		return "Case #" + testCaseId + ": IMPOSSIBLE";
	}
	
	private String getErrorSolution(Exception e){
		return "Case #" + testCaseId + ": Error: " + e.getMessage();
	}
	
	//Different testCase has different size, colors, customers; and satisfactionMap should also be refresh new
	private void initialParameters(TestCase testCase){
		initialAllGlossyPaintTypes(testCase.getColorCount());
		this.customerMap = testCase.getCustomerMap();
		satisfactionMap = new SatisfactionMap();
		this.testCaseId = testCase.getId();
	}
	
	private void initialAllGlossyPaintTypes(int colorCount){
		this.allGlossyPaintTypes = new ArrayList<PaintType>();
		for(int color=1; color<=colorCount; color++){
			allGlossyPaintTypes.add(new PaintType(color, PaintType.ColorType.GLOSSY));
		}
	}
	
	private void analyzeSolutionCanSatisfyAllCustomer() throws Exception{
		analyzeSolutionCanSatisfyCustomerWithGlossyPaint();
		analyzeSolutionCanSatisfyCustomerWithMattePaint();
	}
	
	//queue maximum customers under each glossy-batch without repeat, the rest would be who only prefer matte paint
	private void analyzeSolutionCanSatisfyCustomerWithGlossyPaint() throws Exception{
		for(PaintType glossyPaintType: allGlossyPaintTypes){
			List<Integer> preferGlossyPaintCustomerIds = listCustomerIdWhoesPreferenceStartWithPaintType(glossyPaintType);
			satisfyCustomerWithCurrentPaintType(preferGlossyPaintCustomerIds, glossyPaintType);
		}
	}
	
	// change batch to matte from glossy, to satisfy matte-only customer
	private void analyzeSolutionCanSatisfyCustomerWithMattePaint() throws Exception{
		for(Customer customer: listCustomerWhoOnlyPreferMattePaint()){
			satisfyCustomerWithCurrentPaintType(customer.getId(), customer.getPreferedMattePaint());
		}
	}
	
	private List<Customer> listCustomerWhoOnlyPreferMattePaint() {
		List<Customer> result = new ArrayList<Customer>();
		for(Customer c: customerMap.values()) {
			if(c.getPreferedMattePaint()!=null && c.getPreferedGlossyPaints().size()==0)
				result.add(c);
		}
		return result;
	}
	
	private List<Integer> listCustomerIdWhoesPreferenceStartWithPaintType(PaintType paint) {
		List<Integer> result = customerMap.values().stream()
		        .filter(c -> c.isPreferedGlossyPaintsStartWithPaint(paint))
		        .map(Customer::getId)
		        .collect(Collectors.toList());
		return result;
	}
	
	private void satisfyCheckedCustomersWithAnotherPaintTypeFromCurrentPaintType(PaintType currentPaintType) throws Exception {
		List<Integer> customerIds = satisfactionMap.listCustomerIdsWhoIsSatisfiedByCurrentPaintType(currentPaintType);
		//dismiss the customer queue under this batch
		satisfactionMap.removeCustomerIdsSatisfiedByCurrentPaintType(currentPaintType);
		//and map them into another batch
		for(Integer customerId: customerIds){
			Customer customer = customerMap.get(customerId);
			this.satisfyCustomerWithAnotherPaintTypeFromCurrentPaintType(customer, currentPaintType);
		}
	}
	
	private void satisfyCustomerWithAnotherPaintTypeFromCurrentPaintType(Customer customer, PaintType currentPaintType) throws Exception{
		PaintType newPaintType = this.findAnotherPaintTypeCanSatisfyCustomerFromCurrentPaintType(customer, currentPaintType);
		if(newPaintType == null)
			throw new ImpossibleException(); // Can not find any other paint type to satisfy current member, then IMPOSSIBLE
		
		satisfyCustomerWithCurrentPaintType(customer.getId(), newPaintType);
	}
	
	private PaintType findAnotherPaintTypeCanSatisfyCustomerFromCurrentPaintType(Customer customer, PaintType currentPaint) {
		List<PaintType> glossyPaints = customer.listAllOtherPreferedGlossyPaints(currentPaint);
		for(PaintType p: glossyPaints) {
			if(isCurrentGlossyPaintTypeStillAvaible(p))
				return p;
		}
		return customer.getPreferedMattePaint();
	}
	
	private boolean isCurrentGlossyPaintTypeStillAvaible(PaintType currentPaintType){
		return !satisfactionMap.isPaintTypeLocked(currentPaintType.toMattePaint());
	}
	
	private void satisfyCustomerWithCurrentPaintType(Integer customerId, PaintType currentPaintType) throws Exception{
		satisfyCustomerWithCurrentPaintType(new ArrayList<Integer>(Arrays.asList(customerId)), currentPaintType);
	}
	
	private void satisfyCustomerWithCurrentPaintType(List<Integer> customerIds, PaintType currentPaintType) throws Exception{
		if(currentPaintType.isMatte() && this.isCurrentGlossyPaintTypeStillAvaible(currentPaintType)){
			//dismiss the customer queue under this batch, and map them into another batch
			satisfactionMap.put(currentPaintType, customerIds);
			this.satisfyCheckedCustomersWithAnotherPaintTypeFromCurrentPaintType(currentPaintType.toGlossyPaint());
		} else {
			satisfactionMap.put(currentPaintType, customerIds);
		}
	}
}
