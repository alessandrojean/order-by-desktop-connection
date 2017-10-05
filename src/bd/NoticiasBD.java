package bd;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import model.Endereco;
import model.Funcionario;
import model.Noticia;
import model.Tag;

public class NoticiasBD
{
	private String ip;
	private String username;
	private String password;
	private String database;
	private String driver = "com.mysql.jdbc.Driver";

	public NoticiasBD()
	{
		this("", "", "", "");
	}

	public NoticiasBD(String i, String u, String p, String d)
	{
		ip = i;
		username = u;
		password = p;
		database = d;
	}

	public int NovaNoticia(Noticia n)
	{
		int retorno = 0;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/" + database, username, password);
			ps = con.prepareStatement((n.getImagem() != null ? "insert into noticias values (null,?,?,?,?,NOW(),?,?);" : "insert into noticias values (null,?,?,null,?,NOW(),?,?);"), PreparedStatement.RETURN_GENERATED_KEYS);

			if (n.getImagem() != null)
			{
				ByteArrayInputStream inStream = gerarImagem(n.getImagem());
				ps.setBinaryStream(3, inStream, inStream.available());
				ps.setString(1, n.getTitulo());
				ps.setString(2, n.getConteudo());
				ps.setString(4, n.getCriador().getCPF());
				ps.setBoolean(5, n.isDestaque());
				ps.setString(6, n.getTipo());
			}
			else
			{
				ps.setString(1, n.getTitulo());
				ps.setString(2, n.getConteudo());
				ps.setString(3, n.getCriador().getCPF());
				ps.setBoolean(4, n.isDestaque());
				ps.setString(5, n.getTipo());
			}

			ps.executeUpdate();

			rs = ps.getGeneratedKeys();
			if (rs.next())
				retorno = rs.getInt(1);

			for (Tag t : n.getTags())
			{
				ps = con.prepareStatement("insert into noticias_tags values (null,?,?);");
				ps.setString(1, t.getTag());
				ps.setInt(2, retorno);

				ps.executeUpdate();
			}
		}
		catch (ClassNotFoundException | SQLException | IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (con != null)
					con.close();
				if (ps != null)
					ps.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

		return retorno;
	}

	public void EditarNoticia(Noticia n)
	{
		Connection con = null;
		PreparedStatement ps = null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/" + database, username, password);
			ps = con.prepareStatement("update noticias set titulo_noticia=?,conteudo_noticia=?,imagem_noticia=?,criador_noticia=?,data_noticia=?,destaque_noticia=?,tipo_noticia=? where id_noticia=?;");

			ps.setString(1, n.getTitulo());
			ps.setString(2, n.getConteudo());
			
			if(n.getImagem()!=null)
			{
				ByteArrayInputStream inStream = gerarImagem(n.getImagem());
				ps.setBinaryStream(3, inStream, inStream.available());
			}
			else
			{
				ps.setBinaryStream(3, null);
			}

			ps.setString(4, n.getCriador().getCPF());
			ps.setTimestamp(5, n.getData());
			ps.setBoolean(6, n.isDestaque());
			ps.setString(7, n.getTipo());
			ps.setInt(8, n.getId());

			ps.executeUpdate();

			ps = con.prepareStatement("delete from noticias_tags where noticia_tag=?;");
			ps.setInt(1, n.getId());
			ps.executeUpdate();

			for (Tag t : n.getTags())
			{
				ps = con.prepareStatement("insert into noticias_tags values (null,?,?);");
				ps.setString(1, t.getTag());
				ps.setInt(2, n.getId());

				ps.executeUpdate();
			}
		}
		catch (ClassNotFoundException | SQLException | IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (con != null)
					con.close();
				if (ps != null)
					ps.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}

