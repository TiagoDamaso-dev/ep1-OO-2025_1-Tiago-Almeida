package sistema.controles.ModulosDoSistema.Funcionalidades;


import sistema.controles.ModulosDoSistema.EntidadesSistema.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


public class GerenciarAvaliacaoAlunos implements Persistivel {
    private Map<String, Map<String, Turma.NotasAluno>> notasPorTurma;
    private GerenciamentoDeDisciplinas gerenciamentoDeDisciplinas;

    public GerenciarAvaliacaoAlunos() {
        this.notasPorTurma = new HashMap<>();
    }

    public void setDisciplinaController(GerenciamentoDeDisciplinas gerenciamentoDeDisciplinas) {
        this.gerenciamentoDeDisciplinas = gerenciamentoDeDisciplinas;
    }


    public boolean RegistrarNotasAlunos(String codigoTurma, Estudante estudante, double p1, double p2, double p3,
                                        double listas, double seminario) {
        // Validação de notas
        if (p1 < 0 || p1 > 10 || p2 < 0 || p2 > 10 || p3 < 0 || p3 > 10 ||
            listas < 0 || listas > 10 || seminario < 0 || seminario > 10) {
            throw new IllegalArgumentException("Todas as notas devem estar entre 0 e 10");
        }

        if (estudante instanceof EstudanteEspecial) {
            return false; // Alunos especiais não recebem notas
        }

        Turma turma = getTurma(codigoTurma);
        if (turma == null) {
            return false;
        }

        if (!turma.getAlunosMatriculados().contains(estudante)) {
            throw new IllegalStateException("Estudante não está matriculado na turma");
        }

        turma.lancarNotas(estudante, p1, p2, p3, listas, seminario);
        return true;
    }


    public boolean RegistrarFreqAlunos(String codigoTurma, Estudante estudante, int totalAulas, int presencas) {
        if (totalAulas <= 0) {
            throw new IllegalArgumentException("Total de aulas deve ser maior que zero");
        }
        if (presencas < 0 || presencas > totalAulas) {
            throw new IllegalArgumentException("Número de presenças inválido");
        }

        Turma turma = getTurma(codigoTurma);
        if (turma == null) {
            return false;
        }

        if (!turma.getAlunosMatriculados().contains(estudante)) {
            throw new IllegalStateException("Estudante não está matriculado na turma");
        }

        turma.lancarPresenca(estudante, totalAulas, presencas);
        return true;
    }


    public String RelatorioDesempenhoTurma(String codigoTurma) {
        Turma turma = getTurma(codigoTurma);
        if (turma == null) {
            return "Turma não encontrada";
        }

        StringBuilder relatorio = new StringBuilder();
        relatorio.append(String.format("Relatório da Turma: %s\n", codigoTurma));
        relatorio.append(String.format("Materia: %s\n", turma.getDisciplina().getNomeMateria()));
        relatorio.append(String.format("ProfessorFCTE: %s\n", turma.getProfessor().getNome()));
        relatorio.append(String.format("Semestre: %s\n\n", turma.getSemestre()));

        for (Estudante estudante : turma.getAlunosMatriculados()) {
            Turma.NotasAluno notas = turma.getNotasAluno(estudante);
            if (notas != null) {
                relatorio.append(String.format("Estudante: %s (%s)\n", estudante.getNome(), estudante.getMatricula()));
                relatorio.append(notas.toString()).append("\n\n");
            }
        }

        return relatorio.toString();
    }


    public String criarRelatorioDasDisciplinas(String codigoDisciplina, GerenciamentoDeDisciplinas gerenciamentoDeDisciplinas) {
        Materia materia = gerenciamentoDeDisciplinas.ProcuraDisciplinaSistema(codigoDisciplina);
        if (materia == null) {
            return "Materia não encontrada";
        }

        StringBuilder relatorio = new StringBuilder();
        relatorio.append(String.format("Relatório da Materia: %s (%s)\n",
                materia.getNomeMateria(), materia.getCodigoMateria()));
        relatorio.append(String.format("Carga Horária: %d horas\n\n", materia.getCargaHoraria()));

        for (Turma turma : materia.getTurmas()) {
            relatorio.append(String.format("Turma: %s\n", turma.getCodigo()));
            relatorio.append(String.format("ProfessorFCTE: %s\n", turma.getProfessor().getNome()));
            relatorio.append(String.format("Semestre: %s\n", turma.getSemestre()));
            relatorio.append(String.format("Alunos Matriculados: %d\n\n",
                    turma.getAlunosMatriculados().size()));
        }

        return relatorio.toString();
    }


