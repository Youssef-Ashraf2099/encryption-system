#include<iostream>
#include<cmath>
#include<ctime>


bool isPrime(int n) {
    if (n <= 1) return false;
    for (int i = 2; i <= sqrt(n); i++) {
        if (n % i == 0) return false;
    }
    return true;
}

int generatePrime(int bits){
    if(bits<256){
        std::cout<<"Invalid bit size. Please enter a value between 256 and 512."<<std::endl;
        return -1;
    }
    while (true)
    {
        int prime = rand() % (1 << bits); // Generate a random number with the specified number of bits
        if (isPrime(prime)) {
            return prime;
        }
    }
    return -1; //ay hagaa
}

int systemModule(int bits) {
    if(bits < 256 ) {
        std::cout << "Invalid bit size. Please enter a value between 256 and 512." << std::endl;
        return -1;
    }
   int p = generatePrime(bits);
    int q = generatePrime(bits);

    while(p == q) { // Ensure p and q are different
        q = generatePrime(bits);
    }
    return p * q; // Return the product of the two primes
}

int Euler(int p, int q) { //ø(n)
    return (p - 1) * (q - 1); // Calculate Euler's totient function
}

// int generatePublicKey(int euler) {
//     int e = 3; // Start with a small prime number
//     while (true) {
//         if (gcd(e, euler) == 1) { // Check if e and ø(n) are coprime
//             return e;
//         }
//         e += 2; // Increment by 2 to keep it odd
//     }
//     return -1; //ay hagaa
// }
