import java.util.*;
import java.security.*;
public class RSA {


    public boolean isPrime(int n) {
        if (n <= 1) return false;
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }
        return true;
    }
    int generatePrime(int bits) throws Exception{  //generate el p w el q
        if (bits < 256) {
            throw new Exception("INVALIDE SIZE");
        }
        while (true) {
            int prime = (int)(Math.random() * (1 << bits)) + 1; // Cast to int and generate in proper range
            if (isPrime(prime)) {

                return prime;
            }
        }

    }

    public int systemModule(int p,int q) {

        return p * q; // Return the product of the two primes
    }

    public int Euler(int p, int q) { //ø(n)
        return (p - 1) * (q - 1); // Calculate Euler's totient function
    }


    public int gcd(int a, int b) {
        if (b == 0 ) return a;
        return gcd(b, a % b);
    }


//    public int backSubstitution(int p, int q) {
//        if (q == 0||q==1) return p;
//        return backSubstitution(p - 1, q - 1) + backSubstitution(p, q - 1);
//    }


public int generateEncryptionKey(int euler) {
    Random random = new Random();
    int e;

    do {
        // Generate a random number between 2 and euler-1
        e = random.nextInt(euler - 2) + 2;
    } while (gcd(e, euler) != 1); // Check if e and euler are coprime

    return e;
}

    public int[] generatePublicKey(int bits) throws Exception {
        // Generate two distinct prime numbers
        int p = generatePrime(bits);
        int q = generatePrime(bits);

        while (p == q) {
            q = generatePrime(bits); // Ensure p and q are different
        }

        int modulus = systemModule(p, q); // Calculate n = p*q
        int euler = Euler(p, q); // Calculate φ(n) = (p-1)*(q-1)

        // Generate the encryption key e such that 1 < e < φ(n) and gcd(e, φ(n)) = 1
        int e = generateEncryptionKey(euler);

        // Return the public key as [e, n]
        return new int[] {e, modulus};
    }


        public static void main(String[] args) throws Exception {
            Scanner reader = new Scanner(System.in);
            System.out.println("DISCRLEAMER EL SIZE LAZEM YB2A KBEER AKBER MEN 256 ");
            System.out.print("Enter bit size: ");
            int bitsSize = reader.nextInt();
            RSA rsa = new RSA();

            int[] publicKey = rsa.generatePublicKey(bitsSize);
            System.out.println("Public Key:");
            System.out.println("e (encryption key): " + publicKey[0]);
            System.out.println("n (modulus): " + publicKey[1]);
        }
    }

