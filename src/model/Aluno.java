package model;

import java.awt.Image;

public class Aluno extends Pessoa {

	private String RM;
	private Responsavel responsavel;
	private Image foto = null;

	public String getRM() {
		return RM;
	}

	public void setRM(String rm) {
		RM = rm;
	}

	public Responsavel getResponsavel()
	{
		return responsavel;
	}

	public void setResponsavel(Responsavel responsavel)
	{
		this.responsavel = responsavel;
	}
	
	public Image getFoto()
	{
		return foto;
	}

	public void setFoto(Image foto)
	{
		this.foto = foto;
	}

}
