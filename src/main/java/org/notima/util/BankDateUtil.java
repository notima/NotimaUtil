package org.notima.util;

import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BankDateUtil {
    private static String countryCode;
    protected static String startDate;
    protected static int days;
    private static LocalDate returnDate;


    //args should be for example {"SE", "2023-01-01", "10"} where SE is the country code, 2023-01-01 is the starting date
    //and 10 is the how many bankdays that should be added to the start date.
    public static LocalDate bankDateCalculator(String[] args){

        readArgs(args);

        switch (countryCode){
            case "SE":
                BankDateUtil_SE bdu_SE = new BankDateUtil_SE(startDate, days);
                bdu_SE.calculateDate();
                break;
            default:
                System.out.println("Not a valid country code");
                break;
        }
        return returnDate;
    }

	private static void readArgs(String[] args) {
		
        if (args == null || args.length != 3) {
            System.out.println("Can only have and must have 3 arguments");
            System.exit(1);
        }
    
        countryCode = args[0];
        startDate = args[1];
        days = Integer.parseInt(args[2]);

	}

    protected static void calculateNewDate(List<String> holidays) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate initialDate = LocalDate.parse(startDate, formatter);
            LocalDate date = LocalDate.from(initialDate);
            int i = 1;
            while (i <= days) {
                date = date.plusDays(1);
                for (String holiday : holidays) {
                    boolean isHoliday = isHoliday(holiday, date, formatter);
                    Boolean isWeekend = isWeekend(date);
                    if (isHoliday || isWeekend) {
                        i--;
                        break;
                    }
                }
                i++;
            }
            System.out.println("New date after " + days + " bankdays: " + date.format(formatter));
            returnDate = date;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isHoliday(String holiday, LocalDate date, DateTimeFormatter formatter) {

        boolean movableHoliday = false;
        if (holiday.charAt(0) != 'x') {
            LocalDate dateCheck = LocalDate.parse(holiday, formatter);
            movableHoliday = date.equals(dateCheck);
        }

        boolean setHoliday = (holiday.charAt(0) == 'x')
                && (date.getMonthValue() == Integer.parseInt(holiday.substring(5, 7)))
                && (date.getDayOfMonth() == Integer.parseInt(holiday.substring(8, 10)));
        
        if (movableHoliday || setHoliday) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isWeekend(LocalDate dt) {
        switch(dt.getDayOfWeek()) {
            case SATURDAY:
                return true;
            case SUNDAY:
                return true;
            default:
                return false;
        }
    }
}


class BankDateUtil_SE extends BankDateUtil {
    private List<String> bankDays;

    protected BankDateUtil_SE(String startDate, int days){
        bankDays = new ArrayList<>();
        makeBankDaysList();
    }

    protected void calculateDate() {
        calculateNewDate(bankDays);
    }

    protected void makeBankDaysList(){
        // Fasta helgdagar
        bankDays.add("xxxx-01-01");
        bankDays.add("xxxx-01-06");
        bankDays.add("xxxx-05-01");
        bankDays.add("xxxx-06-06");
        bankDays.add("xxxx-12-24");
        bankDays.add("xxxx-12-25");
        bankDays.add("xxxx-12-26");
        bankDays.add("xxxx-12-31");
        //rörliga dagar 2023
        bankDays.add("2023-04-07");
        bankDays.add("2023-04-10");
        bankDays.add("2023-05-18");
        bankDays.add("2023-06-23");
        //rörliga dagar 2024
        bankDays.add("2024-03-29");
        bankDays.add("2024-04-01");
        bankDays.add("2024-05-09");
        bankDays.add("2024-06-21");
        //rörliga dagar 2025
        bankDays.add("2025-04-18");
        bankDays.add("2025-04-21");
        bankDays.add("2025-05-29");
        bankDays.add("2025-06-20");
        //rörliga dagar 2026
        bankDays.add("2026-04-03");
        bankDays.add("2026-04-06");
        bankDays.add("2026-05-14");
        bankDays.add("2026-06-19");
        //rörliga dagar 2027
        bankDays.add("2027-03-26");
        bankDays.add("2027-03-29");
        bankDays.add("2027-05-06");
        bankDays.add("2027-06-25");
        //rörliga dagar 2028
        bankDays.add("2028-04-14");
        bankDays.add("2028-04-17");
        bankDays.add("2028-05-25");
        bankDays.add("2028-06-23");
        //rörliga dagar 2029
        bankDays.add("2029-03-30");
        bankDays.add("2029-04-02");
        bankDays.add("2029-05-10");
        bankDays.add("2029-06-22");
        //rörliga dagar 2030
        bankDays.add("2030-04-19");
        bankDays.add("2030-03-22");
        bankDays.add("2030-05-30");
        bankDays.add("2030-06-21");
        //rörliga dagar 2031
        bankDays.add("2031-04-11");
        bankDays.add("2031-04-14");
        bankDays.add("2031-05-22");
        bankDays.add("2031-06-20");
        //rörliga dagar 2032
        bankDays.add("2032-04-26");
        bankDays.add("2032-04-29");
        bankDays.add("2032-05-06");
        bankDays.add("2032-06-25");
        //rörliga dagar 2033
        bankDays.add("2033-04-15");
        bankDays.add("2033-04-18");
        bankDays.add("2033-05-26");
        bankDays.add("2033-06-24");
        //rörliga dagar 2034
        bankDays.add("2034-04-07");
        bankDays.add("2034-04-10");
        bankDays.add("2034-05-18");
        bankDays.add("2034-06-23");
        //rörliga dagar 2035
        bankDays.add("2035-03-23");
        bankDays.add("2035-03-26");
        bankDays.add("2035-05-03");
        bankDays.add("2035-06-22");
        //rörliga dagar 2036
        bankDays.add("2036-04-11");
        bankDays.add("2036-04-14");
        bankDays.add("2036-05-22");
        bankDays.add("2036-06-20");
        //rörliga dagar 2037
        bankDays.add("2037-04-03");
        bankDays.add("2037-04-06");
        bankDays.add("2037-05-14");
        bankDays.add("2037-06-19");
        //rörliga dagar 2038
        bankDays.add("2038-04-23");
        bankDays.add("2038-04-26");
        bankDays.add("2038-06-03");
        bankDays.add("2038-06-25");
        //rörliga dagar 2039
        bankDays.add("2039-04-08");
        bankDays.add("2039-04-11");
        bankDays.add("2039-05-19");
        bankDays.add("2039-06-24");
        //rörliga dagar 2040
        bankDays.add("2040-03-30");
        bankDays.add("2040-04-02");
        bankDays.add("2040-05-10");
        bankDays.add("2040-06-22");
        //rörliga dagar 2041
        bankDays.add("2034-04-19");
        bankDays.add("2034-04-22");
        bankDays.add("2034-05-30");
        bankDays.add("2034-06-21");
        //rörliga dagar 2042
        bankDays.add("2042-04-04");
        bankDays.add("2042-04-07");
        bankDays.add("2042-05-15");
        bankDays.add("2042-06-20");
        //rörliga dagar 2043
        bankDays.add("2043-03-27");
        bankDays.add("2043-03-30");
        bankDays.add("2043-05-07");
        bankDays.add("2043-06-19");
        //rörliga dagar 2044
        bankDays.add("2044-04-15");
        bankDays.add("2044-04-18");
        bankDays.add("2044-05-26");
        bankDays.add("2044-06-24");
        //rörliga dagar 2045
        bankDays.add("2045-04-07");
        bankDays.add("2045-04-10");
        bankDays.add("2045-05-18");
        bankDays.add("2045-06-23");
        //rörliga dagar 2046
        bankDays.add("2046-03-23");
        bankDays.add("2046-03-26");
        bankDays.add("2046-05-03");
        bankDays.add("2046-06-22");
        //rörliga dagar 2047
        bankDays.add("2047-04-12");
        bankDays.add("2047-04-15");
        bankDays.add("2047-05-23");
        bankDays.add("2047-06-21");
        //rörliga dagar 2048
        bankDays.add("2048-04-03");
        bankDays.add("2048-04-06");
        bankDays.add("2048-05-14");
        bankDays.add("2048-06-19");
        //rörliga dagar 2049
        bankDays.add("2049-04-16");
        bankDays.add("2049-04-19");
        bankDays.add("2049-05-27");
        bankDays.add("2049-06-25");
        //rörliga dagar 2050
        bankDays.add("2050-04-08");
        bankDays.add("2050-04-11");
        bankDays.add("2050-05-19");
        bankDays.add("2050-06-24");
        //rörliga dagar 2051
        bankDays.add("2051-03-31");
        bankDays.add("2051-04-03");
        bankDays.add("2051-05-11");
        bankDays.add("2051-06-23");
        //rörliga dagar 2052
        bankDays.add("2052-04-19");
        bankDays.add("2052-04-22");
        bankDays.add("2052-05-30");
        bankDays.add("2052-06-21");
        //rörliga dagar 2053
        bankDays.add("2053-04-04");
        bankDays.add("2053-04-07");
        bankDays.add("2053-05-15");
        bankDays.add("2053-06-20");
        //rörliga dagar 2054
        bankDays.add("2054-03-27");
        bankDays.add("2054-03-30");
        bankDays.add("2054-05-07");
        bankDays.add("2054-06-19");
        //rörliga dagar 2055
        bankDays.add("2055-04-16");
        bankDays.add("2055-04-19");
        bankDays.add("2055-05-27");
        bankDays.add("2055-06-25");
        //rörliga dagar 2056
        bankDays.add("2056-03-31");
        bankDays.add("2056-04-03");
        bankDays.add("2056-05-11");
        bankDays.add("2056-06-23");
        //rörliga dagar 2057
        bankDays.add("2057-04-20");
        bankDays.add("2057-04-23");
        bankDays.add("2057-05-31");
        bankDays.add("2057-06-22");
        //rörliga dagar 2058
        bankDays.add("2058-04-12");
        bankDays.add("2058-04-15");
        bankDays.add("2058-05-23");
        bankDays.add("2058-06-21");
        //rörliga dagar 2059
        bankDays.add("2059-03-28");
        bankDays.add("2059-03-31");
        bankDays.add("2059-05-08");
        bankDays.add("2059-06-20");
        //rörliga dagar 2060
        bankDays.add("2060-04-16");
        bankDays.add("2060-04-19");
        bankDays.add("2060-05-27");
        bankDays.add("2060-06-25");
        //rörliga dagar 2061
        bankDays.add("2061-04-08");
        bankDays.add("2061-04-11");
        bankDays.add("2061-05-19");
        bankDays.add("2061-06-24");
        //rörliga dagar 2062
        bankDays.add("2062-03-24");
        bankDays.add("2062-03-27");
        bankDays.add("2062-05-04");
        bankDays.add("2062-06-23");
        //rörliga dagar 2063
        bankDays.add("2063-04-13");
        bankDays.add("2063-04-16");
        bankDays.add("2063-05-24");
        bankDays.add("2063-06-22");
        //rörliga dagar 2064
        bankDays.add("2064-04-07");
        bankDays.add("2064-04-10");
        bankDays.add("2064-05-18");
        bankDays.add("2064-06-20");
        //rörliga dagar 2065
        bankDays.add("2065-03-27");
        bankDays.add("2065-03-30");
        bankDays.add("2065-05-07");
        bankDays.add("2065-06-19");
        //rörliga dagar 2066
        bankDays.add("2066-04-09");
        bankDays.add("2066-04-12");
        bankDays.add("2066-05-20");
        bankDays.add("2066-06-25");
        //rörliga dagar 2067
        bankDays.add("2067-04-01");
        bankDays.add("2067-04-04");
        bankDays.add("2067-05-12");
        bankDays.add("2067-06-24");
        //rörliga dagar 2068
        bankDays.add("2068-04-20");
        bankDays.add("2068-04-23");
        bankDays.add("2068-05-31");
        bankDays.add("2068-06-22");
        //rörliga dagar 2069
        bankDays.add("2069-04-12");
        bankDays.add("2069-04-15");
        bankDays.add("2069-05-23");
        bankDays.add("2069-06-21");
        //rörliga dagar 2070
        bankDays.add("2070-03-28");
        bankDays.add("2070-03-31");
        bankDays.add("2070-05-08");
        bankDays.add("2070-06-20");
        //rörliga dagar 2071
        bankDays.add("2071-04-17");
        bankDays.add("2071-04-20");
        bankDays.add("2071-05-28");
        bankDays.add("2071-06-19");
        //rörliga dagar 2072
        bankDays.add("2072-04-08");
        bankDays.add("2072-04-11");
        bankDays.add("2072-05-19");
        bankDays.add("2072-06-24");
        //rörliga dagar 2073
        bankDays.add("2073-03-24");
        bankDays.add("2073-03-27");
        bankDays.add("2073-05-04");
        bankDays.add("2073-06-23");
        //rörliga dagar 2074
        bankDays.add("2074-04-13");
        bankDays.add("2074-04-16");
        bankDays.add("2074-05-24");
        bankDays.add("2074-06-22");
        //rörliga dagar 2075
        bankDays.add("2075-04-05");
        bankDays.add("2075-04-08");
        bankDays.add("2075-05-16");
        bankDays.add("2075-06-21");
    }
}
