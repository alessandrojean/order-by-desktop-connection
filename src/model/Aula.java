package model;

import java.sql.Time;


public class Aula
{
	private Integer idAula ;
	private Sala sala;
	private Turma turma;
	private Materia materia;
	private String dia; 
	private Time inicio;
	private Time fim;
	public Integer getIdAula()
	{
		return idAula;
	}
	public void setIdAula(Integer idAula)
	{
		this.idAula = idAula;
	}
	public Sala getSala()
	{
		return sala;
	}
	public void setSala(Sala sala)
	{
		this.sala = sala;
	}
	public Turma getTurma()
	{
		return turma;
	}
	public void setTurma(Turma turma)
	{
		this.turma = turma;
	}
	public Materia getMateria()
	{
		return materia;
	}
	public void setMateria(Materia materia)
	{
		this.materia = materia;
	}
	public String getDia()
	{
		return dia;
	}
	public void setDia(String dia)
	{
		this.dia = dia;
	}
	public Time getInicio()
	{
		return inicio;
	}
	public void setInicio(Time inicio)
	{
		this.inicio = inicio;
	}
	public Time getFim()
	{
		return fim;
	}
	public void setFim(Time fim)
	{
		this.fim = fim;
	}

}
