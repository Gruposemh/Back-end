-- Script para alterar a coluna imagem na tabela tb_evento para LONGTEXT
-- Execute este script no seu banco de dados MySQL

ALTER TABLE tb_evento 
MODIFY COLUMN imagem LONGTEXT;

