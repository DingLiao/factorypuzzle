package ding.demo.model;

public class PaintType implements Comparable<PaintType>{
	public static enum ColorType{GLOSSY(0),MATTE(1);
		private int value;
		ColorType(int value){
			this.value = value;
		}
		
		public int getValue(){
			return value;
		}
		
		public static ColorType get(int value){
			for(ColorType ct:values()){
				if(value==ct.value)
					return ct;
			}
			return null;
		}
	};
	private int color;
	private ColorType colorType;
	
	public PaintType() {
	}
	
	public PaintType(int color, int colorType) {
		this(color, ColorType.get(colorType));
	}
	
	public PaintType(int color, ColorType colorType) {
		this.color = color;
		this.colorType = colorType;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public ColorType getColorType() {
		return colorType;
	}
	public void setColorType(ColorType colorType) {
		this.colorType = colorType;
	}
	
	public boolean isMatte() {
		return this.colorType.equals(ColorType.MATTE);
	}

	public boolean equals(PaintType paint) {
		return this.color==paint.getColor() 
				&& this.colorType.equals(paint.getColorType());
	}
	
	public PaintType toGlossyPaint(){
		return new PaintType(this.getColor(),ColorType.GLOSSY);
	}
	
	public PaintType toMattePaint(){
		return new PaintType(this.getColor(),ColorType.MATTE);
	}
	
	public PaintType getReversedPaint() {
		if(this.colorType.equals(ColorType.GLOSSY))
			return this.toMattePaint();
		return this.toGlossyPaint();
	}

	@Override
	public int hashCode() {
		return this.color + this.colorType.getValue()*10000;
	}

	@Override
	public int compareTo(PaintType toPaint) {
		int result = this.color - toPaint.color;
		if(result == 0)
			result = this.colorType.value - toPaint.colorType.value;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PaintType) {  
            if (this.getColor()==(((PaintType) obj).getColor()) && this.getColorType().equals(((PaintType) obj).getColorType())) {  
                return true;  
            }
        }  
        return false;  
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		sb.append("color:" + this.color);
		sb.append("type:" + this.colorType);
		sb.append("]");
		return sb.toString();
	}
	
}
