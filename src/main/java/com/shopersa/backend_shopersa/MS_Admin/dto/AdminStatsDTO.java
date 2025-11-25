package com.shopersa.backend_shopersa.MS_Admin.dto;

import java.util.List;

public class AdminStatsDTO {
    private long totalProductos;
    private long totalClientes;
    private long totalVentas;
    private long ofertas;
    private long novedades;
    private List<LowStockItem> lowStock;

    public AdminStatsDTO() {}

    public AdminStatsDTO(long totalProductos, long totalClientes, long totalVentas, long ofertas, long novedades, List<LowStockItem> lowStock) {
        this.totalProductos = totalProductos;
        this.totalClientes = totalClientes;
        this.totalVentas = totalVentas;
        this.ofertas = ofertas;
        this.novedades = novedades;
        this.lowStock = lowStock;
    }

    //getters y setters
    public long getTotalProductos() { return totalProductos; }
    public void setTotalProductos(long totalProductos) { this.totalProductos = totalProductos; }
    public long getTotalClientes() { return totalClientes; }
    public void setTotalClientes(long totalClientes) { this.totalClientes = totalClientes; }
    public long getTotalVentas() { return totalVentas; }
    public void setTotalVentas(long totalVentas) { this.totalVentas = totalVentas; }
    public long getOfertas() { return ofertas; }
    public void setOfertas(long ofertas) { this.ofertas = ofertas; }
    public long getNovedades() { return novedades; }
    public void setNovedades(long novedades) { this.novedades = novedades; }
    public List<LowStockItem> getLowStock() { return lowStock; }
    public void setLowStock(List<LowStockItem> lowStock) { this.lowStock = lowStock; }

    //DTO interno para bajo stock
    public static class LowStockItem {
        private Long id;
        private String nombre;
        private int stock;

        public LowStockItem() {}
        public LowStockItem(Long id, String nombre, int stock) {
            this.id = id; this.nombre = nombre; this.stock = stock;
        }
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public int getStock() { return stock; }
        public void setStock(int stock) { this.stock = stock; }
    }
}