	public void DeletarNoticia(Noticia n)
	{
		Connection con = null;
		PreparedStatement ps = null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/" + database, username, password);
			ps = con.prepareStatement("delete from noticias where id_noticia=?;");
			ps.setInt(1, n.getId());
			ps.executeUpdate();

			ps = con.prepareStatement("delete from noticias_tags where noticia_tag=?;");
			ps.setInt(1, n.getId());
			ps.executeUpdate();
		}
		catch (ClassNotFoundException | SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (con != null)
					con.close();
				if (ps != null)
					ps.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}

	public List<Noticia> ListarNoticias()
	{
		List<Noticia> retorno = new ArrayList<Noticia>();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/" + database, username, password);
			stmt = con.createStatement();

			rs = stmt.executeQuery("select * from noticias,funcionarios,enderecos where noticias.criador_noticia=funcionarios.cpf_funcionarios and funcionarios.enderecosidendereco_funcionarios=enderecos.idendereco order by noticias.id_noticia desc;");
			while (rs.next())
			{
				Noticia n = new Noticia();
				n.setId(rs.getInt("id_noticia"));
				n.setTitulo(rs.getString("titulo_noticia"));
				n.setConteudo(rs.getString("conteudo_noticia"));
				n.setData(rs.getTimestamp("data_noticia"));
				n.setDestaque(rs.getBoolean("destaque_noticia"));
				n.setTipo(rs.getString("tipo_noticia"));

				if (rs.getBytes("imagem_noticia") != null)
				{
					ImageIcon ii = new ImageIcon(rs.getBytes("imagem_noticia"));
					n.setImagem(ii.getImage());
				}

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

				n.setCriador(f);

				retorno.add(n);
			}

			for (int i = 0; i < retorno.size(); i++)
			{
				rs = stmt.executeQuery("select * from noticias_tags where noticia_tag=" + retorno.get(i).getId() + ";");

				List<Tag> tags = new ArrayList<Tag>();

				while (rs.next())
				{
					Tag t = new Tag();
					t.setId(rs.getInt("id_tag"));
					t.setTag(rs.getString("nome_tag"));

					tags.add(t);
				}

				Noticia n = retorno.get(i);
				n.setTags(tags);

				retorno.set(i, n);
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
				if (con != null)
					con.close();
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

		return retorno;
	}

	public List<Noticia> ListarNoticias(int id)
	{
		List<Noticia> retorno = new ArrayList<Noticia>();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			Class.forName(driver);
			con = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/" + database, username, password);
			stmt = con.createStatement();

			rs = stmt.executeQuery("select * from noticias,funcionarios,enderecos where noticias.criador_noticia=funcionarios.cpf_funcionarios and funcionarios.enderecosidendereco_funcionarios=enderecos.idendereco and noticias.id_noticia=" + id + " limit 1;");
			while (rs.next())
			{
				Noticia n = new Noticia();
				n.setId(rs.getInt("id_noticia"));
				n.setTitulo(rs.getString("titulo_noticia"));
				n.setConteudo(rs.getString("conteudo_noticia"));
				n.setData(rs.getTimestamp("data_noticia"));
				n.setDestaque(rs.getBoolean("destaque_noticia"));
				n.setTipo(rs.getString("tipo_noticia"));

				if (rs.getBytes("imagem_noticia") != null)
				{
					ImageIcon ii = new ImageIcon(rs.getBytes("imagem_noticia"));
					n.setImagem(ii.getImage());
				}

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

				n.setCriador(f);

				retorno.add(n);
			}

			for (int i = 0; i < retorno.size(); i++)
			{
				rs = stmt.executeQuery("select * from noticias_tags where noticia_tag=" + retorno.get(i).getId() + ";");

				List<Tag> tags = new ArrayList<Tag>();

				while (rs.next())
				{
					Tag t = new Tag();
					t.setId(rs.getInt("id_tag"));
					t.setTag(rs.getString("nome_tag"));

					tags.add(t);
				}

				retorno.get(i).setTags(tags);
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
				if (con != null)
					con.close();
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

		return retorno;
	}

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
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getDatabase()
	{
		return database;
	}

	public void setDatabase(String database)
	{
		this.database = database;
	}
}
