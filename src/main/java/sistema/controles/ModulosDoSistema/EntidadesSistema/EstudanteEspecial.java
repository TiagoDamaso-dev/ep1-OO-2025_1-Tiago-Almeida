package sistema.controles.ModulosDoSistema.EntidadesSistema;

public class EstudanteEspecial extends Estudante {
    private static final int MAXIMO_DISCIPLINAS = 2;

    public EstudanteEspecial(String nome, String matricula, String cursoGraduacao) {
        super(nome, matricula, cursoGraduacao);
    }

    @Override
    public boolean podeMatricular(Materia materia) {

        return getDisciplinasMatriculadas().size() < MAXIMO_DISCIPLINAS;
    }

    @Override
    public String toString() {
        return String.format("Estudante Especial: %s (%s) - %s",
                getNome(), getMatricula(), getCursoDeEnsinoSuperior());
    }
}