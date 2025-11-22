package com.ong.backend.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_usuarios")
public class Usuario implements UserDetails{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	@Column(unique= true)
	private String email;
	private String senha; // Pode ser null para usuários sociais/passwordless
	@Enumerated(EnumType.STRING)
    private StatusRole role;
    
    // Novos campos para autenticação híbrida
    @Enumerated(EnumType.STRING)
    private TipoAutenticacao tipoAutenticacao;
    
    private String providerId; // ID do usuário no provedor social (Google/Facebook)
    private String providerName; // "GOOGLE", "FACEBOOK", etc.
    
    private boolean emailVerificado = false;
    private String emailVerificationToken = null;
    private LocalDateTime emailVerificationTokenExpiryDate = null;
    private String passwordResetToken = null;
    private LocalDateTime passwordResetTokenExpiryDate = null;
    private LocalDateTime criadoEm;
    private LocalDateTime ultimoLogin;
	
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Doacao> doacoes;
	
	@OneToMany(mappedBy = "idUsuario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Blog> blogs;
	
	@OneToMany(mappedBy = "idUsuario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Inscricao> inscricoes;
	
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParticipacaoEvento> participacoes = new ArrayList<>();
	
	@OneToOne(mappedBy = "idUsuario", cascade = CascadeType.ALL, orphanRemoval = true)
	private Voluntario voluntario;
	
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Tarefas> tarefas = new ArrayList<>();
	
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RefreshToken> tokens = new ArrayList<>();
	
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<VerificationToken> verificationTokens = new ArrayList<>();
	
	public Usuario() {
		
	}
	
	public Usuario(Long id, String nome, String email, String senha, StatusRole role) {
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.role = role;
		this.tipoAutenticacao = TipoAutenticacao.TRADICIONAL;
		this.criadoEm = LocalDateTime.now();
	}

	public Usuario(String nome, String email, String senha, StatusRole role) {
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.role = role;
		this.tipoAutenticacao = TipoAutenticacao.TRADICIONAL;
		this.criadoEm = LocalDateTime.now();
	}
	
	// Construtor para usuários sociais
	public Usuario(String nome, String email, StatusRole role, String providerId, String providerName) {
		this.nome = nome;
		this.email = email;
		this.role = role;
		this.tipoAutenticacao = TipoAutenticacao.SOCIAL;
		this.providerId = providerId;
		this.providerName = providerName;
		this.emailVerificado = true; // Provedores sociais já verificam email
		this.criadoEm = LocalDateTime.now();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public StatusRole getRole() {
		return role;
	}
	public void setRole(StatusRole role) {
		this.role = role;
	}
	
	public TipoAutenticacao getTipoAutenticacao() {
		return tipoAutenticacao;
	}
	public void setTipoAutenticacao(TipoAutenticacao tipoAutenticacao) {
		this.tipoAutenticacao = tipoAutenticacao;
	}
	
	public String getProviderId() {
		return providerId;
	}
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	
	public String getProviderName() {
		return providerName;
	}
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	
	public boolean isEmailVerificado() {
		return emailVerificado;
	}
	public void setEmailVerificado(boolean emailVerificado) {
		this.emailVerificado = emailVerificado;
	}
	
	public LocalDateTime getCriadoEm() {
		return criadoEm;
	}
	public void setCriadoEm(LocalDateTime criadoEm) {
		this.criadoEm = criadoEm;
	}
	
	public LocalDateTime getUltimoLogin() {
		return ultimoLogin;
	}
	public void setUltimoLogin(LocalDateTime ultimoLogin) {
		this.ultimoLogin = ultimoLogin;
	}
	
	public String getEmailVerificationToken() {
		return emailVerificationToken;
	}
	
	public void setEmailVerificationToken(String emailVerificationToken) {
		this.emailVerificationToken = emailVerificationToken;
	}
	
	public LocalDateTime getEmailVerificationTokenExpiryDate() {
		return emailVerificationTokenExpiryDate;
	}
	
	public void setEmailVerificationTokenExpiryDate(LocalDateTime emailVerificationTokenExpiryDate) {
		this.emailVerificationTokenExpiryDate = emailVerificationTokenExpiryDate;
	}
	
	public String getPasswordResetToken() {
		return passwordResetToken;
	}
	
	public void setPasswordResetToken(String passwordResetToken) {
		this.passwordResetToken = passwordResetToken;
	}
	
	public LocalDateTime getPasswordResetTokenExpiryDate() {
		return passwordResetTokenExpiryDate;
	}
	
	public void setPasswordResetTokenExpiryDate(LocalDateTime passwordResetTokenExpiryDate) {
		this.passwordResetTokenExpiryDate = passwordResetTokenExpiryDate;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
	}

	@Override
	public String getPassword() {
		return senha;
	}

	@Override
	public String getUsername() {
		return email;
	}	
}