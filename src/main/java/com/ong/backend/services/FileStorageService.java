package com.ong.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Value("${server.url:http://localhost:8080}")
    private String serverUrl;

    @Autowired
    private ImageCompressionService imageCompressionService;

    public String storeFile(MultipartFile file) throws IOException {
        // Validar arquivo
        if (file.isEmpty()) {
            throw new IOException("Arquivo vazio");
        }

        // Validar tipo de arquivo
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IOException("Apenas imagens são permitidas");
        }

        // Validar tamanho (máximo 10MB antes da compressão)
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IOException("Arquivo muito grande. Máximo 10MB");
        }

        // Comprimir a imagem
        byte[] compressedImage = imageCompressionService.compressImage(file);
        
        // Log da compressão
        long originalSize = file.getSize();
        long compressedSize = compressedImage.length;
        double compressionRatio = imageCompressionService.getCompressionRatio(originalSize, compressedSize);
        System.out.println(String.format("Imagem comprimida: %d KB -> %d KB (%.1f%% de redução)", 
            originalSize / 1024, compressedSize / 1024, compressionRatio));

        // Gerar nome único
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String filename = UUID.randomUUID().toString() + extension;

        // Criar diretório se não existir
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Salvar arquivo comprimido
        Path filePath = uploadPath.resolve(filename);
        Files.write(filePath, compressedImage);

        // Retornar URL do arquivo
        return serverUrl + "/uploads/" + filename;
    }

    public void deleteFile(String fileUrl) {
        try {
            if (fileUrl != null && fileUrl.contains("/uploads/")) {
                String filename = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
                Path filePath = Paths.get(uploadDir).resolve(filename);
                Files.deleteIfExists(filePath);
            }
        } catch (IOException e) {
            // Log error but don't throw
            System.err.println("Erro ao deletar arquivo: " + e.getMessage());
        }
    }
}
