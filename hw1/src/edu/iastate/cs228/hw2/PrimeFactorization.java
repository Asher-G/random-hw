package edu.iastate.cs228.hw2;

/**
 *  
 * @author Asher Gust
 *
 */

import java.util.ListIterator;

public class PrimeFactorization implements Iterable<edu.iastate.cs228.hw2.PrimeFactor> {
	private static final long OVERFLOW = -1;
	private long value; 	// the factored integer 
							// it is set to OVERFLOW when the number is greater than 2^63-1, the
						    // largest number representable by the type long. 
	
	/**
	 * Reference to dummy node at the head.
	 */
	private Node head;
	  
	/**
	 * Reference to dummy node at the tail.
	 */
	private Node tail;
	
	private int size;     	// number of distinct prime factors


	// ------------
	// Constructors 
	// ------------
	
    /**
	 *  Default constructor constructs an empty list to represent the number 1.
	 *  
	 *  Combined with the add() method, it can be used to create a prime factorization.  
	 */
	public PrimeFactorization() {
		head = new Node();
		tail = new Node();
		head.next = tail;
		tail.previous = head;
		size = 0;
	}

	
	/** 
	 * Obtains the prime factorization of n and creates a doubly linked list to store the result.   
	 * Follows the direct search factorization algorithm in Section 1.2 of the project description. 
	 * 
	 * @param n
	 * @throws IllegalArgumentException if n < 1
	 */
	public PrimeFactorization(long n) throws IllegalArgumentException {
		this();
		if (n < 1) throw new IllegalArgumentException();
		value = n;

		for (int i = 2; i * i <= n; i++){
			if(!(i > 2 && i % 2 == 0)){
				if(n % i == 0){
					int multiplicity = 0;
					while(n % i == 0){
						multiplicity++;
						n /= i;
					}
					add(i,multiplicity);
				}
			}
		}
		add((int)n,1);
	}
	
	
	/**
	 * Copy constructor. It is unnecessary to verify the primality of the numbers in the list.
	 * 
	 * @param pf
	 */
	public PrimeFactorization(PrimeFactorization pf) {
		this();

		PrimeFactorizationIterator pfIterator = pf.iterator();
		while (pfIterator.hasNext()){
			add(pfIterator.cursor.pFactor.prime,pfIterator.cursor.pFactor.multiplicity);
			pfIterator.next();
		}
		updateValue();
	}
	
	/**
	 * Constructs a factorization from an array of prime factors.  Useful when the number is 
	 * too large to be represented even as a long integer. 
	 * 
	 * @param pfList
	 */
	public PrimeFactorization (PrimeFactor[] pfList) {
		this();
		for (PrimeFactor pf: pfList){
			add(pf.prime,pf.multiplicity);
		}
		updateValue();
	}
	
	

	// --------------
	// Primality Test
	// --------------
	
    /**
	 * Test if a number is a prime or not.  Check iteratively from 2 to the largest 
	 * integer not exceeding the square root of n to see if it divides n. 
	 * 
	 *@param n
	 *@return true if n is a prime 
	 * 		  false otherwise 
	 */
    public static boolean isPrime(long n) {
	    for (int i = 2; i * i <= n; i++){
	    	if (n % i == 0) return false;
		}
		return true;
	}   

   
	// ---------------------------
	// Multiplication and Division 
	// ---------------------------
	
	/**
	 * Multiplies the integer v represented by this object with another number n.  Note that v may 
	 * be too large (in which case this.value == OVERFLOW). You can do this in one loop: Factor n and 
	 * traverse the doubly linked list simultaneously. For details refer to Section 3.1 in the 
	 * project description. Store the prime factorization of the product. Update value and size. 
	 * 
	 * @param n
	 * @throws IllegalArgumentException if n < 1
	 */
	public void multiply(long n) throws IllegalArgumentException {
		if (n < 1) throw new IllegalArgumentException();

		PrimeFactorizationIterator pfIterator = new PrimeFactorizationIterator();

		for (int i = 2; i * i <= n; i++){
			if(!(i > 2 && i % 2 == 0)){
				if(n % i == 0){
					int multiplicity = 0;
					while(n % i == 0){
						multiplicity++;
						n /= i;
					}
					while(pfIterator.hasNext() && pfIterator.cursor.pFactor.prime < i){
						pfIterator.next();
					}
					add(i,multiplicity);
				}
			}
		}
		add((int)n,1);
		updateValue();
	}
	
