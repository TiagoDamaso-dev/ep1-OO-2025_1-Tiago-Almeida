package sistema.controles.ModulosDoSistema.Funcionalidades;

import sistema.controles.ModulosDoSistema.EntidadesSistema.FormasDeAvaliacao;
import sistema.controles.ModulosDoSistema.EntidadesSistema.Materia;
import sistema.controles.ModulosDoSistema.EntidadesSistema.ProfessorFCTE;
import sistema.controles.ModulosDoSistema.EntidadesSistema.Turma;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GerenciamentoDeDisciplinas implements Persistivel {
    private Map<String, Materia> disciplinas;
    private Map<String, Turma> turmas;
    private Map<String, ProfessorFCTE> professores;
    private String ultimoErro;

    public GerenciamentoDeDisciplinas() {
        this.disciplinas = new HashMap<>();
        this.turmas = new HashMap<>();
        this.professores = new HashMap<>();
    }

    public String getUltimoErro() {
        return ultimoErro;
    }


    public boolean cadastrarDisciplina(String nome, String codigo, int cargaHoraria) {
        if (disciplinas.containsKey(codigo)) {
            return false;
        }

        Materia materia = new Materia(nome, codigo, cargaHoraria);
        disciplinas.put(codigo, materia);
        return true;
    }


    public boolean adicionarPreRequisito(String codigoDisciplina, String codigoPreRequisito) {
        Materia materia = disciplinas.get(codigoDisciplina);
        Materia preRequisito = disciplinas.get(codigoPreRequisito);

        if (materia == null || preRequisito == null) {
            return false;
        }

        materia.adicionarPreRequisito(preRequisito);
        return true;
    }


    public boolean criarTurma(String codigoTurma, String codigoDisciplina, ProfessorFCTE professorFCTE,
                              String semestre, FormasDeAvaliacao formasDeAvaliacao, boolean presencial,
                              String horario, int capacidadeMaxima) {
        System.out.println("\nTentando criar turma:");
        System.out.println("- Código da turma: " + codigoTurma);
        System.out.println("- Código da materia: " + codigoDisciplina);
        System.out.println("- ProfessorFCTE: " + professorFCTE.getNome() + " (" + professorFCTE.getMatricula() + ")");
        System.out.println("- Horário: " + horario);

        if (turmas.containsKey(codigoTurma)) {
            ultimoErro = "Já existe uma turma com o código " + codigoTurma;
            System.out.println("ERRO: " + ultimoErro);
            return false;
        }

        Materia materia = disciplinas.get(codigoDisciplina);
        if (materia == null) {
            ultimoErro = "Materia não encontrada: " + codigoDisciplina;
            System.out.println("ERRO: " + ultimoErro);
            return false;
        }

        // Verifica se o professorFCTE já tem uma turma no mesmo horário
        for (Turma turmaExistente : professorFCTE.getTurmasMinistradas()) {
            if (turmaExistente.getHorario().equals(horario)) {
                ultimoErro = "ProfessorFCTE já tem uma turma no horário " + horario;
                System.out.println("ERRO: " + ultimoErro);
                return false;
            }
        }

        Turma turma = new Turma(codigoTurma, materia, professorFCTE, semestre,
                formasDeAvaliacao, presencial, horario, capacidadeMaxima);
        
        turmas.put(codigoTurma, turma);
        materia.adicionarTurma(turma);
        professorFCTE.adicionarTurma(turma);
        
        System.out.println("Turma criada com sucesso!");
        return true;
    }


    public boolean definirSala(String codigoTurma, String sala) {
        Turma turma = turmas.get(codigoTurma);
        if (turma == null || !turma.isPresencial()) {
            return false;
        }

        turma.setSala(sala);
        return true;
    }


    public List<Turma> listarTurmas() {
        return new ArrayList<>(turmas.values());
    }


    public List<Materia> listarDisciplinas() {
        return new ArrayList<>(disciplinas.values());
    }


    public Materia ProcuraDisciplinaSistema(String codigo) {
        return disciplinas.get(codigo);
    }


    public Turma buscarTurma(String codigo) {
        return turmas.get(codigo);
    }


    public boolean cadastrarProfessor(String nome, String matricula, String departamento) {
        if (professores.containsKey(matricula)) {
            return false;
        }

        ProfessorFCTE professorFCTE = new ProfessorFCTE(nome, matricula, departamento);
        professores.put(matricula, professorFCTE);
        return true;
    }


    public ProfessorFCTE ProcuraProfessorSistema(String matricula) {
        return professores.get(matricula);
    }


    public List<ProfessorFCTE> listarProfessores() {
        return new ArrayList<>(professores.values());
    }

    @Override
    public boolean Salvo(String arquivo) {
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(arquivo), "UTF-8"))) {
            // Salva os professores
            writer.println("### PROFESSORES ###");
            for (ProfessorFCTE professorFCTE : professores.values()) {
                // Formato: nome;matricula;departamento
                String nome = professorFCTE.getNome();
                String matricula = professorFCTE.getMatricula();
                String departamento = professorFCTE.getDepartamento();
                
                // Garante que nenhum campo esteja vazio
                if (departamento == null || departamento.trim().isEmpty()) {
                    departamento = "Não informado";
                }
                
                writer.printf("PROF;%s;%s;%s%n",
                    nome, matricula, departamento);
            }


            writer.println("### DISCIPLINAS ###");
            for (Materia materia : disciplinas.values()) {

                String nome = materia.getNomeMateria();
                String codigo = materia.getCodigoMateria();
                int cargaHoraria = materia.getCargaHoraria();
                
                StringBuilder preReqs = new StringBuilder();
                for (Materia preReq : materia.getPreRequisitos()) {
                    if (preReqs.length() > 0) preReqs.append(",");
                    preReqs.append(preReq.getCodigoMateria());
                }
                
                writer.printf("DISC;%s;%s;%d;%s%n",
                    nome, codigo, cargaHoraria, preReqs.toString());
            }


            writer.println("### TURMAS ###");
            for (Turma turma : turmas.values()) {


                String codigo = turma.getCodigo();
                String codigoDisciplina = turma.getDisciplina().getCodigoMateria();
                String matriculaProfessor = turma.getProfessor().getMatricula();
                String semestre = turma.getSemestre();
                String formaAvaliacao = turma.getFormaAvaliacao().toString();
                boolean presencial = turma.isPresencial();
                String horario = turma.getHorario();
                int capacidadeMaxima = turma.getCapacidadeMaxima();
                String sala = turma.getSala() != null ? turma.getSala() : "";
                
                writer.printf("TURM;%s;%s;%s;%s;%s;%b;%s;%d;%s%n",
                    codigo, codigoDisciplina, matriculaProfessor,
                    semestre, formaAvaliacao, presencial,
                    horario, capacidadeMaxima, sala);
            }

            writer.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            ultimoErro = "Erro ao salvar: " + e.getMessage();
            return false;
        }
    }

    @Override
    public boolean carregamento(String arquivo) {
        disciplinas.clear();
        turmas.clear();
        professores.clear();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(arquivo), "UTF-8"))) {
            String linha;
            
            while ((linha = reader.readLine()) != null) {
                if (linha.startsWith("###")) {
                    continue;
                }

                String[] partes = linha.split(";");
                if (partes.length < 2) continue;

                switch (partes[0]) {
                    case "PROF" -> {
                        if (partes.length == 4) {
                            String nome = partes[1];
                            String matricula = partes[2];
                            String departamento = partes[3];
                            cadastrarProfessor(nome, matricula, departamento);
                        }
                    }
                    case "DISC" -> {
                        if (partes.length == 5) {
                            String nome = partes[1];
                            String codigo = partes[2];
                            int cargaHoraria = Integer.parseInt(partes[3]);
                            cadastrarDisciplina(nome, codigo, cargaHoraria);

                            // Adiciona pré-requisitos
                            if (!partes[4].isEmpty()) {
                                String[] preReqs = partes[4].split(",");
                                for (String preReq : preReqs) {
                                    adicionarPreRequisito(codigo, preReq);
                                }
                            }
                        }
                    }
                    case "TURM" -> {
                        if (partes.length == 10) {
                            String codigo = partes[1];
                            String codigoDisciplina = partes[2];
                            String matriculaProfessor = partes[3];
                            String semestre = partes[4];
                            FormasDeAvaliacao formasDeAvaliacao = FormasDeAvaliacao.valueOf(partes[5]);
                            boolean presencial = Boolean.parseBoolean(partes[6]);
                            String horario = partes[7];
                            int capacidadeMaxima = Integer.parseInt(partes[8]);
                            String sala = partes[9];

                            ProfessorFCTE professorFCTE = ProcuraProfessorSistema(matriculaProfessor);
                            if (professorFCTE != null) {
                                if (criarTurma(codigo, codigoDisciplina, professorFCTE, semestre,
                                        formasDeAvaliacao, presencial, horario, capacidadeMaxima)) {
                                    
                                    if (presencial && !sala.isEmpty()) {
                                        definirSala(codigo, sala);
                                    }
                                } else {
                                    System.err.println("Erro ao criar turma: " + ultimoErro);
                                }
                            } else {
                                System.err.println("ProfessorFCTE não encontrado: " + matriculaProfessor);
                            }
                        }
                    }
                }
            }
            return true;
        } catch (IOException e) {
            // Se o arquivo não existir na primeira execução, não é erro
            if (!(e instanceof FileNotFoundException)) {
                e.printStackTrace();
                ultimoErro = "Erro ao carregar: " + e.getMessage();
            }
            return false;
        }
    }
} 