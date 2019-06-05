package com.ml.util.data.pojo;

public enum ApartmentMaterial {
    TYPE_1("Кирпич", "1"),
    TYPE_2("Керамзито-бетон", "2"),
    TYPE_3("Монолит", "3"),
    TYPE_4("Панель", "4"),
    TYPE_5("Керамоблок", "5"),
    TYPE_6("Пеноблок", "6"),
    TYPE_7("Газоблок", "7"),
    TYPE_8("null", "null"),
    TYPE_9("Ракушняк", "9"),
    TYPE_10("Пеноблок/кирпич", "10"),
    TYPE_11("Комбинир.", "11");

    private String name;
    private String code;

    ApartmentMaterial(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public static String getCode(String name) {
        for (ApartmentMaterial value : values()) {
            if (name.equals(value.name)) {
                return value.code;
            }
        }
        throw new RuntimeException("There are no apartment materials with name " + name);
    }
}
