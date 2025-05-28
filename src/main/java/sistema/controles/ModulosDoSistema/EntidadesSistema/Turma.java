package sistema.controles.ModulosDoSistema.EntidadesSistema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Turma implements Serializable {
    private static final long serialVersionUID = 1L;
    private String codigo;
    private Materia materia;
    private ProfessorFCTE professorFCTE;
    private String semestre;
    private FormasDeAvaliacao formasDeAvaliacao;
    private boolean presencial;
    private String sala;
    private String horario;
    private int capacidadeMaxima;
    private List<Estudante> alunosMatriculados;
    private Map<String, NotasAluno> notasAlunos;

    public Turma(String codigo, Materia materia, ProfessorFCTE professorFCTE, String semestre,
                 FormasDeAvaliacao formasDeAvaliacao, boolean presencial, String horario, int capacidadeMaxima) {
        this.codigo = codigo;
        this.materia = materia;
        this.professorFCTE = professorFCTE;
        this.semestre = semestre;
        this.formasDeAvaliacao = formasDeAvaliacao;
        this.presencial = presencial;
        this.horario = horario;
        this.capacidadeMaxima = capacidadeMaxima;
        this.alunosMatriculados = new ArrayList<>();
        this.notasAlunos = new HashMap<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public Materia getDisciplina() {
        return materia;
    }

    public ProfessorFCTE getProfessor() {
        return professorFCTE;
    }

    public String getSemestre() {
        return semestre;
    }

    public FormasDeAvaliacao getFormaAvaliacao() {
        return formasDeAvaliacao;
    }

    public boolean isPresencial() {
        return presencial;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        // Só permite definir sala se a turma for presencial
        if (presencial) {
            this.sala = sala;
        }
    }

    public String getHorario() {
        return horario;
    }

    public int getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public List<Estudante> getAlunosMatriculados() {
        return new ArrayList<>(alunosMatriculados);
    }

    public boolean matricularAluno(Estudante estudante) {
        if (alunosMatriculados.size() >= capacidadeMaxima) {
            return false;
        }

        if (!alunosMatriculados.contains(estudante)) {
            alunosMatriculados.add(estudante);
            NotasAluno notas = new NotasAluno();
            notas.setFormaAvaliacao(formasDeAvaliacao);
            notasAlunos.put(estudante.getMatricula(), notas);
            return true;
        }
        return false;
    }

    public void desmatricularAluno(Estudante estudante) {
        alunosMatriculados.remove(estudante);
        notasAlunos.remove(estudante.getMatricula());
    }

    public void lancarNotas(Estudante estudante, double p1, double p2, double p3, double listas, double seminario) {
        if (estudante instanceof EstudanteEspecial) {
            return; // Alunos especiais não recebem notas
        }

        NotasAluno notas = notasAlunos.get(estudante.getMatricula());
        if (notas != null) {
            notas.setNotas(p1, p2, p3, listas, seminario);
        }
    }

    public void lancarPresenca(Estudante estudante, int totalAulas, int presencas) {
        NotasAluno notas = notasAlunos.get(estudante.getMatricula());
        if (notas != null) {
            notas.setFrequencia(totalAulas, presencas);
        }
    }

    public NotasAluno getNotasAluno(Estudante estudante) {
        return notasAlunos.get(estudante.getMatricula());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Turma turma)) return false;
        return codigo.equals(turma.codigo);
    }

    @Override
    public int hashCode() {
        return codigo.hashCode();
    }

    public static class NotasAluno implements Serializable {
        private static final long serialVersionUID = 1L;
        private double prova1;
        private double prova2;
        private double prova3;
        private double listas;
        private double seminario;
        private int aulasTotais;
        private int presencas;
        private FormasDeAvaliacao formasDeAvaliacao;

        public NotasAluno() {
            this.prova1 = 0;
            this.prova2 = 0;
            this.prova3 = 0;
            this.listas = 0;
            this.seminario = 0;
            this.aulasTotais = 0;
            this.presencas = 0;
        }

        public void setNotas(double p1, double p2, double p3, double listas, double seminario) {
            this.prova1 = p1;
            this.prova2 = p2;
            this.prova3 = p3;
            this.listas = listas;
            this.seminario = seminario;
        }

        public void setFrequencia(int totalAulas, int presencas) {
            this.aulasTotais = totalAulas;
            this.presencas = presencas;
        }

        public void setFormaAvaliacao(FormasDeAvaliacao formasDeAvaliacao) {
            this.formasDeAvaliacao = formasDeAvaliacao;
        }

        public double getMediaFinal() {
            if (formasDeAvaliacao == null) {
                return 0;
            }
            return formasDeAvaliacao.mediaCalculada(prova1, prova2, prova3, listas, seminario);
        }

        public double getFrequencia() {
            return aulasTotais > 0 ? (double) presencas / aulasTotais * 100 : 0;
        }

        public boolean isAprovado() {
            return getMediaFinal() >= 5.0 && getFrequencia() >= 75.0;
        }

        public String getSituacao() {
            if (isAprovado()) {
                return "Aluno Aprovado!!";
            } else if (getFrequencia() < 75.0) {
                return "Reprovado por Falta";
            } else {
                return "Reprovado por Nota";
            }
        }

        @Override
        public String toString() {
            return String.format("P1: %.1f, P2: %.1f, P3: %.1f, Listas: %.1f, Seminário: %.1f\n" +
                            "Média Final: %.1f, Frequência: %.1f%%, Situação: %s",
                    prova1, prova2, prova3, listas, seminario, getMediaFinal(), getFrequencia(), getSituacao());
        }

        public double getProva1() { return prova1; }
        public double getProva2() { return prova2; }
        public double getProva3() { return prova3; }
        public double getListas() { return listas; }
        public double getSeminario() { return seminario; }
        public int getAulasTotais() { return aulasTotais; }
        public int getPresencas() { return presencas; }
    }
} 