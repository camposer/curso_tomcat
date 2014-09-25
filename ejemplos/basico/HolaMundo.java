public class HolaMundo {
	public static void main(String args[]) {
		System.out.println("Hola mundo!");
		
		String s1 = "hola";
		String s2 = "hola";
		
		if (s1 == s2) // Apuntan a la misma dirección de memoria
			System.out.println("Son idénticos");
		else if (s1.equals(s2)) // Iguales en valor
			System.out.println("Sin iguales");
		else
			System.out.println("Sin diferentes");
	}
}