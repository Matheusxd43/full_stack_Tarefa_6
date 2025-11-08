package br.com.tarefas.api.config;

import br.com.tarefas.api.model.Tarefa;
import br.com.tarefas.api.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final TarefaRepository tarefaRepository;

    @Autowired
    public DataInitializer(TarefaRepository tarefaRepository) {
        this.tarefaRepository = tarefaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        tarefaRepository.deleteAll();

        Tarefa t1 = new Tarefa();
        t1.setTitulo("Configurar o backend Spring Boot com MVC");
        t1.setConcluida(true);

        Tarefa t2 = new Tarefa();
        t2.setTitulo("Criar a entidade Tarefa e o Repository");
        t2.setConcluida(true);

        Tarefa t3 = new Tarefa();
        t3.setTitulo("Desenvolver a camada de Serviço e o Controller");
        t3.setConcluida(false);

        Tarefa t4 = new Tarefa();
        t4.setTitulo("Estudar e configurar o CORS para o frontend");
        t4.setConcluida(false);

        Tarefa t5 = new Tarefa();
        t5.setTitulo("Inicializar o projeto Vue.js com a CLI");
        t5.setConcluida(false);

        Tarefa t6 = new Tarefa();
        t6.setTitulo("Criar um serviço no frontend para consumir a API");
        t6.setConcluida(false);

        Tarefa t7 = new Tarefa();
        t7.setTitulo("Desenvolver o componente principal para listar e adicionar tarefas");
        t7.setConcluida(false);

        tarefaRepository.saveAll(Arrays.asList(t1, t2, t3, t4, t5, t6, t7));

        System.out.println("✅ Base de dados inicializada com 7 tarefas.");
    }
}