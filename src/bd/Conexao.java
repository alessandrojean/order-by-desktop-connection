package bd;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.text.StyleContext.SmallAttributeSet;

import model.AcaoRecente;
import model.Aluno;
import model.Aula;
import model.Endereco;
import model.Falta;
import model.Funcionario;
import model.Horario;
import model.Materia;
import model.Nota;
import model.Professor;
import model.Responsavel;
import model.Sala;
import model.Turma;

public class Conexao //instancia as informações basicas para conexão com o banco
{

	private String driver = "com.mysql.jdbc.Driver"; //pega o driver do banco
	private String ip = "jdbc:mysql://localhost:3306/"; //pega a porta para conexão ao banco
	private String usuario = "root";//usuário padrão
	private String senha = "";//senha padrão
	private String database = "escola";//nome da database 

	public Conexao()
	{
		ip+=database; //junta as informações para inciar a database
	}

	public Conexao(String i, String u, String s,String d)
	{
		ip = "jdbc:mysql://"+i+":3306/"+d;
		usuario = u;
		senha = s;
	}

	public boolean testaConexao()//testa a conexão antes de usá-la
	{
		Connection con = null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	//conta as colunas de determinada tabela o teste 
	public int contar(String table, String coluna)
	{
		Connection con = null;
		int retorno = 0;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "SELECT COUNT(" + coluna + ") AS 'teste' FROM " + table + ";";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			retorno = rs.getInt(1);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return retorno;
	}
	/**métodos para busca de dados*/
	//Cria uma lista das turmas cadastradas 
	public List<Turma> buscarTurmas()
	{
		List<Turma> turmas = new ArrayList<Turma>(); //criação da variavel que conterá a lista desejada
		Connection con = null; //responsavel pela conexão do banco com este método
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha); //comandos de conexão
			Statement stmt = con.createStatement();  
			String sql = "SELECT * from turmas order by grau_turmas;"; //Comando sql executado na database 
			ResultSet rs = stmt.executeQuery(sql); 
			//executa o comando na database para utilizar a pesquisas dos atributos abaixo
			while (rs.next())//executa as querys até não haver mais porque o método busca tudo
			{
				Turma t = new Turma(); //instancia a classe turma para buscar as informações dda database a partir de suas variáveis 
				t.setGrau(rs.getInt("grau_turmas"));
				t.setClasse(rs.getString("classe_turmas"));
				t.setCurso(rs.getString("curso_turmas"));
				t.setPeriodo(rs.getString("período_turmas"));
				t.setIdTurma(rs.getInt("idturma"));
				turmas.add(t); //adiciona as informações à variável da Lista criado acima
			}

		}//imprime erros que possam ocorrer devido a comandos incorretos
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try//fecha a conexão ao fim do uso do método
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return turmas; //retorna a lista 
	}
	
	public Turma buscarTurmas(int id) //Overload pelo id
	{
		Connection con = null;
		Turma turma = null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "SELECT * from turmas where turmas.idturma=" + id +";"; 
			// busca apenas as informações da turma com o id inserido acima 
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
//retorna as informações de só uma sequência, por isso o "if" quando é estipulado um específico e o geral um while (ver acima)
			{
				turma = new Turma();				
				turma.setIdTurma(rs.getInt("idturma"));
				turma.setGrau(rs.getInt("grau_turmas"));
				turma.setClasse(rs.getString("classe_turmas"));
				turma.setCurso(rs.getString("curso_turmas"));
				turma.setPeriodo(rs.getString("período_turmas"));
			}

		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return turma;
	}

	public List<Sala> buscarSalas()
	{
		List<Sala> salas = new ArrayList<Sala>();
		Connection con = null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "SELECT * from salas order by idsala;";
			//busca uma lista com todas as salas inseridas ordenando pelos id's
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				Sala s = new Sala();
				s.setIdSala(rs.getString("idsala"));
				s.setTipo(rs.getString("tipo_sala"));
				salas.add(s);
			}

		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return salas;
	}

	public Sala buscarSalas(String id)
	{
		Connection con = null;
		Sala sala = null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "SELECT * from salas where idsala='"+ id +"';";
			//busca as informações da sala de determinado id (não necesita ordernar por só pesquisar por um)
			ResultSet rs=stmt.executeQuery(sql);
			if (rs.next())
			{
				sala=new Sala();
				sala.setIdSala(rs.getString("idsala"));
				sala.setTipo(rs.getString("tipo_sala"));
			}

		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return sala;
	}
	
	public List<Materia> buscarMaterias()
	{
		List<Materia> materias=new ArrayList<Materia>();
		Connection con=null;
		try
		{
			Class.forName(driver);
			con=DriverManager.getConnection(ip, usuario, senha);
			Statement stmt=con.createStatement();
			String sql="SELECT * from materias order by nome_materias;";
			//busca todas as matéria ordenado-as pelo nome
			ResultSet rs=stmt.executeQuery(sql);
			while (rs.next())
			{
				Materia m=new Materia();
				m.setIdMateria(rs.getInt("idmateria"));
				m.setNome(rs.getString("nome_materias"));
				m.setSigla(rs.getString("sigla_materias"));
				materias.add(m);
			}

		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return materias;
	}

	public Materia buscarMaterias(int id)
	{
		Connection con=null;
		Materia materia=null;
		try
		{
			Class.forName(driver);
			con=DriverManager.getConnection(ip, usuario, senha);
			Statement stmt=con.createStatement();
			String sql="SELECT * from materias where materias.idmateria=" + id +";";
			//busca as matérias pelo seu id inserido no parâmetro do método
			ResultSet rs=stmt.executeQuery(sql);
			if (rs.next())
			{
				materia=new Materia();
				materia.setIdMateria(rs.getInt("idmateria"));
				materia.setNome(rs.getString("nome_materias"));
				materia.setSigla(rs.getString("sigla_materias"));
			}

		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return materia;
	}
	
	public Materia buscarMaterias(String nome)
	{
		Materia m=null;
		Connection con=null;
		try
		{
			Class.forName(driver);
			con=DriverManager.getConnection(ip, usuario, senha);
			Statement stmt=con.createStatement();
			String sql="SELECT * from materias where sigla_materias='"+nome+"';";
			//busca as matérias pelo seu nome inserido no parâmetro do método
			ResultSet rs=stmt.executeQuery(sql);
			if (rs.next())
			{
				m=new Materia();
				m.setIdMateria(rs.getInt("idmateria"));
				m.setNome(rs.getString("nome_materias"));
				m.setSigla(rs.getString("sigla_materias"));
			}
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return m;
	}
	
	public List<Professor> buscarProfessores(Materia m, Turma t, Sala s)
	{
		List<Professor> professores=new ArrayList<Professor>();
		Connection con=null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "SELECT * from prof_mat_tur_sal,professores where prof_mat_tur_sal.idmat_pmts=" + m.getIdMateria() + " and prof_mat_tur_sal.idtur_pmts=" + t.getIdTurma() + " and prof_mat_tur_sal.idsala_pmts='"+s.getIdSala()+"' and prof_mat_tur_sal.cpfprof_pmts=professores.cpf_professores order by professores.nome_professores;";
			//pesquisa todas as informações de todos professor que se relacionam aos id's inseridos da matéria, turma e sala em sua tabela de relacionamento ordenando pelos nomes dos professores
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				Professor p = new Professor();
				p.setCPF(rs.getString("cpf_professores"));
				p.setNome(rs.getString("nome_professores"));
				p.setTelefone(rs.getString("telefone_professores"));
				p.setCelular(rs.getString("celular_professores"));
				p.setSigla(rs.getString("sigla_professores"));
				p.setSenha(rs.getString("senha_professores"));
				p.setRg(rs.getString("rg_professores"));
				professores.add(p);
			}
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return professores;
	}

	public List<String> buscarProfessores(boolean pmts)
	{
		List<String> professores = new ArrayList<String>();	
		Connection con = null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "SELECT * from prof_mat_tur_sal order by cpfprof_pmts;";
			//busca todos os professores que se encontram relacionados na tabela prof_mat_tur_sal ordenando pelos seus cpf's
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				professores.add(buscarProfessores(rs.getString("cpfprof_pmts")).getSigla());
				professores.add(buscarMaterias(rs.getInt("idmat_pmts")).getSigla());
				professores.add(rs.getString("idtur_pmts"));	
				professores.add(rs.getString("idsala_pmts"));
				professores.add(String.valueOf(buscarLimite(buscarMaterias(rs.getInt("idmat_pmts")), buscarTurmas(rs.getInt("idtur_pmts")))));
				professores.add(String.valueOf(buscarPrioridade(buscarMaterias(rs.getInt("idmat_pmts")), buscarTurmas(rs.getInt("idtur_pmts")))));
			}
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return professores;
	}

	public List<String> buscarProfessor_Aula()
	{
		List<String> professores = new ArrayList<String>();	
		Connection con = null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "SELECT * from aulas_professores;";
			//busca tudo o que há na tabela de relação da aula com o professor
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				professores.add(buscarProfessores(rs.getString("professorescpf_ap")).getSigla());	
				//retorna apenas as informações dos professores em list
			}
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return professores;
	}

	public List<Professor> buscarProfessores()
	{
		List<Professor> professores = new ArrayList<Professor>();
		Connection con = null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "SELECT * from professores, enderecos where enderecos.idendereco=professores.enderecosidendereco_professores order by professores.nome_professores;";
			//busca os todos os professores e todas as informações de seus respectivos endereços ordenando pelos seus nomes 
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				Professor p = new Professor();
				p.setCPF(rs.getString("cpf_professores"));
				p.setNome(rs.getString("nome_professores"));
				p.setTelefone(rs.getString("telefone_professores"));
				p.setCelular(rs.getString("celular_professores"));
				p.setSigla(rs.getString("sigla_professores"));
				p.setSenha(rs.getString("senha_professores"));
				p.setRg(rs.getString("rg_professores"));
				
				//adiciona à variável retornada a lista deas informações aqui pedidas

				Endereco e = new Endereco();
				e.setIdEndereco(rs.getInt("idendereco"));
				e.setTipo(rs.getString("tipo_endereco"));
				e.setEstado(rs.getString("estado_endereco"));
				e.setCidade(rs.getString("cidade_endereco"));
				e.setBairro(rs.getString("bairro_endereco"));
				e.setRua(rs.getString("rua_endereco"));
				e.setCEP(rs.getString("cep_endereco"));
				e.setNumero(rs.getInt("numero_endereco"));
				e.setComplemento(rs.getString("complemento_endereco"));
				p.setEndereco(e);
				professores.add(p);
			}
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return professores;
	}

	public Professor buscarProfessores(String cpf)
	{
		Connection con = null;
		Professor professores = null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "SELECT * from professores,enderecos where professores.cpf_professores='"+ cpf +"' and professores.enderecosidendereco_professores=enderecos.idendereco;";
			//pesquisa um professor e suas informações de endereço pelo seu cpf
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
			{				
				professores = new Professor();
				professores.setCPF(rs.getString("cpf_professores"));
				professores.setNome(rs.getString("nome_professores"));
				professores.setTelefone(rs.getString("telefone_professores"));
				professores.setCelular(rs.getString("celular_professores"));
				professores.setSigla(rs.getString("sigla_professores"));
				professores.setSenha(rs.getString("senha_professores"));
				professores.setRg(rs.getString("rg_professores"));
				
				Endereco e = new Endereco();
				e.setIdEndereco(rs.getInt("idendereco"));
				e.setTipo(rs.getString("tipo_endereco"));
				e.setEstado(rs.getString("estado_endereco"));
				e.setCidade(rs.getString("cidade_endereco"));
				e.setBairro(rs.getString("bairro_endereco"));
				e.setRua(rs.getString("rua_endereco"));
				e.setCEP(rs.getString("cep_endereco"));
				e.setNumero(rs.getInt("numero_endereco"));
				e.setComplemento(rs.getString("complemento_endereco"));
				professores.setEndereco(e);
			}
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return professores;
	}
	
	public Professor buscarProfessores(String nome, boolean comnome)
	//busca um professor pelo seu nome; para não conflitar com o método anterior adicionamos um boolean que tem apenas a função de diferenciá-los
	{
		Connection con = null;
		Professor professores = null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "SELECT * from professores,enderecos where professores.sigla_professores='" + nome + "' and professores.enderecosidendereco_professores=enderecos.idendereco order by professores.nome_professores;";
			//busca o professor pelo nome ordenando, também, pelo nome
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
			{
				professores = new Professor();
				professores.setCPF(rs.getString("cpf_professores"));
				professores.setNome(rs.getString("nome_professores"));
				professores.setTelefone(rs.getString("telefone_professores"));
				professores.setCelular(rs.getString("celular_professores"));
				professores.setSigla(rs.getString("sigla_professores"));
				professores.setSenha(rs.getString("senha_professores"));
				professores.setRg(rs.getString("rg_professores"));
				
				Endereco e = new Endereco();
				e.setIdEndereco(rs.getInt("idendereco"));
				e.setTipo(rs.getString("tipo_endereco"));
				e.setEstado(rs.getString("estado_endereco"));
				e.setCidade(rs.getString("cidade_endereco"));
				e.setBairro(rs.getString("bairro_endereco"));
				e.setRua(rs.getString("rua_endereco"));
				e.setCEP(rs.getString("cep_endereco"));
				e.setNumero(rs.getInt("numero_endereco"));
				e.setComplemento(rs.getString("complemento_endereco"));
				professores.setEndereco(e);
			}
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return professores;
	}
	
	public List<Aluno> buscarAlunos()
	{
		List<Aluno> alunos = new ArrayList<Aluno>();
		Connection con = null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "SELECT * from alunos,enderecos,responsaveis where alunos.enderecosidedereco_aluno=enderecos.idendereco and alunos.cpf_responsavel_aluno=responsaveis.cpf_responsaveis order by alunos.rm_aluno;";
			//busca uma lista dos alunos inseridos no banco e seus respectivos responsaveis e endreços ordenando pelos seus rm's
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				Aluno a = new Aluno();
				a.setRM(rs.getString("rm_aluno"));
				a.setNome(rs.getString("nome_aluno"));
				a.setCPF(rs.getString("cpf_aluno"));
				a.setTelefone(rs.getString("tel_aluno"));
				a.setCelular(rs.getString("cel_aluno"));
				a.setRg(rs.getString("rg_aluno"));
				
				if (rs.getBytes("foto_aluno") != null)
				{
					ImageIcon ii = new ImageIcon(rs.getBytes("foto_aluno"));
					a.setFoto(ii.getImage());
				}
				//código para busca da foto do aluno que está salva com imagem
				Responsavel r = new Responsavel();
				r.setCPF(rs.getString("cpf_responsaveis"));
				r.setNome(rs.getString("nome_responsaveis"));
				r.setTel_com(rs.getString("tel_com_responsaveis"));
				r.setRamal(rs.getInt("ramal_responsaveis"));
				r.setCelular(rs.getString("celular_responsaveis"));		
				r.setRg(rs.getString("rg_responsaveis"));
				a.setResponsavel(r);
				
				Endereco e = new Endereco();
				e.setIdEndereco(rs.getInt("idendereco"));
				e.setTipo(rs.getString("tipo_endereco"));
				e.setEstado(rs.getString("estado_endereco"));
				e.setCidade(rs.getString("cidade_endereco"));
				e.setBairro(rs.getString("bairro_endereco"));
				e.setRua(rs.getString("rua_endereco"));
				e.setCEP(rs.getString("cep_endereco"));
				e.setNumero(rs.getInt("numero_endereco"));
				e.setComplemento(rs.getString("complemento_endereco"));
				a.setEndereco(e);				
				alunos.add(a);
			}
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return alunos;
	}

	public Aluno buscarAlunos(String rm)
	{
		Connection con = null;
		Aluno alunos = null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "SELECT * from alunos,enderecos,responsaveis where alunos.rm_aluno='" + rm + "' and alunos.enderecosidedereco_aluno=enderecos.idendereco and alunos.cpf_responsavel_aluno=responsaveis.cpf_responsaveis;";
			//busca um aluno pelo seu rm dando seu respectivo responsável e endereço
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
			{
				alunos = new Aluno();
				alunos.setRM(rs.getString("rm_aluno"));
				alunos.setCPF(rs.getString("cpf_aluno"));
				alunos.setNome(rs.getString("nome_aluno"));
				alunos.setTelefone(rs.getString("tel_aluno"));
				alunos.setCelular(rs.getString("cel_aluno"));
				alunos.setRg(rs.getString("rg_aluno"));
				
				if (rs.getBytes("foto_aluno") != null)
				{
					ImageIcon ii = new ImageIcon(rs.getBytes("foto_aluno"));
					alunos.setFoto(ii.getImage());
				}

				Responsavel r = new Responsavel();
				r.setCPF(rs.getString("cpf_responsaveis"));
				r.setNome(rs.getString("nome_responsaveis"));
				r.setTel_com(rs.getString("tel_com_responsaveis"));
				r.setRamal(rs.getInt("ramal_responsaveis"));
				r.setCelular(rs.getString("celular_responsaveis"));	
				r.setRg(rs.getString("rg_responsaveis"));
				alunos.setResponsavel(r);
				
				Endereco e = new Endereco();
				e.setIdEndereco(rs.getInt("idendereco"));
				e.setTipo(rs.getString("tipo_endereco"));
				e.setEstado(rs.getString("estado_endereco"));
				e.setCidade(rs.getString("cidade_endereco"));
				e.setBairro(rs.getString("bairro_endereco"));
				e.setRua(rs.getString("rua_endereco"));
				e.setCEP(rs.getString("cep_endereco"));
				e.setNumero(rs.getInt("numero_endereco"));
				e.setComplemento(rs.getString("complemento_endereco"));
				alunos.setEndereco(e);
			}
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return alunos;
	}
	
	public List<Funcionario> buscarFuncionarios()
	{
		List<Funcionario> funcionarios = new ArrayList<Funcionario>();
		Connection con = null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "SELECT * from funcionarios,enderecos where funcionarios.enderecosidendereco_funcionarios=enderecos.idendereco order by funcionarios.nome_funcionarios;";
			//busca uma lista de todos os funcionários inseridos e seus respectivos endereços ordenando pelos nomes dos funcionários
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				Funcionario f = new Funcionario();
				f.setCPF(rs.getString("cpf_funcionarios"));
				f.setNome(rs.getString("nome_funcionarios"));
				f.setTelefone(rs.getString("telefone_funcionarios"));
				f.setCelular(rs.getString("celular_funcionarios"));
				f.setSenha(rs.getString("senha_funcionarios"));
				f.setCargo(rs.getString("cargo_funcionarios"));
				f.setTipo(rs.getString("tipo_funcionarios"));
				f.setRg(rs.getString("rg_funcionarios"));
				
				Endereco e = new Endereco();
				e.setIdEndereco(rs.getInt("idendereco"));
				e.setTipo(rs.getString("tipo_endereco"));
				e.setEstado(rs.getString("estado_endereco"));
				e.setCidade(rs.getString("cidade_endereco"));
				e.setBairro(rs.getString("bairro_endereco"));
				e.setRua(rs.getString("rua_endereco"));
				e.setCEP(rs.getString("cep_endereco"));
				e.setNumero(rs.getInt("numero_endereco"));
				e.setComplemento(rs.getString("complemento_endereco"));
				f.setEndereco(e);
				funcionarios.add(f);
			}
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return funcionarios;
	}

	public Funcionario buscarFuncionarios(String cpf)
	{
		Connection con = null;
		Funcionario f = null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "SELECT * from funcionarios, enderecos where funcionarios.cpf_funcionarios='" + cpf + "' and funcionarios.enderecosidendereco_funcionarios=enderecos.idendereco;";
			//busca um funcionário e seu endereço em específico pelo seu cpf
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
			{
				f = new Funcionario();
				f.setCPF(rs.getString("cpf_funcionarios"));
				f.setNome(rs.getString("nome_funcionarios"));
				f.setTelefone(rs.getString("telefone_funcionarios"));
				f.setCelular(rs.getString("celular_funcionarios"));
				f.setSenha(rs.getString("senha_funcionarios"));
				f.setCargo(rs.getString("cargo_funcionarios"));
				f.setTipo(rs.getString("tipo_funcionarios"));
				f.setRg(rs.getString("rg_funcionarios"));

				Endereco e = new Endereco();
				e.setIdEndereco(rs.getInt("idendereco"));
				e.setTipo(rs.getString("tipo_endereco"));
				e.setEstado(rs.getString("estado_endereco"));
				e.setCidade(rs.getString("cidade_endereco"));
				e.setBairro(rs.getString("bairro_endereco"));
				e.setRua(rs.getString("rua_endereco"));
				e.setCEP(rs.getString("cep_endereco"));
				e.setNumero(rs.getInt("numero_endereco"));
				e.setComplemento(rs.getString("complemento_endereco"));
				f.setEndereco(e);
			}
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return f;
	}
	
	public List<Responsavel> buscarResponsavel()
	{
		List<Responsavel> responsaveis = new ArrayList<Responsavel>();
		Connection con = null;
		try
		{

			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "SELECT * from responsaveis,enderecos where and responsaveis.enderecosidendereco_responsaveis=enderecos.idendereco order by responsaveis.nome_responsaveis;";
			//busca uma lista de todos os responsáveis inseridos e seus respectivos endereços ordenando pelos nomes dos responsáveis
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				Responsavel r = new Responsavel();
				r.setCPF(rs.getString("cpf_responsaveis"));
				r.setNome(rs.getString("nome_responsaveis"));
				r.setTel_com(rs.getString("tel_com_responsaveis"));
				r.setCelular(rs.getString("celular_responsaveis"));				
				r.setRamal(rs.getInt("ramal_responsaveis"));
				r.setRg(rs.getString("rg_responsaveis"));

				Endereco e = new Endereco();
				e.setIdEndereco(rs.getInt("idendereco"));
				e.setTipo(rs.getString("tipo_endereco"));
				e.setEstado(rs.getString("estado_endereco"));
				e.setCidade(rs.getString("cidade_endereco"));
				e.setBairro(rs.getString("bairro_endereco"));
				e.setRua(rs.getString("rua_endereco"));
				e.setCEP(rs.getString("cep_endereco"));
				e.setNumero(rs.getInt("numero_endereco"));
				e.setComplemento(rs.getString("complemento_endereco"));
				r.setEndereco(e);

				responsaveis.add(r);

			}

		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return responsaveis;

	}

	public Responsavel buscarResponsavel(String cpf)
	{
		Connection con = null;
		Responsavel r = null;
		try
		{

			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "SELECT * from responsaveis,enderecos where responsaveis.cpf_responsaveis='" + cpf + "' and responsaveis.enderecosidendereco_responsaveis=enderecos.idendereco;";
			//busca um responsável e seu endereço pelo seu cpf
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
			{
				r = new Responsavel();
				r.setCPF(rs.getString("cpf_responsaveis"));
				r.setNome(rs.getString("nome_responsaveis"));
				r.setTel_com(rs.getString("tel_com_responsaveis"));
				r.setRamal(rs.getInt("ramal_responsaveis"));
				r.setCelular(rs.getString("celular_responsaveis"));
				r.setRg(rs.getString("rg_responsaveis"));

				Endereco e = new Endereco();
				e.setIdEndereco(rs.getInt("idendereco"));
				e.setTipo(rs.getString("tipo_endereco"));
				e.setEstado(rs.getString("estado_endereco"));
				e.setCidade(rs.getString("cidade_endereco"));
				e.setBairro(rs.getString("bairro_endereco"));
				e.setRua(rs.getString("rua_endereco"));
				e.setCEP(rs.getString("cep_endereco"));
				e.setNumero(rs.getInt("numero_endereco"));
				e.setComplemento(rs.getString("complemento_endereco"));
				r.setEndereco(e);

			}

		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return r;
	}
	
	public List<Horario> buscarHorarios_Professor(Professor p)
	{
		Connection con = null;
		List<Horario> horario = new ArrayList<Horario>();
		try
		{

			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "SELECT * from horario_professor where professorcpf_horario='" + p.getCPF() + "' order by idhp;";
			//lista os horarios de um professor específico, identificado pelo cpf, ordenando-os pelos id's de seus horarios
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{

				Horario h = new Horario();
				h.setIdHorario(rs.getInt("idhp"));
				h.setEntrada(rs.getTime("entrada_horario_p"));
				h.setSaida(rs.getTime("saida_horario_p"));
				h.setDia(rs.getString("dia_horario_p"));
				h.setProfessor((buscarProfessores(rs.getString("professorcpf_horario"))));
				horario.add(h);

			}

		}
		catch (ClassNotFoundException | SQLException e)
		{
			e.printStackTrace();
		}

		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return horario;
	}
	
	public List<Horario> buscarHorarios_Professor()
	{
		Connection con = null;
		List<Horario> horario = new ArrayList<Horario>();
		try
		{

			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "SELECT * from horario_professor order by idhp;";
			//busca os horarios de todos os professores ordenando-os pelos id's dos horarios
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				Horario h = new Horario();
				h.setIdHorario(rs.getInt("idhp"));
				h.setEntrada(rs.getTime("entrada_horario_p"));
				h.setSaida(rs.getTime("saida_horario_p"));
				h.setDia(rs.getString("dia_horario_p"));
				h.setProfessor((buscarProfessores(rs.getString("professorcpf_horario"))));
				horario.add(h);

			}

		}
		catch (ClassNotFoundException | SQLException e)
		{
			e.printStackTrace();
		}

		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return horario;
	}
	
	public Horario buscarHorarios_Professor(int id)
	{
		Connection con = null;
		Horario horario = null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "SELECT * from horario_professor,professores where horario_professor.idhp=" + id + " and horario_professor.professorcpf_horario=professores.cpf_professores;";
			//pesquisa um horario específico de um determinado professor ao pesquisar colocando um o idht para filtrar, retornando informações de ambos
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
			{
				horario = new Horario();
				horario.setIdHorario(rs.getInt("idhp"));
				horario.setEntrada(rs.getTime("entrada_horario_p"));
				horario.setSaida(rs.getTime("saida_horario_p"));
				horario.setDia(rs.getString("dia_horario_p"));	
				horario.setProfessor((buscarProfessores(rs.getString("professorcpf_horario"))));
				
				Professor p = new Professor();
				p.setCPF(rs.getString("cpf_professores"));
				p.setNome(rs.getString("nome_professores"));
				p.setTelefone(rs.getString("telefone_professores"));
				p.setCelular(rs.getString("celular_professores"));
				p.setSigla(rs.getString("sigla_professores"));
				p.setSenha(rs.getString("senha_professores"));
				p.setRg(rs.getString("rg_professores"));
				horario.setProfessor(p);	
			}

		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return horario;

	}

	public List<Horario> buscarHorarios_Turma(Turma t)
	{
		Connection con = null;
		List<Horario> horario = new ArrayList<Horario>();
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "SELECT * from horario_turma where idturma_t=" + t.getIdTurma() + " order by idht;";
			//busca uma lista dos horarios de uma turma especificada acima pelo id desta ordenando pelo id dos horarios
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				Horario h = new Horario();
				h.setIdHorario(rs.getInt("idht"));
				h.setEntrada(rs.getTime("entrada_horario_t"));
				h.setSaida(rs.getTime("saida_horario_t"));
				h.setTipo(rs.getString("tipo_horario_t"));
				horario.add(h);
			}
		}
		catch (ClassNotFoundException | SQLException e)
		{
			e.printStackTrace();
		}

		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return horario;
	}
	
	public Horario buscarHorarios_Turma(int id)
	{
		Connection con = null;
		Horario horario = null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "SELECT * from horario_turma where horario_turma.idht=" + id + " and horario_turma.idturma_t=turmas.idturma;";
			//pesquisa um determinado horario especificado acima de uma determinada turma que ele tem relação.
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
			{
				horario = new Horario();
				horario.setIdHorario(rs.getInt("idht"));
				horario.setEntrada(rs.getTime("entrada_horario"));
				horario.setSaida(rs.getTime("saida_horario"));
				horario.setTipo(rs.getString("tipo_horario_t"));
				
				Turma t = new Turma();
				t.setIdTurma(rs.getInt("idturma"));
				t.setGrau(rs.getInt("grau_turmas"));
				t.setClasse(rs.getString("classe_turmas"));
				t.setCurso(rs.getString("curso_turmas"));
				t.setPeriodo(rs.getString("período_turmas"));
				horario.setTurma(t);	
			}
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return horario;

	}
	
	public List<Aula> buscarAulas()
	{
		List<Aula> aulas = new ArrayList<Aula>();
		Connection con = null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "SELECT * from aula,materias,turmas,salas where salas.idsala=aula.salasidsala_aula and turmas.idturma=aula.turmasidturma_aula and materias.idmateria=aula.materiasidmateria_aula order by aula.idaula_aula;";
			/*faz uma lista de todas as aulas inseridas na database e, juntamente com elas, suas respectivas matérias, 
			 * turmas e salas; retornando todas as informações de todas estas tabelas relacionadas a aula, 
			 * ordenando a lista pelos id's das aulas
			 */
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				Aula n = new Aula();
				n.setIdAula(rs.getInt("idaula_aula"));
				n.setDia(rs.getString("dia_aula"));
				n.setInicio(rs.getTime("inicio_aula"));
				n.setFim(rs.getTime("fim_aula"));
				
				Sala s = new Sala();
				s.setIdSala(rs.getString("idsala"));
				s.setTipo(rs.getString("tipo_sala"));
				n.setSala(s);
				
				Materia m = new Materia();
				m.setIdMateria(rs.getInt("idmateria"));
				m.setNome(rs.getString("nome_materias"));
				m.setSigla(rs.getString("sigla_materias"));
				n.setMateria(m);
				
				Turma t = new Turma();
				t.setIdTurma(rs.getInt("idturma"));
				t.setGrau(rs.getInt("grau_turmas"));
				t.setClasse(rs.getString("classe_turmas"));
				t.setCurso(rs.getString("curso_turmas"));
				t.setPeriodo(rs.getString("período_turmas"));
				n.setTurma(t);
				
				aulas.add(n);
			}
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return aulas;
	}
	
	public Aula buscarAulas(int id)
	{
		Connection con = null;
		Aula a = null;
		try
		{

			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "SELECT * from aula where aula.idaula_aula=" + id + ";";
			//busca todas as informações referentes a uma aula específica dita pelo seu id 
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
			{
				a = new Aula();
				a.setIdAula(rs.getInt("idaula_aula"));
				a.setDia(rs.getString("dia_aula"));
				a.setInicio(rs.getTime("inicio_aula"));
				a.setFim(rs.getTime("fim_aula"));
				
				Sala s = new Sala();
				s.setIdSala(rs.getString("idsala"));
				s.setTipo(rs.getString("tipo_sala"));
				a.setSala(s);
				
				Turma t = new Turma();
				t.setIdTurma(rs.getInt("idturma"));
				t.setGrau(rs.getInt("grau_turmas"));
				t.setClasse(rs.getString("classe_turmas"));
				t.setCurso(rs.getString("curso_turmas"));
				t.setPeriodo(rs.getString("período_turmas"));
				a.setTurma(t);
				
				Materia m = new Materia();
				m.setIdMateria(rs.getInt("idmateria"));
				m.setNome(rs.getString("nome_materias"));
				m.setSigla(rs.getString("sigla_materias"));
				a.setMateria(m);
			}

		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return a;

	}
	
	public List<Integer> buscarLimite(Materia m, Turma t){
		
		Connection con = null;
		List<Integer> ho = new ArrayList<Integer>();
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "SELECT * from materias_turmas where materiasidmateria_mt=" + m.getIdMateria() + " and turmasidturma_mt="+t.getIdTurma()+" order by materiasidmateria_mt;";
			/*busca o limite de aulas por semana que podem ser dadas, dados encontrado na tabela materias_turmas, 
			 * por isso busca-se os limites dos id's específicados da turma e materia, ordenando pelo id da matéria
			 */
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				Integer l;
				l=(rs.getInt("limite_aula_mt"));
				ho.add(l);

			}

		}
		catch (ClassNotFoundException | SQLException e)
		{
			e.printStackTrace();
		}

		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return ho;
	}

	public List<Integer> buscarPrioridade(Materia m, Turma t){
		
		Connection con = null;
		List<Integer> ho = new ArrayList<Integer>();
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "SELECT * from materias_turmas where materiasidmateria_mt=" + m.getIdMateria() + " and turmasidturma_mt="+t.getIdTurma()+" order by turmasidturma_mt;";
			/*busca a prioridade da forma de organização das turmas para ter determinada matéria, dados encontrado na tabela materias_turmas,
			 *por isso busca-se os limites dos id's específicados da turma e materia, ordenando pelo id da turma
			 */
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				Integer p;
				p=(rs.getInt("prioridade_mt"));
				ho.add(p);

			}

		}
		catch (ClassNotFoundException | SQLException e)
		{
			e.printStackTrace();
		}

		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return ho;
	}
	
	public Endereco buscarEnderecos(int id)
	{
		Connection con = null;
		Endereco enderecos = null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "SELECT * from enderecos where enderecos.idendereco=" + id + ";";
			//busca as informações de um endereço especificado pelo id colocado acima
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
			{	
				enderecos = new Endereco();
				enderecos.setIdEndereco(rs.getInt("idendereco"));
				enderecos.setTipo(rs.getString("tipo_endereco"));
				enderecos.setEstado(rs.getString("estado_endereco"));
				enderecos.setCidade(rs.getString("cidade_endereco"));
				enderecos.setBairro(rs.getString("bairro_endereco"));
				enderecos.setRua(rs.getString("rua_endereco"));
				enderecos.setCEP(rs.getString("cep_endereco"));
				enderecos.setNumero(rs.getInt("numero_endereco"));
				enderecos.setComplemento(rs.getString("complemento_endereco"));

			}
		}
		catch (ClassNotFoundException er)
		{
			er.printStackTrace();
		}
		catch (SQLException er)
		{
			er.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception er)
			{
				er.printStackTrace();
			}
		}

		return enderecos;
	}

	public List<Turma> buscarMaterias_Turmas(Materia m)
	{
		List<Turma> turmas = new ArrayList<Turma>();	
		Connection con = null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "SELECT * from materias_turmas where materiasidmateria_mt="+m.getIdMateria()+" order by turmasidturma_mt;";
			//busca as turmas que se relacionam a determinada matéria específicada acima pelo seu id
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				turmas.add(buscarTurmas(rs.getInt("turmasidturma_mt")));	
			}
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return turmas;
	}
	
	public List<Materia> buscarMaterias_Turmas(Turma t)
	{
		List<Materia> materia = new ArrayList<Materia>();	
		Connection con = null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "SELECT * from materias_turmas where turmasidturma_mt="+t.getIdTurma()+" order by materiasidmateria_mt;";
			//busca as matérias que se relacionam a determinada turma específicada acima pelo seu id
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				materia.add(buscarMaterias(rs.getInt("materiasidmateria_mt")));	
			}
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return materia;
	}
	
	
	public List<Materia> buscarProfessor_Materia(Professor p)
	{
		List<Materia> materias = new ArrayList<Materia>();	
		Connection con = null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "SELECT * from professores_materias where professorescpf_pm='"+p.getCPF()+"';";
			//busca as matérias relacionadas a um professor especificadopelo seu cpf inserido acima 
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				Materia m =new Materia();
				m.setIdMateria(rs.getInt("materiasidmateria_pm"));
				materias.add(m);
			}
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return materias;
	}
	
	public List<Turma> buscarDistinct_Grau()
	{
		List<Turma> turmas = new ArrayList<Turma>();
		Connection con = null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();  
			String sql = "SELECT * from turmas group by grau_turmas order by grau_turmas;"; 
			/*busca todas as informações da turma ordenando por grau e apenas mostrando as informações de um tipo de grau
			 * ou seja, se houverem dois graus iguais ele só mostrará as informações do primeiro
			 */
			ResultSet rs = stmt.executeQuery(sql); 
			while (rs.next())
			{
				Turma t = new Turma(); 
				t.setGrau(rs.getInt("grau_turmas"));
				t.setClasse(rs.getString("classe_turmas"));
				t.setCurso(rs.getString("curso_turmas"));
				t.setPeriodo(rs.getString("período_turmas"));
				t.setIdTurma(rs.getInt("idturma"));
				turmas.add(t); 
			}

		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return turmas;  
	}
		
	public Turma buscarTurmas_Alunos(String rm) 
	{
		Connection con = null;
		Turma turma = null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "SELECT * from turmas_alunos,turmas where turmas_alunos.alunosrm_ta='" + rm +"' and turmas_alunos.situacao_ta='' and turmas_alunos.turmasidturma_ta=turmas.idturma limit 1;"; 
			/*busca a relação das turmas com os alunos a partir do rm inserido no parâmetro do método e 
			 * pelo fato da variével situação estar vazia forçando-o a retornar apenas 1 resultado (limit 1)
			 */
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
			{
				turma = new Turma();				
				turma.setIdTurma(rs.getInt("idturma"));
				turma.setGrau(rs.getInt("grau_turmas"));
				turma.setClasse(rs.getString("classe_turmas"));
				turma.setCurso(rs.getString("curso_turmas"));
				turma.setPeriodo(rs.getString("período_turmas"));
			}

		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return turma;
	}

	public String buscarTurmas_Situacao(String rm) 
	{
		Connection con = null;
		String s = null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "SELECT * from turmas_alunos,turmas where turmas_alunos.alunosrm_ta='" + rm +"' and turmas_alunos.situacao_ta='' and turmas_alunos.turmasidturma_ta=turmas.idturma limit 1;"; 
			// busca a relação das turmas com alunos a partir do rm inserido no parâmetro do método obrigando-o a retornar apenas um resultado
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
			{
				s = rs.getString("situacao_ta");	
				//retorna a informação contida na variável situação
			}

		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return s;
	}

	public Integer buscarTurmas_Numero(String rm) 
	{
		Connection con = null;
		Integer n = null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "SELECT * from turmas_alunos,turmas where turmas_alunos.alunosrm_ta='" + rm +"' and turmas_alunos.situacao_ta='' and turmas_alunos.turmasidturma_ta=turmas.idturma limit 1;"; 
			// busca a relação das turmas com alunos a partir do rm inserido no parâmetro do método obrigando-o a retornar apenas um resultado
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
			{
				n = rs.getInt("numero_ta");		
				//retorna somente a informação contida na variável numero
			}

		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return n;
	}

	public List<AcaoRecente> buscarAcao_Recente()
	{
		List<AcaoRecente> acao = new ArrayList<AcaoRecente>();
		Connection con = null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "SELECT * from acoes_recentes,funcionarios where funcionarios.cpf_funcionarios=acoes_recentes.cpffuncionario_ar order by idar;";
			//busca uma lista de todas as ações recentes inseridas na tabela, ordenando pelos id's das ações
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				AcaoRecente a = new AcaoRecente();
				a.setId(rs.getInt("idar"));
				a.setAcao(rs.getString("texto_ac"));
				
				Funcionario f = new Funcionario();
				f.setNome(rs.getString("nome_funcionarios")); // retorna o nome do funcionário
				a.setFuncionario(f);
				acao.add(a);
			}
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return acao;
	}
	
	public List<Professor> buscarProf_Mat_Tur_Sal()
	{
		List<Professor> professores = new ArrayList<Professor>();
		Connection con=null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "SELECT * from prof_mat_tur_sal,professores,materias,turmas,salas prof_mat_tur_sal.cpfprof_pmts=professores.cpf_professores and prof_mat_tur_sal.idmat_pmts=materias.idmateria and prof_mat_tur_sal.idtur_pmts=turmas.idturma and prof_mat_tur_sal.idsala_pmts=salas.idsala;";
			/* busca todas as informações de todos os id's e cpf relacionados no prof_mat_tur_sal exibindo todas as informações 
			 * sem excessão. As restrições "where" servem para validar os cpf e id's buscados abaixo
			 */
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				Professor p = new Professor();
				p.setCPF(rs.getString("cpf_professores"));
				p.setNome(rs.getString("nome_professores"));
				p.setTelefone(rs.getString("telefone_professores"));
				p.setCelular(rs.getString("celular_professores"));
				p.setSigla(rs.getString("sigla_professores"));
				p.setSenha(rs.getString("senha_professores"));
				p.setRg(rs.getString("rg_professores"));
				
				Sala s = new Sala();
				s.setIdSala(rs.getString("idsala"));
				s.setTipo(rs.getString("tipo_sala"));
				p.setSala(s);
				
				Materia m = new Materia();
				m.setIdMateria(rs.getInt("idmateria"));
				m.setNome(rs.getString("nome_materias"));
				m.setSigla(rs.getString("sigla_materias"));
				p.setMateria(m);
				
				Turma t = new Turma();
				t.setIdTurma(rs.getInt("idturma"));
				t.setGrau(rs.getInt("grau_turmas"));
				t.setClasse(rs.getString("classe_turmas"));
				t.setCurso(rs.getString("curso_turmas"));
				t.setPeriodo(rs.getString("período_turmas"));
				p.setTurma(t);
				
				professores.add(p);
			}
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return professores;
	}
	
	/**métodos para inserção de dados*/
	public int inserir(Materia m)
	{
		int a = 0;//variável de retorno para confirmação de inserção
		Connection con = null;
		PreparedStatement ps = null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "insert into materias values(null,?,?);";
			//insere valores na matéria, o nulo representa o id (auto increment) e as interrogações são ações sequênciais programadas abaixo
			ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, m.getNome());
			ps.setString(2, m.getSigla());			
			ps.executeUpdate();	
			//executa as ações para a inserção de dados no parâmetro do método (a ordem importa e tem que ser igual a da database)
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next())
				a = rs.getInt(1); 
			//faz a ação da variável
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return a; //retorna a variável criada a acima
	}

	public int inserir(Endereco m)// insere dados na tabela endereço
	{
		int a = 0;
		Connection con = null;
		PreparedStatement ps = null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "insert into enderecos values(null,?,?,?,?,?,?,?,?);";
			ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, m.getTipo());
			ps.setString(2, m.getEstado());
			ps.setString(3, m.getCidade());
			ps.setString(4, m.getBairro());
			ps.setString(5, m.getRua());
			ps.setString(6, m.getCEP());
			ps.setInt(7, m.getNumero());
			ps.setString(8, m.getComplemento());
			ps.executeUpdate();			
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next())
				a = rs.getInt(1);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return a;
	}

	public String inserir(Funcionario m)// insere dados na tabela funcionários
	{
		String cpf = null;
		int a=0;
		Connection con = null;
		PreparedStatement ps = null;
		try
		{

			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			
			String sql = "insert into enderecos values(null,?,?,?,?,?,?,?,?);";
			ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, m.getEndereco().getTipo());
			ps.setString(2, m.getEndereco().getEstado());
			ps.setString(3, m.getEndereco().getCidade());
			ps.setString(4, m.getEndereco().getBairro());
			ps.setString(5, m.getEndereco().getRua());
			ps.setString(6, m.getEndereco().getCEP());
			ps.setInt(7, m.getEndereco().getNumero());
			ps.setString(8, m.getEndereco().getComplemento());
			ps.executeUpdate();	
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next())
				a = rs.getInt(1);
			
			//ao inserir um funcionário, já se pede, portanto, para inserir outras informações intríssecas a ele, como observado adiante também
			sql = "insert into funcionarios values(?,?,?,?,?,?,?,?,?);";
			ps = con.prepareStatement(sql);
			ps.setString(1, m.getCPF());
			ps.setString(2, m.getNome());
			ps.setString(3, m.getTelefone());
			ps.setString(4, m.getCelular());
			ps.setString(5, m.getSenha());
			ps.setString(6, m.getCargo());
			ps.setInt(7, a);
			ps.setString(8, m.getTipo());
			ps.setString(9, m.getRg());
			ps.executeUpdate();			
			
			
			cpf = m.getCPF();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return cpf;
	}

	public String inserir(Responsavel m)// insere dados na tabela responsáveis e seu endereço
	{
		int a = 0; 
		String cpf = null;
		Connection con = null;
		PreparedStatement ps = null;
		try
		{

			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			
			String sql = "insert into enderecos values(null,?,?,?,?,?,?,?,?);";
			ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, m.getEndereco().getTipo());
			ps.setString(2, m.getEndereco().getEstado());
			ps.setString(3, m.getEndereco().getCidade());
			ps.setString(4, m.getEndereco().getBairro());
			ps.setString(5, m.getEndereco().getRua());
			ps.setString(6, m.getEndereco().getCEP());
			ps.setInt(7, m.getEndereco().getNumero());
			ps.setString(8, m.getEndereco().getComplemento());
			ps.executeUpdate();	
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next())
				a = rs.getInt(1);
			
			sql = "insert into responsaveis values(?,?,?,?,?,?,?);";
			ps = con.prepareStatement(sql);
			ps.setString(1, m.getCPF());
			ps.setString(2, m.getNome());
			ps.setString(3, m.getTel_com());
			ps.setInt(4, m.getRamal());
			ps.setString(5, m.getCelular());
			ps.setInt(6, a);
			ps.setString(7, m.getRg());
			ps.executeUpdate();
			
			cpf = m.getCPF();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return cpf;
	}

	public String inserir(Aluno m)// insere dados na tabela alunos e seus responsáveis e endereço
	{
		int a = 0;
		String cpf = null;
		Connection con = null;
		PreparedStatement ps = null;
		try
		{

			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			
			String sql = "insert into enderecos values(null,?,?,?,?,?,?,?,?);";
			ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, m.getEndereco().getTipo());
			ps.setString(2, m.getEndereco().getEstado());
			ps.setString(3, m.getEndereco().getCidade());
			ps.setString(4, m.getEndereco().getBairro());
			ps.setString(5, m.getEndereco().getRua());
			ps.setString(6, m.getEndereco().getCEP());
			ps.setInt(7, m.getEndereco().getNumero());
			ps.setString(8, m.getEndereco().getComplemento());
			ps.executeUpdate();	
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next())
				a = rs.getInt(1);
				
			sql = "insert into responsaveis values(?,?,?,?,?,?,?);";
			ps = con.prepareStatement(sql);
			ps.setString(1, m.getResponsavel().getCPF());
			ps.setString(2, m.getResponsavel().getNome());
			ps.setString(3, m.getResponsavel().getTel_com());
			ps.setInt(4, m.getResponsavel().getRamal());
			ps.setString(5, m.getResponsavel().getCelular());
			ps.setInt(6, a);
			ps.setString(7, m.getResponsavel().getRg());
			ps.executeUpdate();
			
			sql = (m.getFoto()==null ? "insert into alunos values(?,?,null,?,?,?,?,?,?);" : "insert into alunos values(?,?,?,?,?,?,?,?,?);");
			if (m.getFoto() != null)
			{
			ByteArrayInputStream inStream = gerarImagem(m.getFoto());
			ps = con.prepareStatement(sql);
			//lógica para a inserção de fotos, verificando de esta é nula ou não; e, dependendo do caso, executando determinada ação
			ps.setString(1, m.getRM());
			ps.setString(2, m.getNome());
			ps.setBinaryStream(3,inStream, inStream.available());
			ps.setString(4, m.getCPF());
			ps.setString(5, m.getRg());
			ps.setString(6, m.getTelefone());
			ps.setString(7, m.getCelular());
			ps.setString(8, m.getResponsavel().getCPF());
			ps.setInt(9, a);
			}
			else
			{
			ps = con.prepareStatement(sql);
			ps.setString(1, m.getRM());
			ps.setString(2, m.getNome());
			ps.setString(3, m.getCPF());
			ps.setString(4, m.getRg());
			ps.setString(5, m.getTelefone());
			ps.setString(6, m.getCelular());
			ps.setString(7, m.getResponsavel().getCPF());
			ps.setInt(8, a);
			}
			ps.executeUpdate();
			
			cpf = m.getCPF();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return cpf;
	}

	public String inserir(Professor m)// insere dados na tabela professores e seu endereço
	{
		int a = 0;
		String cpf = null;
		Connection con = null;
		PreparedStatement ps = null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			
			String sql = "insert into enderecos values(null,?,?,?,?,?,?,?,?);";
			ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, m.getEndereco().getTipo());
			ps.setString(2, m.getEndereco().getEstado());
			ps.setString(3, m.getEndereco().getCidade());
			ps.setString(4, m.getEndereco().getBairro());
			ps.setString(5, m.getEndereco().getRua());
			ps.setString(6, m.getEndereco().getCEP());
			ps.setInt(7, m.getEndereco().getNumero());
			ps.setString(8, m.getEndereco().getComplemento());
			ps.executeUpdate();	
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next())
				a = rs.getInt(1);
			
			sql = "insert into professores values(?,?,?,?,?,?,?,?);";
			ps = con.prepareStatement(sql);
			ps.setString(1, m.getCPF());
			ps.setString(2, m.getNome());
			ps.setString(3, m.getTelefone());
			ps.setString(4, m.getCelular());
			ps.setString(5, m.getSigla());
			ps.setString(6, m.getSenha());
			ps.setInt(7, a);
			ps.setString(8, m.getRg());
			ps.executeUpdate();
			
			cpf = m.getCPF();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return cpf;
	}
	
	public int inserir_p(Horario m)// insere dados na tabela horário_professores
	{
		int a = 0;
		Connection con = null;
		PreparedStatement ps = null;
		try
		{

			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "insert into horario_professor values(null,?,?,?,?);";
			ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setTime(1, m.getEntrada());
			ps.setTime(2, m.getSaida());
			ps.setString(3, m.getDia());
			ps.setString(4, m.getProfessor().getCPF());
			ps.executeUpdate();	
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next())
				a = rs.getInt(1);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return a;
	}

	public int inserir_t(Horario m)// insere dados na tabela horario_turmas
	{
		int a = 0;
		Connection con = null;
		PreparedStatement ps = null;
		try
		{

			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "insert into horario_turma values(null,?,?,?,?);";
			ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setTime(1, m.getEntrada());
			ps.setTime(2, m.getSaida());
			ps.setInt(3, m.getTurma().getIdTurma());
			ps.setString(4, m.getTipo());
			ps.executeUpdate();	
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next())
				a = rs.getInt(1);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return a;
	}
	
	public String inserir(Sala m)// insere dados na tabela salas
	{
		String id = null;
		Connection con = null;
		PreparedStatement ps = null;
		try
		{

			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "insert into salas values(?,?);";
			ps = con.prepareStatement(sql);
			ps.setString(1, m.getIdSala());
			ps.setString(2, m.getTipo());
			ps.executeUpdate();
			
			id = m.getIdSala();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return id;
	}

	public int inserir(Turma m)// insere dados na tabela turmas
	{
		int a = 0;
		Connection con = null;
		PreparedStatement ps = null;
		try
		{

			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "insert into turmas values(null,?,?,?,?);";
			ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, m.getGrau());
			ps.setString(2, m.getClasse());
			ps.setString(3, m.getCurso());
			ps.setString(4, m.getPeriodo());
			ps.executeUpdate();	
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next())
				a = rs.getInt(1);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return a;
	}

	public int inserir(Materia m, Turma t, Integer l, Integer p)// insere dados na tabela Materia_Turmas
	{//variáveis criadas para a inserção dos atributos especiais do limite e prioridade
		int a = 0;
		Connection con = null;
		PreparedStatement ps = null;
		try
		{

			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "insert into materias_turmas values(?,?,?,?);";
			ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, m.getIdMateria());
			ps.setInt(2, t.getIdTurma());
			ps.setInt(3, l);
			ps.setInt(4, p);
			ps.executeUpdate();	
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next())
				a = rs.getInt(1);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return a;
	}

	public int inserir(Turma t,Aluno al,Integer n, String s)// insere dados na tabela Turmas_alunos
	{//variáveis criadas para os atributos especiais no numero e situação
		int a = 0;
		Connection con = null;
		PreparedStatement ps = null;
		try
		{

			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "insert into turmas_alunos values(?,?,?,?);";
			ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, t.getIdTurma());
			ps.setString(2, al.getRM());
			ps.setInt(3, n);
			ps.setString(4, s);
			ps.executeUpdate();	
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next())
				a = rs.getInt(1);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return a;
	}

	public int inserir(Professor p,Materia m,Turma t,Sala s)// insere dados na tabela prof_mat_tur_sal
	{
		int a = 0;
		Connection con = null;
		PreparedStatement ps = null;
		try
		{

			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "insert into prof_mat_tur_sal values(?,?,?,?);";
			ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, p.getCPF());
			ps.setInt(2, m.getIdMateria());
			ps.setInt(3, t.getIdTurma());
			ps.setString(4, s.getIdSala());
			ps.executeUpdate();	
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next())
				a = rs.getInt(1);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return a;
	}
	
	public int inserir(Professor p,Materia m)// insere dados na tabela Professores_materias
	{
		int a = 0;
		Connection con = null;
		PreparedStatement ps = null;
		try
		{

			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "insert into professores_materias values(?,?);";
			ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, p.getCPF());
			ps.setInt(2, m.getIdMateria());
			ps.executeUpdate();	
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next())
				a = rs.getInt(1);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return a;
	}
	
	public int inserir(Aula au)// insere dados na tabela aulas
	{
		int a = 0;
		Connection con = null;
		PreparedStatement ps = null;
		try
		{

			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "insert into aula values(null,?,?,?,?,?,?);";
			ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, au.getSala().getIdSala());
			ps.setInt(2, au.getTurma().getIdTurma());
			ps.setInt(3, au.getMateria().getIdMateria());
			ps.setString(4, au.getDia());
			ps.setTime(5, au.getInicio());
			ps.setTime(6, au.getFim());
			ps.executeUpdate();	
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next())
				a = rs.getInt(1);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return a;
	}
	
	public int inserir(Aula au,Professor p)// insere dados na tabela aulas_professores
	{
		int a = 0;
		Connection con = null;
		PreparedStatement ps = null;
		try
		{

			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "insert into aulas_professores values(?,?);";
			ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, p.getCPF());
			ps.setInt(2, au.getIdAula());
			ps.executeUpdate();	
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next())
				a = rs.getInt(1);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return a;
	}

	public int inserir(AcaoRecente ar)// insere dados na tabela acoes_recentes
	{
		int a = 0;
		Connection con = null;
		PreparedStatement ps = null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			Statement stmt = con.createStatement();
			String sql = "insert into acoes_recentes values(null,?,?);";
			ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, ar.getFuncionario().getCPF());
			ps.setString(2, ar.getAcao());
			ps.executeUpdate();	
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next())
				a = rs.getInt(1);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return a;
	}

	
	/**métodos para alteração de dados*/
	//altera a matéria já existente no banco, assim como todos os "alterar" que virão em sequência
	public void Alterar(Materia m)
	{
		Connection con = null;
		PreparedStatement ps = null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			String sqlUpdate = "update materias set nome_materias=?, sigla_materias=? where idmateria=?;";
			//comando para alteração de dados na database (o id não é alterado, ele é o indicador do que se deve alterar; isso em todos)
			ps = con.prepareStatement(sqlUpdate);
			ps.setString(1, m.getNome());
			ps.setString(2, m.getSigla());
			ps.setInt(3, m.getIdMateria());
			//novamente, por causa das interogações nos códigos sql, a ordem dos "ps" deve ser seguida lógicamente como se escreveu acima
			ps.executeUpdate();
			
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}//este método não retorna nada
	}
	
	public void Alterar(Funcionario m)//altera os dados do funcionário selecionado que já tenha sido inserido
	{
		Connection con = null;
		PreparedStatement ps = null;
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			String sqlUpdate = "update funcionarios set nome_funcionarios=?, telefone_funcionarios=?, celular_funcionarios=?, senha_funcionarios=?, cargo_funcionarios=?, enderecosidendereco_funcionarios=?, tipo_funcionarios=?, rg_funcionarios=? where cpf_funcionarios=?;";
			ps = con.prepareStatement(sqlUpdate);
			ps.setString(1, m.getNome());
			ps.setString(2, m.getTelefone());
			ps.setString(3, m.getCelular());
			ps.setString(4, m.getSenha());
			ps.setString(5, m.getCargo());
			ps.setInt(6, m.getEndereco().getIdEndereco());
			ps.setString(7, m.getTipo());
			ps.setString(8, m.getRg());
			ps.setString(9, m.getCPF());
			ps.executeUpdate();
			//ao se alterar as informações do funcionário, ter-se-á que se mudar os dados do endereço, tabela com chave estrangeira escrita nele (isto acontecerá com todos)
			sqlUpdate = "update enderecos set tipo_endereco=?, estado_endereco=?, cidade_endereco=?, bairro_endereco=?, rua_endereco=?, cep_endereco=?, numero_endereco=?, complemento_endereco=? where idendereco=?;";
			ps = con.prepareStatement(sqlUpdate);
			ps.setString(1, m.getEndereco().getTipo());
			ps.setString(2, m.getEndereco().getEstado());
			ps.setString(3, m.getEndereco().getCidade());
			ps.setString(4, m.getEndereco().getBairro());
			ps.setString(5, m.getEndereco().getRua());
			ps.setString(6, m.getEndereco().getCEP());
			ps.setInt(7, m.getEndereco().getNumero());
			ps.setString(8, m.getEndereco().getComplemento());
			ps.setInt(9, m.getEndereco().getIdEndereco());
			ps.executeUpdate();
			
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void Alterar(Responsavel m)//altera os dados do responsável selecionado que já tenha sido inserido
	{
		Connection con = null;
		PreparedStatement ps = null;
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			String sqlUpdate = "update responsaveis set nome_responsaveis=?, tel_com_responsaveis=?, ramal_responsaveis=?, celular_responsaveis=?, enderecosidendereco_responsaveis=?, rg_responsaveis=? where cpf_responsaveis=?;";
			ps = con.prepareStatement(sqlUpdate);
			ps.setString(1, m.getNome());
			ps.setString(2, m.getTel_com());
			ps.setInt(3, m.getRamal());
			ps.setString(4, m.getCelular());
			ps.setInt(5, m.getEndereco().getIdEndereco());
			ps.setString(6, m.getCPF());
			ps.setString(7, m.getRg());
			ps.executeUpdate();
			
			sqlUpdate = "update enderecos set tipo_endereco=?, estado_endereco=?, cidade_endereco=?, bairro_endereco=?, rua_endereco=?, cep_endereco=?, numero_endereco=?, complemento_endereco=? where idendereco=?;";
			ps = con.prepareStatement(sqlUpdate);
			ps.setString(1, m.getEndereco().getTipo());
			ps.setString(2, m.getEndereco().getEstado());
			ps.setString(3, m.getEndereco().getCidade());
			ps.setString(4, m.getEndereco().getBairro());
			ps.setString(5, m.getEndereco().getRua());
			ps.setString(6, m.getEndereco().getCEP());
			ps.setInt(7, m.getEndereco().getNumero());
			ps.setString(8, m.getEndereco().getComplemento());
			ps.setInt(9, m.getEndereco().getIdEndereco());
			ps.executeUpdate();
			
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void Alterar(Aluno m)//altera os dados do aluno selecionado que já tenha sido inserido
	{
		Connection con = null;
		PreparedStatement ps = null;
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			String sqlUpdate = "update alunos set nome_aluno=?, cpf_aluno=?, foto_aluno=?, rg_aluno=?, tel_aluno=?, cel_aluno=?, cpf_responsavel_aluno=? where rm_aluno=?;";
			ps = con.prepareStatement(sqlUpdate);
			ps.setString(1, m.getNome());
			ps.setString(2, m.getCPF());
			ByteArrayInputStream inStream = gerarImagem(m.getFoto());
			ps.setBinaryStream(3, inStream, inStream.available());
			ps.setString(4, m.getRg());
			ps.setString(5, m.getTelefone());
			ps.setString(6, m.getCelular());
			ps.setString(7, m.getResponsavel().getCPF());
			ps.setString(8, m.getRM());
			ps.executeUpdate();
			//neste caso se altera os dados responsável e do endereço
			sqlUpdate = "update responsaveis set nome_responsaveis=?, tel_com_responsaveis=?, ramal_responsaveis=?, celular_responsaveis=?, rg_responsaveis=? where cpf_responsaveis=?;";
			ps = con.prepareStatement(sqlUpdate);
			ps.setString(1, m.getResponsavel().getNome());
			ps.setString(2, m.getResponsavel().getTel_com());
			ps.setInt(3, m.getResponsavel().getRamal());
			ps.setString(4, m.getResponsavel().getCelular());
			ps.setString(5, m.getResponsavel().getCPF());
			ps.setString(6, m.getResponsavel().getRg());
			ps.executeUpdate();
			
			sqlUpdate = "update enderecos set tipo_endereco=?, estado_endereco=?, cidade_endereco=?, bairro_endereco=?, rua_endereco=?, cep_endereco=?, numero_endereco=?, complemento_endereco=? where idendereco=?;";
			ps = con.prepareStatement(sqlUpdate);
			ps.setString(1, m.getEndereco().getTipo());
			ps.setString(2, m.getEndereco().getEstado());
			ps.setString(3, m.getEndereco().getCidade());
			ps.setString(4, m.getEndereco().getBairro());
			ps.setString(5, m.getEndereco().getRua());
			ps.setString(6, m.getEndereco().getCEP());
			ps.setInt(7, m.getEndereco().getNumero());
			ps.setString(8, m.getEndereco().getComplemento());
			ps.setInt(9, m.getEndereco().getIdEndereco());
			ps.executeUpdate();
			
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void Alterar(Professor m)//altera os dados do professor selecionado que já tenha sido inserido
	{
		Connection con = null;
		PreparedStatement ps = null;
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			String sqlUpdate = "update professores set nome_professores=?, telefone_professores=?, celular_professores=?, sigla_professores=?, senha_professores=?, rg_professores=? where  cpf_professores=?;";
			ps = con.prepareStatement(sqlUpdate);
			ps.setString(1, m.getNome());
			ps.setString(2, m.getTelefone());
			ps.setString(3, m.getCelular());
			ps.setString(4, m.getSigla());
			ps.setString(5, m.getSenha());
			ps.setString(6, m.getRg());
			ps.setString(7, m.getCPF());
			ps.executeUpdate();
			
			sqlUpdate = "update enderecos set tipo_endereco=?, estado_endereco=?, cidade_endereco=?, bairro_endereco=?, rua_endereco=?, cep_endereco=?, numero_endereco=?, complemento_endereco=? where idendereco=?;";
			ps = con.prepareStatement(sqlUpdate);
			ps.setString(1, m.getEndereco().getTipo());
			ps.setString(2, m.getEndereco().getEstado());
			ps.setString(3, m.getEndereco().getCidade());
			ps.setString(4, m.getEndereco().getBairro());
			ps.setString(5, m.getEndereco().getRua());
			ps.setString(6, m.getEndereco().getCEP());
			ps.setInt(7, m.getEndereco().getNumero());
			ps.setString(8, m.getEndereco().getComplemento());
			ps.setInt(9, m.getEndereco().getIdEndereco());
			ps.executeUpdate();
			
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void Alterar_p(Horario m)//altera os dados do horário do professor selecionado que já tenha sido inserido
	{
		Connection con = null;
		PreparedStatement ps = null;
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			String sqlUpdate = "update horario_professor set entrada_horario_p=?, saida_horario_p=?, dia_horario_p=?, professorcpf_horario=? where idhp=?;";
			ps = con.prepareStatement(sqlUpdate);
			ps.setTime(1, m.getEntrada());
			ps.setTime(2, m.getSaida());
			ps.setString(3, m.getDia());
			ps.setString(4, m.getProfessor().getCPF());
			ps.setInt(5, m.getIdHorario());
			ps.executeUpdate();
			
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void Alterar_t(Horario m)//altera os dados do horário da turma selecionado que já tenha sido inserido
	{
		Connection con = null;
		PreparedStatement ps = null;
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			String sqlUpdate = "update horario_turma set entrada_horario_t=?, saida_horario_t=?, idturma_t=?, tipo_horario_t=? where idht=?;";
			ps = con.prepareStatement(sqlUpdate);
			ps.setTime(1, m.getEntrada());
			ps.setTime(2, m.getSaida());
			ps.setInt(3, m.getTurma().getIdTurma());
			ps.setString(4, m.getTipo());
			ps.setInt(5, m.getIdHorario());
			ps.executeUpdate();
			
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void Alterar(Sala m)//altera os dados da sala selecionada que já tenha sido inserida
	{
		Connection con = null;
		PreparedStatement ps = null;
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			String sqlUpdate = "update salas set tipo_sala=? where idsala=?;";
			ps = con.prepareStatement(sqlUpdate);
			ps.setString(1, m.getTipo());
			ps.setString(2, m.getIdSala());
			ps.executeUpdate();
			
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void Alterar(Turma m)//altera os dados da turma selecionada que já tenha sido inserida
	{
		Connection con = null;
		PreparedStatement ps = null;
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			String sqlUpdate = "update turmas set grau_turmas=?, classe_turmas=?, curso_turmas=?, período_turmas=? where idturma=?;";
			ps = con.prepareStatement(sqlUpdate);
			ps.setInt(1, m.getGrau());
			ps.setString(2, m.getClasse());
			ps.setString(3, m.getCurso());
			ps.setString(4, m.getPeriodo());
			ps.setInt(5, m.getIdTurma());
			ps.executeUpdate();
			
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void Alterar(Aula au)//altera os dados da aula selecionada que já tenha sido inserida
	{
		Connection con = null;
		PreparedStatement ps = null;
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			String sqlUpdate = "update aula set salasidsala_aula=?, turmasidturma_aula=?, materiasidmateria_aula=?, dia_aula=?, inicio_aula=?, fim_aula=? where idaula_aula=?;";
			ps = con.prepareStatement(sqlUpdate);
			ps.setString(1, au.getSala().getIdSala());
			ps.setInt(2, au.getTurma().getIdTurma());
			ps.setInt(3, au.getMateria().getIdMateria());
			ps.setString(4, au.getDia());
			ps.setTime(5, au.getInicio());
			ps.setTime(5, au.getFim());
			ps.setInt(6, au.getIdAula());
			ps.executeUpdate();
			
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void Alterar(Endereco m)//altera os dados do endereço selecionado que já tenha sido inserido
	{
		Connection con = null;
		PreparedStatement ps = null;
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			String sqlUpdate = "update enderecos set tipo_endereco=?, estado_endereco=?, cidade_endereco=?, bairro_endereco=?, rua_endereco=?, cep_endereco=?, numero_endereco=?, complemento_endereco=? where idendereco=?;";
			ps = con.prepareStatement(sqlUpdate);
			ps.setString(1, m.getTipo());
			ps.setString(2, m.getEstado());
			ps.setString(3, m.getCidade());
			ps.setString(4, m.getBairro());
			ps.setString(5, m.getRua());
			ps.setString(6, m.getCEP());
			ps.setInt(7, m.getNumero());
			ps.setString(8, m.getComplemento());
			ps.setInt(9, m.getIdEndereco());
			ps.executeUpdate();
			
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void Alterar(Aula au,Professor pe,Aula a,Professor p)
	{	/* altera os dados da relação do Professor e Aula selecionada que já tenha sido inserida
		 * as variáveis criadas servem para: as duas primeiras -> O que já está na database; as duas última -> Para o que se quer alterar 
		 * isto servirá para todas as alterações de relções entre tabelas
		 */
		Connection con = null;
		PreparedStatement ps = null;
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			String sqlUpdate = "update aulas_professores aulasidaula_ap=?, professorescpf_ap=?  where professorescpf_ap=? and aulasidaula_ap=?;";
			ps = con.prepareStatement(sqlUpdate);
			ps.setInt(1, a.getIdAula());
			ps.setString(2, p.getCPF());
			ps.setString(3, pe.getCPF());
			ps.setInt(4, au.getIdAula());
			ps.executeUpdate();
			
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void Alterar(Materia ma,Turma tu,Materia m,Turma t, Integer l, Integer p)
	//altera os dados da relação da Materia e Turma selecionada que já tenha sido inserida e suas variáveis especiais
	{
		Connection con = null;
		PreparedStatement ps = null;
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			String sqlUpdate = "update materias_turmas materiasidmateria_mt=?, turmasidturmas_mt=?, limite_aula_mt=?, prioridade_mt=?  where materiasidmateria_mt=? and turmasidturmas_mt=?;";
			ps = con.prepareStatement(sqlUpdate);
			ps.setInt(1, m.getIdMateria());
			ps.setInt(2, t.getIdTurma());
			ps.setInt(3, l);
			ps.setInt(4, p);
			ps.setInt(5, ma.getIdMateria());
			ps.setInt(6, tu.getIdTurma());
			ps.executeUpdate();
			
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void Alterar(Professor pe,Materia ma,Professor p,Materia m)
	{//altera os dados da relação do Professor e Matéria selecionada que já tenha sido inserida
		Connection con = null;
		PreparedStatement ps = null;
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			String sqlUpdate = "update professores_materias professorescpf_pm=?, materiasidmateria_pm=?  where professorescpf_pm=? and materiasidmateria_pm=? ;";
			ps = con.prepareStatement(sqlUpdate);
			ps.setString(1, p.getCPF());
			ps.setInt(2, m.getIdMateria());
			ps.setString(3, pe.getCPF());
			ps.setInt(4, ma.getIdMateria());			
			ps.executeUpdate();
			
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void Alterar(Professor pe,Materia ma,Turma te,Sala sa,Professor p,Materia m,Turma t,Sala s)
	{//altera os dados da relação do Professor, Matéria, Turma e Sala selecionada que já tenha sido inserida
		Connection con = null;
		PreparedStatement ps = null;
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			String sqlUpdate = "update prof_mat_tur_sal cpfprof_pmts=?, idmat_pmts=?, idtur_pmts=?, idsala_pmts=?  where cpfprof_pmts=? and idmat_pmts=? and idtur_pmts=? and idsala_pmts=? ;";
			ps = con.prepareStatement(sqlUpdate);
			ps.setString(1, p.getCPF());
			ps.setInt(2, m.getIdMateria());
			ps.setInt(3, t.getIdTurma());
			ps.setString(4, s.getIdSala());
			ps.setString(5, pe.getCPF());
			ps.setInt(6, ma.getIdMateria());
			ps.setInt(7, te.getIdTurma());
			ps.setString(8, sa.getIdSala());
			ps.executeUpdate();
			
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void Alterar(Turma te,Aluno al,String s,Integer n)
	{//altera os dados da relação da Turma e Aluno selecionada que já tenha sido inserida
		Connection con = null;
		PreparedStatement ps = null;
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(ip, usuario, senha);
			String sqlUpdate = "update turmas_alunos set situacao_ta=?, numero_ta=? where turmasidturma_ta=? and alunosrm_ta=? and situacao_ta='';";
			ps = con.prepareStatement(sqlUpdate);
			ps.setString(1, s);
			ps.setInt(2, n);
			ps.setInt(3, te.getIdTurma());
			ps.setString(4, al.getRM());			
			ps.executeUpdate();
			
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	/**métodos para Deleção de dados*/
	//Exclui os dados da matéria e todos os seu relacionados a partir de um id selecionado 
    public void Excluir(Materia usu){
		Connection con = null;
		PreparedStatement ps = null;
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(ip,usuario,senha);			
			String sqlDelete = "delete from materias_turmas where materiasidmateria_mt=?;";
			//deleta a relação do id com o materias_turmas
			ps = con.prepareStatement(sqlDelete);
			ps.setInt(1,usu.getIdMateria());
			ps.executeUpdate();
					
			sqlDelete = "delete from professores_materias where materiasidmateria_pm=?;";
			//deleta a relação do id com o professores_materias
			ps = con.prepareStatement(sqlDelete);
			ps.setInt(1,usu.getIdMateria());
			ps.executeUpdate();
			
			sqlDelete = "delete from prof_mat_tur_sal where idmat_pmts=?;";
			//deleta a relação do id com o prof_mat_tur_sal
			ps = con.prepareStatement(sqlDelete);
			ps.setInt(1,usu.getIdMateria());
			ps.executeUpdate();
			
			sqlDelete = "delete from faltas_materias where materiaidmateria=?;";
			//deleta a relação do id com o faltas_materias
			ps = con.prepareStatement(sqlDelete);
			ps.setInt(1,usu.getIdMateria());
			ps.executeUpdate();
			
			sqlDelete = "delete from faltas where materiasidmateria_falta=?;";
			//deleta a relação do id com o faltas
			ps = con.prepareStatement(sqlDelete);
			ps.setInt(1,usu.getIdMateria());
			ps.executeUpdate();					
			
			Statement stmt = con.createStatement();
			String sql = "SELECT * from aula where materiasidmateria_aula="+usu.getIdMateria()+";";
			ResultSet rs=stmt.executeQuery(sql); 
			//busca o id da sala para deletar a relação da sala com o professor, por que é necessário apagar a relaçaõ da matéria com a sala também
			List<Integer> ret = new ArrayList<Integer>();
			while (rs.next())
			{
				ret.add(rs.getInt("idaula_aula"));
				//retorna o id da sala
			}

			for(Integer i : ret)
			{
				sqlDelete = "delete from aulas_professores where aulasidaula_ap=?;";
				//apaga a relação aulas_professores
				ps = con.prepareStatement(sqlDelete);
				ps.setInt(1,i);
				ps.executeUpdate();
			}
			
			sqlDelete = "delete from aula where materiasidmateria_aula=?;";
			//deleta a relação do id com as salas
			ps = con.prepareStatement(sqlDelete);
			ps.setInt(1,usu.getIdMateria());
			ps.executeUpdate();
			
			sqlDelete = "delete from notas where materiasidmateria_nota=?;";
			//deleta a relação do id com as notas
			ps = con.prepareStatement(sqlDelete);
			ps.setInt(1,usu.getIdMateria());
			ps.executeUpdate();
			
			sqlDelete = "delete from materias where idmateria=?;";
			//deleta a tabela da matéria que corresponde ao id selecionado
			ps = con.prepareStatement(sqlDelete);
			ps.setInt(1,usu.getIdMateria());
			ps.executeUpdate();	
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void Excluir(Funcionario usu){
		Connection con = null;
		PreparedStatement ps = null;
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(ip,usuario,senha);
			String sqlDelete = "delete from funcionarios where cpf_funcionarios=?;";
			//deleta a tabela do funcionário que corresponde ao cpf selecionado
			ps = con.prepareStatement(sqlDelete);
			ps.setString(1,usu.getCPF());
			ps.executeUpdate();
			
			sqlDelete = "delete from enderecos where idendereco=?;";
			//deleta a tabela do endereço que se relaciona ao cpf selecionado
			ps = con.prepareStatement(sqlDelete);
			ps.setInt(1,usu.getEndereco().getIdEndereco());
			ps.executeUpdate();
			
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void Excluir(Responsavel usu){
		Connection con = null;
		PreparedStatement ps = null;
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(ip,usuario,senha);
			String sqlDelete = "delete from alunos where cpf_responsavel_aluno=?;";
			//deleta a tabela do alunos que corresponde ao cpf selecionado
			ps = con.prepareStatement(sqlDelete);
			ps.setString(1,usu.getCPF());
			ps.executeUpdate();
			
			sqlDelete = "delete from enderecos where idendereco=?;";
			//deleta a tabela do endreço que se relaciona ao cpf selecionado
			ps = con.prepareStatement(sqlDelete);
			ps.setInt(1,usu.getEndereco().getIdEndereco());
			ps.executeUpdate();
			
			sqlDelete = "delete from responsaveis where cpf_responsaveis=?;";
			//deleta a tabela do responsável que corresponde ao cpf selecionado
			ps = con.prepareStatement(sqlDelete);
			ps.setString(1, usu.getCPF());
			ps.executeUpdate();
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void Excluir(Aluno usu){
		Connection con = null;
		PreparedStatement ps = null;
		int quant = 0;
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(ip,usuario,senha);
			
			Statement stmt = con.createStatement();
			String sql = "SELECT * from alunos where cpf_responsavel_aluno='"+usu.getResponsavel().getCPF()+"';";
			//busca o cpf do responsável a partir do rm do aluno para corrigir um erro que pode ocorrer
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				quant++;
			}
			if (quant <= 1){
			String sqlDelete = "delete from responsaveis where cpf_responsaveis=?;";
			ps = con.prepareStatement(sqlDelete);
			ps.setString(1,usu.getResponsavel().getCPF());
			ps.executeUpdate();		
			}
			//estes comandos evitam erros quando tenta se excluir um responsável que se relaciona com mais de um aluno (além do selecionado)
			String sqlDelete = "delete from turmas_alunos where alunosrm_ta=?;";
			ps = con.prepareStatement(sqlDelete);
			ps.setString(1,usu.getRM());
			ps.executeUpdate();
			
			sqlDelete = "delete from alunos where rm_aluno=?;";
			//deleta a tabela do alunos que corresponde ao rm selecionado
			ps = con.prepareStatement(sqlDelete);
			ps.setString(1,usu.getRM());
			ps.executeUpdate();
			
			sqlDelete = "delete from enderecos where idendereco=?;";
			//deleta a tabela do endereço que se relaciona ao rm selecionado
			ps = con.prepareStatement(sqlDelete);
			ps.setInt(1,usu.getEndereco().getIdEndereco());
			ps.executeUpdate();
		
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void Excluir(Professor usu){
		Connection con = null;
		PreparedStatement ps = null;
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(ip,usuario,senha);			
						
			String sqlDelete = "delete from aulas_professores where professorescpf_ap=?;";
			//deleta a tabela da relação aulas_professores que corresponde ao cpf selecionado
			ps = con.prepareStatement(sqlDelete);
			ps.setString(1,usu.getCPF());
			ps.executeUpdate();
			
			sqlDelete = "delete from horario_professor where professorcpf_horario=?;";
			//deleta a tabela do horário_professor que corresponde ao cpf selecionado
			ps = con.prepareStatement(sqlDelete);
			ps.setString(1,usu.getCPF());
			ps.executeUpdate();
			
			sqlDelete = "delete from professores_materias where professorescpf_pm=?;";
			//deleta a tabela da relação do professor_materias que corresponde ao cpf selecionado
			ps = con.prepareStatement(sqlDelete);
			ps.setString(1,usu.getCPF());
			ps.executeUpdate();
			
			sqlDelete = "delete from prof_mat_tur_sal where cpfprof_pmts=?;";
			//deleta a tabela da relação do prof_mat_tur_sal que corresponde ao cpf selecionado
			ps = con.prepareStatement(sqlDelete);
			ps.setString(1,usu.getCPF());
			ps.executeUpdate();
			
			sqlDelete = "delete from professores where cpf_professores=?;";
			//deleta a tabela do professor que corresponde ao cpf selecionado
			ps = con.prepareStatement(sqlDelete);
			ps.setString(1,usu.getCPF());
			ps.executeUpdate();
			
			sqlDelete = "delete from enderecos where idendereco=?;";
			//deleta a tabela do endereço que se relaciona ao cpf selecionado
			ps = con.prepareStatement(sqlDelete);
			ps.setInt(1,usu.getEndereco().getIdEndereco());
			ps.executeUpdate();
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void limpar(){
		Connection con = null;
		PreparedStatement ps = null;
		try{
			//apaga determinadas relações inteiras
			Class.forName(driver);
			con = DriverManager.getConnection(ip,usuario,senha);
			String sqlDelete = "TRUNCATE TABLE aulas_professores;";
			ps = con.prepareStatement(sqlDelete);
			ps.executeUpdate();
			sqlDelete = "set FOREIGN_KEY_CHECKS=0;";
			ps = con.prepareStatement(sqlDelete);
			ps.executeUpdate();
			sqlDelete = "truncate table aula;";
			ps = con.prepareStatement(sqlDelete);
			ps.executeUpdate();
			sqlDelete = "set FOREIGN_KEY_CHECKS=1;";
			ps = con.prepareStatement(sqlDelete);
			ps.executeUpdate();
			// truncate table aula; set FOREIGN_KEY_CHECKS=1;
			
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void Excluir_p(Horario usu){
		Connection con = null;
		PreparedStatement ps = null;
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(ip,usuario,senha);
			String sqlDelete = "delete from horario_professor where idhp=?;";
			//exclui o horario_professor que corresponde ao id deste que se escreveu no parâmetro
			ps = con.prepareStatement(sqlDelete);
			ps.setInt(1,usu.getIdHorario());
			ps.executeUpdate();
			
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void Excluir_t(Horario usu){
		Connection con = null;
		PreparedStatement ps = null;
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(ip,usuario,senha);
			String sqlDelete = "delete from horario_turma where idht=?;";
			//exclui o horario_turma que corresponde ao id deste que se escreveu no parâmetro
			ps = con.prepareStatement(sqlDelete);
			ps.setInt(1,usu.getIdHorario());
			ps.executeUpdate();
			
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void Excluir(Sala usu){
		Professor p = null;
		Connection con = null;
		PreparedStatement ps = null;
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(ip,usuario,senha);
			Statement stmt = con.createStatement();			
			String sqlDelete = "delete from prof_mat_tur_sal where idsala_pmts=?;";
			//exclui a relação prof_mat_tur_sal que corresponde ao id da sala que se escreveu no parâmetro
			ps = con.prepareStatement(sqlDelete);
			ps.setString(1,usu.getIdSala());
			ps.executeUpdate();
			
			Statement stmt1 = con.createStatement();
			String sql = "SELECT * from aula where salasidsala_aula="+usu.getIdSala()+";";
			//busca o id da aula que estiver relacionado ao id da sala inserido acima
			ResultSet rs=stmt1.executeQuery(sql);
			List<Integer> ret = new ArrayList<Integer>();
			while (rs.next())
			{
				ret.add(rs.getInt("idaula_aula"));
			}

			for(Integer i : ret)
			{ 
				//deletará o aulas professores a partir do id da sala encontrado
				sqlDelete = "delete from aulas_professores where aulasidaula_ap=?;";
				ps = con.prepareStatement(sqlDelete);
				ps.setInt(1,i);
				ps.executeUpdate();
			}
			
			sqlDelete = "delete from aula where salasidsala_aula=?;";
			//deleta a aula que estiver relacionada ao id da sala referenciado
			ps = con.prepareStatement(sqlDelete);
			ps.setString(1,usu.getIdSala());
			ps.executeUpdate();
			
			String sqlDelete1 = "delete from salas where idsala=?;";
			//deleta as informações da sala do id inserido
			ps = con.prepareStatement(sqlDelete);
			ps.setString(1,usu.getIdSala());
			ps.executeUpdate();
			
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void Excluir(Turma usu){
		Connection con = null;
		PreparedStatement ps = null;
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(ip,usuario,senha);		
			String sqlDelete = "delete from turmas_alunos where turmasidturma_ta=?;";
			//exclui a relação da turma do id a ser deletado com o aluno, excluindo-o também
			ps = con.prepareStatement(sqlDelete);
			ps.setInt(1,usu.getIdTurma());
			ps.executeUpdate();
			
			sqlDelete = "delete from prof_mat_tur_sal where idtur_pmts=?;";
			//exclui a relação da turma do id a ser deletado com o prof_mat_tur_sal, excluindo-o também
			ps = con.prepareStatement(sqlDelete);
			ps.setInt(1,usu.getIdTurma());
			ps.executeUpdate();
			
			sqlDelete = "delete from materias_turmas where turmasidturma_mt=?;";
			//exclui a relação da turma do id a ser deletado com a matéria excluindo-a também
			ps = con.prepareStatement(sqlDelete);
			ps.setInt(1,usu.getIdTurma());
			ps.executeUpdate();
			
			sqlDelete = "delete from horario_turma where idturma_t=?;";
			//exclui a relação da turma do id a ser deletado com o horário, excluindo-o também
			ps = con.prepareStatement(sqlDelete);
			ps.setInt(1,usu.getIdTurma());
			ps.executeUpdate();
			
			sqlDelete = "delete from turmas where idturma=?;";
			//exclui a turma com o id selecionado no parâmetro
			ps = con.prepareStatement(sqlDelete);
			ps.setInt(1,usu.getIdTurma());
			ps.executeUpdate();
			
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void Excluir(Aula usu){
		Connection con = null;
		PreparedStatement ps = null;
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(ip,usuario,senha);
			String sqlDelete = "delete from aula where idaula_aula=?;";
			//apaga a aula a partir do id selecionado
			ps = con.prepareStatement(sqlDelete);
			ps.setInt(1,usu.getIdAula());
			ps.executeUpdate();
			
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void Excluir(Endereco usu){
		Connection con = null;
		PreparedStatement ps = null;
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(ip,usuario,senha);
			String sqlDelete = "delete from alunos where enderecosidedereco_aluno=?;";
			//apaga o endreço que está relacionando à Pessoa Aluno
			ps = con.prepareStatement(sqlDelete);
			ps.setInt(1,usu.getIdEndereco());
			
			sqlDelete = "delete from funcionarios where enderecosidendereco_funcionarios=?;";
			//apaga o endreço que está relacionando à Pessoa Funcionário
			ps = con.prepareStatement(sqlDelete);
			ps.setInt(1,usu.getIdEndereco());
			
			sqlDelete = "delete from professores where enderecosidendereco_professores=?;";
			//apaga o endreço que está relacionando à Pessoa Professor
			ps = con.prepareStatement(sqlDelete);
			ps.setInt(1,usu.getIdEndereco());
			
			sqlDelete = "delete from responsaveis where enderecosidendereco_responsaveis=?;";
			//apaga o endreço que está relacionando à Pessoa Responsável
			ps = con.prepareStatement(sqlDelete);
			ps.setInt(1,usu.getIdEndereco());
			
			sqlDelete = "delete from enderecos where idendereco=?;";
			//apaga o endreço que se pede pelo id 
			ps = con.prepareStatement(sqlDelete);
			ps.setInt(1,usu.getIdEndereco());
			ps.executeUpdate();
			
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void Excluir(Aula a,Professor p){
		Connection con = null;
		PreparedStatement ps = null;
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(ip,usuario,senha);
			String sqlDelete = "delete from aulas_professores where professorescpf_ap=? and aulasidaula_ap=?;";
			//apaga só a relação aula_professor dependendo do id de ambos
			ps = con.prepareStatement(sqlDelete);
			ps.setString(1,p.getCPF());
			ps.setInt(2,a.getIdAula());
			ps.executeUpdate();
		
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void Excluir(Materia m,Turma t){
		Connection con = null;
		PreparedStatement ps = null;
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(ip,usuario,senha);
			String sqlDelete = "delete from materias_turmas where materiasidmateria_mt=? and turmasidturma_mt=?;";
			//apaga só a relação materias_turmas dependendo do id de ambos
			ps = con.prepareStatement(sqlDelete);
			ps.setInt(1,m.getIdMateria());
			ps.setInt(2,t.getIdTurma());
			ps.executeUpdate();
		
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void Excluir(Professor p,Materia m){
		Connection con = null;
		PreparedStatement ps = null;
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(ip,usuario,senha);
			String sqlDelete = "delete from professores_materias where professorescpf_pm =? and materiasidmateria_pm=?;";
			//apaga só a relação professor_materias dependendo do id de ambos
			ps = con.prepareStatement(sqlDelete);
			ps.setString(1,p.getCPF());
			ps.setInt(2,m.getIdMateria());
			ps.executeUpdate();
		
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void Excluir(Professor p,Materia m,Turma t,Sala s){
		Connection con = null;
		PreparedStatement ps = null;
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(ip,usuario,senha);
			String sqlDelete = "delete from prof_mat_tur_sal where cpfprof_pmts =? and idmat_pmts=? and idtur_pmts=? and idsala_pmts=?;";
			//apaga só a relação prof_mat_tur_sal dependendo do id de todos
			ps = con.prepareStatement(sqlDelete);
			ps.setString(1,p.getCPF());
			ps.setInt(2,m.getIdMateria());
			ps.setInt(3, t.getIdTurma());
			ps.setString(4, s.getIdSala());
			ps.executeUpdate();
		
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void Excluir(Turma t, Aluno a){
		Connection con = null;
		PreparedStatement ps = null;
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(ip,usuario,senha);
			String sqlDelete = "delete from turmas_alunos where turmasidturma_ta=? and alunosrm_ta=? ;";
			//apaga só a relação turmas_alunos dependendo do id de ambos
			ps = con.prepareStatement(sqlDelete);
			ps.setInt(1, t.getIdTurma());
			ps.setString(2, a.getRM());
			ps.executeUpdate();
		
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void Excluir(Professor p, Sala s){
		Connection con = null;
		PreparedStatement ps = null;
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(ip,usuario,senha);
			String sqlDelete = "delete from prof_sala where cpfprof_ps=? and salaidsala_ps=? ;";
			//apaga só a relação professor_sala dependendo do id de ambos
			ps = con.prepareStatement(sqlDelete);
			ps.setString(1, p.getCPF());
			ps.setString(2, s.getIdSala());
			ps.executeUpdate();
		
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ps.close();
				con.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	
	public String getSenha()
	{
		return senha;
	}

	public void setSenha(String senha)
	{
		this.senha = senha;
	}

	public String getUsuario()
	{
		return usuario;
	}

	public void setUsuario(String usuario)
	{
		this.usuario = usuario;
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = "jdbc:mysql://"+ip+":3306/"+database;
	}

	public String getDriver()
	{
		return driver;
	}

	public void setDriver(String driver)
	{
		this.driver = driver;
	}
	//Get's e Set's para a conxão bruta com o banco de dados
	public ByteArrayInputStream gerarImagem(Image i) throws IOException
	{
		BufferedImage bImage = new BufferedImage(i.getWidth(null), i.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics bg = bImage.getGraphics();
		bg.drawImage(i, 0, 0, null);
		bg.dispose();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(bImage, "jpeg", out);
		byte[] buf = out.toByteArray();

		return new ByteArrayInputStream(buf);
		
		//método para a leitura e o funcionamento dos comando para buscar,inserir, alterar e excluir imagens; no caso, a foto do aluno
	}

}
