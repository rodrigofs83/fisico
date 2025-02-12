/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 *
 * @author POSITIVO
 */
public class DateSystem {

    public static LocalDate getDateSystem() {
        LocalDate dataAtual = LocalDate.now();
        return dataAtual;
    }

    public static int getDia(LocalDate date) {
        int dia = date.getDayOfMonth();
        return dia;
    }

    public static int getMes(LocalDate date) {
        int mes = date.getMonthValue();
        return mes;
    }

    public static int getAno(LocalDate date) {
        int ano = date.getYear();
        return ano;
    }

    public static int ultimoDiaDoMes(LocalDate date) {
        int maxDayOfMonth = date.lengthOfMonth();
        return maxDayOfMonth;
    }
    //data vencida 

    public static boolean isVencido(LocalDate date) {
        LocalDate hoje = LocalDate.now();
        return date.isBefore(hoje);
    }

    //vencimeto na data de hoje 
    public static boolean isVencendoHoje(LocalDate date) {
        LocalDate hoje = LocalDate.now();
        return date.equals(hoje);
    }

    public static LocalDate atualizarParaProximoMes(LocalDate date) {
        return date.plusMonths(1);
    }

    public static LocalDate retrocedeMes(LocalDate date) {
        return date.minusMonths(1);
    }

    public static long diasAteVencimento(LocalDate date) {
        LocalDate hoje = LocalDate.now();
        return ChronoUnit.DAYS.between(hoje, date);
    }

    public static LocalDate converteDateParalocalDate(Date date) {
       
       // Converter o objeto Date para Instant
        Instant instant = date.toInstant();
        
        // Obter o fuso horário do sistema. Usar ZoneId.systemDefault() para obter.
        ZoneId zoneId = ZoneId.systemDefault();

        // Converter Instant para LocalDate
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();
        return localDate;
    }

    public static Date convertLocalDateToDate(LocalDate localDate) {
        // Converter LocalDate para LocalDateTime
        LocalDateTime localDateTime = localDate.atStartOfDay();
        // Obter o fuso horário do sistema. Usar ZoneId.systemDefault() para obter.
        ZoneId zoneId = ZoneId.systemDefault();
        // Converter LocalDateTime para Instant
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        // Converter Instant para Date
        return Date.from(instant);
    }
    public static Date convertSQLDateToDate(Date date) {
           Date data = new java.util.Date(date.getTime());
           return data;
    }
}
