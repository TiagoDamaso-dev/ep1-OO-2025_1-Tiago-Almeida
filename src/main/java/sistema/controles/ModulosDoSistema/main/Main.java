package sistema.controles.ModulosDoSistema.main;


import sistema.controles.ModulosDoSistema.EntidadesSistema.*;
import sistema.controles.ModulosDoSistema.Funcionalidades.CadastroDeAlunosSistema;
import sistema.controles.ModulosDoSistema.Funcionalidades.GerenciamentoDeDisciplinas;
import sistema.controles.ModulosDoSistema.Funcionalidades.GerenciarAvaliacaoAlunos;

import java.io.*;
import java.util.*;

public class Main {
    // Constantes para nomes dos arquivos
    private static final String ARQ_ALUNOS = "alunos.dat";
    private static final String ARQ_DISC = "disciplinas.dat";
    private static final String ARQ_AVAL = "avaliacoes.dat";

    // Instâncias dos gerenciadores
    private final CadastroDeAlunosSistema alunoSys;
    private final GerenciamentoDeDisciplinas discSys;
    private final GerenciarAvaliacaoAlunos avaliacaoSys;
    private final Scanner entrada;

    public Main() {
        // Configuração do encoding para UTF-8
        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("sun.jnu.encoding", "UTF-8");

        this.alunoSys = new CadastroDeAlunosSistema();
        this.discSys = new GerenciamentoDeDisciplinas();
        this.avaliacaoSys = new GerenciarAvaliacaoAlunos();
        this.entrada = new Scanner(System.in, "UTF-8");

        this.avaliacaoSys.setDisciplinaController(discSys);

        // Garante que os arquivos existam
        criarArquivoSeNaoExiste(ARQ_ALUNOS);
        criarArquivoSeNaoExiste(ARQ_DISC);
        criarArquivoSeNaoExiste(ARQ_AVAL);

        // Carrega os dados existentes
        carregarDados();
    }

