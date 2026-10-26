package br.com.urnapokemon;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

/**
 * Exemplo didatico SOMENTE DE LEITURA para ambiente confiavel.
 * NUNCA distribuir uma chave de conta de servico com o aplicativo Swing.
 */
public final class ConsultaFirestore {

    private ConsultaFirestore() { }

    public static void main(String[] args) throws Exception {
        String projeto = System.getenv("FIREBASE_PROJECT_ID");
        if (projeto == null || projeto.isBlank()) {
            throw new IllegalStateException("Configure FIREBASE_PROJECT_ID no ambiente.");
        }
        if (System.getenv("GOOGLE_APPLICATION_CREDENTIALS") == null) {
            throw new IllegalStateException("Configure GOOGLE_APPLICATION_CREDENTIALS para teste local.");
        }

        FirebaseOptions opcoes = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .setProjectId(projeto)
                .build();
        FirebaseApp app = FirebaseApp.initializeApp(opcoes);

        try {
            Firestore db = FirestoreClient.getFirestore(app);
            System.out.println("Projeto: " + projeto);

            // 1) Pokemon com ID de documento "001" (string, tres digitos).
            DocumentSnapshot pokemon = db.collection("pokemons").document("001").get().get();
            if (!pokemon.exists()) {
                System.out.println("Pokemon 001 nao encontrado.");
            } else {
                String partidoId = pokemon.getString("partidoId");
                System.out.println("Pokemon 001: " + pokemon.getString("nome"));
                System.out.println("Imagem: " + pokemon.getString("imagemUrl"));
                if (partidoId != null) {
                    DocumentSnapshot partido = db.collection("partidos").document(partidoId).get().get();
                    if (partido.exists()) {
                        System.out.println("Partido: " + partido.getString("nome")
                                + " (" + partido.getString("sigla") + ")");
                    } else {
                        System.out.println("Partido referenciado nao encontrado: " + partidoId);
                    }
                }
            }

            // 2) Consulta por numero eleitoral; filtra cargo/ativo localmente neste
            // exemplo pequeno para nao depender de um indice composto.
            QuerySnapshot resultados = db.collection("eleicoes").document("eleicao-001")
                    .collection("candidatos").whereEqualTo("numero", "12001").get().get();
            boolean encontrou = false;
            for (QueryDocumentSnapshot candidato : resultados.getDocuments()) {
                if ("VEREADOR".equals(candidato.getString("cargo"))
                        && Boolean.TRUE.equals(candidato.getBoolean("ativo"))) {
                    encontrou = true;
                    System.out.println("Candidato VEREADOR numero 12001: Pokemon "
                            + candidato.getString("pokemonId")
                            + ", documento " + candidato.getId());
                }
            }
            if (!encontrou) {
                System.out.println("Nenhum candidato ativo encontrado para VEREADOR 12001.");
            }

            // 3) Leitura do documento de apuracao; nao modifica contadores.
            DocumentSnapshot apuracao = db.collection("eleicoes").document("eleicao-001")
                    .collection("apuracao").document("VEREADOR").get().get();
            if (apuracao.exists()) {
                System.out.println("Apuracao VEREADOR -> validos: " + apuracao.getLong("validos")
                        + ", brancos: " + apuracao.getLong("brancos")
                        + ", nulos: " + apuracao.getLong("nulos")
                        + ", total: " + apuracao.getLong("total"));
            } else {
                System.out.println("Documento de apuracao VEREADOR ainda nao existe.");
            }
        } finally {
            app.delete();
        }
    }
}
