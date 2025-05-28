package sistema.controles.ModulosDoSistema.EntidadesSistema;

import java.util.ArrayList;
import java.util.List;


public class ProfessorFCTE extends Pessoa {
    private String departamento;
    private List<Turma> turmasMinistradas;

    public ProfessorFCTE(String nome, String matricula, String departamento) {
        super(nome, matricula);
        this.departamento = departamento;
        this.turmasMinistradas = new ArrayList<>();
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public List<Turma> getTurmasMinistradas() {
        return new ArrayList<>(turmasMinistradas);
    }

    public void adicionarTurma(Turma turma) {
        if (!turmasMinistradas.contains(turma)) {
            turmasMinistradas.add(turma);
        }
    }

    public void removerTurma(Turma turma) {
        turmasMinistradas.remove(turma);
    }

    @Override
    public String toString() {
        return String.format("Prof. %s (%s) - %s", getNome(), getMatricula(), departamento);
    }
} 