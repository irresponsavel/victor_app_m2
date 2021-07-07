package com.example.unccrudapp.model;

public class Store {
    // realizar a implementação da classe
    // de acordo com o model da API

    String nome = "";
    String endereco = "";
    String estado = "";
    String cidade = "";
    String cnpj = "";
    String id;

    public Store(String id, String nome, String endereco, String estado, String cidade, String cnpj) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.estado = estado;
        this.cidade = cidade;
        this.cnpj = cnpj;
    }
    public Store(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

}
