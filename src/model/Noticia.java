package model;

import java.awt.Image;
import java.sql.Timestamp;
import java.util.List;

public class Noticia
{
	private int id;
	private String titulo;
	private String conteudo;
	private Funcionario criador;
	private Timestamp data;
	private boolean destaque;
	private String noticia;
	private List<Tag> tags;
	private String tipo;
	private Image imagem = null;
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getTitulo()
	{
		return titulo;
	}
	public void setTitulo(String titulo)
	{
		this.titulo = titulo;
	}
	public String getConteudo()
	{
		return conteudo;
	}
	public void setConteudo(String conteudo)
	{
		this.conteudo = conteudo;
	}
	public Funcionario getCriador()
	{
		return criador;
	}
	public void setCriador(Funcionario criador)
	{
		this.criador = criador;
	}
	public Timestamp getData()
	{
		return data;
	}
	public void setData(Timestamp data)
	{
		this.data = data;
	}
	public boolean isDestaque()
	{
		return destaque;
	}
	public void setDestaque(boolean destaque)
	{
		this.destaque = destaque;
	}
	public String getNoticia()
	{
		return noticia;
	}
	public void setNoticia(String noticia)
	{
		this.noticia = noticia;
	}
	public List<Tag> getTags()
	{
		return tags;
	}
	public void setTags(List<Tag> tags)
	{
		this.tags = tags;
	}
	public String getTipo()
	{
		return tipo;
	}
	public void setTipo(String tipo)
	{
		this.tipo = tipo;
	}
	public Image getImagem()
	{
		return imagem;
	}
	public void setImagem(Image imagem)
	{
		this.imagem = imagem;
	}
}