    private void criarArquivoSeNaoExiste(String nomeArquivo) {
        try {
            File arquivo = new File(nomeArquivo);
            if (!arquivo.exists()) {
                arquivo.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Erro ao criar arquivo: " + nomeArquivo);
            e.printStackTrace();
        }
    }

    private void carregarDados() {
        try {
            alunoSys.carregamento(ARQ_ALUNOS);
            discSys.carregamento(ARQ_DISC);
            avaliacaoSys.carregamento(ARQ_AVAL);
            System.out.println("✅ Dados carregados com sucesso!");
        } catch (Exception e) {
            System.err.println("⚠️ Erro ao carregar dados: " + e.getMessage());
            System.out.println("Iniciando com dados vazios...");
        }
    }

    private void salvarDados() {
        try {
            alunoSys.Salvo(ARQ_ALUNOS);
            discSys.Salvo(ARQ_DISC);
            avaliacaoSys.Salvo(ARQ_AVAL);
        } catch (Exception e) {
            System.err.println("⚠️ Erro ao salvar os dados: " + e.getMessage());
        }
    }

    public void iniciar() {
        boolean sair = false;
        while (!sair) {
            System.out.println("\n╔════════════════════════════════════════════╗");
            System.out.println("║       SISTEMA ACADÊMICO FCTE 2.0           ║");
            System.out.println("╠════════════════════════════════════════════╣");
            System.out.println("║ [A] Área do Estudante                      ║");
            System.out.println("║ [B] Gerenciar Disciplinas                  ║");
            System.out.println("║ [C] Sistema de Avaliações                  ║");
            System.out.println("║ [S] Sair                                   ║");
            System.out.println("╚════════════════════════════════════════════╝");
            System.out.print("➤ Escolha: ");

            String opcao = entrada.nextLine().toUpperCase();
            switch (opcao) {
                case "A" -> menuAluno();
                case "B" -> menuDisciplina();
                case "C" -> menuAvaliacao();
                case "S" -> {
                    System.out.println("\n✅ Salvando dados...");
                    salvarDados();
                    System.out.println("\n👋 Obrigado por usar o Sistema Acadêmico FCTE!");
                    sair = true;
                }
                default -> System.out.println("\n❌ Opção inválida!");
            }
        }
        entrada.close();
    }
    // ==================== MÉTODOS DO MENU DO ALUNO ====================
    private void menuAluno() {
        boolean sair = false;
        while (!sair) {
            System.out.println("\n╔════════════════════════════════════════════╗");
            System.out.println("║            ÁREA DO ALUNO                   ║");
            System.out.println("╠════════════════════════════════════════════╣");
            System.out.println("║ [A] Cadastrar Novo Estudante                  ║");
            System.out.println("║ [B] Editar Dados do Estudante                 ║");
            System.out.println("║ [C] Listar Todos os Alunos                ║");
            System.out.println("║ [D] Matricular em Disciplina              ║");
            System.out.println("║ [E] Trancar Disciplina                    ║");
            System.out.println("║ [F] Trancar Semestre                      ║");
            System.out.println("║ [V] Voltar ao Menu Principal              ║");
            System.out.println("╚════════════════════════════════════════════╝");
            System.out.print("➤ Escolha: ");

            String opcao = entrada.nextLine().toUpperCase();
            switch (opcao) {
                case "A" -> cadastrarAluno();
                case "B" -> editarAluno();
                case "C" -> listarAlunos();
                case "D" -> matricularEmDisciplina();
                case "E" -> trancarDisciplina();
                case "F" -> trancarSemestre();
                case "V" -> sair = true;
                default -> System.out.println("\n❌ Opção inválida! Tente novamente.");
            }
        }
    }

    private void cadastrarAluno() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║          CADASTRAR NOVO ALUNO              ║");
        System.out.println("╚════════════════════════════════════════════╝");

        System.out.print("Nome completo: ");
        String nome = entrada.nextLine();

        System.out.print("Matrícula: ");
        String matricula = entrada.nextLine();

        System.out.print("Curso: ");
        String curso = entrada.nextLine();

        System.out.print("É aluno especial? (S/N): ");
        boolean especial = entrada.nextLine().equalsIgnoreCase("S");

        if (alunoSys.cadastrarAluno(nome, matricula, curso, especial)) {
            System.out.println("\n✅ Estudante cadastrado com sucesso!");
            salvarDados();
        } else {
            System.out.println("\n❌ Erro: Matrícula já cadastrada!");
        }
    }

    private void editarAluno() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║           EDITAR DADOS DO ALUNO            ║");
        System.out.println("╚════════════════════════════════════════════╝");

        System.out.print("Matrícula do estudante: ");
        String matricula = entrada.nextLine();

        Estudante estudante = alunoSys.buscarAluno(matricula);
        if (estudante == null) {
            System.out.println("\n❌ Estudante não encontrado!");
            return;
        }

        System.out.println("\nDados atuais:");
        System.out.println("Nome: " + estudante.getNome());
        System.out.println("Matrícula: " + estudante.getMatricula());
        System.out.println("Curso: " + estudante.getCursoDeEnsinoSuperior());
        System.out.println("Tipo: " + (estudante instanceof EstudanteEspecial ? "Especial" : "Regular"));

        System.out.println("\nNovos dados (deixe em branco para manter o atual):");
        System.out.print("Novo nome: ");
        String novoNome = entrada.nextLine();

        System.out.print("Novo curso: ");
        String novoCurso = entrada.nextLine();

        // Se os campos estiverem em branco, mantém os valores atuais
        if (novoNome.isBlank()) {
            novoNome = estudante.getNome();
        }
        if (novoCurso.isBlank()) {
            novoCurso = estudante.getCursoDeEnsinoSuperior();
        }

