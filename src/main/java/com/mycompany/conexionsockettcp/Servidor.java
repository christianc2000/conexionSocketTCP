/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.conexionsockettcp;

/**
 *
 * @author Christian
 */
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor implements InterfaceSocketListener {

    int port;
    ServerSocket server;
    private Map<String, Map<String, Object>> Clientes = new HashMap<>();

    public Servidor(int port) {
        this.port = port;
    }

    public void iniciar() {
        try {
            // Crear un socket de servidor en el puerto especificado
            server = new ServerSocket(port);
            System.out.println("Servidor: Esperando conexiones de clientes...");
            HiloPingCliente hpc = new HiloPingCliente(Clientes);
            hpc.addMyEventListener(this);
            hpc.start();

            HiloConexion hc = new HiloConexion(server);
            hc.addMyEventListener(this);
            hc.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para generar un identificador único para el cliente
    private String generarClienteId() {
        return UUID.randomUUID().toString(); // Utilizar la clase UUID para generar un identificador único
    }
    //Agregar cliente a la lista con HashMap

    public void agregarCliente(String clienteId, String nombre, Socket socket) {
        Map<String, Object> atributosCliente = new HashMap<>(); // HashMap para almacenar los atributos del cliente
        atributosCliente.put("nombre", nombre);
        atributosCliente.put("socket", socket);
        Clientes.put(clienteId, atributosCliente); // Agregar el cliente al mapa usando el identificador único como clave
    }

    //***********************************************************************************************
    @Override
    public void nuevoClienteConectado(EventConexion evento) {
        Socket socket = evento.getSocket();
        try {
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            salida.println(generarClienteId());//Envía el ID al cliente
//**********solicita nombre al cliente
            salida.println("Por favor, ingrese su nombre:");
            String key = in.readLine();//Espera que el cliente mande su nombre
//********agregar cliente a la lista hashmap
            String[] parte = key.split(",");//El cliente manda el ID,Nombre, separar en un vector
            salida.println("Servidor: Conexión exitosa");
            System.out.println("se conectó el cliente con id: " + parte[0] + " y nombre: " + parte[1]);
            agregarCliente(parte[0], parte[1], socket);//ID,Nombre, Socket
//********Inicializar Hilo mensaje
            HiloMensaje hm = new HiloMensaje(socket);//En donde vamos a leer lo que el cliente nos envía, también vamos a enviar una salida al Cliente
            hm.addMyEventListener(this);
            hm.start();
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * @param evento
     */
    @Override
    public void ClienteDesconectado(EventDesconexion evento){
       
        Map<String, Object> cliente =Clientes.remove(evento.getKey());
        String mensaje=(String) cliente.get("nombre")+" abandono la conversación";
        EnviarMensajes(mensaje, evento.getKey());
        System.out.println("cliente "+evento.getKey()+"."+cliente.get("nombre")+" está desconectado");
        System.out.println("**************************************************************************");
        if(Clientes.size()==0){
            System.out.println("¡NINGÚN CLIENTE ESTÁ CONECTADO!");
        }else{
         System.out.println("TIENES "+Clientes.size()+" CONECTADOS");   
        }
        
    }
    
    public void leerMensaje(EventMensaje evento) {
        String parte[] = evento.getMensaje().split(",");
        Map<String, Object> cliente = Clientes.get(parte[0]);
        System.out.println("mensaje nuevo de <" + parte[0] + ", " + cliente.get("nombre") + "> : " + parte[1]);
        String mensaje=(String) cliente.get("nombre")+": "+parte[1];
      //  System.out.println("mensaje: "+mensaje);
        EnviarMensajes(mensaje, parte[0]);
    }

    public void EnviarMensajes(String mensaje, String clienteId) {
        for (Map.Entry<String, Map<String, Object>> entry : Clientes.entrySet()) {
            String key = entry.getKey(); // Obtener la clave del mapa exterior
            if (key.equals(clienteId) != true) {
                System.out.println(entry);
                Map<String, Object> valor = entry.getValue();
                Socket socket = (Socket) valor.get("socket");
                try {
                    PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
                    salida.println(mensaje);
                } catch (IOException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