	/**
	 * Multiplies the represented integer v with another number in the factorization form.  Traverse both 
	 * linked lists and store the result in this list object.  See Section 3.1 in the project description 
	 * for details of algorithm. 
	 * 
	 * @param pf 
	 */
	public void multiply(PrimeFactorization pf) {
		PrimeFactorizationIterator pfIterator = new PrimeFactorizationIterator();
		PrimeFactorizationIterator pfIterator2 = pf.iterator();

		while (pfIterator2.hasNext()) {
			while (pfIterator.hasNext() && pfIterator.cursor.pFactor.prime < pfIterator2.cursor.pFactor.prime) {
				pfIterator.next();
			}
			add(pfIterator2.cursor.pFactor.prime, pfIterator2.cursor.pFactor.multiplicity);
			pfIterator2.next();

		}
		updateValue();
	}
	
	/**
	 * Multiplies the integers represented by two PrimeFactorization objects.  
	 * 
	 * @param pf1
	 * @param pf2
	 * @return object of PrimeFactorization to represent the product 
	 */
	public static PrimeFactorization multiply(PrimeFactorization pf1, PrimeFactorization pf2) {

		PrimeFactorization pfNew = new PrimeFactorization(pf1);
		pfNew.multiply(pf2);

		return pfNew;
	}

	
	/**
	 * Divides the represented integer v by n.  Make updates to the list, value, size if divisible.  
	 * No update otherwise. Refer to Section 3.2 in the project description for details. 
	 *  
	 * @param n
	 * @return  true if divisible 
	 *          false if not divisible 
	 * @throws IllegalArgumentException if n <= 0
	 */
	public boolean dividedBy(long n) throws IllegalArgumentException {
		if (this.value != OVERFLOW && this.value < n) return false;

		PrimeFactorization pf = new PrimeFactorization(n);
		return this.dividedBy(pf);
	}

	
	/**
	 * Division where the divisor is represented in the factorization form.  Update the linked 
	 * list of this object accordingly by removing those nodes housing prime factors that disappear  
	 * after the division.  No update if this number is not divisible by pf. Algorithm details are 
	 * given in Section 3.2. 
	 * 
	 * @param pf
	 * @return	true if divisible by pf
	 * 			false otherwise
	 */
	public boolean dividedBy(PrimeFactorization pf) {
		PrimeFactorization copy = new PrimeFactorization(this);
		PrimeFactorizationIterator copyIterator = copy.iterator();
		PrimeFactorizationIterator pfIterator = pf.iterator();

		while (pfIterator.hasNext()) {
			while (copyIterator.cursor.pFactor.prime < pfIterator.cursor.pFactor.prime) {
				if (!copyIterator.hasNext() && pfIterator.hasNext()) return false;
				copyIterator.next();
			}
			if (copyIterator.cursor.pFactor.prime > pfIterator.cursor.pFactor.prime) return false; //number is not divisible by pf.
			if (copyIterator.cursor.pFactor.prime == pfIterator.cursor.pFactor.prime && copyIterator.cursor.pFactor.multiplicity < pfIterator.cursor.pFactor.multiplicity) return false; // number not divisible by pf.
			copy.remove(pfIterator.cursor.pFactor.prime, pfIterator.cursor.pFactor.multiplicity);
			pfIterator.next();
		}
		this.head = copy.head;
		this.tail = copy.tail;
		updateValue();
		this.size = copy.size;
		return true;
	}
	
	/**
	 * Divide the integer represented by the object pf1 by that represented by the object pf2. 
	 * Return a new object representing the quotient if divisible. Do not make changes to pf1 and 
	 * pf2. No update if the first number is not divisible by the second one. 
	 *  
	 * @param pf1
	 * @param pf2
	 * @return quotient as a new PrimeFactorization object if divisible
	 *         null otherwise 
	 */
	public static PrimeFactorization dividedBy(PrimeFactorization pf1, PrimeFactorization pf2) {
		if (pf1.dividedBy(pf2)) return pf1;
		return null; 
	}

	
	// -------------------------------------------------
	// Greatest Common Divisor and Least Common Multiple 
	// -------------------------------------------------

