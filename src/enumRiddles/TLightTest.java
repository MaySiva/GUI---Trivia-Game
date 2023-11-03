package enumRiddles;

enum TLight {
	// Each instance provides its implementation to abstract method
	// RED(30),
	// AMBER(10),
	//GREEN(30);

	RED(30) {
		public TLight next() {
			return TLight.GREEN;
		}
	},
	AMBER(10) {
		public TLight next() {
			return TLight.RED;
		}
	},
	GREEN(30) {
		public TLight next() {
			return TLight.AMBER;
		}
	};

	public abstract TLight next();


	private final int seconds;     // Private variable

	TLight(int seconds) {          // Constructor
		this.seconds = seconds;
	}


	int getSeconds() {             // Getter
		return seconds;
	}


}
	   
	public class TLightTest {
	   public static void main(String[] args) {
	      for (TLight light : TLight.values()) {
	         System.out.printf("%s: %d seconds, next is %s\n", light,
	               light.getSeconds(), light.next());
	      }
	   }

	}