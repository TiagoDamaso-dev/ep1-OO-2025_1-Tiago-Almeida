package sistema.controles.ModulosDoSistema.EntidadesSistema;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Estudante extends Pessoa {
    private String CursoDeEnsinoSuperior;
    private List<Materia> disciplinasMatriculadas;
    private boolean TrancouSemestre;

    public Estudante(String nome, String matricula, String CursoDeEnsinoSuperior) {
        super(nome, matricula);
        this.CursoDeEnsinoSuperior = CursoDeEnsinoSuperior;
        this.disciplinasMatriculadas = new ArrayList<>();
        this.TrancouSemestre = false;
    }

    public String getCursoDeEnsinoSuperior() {
        return CursoDeEnsinoSuperior;
    }

    public void setCursoDeEnsinoSuperior(String cursoDeEnsinoSuperior) {
        if (cursoDeEnsinoSuperior != null && !cursoDeEnsinoSuperior.trim().isEmpty()) {
            this.CursoDeEnsinoSuperior = cursoDeEnsinoSuperior.trim();
        }
    }

    public List<Materia> getDisciplinasMatriculadas() {
        return new ArrayList<>(disciplinasMatriculadas);
    }

    public boolean isTrancouSemestre() {
        return TrancouSemestre;
    }

    public void trancarSemestre() {
        this.TrancouSemestre = true;
        this.disciplinasMatriculadas.clear();
    }

    public void destrancarSemestre() {
        this.TrancouSemestre = false;
    }

    public abstract boolean podeMatricular(Materia materia);

    public boolean matricular(Materia materia) {
        if (TrancouSemestre || materia == null) {
            return false;
        }

        if (!podeMatricular(materia) || disciplinasMatriculadas.contains(materia)) {
            return false;
        }

        return disciplinasMatriculadas.add(materia);
    }

    public boolean trancarDisciplina(Materia materia) {
        if (materia == null) {
            return false;
        }
        return disciplinasMatriculadas.remove(materia);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Estudante estudante = (Estudante) obj;
        return getMatricula().equals(estudante.getMatricula());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMatricula());
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%s)", getNome(), CursoDeEnsinoSuperior, getMatricula());
    }
}