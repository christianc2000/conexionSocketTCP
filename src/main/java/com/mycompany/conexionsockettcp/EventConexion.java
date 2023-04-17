/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.conexionsockettcp;

import java.net.Socket;
import java.util.EventObject;

/**
 *
 * @author Christian
 */
public class EventConexion extends EventObject{
    Socket socket;

    public EventConexion(Socket socket, Object source) {
        super(source);
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

   
}