	/**
	 * Computes the greatest common divisor (gcd) of the represented integer v and an input integer n.
	 * Returns the result as a PrimeFactor object.  Calls the method Euclidean() if 
	 * this.value != OVERFLOW.
	 *     
	 * It is more efficient to factorize the gcd than n, which can be much greater. 
	 *     
	 * @param n
	 * @return prime factorization of gcd
	 * @throws IllegalArgumentException if n < 1
	 */
	public PrimeFactorization gcd(long n) throws IllegalArgumentException {
		if (n < 1) throw new IllegalArgumentException();
		if (value != OVERFLOW) return new PrimeFactorization(Euclidean(value,n));
		return gcd(new PrimeFactorization(n));
	}
	

	/**
	  * Implements the Euclidean algorithm to compute the gcd of two natural numbers m and n. 
	  * The algorithm is described in Section 4.1 of the project description. 
	  * 
	  * @param m
	  * @param n
	  * @return gcd of m and n. 
	  * @throws IllegalArgumentException if m < 1 or n < 1
	  */
 	public static long Euclidean(long m, long n) throws IllegalArgumentException {
 		if (m < 1 || n < 1) throw new IllegalArgumentException();
		if (m % n == 0) return n;
		if (n % m == 0) return m;

		if (m > n){
			return Euclidean(n, m % n);
		} else {
			return Euclidean(m, n % m);
		}
	}

 	
	/**
	 * Computes the gcd of the values represented by this object and pf by traversing the two lists.  No 
	 * direct computation involving value and pf.value. Refer to Section 4.2 in the project description 
	 * on how to proceed.  
	 * 
	 * @param  pf
	 * @return prime factorization of the gcd
	 */
	public PrimeFactorization gcd(PrimeFactorization pf) {
		PrimeFactorizationIterator pfIterator = new PrimeFactorizationIterator();
		PrimeFactorizationIterator pfIterator2 = pf.iterator();
		PrimeFactorization pfNew = new PrimeFactorization();

		while(pfIterator.hasNext()){
			while(pfIterator2.hasNext() && pfIterator2.cursor.pFactor.prime < pfIterator.cursor.pFactor.prime){
				pfIterator2.next();
			}
			if (pfIterator.cursor.pFactor.prime == pfIterator2.cursor.pFactor.prime) {
				pfNew.add(pfIterator.cursor.pFactor.prime,Math.min(pfIterator.cursor.pFactor.multiplicity, pfIterator2.cursor.pFactor.multiplicity));
			}
		}
		pfNew.updateValue();
		return pfNew;
	}
	
	
	/**
	 * 
	 * @param pf1
	 * @param pf2
	 * @return prime factorization of the gcd of two numbers represented by pf1 and pf2
	 */
	public static PrimeFactorization gcd(PrimeFactorization pf1, PrimeFactorization pf2) {
		return pf1.gcd(pf2);
	}

	
	/**
	 * Computes the least common multiple (lcm) of the two integers represented by this object 
	 * and pf.  The list-based algorithm is given in Section 4.3 in the project description. 
	 * 
	 * @param pf  
	 * @return factored least common multiple  
	 */
	public PrimeFactorization lcm(PrimeFactorization pf) {
		PrimeFactorizationIterator pfIterator = new PrimeFactorizationIterator();
		PrimeFactorizationIterator pfIterator2 = pf.iterator();
		PrimeFactorization pfNew = new PrimeFactorization();

		while(pfIterator.hasNext()){
			while(pfIterator2.hasNext() && pfIterator2.cursor.pFactor.prime < pfIterator.cursor.pFactor.prime){
				pfNew.add(pfIterator2.cursor.pFactor.prime,pfIterator2.cursor.pFactor.multiplicity);
				pfIterator2.next();
			}
			if (pfIterator2.hasNext() && pfIterator.cursor.pFactor.prime == pfIterator2.cursor.pFactor.prime) {
				pfNew.add(pfIterator.cursor.pFactor.prime,Math.max(pfIterator.cursor.pFactor.multiplicity, pfIterator2.cursor.pFactor.multiplicity));
				pfIterator2.next();
			} else {
				pfNew.add(pfIterator.cursor.pFactor.prime,pfIterator.cursor.pFactor.multiplicity);
			}
			pfIterator.next();
		}
		while(pfIterator2.hasNext()){
			pfNew.add(pfIterator2.cursor.pFactor.prime,pfIterator2.cursor.pFactor.multiplicity);
			pfIterator2.next();
		}
		if(value == OVERFLOW || pf.value == OVERFLOW) pfNew.value = OVERFLOW;
		else pfNew.updateValue();
		return pfNew;
	}

	
	/**
	 * Computes the least common multiple of the represented integer v and an integer n. Construct a 
	 * PrimeFactors object using n and then call the lcm() method above.  Calls the first lcm() method. 
	 * 
	 * @param n
	 * @return factored least common multiple 
	 * @throws IllegalArgumentException if n < 1
	 */
	public PrimeFactorization lcm(long n) throws IllegalArgumentException {
		return lcm(new PrimeFactorization(n));
	}