    public String RelatorioProfessor(String matriculaProfessor, List<Turma> todasTurmas) {
        List<Turma> turmasProfessor = todasTurmas.stream()
                .filter(t -> t.getProfessor().getMatricula().equals(matriculaProfessor))
                .collect(Collectors.toList());

        if (turmasProfessor.isEmpty()) {
            return "ProfessorFCTE não encontrado ou sem turmas";
        }

        ProfessorFCTE professorFCTE = turmasProfessor.get(0).getProfessor();
        StringBuilder relatorio = new StringBuilder();
        relatorio.append(String.format("Relatório do ProfessorFCTE: %s (%s)\n",
                professorFCTE.getNome(), professorFCTE.getMatricula()));
        relatorio.append(String.format("Departamento: %s\n\n", professorFCTE.getDepartamento()));

        for (Turma turma : turmasProfessor) {
            relatorio.append(String.format("Turma: %s\n", turma.getCodigo()));
            relatorio.append(String.format("Materia: %s\n", turma.getDisciplina().getNomeMateria()));
            relatorio.append(String.format("Semestre: %s\n", turma.getSemestre()));
            relatorio.append(String.format("Alunos Matriculados: %d\n\n",
                    turma.getAlunosMatriculados().size()));
        }

        return relatorio.toString();
    }


    public String gerarBoletimAluno(String matriculaAluno, String semestre, boolean incluirDadosTurma,
                                  List<Turma> todasTurmas) {
        List<Turma> turmasAluno = todasTurmas.stream()
                .filter(t -> t.getSemestre().equals(semestre))
                .filter(t -> t.getAlunosMatriculados().stream()
                        .anyMatch(a -> a.getMatricula().equals(matriculaAluno)))
                .collect(Collectors.toList());

        if (turmasAluno.isEmpty()) {
            return "Estudante não encontrado ou sem matrículas no semestre";
        }

        Estudante estudante = turmasAluno.get(0).getAlunosMatriculados().stream()
                .filter(a -> a.getMatricula().equals(matriculaAluno))
                .findFirst().orElse(null);

        if (estudante == null) {
            return "Estudante não encontrado";
        }

        StringBuilder boletim = new StringBuilder();
        boletim.append(String.format("Boletim do Estudante: %s (%s)\n", estudante.getNome(), estudante.getMatricula()));
        boletim.append(String.format("Curso: %s\n", estudante.getCursoDeEnsinoSuperior()));
        boletim.append(String.format("Semestre: %s\n\n", semestre));

        for (Turma turma : turmasAluno) {
            boletim.append(String.format("Materia: %s (%s)\n",
                    turma.getDisciplina().getNomeMateria(), turma.getDisciplina().getCodigoMateria()));

            if (incluirDadosTurma) {
                boletim.append(String.format("ProfessorFCTE: %s\n", turma.getProfessor().getNome()));
                boletim.append(String.format("Modalidade: %s\n",
                        turma.isPresencial() ? "Presencial" : "Remota"));
                boletim.append(String.format("Carga Horária: %d horas\n",
                        turma.getDisciplina().getCargaHoraria()));
            }

            Turma.NotasAluno notas = turma.getNotasAluno(estudante);
            if (notas != null) {
                boletim.append(notas.toString()).append("\n\n");
            }
        }

        return boletim.toString();
    }

    private Turma getTurma(String codigoTurma) {
        return gerenciamentoDeDisciplinas.buscarTurma(codigoTurma);
    }


    @Override
    public boolean Salvo(String arquivo) {
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(arquivo), "UTF-8"))) {
            for (Map.Entry<String, Map<String, Turma.NotasAluno>> turmaEntry : notasPorTurma.entrySet()) {
                String codigoTurma = turmaEntry.getKey();
                Map<String, Turma.NotasAluno> notasAlunos = turmaEntry.getValue();

                for (Map.Entry<String, Turma.NotasAluno> alunoEntry : notasAlunos.entrySet()) {
                    String matriculaAluno = alunoEntry.getKey();
                    Turma.NotasAluno notas = alunoEntry.getValue();

                    // Formato: codigoTurma;matriculaAluno;p1;p2;p3;listas;seminario;totalAulas;presencas
                    writer.printf("%s;%s;%.1f;%.1f;%.1f;%.1f;%.1f;%d;%d%n",
                        codigoTurma, matriculaAluno,
                        notas.getProva1(), notas.getProva2(), notas.getProva3(),
                        notas.getListas(), notas.getSeminario(),
                        notas.getAulasTotais(), notas.getPresencas());
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean carregamento(String arquivo) {
        notasPorTurma.clear();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(arquivo), "UTF-8"))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length == 9) {
                    String codigoTurma = partes[0];
                    String matriculaAluno = partes[1];
                    double p1 = Double.parseDouble(partes[2]);
                    double p2 = Double.parseDouble(partes[3]);
                    double p3 = Double.parseDouble(partes[4]);
                    double listas = Double.parseDouble(partes[5]);
                    double seminario = Double.parseDouble(partes[6]);
                    int totalAulas = Integer.parseInt(partes[7]);
                    int presencas = Integer.parseInt(partes[8]);

                    Map<String, Turma.NotasAluno> notasAlunos = notasPorTurma.computeIfAbsent(
                        codigoTurma, k -> new HashMap<>());

                    Turma.NotasAluno notas = new Turma.NotasAluno();
                    notas.setNotas(p1, p2, p3, listas, seminario);
                    notas.setFrequencia(totalAulas, presencas);
                    notasAlunos.put(matriculaAluno, notas);
                }
            }
            return true;
        } catch (IOException e) {
            // Se o arquivo não existir na primeira execução, não é erro
            if (!(e instanceof FileNotFoundException)) {
                e.printStackTrace();
            }
            return false;
        }
    }
}