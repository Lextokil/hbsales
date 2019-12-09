package br.com.hbsis.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Configuration
public class DateValidator {

    public static boolean isThisDateValid(String dateToValidate, String dateFromat){

        if(dateToValidate == null){
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
        sdf.setLenient(false);

        try {

            //if not valid, it will throw ParseException
            Date date = sdf.parse(dateToValidate);
            System.out.println(date);

        } catch (ParseException e) {

            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static LocalDateTime convertToLocalDateTime(String dateToConvert){
        if(isThisDateValid(dateToConvert, "dd-MM-yyyy")){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");

            LocalDateTime localDate = LocalDateTime.parse(dateToConvert, formatter);

            System.out.println(localDate);

            System.out.println(formatter.format(localDate));
            return localDate;
        }else{
            return null;
        }

    }

    public  static String convertDateToString(LocalDateTime dateToConvert){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String formatDateTime = dateToConvert.format(formatter);

        return formatDateTime;
    }

}