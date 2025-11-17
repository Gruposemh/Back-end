-- Script para alterar a coluna urlNoticia na tabela tb_blog de LONGTEXT para VARCHAR(500)
-- Isso melhora significativamente a performance, pois URLs são pequenas strings
-- e não precisam de LONGTEXT que é otimizado para textos muito grandes

ALTER TABLE tb_blog
MODIFY COLUMN urlNoticia VARCHAR(500);

-- Nota: Se houver dados base64 antigos muito grandes, eles podem ser truncados.
-- Execute este script apenas após garantir que todas as imagens foram migradas para URLs.

