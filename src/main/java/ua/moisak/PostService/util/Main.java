package ua.moisak.PostService.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Main {
    public static void main(String[] args) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        BCryptPasswordEncoder encoder2 = new BCryptPasswordEncoder();

        System.out.println("encoder:  " + encoder.encode("admin"));
        System.out.println("encoder:  " + encoder.encode("admin"));

        System.out.println("encoder:  " + encoder.encode("onlyForView"));

//        String encode1 = encoder.encode("user300");
//        String encode2 = encoder2.encode("user300");
//
//        System.out.println("user300 - 1 " + encode1);
//        System.out.println("user300 - 2 " + encode2);

//        boolean result = encoder2.matches("user300", encode1);
//        System.out.println("result " + result);



        if (encoder.matches("admin", "$2a$10$MHXPpH6Q.RTDwMkoc1khB.07zxAnzcrnK3eAytkb.wTNAAvifMaNW")) {
            System.out.println("encoder: true");
        }

        if (encoder.matches("admin", "$2a$10$Src5grEycLX5m7PJp0B8YupSTaIM5t3tC7Xknl/Hvf9/V6F.wtXnm")) {
            System.out.println("encoder: true");
        }

    }

}