        if (alunoSys.editarAluno(matricula, novoNome, novoCurso)) {
            System.out.println("\n✅ Dados atualizados com sucesso!");
            salvarDados();
        } else {
            System.out.println("\n❌ Erro ao atualizar os dados do estudante!");
        }
    }

    private void listarAlunos() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                       LISTA DE ALUNOS                              ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ MATRÍCULA   NOME                                   CURSO           ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");

        for (Estudante estudante : alunoSys.listarAlunos()) {
            System.out.printf("║ %-11s %-40s %-20s ║%n",
                    estudante.getMatricula(),
                    estudante.getNome(),
                    estudante.getCursoDeEnsinoSuperior());
        }

        System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
        System.out.print("\nPressione ENTER para continuar...");
        entrada.nextLine();
    }

    private void matricularEmDisciplina() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║        MATRÍCULA EM DISCIPLINA            ║");
        System.out.println("╚════════════════════════════════════════════╝");

        System.out.print("Matrícula do estudante: ");
        String matricula = entrada.nextLine();

        Estudante estudante = alunoSys.buscarAluno(matricula);
        if (estudante == null) {
            System.out.println("\n❌ Estudante não encontrado!");
            return;
        }

        System.out.println("\nDisciplinas disponíveis:");
        listarDisciplinas();

        System.out.print("\nCódigo da disciplina: ");
        String codigoDisciplina = entrada.nextLine();

        Materia disciplina = discSys.ProcuraDisciplinaSistema(codigoDisciplina);
        if (disciplina == null) {
            System.out.println("\n❌ Disciplina não encontrada!");
            return;
        }

        System.out.println("\nTurmas disponíveis para " + disciplina.getNomeMateria() + ":");
        listarTurmasPorDisciplina(codigoDisciplina);

        System.out.print("\nCódigo da turma: ");
        String codigoTurma = entrada.nextLine();

        Turma turma = discSys.buscarTurma(codigoTurma);
        if (turma == null || !turma.getDisciplina().getCodigoMateria().equals(codigoDisciplina)) {
            System.out.println("\n❌ Turma não encontrada ou inválida para esta disciplina!");
            return;
        }

        // Aqui está a correção - usando o método correto para matricular o estudante
        if (turma.matricularAluno(estudante)) {
            if (alunoSys.matricularEmDisciplina(estudante.getMatricula(), disciplina)) {
                System.out.println("\n✅ Matrícula realizada com sucesso!");
                salvarDados();
            } else {
                turma.desmatricularAluno(estudante); // Desfaz a matrícula na turma se falhar no sistema
                System.out.println("\n❌ Erro ao realizar matrícula no sistema!");
            }
        } else {
            System.out.println("\n❌ Não foi possível realizar a matrícula. Turma lotada ou estudante já matriculado.");
        }
    }

    private void trancarDisciplina() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║           TRANCADISCIPLINA                 ║");
        System.out.println("╚════════════════════════════════════════════╝");

        System.out.print("Matrícula do estudante: ");
        String matricula = entrada.nextLine();

        Estudante estudante = alunoSys.buscarAluno(matricula);
        if (estudante == null) {
            System.out.println("\n❌ Estudante não encontrado!");
            return;
        }

        System.out.println("\nDisciplinas matriculadas:");
        listarDisciplinasDoAluno(matricula);

        System.out.print("\nCódigo da disciplina para trancar: ");
        String codigoDisciplina = entrada.nextLine();

        // Busca a disciplina pelo código
        Materia disciplina = discSys.ProcuraDisciplinaSistema(codigoDisciplina);
        if (disciplina == null) {
            System.out.println("\n❌ Disciplina não encontrada!");
            return;
        }

        // Verifica se o estudante está matriculado na disciplina
        if (!estudante.getDisciplinasMatriculadas().contains(disciplina)) {
            System.out.println("\n❌ Estudante não está matriculado nesta disciplina!");
            return;
        }

        // Remove o estudante de todas as turmas da disciplina
        for (Turma turma : discSys.listarTurmas()) {
            if (turma.getDisciplina().equals(disciplina) &&
                    turma.getAlunosMatriculados().contains(estudante)) {
                turma.desmatricularAluno(estudante);
            }
        }

        // Remove a disciplina do estudante
        if (estudante.getDisciplinasMatriculadas().remove(disciplina)) {
            System.out.println("\n✅ Disciplina trancada com sucesso!");
            salvarDados();
        } else {
            System.out.println("\n❌ Não foi possível trancar a disciplina.");
        }
    }

    private void trancarSemestre() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║           TRANCAMENTO DE SEMESTRE          ║");
        System.out.println("╚════════════════════════════════════════════╝");

        System.out.print("Matrícula do aluno: ");
        String matricula = entrada.nextLine();

        if (alunoSys.trancarSemestre(matricula)) {
            // Remove o estudante de todas as turmas
            Estudante estudante = alunoSys.buscarAluno(matricula);
            for (Turma turma : discSys.listarTurmas()) {
                if (turma.getAlunosMatriculados().contains(estudante)) {
                    turma.desmatricularAluno(estudante);
                }
            }
            System.out.println("\n✅ Semestre trancado com sucesso!");
            salvarDados();
        } else {
            System.out.println("\n❌ Não foi possível trancar o semestre. Verifique os dados.");
        }
    }
    // ==================== MÉTODOS DO MENU DE DISCIPLINAS ====================
    private void menuDisciplina() {
        boolean sair = false;
        while (!sair) {
            System.out.println("\n╔════════════════════════════════════════════╗");
            System.out.println("║        GERENCIAR DISCIPLINAS               ║");
            System.out.println("╠════════════════════════════════════════════╣");
            System.out.println("║ [A] Cadastrar Nova Disciplina              ║");
            System.out.println("║ [B] Adicionar Pré-requisito                ║");
            System.out.println("║ [C] Criar Nova Turma                       ║");
            System.out.println("║ [D] Definir Sala para Turma                ║");
            System.out.println("║ [E] Listar Todas as Disciplinas            ║");
            System.out.println("║ [F] Listar Todas as Turmas                 ║");
            System.out.println("║ [G] Cadastrar ProfessorFCTE                    ║");
            System.out.println("║ [H] Listar Professores                     ║");
            System.out.println("║ [V] Voltar ao Menu Principal               ║");
            System.out.println("╚════════════════════════════════════════════╝");
            System.out.print("➤ Escolha: ");

            String opcao = entrada.nextLine().toUpperCase();
            switch (opcao) {
                case "A" -> cadastrarDisciplina();
                case "B" -> adicionarPreRequisito();
                case "C" -> criarTurma();
                case "D" -> definirSala();
                case "E" -> listarDisciplinas();
                case "F" -> listarTurmas();
                case "G" -> cadastrarProfessor();
                case "H" -> listarProfessores();
                case "V" -> sair = true;
                default -> System.out.println("\n❌ Opção inválida! Tente novamente.");
            }
        }
    }

    private void cadastrarDisciplina() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║        CADASTRAR NOVA DISCIPLINA           ║");
        System.out.println("╚════════════════════════════════════════════╝");

        System.out.print("Código da disciplina (ex: MAT101): ");
        String codigo = entrada.nextLine().toUpperCase();

        System.out.print("Nome da disciplina: ");
        String nome = entrada.nextLine();

        System.out.print("Carga horária (horas): ");
        int cargaHoraria = Integer.parseInt(entrada.nextLine());

        if (discSys.cadastrarDisciplina(nome, codigo, cargaHoraria)) {
            System.out.println("\n✅ Disciplina cadastrada com sucesso!");
            salvarDados();
        } else {
            System.out.println("\n❌ Erro: Código de disciplina já existe!");
        }
    }

    private void adicionarPreRequisito() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║        ADICIONAR PRÉ-REQUISITO             ║");
        System.out.println("╚════════════════════════════════════════════╝");

        System.out.print("Código da disciplina: ");
        String codigoDisciplina = entrada.nextLine().toUpperCase();

        System.out.print("Código do pré-requisito: ");
        String codigoPreRequisito = entrada.nextLine().toUpperCase();

        if (discSys.adicionarPreRequisito(codigoDisciplina, codigoPreRequisito)) {
            System.out.println("\n✅ Pré-requisito adicionado com sucesso!");
            salvarDados();
        } else {
            System.out.println("\n❌ Erro: Disciplina ou pré-requisito não encontrado!");
        }
    }

    private void criarTurma() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║            CRIAR NOVA TURMA                ║");
        System.out.println("╚════════════════════════════════════════════╝");

        System.out.print("Código da turma (ex: T01): ");
        String codigoTurma = entrada.nextLine().toUpperCase();

        System.out.println("\nDisciplinas disponíveis:");
        listarDisciplinas();

        System.out.print("\nCódigo da disciplina: ");
        String codigoDisciplina = entrada.nextLine().toUpperCase();

        System.out.println("\nProfessores disponíveis:");
        listarProfessores();

        System.out.print("\nMatrícula do professor: ");
        String matriculaProfessor = entrada.nextLine();

        System.out.print("Semestre (ex: 2024.1): ");
        String semestre = entrada.nextLine();

        System.out.print("Forma de avaliação (1-Média Simples, 2-Média Ponderada): ");
        FormasDeAvaliacao formasDeAvaliacao = entrada.nextLine().equals("1") ?
                FormasDeAvaliacao.MEDIA_SIMPLES : FormasDeAvaliacao.MEDIA_PONDERADA;

        System.out.print("É presencial? (S/N): ");
        boolean presencial = entrada.nextLine().equalsIgnoreCase("S");

        System.out.print("Horário (ex: SEG 14:00-15:40): ");
        String horario = entrada.nextLine();

        System.out.print("Capacidade máxima: ");
        int capacidadeMaxima = Integer.parseInt(entrada.nextLine());

        if (discSys.criarTurma(codigoTurma, codigoDisciplina,
                discSys.ProcuraProfessorSistema(matriculaProfessor), semestre,
                formasDeAvaliacao, presencial, horario, capacidadeMaxima)) {
            System.out.println("\n✅ Turma criada com sucesso!");
            salvarDados();
        } else {
            System.out.println("\n❌ Erro ao criar turma. Verifique os dados informados.");
        }
    }

    private void definirSala() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║          DEFINIR SALA PARA TURMA           ║");
        System.out.println("╚════════════════════════════════════════════╝");

        System.out.print("Código da turma: ");
        String codigoTurma = entrada.nextLine().toUpperCase();

        System.out.print("Número da sala: ");
        String sala = entrada.nextLine();

        if (discSys.definirSala(codigoTurma, sala)) {
            System.out.println("\n✅ Sala definida com sucesso!");
            salvarDados();
        } else {
            System.out.println("\n❌ Erro: Turma não encontrada ou não é presencial!");
        }
    }

    private void listarDisciplinas() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                      LISTA DE DISCIPLINAS                          ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ CÓDIGO   NOME                                   CARGA HORÁRIA      ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");

        for (Materia disciplina : discSys.listarDisciplinas()) {
            System.out.printf("║ %-8s %-40s %4d horas         ║%n",
                    disciplina.getCodigoMateria(),
                    disciplina.getNomeMateria(),
                    disciplina.getCargaHoraria());
        }

        System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
        System.out.print("\nPressione ENTER para continuar...");
        entrada.nextLine();
    }

    private void listarTurmas() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                 LISTA DE TURMAS                                                   ║");
        System.out.println("╠═══════════╦══════════════════════════════════════╦════════════════════════════╦══════════╦══════════╦══════════════╣");
        System.out.println("║ CÓDIGO    ║ DISCIPLINA                         ║ PROFESSOR                 ║ VAGAS    ║ HORÁRIO  ║ SALA         ║");
        System.out.println("╠═══════════╬══════════════════════════════════════╬════════════════════════════╬══════════╬══════════╬══════════════╣");

        for (Turma turma : discSys.listarTurmas()) {
            System.out.printf("║ %-9s ║ %-36s ║ %-26s ║ %2d/%2d    ║ %-8s ║ %-12s ║%n",
                    turma.getCodigo(),
                    turma.getDisciplina().getNomeMateria().length() > 35 ?
                            turma.getDisciplina().getNomeMateria().substring(0, 32) + "..." :
                            turma.getDisciplina().getNomeMateria(),
                    turma.getProfessor().getNome().length() > 25 ?
                            turma.getProfessor().getNome().substring(0, 22) + "..." :
                            turma.getProfessor().getNome(),
                    turma.getAlunosMatriculados().size(),
                    turma.getCapacidadeMaxima(),
                    turma.getHorario(),
                    turma.getSala() != null ? turma.getSala() : "Online");
        }

        System.out.println("╚═══════════╩══════════════════════════════════════╩════════════════════════════╩══════════╩══════════╩══════════════╝");
        System.out.print("\nPressione ENTER para continuar...");
        entrada.nextLine();
    }

    private void cadastrarProfessor() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║          CADASTRAR PROFESSOR               ║");
        System.out.println("╚════════════════════════════════════════════╝");

        System.out.print("Nome do professor: ");
        String nome = entrada.nextLine();

        System.out.print("Matrícula: ");
        String matricula = entrada.nextLine();

        System.out.print("Departamento: ");
        String departamento = entrada.nextLine();

        if (discSys.cadastrarProfessor(nome, matricula, departamento)) {
            System.out.println("\n✅ ProfessorFCTE cadastrado com sucesso!");
            salvarDados();
        } else {
            System.out.println("\n❌ Erro: Matrícula já cadastrada!");
        }
    }

    private void listarProfessores() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                      LISTA DE PROFESSORES                           ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ MATRÍCULA   NOME                                   DEPARTAMENTO     ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");

        for (ProfessorFCTE professorFCTE : discSys.listarProfessores()) {
            System.out.printf("║ %-11s %-40s %-20s ║%n",
                    professorFCTE.getMatricula(),
                    professorFCTE.getNome(),
                    professorFCTE.getDepartamento());
        }

        System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
        System.out.print("\nPressione ENTER para continuar...");
        entrada.nextLine();
    }

    // ==================== MÉTODOS AUXILIARES ====================
    private void listarTurmasPorDisciplina(String codigoDisciplina) {
        System.out.println("\n╔═══════════╦════════════════════════════╦══════════╦══════════╦══════════════╗");
        System.out.println("║ CÓDIGO    ║ PROFESSOR                 ║ VAGAS    ║ HORÁRIO  ║ SALA         ║");
        System.out.println("╠═══════════╬════════════════════════════╬══════════╬══════════╬══════════════╣");

        for (Turma turma : discSys.listarTurmas()) {
            if (turma.getDisciplina().getCodigoMateria().equals(codigoDisciplina)) {
                System.out.printf("║ %-9s ║ %-26s ║ %2d/%2d    ║ %-8s ║ %-12s ║%n",
                        turma.getCodigo(),
                        turma.getProfessor().getNome().length() > 25 ?
                                turma.getProfessor().getNome().substring(0, 22) + "..." :
                                turma.getProfessor().getNome(),
                        turma.getAlunosMatriculados().size(),
                        turma.getCapacidadeMaxima(),
                        turma.getHorario(),
                        turma.getSala() != null ? turma.getSala() : "Online");
            }
        }

        System.out.println("╚═══════════╩════════════════════════════╩══════════╩══════════╩══════════════╝");
    }

    private void listarDisciplinasDoAluno(String matricula) {
        Estudante estudante = alunoSys.buscarAluno(matricula);
        if (estudante == null) return;

        System.out.println("\n╔═══════════╦══════════════════════════════════════╦════════════════════════════╗");
        System.out.println("║ CÓDIGO    ║ DISCIPLINA                         ║ SITUAÇÃO                  ║");
        System.out.println("╠═══════════╬══════════════════════════════════════╬════════════════════════════╣");

        for (Materia disciplina : estudante.getDisciplinasMatriculadas()) {
            System.out.printf("║ %-9s ║ %-36s ║ %-26s ║%n",
                    disciplina.getCodigoMateria(),
                    disciplina.getNomeMateria().length() > 35 ?
                            disciplina.getNomeMateria().substring(0, 32) + "..." :
                            disciplina.getNomeMateria(),
                    "Matriculado");
        }

        System.out.println("╚═══════════╩══════════════════════════════════════╩════════════════════════════╝");
    }
    // ==================== MÉTODOS DO MENU DE AVALIAÇÕES ====================
    private void menuAvaliacao() {
        boolean sair = false;
        while (!sair) {
            System.out.println("\n╔════════════════════════════════════════════╗");
            System.out.println("║        SISTEMA DE AVALIAÇÕES               ║");
            System.out.println("╠════════════════════════════════════════════╣");
            System.out.println("║ [A] Lançar Notas                           ║");
            System.out.println("║ [B] Registrar Frequência                   ║");
            System.out.println("║ [C] Relatório por Turma                    ║");
            System.out.println("║ [D] Relatório por Disciplina               ║");
            System.out.println("║ [E] Relatório por ProfessorFCTE                ║");
            System.out.println("║ [F] Boletim do Estudante                       ║");
            System.out.println("║ [V] Voltar ao Menu Principal               ║");
            System.out.println("╚════════════════════════════════════════════╝");
            System.out.print("➤ Escolha: ");

            String opcao = entrada.nextLine().toUpperCase();
            switch (opcao) {
                case "A" -> lancarNotas();
                case "B" -> registrarFrequencia();
                case "C" -> gerarRelatorioTurma();
                case "D" -> gerarRelatorioDisciplina();
                case "E" -> gerarRelatorioProfessor();
                case "F" -> exibirBoletimAluno();
                case "V" -> sair = true;
                default -> System.out.println("\n❌ Opção inválida! Tente novamente.");
            }
        }
    }

    private void lancarNotas() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║             LANÇAR NOTAS                   ║");
        System.out.println("╚════════════════════════════════════════════╝");

        System.out.print("Código da turma: ");
        String codigoTurma = entrada.nextLine().toUpperCase();

        Turma turma = discSys.buscarTurma(codigoTurma);
        if (turma == null) {
            System.out.println("\n❌ Turma não encontrada!");
            return;
        }

        System.out.println("\nAlunos matriculados na turma " + codigoTurma + ":");
        listarAlunosTurma(turma);

        System.out.print("\nMatrícula do estudante: ");
        String matricula = entrada.nextLine();

        Estudante estudante = alunoSys.buscarAluno(matricula);
        if (estudante == null || !turma.getAlunosMatriculados().contains(estudante)) {
            System.out.println("\n❌ Estudante não encontrado nesta turma!");
            return;
        }

        if (estudante instanceof EstudanteEspecial) {
            System.out.println("\n⚠️ Alunos especiais não recebem notas!");
            return;
        }

        try {
            System.out.print("\nNota P1 (0-10): ");
            double p1 = Double.parseDouble(entrada.nextLine());

            System.out.print("Nota P2 (0-10): ");
            double p2 = Double.parseDouble(entrada.nextLine());

            System.out.print("Nota P3 (0-10): ");
            double p3 = Double.parseDouble(entrada.nextLine());

            System.out.print("Nota das Listas (0-10): ");
            double listas = Double.parseDouble(entrada.nextLine());

            System.out.print("Nota do Seminário (0-10): ");
            double seminario = Double.parseDouble(entrada.nextLine());

            if (avaliacaoSys.RegistrarNotasAlunos(codigoTurma, estudante, p1, p2, p3, listas, seminario)) {
                System.out.println("\n✅ Notas lançadas com sucesso!");
                salvarDados();
            } else {
                System.out.println("\n❌ Erro ao lançar as notas!");
            }
        } catch (NumberFormatException e) {
            System.out.println("\n❌ Valor inválido! Use números decimais separados por vírgula.");
        }
    }

    private void registrarFrequencia() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║          REGISTRAR FREQUÊNCIA               ║");
        System.out.println("╚════════════════════════════════════════════╝");

        System.out.print("Código da turma: ");
        String codigoTurma = entrada.nextLine().toUpperCase();

        Turma turma = discSys.buscarTurma(codigoTurma);
        if (turma == null) {
            System.out.println("\n❌ Turma não encontrada!");
            return;
        }

        System.out.print("Total de aulas ministradas: ");
        int totalAulas = Integer.parseInt(entrada.nextLine());

        System.out.println("\nRegistrando frequência para a turma " + codigoTurma);
        System.out.println("Para cada aluno, informe o número de faltas:");

        for (Estudante estudante : turma.getAlunosMatriculados()) {
            boolean entradaValida = false;
            while (!entradaValida) {
                try {
                    System.out.printf("\n%s - %s%n", estudante.getMatricula(), estudante.getNome());
                    System.out.print("Número de faltas: ");
                    int faltas = Integer.parseInt(entrada.nextLine());

                    if (faltas < 0 || faltas > totalAulas) {
                        System.out.println("❌ Número de faltas inválido! Deve estar entre 0 e " + totalAulas);
                        continue;
                    }

                    int presencas = totalAulas - faltas;
                    avaliacaoSys.RegistrarFreqAlunos(codigoTurma, estudante, totalAulas, presencas);
                    entradaValida = true;
                } catch (NumberFormatException e) {
                    System.out.println("❌ Por favor, digite um número inteiro válido.");
                }
            }
        }

        System.out.println("\n✅ Frequência registrada com sucesso para todos os alunos!");
        salvarDados();
    }

    private void gerarRelatorioTurma() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║          RELATÓRIO POR TURMA               ║");
        System.out.println("╚════════════════════════════════════════════╝");

        System.out.print("Código da turma: ");
        String codigoTurma = entrada.nextLine().toUpperCase();

        String relatorio = avaliacaoSys.RelatorioDesempenhoTurma(codigoTurma);
        System.out.println(relatorio);

        System.out.print("\nPressione ENTER para continuar...");
        entrada.nextLine();
    }

    private void gerarRelatorioDisciplina() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║        RELATÓRIO POR DISCIPLINA            ║");
        System.out.println("╚════════════════════════════════════════════╝");

        System.out.print("Código da disciplina: ");
        String codigoDisciplina = entrada.nextLine().toUpperCase();

        String relatorio = avaliacaoSys.criarRelatorioDasDisciplinas(codigoDisciplina, discSys);
        System.out.println(relatorio);

        System.out.print("\nPressione ENTER para continuar...");
        entrada.nextLine();
    }

    private void gerarRelatorioProfessor() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║         RELATÓRIO POR PROFESSOR             ║");
        System.out.println("╚════════════════════════════════════════════╝");

        System.out.print("Matrícula do professor: ");
        String matriculaProfessor = entrada.nextLine();

        String relatorio = avaliacaoSys.RelatorioProfessor(matriculaProfessor, discSys.listarTurmas());
        System.out.println(relatorio);

        System.out.print("\nPressione ENTER para continuar...");
        entrada.nextLine();
    }

    private void exibirBoletimAluno() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║             BOLETIM DO ALUNO                ║");
        System.out.println("╚════════════════════════════════════════════╝");

        System.out.print("Matrícula do aluno: ");
        String matricula = entrada.nextLine();

        System.out.print("Semestre (ex: 2024.1): ");
        String semestre = entrada.nextLine();

        String boletim = avaliacaoSys.gerarBoletimAluno(matricula, semestre, true, discSys.listarTurmas());
        System.out.println(boletim);

        System.out.print("\nPressione ENTER para continuar...");
        entrada.nextLine();
    }

    // ==================== MÉTODOS AUXILIARES ADICIONAIS ====================
    private void listarAlunosTurma(Turma turma) {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════╗");
        System.out.println("║ ALUNOS MATRICULADOS: " + turma.getDisciplina().getNomeMateria() +
                " - " + turma.getCodigo() + " " +
                String.format("%" + (35 - turma.getDisciplina().getNomeMateria().length() -
                        turma.getCodigo().length()) + "s", "") + "║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ MATRÍCULA   NOME                                   SITUAÇÃO         ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");

        for (Estudante estudante : turma.getAlunosMatriculados()) {
            System.out.printf("║ %-11s %-40s %-18s ║%n",
                    estudante.getMatricula(),
                    estudante.getNome(),
                    estudante instanceof EstudanteEspecial ? "Estudante Especial" : "Regular");
        }

        System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
    }

    // ==================== MÉTODO MAIN ====================
    public static void main(String[] args) {
        try {
            // Configura o encoding padrão para UTF-8
            System.setProperty("file.encoding", "UTF-8");
            System.setProperty("sun.jnu.encoding", "UTF-8");

            // Inicializa e executa o sistema
            Main sistema = new Main();
            System.out.println("\n🚀 Iniciando Sistema Acadêmico FCTE 2.0...");
            sistema.iniciar();
        } catch (Exception e) {
            System.err.println("\n❌ Ocorreu um erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}