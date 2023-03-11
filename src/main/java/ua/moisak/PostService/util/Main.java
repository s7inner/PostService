package ua.moisak.PostService.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Main {
    public static void main(String[] args) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        BCryptPasswordEncoder encoder2 = new BCryptPasswordEncoder();

        System.out.println("encoder:  " + encoder.encode("admin"));
        System.out.println("encoder:  " + encoder.encode("onlyForView"));

//        String encode1 = encoder.encode("user300");
//        String encode2 = encoder2.encode("user300");
//
//        System.out.println("user300 - 1 " + encode1);
//        System.out.println("user300 - 2 " + encode2);

//        boolean result = encoder2.matches("user300", encode1);
//        System.out.println("result " + result);



        if (encoder.matches("admin", "$2a$10$xsEoIz3CzP13yf6L631TUOdqh2rn/Q8FoKI3uxWJk2jHN/IZyVhtS")) {
            System.out.println("encoder: true");
        }

        if (encoder.matches("admin", "$2a$10$T7UyAAXFk95SeEhAhzz9f.bobHGS1uWPSEmgrutSU3pVVjco90yAW")) {
            System.out.println("encoder: true");
        }

    }

}
