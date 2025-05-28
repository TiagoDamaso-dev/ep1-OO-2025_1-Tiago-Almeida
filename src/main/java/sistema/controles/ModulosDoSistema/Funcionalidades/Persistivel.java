package sistema.controles.ModulosDoSistema.Funcionalidades;


public interface Persistivel {

    boolean Salvo(String arquivo);


    boolean carregamento(String arquivo);
} 