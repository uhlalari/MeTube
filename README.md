MeTube - Aplicativo de Pesquisa e Organização de Vídeos
MeTube é um aplicativo desenvolvido em Kotlin seguindo os princípios do MVVM, Clean Architecture e boas práticas de desenvolvimento. Ele permite pesquisar vídeos do YouTube, favoritar, adicionar a listas de reprodução e receber notificações locais com o vídeo musical mais popular.

Funcionalidades
Pesquisa de vídeos: Tela inicial permite buscar vídeos no YouTube.
Listagem de vídeos: Resultados são exibidos em uma lista interativa.
Favoritar vídeos: Clique na estrela para adicionar/remover vídeos dos favoritos.
Adicionar à lista de reprodução: Clique no botão de lista para organizar vídeos.
Pressionar o vídeo: Abre a tela correspondente (favoritos ou listas de reprodução).
WebView para Termos de Uso: Ícone que abre uma página com os termos do app.
Notificações Push Locais: Dispara uma notificação com o vídeo de música mais popular no YouTube.
Arquitetura e Padrões Utilizados
Este projeto segue Clean Architecture e MVVM, garantindo modularidade, testabilidade e separação de responsabilidades.

Camadas utilizadas:
Presentation → Activity/Fragments/ViewModels (UI e lógica de exibição)
Domain → Casos de uso e repositórios (regras de negócio)
Data → Implementações de repositórios e acesso ao YouTube API
Principais princípios seguidos:
SOLID - Código modular e bem estruturado
Clean Code - Código limpo, nomeações significativas e fácil manutenção
Boas práticas do Android - Uso correto de ViewModels, LiveData e SharedPreferences
Tecnologias e Bibliotecas
Linguagem: Kotlin
Arquitetura: MVVM + Clean Architecture
Interface: XML
API do YouTube: Consumo de vídeos via YouTube Data API v3
Armazenamento Local: Shared Preferences
Gerenciamento de dependências: Gradle
Networking: Retrofit + Gson
Injeção de dependência: Koin
Navegação: Jetpack Navigation
Notificações Locais: Push Notification com Firebase ou AlarmManager
