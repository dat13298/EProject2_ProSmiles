package com.aptech.eproject2_prosmiles.Global;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Format {
    public static String formatDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return date.format(formatter);
    }

}
