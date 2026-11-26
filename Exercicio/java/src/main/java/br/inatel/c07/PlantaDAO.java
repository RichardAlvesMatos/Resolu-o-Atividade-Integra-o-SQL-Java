package br.inatel.c07;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// EXERCÍCIO: complete cada método no espaço indicado (onde está o TODO).
// Use sempre PreparedStatement com parâmetros (?) e feche os recursos no finally.
public class PlantaDAO extends ConnectionDAO {

    // Questão 1: inserir uma planta.
    // Deve inserir na tabela planta e devolver true se der certo, false se houver erro.
    public boolean inserirPlanta(Planta planta) {
        if (connectToDb() == null) {
            return false;
        }
        String sql = "INSERT INTO planta (nome, especie, preco, estoque) VALUES (?, ?, ?, ?)";
        try {
            pst = connection.prepareStatement(sql);
            pst.setString(1, planta.getNome());
            pst.setString(2, planta.getEspecie());
            pst.setDouble(3, planta.getPreco());
            pst.setInt(4, planta.getEstoque());
            pst.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao inserir a planta: " + e.getMessage());
            return false;
        } finally {
            fecharRecursos();
        }
    }

    // Questão 2: listar todas as plantas, ordenadas pelo id.
    // Deve devolver uma lista de objetos Planta (use rs.next() para percorrer).
    public List<Planta> listarPlantas() {
        List<Planta> plantas = new ArrayList<>();
        if (connectToDb() == null) {
            return plantas;
        }
        String sql = "SELECT id, nome, especie, preco, estoque FROM planta ORDER BY id";
        try {
            st = connection.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                plantas.add(new Planta(rs.getInt("id"), rs.getString("nome"),
                        rs.getString("especie"), rs.getDouble("preco"), rs.getInt("estoque")));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar as plantas: " + e.getMessage());
        } finally {
            fecharRecursos();
        }
        return plantas;
    }

    // Questão 3: buscar as plantas de uma espécie.
    // Deve devolver a lista das plantas cuja espécie seja igual ao parâmetro.
    public List<Planta> buscarPorEspecie(String especie) {
        List<Planta> plantas = new ArrayList<>();
        if (connectToDb() == null) {
            return plantas;
        }
        String sql = "SELECT id, nome, especie, preco, estoque FROM planta WHERE especie = ? ORDER BY id";
        try {
            pst = connection.prepareStatement(sql);
            pst.setString(1, especie);
            rs = pst.executeQuery();
            while (rs.next()) {
                plantas.add(new Planta(rs.getInt("id"), rs.getString("nome"),
                        rs.getString("especie"), rs.getDouble("preco"), rs.getInt("estoque")));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar pela espécie: " + e.getMessage());
        } finally {
            fecharRecursos();
        }
        return plantas;
    }

    // Questão 4: atualizar o preço de uma planta pelo id.
    // Deve devolver true se alguma linha foi alterada (executeUpdate() > 0), senão false.
    public boolean atualizarPreco(int id, double novoPreco) {
        if (connectToDb() == null) {
            return false;
        }
        String sql = "UPDATE planta SET preco = ? WHERE id = ?";
        try {
            pst = connection.prepareStatement(sql);
            pst.setDouble(1, novoPreco);
            pst.setInt(2, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar o preço: " + e.getMessage());
            return false;
        } finally {
            fecharRecursos();
        }
    }

    // Questão 5: remover uma planta pelo id.
    // Deve devolver true se alguma linha foi removida, senão false.
    public boolean removerPlanta(int id) {
        if (connectToDb() == null) {
            return false;
        }
        String sql = "DELETE FROM planta WHERE id = ?";
        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao remover a planta: " + e.getMessage());
            return false;
        } finally {
            fecharRecursos();
        }
    }

    // Questão 6 (Desafio): valor total do estoque.
    // Deve devolver a soma de preco * estoque de todas as plantas.
    // Dica: SELECT COALESCE(SUM(preco * estoque), 0) AS total FROM planta
    public double valorTotalEstoque() {
        if (connectToDb() == null) {
            return 0.0;
        }
        String sql = "SELECT COALESCE(SUM(preco * estoque), 0) AS total FROM planta";
        try {
            st = connection.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao calcular o valor do estoque: " + e.getMessage());
        } finally {
            fecharRecursos();
        }
        return 0.0;
    }

    // Já pronto: libera os recursos na ordem inversa da abertura.
    // Chame este método dentro do finally de cada questão.
    private void fecharRecursos() {
        try {
            if (rs != null) rs.close();
            if (pst != null) pst.close();
            if (st != null) st.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            System.out.println("Erro ao fechar recursos: " + e.getMessage());
        }
        rs = null;
        pst = null;
        st = null;
    }
}