package enums

enum FeeStatusEnum {

    Iniciado ("Iniciado"),
    Parcial ("Parcial"),
    Pagado ("Pagado"),
    Trasladado ("Trasladado"),

    final String value
    FeeStatusEnum(String value){ this.value = value }

    String toString(){ value }
    String getKey() { name() }

}