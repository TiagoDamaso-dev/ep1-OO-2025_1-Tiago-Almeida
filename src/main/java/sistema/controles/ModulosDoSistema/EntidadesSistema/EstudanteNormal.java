package sistema.controles.ModulosDoSistema.EntidadesSistema;


public class EstudanteNormal extends Estudante {
    
    public EstudanteNormal(String nome, String matricula, String cursoGraduacao) {
        super(nome, matricula, cursoGraduacao);
    }

    @Override
    public boolean podeMatricular(Materia materia) {
        // Verifica se o aluno possui todos os pré-requisitos
        return materia.getPreRequisitos().stream()
                .allMatch(preReq -> getDisciplinasMatriculadas().contains(preReq));
    }

    @Override
    public String toString() {
        return String.format("Estudante Normal: %s (%s) - %s",
            getNome(), getMatricula(), getCursoDeEnsinoSuperior());
    }
} 