	/**
	 * Computes the least common multiple of the integers represented by pf1 and pf2. 
	 * 
	 * @param pf1
	 * @param pf2
	 * @return prime factorization of the lcm of two numbers represented by pf1 and pf2
	 */
	public static PrimeFactorization lcm(PrimeFactorization pf1, PrimeFactorization pf2) {
		return pf1.lcm(pf2);
	}

	
	// ------------
	// List Methods
	// ------------
	
	/**
	 * Traverses the list to determine if p is a prime factor. 
	 * 
	 * Precondition: p is a prime. 
	 * 
	 * @param p  
	 * @return true  if p is a prime factor of the number v represented by this linked list
	 *         false otherwise 
	 * @throws IllegalArgumentException if p is not a prime
	 */
	public boolean containsPrimeFactor(int p) throws IllegalArgumentException {
		if (!isPrime(p)) throw new IllegalArgumentException("input must be prime");

		PrimeFactorizationIterator pfIterator = new PrimeFactorizationIterator();
		while (pfIterator.hasNext()){
			if (pfIterator.cursor.pFactor.prime == p) return true;
			pfIterator.next();
		}
		return false; 
	}
	
	// The next two methods ought to be private but are made public for testing purpose. Keep
	// them public 
	
	/**
	 * Adds a prime factor p of multiplicity m.  Search for p in the linked list.  If p is found at 
	 * a node N, add m to N.multiplicity.  Otherwise, create a new node to store p and m. 
	 *  
	 * Precondition: p is a prime. 
	 * 
	 * @param p  prime 
	 * @param m  multiplicity
	 * @return   true  if m >= 1
	 *           false if m < 1   
	 */
    public boolean add(int p, int m) {
    	if (m < 1 || p == 1) return false;
		PrimeFactorizationIterator pfIterator = new PrimeFactorizationIterator();
		while (pfIterator.hasNext()){
			if (pfIterator.cursor.pFactor.prime == p) {
				pfIterator.cursor.pFactor.multiplicity += m;
				return true;
			}
			if (pfIterator.cursor.pFactor.prime > p) {
				pfIterator.add(new PrimeFactor(p,m));
				return true;
			}
			pfIterator.next();
		}
		pfIterator.add(new PrimeFactor(p,m));
		return true;
	}

	    
    /**
     * Removes m from the multiplicity of a prime p on the linked list.  It starts by searching 
     * for p.  Returns false if p is not found, and true if p is found. In the latter case, let 
     * N be the node that stores p. If N.multiplicity > m, subtracts m from N.multiplicity.  
     * If N.multiplicity <= m, removes the node N.  
     * 
     * Precondition: p is a prime. 
     * 
     * @param p
     * @param m
     * @return true  when p is found. 
     *         false when p is not found. 
     * @throws IllegalArgumentException if m < 1
     */
    public boolean remove(int p, int m) throws IllegalArgumentException {
		if (m < 1) throw new IllegalArgumentException("input must be 1 or greater");

		PrimeFactorizationIterator pfIterator = new PrimeFactorizationIterator();
		while (pfIterator.hasNext()){
			if (pfIterator.cursor.pFactor.prime == p) {
				if (pfIterator.cursor.pFactor.multiplicity <= m) pfIterator.remove();
				else pfIterator.cursor.pFactor.multiplicity -= m;
				return true;
			}
			pfIterator.next();
		}
		return false;
	}


