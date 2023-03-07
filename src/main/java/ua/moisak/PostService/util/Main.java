package ua.moisak.PostService.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Main {
    public static void main(String[] args) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        BCryptPasswordEncoder encoder2 = new BCryptPasswordEncoder();

        System.out.println("encoder:  " + encoder.encode("abel"));
        System.out.println("encoder:  " + encoder.encode("admin"));

        String encode1 = encoder.encode("user300");
        String encode2 = encoder2.encode("user300");

        System.out.println("user300 - 1 " + encode1);
        System.out.println("user300 - 2 " + encode2);

        boolean result = encoder2.matches("user300", encode1);
        System.out.println("result " + result);



        if (encoder.matches("abel", "$2a$10$IAz6WzJ314LH1NXq7Rf.dOYPP2uvzk08g.eAl9l4DRG4YsxavEV4W")) {
            System.out.println("encoder: true");
        }


//        System.out.println("------------华丽的分割线-----------------------");
//        String Md5Password = MD5Util.encode("abel");
//        System.out.println("Md5Password:  " + Md5Password);
//        System.out.println("encoder:  " + encoder.encode(Md5Password));
//        if (encoder.matches(Md5Password, "$2a$10$37MXEfzlbtC6QSsRTlRhIOmykMRJtO5mU8Y.yiJBjy1x4WYWFR5gG")) {
//            System.out.println("Md5Password: true");
//        }

    }

}
