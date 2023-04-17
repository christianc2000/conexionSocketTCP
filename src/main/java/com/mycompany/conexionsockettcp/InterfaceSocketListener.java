/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.conexionsockettcp;

import java.util.EventListener;

/**
 *
 * @author Christian
 */
public interface InterfaceSocketListener extends EventListener{
    void nuevoClienteConectado(EventConexion evento);
    void leerMensaje(EventMensaje evento);
    void ClienteDesconectado(EventDesconexion evento);
}
