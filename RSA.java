import java.nio.charset.StandardCharsets;
import java.util.*;
import java.math.BigInteger;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
public class RSA {

    public BigInteger EulersTotient(BigInteger p, BigInteger q) {
        return p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)); //subtract from p and q ONE and multiply them (p-1)*(q-1)
    }

    BigInteger[] generateModSystem(int bits) {
        SecureRandom random = new SecureRandom(); // Create a SecureRandom instance for secure random number generation
        // Generate two distinct prime numbers
        BigInteger p = BigInteger.probablePrime((bits + 1) / 2, random);
        BigInteger q, n;
        int counter = 0; // Initialize a counter to track the number of iterations
        do {
            counter++; // Increment the counter for each iteration
            q = BigInteger.probablePrime((bits + 1) / 2, random); // Generate the second prime number
            n = p.multiply(q); // Calculate n = p * q
        } while (p.equals(q) || n.bitLength() != bits); // Ensure p and q are distinct and n has the desired bit length

        // Print the number of iterations taken to find distinct primes
        System.out.println("Number of iterations to find distinct primes: " + counter);
        return new BigInteger[] {p, q, n}; // Return the generated prime numbers and modulus
    }

    public BigInteger generatePublicKey(BigInteger euler) {
        return new BigInteger("1000000007"); // Return a commonly used public exponent (Fermat's prime)
        // fermat's prime is a prime number of the form 2^n + 1, where n is a positive integer.
        // It is often used as a public exponent in RSA encryption due to its properties that make encryption and decryption efficient.
    }
    /*
    ASCII: 7 bits. 128 code points.

    ISO-8859-1: 8 bits. 256 code points. bed3m el english w latin w kam haga zyada bas mesh byst3ml kteer

À	Á	Â	Ã	Ä	Å	Æ	Ç	È	É	Ê	Ë	Ì	Í	Î	Ï
Dx	Ð	Ñ	Ò	Ó	Ô	Õ	Ö	×	Ø	Ù	Ú	Û	Ü	Ý	Þ	ß
Ex	à	á	â	ã	ä	å	æ	ç	è	é	ê	ë	ì	í	î	ï
Fx	ð	ñ	ò	ó	ô	õ	ö	÷	ø	ù	ú	û	ü	ý	þ	ÿ ++ alphabitics

    UTF-8: 8-32 bits (1-4 bytes). 1,112,064 code points. benst3mlto feh el html for langss bed3am el arabic w many languages kteer
     */
   public static BigInteger fromStringToBigInteger(String s){
     byte [] bytes=s.getBytes(StandardCharsets.ISO_8859_1);
     return new BigInteger(1,bytes);
}
    public static String fromBigIntegerToString(BigInteger number) {
        byte[] bytes = number.toByteArray(); // Convert BigInteger to a byte array
        return new String(bytes, StandardCharsets.ISO_8859_1); // Convert the byte array to a String

    }
    public BigInteger generatePrivateKey(BigInteger e, BigInteger euler) {
        return e.modInverse(euler); // Calculate the modular multiplicative inverse
    }

    public static BigInteger encrypt(BigInteger m, BigInteger e, BigInteger n) {
        return m.modPow(e, n);
    }
    public static String encryptString(String message, BigInteger e, BigInteger n) {
        BigInteger m = fromStringToBigInteger(message);
        BigInteger encryptedBigInt = encrypt(m, e, n);
        return fromBigIntegerToString(encryptedBigInt);
    }
    public static BigInteger decrypt(BigInteger c, BigInteger d, BigInteger n) {
        return c.modPow(d, n);
    }

    public static String decryptString(String encryptedMessage, BigInteger d, BigInteger n) {
        BigInteger encryptedBigInt = fromStringToBigInteger(encryptedMessage); // Convert encrypted string to BigInteger
        BigInteger decryptedBigInt = decrypt(encryptedBigInt, d, n); // Decrypt the BigInteger
        return fromBigIntegerToString(decryptedBigInt); // Convert the decrypted BigInteger back to a string
    }
    public static void main(String[] args) throws Exception {
        Scanner sc;
        String filename = "message.txt";
        String message;

        // Read the message from the file
        try {
            File file = new File(filename);
            sc = new Scanner(file);

            message = sc.nextLine(); // Read the first word from the file
            System.out.println("Message from file: " + message);
        } catch (IOException e) {
            System.err.println("Error reading the file: " + filename);
            e.printStackTrace();
            return;
        }

        // Prompt the user for the modulus bit length
        System.out.println("Enter the number of bits for the modulus (minimum 256 bits): ");
        sc = new Scanner(System.in);
        int bits = sc.nextInt();
        while (bits < 256) {
            System.out.println("Invalid input. Please enter a number greater than or equal to 256: ");
            bits = sc.nextInt();
        }

        // Create an instance of the RSA class
        RSA rsa = new RSA();

        // Generate the public and private keys
        BigInteger[] keys = rsa.generateModSystem(bits);
        BigInteger p = keys[0]; // First prime number
        BigInteger q = keys[1]; // Second prime number
        BigInteger n = keys[2]; // Modulus n = p * q

        // Display the generated primes and modulus
        System.out.println("Generated prime p: " + p);
        System.out.println("Generated prime q: " + q);
        System.out.println("Modulus n (p * q): " + n);

        // Display the bit lengths of p, q, and n
        System.out.println("Bit length of p: " + p.bitLength());
        System.out.println("Bit length of q: " + q.bitLength());
        System.out.println("Bit length of n: " + n.bitLength());

        // Calculate Euler's totient
        BigInteger euler = rsa.EulersTotient(p, q);
        System.out.println("Euler's totient (φ(n)): " + euler);

        // Generate the public key
        BigInteger e = rsa.generatePublicKey(euler);
        System.out.println("Generated public key (e) in plaintext: " + fromBigIntegerToString(e));
        System.out.println("Generated public key (e) in BigInteger: " + e);

        // Generate the private key
        BigInteger d = rsa.generatePrivateKey(e, euler);
        System.out.println("Generated private key (d) in plaintext: " + fromBigIntegerToString(d));
        System.out.println("Generated private key (d) in BigInteger: " + d);

        System.out.println("/////////////// Encryption and Decryption Test ///////////////");

        // Test encryption and decryption with a BigInteger
        BigInteger bp = fromStringToBigInteger(message); // Convert the message to BigInteger

        // Encrypt and decrypt the message from the file
        String encryptedMessage = encryptString(message, e, n);
        BigInteger encryptedInteger = encrypt(bp, e, n);
        System.out.println("Encrypted Message: " + encryptedMessage);
        System.out.println("Encrypted Integer: " + encryptedInteger);

        String decryptedMessage = decryptString(encryptedMessage, d, n);
        BigInteger decryptedInteger = decrypt(encryptedInteger, d, n);
        System.out.println("Decrypted Message: " + decryptedMessage);
        System.out.println("Decrypted Integer: " + decryptedInteger);

        // Write the encrypted message to encryptedRSA.txt
        try (FileWriter encryptedWriter = new FileWriter("encryptedRSA.txt")) {
            encryptedWriter.write("Encrypted Message: " + encryptedMessage + "\n");
            encryptedWriter.write("Encrypted Integer: " + encryptedInteger + "\n");
        } catch (IOException ex) {
            System.err.println("Error writing to encryptedRSA.txt");
            ex.printStackTrace();
        }

        // Write the decrypted message to decryptedRSA.txt
        try (FileWriter decryptedWriter = new FileWriter("decryptedRSA.txt")) {
            decryptedWriter.write("Decrypted Message: " + decryptedMessage + "\n");
            decryptedWriter.write("Decrypted Integer: " + decryptedInteger + "\n");
        } catch (IOException ex) {
            System.err.println("Error writing to decryptedRSA.txt");
            ex.printStackTrace();
        }

        // Verify the encryption and decryption
        if (message.equals(decryptedMessage) && bp.equals(decryptedInteger)) {
            System.out.println("Encryption and Decryption successful!");
        } else {
            System.out.println("Encryption and Decryption failed.");
        }

        // Close the scanner to release resources
        sc.close();
        System.out.println("End of program.");
    }
}
