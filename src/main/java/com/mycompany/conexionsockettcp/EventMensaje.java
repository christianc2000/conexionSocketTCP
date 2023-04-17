/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.conexionsockettcp;

import java.net.Socket;

/**
 *
 * @author Christian
 */
public class EventMensaje {

    String mensaje;
    
    public EventMensaje(String mensaje) {
        this.mensaje = mensaje;
     
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
