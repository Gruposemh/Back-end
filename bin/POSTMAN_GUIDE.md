# Guia de Testes no Postman - API de Atividades

## 📋 Configuração Inicial

### 1. URL Base
```
http://localhost:8080
```
*(Ajuste a porta se necessário - verifique no application.properties)*

### 2. Configuração de Autenticação
O sistema usa autenticação por sessão. Você precisa fazer login primeiro para obter os cookies de sessão.

---

## 🔐 Passo 1: Fazer Login

### Endpoint: `POST /auth/login`

**Headers:**
```
Content-Type: application/json
```

**Body (raw JSON):**
```json
{
  "email": "seu-email@exemplo.com",
  "senha": "sua-senha"
}
```

**Resposta esperada:**
- Status: `200 OK`
- O Postman automaticamente salvará os cookies de sessão

**⚠️ IMPORTANTE:** Após o login, o Postman manterá os cookies automaticamente. Certifique-se de que a opção "Send cookies" está habilitada nas configurações do Postman.

---

## 📚 Endpoints de ATIVIDADE

### 1. Cadastrar Atividade
**Endpoint:** `POST /atividade/cadastrar`

**Headers:**
```
Content-Type: application/json
```

**Body (raw JSON):**
```json
{
  "nome": "Yoga Matinal",
  "descricao": "Aula de yoga para iniciantes todas as manhãs",
  "dias": "Segunda a Sexta",
  "horario": "08:00:00",
  "vagas": 20,
  "imagem": "https://exemplo.com/imagens/yoga.jpg"
}
```

**Exemplo de resposta:**
```json
{
  "id": 1,
  "nome": "Yoga Matinal",
  "descricao": "Aula de yoga para iniciantes todas as manhãs",
  "dias": "Segunda a Sexta",
  "horario": "08:00:00",
  "vagas": 20,
  "imagem": "https://exemplo.com/imagens/yoga.jpg"
}
```

---

### 2. Listar Todas as Atividades
**Endpoint:** `GET /atividade/listar`

**Headers:**
```
(nenhum header especial necessário)
```

**Resposta esperada:**
```json
[
  {
    "id": 1,
    "nome": "Yoga Matinal",
    "descricao": "Aula de yoga para iniciantes todas as manhãs",
    "dias": "Segunda a Sexta",
    "horario": "08:00:00",
    "vagas": 20,
    "imagem": "https://exemplo.com/imagens/yoga.jpg"
  },
  {
    "id": 2,
    "nome": "Aulas de Inglês",
    "descricao": "Curso básico de inglês",
    "dias": "Terça e Quinta",
    "horario": "14:00:00",
    "vagas": 15,
    "imagem": "https://exemplo.com/imagens/ingles.jpg"
  }
]
```

---

### 3. Buscar Atividade por Nome
**Endpoint:** `GET /atividade/buscar?nome=Yoga`

**Query Parameters:**
- `nome`: Nome da atividade (parcial ou completo)

**Exemplo de URL:**
```
http://localhost:8080/atividade/buscar?nome=Yoga
```

---

### 4. Atualizar Atividade
**Endpoint:** `PUT /atividade/atualizar/{id}`

**Exemplo:** `PUT /atividade/atualizar/1`

**Headers:**
```
Content-Type: application/json
```

**Body (raw JSON):**
```json
{
  "nome": "Yoga Matinal - Avançado",
  "descricao": "Aula de yoga avançada",
  "dias": "Segunda, Quarta e Sexta",
  "horario": "09:00:00",
  "vagas": 15,
  "imagem": "https://exemplo.com/imagens/yoga-avancado.jpg"
}
```

---

### 5. Deletar Atividade
**Endpoint:** `DELETE /atividade/deletar/{id}`

**Exemplo:** `DELETE /atividade/deletar/1`

**Resposta esperada:**
```json
{
  "message": "Atividade excluída!",
  "status": 201
}
```

---

## 🎫 Endpoints de INSCRIÇÃO

### ⚠️ IMPORTANTE: Validações
- Apenas **voluntários APROVADOS** podem se inscrever
- O usuário é obtido automaticamente da sessão (não precisa enviar `idUsuario`)
- Não permite inscrição duplicada
- Não permite inscrição se as vagas estiverem esgotadas

---

### 1. Inscrever-se em Atividade
**Endpoint:** `POST /inscricao/inscrever`

**Headers:**
```
Content-Type: application/json
```

**Body (raw JSON):**
```json
{
  "idAtividade": 1
}
```

**⚠️ Nota:** O `idUsuario` não é necessário - é obtido automaticamente da sessão do usuário autenticado.

**Resposta esperada (sucesso):**
```json
{
  "id": 1,
  "idUsuario": {
    "id": 5,
    "nome": "João Silva",
    "email": "joao@exemplo.com"
  },
  "idAtividade": {
    "id": 1,
    "nome": "Yoga Matinal"
  },
  "dataInscricao": "2025-11-15T17:30:00"
}
```

**Possíveis erros:**
- `400 Bad Request`: "Apenas voluntários aprovados podem se inscrever em atividades."
- `400 Bad Request`: "Usuário já inscrito nessa atividade."
- `400 Bad Request`: "Não há vagas disponíveis para esta atividade."

