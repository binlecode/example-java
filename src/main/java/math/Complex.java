package math;



/** A class to represent Complex Numbers. A Complex object is 
 * immutable once created; the add, subtract and multiply routines 
 * return newly created Complex objects containing the results. 
 */

public class Complex {
 	private double r;  // the real part
	private double i;  // the imaginary part
		
	public Complex(double r, double i) {
		this.r = r;
		this.i = i;
	}
	
	
	

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer().append(r); 
        if (i>0) {
        	sb.append('+');    // else append(i) appends - sign
        }
        return sb.append(i).append('i').toString();
	}
	
	/** Return the magnitude of a complex number */
	public Double magnitude() {
		return Math.sqrt(r * r + i * i);
	}
	
	public Double getReal() {
		return r;
	}

	public Double getImag() {
		return i;
	}
	
	/** Add another Complex to this one */
	public Complex add(Complex other) {
		return add(this, other);
	}
	
	/** Add two Complexes */
	public static Complex add(Complex c1, Complex c2) {
		return new Complex(c1.r+c2.r, c1.i+c2.i);
	}
	
	/** Subtract another Complex from this one */
	public Complex subtract(Complex other) {
		return subtract(this, other);
	}
	
	/** Subtract two Complexes */
	public static Complex subtract(Complex c1, Complex c2) {
		return new Complex(c1.r-c2.r, c1.i-c2.i);
	}
	
	/** Multiply this Complex times another one */ 
    public Complex multiply(Complex other) { 
        return multiply(this, other); 
    } 

    /** Multiply two Complexes */ 
    public static Complex multiply(Complex c1, Complex c2) { 
        return new Complex(c1.r*c2.r - c1.i*c2.i, c1.r*c2.i + c1.i*c2.r); 
    }
    
    /** Divide this Complex by another one */
    public Complex devide(Complex other) {
    	return divide(this, other);
    }
	
    /** Divide first Complex by second one */
    public static Complex divide(Complex c1, Complex c2) {
        return new Complex((c1.r*c2.r+c1.i*c2.i)/(c2.r*c2.r+c2.i*c2.i),
        		(c1.i*c2.r-c1.r*c2.i)/(c2.r*c2.r+c2.i*c2.i));
    }
    
    
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	
}