    /**
     * 
     * @return size of the list
     */
	public int size() 
	{
		return size;
	}

	
	/**
	 * Writes out the list as a factorization in the form of a product. Represents exponentiation 
	 * by a caret.  For example, if the number is 5814, the returned string would be printed out 
	 * as "2 * 3^2 * 17 * 19". 
	 */
	@Override 
	public String toString() {
		String s = "";
		PrimeFactorizationIterator pfIterator = new PrimeFactorizationIterator();

		if (pfIterator.hasNext()) {
			s += pfIterator.cursor.toString();
			pfIterator.next();
	}
		while (pfIterator.hasNext()){
			s += " * " + pfIterator.cursor.toString();
			pfIterator.next();
		}
		return s;
	}

	
	// The next three methods are for testing, but you may use them as you like.  

	/**
	 * @return true if this PrimeFactorization is representing a value that is too large to be within 
	 *              long's range. e.g. 999^999. false otherwise.
	 */
	public boolean valueOverflow() {
		return value == OVERFLOW;
	}

	/**
	 * @return value represented by this PrimeFactorization, or -1 if valueOverflow()
	 */
	public long value() {
		return value;
	}

	
	public PrimeFactor[] toArray() {
		PrimeFactor[] arr = new PrimeFactor[size];
		int i = 0;
		for (PrimeFactor pf : this)
			arr[i++] = pf;
		return arr;
	}


	
	@Override
	public PrimeFactorizationIterator iterator() {
	    return new PrimeFactorizationIterator();
	}
	
	/**
	 * Doubly-linked node type for this class.
	 */
    private class Node {
		public PrimeFactor pFactor;			// prime factor 
		public Node next;
		public Node previous;
		
		/**
		 * Default constructor for creating a dummy node.
		 */
		public Node() {
			pFactor = null;
			next = null;
			previous = null;
		}
	    
		/**
		 * Precondition: p is a prime
		 * 
		 * @param p	 prime number 
		 * @param m  multiplicity 
		 * @throws IllegalArgumentException if m < 1 
		 */
		public Node(int p, int m) throws IllegalArgumentException {
			pFactor = new PrimeFactor(p,m);

		}   

		
		/**
		 * Constructs a node over a provided PrimeFactor object. 
		 * 
		 * @param pf
		 * @throws IllegalArgumentException
		 */
		public Node(PrimeFactor pf) {
			pFactor = pf;
		}


		/**
		 * Printed out in the form: prime + "^" + multiplicity.  For instance "2^3". 
		 * Also, deal with the case pFactor == null in which a string "dummy" is 
		 * returned instead.  
		 */
		@Override
		public String toString() {
			if (pFactor == null) return "dummy";
			return pFactor.toString();
		}
    }

    
    private class PrimeFactorizationIterator implements ListIterator<PrimeFactor> {
        // Class invariants: 
        // 1) logical cursor position is always between cursor.previous and cursor
        // 2) after a call to next(), cursor.previous refers to the node just returned 
        // 3) after a call to previous() cursor refers to the node just returned 
        // 4) index is always the logical index of node pointed to by cursor

        private Node cursor = head.next;
        private Node pending = null;    // node pending for removal
        private int index = 0;      
  	  
    	// other instance variables ... 
    	  
      
        /**
    	 * Default constructor positions the cursor before the smallest prime factor.
    	 */
    	public PrimeFactorizationIterator() {
    	}

