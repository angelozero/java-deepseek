![ollama](https://miro.medium.com/v2/resize:fit:541/1*cTtUxFuTePgc0RoKYNZVHA.png)

### O que é o Ollama Chat
- **Ollama**: Permite que os usuários executem modelos de linguagem localmente e se integra ao **Spring AI** através da API **OllamaChatModel**.
- **Compatibilidade com OpenAI**: O Ollama oferece um endpoint compatível com a API OpenAI, permitindo que o Spring AI se conecte a um servidor Ollama.

### Pré-requisitos
- É necessário ter acesso a uma instância do Ollama, que pode ser instalada localmente ou executada em contêineres (como Testcontainers) ou em Kubernetes.
- Os modelos podem ser baixados do repositório de modelos do Ollama ou de modelos disponíveis no Hugging Face.


### Hello World com Ollama e Spring AI
- **Instalando Ollama**
    - Acesse [Ollama](https://ollama.com/library/deepseek-r1) e baixe a versão **DeepSeek-R1**
    - Após a instalação verifique se Ollama esta de pé acessando o link `http://localhost:11434/`
    - Se tudo estiver certo, no seu navegador você deve ver a seguinte mensagem: `Ollama is running`

- **Criando uma aplicação Java**
    - Crie uma aplicação com [Spring Initializr](https://start.spring.io/)
        - Adicione as seguintes depências na criação
            - *Spring Web*
            - *Ollama*

    - Após o projeto criado, importe no [IntelliJ](https://www.jetbrains.com/pt-br/idea/download/)
    - Confira se no arquivo `pom.xml` se a seguinte dependência foi adicionada
    ```xml
    <dependency>
        <groupId>org.springframework.ai</groupId>
        <artifactId>spring-ai-ollama-spring-boot-starter</artifactId>
    </dependency>
    ```
    - Agora no seu arquivo `application.properties` adicione as seguintes propiedades
    ```shell
    spring.application.name=spring-ai-deepseek
    spring.ai.ollama.base-url=http://localhost:11434/
    spring.ai.ollama.chat.options.model=deepseek-r1:1.5b
    ```
    - O valor para `spring.ai.ollama.chat.options.model=` precisa ser respectivo ao utilizado no momento em que foi instalado Ollama
        - *Ex.: `ollama run deepseek-r1:1.5b`*

    - Primeiro criamos uma classe de configuração para prover a comunicação com o *client*
    ```java
    import org.springframework.ai.chat.client.ChatClient;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;

    @Configuration
    public class ChatClientConfig {

        @Bean
        public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
            return chatClientBuilder.build();
        }
    }
    ```

    - Criamos uma classe de serviço que chama os métodos de comunicação com a IA
    ```java
    import lombok.AllArgsConstructor;
    import org.springframework.ai.chat.client.ChatClient;
    import org.springframework.stereotype.Service;
    import reactor.core.publisher.Flux;

    @Service
    @AllArgsConstructor
    public class ChatAIService {

        private final ChatClient chatClient;

        public String askToDeepSeekAI(String question) {
            return chatClient.prompt(question).call().content();
        }

        public Flux<String> askToDeepSeekAiWithStream(String question) {
            return chatClient.prompt(question).stream().content();
        }
    }
    ```

    - Por fim criamos um *Controller* para testar as chamadas
    ```java
    @RestController
    @RequestMapping("/ai-api")
    @AllArgsConstructor
    public class ChatAIController {

        private final ChatAIService service;

        @PostMapping
        public ResponseEntity<String> askToAi(@RequestBody QuestionRequest questionRequest) {
            return ResponseEntity.ok(service.askToDeepSeekAI(questionRequest.question()));
        }

        @PostMapping("/stream")
        public ResponseEntity<Flux<String>> askToAiWithStream(@RequestBody QuestionRequest questionRequest) {
            return ResponseEntity.ok(service.askToDeepSeekAiWithStream(questionRequest.question()));
        }
    }
    ```

    - Para testar a API localmente acesse `http://localhost:8080/ai-api`
    - Body para envio
    ```json
    {
        "question": "Short answer: why is the color of the sky blue?"
    }
    ```

    - Resposta
    ```xml
    <think>
        Okay, so I'm trying to figure out why the sky is blue. I remember that when I look up at the sky outside, it's mostly clear blue. But why does that happen? I think it has something to do with how light interacts with the atmosphere.

        First off, I know that the sky is a layer of gas and plasma in our atmosphere. Light travels through this medium, which probably causes some scattering or absorption of the blue light. Maybe things like raindrops or smoke scatter the blue color out of view.

        Wait, there's something about Rayleigh scattering. Isn't that when shorter wavelength light scatters more than longer wavelengths? Blue has a shorter wavelength than red and yellow, right? So if blue light is scattered more, it leaves us with a darker sky at those parts where the blue light was absorbed or scattered back towards us.

        I also recall something about the greenhouse effect. I think gases in the atmosphere, especially things like carbon dioxide and water vapor, absorb and reradiate blue light. This causes longer wavelengths, like green and brown, to heat the Earth more than they would if there were no greenhouse gases. So we get warmer temperatures at higher latitudes because their atmospheric gases absorb and reemitting those blue lights.

        But wait, isn't blue also part of the UV? No, I think UV light is much shorter wavelength, which is why it's absorbed by things like ozone in Earth's atmosphere but doesn't contribute as much to the sky. Also, the Earth's atmosphere absorbs a lot of visible and near-infrared radiation, so we don't see all the colors. This filtering out would leave blue as the dominant color.

        I've also heard about something called the cosmic ray effect, where high-energy particles interact with the atmosphere, causing absorption or ionization that affects the scattering of light. This might contribute to some blue hues in the sky, though I'm not entirely sure how much it contributes compared to Rayleigh scattering.

        So putting this together: Rayleigh scattering causes blue light to scatter away from our line of sight, leaving us with a darker sky at those areas. The greenhouse effect adds another layer by absorbing and reradiating blue light, making the higher latitudes warmer than they would otherwise be. And both Rayleigh scattering and UV absorption contribute to the filtering that makes blue the dominant color in the sky.

        I think I've covered most of the factors involved, but I'm not 100% sure if I missed anything. Maybe other atmospheric processes like ozone layer play a role, but the primary reasons are Rayleigh scattering and the greenhouse effect.
    </think>
    ```
    ```shell
    The sky appears blue due to several atmospheric effects:

    1. **Rayleigh Scattering**: Blue light has a shorter wavelength than red or yellow light. According to Rayleigh's law, this causes more of the blue light to scatter away from our line of sight, leaving us with a darker sky at those areas.

    2. **Greenhouse Effect**: The atmosphere absorbs and reradiates blue light, especially during the day, causing longer wavelengths (green and brown) to heat Earth more than usual, leading to higher temperatures in polar regions.

    3. **UV Absorption**: The atmosphere absorbs UV radiation and some visible light, contributing to filtering that makes blue dominant.

    4. **Cosmic Ray Interactions**: High-energy particles interact with the atmosphere, potentially causing absorption or ionization, adding another layer of filtering.

    These factors collectively contribute to the blue color we observe in the sky.
    ```