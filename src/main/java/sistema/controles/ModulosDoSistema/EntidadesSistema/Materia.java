package sistema.controles.ModulosDoSistema.EntidadesSistema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Materia implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nomeMateria;
    private String codigoMateria;
    private int cargaHoraria;
    private List<Materia> preRequisitos;
    private List<Turma> turmas;

    public Materia(String nomeMateria, String codigoMateria, int cargaHoraria) {
        this.nomeMateria = nomeMateria;
        this.codigoMateria = codigoMateria;
        this.cargaHoraria = cargaHoraria;
        this.preRequisitos = new ArrayList<>();
        this.turmas = new ArrayList<>();
    }

    public String getNomeMateria() {
        return nomeMateria;
    }

    public void setNomeMateria(String nomeMateria) {
        this.nomeMateria = nomeMateria;
    }

    public String getCodigoMateria() {
        return codigoMateria;
    }

    public void setCodigoMateria(String codigoMateria) {
        this.codigoMateria = codigoMateria;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public List<Materia> getPreRequisitos() {
        return new ArrayList<>(preRequisitos);
    }

    public void adicionarPreRequisito(Materia materia) {
        if (!preRequisitos.contains(materia)) {
            preRequisitos.add(materia);
        }
    }

    public void removerPreRequisito(Materia materia) {
        preRequisitos.remove(materia);
    }

    public List<Turma> getTurmas() {
        return new ArrayList<>(turmas);
    }

    public void adicionarTurma(Turma turma) {
        if (!turmas.contains(turma)) {
            turmas.add(turma);
        }
    }

    public void removerTurma(Turma turma) {
        turmas.remove(turma);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Materia that)) return false;
        return codigoMateria.equals(that.codigoMateria);
    }

    @Override
    public int hashCode() {
        return codigoMateria.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - %dh", nomeMateria, codigoMateria, cargaHoraria);
    }
} 