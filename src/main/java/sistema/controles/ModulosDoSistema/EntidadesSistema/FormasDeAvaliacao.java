package sistema.controles.ModulosDoSistema.EntidadesSistema;


public enum FormasDeAvaliacao {
    MEDIA_SIMPLES {
        @Override
        public double mediaCalculada(double p1, double p2, double p3, double listas, double seminario) {
            // Média aritmética simples - todas as notas têm peso igual (20%)
            return (p1 + p2 + p3 + listas + seminario) / 5.0;
        }
    },
    MEDIA_PONDERADA {
        @Override
        public double mediaCalculada(double p1, double p2, double p3, double listas, double seminario) {
            // P1: 12.5%, P2: 25%, P3: 37.5%, Listas: 12.5%, Seminário: 12.5%
            return (p1 + p2 * 2 + p3 * 3 + listas + seminario) / 8.0;
        }
    };


    public abstract double mediaCalculada(double p1, double p2, double p3, double listas, double seminario);
} 