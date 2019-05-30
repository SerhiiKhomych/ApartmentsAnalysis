package com.ml.util.data.pojo;

public enum ApartmentAge {
    TYPE_1("спецпроект 2006-2010 гг.", "11"),
    TYPE_2("спецпроект 2016-2020 гг.", "1"),
    TYPE_3("96 серия", "23"),
    TYPE_4("спецпроект 2011-2015 гг.", "6"),
    TYPE_5("60-70-х гг.", "54"),
    TYPE_6("серия КТУ", "22"),
    TYPE_7("спецпроект 70-х", "44"),
    TYPE_8("спецпроект 2000-2005 гг.", "16"),
    TYPE_9("Доревол.", "110"),
    TYPE_10("Сталинка", "81"),
    TYPE_11("АППС-люкс", "26"),
    TYPE_12("серия АППС", "26"),
    TYPE_13("серия КТ", "26"),
    TYPE_14("Хрущевский проект", "62"),
    TYPE_15("спецпроект 80-х", "34"),
    TYPE_16("спецпроект 1990-1999 гг.", "25"),
    TYPE_17("чешский проект", "39"),
    TYPE_18("серия Т4", "24"),
    TYPE_19("1-464 серия", "64"),
    TYPE_20("\"коттедж. тип\"", "null"),
    TYPE_21("реконстр.", "null"),
    TYPE_22("134 серия", "32"),
    TYPE_23("серия КП", "34"),
    TYPE_24("Отельного типа", "null"),
    TYPE_25("серия БПС", "42"),
    TYPE_26("серия КС", "24"),
    TYPE_27("серия Т1", "34"),
    TYPE_28("совмин", "34"),
    TYPE_29("серия ЕС", "42"),
    TYPE_30("null", "null"),
    TYPE_31("серия Т2", "34"),
    TYPE_32("30-х гг.", "84"),
    TYPE_33("40-х гг.", "74"),
    TYPE_34("50-х гг.", "64"),
    TYPE_35("Клубный дом", "null"),
    TYPE_36("неж. здание", "null");

    private String name;
    private String age;

    ApartmentAge(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public static String getAge(String name) {
        for (ApartmentAge value : values()) {
            if (name.equals(value.name)) {
                return value.age;
            }
        }
        throw new RuntimeException("There are no apartment types with name " + name);
    }
}
