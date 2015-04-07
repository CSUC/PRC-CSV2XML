/**
 * 
 */
package global;


/**
 * @author amartinez
 *
 */
public class Tuples {
	
	public static class Pair<K, V> {
	    public static <K, V> Pair<K, V> createPair(K key, V value) {
	        return new Pair<K, V>(key, value);
	    }
	    private final K element0;

	    private final V element1;

	    public Pair(K element0, V element1) {
	        this.element0 = element0;
	        this.element1 = element1;
	    }

	    public K getElement0() {
	        return element0;
	    }

	    public V getElement1() {
	        return element1;
	    }

	}
	
	
	public static class Triplet<A,B,C> {
	    public static <A,B,C> Triplet<A,B,C> createTriplet(A value1, B value2 , C value3) {
	        return new Triplet<A,B,C>(value1, value2, value3);
	    }
	    private final A element0;

	    private final B element1;
	    
	    private final C element2;
	    
	    public Triplet(A element0, B element1, C element2) {
	        this.element0 = element0;
	        this.element1 = element1;
	        this.element2 = element2;
	    }

	    public A getElement0() {
	        return element0;
	    }

	    public B getElement1() {
	        return element1;
	    }
	    public C getElement2(){
	    	return element2;
	    }

	}

}