---

### 2. Listar Todas as Inscrições
**Endpoint:** `GET /inscricao/listar`

**Resposta esperada:**
```json
[
  {
    "id": 1,
    "idUsuario": {
      "id": 5,
      "nome": "João Silva"
    },
    "idAtividade": {
      "id": 1,
      "nome": "Yoga Matinal"
    },
    "dataInscricao": "2025-11-15T17:30:00"
  }
]
```

---

### 3. Cancelar Inscrição (por ID)
**Endpoint:** `DELETE /inscricao/deletar/{id}`

**Exemplo:** `DELETE /inscricao/deletar/1`

**Resposta esperada:**
```json
{
  "message": "Inscrição cancelada!",
  "status": 200
}
```

---

### 4. Cancelar Inscrição (por Atividade)
**Endpoint:** `DELETE /inscricao/cancelar/{idAtividade}`

**Exemplo:** `DELETE /inscricao/cancelar/1`

**⚠️ Nota:** Cancela a inscrição do usuário autenticado na atividade especificada.

**Resposta esperada:**
```json
{
  "message": "Inscrição cancelada com sucesso!",
  "status": 200
}
```

---

## 📝 Exemplos de Testes Completos

### Cenário 1: Criar e Listar Atividades

1. **Login:**
   ```
   POST http://localhost:8080/auth/login
   Body: { "email": "admin@exemplo.com", "senha": "senha123" }
   ```

2. **Criar Atividade:**
   ```
   POST http://localhost:8080/atividade/cadastrar
   Body: {
     "nome": "Aulas de Música",
     "descricao": "Aprenda violão e guitarra",
     "dias": "Sábado",
     "horario": "10:00:00",
     "vagas": 10,
     "imagem": "https://exemplo.com/musica.jpg"
   }
   ```

3. **Listar Atividades:**
   ```
   GET http://localhost:8080/atividade/listar
   ```

---

### Cenário 2: Inscrever-se em Atividade

1. **Login (como voluntário aprovado):**
   ```
   POST http://localhost:8080/auth/login
   Body: { "email": "voluntario@exemplo.com", "senha": "senha123" }
   ```

2. **Inscrever-se:**
   ```
   POST http://localhost:8080/inscricao/inscrever
   Body: { "idAtividade": 1 }
   ```

3. **Verificar Inscrições:**
   ```
   GET http://localhost:8080/inscricao/listar
   ```

4. **Cancelar Inscrição:**
   ```
   DELETE http://localhost:8080/inscricao/cancelar/1
   ```

---

## 🔧 Dicas do Postman

### 1. Configurar Environment
Crie um Environment no Postman com:
- `base_url`: `http://localhost:8080`
- Use `{{base_url}}` nas requisições

### 2. Salvar Cookies Automaticamente
- Vá em **Settings** → **General**
- Certifique-se que "Automatically follow redirects" está ativado
- Os cookies são salvos automaticamente após o login

### 3. Criar Collection
Organize as requisições em uma Collection:
- **Auth** (Login)
- **Atividades** (CRUD)
- **Inscrições** (CRUD)

### 4. Usar Variáveis
Após criar uma atividade, salve o `id` em uma variável:
- Na aba **Tests** da requisição:
```javascript
if (pm.response.code === 200) {
    var jsonData = pm.response.json();
    pm.environment.set("atividade_id", jsonData.id);
}
```

Depois use: `{{atividade_id}}` nas outras requisições.

---

## ⚠️ Troubleshooting

### Erro: "Apenas voluntários aprovados podem se inscrever"
- Verifique se o usuário está autenticado
- Verifique se o usuário tem status `APROVADO` como voluntário
- Use o endpoint `/voluntario/tornar` primeiro para se tornar voluntário

### Erro: "Não há vagas disponíveis"
- A atividade está com todas as vagas preenchidas
- Verifique o número de vagas disponíveis na atividade

### Erro: "Usuário já inscrito"
- O usuário já está inscrito nesta atividade
- Use o endpoint de cancelar antes de tentar novamente

### Cookies não estão sendo enviados
- Verifique se fez login primeiro
- Vá em **Cookies** (ícone de cookie no Postman) e verifique se há cookies salvos
- Tente fazer login novamente

---

## 📊 Formato de Horário

O campo `horario` deve estar no formato `HH:mm:ss`:
- ✅ Correto: `"08:00:00"` (8h da manhã)
- ✅ Correto: `"14:30:00"` (14h30)
- ❌ Errado: `"8:00"` (sem zeros à esquerda)
- ❌ Errado: `"08:00"` (sem segundos)

---

## 🎯 Checklist de Testes

- [ ] Login funcionando
- [ ] Criar atividade
- [ ] Listar atividades
- [ ] Buscar atividade por nome
- [ ] Atualizar atividade
- [ ] Deletar atividade
- [ ] Inscrever-se em atividade (como voluntário aprovado)
- [ ] Listar inscrições
- [ ] Cancelar inscrição por ID
- [ ] Cancelar inscrição por atividade
- [ ] Testar validação de vagas esgotadas
- [ ] Testar validação de inscrição duplicada
- [ ] Testar validação de voluntário não aprovado

