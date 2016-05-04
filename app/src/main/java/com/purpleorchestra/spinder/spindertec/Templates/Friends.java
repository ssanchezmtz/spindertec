package com.purpleorchestra.spinder.spindertec.Templates;

/**
 * Created by Spinder on 03/05/16.
 */
public class Friends {

    public String friendUserId;
    public String friendsFirstName;
    public String friendsLastName;
    public String friendsEmail;

    public Friends() {
        this.friendUserId = "1";
        this.friendsFirstName = "Sergio";
        this.friendsLastName = "Sanchez";
        this.friendsEmail ="ssanchez@accionti.com";

    }

    public Friends(String friendUserId,String friendsFirstName,String friendsLastName,String friendsEmail){
        this.friendUserId = friendUserId;
        this.friendsFirstName = friendsFirstName;
        this.friendsLastName = friendsLastName;
        this.friendsEmail = friendsEmail;
    }
}
