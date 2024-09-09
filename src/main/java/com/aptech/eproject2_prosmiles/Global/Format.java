package com.aptech.eproject2_prosmiles.Global;

import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Format {
    public static String formatDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return date.format(formatter);
    }

//    get pwHash in current PC
    public static String hashResult(String pw){
        return BCrypt.hashpw(pw, BCrypt.gensalt());
    }

}
