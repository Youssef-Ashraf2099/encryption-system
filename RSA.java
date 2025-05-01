import java.util.*;
import java.math.BigInteger;

public class RSA {

    public boolean isPrime(BigInteger n) {
        if (n.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            return false; // Even numbers greater than 2 are not prime
        }

        // Use the Miller-Rabin primality test with a fixed number of iterations
        int certainty = 40; // Higher value increases accuracy
        /*
         * The certainty parameter determines the accuracy of the test:
         * A value of 40 means the probability of a false positive is less than 2^-40,
         * which is sufficient for cryptographic purposes.
         */
        return n.isProbablePrime(certainty);
    }

    BigInteger generatePrime(int bits) {
        Random random = new Random();

        BigInteger primeCandidate;
        do {
            // Generate a random number of the specified bit length
            primeCandidate = new BigInteger(bits, random).setBit(bits - 1); // Ensure the number has the specified bit length
        } while (!isPrime(primeCandidate)); // Check if the number is prime

        return primeCandidate; // Return the prime number
    }

    BigInteger[] generateModSystem(int bits) {
        if (bits % 2 != 0) {
            bits++;
        } // Ensure bits is even for prime generation, to make sure that p * q generates n >= bits

        // Generate two distinct prime numbers
        BigInteger p = generatePrime(bits / 2);
        BigInteger q;
        do {
            q = generatePrime(bits / 2); // Generate the second prime number
        } while (p.equals(q)); // Ensure p and q are different

        return new BigInteger[] { p, q }; // Return the two prime numbers as an array
    }

  public BigInteger EulersTolerance(BigInteger p, BigInteger q) {
        return p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
  }

    public BigInteger generatePublicKey(BigInteger euler) {
        BigInteger e = euler.add(BigInteger.TWO); // Start with e > euler
        while (!isPrime(e)) { // Ensure e is prime
            e = e.add(BigInteger.ONE);
        }
        return e;
    }




    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the number of bits for the modulus where modulus n is at least with size of bits >= 256 bits: ");
        int bits = sc.nextInt(); // Read the number of bits for the modulus
        while (bits < 256) {
            System.out.println("Please enter a number greater than or equal to 256: ");
            bits = sc.nextInt(); // Read the number of bits for the modulus
        }

        RSA rsa = new RSA(); // Create an instance of the RSA class

        // Generate the public and private keys
        BigInteger[] keys = rsa.generateModSystem(bits); // Generate the public and private keys
        BigInteger p = keys[0]; // Get the first prime number
        BigInteger q = keys[1]; // Get the second prime number
        BigInteger n = p.multiply(q); // Calculate n = p * q
        System.out.println("p: " + p); // Print the first prime number
        System.out.println("q: " + q); // Print the second prime number 
        System.out.println("n: " + n); // Print the modulus n
        BigInteger euler = rsa.EulersTolerance(p, q);
        System.out.println("euler: " + euler);
        BigInteger publicKey = rsa.generatePublicKey(euler);
        System.out.println("publicKey: " + publicKey);
    }
}