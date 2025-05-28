package sistema.controles.ModulosDoSistema.Funcionalidades;


import sistema.controles.ModulosDoSistema.EntidadesSistema.EstudanteEspecial;
import sistema.controles.ModulosDoSistema.EntidadesSistema.EstudanteNormal;
import sistema.controles.ModulosDoSistema.EntidadesSistema.Estudante;
import sistema.controles.ModulosDoSistema.EntidadesSistema.Materia;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CadastroDeAlunosSistema implements Persistivel {
    private Map<String, Estudante> alunos;

    public CadastroDeAlunosSistema() {
        this.alunos = new HashMap<>();
    }

    public boolean cadastrarAluno(String nome, String matricula, String cursoGraduacao, boolean especial) {
        if (alunos.containsKey(matricula)) {
            return false;
        }

        Estudante estudante = especial ? new EstudanteEspecial(nome, matricula, cursoGraduacao) : new EstudanteNormal(nome, matricula, cursoGraduacao);

        alunos.put(matricula, estudante);
        return true;
    }

    public boolean editarAluno(String matricula, String novoNome, String novoCurso) {
        Estudante estudante = alunos.get(matricula);
        if (estudante == null) {
            return false;
        }

        if (novoNome != null && !novoNome.trim().isEmpty()) {
            estudante.setNome(novoNome);
        }
        if (novoCurso != null && !novoCurso.trim().isEmpty()) {
            estudante.setCursoDeEnsinoSuperior(novoCurso);
        }
        return true;
    }

    public Estudante buscarAluno(String matricula) {
        return alunos.get(matricula);
    }

    public List<Estudante> listarAlunos() {
        return new ArrayList<>(alunos.values());
    }

    public boolean matricularEmDisciplina(String matricula, Materia materia) {
        Estudante estudante = alunos.get(matricula);
        return estudante != null && estudante.matricular(materia);
    }

    public boolean trancarDisciplina(String matricula, Materia materia) {
        Estudante estudante = alunos.get(matricula);
        return estudante != null && estudante.trancarDisciplina(materia);
    }

    public boolean trancarSemestre(String matricula) {
        Estudante estudante = alunos.get(matricula);
        if (estudante != null) {
            estudante.trancarSemestre();
            return true;
        }
        return false;
    }

    @Override
    public boolean Salvo(String arquivo) {
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(arquivo), "UTF-8"))) {

            for (Estudante estudante : alunos.values()) {
                String tipo = estudante instanceof EstudanteEspecial ? "ESPECIAL" : "NORMAL";
                String curso = estudante.getCursoDeEnsinoSuperior() != null ?
                        estudante.getCursoDeEnsinoSuperior() : "Nao informado";

                writer.println(String.format("%s;%s;%s;%s",
                        tipo,
                        estudante.getNome(),
                        estudante.getMatricula(),
                        curso));
            }
            return true;
        } catch (IOException e) {
            System.err.println("Erro ao salvar alunos: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean carregamento(String arquivo) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(arquivo), "UTF-8"))) {

            alunos.clear();
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length >= 4) {
                    String tipo = partes[0];
                    String nome = partes[1];
                    String matricula = partes[2];
                    String curso = partes[3];

                    boolean especial = "ESPECIAL".equalsIgnoreCase(tipo);
                    cadastrarAluno(nome, matricula, curso, especial);
                }
            }
            return true;
        } catch (FileNotFoundException e) {
            // Arquivo não existe ainda, não é um erro
            return true;
        } catch (IOException e) {
            System.err.println("Erro ao carregar alunos: " + e.getMessage());
            return false;
        }
    }
}