    	@Override
    	public boolean hasNext() {
    		if (cursor.pFactor != null) return true;
    		return false; 
    	}

    	
    	@Override
    	public boolean hasPrevious() {
			if (cursor.previous.pFactor != null) return true;
			return false;
		}

 
    	@Override 
    	public PrimeFactor next() {
    		index++;
    		cursor = cursor.next;
    		return cursor.pFactor;
    	}

 
    	@Override 
    	public PrimeFactor previous() {
    		index--;
    		cursor = cursor.previous;
    		return cursor.pFactor;
    	}

   
    	/**
    	 *  Removes the prime factor returned by next() or previous()
    	 *  
    	 *  @throws IllegalStateException if pending == null 
    	 */
    	@Override
    	public void remove() throws IllegalStateException {
    		pending = cursor;
    		next();
    		if (pending == null) throw new IllegalArgumentException();
    		unlink(pending);
    		pending = null;
    		size--;
    	}
 
 
    	/**
    	 * Adds a prime factor at the cursor position.  The cursor is at a wrong position 
    	 * in either of the two situations below: 
    	 * 
    	 *    a) pf.prime < cursor.previous.pFactor.prime if cursor.previous != head. 
    	 *    b) pf.prime > cursor.pFactor.prime if cursor != tail. 
    	 * 
    	 * Take into account the possibility that pf.prime == cursor.pFactor.prime. 
    	 * 
    	 * Precondition: pf.prime is a prime. 
    	 * 
    	 * @param pf  
    	 * @throws IllegalArgumentException if the cursor is at a wrong position. 
    	 */
    	@Override
        public void add(PrimeFactor pf) throws IllegalArgumentException {
        	if ((cursor.previous != head && pf.prime < cursor.previous.pFactor.prime) || (cursor != tail && pf.prime > cursor.pFactor.prime)) throw new IllegalArgumentException();

        	if(cursor.pFactor == null){
				Node newNode = new Node(pf);
				link(cursor.previous, newNode);
				size++;
			} else if (pf.prime == cursor.pFactor.prime) cursor.pFactor.multiplicity += pf.multiplicity;
        	else {
        		Node newNode = new Node(pf);
        		link(cursor.previous, newNode);
        		size++;
			}
        }


    	@Override
		public int nextIndex() {
			return index;
		}


    	@Override
		public int previousIndex() {
			return index - 1;
		}

		@Deprecated
		@Override
		public void set(PrimeFactor pf) {
			throw new UnsupportedOperationException(getClass().getSimpleName() + " does not support set method");
		}
        
    	// Other methods you may want to add or override that could possibly facilitate 
    	// other operations, for instance, addition, access to the previous element, etc.
    	// 
    	// ...
    	// 
    }

    
    // --------------
    // Helper methods 
    // -------------- 
    
    /**
     * Inserts toAdd into the list after current without updating size.
     * 
     * Precondition: current != null, toAdd != null
     */
    private void link(Node current, Node toAdd) {
		current.next.previous = toAdd;
		toAdd.next = current.next;
		current.next = toAdd;
		toAdd.previous = current;
    }

	 
    /**
     * Removes toRemove from the list without updating size.
     */
    private void unlink(Node toRemove) {
		toRemove.next.previous = toRemove.previous;
		toRemove.previous.next = toRemove.next;
    }


    /**
	  * Remove all the nodes in the linked list except the two dummy nodes. 
	  * 
	  * Made public for testing purpose.  Ought to be private otherwise. 
	  */
	public void clearList() {
		PrimeFactorizationIterator pfIterator = new PrimeFactorizationIterator();
		while (pfIterator.hasNext()){
			pfIterator.remove();
		}
	}	
	
	/**
	 * Multiply the prime factors (with multiplicities) out to obtain the represented integer.  
	 * Use Math.multiply(). If an exception is throw, assign OVERFLOW to the instance variable value.  
	 * Otherwise, assign the multiplication result to the variable. 
	 * 
	 */
	private void updateValue() {
		try {
			value = 1;
			PrimeFactorizationIterator pfIterator = new PrimeFactorizationIterator();
			while (pfIterator.hasNext()){
				value = Math.multiplyExact((long) Math.pow(pfIterator.cursor.pFactor.prime,pfIterator.cursor.pFactor.multiplicity), value);
				pfIterator.next();
			}
		} 
			
		catch (ArithmeticException e) {
			value = OVERFLOW;
		}
		
	}
}
