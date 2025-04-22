package com.studiomuda.estoque.model;

import java.sql.Date;

public class Pedido {
    private int id;
    private Date dataRequisicao;
    private Date dataEntrega;
    private int clienteId;
    private String clienteNome; // Campo auxiliar para exibição

    public Pedido() {}

    public Pedido(int id, Date dataRequisicao, Date dataEntrega, int clienteId) {
        this.id = id;
        this.dataRequisicao = dataRequisicao;
        this.dataEntrega = dataEntrega;
        this.clienteId = clienteId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDataRequisicao() {
        return dataRequisicao;
    }

    public void setDataRequisicao(Date dataRequisicao) {
        this.dataRequisicao = dataRequisicao;
    }

    public Date getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(Date dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }
    
    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }

    @Override
    public String toString() {
        return "Pedido #" + id +
                " | Requisiu00e7u00e3o: " + dataRequisicao +
                " | Entrega: " + dataEntrega +
                " | Cliente ID: " + clienteId;
    }
}
