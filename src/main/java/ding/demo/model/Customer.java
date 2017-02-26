package ding.demo.model;

import java.util.ArrayList;
import java.util.List;

public class Customer {
	private int id;
	private PaintType preferedMattePaint;
	private List<PaintType> preferedGlossyPaints = new ArrayList<PaintType>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public PaintType getPreferedMattePaint() {
		return preferedMattePaint;
	}
	public void setPreferedMattePaint(PaintType preferedMattePaint) {
		this.preferedMattePaint = preferedMattePaint;
	}
	public List<PaintType> getPreferedGlossyPaints() {
		return preferedGlossyPaints;
	}
	public void setPreferedGlossyPaints(List<PaintType> preferedGlossyPaints) {
		this.preferedGlossyPaints = preferedGlossyPaints;
	}
	
	public void addPreferPaintType(PaintType paintType){
		if(paintType.isMatte())
			this.preferedMattePaint = paintType;
		else 
			this.preferedGlossyPaints.add(paintType);
	}
	
	public boolean isPreferedGlossyPaintsStartWithPaint(PaintType paint) {
		if(this.preferedGlossyPaints.size()>0)
			return this.preferedGlossyPaints.get(0).equals(paint);
		
		return false;
	}
	
	public List<PaintType> listAllOtherPreferedGlossyPaints(PaintType currentPaint){
		this.preferedGlossyPaints.remove(currentPaint);
		return preferedGlossyPaints;
	}
}
