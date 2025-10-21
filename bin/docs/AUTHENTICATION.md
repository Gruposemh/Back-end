# Sistema de Autenticação - ONG Backend

## Funcionalidades Implementadas

Este documento descreve as funcionalidades de autenticação implementadas no projeto.

### 1. Cadastro de Usuário (Sign Up)

**Endpoint:** `POST /auth/register`

**Body:**
```json
{
  "nome": "João Silva",
  "email": "joao@exemplo.com",
  "senha": "senha123",
  "role": "USUARIO"
}
```

**Resposta:**
```json
{
  "message": "Usuário cadastrado com sucesso. Verifique seu email para ativar a conta."
}
```

**Funcionalidades:**
- Validação de campos obrigatórios
- Verificação de email duplicado
- Hash seguro da senha
- Envio automático de email de verificação
- Criação de timestamp de criação

---

### 2. Login Tradicional

**Endpoint:** `POST /auth/login`

**Body:**
```json
{
  "email": "joao@exemplo.com",
  "senha": "senha123"
}
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "joao@exemplo.com",
  "role": "USUARIO"
}
```

**Funcionalidades:**
- Rate limiting (proteção contra força bruta)
- Validação de credenciais
- Geração de JWT token
- Atualização de último login

---

### 3. Verificação de Email

#### 3.1 Verificar Email

**Endpoint:** `POST /auth/verify-email`

**Body:**
```json
{
  "email": "joao@exemplo.com",
  "token": "123456"
}
```

**Resposta:**
```json
{
  "message": "Email verificado com sucesso"
}
```

#### 3.2 Reenviar Código de Verificação

**Endpoint:** `POST /auth/resend-verification`

**Body:**
```json
{
  "email": "joao@exemplo.com"
}
```

**Resposta:**
```json
{
  "message": "Email de verificação enviado"
}
```

**Funcionalidades:**
- Token numérico de 6 dígitos
- Expiração após 15 minutos
- Verificação de email única
- Rate limiting

---

### 4. Reset de Senha (Password Reset)

#### 4.1 Solicitar Reset de Senha

**Endpoint:** `POST /auth/request-password-reset`

**Body:**
```json
{
  "email": "joao@exemplo.com"
}
```

**Resposta:**
```json
{
  "message": "Email de reset de senha enviado"
}
```

#### 4.2 Confirmar Reset de Senha

**Endpoint:** `POST /auth/reset-password`

**Body:**
```json
{
  "email": "joao@exemplo.com",
  "token": "123456",
  "novaSenha": "novaSenha123"
}
```

**Resposta:**
```json
{
  "message": "Senha alterada com sucesso"
}
```

**Funcionalidades:**
- Token numérico de 6 dígitos
- Expiração após 15 minutos
- Hash seguro da nova senha
- Rate limiting

---

### 5. Login via Google (OAuth2)

**Endpoint:** `POST /auth/social-login`

**Body:**
```json
{
  "providerId": "google-id-123",
  "providerName": "GOOGLE",
  "email": "joao@gmail.com",
  "nome": "João Silva"
}
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "joao@gmail.com",
  "role": "USUARIO"
}
```

**Funcionalidades:**
- Login automático ou criação de conta
- Email automaticamente verificado
- Integração com Google OAuth2

---

### 6. Login via OTP (One-Time Password)

#### 6.1 Solicitar Código OTP

**Endpoint:** `POST /auth/request-otp`

**Body:**
```json
{
  "email": "joao@exemplo.com"
}
```

**Resposta:**
```json
{
  "message": "Código enviado para seu email"
}
```

#### 6.2 Login com OTP

**Endpoint:** `POST /auth/login-otp`

**Body:**
```json
{
  "email": "joao@exemplo.com",
  "token": "123456"
}
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "joao@exemplo.com",
  "role": "USUARIO"
}
```

**Funcionalidades:**
- Login sem senha (passwordless)
- Token numérico de 6 dígitos
- Expiração após 10 minutos
- Rate limiting

---

## Segurança

### Rate Limiting

O sistema implementa rate limiting para proteger contra:
- **Login**: Máximo de tentativas por hora
- **OTP**: Máximo de solicitações por hora
- **Verificação de Email**: Máximo de solicitações por hora
- **Reset de Senha**: Máximo de solicitações por hora

### Tokens

Todos os tokens são:
- Hash criptografados antes de serem armazenados
- Validados por expiração temporal
- Únicos por usuário
- Descartados após uso ou expiração

### JWT

- Assinatura HMAC SHA-256
- Expiração configurável (padrão: 1 hora)
- Secret key configurável via environment variable

---

## Configuração

### Variáveis de Ambiente

```properties
# JWT Configuration
jwt.secret=${JWT_SECRET:sua-chave-secreta-aqui}
jwt.expiration=${JWT_EXPIRATION:3600000}

# Email Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=seu-email@gmail.com
spring.mail.password=sua-senha-de-app

# OAuth2 Google Configuration
spring.security.oauth2.client.registration.google.client-id=seu-client-id
spring.security.oauth2.client.registration.google.client-secret=seu-client-secret
```

---

## Limpeza Automática

O sistema executa automaticamente a cada 30 minutos:
- Limpeza de tokens de verificação de email expirados
- Limpeza de tokens de reset de senha expirados
- Limpeza de tokens OTP expirados

---

## Estrutura de Banco de Dados

### Campos Adicionados na Tabela `tb_usuarios`

```sql
emailVerificado BOOLEAN DEFAULT FALSE
emailVerificationToken VARCHAR(255)
emailVerificationTokenExpiryDate DATETIME
passwordResetToken VARCHAR(255)
passwordResetTokenExpiryDate DATETIME
tipoAutenticacao ENUM('TRADICIONAL', 'SOCIAL', 'PASSWORDLESS')
providerId VARCHAR(255)
providerName VARCHAR(255)
criadoEm DATETIME
ultimoLogin DATETIME
```

### Nova Tabela `tb_tokens_otp`

```sql
CREATE TABLE tb_tokens_otp (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  email VARCHAR(255) NOT NULL,
  token VARCHAR(255) NOT NULL,
  criadoEm DATETIME NOT NULL,
  expiraEm DATETIME NOT NULL,
  usado BOOLEAN DEFAULT FALSE
);
```

---

## Serviços Implementados

### TokenService
- Geração e validação de JWT tokens
- Extração de informações do token

### VerificationTokenService
- Geração de tokens de verificação de email
- Geração de tokens de reset de senha
- Validação e limpeza de tokens

### EmailService
- Envio de emails de verificação
- Envio de emails de reset de senha
- Envio de emails de boas-vindas
- Envio de códigos OTP

### OTPService
- Geração de códigos OTP
- Validação de códigos OTP
- Limpeza de códigos expirados

### RateLimitService
- Proteção contra força bruta
- Limitação de requisições por IP/email

---

## Próximos Passos

1. **Implementar refresh tokens** para renovação automática de JWT
2. **Adicionar autenticação de dois fatores (2FA)** com TOTP
3. **Implementar log de atividades** de login
4. **Adicionar detecção de dispositivos** para notificações de login
5. **Implementar blacklist de tokens** para logout forçado

---

## Testes

Para testar as funcionalidades, use o Postman ou qualquer cliente HTTP:

1. Registre um usuário
2. Verifique o email recebido
3. Teste o login
4. Teste o reset de senha
5. Teste o login via OTP

---

## Suporte

Para dúvidas ou problemas, entre em contato com a equipe de desenvolvimento.
