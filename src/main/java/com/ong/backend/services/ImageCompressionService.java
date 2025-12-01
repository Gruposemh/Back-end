package com.ong.backend.services;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ImageCompressionService {

    private static final int MAX_WIDTH = 1920;
    private static final int MAX_HEIGHT = 1920;
    private static final double QUALITY = 0.85; // 85% de qualidade

    /**
     * Comprime uma imagem mantendo a qualidade visual mas reduzindo o tamanho do arquivo
     * 
     * @param file Arquivo de imagem original
     * @return Array de bytes da imagem comprimida
     * @throws IOException Se houver erro na compressão
     */
    public byte[] compressImage(MultipartFile file) throws IOException {
        // Ler a imagem original
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        
        if (originalImage == null) {
            throw new IOException("Não foi possível ler a imagem");
        }

        // Obter dimensões originais
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        // Calcular novas dimensões mantendo a proporção
        int newWidth = originalWidth;
        int newHeight = originalHeight;

        if (originalWidth > MAX_WIDTH || originalHeight > MAX_HEIGHT) {
            double widthRatio = (double) MAX_WIDTH / originalWidth;
            double heightRatio = (double) MAX_HEIGHT / originalHeight;
            double ratio = Math.min(widthRatio, heightRatio);

            newWidth = (int) (originalWidth * ratio);
            newHeight = (int) (originalHeight * ratio);
        }

        // Comprimir a imagem
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        Thumbnails.of(originalImage)
                .size(newWidth, newHeight)
                .outputQuality(QUALITY)
                .outputFormat(getImageFormat(file.getOriginalFilename()))
                .toOutputStream(outputStream);

        return outputStream.toByteArray();
    }

    /**
     * Comprime uma imagem a partir de um array de bytes
     * 
     * @param imageBytes Array de bytes da imagem original
     * @param originalFilename Nome do arquivo original para determinar o formato
     * @return Array de bytes da imagem comprimida
     * @throws IOException Se houver erro na compressão
     */
    public byte[] compressImage(byte[] imageBytes, String originalFilename) throws IOException {
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
        
        if (originalImage == null) {
            throw new IOException("Não foi possível ler a imagem");
        }

        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        int newWidth = originalWidth;
        int newHeight = originalHeight;

        if (originalWidth > MAX_WIDTH || originalHeight > MAX_HEIGHT) {
            double widthRatio = (double) MAX_WIDTH / originalWidth;
            double heightRatio = (double) MAX_HEIGHT / originalHeight;
            double ratio = Math.min(widthRatio, heightRatio);

            newWidth = (int) (originalWidth * ratio);
            newHeight = (int) (originalHeight * ratio);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        Thumbnails.of(originalImage)
                .size(newWidth, newHeight)
                .outputQuality(QUALITY)
                .outputFormat(getImageFormat(originalFilename))
                .toOutputStream(outputStream);

        return outputStream.toByteArray();
    }

    /**
     * Obtém o formato da imagem baseado no nome do arquivo
     * 
     * @param filename Nome do arquivo
     * @return Formato da imagem (jpg, png, etc.)
     */
    private String getImageFormat(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "jpg";
        }
        
        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        
        // Converter alguns formatos para formatos mais eficientes
        switch (extension) {
            case "jpeg":
                return "jpg";
            case "png":
            case "gif":
            case "bmp":
                return extension;
            default:
                return "jpg";
        }
    }

    /**
     * Calcula a taxa de compressão alcançada
     * 
     * @param originalSize Tamanho original em bytes
     * @param compressedSize Tamanho comprimido em bytes
     * @return Percentual de redução
     */
    public double getCompressionRatio(long originalSize, long compressedSize) {
        return ((double) (originalSize - compressedSize) / originalSize) * 100;
    }
}
