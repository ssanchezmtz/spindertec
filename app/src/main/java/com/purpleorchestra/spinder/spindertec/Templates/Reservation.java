package com.purpleorchestra.spinder.spindertec.Templates;

/**
 * Created by alichel_6288 on 02/05/16.
 */
public class Reservation {

    public String reservationSport; //Nombre del deporte que se jugara
    public String reservationFacility; //Lugar donde se llevará acabo el partido
    public String reservationScheduleDate; //Día del partido
    public String reservationScheduleTime; //Hora del partido
    public String reservationOpponent; //Nombre del usuario contra quien se jugará el partido

    public Reservation (String reservationSport, String reservationFacility, String reservationScheduleDate,
    String reservationScheduleTime,String reservationOpponent){
        this.reservationSport = reservationSport;
        this.reservationFacility = reservationFacility;
        this.reservationScheduleDate = reservationScheduleDate;
        this.reservationScheduleTime = reservationScheduleTime;
        this.reservationOpponent = reservationOpponent;
    }
}
