package com.shopersa.backend_shopersa.MS_Producto.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;


@Service
public class ImageStorageService {

    private final Path rootDir;
    private static final Set<String> ALLOWED_EXT = Set.of(".png", ".jpg", ".jpeg", ".webp");

    public ImageStorageService(
            @Value("${app.uploads.products-dir:uploads/products}") String dir) throws IOException {
        this.rootDir = Paths.get(dir).toAbsolutePath().normalize();
        Files.createDirectories(this.rootDir);
    }


    public String saveForProduct(MultipartFile file, Long productId) throws IOException {
        if (file == null || file.isEmpty()) throw new IOException("Archivo vacío");
        if (productId == null) throw new IOException("Producto inválido");
        if (file.getSize() <= 0) throw new IOException("Tamaño inválido");

        String raw = file.getOriginalFilename();
        String original = StringUtils.cleanPath(raw == null ? "file" : raw);
        String ext = "";
        int dot = original.lastIndexOf('.');
        if (dot != -1) ext = original.substring(dot).toLowerCase();
        if (!ALLOWED_EXT.contains(ext)) throw new IOException("Formato no permitido: " + ext);

        String filename = UUID.randomUUID().toString() + ext;
        Path productDir = this.rootDir.resolve(String.valueOf(productId)).normalize();
        Files.createDirectories(productDir);
        Path target = productDir.resolve(filename).normalize();

        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return "/uploads/" + productDir.getFileName().toString() + "/" + filename;
    }


    public void deleteByUrl(String url) throws IOException {
        if (url == null || url.isBlank()) return;
        int idx = url.lastIndexOf('/');
        String name = (idx >= 0 ? url.substring(idx + 1) : url);
        String[] parts = url.split("/");
        String productId = null;
        for (int i = 0; i < parts.length - 1; i++) {
            if ("uploads".equals(parts[i]) && (i + 2) < parts.length) {
                productId = parts[i + 2];
                break;
            }
        }
        Path base = this.rootDir;
        if (productId != null && !productId.isBlank()) {
            base = base.resolve(productId);
        }
        Path target = base.resolve(name).normalize();
        if (Files.exists(target)) {
            Files.delete(target);
        }
    }
}
