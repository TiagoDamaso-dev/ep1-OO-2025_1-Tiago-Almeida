# Sistema Acadêmico - FCTE

## Descrição do Projeto

Desenvolvimento de um sistema acadêmico para gerenciar alunos, disciplinas, professores, turmas, avaliações e frequência, utilizando os conceitos de orientação a objetos (herança, polimorfismo e encapsulamento) e persistência de dados em arquivos.

## Dados do Aluno

- **Nome completo:** Tiago Almeida Damaso
- **Matrícula:** 241040664
- **Curso:** Engenharia Automotiva
- **Turma:** 06

---

## Instruções para Compilação e Execução

### Pré-requisitos
- Java Development Kit (JDK) 11 ou superior
- Git (opcional, apenas para controle de versão)
O projeto pode ser compilado usando o comando `javac`. Navegue até o diretório raiz do projeto e execute:
javac -d out/production/SistemaAcademicoFCTE -cp src/main/java src/main/java/sistema/controles/ModulosDoSistema/main/Main.java

2. **Execução:**  
   # Navegue até a pasta de saída
   cd out/production/SistemaAcademicoFCTE

# Execute o programa
java sistema.controles.ModulosDoSistema.main.Main


3. **Estrutura de Pastas:**  
src/main/java/ - Código-fonte Java
sistema/controles/ModulosDoSistema/ - Estrutura principal do pacote
EntidadesSistema/ - Classes de domínio (Aluno, Disciplina, etc.)
Funcionalidades/ - Lógica de negócios
main/ - Classe principal (Main.java)
out/ - Arquivos compilados (.class)
Arquivos de dados:
alunos.dat - Dados dos alunos
disciplinas.dat - Dados das disciplinas
avaliacoes.dat - Dados das avaliações

3. **Versão do JAVA utilizada:**  
   Java 11 ou superior (compatível com Java 11+)

---

## Vídeo de Demonstração

https://drive.google.com/file/d/1CJX-pKrsGAl_n_mbhqk1tMqXWu5ICeT8/view?usp=sharing

---

## Prints da Execução

1. Menu Principal:  
   ![image](https://github.com/user-attachments/assets/a7ce9b3f-aeb2-4e43-86f4-ae0d2721b772)


2. Cadastro de Aluno:  
   ![image](https://github.com/user-attachments/assets/6a3c5d50-c24a-462a-a25c-54bfe7ac3a3e)


3. Relatório de Frequência/Notas:  
  ![image](https://github.com/user-attachments/assets/62e528e7-85ec-4cc2-96ec-567ddbd1a52f)


---

## Principais Funcionalidades Implementadas

- [*] Cadastro, listagem, matrícula e trancamento de alunos (Normais e Especiais)
- [*] Cadastro de disciplinas e criação de turmas (presenciais e remotas)
- [*] Matrícula de alunos em turmas, respeitando vagas e pré-requisitos
- [*] Lançamento de notas e controle de presença
- [*] Cálculo de média final e verificação de aprovação/reprovação
- [ ] Relatórios de desempenho acadêmico por aluno, turma e disciplina
- [*] Persistência de dados em arquivos (.txt ou .csv)
- [ ] Tratamento de duplicidade de matrículas
- [*] Uso de herança, polimorfismo e encapsulamento

---

## Observações (Extras ou Dificuldades)

- A parte de tratamento de arquivos foi a mais complicada pela quantidade de bugs gerados

---


